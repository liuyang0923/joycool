/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.ebook.EBookBean;

/**
 * @author lbj
 *
 */
public interface IEBookService {
    
    public EBookBean getEBook(String condition);
    public Vector getEBooksList(String condition);
    public boolean updateEBook(String set, String condition);
    public int getEBooksCount(String condition);

}
