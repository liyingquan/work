package cn.rkang.wxgate.ctrl.wxacc;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rkang.wxgate.model.WxAccMenu;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-xml/wxgate-common-context.xml",
		"classpath:/spring-xml/wxgate-mvc.xml" })
public class MenuJsonObjTest {
	@Resource
	MongoTemplate	mongoTemplate;

	@Test
	public void testGetButton() throws Exception {
		MenuJsonObj mjo = new MenuJsonObj();
		List<WxAccMenu> allMenuList = mongoTemplate.find(new Query(), WxAccMenu.class);

		mjo.setButton(allMenuList);
		String json = net.sf.json.JSONObject.fromObject(mjo).toString();
		System.out.println(json);
	}
}
