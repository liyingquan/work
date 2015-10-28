package cn.rkang.wxgate.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 微信服务号model
 */
@Document(collection = WxAcc.TABLE_NAME)
public class WxAcc {
	public static final String	TABLE_NAME	= "WxAcc";

	@Id
	String						id;

	String						appId;										//appId
	String						appSecret;									//appSercret
	String						url;										//服务号对应服务的URL 如http://s2idfy2xz9.proxy.qqbrowser.cc/wxgate
	String						token;										//口令 ：用于绑定公众号时校验

	String						accNo;										//微信号
	String						accName;									//服务号名称
	String						accOrgName;								//对应的机构名称

	String						merchantNo;								//商户编号
	String						merchantPayKey;							//商户支付密钥

	boolean						disabled	= false;
	Date						crtDate;
	Date						lastUpdate;
	Date						lastSynchTimestamp;

	List<WxAccMenu>				menuList	= new ArrayList<WxAccMenu>();

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccOrgName() {
		return accOrgName;
	}

	public void setAccOrgName(String accOrgName) {
		this.accOrgName = accOrgName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantPayKey() {
		return merchantPayKey;
	}

	public void setMerchantPayKey(String merchantPayKey) {
		this.merchantPayKey = merchantPayKey;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Date getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	public List<WxAccMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<WxAccMenu> menuList) {
		this.menuList = menuList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLastSynchTimestamp() {
		return lastSynchTimestamp;
	}

	public void setLastSynchTimestamp(Date lastSynchTimestamp) {
		this.lastSynchTimestamp = lastSynchTimestamp;
	}

}