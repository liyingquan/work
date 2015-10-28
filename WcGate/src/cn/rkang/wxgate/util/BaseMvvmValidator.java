package cn.rkang.wxgate.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.impl.XulElement;

/**
 * 通用Validator校验器
 * @param <T>
 */
public class BaseMvvmValidator<T> extends AbstractValidator {
	private static Logger				logger	= Logger.getLogger(BaseMvvmValidator.class);
	private T							formBean;

	private javax.validation.Validator	validator;

	/**
	 * @param formBean 用来bind并validate的对象
	 * @param validator JSR303 Validator
	 */
	public BaseMvvmValidator(T formBean, Validator validator) {
		super();
		this.formBean = formBean;
		this.validator = validator;
	}

	// TODO 属性为null时忽略
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void validate(ValidationContext ctx) {
		for (Entry<String, Property[]> entry : ctx.getProperties().entrySet()) {
			Object value = entry.getValue()[0].getValue();
			Map bindPropertyPair = new HashMap();
			bindPropertyPair.put(entry.getKey(), value);
			try {
				BeanUtils.populate(formBean, bindPropertyPair);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				continue;
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
				continue;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}

		Set<ConstraintViolation<T>> errors = validator.validate(formBean, Default.class);
		for (ConstraintViolation<T> error : errors) {
			String targetProperty = (String) error.getConstraintDescriptor().getAttributes().get("targetProperty");
			String path = targetProperty == null ? error.getPropertyPath().toString() : targetProperty;
			logger.warn(path + "|" + error.getMessage());
			super.addInvalidMessage(ctx, path, error.getMessage());
		}

		// 当页面组件校验出错，滚动页面使其出现在用户可见范围，并获得焦点，
		// 因errors集合里面存放的并不是按照页面顺序，当多个字段同时不符合校验条件时，随机抽取获取焦点展现
		// 获取焦点的控件的页面id必须与页面vmsgs的key一致，否则无法获取焦点
		if (errors != null && errors.size() > 0) {
			ConstraintViolation<T> con = errors.iterator().next();
			String target = (String) con.getConstraintDescriptor().getAttributes().get("targetProperty");
			String compId = target == null ? con.getPropertyPath().toString() : target;
			Component comp = ctx.getBindContext().getComponent();
			Clients.scrollIntoView(comp);// 随机的一个错误控件
			if (StringUtils.isNotBlank(compId)) {
				XulElement xulElement = (XulElement) comp.query("#" + compId);
				if (xulElement != null) {
					xulElement.setFocus(true);
				}
			}
		}
	}

	public void resetFormBean(T formBean) {
		this.formBean = formBean;
	}
}