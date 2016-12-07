package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.AdverBean;

public interface IAdverServic {
    public AdverBean getAdver(String condition);
    public Vector getAdverList(String condition);
    public boolean updateAdver(String set, String condition);
    public int getAdverCount(String condition);
	public boolean deleteAdver(String condition);
	public boolean addAdvice(String condition);
}
