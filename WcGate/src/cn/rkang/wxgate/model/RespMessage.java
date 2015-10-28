package cn.rkang.wxgate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信响应消息model，参考文档见 http://mp.weixin.qq.com/wiki/14/89b871b5466b19b3efa4ada8e577d45e.html#.E5.9B.9E.E5.A4.8D.E6.96.87.E6.9C.AC.E6.B6.88.E6.81.AF
 * text类型的消息例如：
	<xml>
		<ToUserName><![CDATA[toUser]]></ToUserName>
		<FromUserName><![CDATA[fromUser]]></FromUserName>
		<CreateTime>12345678</CreateTime>
		<MsgType><![CDATA[text]]></MsgType>
		<Content><![CDATA[你好]]></Content>
	</xml>
 * 
 * news类型的消息例如：
	<xml>
		<ToUserName><![CDATA[toUser]]></ToUserName>
		<FromUserName><![CDATA[fromUser]]></FromUserName>
		<CreateTime>12345678</CreateTime>
		<MsgType><![CDATA[news]]></MsgType>
		<ArticleCount>2</ArticleCount>
		<Articles>
			<item>
				<Title><![CDATA[title1]]></Title>
				<Description><![CDATA[description1]]></Description>
				<PicUrl><![CDATA[picurl]]></PicUrl>
				<Url><![CDATA[url]]></Url>
			</item>
			<item>
				<Title><![CDATA[title]]></Title>
				<Description><![CDATA[description]]></Description>
				<PicUrl><![CDATA[picurl]]></PicUrl>
				<Url><![CDATA[url]]></Url>
			</item>
		</Articles>
	</xml>
 */
public class RespMessage {
	public enum MSG_TYPE {
		text, news
	}

	// 接收方帐号（收到的OpenID）
	private String				ToUserName;
	// 开发者微信号
	private String				FromUserName;
	// 消息创建时间 （整型）
	private long				CreateTime;
	// 消息类型
	private MSG_TYPE			MsgType		= MSG_TYPE.text;

	String						Content;									//类型为MSG_TYPE.text的 Message文本消息值

	//news类型的消息 附加字段如下
	// 图文消息个数，限制为10条以内
	private Integer				ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<RespArticle>	Articles	= new ArrayList<RespArticle>();

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public Integer getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(Integer articleCount) {
		ArticleCount = articleCount;
	}

	public List<RespArticle> getArticles() {
		return Articles;
	}

	public void setArticles(List<RespArticle> articles) {
		Articles = articles;
	}

	public MSG_TYPE getMsgType() {
		return MsgType;
	}

	public String getMsgTypeStr() {
		return MsgType == null ? null : MsgType.toString();
	}

	public void setMsgType(MSG_TYPE msgType) {
		MsgType = msgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
