package cn.rkang.wxgate.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.rkang.wxgate.model.RespMessage;
import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.model.WxAccMenu;
import cn.rkang.wxgate.model.WxAccMenu.MENU_TYPE;

@Service
public class WxAccService {

	private Logger			logger	= Logger.getLogger(WxAccService.class);

	@Autowired
	private MongoTemplate	mongoTemplate;

	/**
	 * 查询所有的服务号
	 */
	public List<WxAcc> getAllWxAcc() {
		List<WxAcc> wxAccList = mongoTemplate.findAll(WxAcc.class);
		return wxAccList;
	}

	/**
	 * 根据appId查询公众号
	 * @param appId
	 * @return
	 */
	public WxAcc getWxAccByAppId(String appId) {
		Query query = new Query().addCriteria(Criteria.where("appId").is(appId));
		WxAcc wxAcc = mongoTemplate.findOne(query, WxAcc.class);
		if (wxAcc == null) {
			logger.warn("查询不到WxAcc " + query);
		}
		return wxAcc;
	}

	public RespMessage findRespMessageById(String messageId) {
		return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("id").is(messageId)), RespMessage.class);
	}

	public void saveOrUpdateMenuRespMsg(WxAcc wxAcc, WxAccMenu menu, RespMessage message) {
		if (menu.getRespMessage() == null) {
			menu.setRespMessage(message);
		} else {
			BeanUtils.copyProperties(message, menu.getRespMessage());
		}

		mongoTemplate.save(wxAcc);
	}

	public void saveOrUpdateMenu(WxAcc wxAcc, WxAccMenu parentMenu, WxAccMenu menu) {
		if (parentMenu == null && menu.getParentId() != null) {
			for (WxAccMenu wam : wxAcc.getMenuList()) {
				if (StringUtils.equals(menu.getParentId(), wam.getId())) {
					parentMenu = wam;
					break;
				}
			}
		}

		if (parentMenu != null && !parentMenu.getSub_button().contains(menu)) {
			parentMenu.getSub_button().add(menu);
			menu.setParentId(parentMenu.getId());
		}
		if (parentMenu == null && !wxAcc.getMenuList().contains(menu)) {
			wxAcc.getMenuList().add(menu);
		}

		if (menu.getId() == null) {
			menu.setId(UUID.randomUUID().toString());
		}

		if (menu.getType() == MENU_TYPE.view) {
			menu.setKey(null);
		}
		if (menu.getType() == MENU_TYPE.click) {
			menu.setUrl(null);
		}

		mongoTemplate.save(wxAcc);
	}

	public void updateAccLastSynchTimestamp(String appId, Date date) {
		WxAcc wa = this.getWxAccByAppId(appId);
		wa.setLastSynchTimestamp(date);
		mongoTemplate.save(wa);
	}

	public boolean deleteMenu(String appId, WxAccMenu menu) {
		WxAcc wa = this.getWxAccByAppId(appId);
		if (menu.getParentId() != null) {
			WxAccMenu parentMenu = this.searchMenuById(wa.getMenuList(), menu.getParentId());
			if (parentMenu == null) {
				logger.warn("menu不存在" + menu.getParentId());
				return false;
			}
			WxAccMenu menuToDelete = this.searchMenuById(parentMenu.getSub_button(), menu.getId());
			if (menuToDelete == null) {
				logger.warn("menu不存在" + menu.getId());
				return false;
			}
			parentMenu.getSub_button().remove(menuToDelete);
			mongoTemplate.save(wa);
			return true;
		} else {
			WxAccMenu menuToDelete = this.searchMenuById(wa.getMenuList(), menu.getId());
			if (menuToDelete == null) {
				logger.warn("menu不存在" + menu.getId());
				return false;
			}
			wa.getMenuList().remove(menuToDelete);
			mongoTemplate.save(wa);
			return true;
		}
	}

	private WxAccMenu searchMenuById(List<WxAccMenu> list, String menuId) {
		Iterator<WxAccMenu> it = list.iterator();
		while (it.hasNext()) {
			WxAccMenu next = it.next();
			if (StringUtils.equals(next.getId(), menuId)) {
				return next;
			}
		}
		return null;
	}
}