/*
 * Created on 2005-12-26
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.guestbook.BoardBean;
import net.joycool.wap.bean.guestbook.ContentBean;

/**
 * @author lbj
 *
 */
public interface IGuestbookService {
    public BoardBean getBoard(String condition);
    public ContentBean getContent(String condition);
    public Vector getContentList(String condition);
    public boolean addContent(ContentBean content);
    public int getContentCount(String condition);
}
