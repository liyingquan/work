package cn.rkang.wxgate.ctrl.wxacc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.zkoss.bind.AnnotateBinder;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import cn.rkang.wxgate.model.RespArticle;
import cn.rkang.wxgate.model.RespMessage;
import cn.rkang.wxgate.model.RespMessage.MSG_TYPE;
import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.model.WxAccMenu;
import cn.rkang.wxgate.service.WxAccService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@VariableResolver(DelegatingVariableResolver.class)
public class AccMainMenuReplyEditCtrl {
	private static final String	FILE_UPLOAD_TEMP_KEY	= "media";											//上传图片的media对象 以此为key暂存在image组件的attribute中

	static Logger				logger					= Logger.getLogger(AccMainMenuReplyEditCtrl.class);

	@WireVariable
	GridFsTemplate				gridFsTemplate;
	@WireVariable
	WxAccService				wxAccService;

	WxAcc						wxAcc;
	WxAccMenu					menu;

	@Wire
	Grid						gridTemplate4MessageForm;
	@Wire
	Vbox						vbox4MessageForm;
	@Wire
	Vbox						vboxNewsForms;

	Map<String, Object>			initParamMap			= new HashMap<String, Object>();

	RespMessage					respMessagForm;

	RespArticle					mainArticle;

	List<RespArticle>			articleDraftList		= new ArrayList<RespArticle>();

	@Init
	public void init(@ExecutionArgParam("wxAcc") WxAcc wxAcc, @ExecutionArgParam("menu") WxAccMenu selectedMenu) {
		if (wxAcc != null) {
			initParamMap.put("wxAcc", wxAcc);
			initParamMap.put("menu", selectedMenu);
			this.wxAcc = wxAcc;
			this.menu = selectedMenu;
		} else {
			this.wxAcc = (WxAcc) initParamMap.get("wxAcc");
			this.menu = (WxAccMenu) initParamMap.get("menu");
		}

		if (menu != null && menu.getRespMessage() != null) {
			respMessagForm = menu.getRespMessage();
			mainArticle = respMessagForm.getArticles().size() > 0 ? respMessagForm.getArticles().get(0) : null;
			if (mainArticle == null) {
				mainArticle = new RespArticle();
				respMessagForm.getArticles().add(mainArticle);
			}
		} else if (menu != null && menu.getRespMessage() == null) {
			respMessagForm = new RespMessage();
			mainArticle = new RespArticle();
			respMessagForm.getArticles().add(mainArticle);
			menu.setRespMessage(respMessagForm);
		}

	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException {
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);
		if (respMessagForm == null) {
			return;
		}
		Iterator<RespArticle> iterator = respMessagForm.getArticles().iterator();
		if (iterator.hasNext()) {
			iterator.next();
			while (iterator.hasNext()) {
				this.appendNewsMsgForm(iterator.next(), false);//生成副标题文章form列表
			}
		}
		//初始化mainArticle的image
		if (mainArticle.getPicUrl() != null) {
			GridFSDBFile mainArticlePicFile = getImageFileByPicUrl(mainArticle.getPicUrl());
			if (mainArticlePicFile == null) {
				return;
			}
			Image img = (Image) view.query("image");
			img.setContent(new AImage(mainArticle.getTitle(), mainArticlePicFile.getInputStream()));
			img.setVisible(true);
		}
	}

	private GridFSDBFile getImageFileByPicUrl(String picUrl) {
		Query query = new Query().addCriteria(Criteria.where("filename").is(picUrl));
		GridFSDBFile mainArticlePicFile = gridFsTemplate.findOne(query);
		if (mainArticlePicFile == null) {
			logger.warn("找不到mongodb文件 " + query);
			return null;
		} else {
			return mainArticlePicFile;
		}
	}

	@Command
	public void onSelectMsgTypeText() {
		respMessagForm.setMsgType(MSG_TYPE.text);
		BindUtils.postNotifyChange(null, null, this, "respMessagForm");
	}

	@Command
	public void onSelectMsgTypeNews() {
		respMessagForm.setMsgType(MSG_TYPE.news);
		BindUtils.postNotifyChange(null, null, this, "respMessagForm");
	}

