package cn.rkang.wxgate.ctrl.wxacc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cn.rkang.wxgate.model.Token;
import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.model.WxAccMenu;
import cn.rkang.wxgate.model.WxAccMenu.MENU_TYPE;
import cn.rkang.wxgate.service.WxAccService;
import cn.rkang.wxgate.util.CommonUtil;

@VariableResolver(DelegatingVariableResolver.class)
public class AccMainCtrl {
	static Logger	logger					= Logger.getLogger(AccMainCtrl.class);

	@WireVariable
	MongoTemplate	mongoTemplate;
	@WireVariable
	WxAccService	wxAccService;
	@WireVariable
	HttpClient		httpClient;

	@Wire
	Menupopup		menupopup;
	@Wire
	Vbox			vboxTextReply;

	String			appId;

	WxAcc			wxAccForm;

	boolean			accBasicEditFlag		= false;
	boolean			accInterfaceEditFlag	= false;
	boolean			accWxPayEditFlag		= false;

	boolean			newsMessageEdit			= false;

	MenuTreeNodeVo	rightSelectedTreeNode;

	WxAccMenu		selectedMenu;

	@SuppressWarnings("rawtypes")
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);

		Map param = Executions.getCurrent().getArg();
		appId = (String) param.get("appId");
		logger.debug("accMain.zul[appId:" + appId + "]");
		wxAccForm = wxAccService.getWxAccByAppId(appId);
	}

	@Command
	public void onAccBasicEdit() {//开始编辑基本信息form
		accBasicEditFlag = true;
		BindUtils.postNotifyChange(null, null, this, "accBasicEditFlag");
		accInterfaceEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accInterfaceEditFlag");
		accWxPayEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accWxPayEditFlag");

		wxAccForm = wxAccService.getWxAccByAppId(appId);
		BindUtils.postNotifyChange(null, null, this, "wxAccForm");

	}

	@Command
	public void onAccInterfaceEdit() {
		accBasicEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accBasicEditFlag");
		accInterfaceEditFlag = true;
		BindUtils.postNotifyChange(null, null, this, "accInterfaceEditFlag");
		accWxPayEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accWxPayEditFlag");

		wxAccForm = wxAccService.getWxAccByAppId(appId);
		BindUtils.postNotifyChange(null, null, this, "wxAccForm");

	}

	@Command
	public void genRandomToken() {
		wxAccForm.setToken(UUID.randomUUID().toString());
		BindUtils.postNotifyChange(null, null, this, "wxAccForm");
	}

	@Command
	public void onAccWxPayEdit() {
		accBasicEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accBasicEditFlag");
		accInterfaceEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accInterfaceEditFlag");
		accWxPayEditFlag = true;
		BindUtils.postNotifyChange(null, null, this, "accWxPayEditFlag");

		wxAccForm = wxAccService.getWxAccByAppId(appId);
		BindUtils.postNotifyChange(null, null, this, "wxAccForm");
	}

	@Command
	public void onAccFormSubmit() {
		mongoTemplate.save(wxAccForm);
		Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "wxAccCtrlRefreshAccList", null);
			}
		});

		BindUtils.postNotifyChange(null, null, this, "wxAccForm");

		accBasicEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accBasicEditFlag");
	}

	@Command
	public void onAccFormCancel() {
		accBasicEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accBasicEditFlag");
		accInterfaceEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accInterfaceEditFlag");
		accWxPayEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "accWxPayEditFlag");
	}

	@Command
	public void onAddMenu() {
		List<WxAccMenu> allMenuList = findAllMenuList();
		if (allMenuList.size() >= 3) {
			Messagebox.show("微信一级菜单最大数量为3个!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("wxAcc", wxAccForm);
		Window win = (Window) Executions.createComponents("/zuls/wxAcc/addMenuDialog.zul", null, args);
		win.doModal();
	}

	/**
	 * @param withoutDeleted true 不包含已逻辑删除的
	 */
	private List<WxAccMenu> findAllMenuList() {
		//		if (withoutDeleted) {
		//			criteria = criteria.andOperator(Criteria.where("status").ne(false));
		//		}
		WxAcc wxAcc = wxAccService.getWxAccByAppId(appId);
		if (wxAcc == null) {
			logger.warn("查询不到WxAcc by appId " + appId);
			return null;
		}

		return wxAcc.getMenuList();
	}

	@GlobalCommand("refreshMenuTree")
	@NotifyChange({ "menuTree" })
	public void refreshMenuTree() {
	}

	@SuppressWarnings("serial")
	public DefaultTreeModel<MenuTreeNodeVo> getMenuTree() {
		DefaultTreeNode<MenuTreeNodeVo> result = new DefaultTreeNode<MenuTreeNodeVo>(null,
				new ArrayList<TreeNode<MenuTreeNodeVo>>());
		List<WxAccMenu> allMenuList = wxAccForm.getMenuList();

		for (WxAccMenu wam : allMenuList) {
			MenuTreeNodeVo menuLevel1 = new MenuTreeNodeVo(null, wam.getName(), wam.getKey(), false, false);
			menuLevel1.setValue(wam);
			DefaultTreeNode<MenuTreeNodeVo> menuTreeNode = null;
			if (wam.getType() != null) {
				menuTreeNode = new DefaultTreeNode<MenuTreeNodeVo>(menuLevel1) {
					@Override
					public boolean isLeaf() {
						return true;
					}
				};
			} else {
				ArrayList<TreeNode<MenuTreeNodeVo>> childList = new ArrayList<TreeNode<MenuTreeNodeVo>>();
				if (wam.getSub_button() != null) {
					for (WxAccMenu subMenu : wam.getSub_button()) {
						MenuTreeNodeVo subMenuNodeVo = new MenuTreeNodeVo(null, subMenu.getName(), subMenu.getKey(),
								true, true);
						subMenuNodeVo.setValue(subMenu);

						TreeNode<MenuTreeNodeVo> aChild = new DefaultTreeNode<MenuTreeNodeVo>(subMenuNodeVo) {
							@Override
							public boolean isLeaf() {
								return true;
							}
						};
						childList.add(aChild);
						menuLevel1.getChildList().add(subMenuNodeVo);
					}
				}
				logger.debug(menuLevel1.getOpen());
				menuTreeNode = new DefaultTreeNode<MenuTreeNodeVo>(menuLevel1, childList);
			}

			result.getChildren().add(menuTreeNode);
		}

		DefaultTreeModel<MenuTreeNodeVo> model = new DefaultTreeModel<MenuTreeNodeVo>(result);
		return model;
	}

	@Command
	public void onRightClickTreeMenu(@BindingParam("paramEvent") Event paramEvent,
			@BindingParam("data") MenuTreeNodeVo node) {
		if (node.getValue().getType() == null) {//folder类型 type为空
			menupopup.open(paramEvent.getTarget(), "after_start");
			rightSelectedTreeNode = node;
		}
	}

	@Command
	public void onAddSubMenu() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("wxAcc", wxAccForm);
		args.put("parentMenu", rightSelectedTreeNode.getValue());
		Window win = (Window) Executions.createComponents("/zuls/wxAcc/addMenuDialog.zul", null, args);
		win.doModal();
	}

	@Command
	public void onReleaseMenu() {
		Messagebox.show("是否确认上传到微信平台？上传后将会被所有订阅用户看到", "提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (StringUtils.equals(event.getName(), "onYes")) {
							uploadWxAccMenu();
						}
					}
				});
	}

	private void uploadWxAccMenu() {
		List<WxAccMenu> allMenuList = findAllMenuList();//同步到微信平台时 忽略逻辑删除的menu

		MenuJsonObj mjo = new MenuJsonObj();
		mjo.setButton(allMenuList);
		String json = JSONObject.fromObject(mjo).toString();
		logger.debug(json);

		Token token = CommonUtil.getToken(appId, this.getWxAccSecritKey(appId));

		String showMsg = "";
		boolean result;
		if (null != token) {
			logger.debug("上传微信菜单\n" + json);
			// 创建菜单
			JSONObject jsonObject = CommonUtil.httpsRequest(
					CommonUtil.menu_create_url.replace("ACCESS_TOKEN", token.getAccessToken()), "POST", json);
			// 判断菜单创建结果
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				result = true;
				showMsg = "菜单同步微信平台成功！";

				wxAccService.updateAccLastSynchTimestamp(appId, new Date());
			} else {
				result = false;
				showMsg = "创建菜单失败 errcode:{" + "errorCode+} errmsg:{" + errorMsg + "}";
				logger.error(showMsg);
			}
		} else {
			result = false;
			showMsg = "ACCESS_TOKEN获取失败！";
		}

		Messagebox.show(showMsg, "提示", Messagebox.OK, result ? Messagebox.INFORMATION : Messagebox.ERROR,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						BindUtils.postGlobalCommand(null, null, "refreshMenuSychTimestamp", null);
					}
				});
	}

	@Command
	public void onRefreshMenuTree() {
		BindUtils.postNotifyChange(null, null, this, "menuTree");
	}

	@GlobalCommand("refreshMenuSychTimestamp")
	@NotifyChange({ "menuSychTimestamp" })
	public void refreshMenuSychTimestamp() {

	}

	public String getMenuSychTimestamp() {
		WxAcc wa = wxAccService.getWxAccByAppId(appId);
		if (wa.getLastSynchTimestamp() != null) {
			return "最近一次：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(wa.getLastSynchTimestamp());
		} else {
			return null;
		}
	}

	@Wire
	Include	include4MenuDetail;

	@Command
	public void onDetailMenu(@BindingParam("e") Event event, @BindingParam("menu") WxAccMenu menu) {
		if (menu.getType() != MENU_TYPE.click) {
			String alertNotSupport = "不是click类型的menu，不支持detail操作！";
			logger.debug(alertNotSupport);
			//			selectedMenu = null;
			//			logger.debug(selectedMenu);
			//			BindUtils.postNotifyChange(null, null, this, "selectedMenu");
			//			BindUtils.postNotifyChange(null, null, this, "selectedMenuTextReply");
			((Treerow) event.getTarget()).setTooltiptext(alertNotSupport);
			return;
		}
		selectedMenu = menu;
		logger.debug(selectedMenu);

		include4MenuDetail.setDynamicProperty("wxAcc", wxAccForm);
		include4MenuDetail.setDynamicProperty("menu", selectedMenu);
		include4MenuDetail.setSrc("/zuls/wxAcc/accMain_menuReplyEdit.zul");
		include4MenuDetail.invalidate();
	}

	@GlobalCommand("AccMainCtrl_refreshMenuReplyInclude")
	public void refreshMenuReplyInclude() {
		wxAccForm = wxAccService.getWxAccByAppId(appId);

		include4MenuDetail.setDynamicProperty("wxAcc", null);
		include4MenuDetail.setDynamicProperty("menu", null);
		include4MenuDetail.setSrc("/zuls/wxAcc/accMain_menuReplyEdit.zul");
		include4MenuDetail.invalidate();

		BindUtils.postNotifyChange(null, null, this, "menuTree");
	}

	@Command
	public void onDoubleClickMenu(@BindingParam("menu") WxAccMenu menu) {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("wxAcc", wxAccForm);
		args.put("menu", menu);
		Window win = (Window) Executions.createComponents("/zuls/wxAcc/addMenuDialog.zul", null, args);
		win.doModal();
	}

	private String getWxAccSecritKey(String appId2) {
		WxAcc wxAcc = mongoTemplate.find(new Query().addCriteria(Criteria.where("appId").is(appId2)), WxAcc.class).get(
				0);

		return wxAcc.getAppSecret();
	}

	public WxAcc getWxAccForm() {
		return wxAccForm;
	}

	@Command
	public void onOpenWest(@BindingParam("e") Event e) {
		e.getTarget().invalidate();
	}

	public WxAccMenu getSelectedMenu() {
		return selectedMenu;
	}

	public boolean getAccBasicEditFlag() {
		return accBasicEditFlag;
	}

	public String getAppId() {
		return appId;
	}

	public boolean getAccInterfaceEditFlag() {
		return accInterfaceEditFlag;
	}

	public boolean getAccWxPayEditFlag() {
		return accWxPayEditFlag;
	}

	public boolean getNewsMessageEdit() {
		return newsMessageEdit;
	}
}