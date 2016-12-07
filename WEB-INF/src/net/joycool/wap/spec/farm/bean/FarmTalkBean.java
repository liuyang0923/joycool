package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.joycool.wap.spec.farm.FarmNpcWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 聊天对话数据，暂时只有npc有
 * @datetime:1007-10-24
 */
public class FarmTalkBean {
	int id;
	String title;
	String content;
	String link;		// 该内容的后续内容
	int questBegin;		// 这段对话可以开始的任务
	int questEnd;		// 这段对话可以结束的任务
	int quest;		// 这段对话只有当接受某个任务后才能看到
	int preQuest;		// 先修任务，完成后才能看到这个聊天
	List linkList = null;
	String condition = "";		// 完成任务的条件
	List conditionList;
	public void init() {
		conditionList = StringUtil.toIntss(condition);
	}

	public void initLink(FarmNpcWorld world) {
		linkList = new ArrayList(4);
		List l = StringUtil.toInts(link);
		for(int j = 0;j < l.size();j++) {
			Integer iid = (Integer)l.get(j);
			linkList.add(world.getTalk(iid));
		}
	}
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the link.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link The link to set.
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the links.
	 */
	public List getLinkList() {
		return linkList;
	}

	public int getQuestBegin() {
		return questBegin;
	}

	public void setQuestBegin(int questBegin) {
		this.questBegin = questBegin;
	}

	public int getQuestEnd() {
		return questEnd;
	}

	public void setQuestEnd(int questEnd) {
		this.questEnd = questEnd;
	}

	public int getQuest() {
		return quest;
	}

	public void setQuest(int quest) {
		this.quest = quest;
	}

	public int getPreQuest() {
		return preQuest;
	}

	public void setPreQuest(int preQuest) {
		this.preQuest = preQuest;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List getConditionList() {
		return conditionList;
	}
}
