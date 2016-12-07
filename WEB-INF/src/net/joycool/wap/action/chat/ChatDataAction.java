/*
 * Created on 2006-3-17
 *
 */
package net.joycool.wap.action.chat;

import java.util.Hashtable;
import java.util.Vector;

import net.joycool.wap.bean.chat.MessageBean;


/**
 * @author lbj
 * 
 */
public class ChatDataAction {
	static int MAX_LENGTH = 1000;

	static int PRIVATE_MAX_LENGTH = 80;

	static Vector messageList;

	static Vector rumorList;

	static Hashtable privateList;


	/**
	 * 取得私人信息列表
	 * 
	 * @return
	 */
	public static Hashtable getPrivateMessageList() {
		if (privateList == null) {
			privateList = new Hashtable();
		}
		return privateList;
	}

	/**
	 * 取得谣言列表
	 * 
	 * @return
	 */
	public static Vector getRumorList() {
		if (rumorList == null) {
			rumorList = new Vector();
		}
		return rumorList;
	}

	/**
	 * 取得消息列表。
	 * 
	 * @return
	 */
	public static Vector getMessageList() {
		if (messageList == null) {
			messageList = new Vector();
		}
		return messageList;
	}
    
	/**
	 * 加入一条消息。
	 * 
	 * @param message
	 */
	public static void addMessage(MessageBean message) {
		if(message.getIsPrivate() == 1){
			return;
		}
		Vector ms = getMessageList();
		synchronized (ms) {
			if (ms.size() >= MAX_LENGTH) {
				ms.remove(MAX_LENGTH - 1);
				ms.insertElementAt(message, 0);
			} else {
				ms.insertElementAt(message, 0);
			}
		}
	}

	/**
	 * 加入一条私人消息。
	 * 
	 * @param message
	 */
	public static void addPrivateMessage(MessageBean message) {
		Hashtable pms = getPrivateMessageList();
		if (!pms.containsKey(message.getFromUserName())) {
			Vector pmsv = new Vector();
			pmsv.insertElementAt(message, 0);
			pms.put(message.getFromUserName(), pmsv);

		} else {
			Vector pmsl = (Vector) pms.get(message.getFromUserName());
			if (pmsl.size() >= PRIVATE_MAX_LENGTH) {
				pmsl.remove(PRIVATE_MAX_LENGTH - 1);
				pmsl.insertElementAt(message, 0);
			} else {
				pmsl.insertElementAt(message, 0);
			}
		}
		if(message.getToUserName()==null){
			return;
		}
		if (!pms.containsKey(message.getToUserName())) {		
			Vector pmsv = new Vector();
			pmsv.insertElementAt(message, 0);
			pms.put(message.getToUserName(), pmsv);
		} else {
			Vector pmsl = (Vector) pms.get(message.getToUserName());
			if (pmsl.size() >= PRIVATE_MAX_LENGTH) {
				pmsl.remove(PRIVATE_MAX_LENGTH - 1);
				pmsl.insertElementAt(message, 0);
			} else {
				pmsl.insertElementAt(message, 0);
			}
		}
	}
	/**
	 * 加入一条谣言。
	 * 
	 * @param rumor
	 */
	public static void addRumor(String rumor) {
		Vector rs = getRumorList();
		synchronized (rs) {
			if (rs.size() >= MAX_LENGTH) {
				rs.remove(MAX_LENGTH - 1);
				rs.insertElementAt(rumor, 0);
			} else {
				rs.insertElementAt(rumor, 0);
			}
		}
	}
}
