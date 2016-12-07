/*
 * Created on 2005-11-28
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.JaLineBean;

/**
 * @author lbj
 *
 */
public interface IJaLineService {
    public JaLineBean getLine(String condition);
    public JaLineBean getLine(int id);
    public Vector getLineList(String condition);
    public boolean addLine(JaLineBean line);
    public boolean updateLine(JaLineBean line);
    public boolean updateLine(String set, String condition);
    public boolean deleteLine(String condition);
    public int getLineCount(String condition);
}
