//20070207 liq
package net.joycool.wap.bean.question;

import java.util.HashMap;
import java.util.List;

public interface QuestionService {
	
	public  HashMap  getAllQuestionWareHouse(int grade);
	
	public int getTotalValue(int id);
	
	public int getTodayValue(int id);
	
	public void setValue(int id,int totalvalue,int todayvalue);
	
	public int getOrderTotal(String value,int int_value) ;
	
	public List getTotalList();
	
	public List getTodayList();
	
	public void setNullToday();
}