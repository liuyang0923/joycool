package jc.exam.bag;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.SqlUtil;

public class ExamAction extends CustomAction {

	public static ExamService service = new ExamService();
	public static LinkedHashMap subjectTypeMap = null;	// 所有科目的Map
	
	public static String typeString[] = {"","数学","语文","英语","物理","化学","生物","历史","政治",
		                                      "地理","数学->公式大全","数学->基本定理","数学->经典例题","语文->背诵默写","语文->文学常识",
		                                      "语文->范文例句","英语->词汇表","英语->语法大全","英语->范文例句","物理->常量","物理->公式大全",
		                                      "物理->基本定理","物理->经典例题","化学->公式大全","化学->基本定理","化学->常量","化学->经典实验"};

	public static byte[] initLock = new byte[0];
	
	public static String[] getTypeString() {
		return typeString;
	}
	
	public ExamAction(){}
	
	public ExamAction(HttpServletRequest request){
		super(request);
		subjectTypeMap = getSubjectTypeMap();
	}
	
	// 取得所有科目
	public static LinkedHashMap getSubjectTypeMap() {
		if (subjectTypeMap != null){
			return subjectTypeMap;
		}
		synchronized (initLock){
			if (subjectTypeMap != null){
				return subjectTypeMap;
			}
			// 所有的基础科目
			subjectTypeMap = service.getTypeMap("1");
		}
		return subjectTypeMap;
	}
	