	@Command
	public void onClickTextMsgSave() {
		wxAccService.saveOrUpdateMenuRespMsg(wxAcc, menu, respMessagForm);
		Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "globalCancelForm", null);
			}
		});
	}

	@GlobalCommand
	public void globalCancelForm() {
		this.respMessagForm = new RespMessage();
		BindUtils.postNotifyChange(null, null, this, "respMessagForm");
		this.menu = null;
		BindUtils.postNotifyChange(null, null, this, "menu");
	}

	@Command
	public void onClickAddMoreMessageForm() throws IOException {
		RespArticle draft = new RespArticle();
		articleDraftList.add(draft);
		this.appendNewsMsgForm(draft, true);
	}

	/**
	 * 构造新的news图文form 附加在最后，每个form相互独立
	 * 动态构造form表单 参考 
	 * https://code.google.com/p/zkbooks/source/browse/trunk/developersreference/developersreference/src/main/java/org/zkoss/reference/developer/mvvm/advance/DynamicFormBindingComposer.java?r=478
	 * @throws IOException 
	 */
	private void appendNewsMsgForm(final RespArticle article, final boolean isDraft) throws IOException {
		final Grid grid = (Grid) gridTemplate4MessageForm.clone();
		Iterator<Component> iterator = grid.queryAll("textbox").iterator();
		final Textbox tbTitle = (Textbox) iterator.next();
		final Image img = (Image) grid.query("image");
		if (!isDraft) {
			GridFSDBFile picFile = this.getImageFileByPicUrl(article.getPicUrl());
			if (picFile != null) {
				img.setContent(new AImage(picFile.getMetaData().get("fileName").toString(), picFile.getInputStream()));
				img.setVisible(true);
			}
		}
		final Textbox tbDesc = (Textbox) iterator.next();
		final Textbox tbUrl = (Textbox) iterator.next();
		tbTitle.setReadonly(!isDraft);
		tbDesc.setReadonly(!isDraft);
		tbUrl.setReadonly(!isDraft);

		grid.setId(UUID.randomUUID().toString());
		grid.setVisible(true);

		Binder binder = new AnnotateBinder();
		binder.init(grid, this, initParamMap);
		grid.setAttribute("fx", article);

		binder.addPropertyLoadBindings(tbTitle, "value", "fx.title", null, null, null, null, null);
		binder.addPropertySaveBindings(tbTitle, "value", "fx.title", null, null, null, null, null, null, null);
		binder.addPropertyLoadBindings(tbDesc, "value", "fx.description", null, null, null, null, null);
		binder.addPropertySaveBindings(tbDesc, "value", "fx.description", null, null, null, null, null, null, null);
		binder.addPropertyLoadBindings(tbUrl, "value", "fx.url", null, null, null, null, null);
		binder.addPropertySaveBindings(tbUrl, "value", "fx.url", null, null, null, null, null, null, null);

		iterator = grid.queryAll("button").iterator();
		final Button btnFile = (Button) iterator.next();
		btnFile.setDisabled(!isDraft);
		final Button btnEdit = (Button) iterator.next();
		btnEdit.setVisible(!isDraft);
		final Button btnDelete = (Button) iterator.next();
		btnDelete.setVisible(!isDraft);
		final Button btnCancel = (Button) iterator.next();
		btnCancel.setVisible(isDraft);
		final Button btnSave = (Button) iterator.next();
		btnSave.setVisible(isDraft);

		//上传图片
		btnFile.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>() {

			@Override
			public void onEvent(UploadEvent event) throws Exception {
				Media media = event.getMedia();
				BigDecimal fileSizeKilo = new BigDecimal(0);

				if (media.isBinary()) {
					byte[] uploadedfile = IOUtils.toByteArray(media.getStreamData());
					fileSizeKilo = fileSizeKilo.add((new BigDecimal(uploadedfile.length).divide(new BigDecimal(1024),
							2, BigDecimal.ROUND_HALF_UP)));
				} else {
					byte[] uploadedfile = IOUtils.toByteArray(media.getReaderData());
					fileSizeKilo = fileSizeKilo.add((new BigDecimal(uploadedfile.length).divide(new BigDecimal(1024),
							2, BigDecimal.ROUND_HALF_UP)));
				}
				logger.debug("文件名【" + media.getName() + "】 类型【" + media.getContentType() + "】size【" + fileSizeKilo
						+ "k】");
				if (fileSizeKilo.intValue() >= 10240) {//上传附件大于10M
					Messagebox.show("附件不能大于10M", "通知", Messagebox.OK, Messagebox.INFORMATION, null);
					return;
				}

				img.setAttribute(FILE_UPLOAD_TEMP_KEY, media);
				img.setContent(new AImage("", media.getStreamData()));
				img.setVisible(true);
			}
		});

		btnEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				tbTitle.setReadonly(false);
				btnFile.setDisabled(false);
				tbDesc.setReadonly(false);
				tbUrl.setReadonly(false);

				btnEdit.setVisible(false);
				btnDelete.setVisible(false);
				btnCancel.setVisible(true);
				btnSave.setVisible(true);
			}
		});

		btnDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.show("是否确认删除文章？", "提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
						new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								if (StringUtils.equals(event.getName(), "onYes")) {
									grid.detach();
									respMessagForm.getArticles().remove(article);
									wxAccService.saveOrUpdateMenuRespMsg(wxAcc, menu, respMessagForm);
								}
							}
						});
			}
		});

		btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (isDraft) {//draft草稿状态的form 点cancel取消即删除
					grid.detach();
					articleDraftList.remove(article);
					return;
				}

				BindUtils.postGlobalCommand(null, null, "AccMainCtrl_refreshMenuReplyInclude", null);
			}
		});

		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				logger.debug(article + "[Title:" + article.getTitle() + "|PicUrl:" + article.getPicUrl() + "|Url:"
						+ article.getUrl() + "]");

				if (img.getAttribute(FILE_UPLOAD_TEMP_KEY) != null) {
					String uuid = UUID.randomUUID().toString();
					Media media = (Media) img.getAttribute(FILE_UPLOAD_TEMP_KEY);
					DBObject metadata = new BasicDBObject();
					metadata.put("fileName", media.getName());
					metadata.put("contentType", media.getContentType());
					metadata.put("format", media.getFormat());
					gridFsTemplate.store(media.getStreamData(), uuid, metadata);
					article.setPicUrl(uuid);//设置文章图片id——关联到mongodb的GridFS中的文件filename
				}

				if (!respMessagForm.getArticles().contains(article)) {
					respMessagForm.getArticles().add(article);
				}
				wxAccService.saveOrUpdateMenuRespMsg(wxAcc, menu, respMessagForm);

				Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
					}
				});

				tbTitle.setReadonly(true);
				btnFile.setDisabled(true);
				tbDesc.setReadonly(true);
				tbUrl.setReadonly(true);
				btnEdit.setVisible(true);
				btnDelete.setVisible(true);
				btnCancel.setVisible(false);
				btnSave.setVisible(false);
			}
		});

		binder.loadComponent(grid, true);

		vboxNewsForms.getChildren().add(vboxNewsForms.getChildren().size(), grid);
		vboxNewsForms.invalidate();
	}

	@Command
	public void uploadImage(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event, Event e) throws IOException {
		logger.debug(event.getTarget());

		Media media = event.getMedia();
		if (media != null) {
			BigDecimal fileSizeKilo = new BigDecimal(0);

			if (media.isBinary()) {
				byte[] uploadedfile = IOUtils.toByteArray(media.getStreamData());
				fileSizeKilo = fileSizeKilo.add((new BigDecimal(uploadedfile.length).divide(new BigDecimal(1024), 2,
						BigDecimal.ROUND_HALF_UP)));
			} else {
				byte[] uploadedfile = IOUtils.toByteArray(media.getReaderData());
				fileSizeKilo = fileSizeKilo.add((new BigDecimal(uploadedfile.length).divide(new BigDecimal(1024), 2,
						BigDecimal.ROUND_HALF_UP)));
			}
			logger.debug("文件名【" + media.getName() + "】 类型【" + media.getContentType() + "】size【" + fileSizeKilo + "k】");
			if (fileSizeKilo.intValue() >= 10240) {//上传附件大于10M
				Messagebox.show("附件不能大于10M", "通知", Messagebox.OK, Messagebox.INFORMATION, null);
				return;
			}

			DBObject metadata = new BasicDBObject();
			metadata.put("fileName", media.getName());
			metadata.put("contentType", media.getContentType());
			metadata.put("format", media.getFormat());

			mainArticle.setPicUrl(UUID.randomUUID().toString());
			mainArticle.setTransientPicFileMetadata(metadata);
			mainArticle.setTransientPicContent(media);

			Image img = (Image) event.getTarget().getParent().query("image");
			img.setVisible(true);
			logger.debug(img);
			img.setContent(new AImage(media.getName(), media.getStreamData()));
		}
	}

	boolean	mainArticleEditFlag	= false;	//主文章编辑标识

	@Command
	public void onMainArticleEdit() {
		mainArticleEditFlag = true;
		BindUtils.postNotifyChange(null, null, this, "mainArticleEditFlag");
	}

	@Command
	public void onMainArticleSubmit() {
		wxAccService.saveOrUpdateMenuRespMsg(wxAcc, menu, respMessagForm);
		if (mainArticle.getTransientPicContent() != null) {
			gridFsTemplate.store(((Media) mainArticle.getTransientPicContent()).getStreamData(),
					mainArticle.getPicUrl(), mainArticle.getTransientPicFileMetadata());
		}
		Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "globalCancelFormMainArticle", null);
			}
		});
	}

	@GlobalCommand
	public void globalCancelFormMainArticle() {
		mainArticleEditFlag = false;
		BindUtils.postNotifyChange(null, null, this, "mainArticleEditFlag");
	}

	public WxAccMenu getMenu() {
		return menu;
	}

	public RespMessage getRespMessagForm() {
		return respMessagForm;
	}

	public RespArticle getMainArticle() {
		return mainArticle;
	}

	public void setMainArticle(RespArticle mainArticle) {
		this.mainArticle = mainArticle;
	}

	public boolean getMainArticleEditFlag() {
		return mainArticleEditFlag;
	}

}