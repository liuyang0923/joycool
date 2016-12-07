/**
 * 
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.beginner.BeginnerHelpBean;
import net.joycool.wap.bean.beginner.BeginnerQuestionBean;

/**
 * @author macq 2006-12-23 新手答题系统
 */
public interface IBeginnerService {
	//新手问答
	public BeginnerQuestionBean getBeginnerQuestion(String condition);

	public Vector getBeginnerQuestionList(String condition);

	public boolean addBeginnerQuestion(BeginnerQuestionBean bean);

	public boolean delBeginnerQuestion(String condition);

	public boolean updateBeginnerQuestion(String set, String condition);

	public int getBeginnerQuestionCount(String condition);
	
	//管理员和热心用户
	public BeginnerHelpBean getBeginnerHelp(String condition);

	public Vector getBeginnerHelpList(String condition);

	public boolean addBeginnerHelp(BeginnerHelpBean bean);

	public boolean delBeginnerHelp(String condition);

	public boolean updateBeginnerHelp(String set, String condition);

	public int getBeginnerHelpCount(String condition);

}