	// 返回页面顶部的科目连接
	public String getSubjectTop(String pageName,int subjectId,boolean addAnd){
		StringBuffer topLink = new StringBuffer();	//主科目
		StringBuffer subLink = new StringBuffer();	//子科目
		int subId = 0;								//子科目ID
		ExamType exType = (ExamType)subjectTypeMap.get(new Integer(subjectId));
		if (exType.getHypo() != 0){
			subjectId = exType.getHypo();
			subId = exType.getId();
		}
		Iterator iter = subjectTypeMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next(); 
			Integer key = (Integer)entry.getKey(); 
			if (key.intValue() == subjectId){										 //主科目
				exType = this.getTypeFromMap(key.intValue());
				topLink.append(exType.getTypeName() + "|");
				// 当前科目的类型
				this.setAttribute("currentType", exType.getId());
			} else if (key.intValue() == subId){							 		 //子科目
				exType = this.getTypeFromMap(key.intValue());
				if ("".equals(subLink.toString())){
					subLink.append(">>");
				}
				subLink.append(exType.getTypeName() + "|");
				// 当前科目的类型
				this.setAttribute("currentType", exType.getId());
			} else if (this.getTypeFromMap(key.intValue()).getHypo() == 0){	     //主科目
				exType = this.getTypeFromMap(key.intValue());
				if (addAnd){
					topLink.append("<a href=\"" + pageName + "&amp;s=" + exType.getId() + "\">" + exType.getTypeName() + "</a>|");
				} else {
					topLink.append("<a href=\"" + pageName + "?s=" + exType.getId() + "\">" + exType.getTypeName() + "</a>|");
				}
			} else if (this.getTypeFromMap(key.intValue()).getHypo() == subjectId){ //子科目
				exType = this.getTypeFromMap(key.intValue());
				if ("".equals(subLink.toString())){
					subLink.append(">>");
				}
				if (subId == 0){
					subLink.append(exType.getTypeName() + "|");
					subId = exType.getHypo();
					// 当前科目的类型
					this.setAttribute("currentType", exType.getId());
				} else {
					if (addAnd){
						subLink.append("<a href=\"" + pageName + "&amp;s=" + exType.getId() + "\">" + exType.getTypeName() + "</a>|");
					} else {
						subLink.append("<a href=\"" + pageName + "?s=" + exType.getId() + "\">" + exType.getTypeName() + "</a>|");
					}
				}
			}
		}
		topLink.append("<br/>");
		if (!"".equals(subLink.toString())){
			topLink.append(subLink.toString());
			topLink.append("<br/>");
		}
		return topLink.toString();
	}
	
	// 写入书包
	public boolean writeToBag(ExamBag bag){
		boolean result = false;
		bag.setTitle(bag.getTitle()!=null?bag.getTitle().trim():"");
		if (bag.getTitle() == null || "".equals(bag.getTitle()) || bag.getTitle().length() > 20){
			this.setAttribute("tip", "标题太长或为空.");
			return result;
		}
		if (bag.getTitle() == null || "".equals(bag.getContent())){
			this.setAttribute("tip", "内容不可为空.");
			return result;
		}
		if (subjectTypeMap.get(new Integer(bag.getQueType())) == null){
			this.setAttribute("tip", "科目类型错误.");
			return result;
		}
		if (this.isCooldown("chat",15000)){
			result = service.addNewBag(bag);
		} else {
			this.setAttribute("tip", "你添加的太快了.");
			return result;
		}
		return result;
	}
	
	// 写入笔记 
	public boolean writeNote(ExamNote note){
		boolean result = false;
		if ("".equals(note.getContent()) || note.getContent().length() > 100){
			this.setAttribute("tip", "内容太长或为空.");
			return result;
		}
		if (service.getBag(" id = " + note.getBagId()) == null){
			this.setAttribute("tip", "书包中没有些信息.");
			return result;
		}
		if (this.isCooldown("chat",15000)){
			result = service.addNewNote(note);
		} else {
			this.setAttribute("tip", "你记录的太快了.");
			return result;
		}
		return result;
	}
	
	// 删除一条笔记[假删除]
	public boolean delNote(int noteId){
		boolean result = false;
		ExamNote note = service.getNote(" id =" + noteId);
		if (note == null){
			this.setAttribute("tip", "要删除的笔记不存在.");
			return result;
		}
		if (note.getUserId() != this.getLoginUser().getId()){
			this.setAttribute("tip", "不可删除他人的笔记.");
			return result;
		}
		result = SqlUtil.executeUpdate("update exam_note set del=1 where id=" + noteId, 5);
		return result;
	}
	
	// 删除书包中的一条记录[假删除]
	public boolean delBag(ExamBag bag){
		boolean result = false;
		if (bag == null){
			this.setAttribute("tip", "要删除的记录不存在.");
			return result;
		}
		if (bag.getUserId() != this.getLoginUser().getId()){
			this.setAttribute("tip", "不可删除他人的记录.");
			return result;
		}
		result = SqlUtil.executeUpdate("update exam_bag set del=1 where id=" + bag.getId(), 5);
		return result;
	}
	
	// 取得已使用备考插件的好友的列表
	public List getFriendList(){
		String friendString = this.getLoginUser().getFriendString();
		if ("".equals(friendString)){
			return null;
		} else {
			List friendBagList = service.getBagList(" user_id in (" + this.getLoginUser().getFriendString() + ") group by user_id");
			return friendBagList;
		}
	}
	
	// 修改题库
	public boolean modifyLib(ExamLib lib){
		boolean result = false;
		if ("".equals(lib.getTitle()) || lib.getTitle().length() > 20){
			this.setAttribute("tip", "标题太长或为空.");
			return result;
		}
		if ("".equals(lib.getContent()) || lib.getContent().length() > 1000){
			this.setAttribute("tip", "内容太长或为空.");
			return result;
		}
		return service.modifyLib(lib);
	}
	
	// 题库加题
	public boolean addLib(ExamLib lib){
		boolean result = false;
		if ("".equals(lib.getTitle()) || lib.getTitle().length() > 20){
			this.setAttribute("tip", "标题太长或为空.");
			return result;
		}
		if ("".equals(lib.getContent()) || lib.getContent().length() > 1000){
			this.setAttribute("tip", "内容太长或为空.");
			return result;
		}
		return service.addNewLib(lib);
	}
	
	// 修改用户书包
	public boolean modifyBag(ExamBag bag){
		boolean result = false;
		if ("".equals(bag.getTitle()) || bag.getTitle().length() > 20){
			this.setAttribute("tip", "标题太长或为空.");
			return result;
		}
		if ("".equals(bag.getContent()) || bag.getContent().length() > 1000){
			this.setAttribute("tip", "内容太长或为空.");
			return result;
		}
		return service.modifyBag(bag);
	}
	
	public String array2String(String[] strs, String split) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder(strs.length * 10);
		for (int i = 0; i < strs.length; i++) {
			if (i != 0) {
				builder.append(split);
			}
			builder.append(strs[i]);
		}
		return builder.toString();
	}
	
	public ExamType getTypeFromMap(int key){
		return (ExamType)subjectTypeMap.get(new Integer(key));
	}
}