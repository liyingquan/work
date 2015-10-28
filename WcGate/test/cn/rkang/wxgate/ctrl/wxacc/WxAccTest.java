package cn.rkang.wxgate.ctrl.wxacc;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rkang.wxgate.model.RespArticle;
import cn.rkang.wxgate.model.RespMessage;
import cn.rkang.wxgate.model.RespMessage.MSG_TYPE;
import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.model.WxAccMenu;
import cn.rkang.wxgate.model.WxAccMenu.MENU_TYPE;
import cn.rkang.wxgate.service.WxAccService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-xml/wxgate-common-context.xml",
		"classpath:/spring-xml/wxgate-mvc.xml" })
public class WxAccTest {

	@Resource
	WxAccService	wxAccService;

	@Resource
	MongoTemplate	mongoTemplate;

	WxAcc			wxAcc;

	@Before
	public void beforeEveryTest() {
		mongoTemplate.dropCollection(WxAcc.class);

		wxAcc = new WxAcc();
		wxAcc.setAccName("李英权测试号1");
		wxAcc.setAccNo("gh_e9dae6e39ab9");
		wxAcc.setAppId("wxfabea7f9f92255f7");
		wxAcc.setAppSecret("a742241156ec66ab4294e9dad63a9842");
		wxAcc.setCrtDate(new Date());
		mongoTemplate.save(wxAcc);

		WxAccMenu wxAccMenu1 = new WxAccMenu();
		wxAccMenu1.setId(UUID.randomUUID().toString());
		wxAccMenu1.setKey("一级click");
		wxAccMenu1.setType(MENU_TYPE.click);
		wxAccMenu1.setName("按钮1");
		wxAccService.saveOrUpdateMenu(wxAcc, null, wxAccMenu1);

		WxAccMenu wxAccMenu2 = new WxAccMenu();
		wxAccMenu2.setId(UUID.randomUUID().toString());
		wxAccMenu2.setKey("一级菜单");
		wxAccMenu2.setType(MENU_TYPE.view);
		wxAccMenu2.setName("菜单");
		wxAccService.saveOrUpdateMenu(wxAcc, null, wxAccMenu2);

		WxAccMenu wxAccMenu3 = new WxAccMenu();
		wxAccMenu3.setId(UUID.randomUUID().toString());
		wxAccMenu3.setKey("sub_button1");
		wxAccMenu3.setType(MENU_TYPE.click);
		wxAccMenu3.setName("二级子菜单1");
		wxAccService.saveOrUpdateMenu(wxAcc, wxAccMenu2, wxAccMenu3);

		RespMessage resp = new RespMessage();
		resp.setContent("文本消息");
		resp.setMsgType(MSG_TYPE.text);
		wxAccService.saveOrUpdateMenuRespMsg(wxAcc, wxAccMenu3, resp);

		WxAccMenu wxAccMenu4 = new WxAccMenu();
		wxAccMenu4.setId(UUID.randomUUID().toString());
		wxAccMenu4.setKey("sub_button2");
		wxAccMenu4.setType(MENU_TYPE.click);
		wxAccMenu4.setName("二级子菜单2");
		wxAccMenu4.setRespMessage(resp);
		wxAccService.saveOrUpdateMenu(wxAcc, wxAccMenu2, wxAccMenu4);

		System.out.println(mongoTemplate.find(new Query(), WxAcc.class));
	}

	@Test
	public void updateMenu() {//测试    更新微信菜单字段   的逻辑
		WxAccMenu textMenu = wxAcc.getMenuList().get(1).getSub_button().get(0);

		textMenu.setName("新的菜单名abc");

		wxAccService.saveOrUpdateMenu(wxAcc, wxAcc.getMenuList().get(1), textMenu);

		wxAcc = mongoTemplate.findOne(
				new Query().addCriteria(Criteria.where("menuList.sub_button.name").is("新的菜单名abc")), WxAcc.class);
		Assert.assertNotNull(wxAcc);
	}

	@Test
	public void addMenu二级() {//测试    更新微信菜单字段   的逻辑
		WxAccMenu 一级菜单 = wxAcc.getMenuList().get(1);
		WxAccMenu newMenu = new WxAccMenu();
		newMenu.setName("新增二级text菜单");
		newMenu.setKey("new");
		newMenu.setType(MENU_TYPE.click);

		wxAccService.saveOrUpdateMenu(wxAcc, 一级菜单, newMenu);

		wxAcc = mongoTemplate.findOne(
				new Query().addCriteria(Criteria.where("menuList.sub_button.name").is("新增二级text菜单")), WxAcc.class);
		Assert.assertNotNull(wxAcc);
	}

	@Test
	public void testAdd一级菜单() {//测试    更新微信菜单字段   的逻辑
		WxAccMenu newMenu = new WxAccMenu();
		newMenu.setName("新增一级text菜单");
		newMenu.setKey("new");
		newMenu.setType(MENU_TYPE.click);

		wxAccService.saveOrUpdateMenu(wxAcc, null, newMenu);

		wxAcc = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("menuList.name").is("新增一级text菜单")),
				WxAcc.class);
		Assert.assertNotNull(wxAcc);

		Assert.assertEquals(3, wxAcc.getMenuList().size());
	}

	@Test
	public void test更新二级菜单() {//测试    更新微信菜单字段   的逻辑
		WxAccMenu textMenu = wxAcc.getMenuList().get(1).getSub_button().get(0);
		textMenu.setName("更新的二级text菜单");

		wxAccService.saveOrUpdateMenu(wxAcc, null, textMenu);

		wxAcc = mongoTemplate.findOne(
				new Query().addCriteria(Criteria.where("menuList.sub_button.name").is("更新的二级text菜单")), WxAcc.class);
		Assert.assertNotNull(wxAcc);

		Assert.assertEquals(2, wxAcc.getMenuList().size());
	}

	@Test
	public void updateMenuRespMessage() {//测试    更新微信菜单响应消息   的逻辑
		WxAccMenu textMenu = wxAcc.getMenuList().get(1).getSub_button().get(0);
		RespMessage textMessage = textMenu.getRespMessage();
		textMessage.setContent("测试text message");

		wxAccService.saveOrUpdateMenuRespMsg(wxAcc, textMenu, textMessage);

		wxAcc = mongoTemplate
				.findOne(
						new Query().addCriteria(Criteria.where("menuList.sub_button.respMessage.Content").is(
								"测试text message")), WxAcc.class);
		Assert.assertNotNull(wxAcc);
	}

	@Test
	public void updateMenuRespMessage图文消息() {//测试    更新微信菜单响应消息   的逻辑
		WxAccMenu textMenu = wxAcc.getMenuList().get(0);
		RespMessage newsMessage = new RespMessage();
		newsMessage.setMsgType(MSG_TYPE.news);
		newsMessage.getArticles().add(new RespArticle("title1", "Des", "pic1", "http://1"));
		newsMessage.getArticles().add(new RespArticle("title2", "Des", "pic2", "http://2"));
		newsMessage.getArticles().add(new RespArticle("title3", "Des", "pic3", "http://3"));
		textMenu.setRespMessage(newsMessage);

		wxAccService.saveOrUpdateMenuRespMsg(wxAcc, textMenu, newsMessage);

		Query query = new Query().addCriteria(Criteria.where("menuList.respMessage.Articles.Title").is("title1"));
		System.out.println(query);
		wxAcc = mongoTemplate.findOne(query, WxAcc.class);
		Assert.assertNotNull(wxAcc);
	}

}