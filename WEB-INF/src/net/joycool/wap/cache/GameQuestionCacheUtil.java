package net.joycool.wap.cache;

import java.util.HashMap;
import java.util.Random;

import net.joycool.wap.bean.question.QuestionService;
import net.joycool.wap.bean.question.QuestionWareHouseBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.util.RandomUtil;

public class GameQuestionCacheUtil {

	private static HashMap diffMap;

	private static HashMap easyMap;

	/*
	 * (non-Javadoc)
	 * 
	 * 根据题难度的不同,返回题的答案
	 */
	public static QuestionWareHouseBean getQuestionWareHouse(String id) {
		if (Integer.parseInt(id) < 10000)
			return (QuestionWareHouseBean) easyMap.get(new Integer(Integer
					.parseInt(id)));
		else
			return (QuestionWareHouseBean) diffMap.get(new Integer(Integer
					.parseInt(id)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 根据题难度的不同,返回全部题库
	 */
	public static HashMap questionMap(int grade) {
		if (grade == 1) {
			if (diffMap != null)
				return diffMap;
			else {
				diffMap = new HashMap();
				QuestionService questionService = ServiceFactory
						.createQuestionService();
				diffMap = questionService.getAllQuestionWareHouse(1);

				easyMap = new HashMap();
				easyMap = questionService.getAllQuestionWareHouse(0);

				return diffMap;
			}
		} else {
			if (easyMap != null)
				return easyMap;
			else {
				easyMap = new HashMap();
				QuestionService questionService = ServiceFactory
						.createQuestionService();
				easyMap = questionService.getAllQuestionWareHouse(0);
				diffMap = new HashMap();
				diffMap = questionService.getAllQuestionWareHouse(1);
				return easyMap;
			}
		}
		// doing map_diff put ket value database
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 根据题难度的不同,返回题目
	 */
	public static QuestionWareHouseBean getQuestionWareHouse(int grade) {
		HashMap tempMap = questionMap(grade);
		QuestionWareHouseBean questionWareHouse;
		if (grade == 0) {
			questionWareHouse = (QuestionWareHouseBean) tempMap.get(new Integer(RandomUtil.nextInt(tempMap.size())));
			return questionWareHouse;
		} else {
			questionWareHouse = (QuestionWareHouseBean) tempMap.get(new Integer(10000 + RandomUtil.nextInt(tempMap.size())));
			return questionWareHouse;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 清空题库缓存
	 */
	public static void SetHashMapNull () {
		easyMap = null;
		diffMap = null;
	}
}
