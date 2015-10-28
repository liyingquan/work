package cn.rkang.wxgate.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

/**
 * zk通用工具类
 */
public class ZkUtils {
	static Logger	logger	= Logger.getLogger(ZkUtils.class);

	/**
	 * 枚举类型：创建新的tab或window时的模式
	 */
	public enum OverFlowType {
		AUTO("overflow:auto"), SCROLL("overflow:scroll"), HIDDEN("overflow:hidden"), VISIBLE("overflow:visible");
		private final String	text;

		private OverFlowType(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	public static Tabpanel newTab(TabVo tabVo, String url, Tabs mainTabs, Tabpanels mainTabpanels,
			OverFlowType overFlowType, Map map) throws TabDuplicateException {
		Tab tabNew = tabVo.cloneTab();
		return constructNewTabAndPanel(tabNew, url, mainTabs, mainTabpanels, overFlowType, map);
	}

	/**
	 * 创建新tab页签
	 * @param tabNew
	 * @param url
	 * @param mainTabs
	 * @param mainTabpanels
	 * @param overFlowType
	 * @param map
	 * @return Tabpanel
	 * @throws TabDuplicateException
	 */
	@SuppressWarnings({ "rawtypes" })
	protected static Tabpanel constructNewTabAndPanel(Tab tabNew, String url, Tabs mainTabs, Tabpanels mainTabpanels,
			OverFlowType overFlowType, Map map) throws TabDuplicateException {

		List<Component> tabs = mainTabs.getChildren();
		if (tabs.size() == 15) {
			Messagebox.show(Labels.getLabel("common.zkutils.info.tooManyTabs"), "", Messagebox.OK,
					Messagebox.INFORMATION);//为提高您的机器性能，请勿打开超过15个页签！
			return null;
		}
		for (Component comp : tabs) {
			Tab tab = (Tab) comp;
			if (tab.getId().equals(tabNew.getId())) {
				String errorMsg = "打开已存在页签【" + tabNew.getId() + "|" + tab.getLabel() + "】！";
				logger.warn(errorMsg);
				tab.setSelected(true);
				throw new TabDuplicateException(errorMsg, tab);

				//				logger.warn("重新打开已存在页签【" + tabNew.getId() + "|" + tab.getLabel() + "】！");
				//				tab.close();
				//				break;
			}
		}

		//因部分zul url需要传递参数，故构建TabPanel时需要将url后的参数截断
		if (map == null) {
			map = new HashMap();
		}
		url = processZulUrlWithParam(url, map);

		tabNew.setSelected(true);
		if (map.get("tabIconSclass") != null) {
			tabNew.setIconSclass(map.get("tabIconSclass").toString());
		}
		mainTabs.appendChild(tabNew);

		Tabpanel tabpanel = new Tabpanel();
		if (overFlowType != null) {
			tabpanel.setStyle(overFlowType.getText());
		}
		mainTabpanels.appendChild(tabpanel);
		Executions.createComponents(url, tabpanel, map).setPage(tabpanel.getPage());
		return tabpanel;
	}

	/**
	 * 处理新页签zul后面跟的参数
	 * @param url
	 * @param map 参数名-参数值
	 * @return 去掉参数后缀的zul的url
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String processZulUrlWithParam(String url, Map map) {
		int indexOfQueryMark = url.indexOf("?");
		if (indexOfQueryMark > -1) {
			String paramPairString = url.substring(indexOfQueryMark + 1);
			url = url.substring(0, indexOfQueryMark);
			String[] paramPairArray = paramPairString.split("&");
			if (paramPairArray.length > 0) {
				for (String paramPair : paramPairArray) {
					int indexOfEqMark = paramPair.indexOf("=");
					if (indexOfEqMark > -1) {
						String[] paramArray = paramPair.split("=");
						if (paramArray.length > 0) {
							logger.debug("param标识符为【" + paramArray[0] + "】，param值为【" + paramArray[1] + "】");
							map.put(paramArray[0], paramArray[1]);
						}
					}
				}
			}
		}
		return url;
	}

	/**
	 * 弹出提示框：保存成功
	 */
	public static void popupSaveSuccess() {
		Messagebox.show("保存成功！", "保存提示", Messagebox.OK, Messagebox.INFORMATION, null);
	}
}