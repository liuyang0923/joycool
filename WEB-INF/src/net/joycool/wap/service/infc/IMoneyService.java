/*
 * 作者:马长青
 * 日期:2006-7-24
 * 功能:实现统计现金流操作数据库方法接口
 */
package net.joycool.wap.service.infc;

import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.bean.money.MoneyFlowRecordBean;
import net.joycool.wap.bean.money.MoneyFlowTypeBean;
import net.joycool.wap.bean.money.MoneyFluxBean;

public interface IMoneyService {
	//现金流类型表
	public MoneyFlowTypeBean getMoneyFlowType(String condition);
	public HashMap getMoneyFlowTypeMap(String condition);
	public boolean addMoneyFlowType(MoneyFlowTypeBean bean);
	public boolean delMoneyFlowType(String condition);
	public boolean updateMoneyFlowType(String set, String condition);
	public int getMoneyFlowTypeCount(String condition);
	//现金流日志表
	public MoneyFlowRecordBean getMoneyFlowRecord(String condition);
	public Vector getMoneyFlowRecordList(String condition);
	public boolean addMoneyFlowRecord(MoneyFlowRecordBean bean);
	public boolean delMoneyFlowRecord(String condition);
	public boolean updateMoneyFlowRecord(String set, String condition);
	public int getMoneyFlowRecordCount(String condition);
	//现金流每天总量表
	public MoneyFluxBean getMoneyFlux(String condition);
	public Vector getMoneyFluxList(String condition);
	public boolean addMoneyFlux(MoneyFluxBean bean);
	public boolean delMoneyFlux(String condition);
	public boolean updateMoneyFlux(String set, String condition);
	public int getMoneyFluxCount(String condition);
	public void calculateEveryDayMoneyFlux();
}
