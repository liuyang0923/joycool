package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.chat.ChatStatBean;

public interface IChatStatService {
	public boolean addChatStat(ChatStatBean chatStat);

	public ChatStatBean getChatStat(String condition);

	public Vector getChatStatList(String condition);

	public boolean deleteChatStat(String condition);

	public boolean updateChatStat(String set, String condition);

	public int getChatStatCount(String condition);
	

}
