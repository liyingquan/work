package cn.rkang.wxgate.ctrl;

import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;

public class HomeCtrl {
	@Wire("#topWindow")
	Window			topWindow;
	@Wire("vlayout > include")
	List<Include>	includeList;

	private A		topMenuSelected;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);

		ServletRequest request = ServletFns.getCurrentRequest();

		Clients.resize(view);
		//Clients.resize(ehsA.getParent());
		//		Clients.resize(ehsA);
		//		ehsA.getParent().invalidate();
	}

	@Command
	public void onClickTopmenu(@BindingParam("e") Event event) {
		topMenuSelected = (A) event.getTarget();
		if (StringUtils.equals("topmenu-sel", topMenuSelected.getSclass())) {
			return;
		}
		Clients.showBusy("处理中，请稍后....");//点击顶层a页签后，立即反馈loading状态，改善用户体验
		Events.echoEvent("onLater", topWindow, null);
	}

	@Listen("onLater = #topWindow")
	public void onClickTopmenuLater() {
		int i = 0;
		for (Component comp : topMenuSelected.getParent().getChildren()) {
			A a = (A) comp;
			a.setSclass(null);
			includeList.get(i).setVisible(false);
			if (topMenuSelected == a) {
				includeList.get(i).setVisible(true);
				Clients.resize(includeList.get(i));
			}
			i++;
		}
		topMenuSelected.setSclass("topmenu-sel");
		Clients.clearBusy();
	}
}