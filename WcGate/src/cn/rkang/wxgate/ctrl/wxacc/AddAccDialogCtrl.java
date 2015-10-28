package cn.rkang.wxgate.ctrl.wxacc;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.rkang.wxgate.model.WxAcc;

@VariableResolver(DelegatingVariableResolver.class)
public class AddAccDialogCtrl {

	static Logger			logger	= Logger.getLogger(AddAccDialogCtrl.class);

	@WireVariable
	private MongoTemplate	mongoTemplate;

	private WxAcc			form;

	Window					thisWin;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		thisWin = (Window) view;
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);

		form = new WxAcc();
	}

	@Command
	public void onSave() {
		mongoTemplate.save(form);
		Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "wxAccCtrlRefreshAccList", null);
				thisWin.detach();
			}
		});
	}

	@Command
	public void onCancel() {
		thisWin.detach();
	}

	public WxAcc getForm() {
		return form;
	}

}