package net.joycool.wap.action;

import java.util.List;

import net.joycool.wap.bean.LinkBean;
import net.joycool.wap.service.factory.ServiceFactory;

public class LinkAction {
	public static LinkBean getLink(int typeId, int moduleId, int subModuleId) {
		return ServiceFactory.createLinkService().getLink(
				"type_id=" + typeId + " and module_id=" + moduleId
						+ " and sub_module_id=" + subModuleId);
	}

	public static LinkBean getRandomLink(int typeId) {
		List randomList = ServiceFactory.createLinkService().getLinkList(
				"type_id=" + typeId + " and module_id="
						+ LinkBean.RANDOM_MODULE);

		if (randomList == null || randomList.size() < 1) {
			return null;
		}

		int index = (int) (Math.random() * randomList.size());
		return (LinkBean) randomList.get(index);
	}

	public static LinkBean getOneLink(int typeId, int moduleId, int subModuleId) {
		LinkBean ret = getLink(typeId, moduleId, subModuleId);
		if (ret == null) {
			ret = getRandomLink(typeId);
		}
		return ret;
	}
}
