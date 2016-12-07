package net.joycool.wap.service.infc;

import java.util.List;

import net.joycool.wap.bean.LinkBean;

public interface ILinkService {
	
	public boolean addLink(LinkBean bean);

	public boolean updateLink(String set, String condition);

	public boolean deleteLink(String condition);

	public LinkBean getLink(String condition);

	public List getLinkList(String condition);

}
