package cn.rkang.wxgate.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.impl.InputElement;

/**
 * 通用表单验证器组件
 *
 * 与SimpleConstraint的区别：
 * SimpleConstraint可以不写java代码校验逻辑，方便的使用正则表达式，且支持前端校验——实现焦点离开立即提示错误信息
 * BaseFormServerConstraint只支持服务端validation——无法实现焦点离开立即提示错误信息
 */
public abstract class BaseFormServerConstraint implements Constraint {
    private static Logger	logger			= Logger.getLogger(BaseFormServerConstraint.class);

    List<Constraint>		constraintList	= new ArrayList<Constraint>();

    public BaseFormServerConstraint addConstraint(Constraint constraint) {
        constraintList.add(constraint);
        return this;
    }

    /**
     * 表单提交时在Executions.getCurrent()中进行标记，以便constraint进行validation，忽略非表单提交
     */
    public static final String	FORM_SUBMITING_FLAG	= "FORM_SUBMITING_FLAG";

    /**
     * 循环container中的所有输入组件，触发其constraint校验
     * 全部校验成功——没有任何违背constraint时，返回true
     */
    public static void triggerAllConstraintInForm(HtmlBasedComponent container) throws FormConstraintFailureException {
        setFlag4FormSubmitting();

        Iterator<Component> iterator = container.queryAll("*").iterator();
        while (iterator.hasNext()) {
            Component comp = iterator.next();
            if (comp instanceof InputElement) {
                InputElement ie = (InputElement) comp;
                Constraint constraint = ie.getConstraint();
                if (constraint != null) {
                    try {
                        ie.getText();//触发constraint
                    } catch (WrongValueException e) {
                        Clients.scrollIntoView(ie);
                        ie.setFocus(true);
                        //	让输入框获取焦点
                        logger.debug(e.getMessage());
                        Clients.wrongValue(comp, e.getMessage());
                        throw new FormConstraintFailureException();
                    }

                    try {
                        constraint.validate(ie, ie.getText());
                    } catch (WrongValueException e) {
                        Clients.scrollIntoView(ie);
                        ie.setFocus(true);
                        logger.debug(e.getMessage());
                        Clients.wrongValue(ie, e.getMessage());
                        throw new FormConstraintFailureException();
                    } catch (RuntimeException e) {
                        logger.error(e.getMessage(), e);
                        throw new FormConstraintFailureException();
                        //						Clients.scrollIntoView(ie);
                        //						ie.setFocus(true);
                        //						Clients.wrongValue(ie, e.getMessage());
                        //						throw new FormConstraintFailureException();
                    }
                }
            }
        }
        return;
    }

    public static void setFlag4FormSubmitting() {
        Executions.getCurrent().setAttribute(FORM_SUBMITING_FLAG, "asdf");
    }

    @Override
    public void validate(Component comp, Object value) throws WrongValueException {
        if (Executions.getCurrent().getAttribute(FORM_SUBMITING_FLAG) == null) {
            return;//只在表单提交时进行
        }
        validateOnFormSubmit(comp, value);
        for (Constraint constraint : constraintList) {
            constraint.validate(comp, value);
        }
    }

    abstract protected void validateOnFormSubmit(Component comp, Object value);

    /**
     * 手机号码校验
     * 1开头后面10为数字或01开头后面10位数字
     * @return
     */
    public static BaseFormServerConstraint getConst4Mobilephone() {
        return BaseFormServerConstraint//~~~~~~~~~~~~~关闭代码格式化@off
                .getConst4Regression(
                        "(^1\\d{10}$)|(^01\\d{10}$)|^$",Labels.getLabel("common.mobilephone.validate.info"));
    }

