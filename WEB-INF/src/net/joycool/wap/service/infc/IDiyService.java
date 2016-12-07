/*
 * Created on 2005-12-19
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.DiyBean;

/**
 * @author lbj
 *
 */
public interface IDiyService {
    public Vector getDiyList(String condition);
    public DiyBean getDiy(String condition);
}
