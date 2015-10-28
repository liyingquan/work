package cn.rkang.wxgate.model;

import org.springframework.data.annotation.Transient;

import com.mongodb.DBObject;

/**
 * 图文model
 */
public class RespArticle {
	// 图文消息名称
	private String	Title;
	// 图文消息描述
	private String	Description;
	// 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
	private String	PicUrl;
	// 点击图文消息跳转链接
	private String	Url;

	@Transient
	DBObject		transientPicFileMetadata;

	@Transient
	Object			transientPicContent;

	public RespArticle() {

	}

	public RespArticle(String title, String description, String picUrl, String url) {
		super();
		Title = title;
		Description = description;
		PicUrl = picUrl;
		Url = url;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return null == Description ? "" : Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPicUrl() {
		return null == PicUrl ? "" : PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getUrl() {
		return null == Url ? "" : Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public DBObject getTransientPicFileMetadata() {
		return transientPicFileMetadata;
	}

	public void setTransientPicFileMetadata(DBObject transientPicFileMetadata) {
		this.transientPicFileMetadata = transientPicFileMetadata;
	}

	public Object getTransientPicContent() {
		return transientPicContent;
	}

	public void setTransientPicContent(Object transientPicContent) {
		this.transientPicContent = transientPicContent;
	}
}