package cn.rkang.wxgate.ctrl.wxacc;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.model.WxAccMenu;
import cn.rkang.wxgate.model.WxAccMenu.MENU_TYPE;
import cn.rkang.wxgate.service.WxAccService;

@VariableResolver(DelegatingVariableResolver.class)
public class AddMenuDialogCtrl {

	static Logger	logger	= Logger.getLogger(AddMenuDialogCtrl.class);

	@WireVariable
	WxAccService	wxAccService;

	WxAcc			wxAcc;
	WxAccMenu		parentMenu;											//非空 表示正在为菜单文件夹添加子菜单

	WxAccMenu		formMenu;

	Window			thisWin;

	@SuppressWarnings("rawtypes")
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		thisWin = (Window) view;
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);

		Map param = Executions.getCurrent().getArg();
		wxAcc = (WxAcc) param.get("wxAcc");
		parentMenu = (WxAccMenu) param.get("parentMenu");

		formMenu = (WxAccMenu) param.get("menu");
		if (formMenu == null) {
			formMenu = new WxAccMenu();
			formMenu.setType(MENU_TYPE.view);

			formMenu.setParentId(parentMenu == null ? null : parentMenu.getId());
		}

		logger.debug("addMenuDialog.zul[appId:" + wxAcc.getAppId() + "]");
	}

	@Command
	public void onSaveMenu() {
		wxAccService.saveOrUpdateMenu(wxAcc, parentMenu, formMenu);
		Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "refreshMenuTree", null);
				thisWin.detach();
			}
		});
	}

	@Command
	public void onDeleteMenu() {
		Messagebox.show("是否确认删除？", "提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (StringUtils.equals(event.getName(), "onYes")) {
							wxAccService.deleteMenu(wxAcc.getAppId(), formMenu);
							BindUtils.postGlobalCommand(null, null, "refreshMenuTree", null);
							thisWin.detach();
						}
					}
				});
	}

	@Command
	public void onCancel() {
		thisWin.detach();
	}

	public WxAccMenu getFormMenu() {
		return formMenu;
	}

	public WxAccMenu getParentMenu() {
		return parentMenu;
	}

}