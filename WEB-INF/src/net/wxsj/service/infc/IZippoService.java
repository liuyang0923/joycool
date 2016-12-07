/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.service.infc;

import java.util.ArrayList;

import net.wxsj.bean.ZippoBean;
import net.wxsj.bean.ZippoMobileBean;
import net.wxsj.bean.ZippoPairBean;
import net.wxsj.bean.ZippoStarBean;
import net.wxsj.bean.ZippoTypeBean;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public interface IZippoService extends IBaseService {
    public ZippoTypeBean getZippoType(String condition);
    
    public ArrayList getZippoTypeList(String condition, int index, int count,
            String orderBy);

    public ZippoBean getZippo(String condition);

    public ArrayList getZippoList(String condition, int index, int count,
            String orderBy);

    public ZippoMobileBean getZippoMobile(String condition);

    public boolean addZippoMobile(ZippoMobileBean zippoMobile);

    public ZippoStarBean getZippoStar(String condition);
    
    public ArrayList getZippoStarList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getZippoPairList(String condition, int index, int count,
            String orderBy);

    public ZippoPairBean getZippoPair(String condition);

    public boolean addZippoPair(ZippoPairBean zippoPair);
}