    /**
     * 根据正则表达式构造constraint
     * @return
     */
    public static BaseFormServerConstraint getConst4Regression(final String regression, final String errorMessage) {
        return new BaseFormServerConstraint() {
            @Override
            public void validateOnFormSubmit(Component comp, Object value) throws WrongValueException {
                String strValue = (value == null ? null : (String) value);
                if (StringUtils.isNotBlank(strValue)) {
                    Pattern regex = Pattern.compile(regression);
                    if (!regex.matcher(strValue).matches()) {
                        throw new WrongValueException(comp, errorMessage);
                    }
                }
            }
        };
    }

    /**
     * 根据maxlenght判断是否符合标准
     * @return BaseFormServerConstraint
     */
    public static BaseFormServerConstraint getConst4MaxLength(final int max) {
        return new BaseFormServerConstraint() {
            @Override
            public void validateOnFormSubmit(Component comp, Object value) throws WrongValueException {
                String strValue = (value == null ? null : (String) value);
                String errorMesg = null;
                if (StringUtils.isNotBlank(strValue)) {
                    if (strValue.length() > max) {
                        errorMesg = Labels.getLabel("sys.input.maxLength", new java.lang.Object[] { max });
                        throw new WrongValueException(comp, errorMesg);
                    }
                }
            }
        };
    }

    /**
     * 非空判断校验
     * @return BaseFormServerConstraint
     */
    public static BaseFormServerConstraint getConst4NoEmpty() {
        return new BaseFormServerConstraint() {
            @Override
            public void validateOnFormSubmit(Component comp, Object value) throws WrongValueException {
                String bandboxvalue = null;
                logger.debug("非空校验，value:" + value);
                bandboxvalue = value == null ? null : value.toString(); //即使是intbox、doublebox也不会出现强转String异常的情况
                if (StringUtils.isBlank(bandboxvalue)
                        || StringUtils.equals(Labels.getLabel("sys.pleaseSelect"), bandboxvalue)) {
                    throw new WrongValueException(comp, Labels.getLabel("sys.input.notEmpty"));
                }
            }
        };
    }
    

//	/**
//	 * 客户编码constraint：非空并且DB中存在
//	 * @param customerService
//	 * @return
//	 */
//	public static BaseFormServerConstraint getConstraint_CustCode(final CustomerService customerService) {
//		return new BaseFormServerConstraint() {
//			@Override
//			public void validateOnFormSubmit(Component comp, Object value) throws WrongValueException {
//				Textbox tb = (Textbox) comp;
//				if (tb.isReadonly()) {
//					return;
//				}
//				String customerCode = (String) value;
//				if (StringUtils.isBlank(customerCode)) {
//					throw new WrongValueException(comp, Labels.getLabel("common.msgbox.box.notEmpty"));
//				} else {
//					String errorMesg = null;
//					//投诉管理 根据现地法人 查询不同的客户信息，推广公用此方法不受影响 2015.2.26 chenjun
//					if(StringUtils.isNotBlank(tb.getName()) && "HKG".equals(tb.getName())){
//						CustomerSPHK		customerSpHk = customerService.getCustomerSPHKInfo(customerCode);
//						if (customerSpHk != null && StringUtils.isNotBlank(customerSpHk.getSoldToCode())) {
//							return;
//						} else {
//							errorMesg = Labels.getLabel("common.cust.custInfoNotFound");
//							throw new WrongValueException(comp, errorMesg);
//						}
//					}else{
//						CustomerSP customerSp = customerService.getCustomerSPInfo(customerCode);
//						if (customerSp != null && StringUtils.isNotBlank(customerSp.getSoldToCode())) {
//							return;
//						} else {
//							errorMesg = Labels.getLabel("common.cust.custInfoNotFound");
//							throw new WrongValueException(comp, errorMesg);
//						}
//					}
//				}
//			}
//		};
//	}

    /**
     * 自然数、正整数、非负整数
     * 1到9开头的整数
     */
    public static BaseFormServerConstraint getConst_NatureNo() {
        return BaseFormServerConstraint.getConst4Regression("^[1-9]\\d*$|^$",
                Labels.getLabel("common.textbox.NatureNumber"));
    }
    
