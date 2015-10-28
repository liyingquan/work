package cn.rkang.wxgate.rest;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.rkang.wxgate.model.RespArticle;
import cn.rkang.wxgate.model.RespMessage;
import cn.rkang.wxgate.model.RespMessage.MSG_TYPE;
import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.model.WxAccMenu;
import cn.rkang.wxgate.service.WxAccService;
import cn.rkang.wxgate.util.MessageUtil;

import com.mongodb.gridfs.GridFSDBFile;

@Controller
public class WxRest {
	Logger			logger	= Logger.getLogger(WxRest.class);
	@Autowired
	WxAccService	wxAccService;

	@Autowired
	GridFsTemplate	gridFsTemplate;

	/**
	 * 处理微信平台发来的绑定服务号请求
	 * @param appId
	 * @param signature 
	 * @param timestamp
	 * @param nonce 随机数 用于验签名
	 * @param echostr 验签成功后返回 表示绑定成功
	 */
	@RequestMapping(value = "/wx", method = RequestMethod.GET)
	public void doGet(String appId, String signature, String timestamp, String nonce, String echostr,
			ServletResponse resp) throws Exception {
		PrintWriter out = resp.getWriter();
		// 请求校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		//		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
		out.print(echostr);
		//		}
		out.close();
		out = null;
	}

	/**
	 * 
	 * @param appId
	 * @param request
	 * @param resp
	 * @throws Exception
	 */
	@RequestMapping(value = "/wx", method = RequestMethod.POST)
	public void replyWxPost(String appId, ServletRequest request, ServletResponse resp) throws Exception {
		Map<String, String> requestMap = MessageUtil.parseXml((HttpServletRequest) request);
		System.out.println(appId);
		System.out.println(requestMap);
		resp.setCharacterEncoding("UTF-8");

		// 调用核心业务类接收消息、处理消息
		String respXml = processRequest(appId, requestMap);

		PrintWriter out = resp.getWriter();
		out.print(respXml);
		out.flush();
		out.close();

		System.out.println(respXml);
	}

	/**
	 * @param paramMap 
	 * {  FromUserName 
	 *    ToUserName 
	 *    MsgType：event text image link location voice 
	 *    ---Content：获取Msg内容
	 *    ---Event：MsgType为event 时，微信客户端的事件 CLICK LOCATION subscribe unsubscribe scan ...
	 *    -------EventKey :click点击菜单的key值
	 * }
	 * @return
	 */
	private String processRequest(String appId, Map<String, String> paramMap) {
		String MsgType = paramMap.get("MsgType");

		if (StringUtils.equals("event", MsgType)) {
			String Event = paramMap.get("Event");

			if (StringUtils.equals("CLICK", Event)) {//点击click

				String EventKey = paramMap.get("EventKey");
				RespMessage respMessage = new RespMessage();
				respMessage.setToUserName(paramMap.get("FromUserName"));
				respMessage.setFromUserName(paramMap.get("ToUserName"));
				//respMessage.setContent(EventKey);
				respMessage.setCreateTime(new Date().getTime());

				Query query = new Query();
				Criteria criteria = Criteria.where("appid").is(appId);
				query.addCriteria(criteria);
				logger.debug(query);

				WxAcc wxAcc = wxAccService.getWxAccByAppId(appId);

				/**
				 * 创建保存所有菜单的集合
				 */
				List<WxAccMenu> wxAccMenuList = new ArrayList<>();
				for (WxAccMenu wxAccMenu : wxAcc.getMenuList()) {
					if (wxAccMenu.getSub_button() != null) {
						wxAccMenuList.addAll(wxAccMenu.getSub_button());
					}
					wxAccMenuList.add(wxAccMenu);
				}

				for (WxAccMenu wxAccMenu : wxAccMenuList) {
					if (wxAccMenu.getKey() != null && StringUtils.equals(EventKey, wxAccMenu.getKey())) {
						String messageType = wxAccMenu.getRespMessage().getMsgTypeStr();
						if (StringUtils.equals(messageType, MSG_TYPE.text.toString())) {
							respMessage.setMsgType(MSG_TYPE.text);
							respMessage.setContent(wxAccMenu.getRespMessage().getContent());
							break;
						}
						respMessage.setArticles(wxAccMenu.getRespMessage().getArticles());
						respMessage.setArticleCount(wxAccMenu.getRespMessage().getArticles().size());
						respMessage.setMsgType(MSG_TYPE.news);
						break;
					}
				}
				String returnXml;
				if (respMessage.getMsgType() == MSG_TYPE.news) {
					for (RespArticle ra : respMessage.getArticles()) {
						ra.setPicUrl(wxAcc.getUrl() + "/rest/image/" + ra.getPicUrl());//拼接完整的PicUrl
					}

					returnXml = MessageUtil.messageNewsToXml(respMessage);
				} else {
					returnXml = MessageUtil.messageToXml(respMessage);
				}
				logger.debug(returnXml);
				return returnXml;
			} else if (StringUtils.equals("subscribe", Event)) {

				RespMessage newMessage = new RespMessage();
				newMessage.setMsgType(MSG_TYPE.news);
				newMessage.setToUserName(paramMap.get("FromUserName"));
				newMessage.setFromUserName(paramMap.get("ToUserName"));
				newMessage.setCreateTime(new Date().getTime());

				List<RespArticle> artList = new ArrayList<RespArticle>();

				RespArticle art1 = new RespArticle();
				art1.setTitle("消息1");
				art1.setPicUrl("http://img.topitme.com/event/welcome/qrcode.png");
				art1.setDescription("消息1的描述");
				art1.setUrl("http://m.rkang.cn");

				RespArticle art2 = new RespArticle();
				art2.setTitle("消息2");
				art2.setPicUrl("http://ubmcmm.baidustatic.com/media/v1/0f000K0FVpQyqphs88Alcf.gif");
				art2.setDescription("消息2的描述");
				art2.setUrl("http://m.rkang.cn");

				RespArticle art3 = new RespArticle();
				art3.setTitle("消息3");
				art3.setPicUrl("http://ubmcmm.baidustatic.com/media/v1/0f000K0FVpQyqphs88Alcf.gif");
				art3.setDescription("消息3的描述");
				art3.setUrl("http://m.rkang.cn");

				artList.add(art1);
				artList.add(art2);
				artList.add(art3);

				newMessage.setArticles(artList);
				newMessage.setArticleCount(artList.size());
				return MessageUtil.messageNewsToXml(newMessage);
			} else if (StringUtils.equals("SCAN", Event)) {

			}
		}
		return null;
	}

	@RequestMapping(value = "/image/{fileId}", method = RequestMethod.GET)
	public void getImage(@PathVariable("fileId") String fileId, ServletResponse resp) throws Exception {
		Query query = new Query().addCriteria(Criteria.where("filename").is(fileId));
		GridFSDBFile mainArticlePicFile = gridFsTemplate.findOne(query);
		ServletOutputStream out = resp.getOutputStream();
		mainArticlePicFile.writeTo(out);
		out.flush();
		out.close();
	}

}