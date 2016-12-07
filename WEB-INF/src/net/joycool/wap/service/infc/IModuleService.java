package net.joycool.wap.service.infc;

import java.util.List;

import net.joycool.wap.bean.ModuleBean;

public interface IModuleService {
	
		public boolean addModule(ModuleBean moduleBean);

		public ModuleBean getModule(String condition);

		public List getModuleList(String condition);

		public boolean deleteModule(String condition);

		public boolean updateModule(String set, String condition);

		public int getModuleCount(String condition);

}
