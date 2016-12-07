/*
 * Created on 2006-2-16
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.ring.PringBean;
import net.joycool.wap.bean.ring.PringFileBean;
/**
 * @author MCQ
 *
 */
public interface IRingService {
    
    public PringBean getPring(String condition);
    public Vector getPringsList(String condition);
    public boolean updatePring(String set, String condition);
    public int getPringsCount(String condition);
    public PringFileBean getPring_file(String condition);
    public Vector getPring_filesList(String condition);
    public boolean updatePring_file(String set, String condition);
    public int getPring_filesCount(String condition);
}
