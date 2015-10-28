package cn.rkang.wxgate.model;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-xml/wxgate-common-context.xml",
		"classpath:/spring-xml/wxgate-mvc.xml" })
public class WxAccTest {

	private Logger			log	= Logger.getLogger(WxAccTest.class);

	@Autowired
	private MongoTemplate	mongoTemplate;

	@Test
	public void 测试添加服务号() {
		//		WxAcc wxAcc = new WxAcc();
		//
		//		wxAcc.setAppId("wxfabea7f9f92255f7");
		//		wxAcc.setAppSecret("a742241156ec66ab4294e9dad63a9842");
		//		wxAcc.setToken("weixintest");
		//
		//		wxAcc.setAccNo("gh_e9dae6e39ab9");
		//		wxAcc.setAccName("李英权测试账号");
		//		wxAcc.setAccOrgName("上海沐康网络科技");
		//
		//		wxAcc.setCrtDate(new Date());
		//		wxAcc.setLastUpdate(new Date());
		//
		//		wxAcc.setMerchantNo("e5e31b759");
		//		wxAcc.setMerchantPayKey("fdfdggg2");
		//
		//		mongoTemplate.save(wxAcc);

		WxAcc wxAcc2 = new WxAcc();

		wxAcc2.setAppId("wx482b1e5e31b759e1");
		wxAcc2.setAppSecret("23b51125bcea225e0b6d7ed528649baa");
		wxAcc2.setToken("weixintest");
		wxAcc2.setUrl("http://assa.aas/wx482b1e5e31b75000");

		wxAcc2.setAccNo("wx123691");
		wxAcc2.setAccName("康有为");
		wxAcc2.setAccOrgName("上海沐康网络科技");

		wxAcc2.setCrtDate(new Date());
		wxAcc2.setLastUpdate(new Date());

		wxAcc2.setMerchantNo("e5e31b759");
		wxAcc2.setMerchantPayKey("fdfdggg2");

		mongoTemplate.save(wxAcc2);
	}

}
