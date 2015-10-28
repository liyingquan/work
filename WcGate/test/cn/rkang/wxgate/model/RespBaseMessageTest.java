package cn.rkang.wxgate.model;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.rkang.wxgate.util.MessageUtil;

public class RespBaseMessageTest {

	@Test
	public void testGetToUserName() throws Exception {
		RespMessage m=new RespMessage();
		
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
		
		m.setArticles(artList);
		m.setArticleCount(artList.size());
		
		System.out.println( MessageUtil.messageNewsToXml(m));
	}

}