    /**
     * Global No
     * 14或15位
     */
    public static BaseFormServerConstraint getConst_Number() {
        return BaseFormServerConstraint.getConst4Regression("^[A-Za-z0-9]{14,15}$|^$",
                Labels.getLabel("common.textbox.Globalnumber"));
    }
    
    /**
     * 供应商
     * 数字或字母
     */
    public static BaseFormServerConstraint getConst_Number_Letter() {
        return BaseFormServerConstraint.getConst4Regression("^[A-Za-z0-9]+$|^$",
                Labels.getLabel("common.textbox.Supplier"));
    }


    /**
     * 半角的字母数字下划线
     */
    public static BaseFormServerConstraint getConst_letter_Num_Underline() {
        return BaseFormServerConstraint.getConst4Regression("^[0-9a-zA-Z_]+$",
                Labels.getLabel("common.textbox.letterNumUnderline"));
    }

    /**
     * 6位数字或字母
     */
    public static BaseFormServerConstraint getConst_GroupCode() {
        return BaseFormServerConstraint.getConst4Regression("^\\w{1,6}$|^$",
                Labels.getLabel("cust.detail.groupCode.error"));
    }

    /**
     * 传真校验
     * 区号可以有或无3或4位、电话号码7或8位、分机号码可以有或无，分机号码用*号间隔、分机号码1到4位数字
     */
    public static BaseFormServerConstraint getConst_Fax() {
        //^$|((\\d{3,4})|\\d{3,4}-)\\d{7,8}$
        return BaseFormServerConstraint.getConst4Regression(
                /*"^((((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8}))"+  //电话号码或区号加电话号码
                        "|(\\d{4}|\\d{3})-(\\d{7,8})[-](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//有区号的分机号码为-的
                        "|(\\d{4}|\\d{3})-(\\d{7,8})[_](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//有区号的分机号码为_的
                        "|(\\d{4}|\\d{3})-(\\d{7,8})[*](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//有区号的分机号码为*的
                        "|(\\d{7,8})[-](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//无区号分机号码为-
                        "|(\\d{7,8})[_](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//无区号分机号码为_
                        "|(\\d{7,8})[*](\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)"+//无区号分机号码为*
                        "|^$",*/"^.{1,30}$|^$",
                Labels.getLabel("common.fax.faxnumError"));
    }

    /**
     * email校验
     * 非中文字符开头+@+（字母或0到9数字组合可以有下划线但下划线不能连续出现+点）一次或多次+必须以字母结尾
     */
    public static BaseFormServerConstraint getConst_Email() {
        // (^\\w+[-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$)|^$
        return BaseFormServerConstraint.getConst4Regression(
                "(^([^\u4e00-\u9fa5]+)+@(([a-z0-9]*[_]?[a-z0-9]+)|([a-z0-9]*[.]?[a-z0-9]+)|([a-z0-9]*[-]?[a-z0-9]+))+([\\.][A-Za-z]{1,})*$)|^$",
                Labels.getLabel("common.email.emailError"));
    }

    /**
     * 电话号码校验
     * 手机号：11位数字或12位数字
     * 座机：7或8位数字;3或4位区号-7或8位数字;3或4位区号-7或8位数字加*或-或#加1或2或3或4位分机号!
     *
     */
    public static BaseFormServerConstraint getConst_Telephone() {
        return BaseFormServerConstraint
                .getConst4Regression(//@off
                    /*	"^(((\\d{11})|(\\d{12})"+  //手机号
                "|((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8}))"+  //电话号码或区号加电话号码
                "|(\\d{4}|\\d{3})-(\\d{7,8})[-](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//有区号的分机号码为-的
                "|(\\d{4}|\\d{3})-(\\d{7,8})[_](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//有区号的分机号码为_的
                "|(\\d{4}|\\d{3})-(\\d{7,8})[*](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//有区号的分机号码为*的
                "|(\\d{7,8})[-](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//无区号分机号码为-
                "|(\\d{7,8})[_](\\d{4}|\\d{3}|\\d{2}|\\d{1})"+//无区号分机号码为_
                "|(\\d{7,8})[*](\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)"+//无区号分机号码为*
                "|^$",*/"^.{1,30}$|^$",
                        Labels.getLabel("common.telephone.InConformRule"));
    }//@on

