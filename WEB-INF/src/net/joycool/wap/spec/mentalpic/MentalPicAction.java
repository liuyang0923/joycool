package net.joycool.wap.spec.mentalpic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;

public class MentalPicAction extends CustomAction{

	public static MentalPicService service = new MentalPicService();
	
	public String picPath = Constants.RESOURCE_ROOT_PATH + "mental/";
	public String picUrlPath = Constants.RESOURCE_ROOT_URL + "mental/";
	
	public String getPicUrlPath() {
		return picUrlPath;
	}

	public String getPicPath() {
		return picPath;
	}

	public MentalPicAction(){}
	
	public MentalPicAction(HttpServletRequest request){
		super(request);
	}
	
	// 写入看文字读图题
	public boolean updateWordPic(SmartUpload smUpload){
		boolean result = false;
		
//		if ("".equals(path)){
//			this.setAttribute("tip", "没写保存的路径.");
//			return result;
//		}
		
		String title = smUpload.getRequest().getParameter("title").trim();
		
		if (title == null || "".equals(title)){
			this.setAttribute("tip", "没有输入标题?");
			return result;
		}
		if (title.length() > 255){
			this.setAttribute("tip", "标题太长了.");
			return result;
		}
		
		String content = smUpload.getRequest().getParameter("content").trim();
		
		if (content == null || "".equals(content)){
			this.setAttribute("tip", "没有输入内容?");
			return result;
		}
		if (content.length() > 500){
			this.setAttribute("tip", "内容太长了.");
			return result;
		}
		
		String analyse[] = new String[smUpload.getFiles().getCount()];
		List filesList = new ArrayList(smUpload.getFiles().getCount());
		String tmpAnalyse = "";
		File upFile = null;
		
		// 检查输入的分析
		for (int i=0;i<analyse.length;i++){
			analyse[i] = smUpload.getRequest().getParameter("analyse" + (i+1)).trim();
			if (analyse[i] == null || "".equals(analyse[i]) || analyse[i].length() > 500){
				this.setAttribute("tip", "没有输入分析?");
				return result;
			}
		}
		
		for (int i=0;i<smUpload.getFiles().getCount();i++){
			upFile = smUpload.getFiles().getFile(i);
			tmpAnalyse = smUpload.getRequest().getParameter("analyse" + (i+1));
			if ("".equals(upFile.getFileName())){
				this.setAttribute("tip", "没有选择文件?");
				return result;
			} else {
				filesList.add(upFile);
				analyse[i] = tmpAnalyse;
			}
		}
		
		// 插入题目
		MentalPicQuestion question = new MentalPicQuestion();
		question.setTitle(title);
		question.setContent(content);
		question.setContentPic("");
		question.setDel(0);
		question.setFlag(0);
		
		int lastId = this.service.addQuestion(question);
		
		if (lastId > 0){
			// 插入选项
			MentalPicOption option = null;
			String fileName = "";
			try {
				for (int i =0;i<filesList.size();i++){
					option =  new MentalPicOption();
					upFile = (File)filesList.get(i);
					fileName = System.currentTimeMillis() + i + "." + upFile.getFileExt();
					// 将图片上传之
					upFile.saveAs(picPath + fileName,smUpload.SAVE_PHYSICAL);
					option.setQueId(lastId);
					option.setOption(fileName);			// 图片完整名子
					option.setAnalyse(analyse[i]);		// 答案
					option.setDel(0);
					option.setFlag(0);
					result = this.service.addOption(option);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}
		} else {
			// 没插进去
			this.setAttribute("tip", "上传出错.");
			return result;
		}
		
		return result;
	}
	
	// 写入看图读文字题
	public boolean updatePicWord(SmartUpload smUpload,int count){
		boolean result = false;
		
//		if (count <=0 || "".equals(path)){
//			this.setAttribute("tip", "没写保存的路径或选项总数为0.");
//			return result;
//		}
		if (count <=0){
			this.setAttribute("tip", "选项总数为0.");
			return result;
		}
		
		String title = smUpload.getRequest().getParameter("title").trim();
		
		if (title == null || "".equals(title)){
			this.setAttribute("tip", "没有输入标题?");
			return result;
		}
		if (title.length() > 255){
			this.setAttribute("tip", "标题太长了.");
			return result;
		}
		
		String content = smUpload.getRequest().getParameter("content").trim();
		
		if (content == null || "".equals(content)){
			this.setAttribute("tip", "没有输入内容?");
			return result;
		}
		if (content.length() > 500){
			this.setAttribute("tip", "内容太长了.");
			return result;
		}
		
		File upFile = smUpload.getFiles().getFile(0);
		if ("".equals(upFile.getFileName())){
			this.setAttribute("tip", "没有选择文件?");
			return result;
		}
		
		String opt[] = new String[count];
		String analyse[] = new String[count];
		for (int i=0;i<count;i++){
			opt[i] = smUpload.getRequest().getParameter("opt" + (i+1)).trim();
			analyse[i] = smUpload.getRequest().getParameter("analyse" + (i+1)).trim();
			if ("".equals(opt[i]) || opt[i].length() > 255){
				this.setAttribute("tip", "没有填写选项,或选数太多.");
				return result;
			}
			if ("".equals(analyse[i]) || analyse[i].length() > 500){
				this.setAttribute("tip", "没有填写解析,或字数太多.");
				return result;
			}
		}
		
		int lastId = 0;
		
		// 插入题目
		try {
			MentalPicQuestion question = new MentalPicQuestion();
			String fileName = System.currentTimeMillis() + "." + upFile.getFileExt();
			// 将图片上传之
			upFile.saveAs(picPath + fileName,smUpload.SAVE_PHYSICAL);
			question.setTitle(title);
			question.setContent(content);
			question.setContentPic(fileName);			// 图片完整名子
			question.setDel(0);
			question.setFlag(1);
			lastId = this.service.addQuestion(question);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
		
		if (lastId > 0){
			// 插入选项
			MentalPicOption option = null;
			for (int i=0;i<count;i++){
				option = new MentalPicOption();
				option.setQueId(lastId);
				option.setOption(opt[i]);	
				option.setAnalyse(analyse[i]);
				option.setDel(0);
				option.setFlag(0);
				result = this.service.addOption(option);
			}

		} else {
			// 没插进去
			this.setAttribute("tip", "上传出错.");
			return result;
		}
		
		return result;
	}
	
	// 修改看图读文字题
	public boolean modifyPicWord(SmartUpload smUpload,MentalPicQuestion question,List optionList){
		boolean result = false;
		
		if (question == null){
			this.setAttribute("tip", "要修改的题目不存在。");
			return result;
		}
		
//		if ("".equals(path)){
//			this.setAttribute("tip", "没写保存的路径.");
//			return result;
//		}
		
		String title = smUpload.getRequest().getParameter("title").trim();
		
		if (title == null || "".equals(title)){
			this.setAttribute("tip", "没有输入标题?");
			return result;
		}
		if (title.length() > 255){
			this.setAttribute("tip", "标题太长了.");
			return result;
		}
		
		String content = smUpload.getRequest().getParameter("content").trim();
		
		if (content == null || "".equals(content)){
			this.setAttribute("tip", "没有输入内容?");
			return result;
		}
		if (content.length() > 500){
			this.setAttribute("tip", "内容太长了.");
			return result;
		}
		
		String opt[] = new String[optionList.size()];
		String analyse[] = new String[optionList.size()];
		for (int i=0;i<optionList.size();i++){
			opt[i] = smUpload.getRequest().getParameter("opt" + (i+1)).trim();
			analyse[i] = smUpload.getRequest().getParameter("analyse" + (i+1)).trim();
			if ("".equals(opt[i]) || opt[i].length() > 255){
				this.setAttribute("tip", "没有填写选项,或选数太多.");
				return result;
			}
			if ("".equals(analyse[i]) || analyse[i].length() > 500){
				this.setAttribute("tip", "没有填写解析,或字数太多.");
				return result;
			}
		}
		
		// 插入题目
		try {
			if (question == null || question.getFlag() != 1){
				this.setAttribute("tip", "要修改的题目不存在,或题目类型错误.");
				return result;
			} else {
				// 如果选择了新图片，则上传
				File upFile = smUpload.getFiles().getFile(0);
				if (!"".equals(upFile.getFileName())){
					String fileName = System.currentTimeMillis() + "." + upFile.getFileExt();
					// 将图片上传之
					upFile.saveAs(picPath + fileName,smUpload.SAVE_PHYSICAL);
					question.setContentPic(fileName);			// 图片完整名子
				}
				question.setTitle(title);
				question.setContent(content);
				result = this.service.modifyQuestion(question);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
		
		if (result){
			// 插入选项
			MentalPicOption option = null;
			for (int i=0;i<optionList.size();i++){
				option = (MentalPicOption)optionList.get(i);
				option.setOption(opt[i]);	
				option.setAnalyse(analyse[i]);
				result = this.service.modifyOption(option);
			}
		} else {
			// 没插进去
			this.setAttribute("tip", "上传出错.");
			return result;
		}
		
		return result;
	}
	
	// 修改看文字读图题
	public boolean modifyWordPic(SmartUpload smUpload,MentalPicQuestion question,List optionList){
		boolean result = false;
		
		if (question == null){
			this.setAttribute("tip", "要修改的题目不存在。");
			return result;
		}
		
//		if ("".equals(path)){
//			this.setAttribute("tip", "没写保存的路径.");
//			return result;
//		}
		
		String title = smUpload.getRequest().getParameter("title").trim();
		
		if (title == null || "".equals(title)){
			this.setAttribute("tip", "没有输入标题?");
			return result;
		}
		if (title.length() > 255){
			this.setAttribute("tip", "标题太长了.");
			return result;
		}
		
		String content = smUpload.getRequest().getParameter("content").trim();
		
		if (content == null || "".equals(content)){
			this.setAttribute("tip", "没有输入内容?");
			return result;
		}
		if (content.length() > 500){
			this.setAttribute("tip", "内容太长了.");
			return result;
		}
		
		String analyse[] = new String[optionList.size()];
		for (int i=0;i<optionList.size();i++){
			analyse[i] = smUpload.getRequest().getParameter("analyse" + (i+1)).trim();
			if ("".equals(analyse[i]) || analyse[i].length() > 500){
				this.setAttribute("tip", "没有填写解析,或字数太多.");
				return result;
			}
		}
		
		// 插入题目
		if (question == null || question.getFlag() != 0){
			this.setAttribute("tip", "要修改的题目不存在,或题目类型错误.");
			return result;
		} else {
			question.setTitle(title);
			question.setContent(content);
			result = this.service.modifyQuestion(question);
		}
		
		if (result){
			// 插入选项
			MentalPicOption option = null;
			File upFile = null;
			try {
				for (int i=0;i<optionList.size();i++){
					option = (MentalPicOption)optionList.get(i);
					upFile = smUpload.getFiles().getFile(i);
					if (!"".equals(upFile.getFileName())){
						String fileName = System.currentTimeMillis() + "." + upFile.getFileExt();
						// 将图片上传之
						upFile.saveAs(picPath + fileName,smUpload.SAVE_PHYSICAL);
						option.setOption(fileName);			// 图片完整名子
					}
					option.setAnalyse(analyse[i]);
					result = this.service.modifyOption(option);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}
		} else {
			// 没插进去
			this.setAttribute("tip", "上传出错.");
			return result;
		}
		
		return result;
	}
	
	// 删除一道题
	public boolean delQuestion(int queId){
		boolean result = false;
		MentalPicQuestion question = this.service.getQuestion(" id=" + queId + " and del=0");
		if (question == null){
			this.setAttribute("tip", "要删除的题目不存在,或已被删除.");
			return result;
		}
		result = SqlUtil.executeUpdate("update mental_pic_question set del=1 where id=" + question.getId(), 4);
		result = SqlUtil.executeUpdate("update mental_pic_option set del=1 where que_id=" + question.getId(), 4);
		return result;
	}
	
	// 看是否经过了一定的时间。若经过，则返回True。如果判断是否经过7天，则pastTime应写“INTERVAL 7 DAY”。
	public boolean pastTime(MentalPicUser mentalUser,String pastTime){
		long time = this.service.getTimePastDays(pastTime, mentalUser);
		return System.currentTimeMillis() - time >= 0;
	}
	
	/**
	 * 答题。如果mentalUser==null，说明是第一次答题，否则视为半年后重新答题。
	 * @return MentalPicUser mentalUser
	 */
	public MentalPicOption answerQuestion(MentalPicQuestion question,List optionList,int answerId,MentalPicUser mentalUser){
		if (question == null || optionList == null){
			this.setAttribute("tip", "参数错误.");
			return null;
		}
		if (answerId < 1 || answerId > optionList.size()){
			this.setAttribute("tip", "答案ID错误.");
			return null;
		}
		if (mentalUser != null){
			mentalUser.setSelected(answerId);
			this.service.modifyUser(mentalUser);
		} else {
			mentalUser = new MentalPicUser();
			mentalUser.setUserId(this.getLoginUser().getId());
			mentalUser.setQueId(question.getId());
			mentalUser.setFlag(0);
			mentalUser.setSelected(answerId);
			this.service.addUser(mentalUser);
		}
		return (MentalPicOption)optionList.get(answerId-1);
	}
}
