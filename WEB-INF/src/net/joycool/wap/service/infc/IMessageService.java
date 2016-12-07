/*
 * Created on 2005-11-21
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.MessageBean;

/**
 * @author lbj
 *
 */
public interface IMessageService {
    public MessageBean getMessage(String condition);
    public boolean addMessage(MessageBean message);
    public boolean deleteMessage(String condition);
    public boolean updateMessage(String set, String condition);
    public Vector getMessageList(String condition);
    public int getMessageCount(String condition);
}