    /**
     * 金额验证
     * 1到9开头或以0+点开头的数字组合
     */
    public static BaseFormServerConstraint getConst_money() {
        //(^(([1-9]\\d{0,9})|0)(\\.\\d{1,})?$)|^$
        return BaseFormServerConstraint.getConst4Regression("(^(([1-9]\\d{0,9})|0)(\\.\\d{1,})?$)|^$",
                "非法的输入");
    }

    /**
     * 邮编验证
     * 数字组合
     */
    public static BaseFormServerConstraint getConst_postalCode() {
        return BaseFormServerConstraint.getConst4Regression("^\\d{6}$|^$",
                Labels.getLabel("common.postalCode.InConformRule"));
    }

    /**
     * 下拉框请选择为空判断
     */
    public static BaseFormServerConstraint getConst_ComboxSelect() {
        return new BaseFormServerConstraint() {
            @Override
            public void validateOnFormSubmit(Component comp, Object value) throws WrongValueException {
                String strValue = (value == null ? null : (String) value);
                if (StringUtils.isBlank(strValue)
                        || StringUtils.equals(Labels.getLabel("common.pleaseSelect"), strValue)) {
                    String errorMesg = Labels.getLabel("common.msgbox.box.notEmpty");
                    throw new WrongValueException(comp, errorMesg);
                }
            }
        };
    }

    /**
     * Double 值范围 check
     * @param min
     * @param max
     * @return
     */
    public static Constraint getConst4DoubleMoney(final Double min, final Double max) {
        return new BaseFormServerConstraint() {
            @Override
            public void validateOnFormSubmit(Component comp, Object value) throws WrongValueException {
                if (StringUtils.isBlank((String) value)) {
                    return;
                }
                Double moneyValue = null;
                String moneyValueStr = "";
                int index = 0;
                String intNum = "";
                String pointNum = "";
                try {
                    moneyValue = (value == null ? null : Double.valueOf(((String) value).replaceAll(",", "")));
                    moneyValueStr = (value == null ? null : ((String) value).replaceAll(",", ""));
                    index = moneyValueStr.indexOf(".");
                    if (index > 0) {
                        intNum = moneyValueStr.substring(0, index);
                        pointNum = moneyValueStr.substring(index + 1);
                    } else {
                        intNum = moneyValueStr;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new WrongValueException(comp, Labels.getLabel("common.money.error"));
                }
                if (intNum.length() > 13) {
                    throw new WrongValueException(comp, Labels.getLabel("common.money.intnum.error"));
                }
                if (pointNum.length() > 2) {
                    throw new WrongValueException(comp, Labels.getLabel("common.money.pointnum.error"));
                }
                if ((intNum + pointNum).length() > 16) {
                    throw new WrongValueException(comp, Labels.getLabel("common.money.length.error"));
                }
                if (moneyValue != null) {
                    if (moneyValue.doubleValue() < min || moneyValue.doubleValue() > max) {
                        int lengthBeforePoint = calculateLengthBeforePoint(max);
                        String errorMesg = "超出范围(大于0，小数点前最多" + lengthBeforePoint + "位)";// Labels.getLabel("common.msgbox.box.notEmpty");
                        throw new WrongValueException(comp, errorMesg);
                    }
                } else {
                    return;
                }
            }
        };
    }

    protected int calculateLengthBeforePoint(Double max) {
        int i = 0;
        while (Math.pow(10, i) - max < 0) {
            i++;
        }
        return i;
    }

}