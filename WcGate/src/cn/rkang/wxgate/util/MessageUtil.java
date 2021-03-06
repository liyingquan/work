package cn.rkang.wxgate.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.rkang.wxgate.model.RespArticle;
import cn.rkang.wxgate.model.RespMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息处理工具类
 * 
 * @author liufeng
 * @date 2013-09-15
 */
public class MessageUtil {
	// 请求消息类型：文本
	public static final String	REQ_MESSAGE_TYPE_TEXT		= "text";
	// 请求消息类型：图片
	public static final String	REQ_MESSAGE_TYPE_IMAGE		= "image";
	// 请求消息类型：语音
	public static final String	REQ_MESSAGE_TYPE_VOICE		= "voice";
	// 请求消息类型：视频
	public static final String	REQ_MESSAGE_TYPE_VIDEO		= "video";
	// 请求消息类型：地理位置
	public static final String	REQ_MESSAGE_TYPE_LOCATION	= "location";
	// 请求消息类型：链接
	public static final String	REQ_MESSAGE_TYPE_LINK		= "link";

	// 请求消息类型：事件推送
	public static final String	REQ_MESSAGE_TYPE_EVENT		= "event";

	// 事件类型：subscribe(订阅)
	public static final String	EVENT_TYPE_SUBSCRIBE		= "subscribe";
	// 事件类型：unsubscribe(取消订阅)
	public static final String	EVENT_TYPE_UNSUBSCRIBE		= "unsubscribe";
	// 事件类型：scan(用户已关注时的扫描带参数二维码)
	public static final String	EVENT_TYPE_SCAN				= "scan";
	// 事件类型：LOCATION(上报地理位置)
	public static final String	EVENT_TYPE_LOCATION			= "LOCATION";
	// 事件类型：CLICK(自定义菜单)
	public static final String	EVENT_TYPE_CLICK			= "CLICK";

	// 响应消息类型：文本
	public static final String	RESP_MESSAGE_TYPE_TEXT		= "text";
	// 响应消息类型：图片
	public static final String	RESP_MESSAGE_TYPE_IMAGE		= "image";
	// 响应消息类型：语音
	public static final String	RESP_MESSAGE_TYPE_VOICE		= "voice";
	// 响应消息类型：视频
	public static final String	RESP_MESSAGE_TYPE_VIDEO		= "video";
	// 响应消息类型：音乐
	public static final String	RESP_MESSAGE_TYPE_MUSIC		= "music";
	// 响应消息类型：图文
	public static final String	RESP_MESSAGE_TYPE_NEWS		= "news";

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return Map<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 扩展xstream使其支持CDATA
	 */
	//	private static XStream	xstream	= new XStream(new XppDriver() {
	//										@Override
	//										public HierarchicalStreamWriter createWriter(Writer out) {
	//											return new PrettyPrintWriter(out) {
	//												// 对所有xml节点的转换都增加CDATA标记
	//												boolean	cdata	= true;
	//
	//												@Override
	//												@SuppressWarnings("unchecked")
	//												public void startNode(String name, Class clazz) {
	//													super.startNode(name, clazz);
	//												}
	//
	//												@Override
	//												protected void writeText(QuickWriter writer, String text) {
	//													if (cdata) {
	//														writer.write("<![CDATA[");
	//														writer.write(text);
	//														writer.write("]]>");
	//													} else {
	//														writer.write(text);
	//													}
	//												}
	//											};
	//										}
	//									});

	/**
	 * 消息对象转换成xml
	 * 
	 * @param imageMessage 图片消息对象
	 * @return xml
	 */
	public static String messageToXml(RespMessage respBaseMessage) {
		XStream xstream = new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					// 对所有xml节点的转换都增加CDATA标记
					boolean	cdata	= true;

					@Override
					@SuppressWarnings("unchecked")
					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
					}

					@Override
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});

		xstream.alias("xml", respBaseMessage.getClass());
		return xstream.toXML(respBaseMessage);
	}

	public static String messageNewsToXml(RespMessage respMessage) {
		XStream xstream = new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					// 对所有xml节点的转换都增加CDATA标记
					boolean	cdata	= true;

					@Override
					@SuppressWarnings("unchecked")
					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
					}

					@Override
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});

		xstream.alias("xml", respMessage.getClass());
		xstream.alias("item", new RespArticle().getClass());
		return xstream.toXML(respMessage);
	}

}
