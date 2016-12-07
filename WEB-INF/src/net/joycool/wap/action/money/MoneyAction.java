/**
 * 
 */
package net.joycool.wap.action.money;

import java.util.HashMap;

import net.joycool.wap.bean.money.MoneyFlowRecordBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMoneyService;

/**
 * @author Administrator
 * 
 */
public class MoneyAction {

	private static IMoneyService moneyService = ServiceFactory
			.createMoneyService();

	public MoneyAction() {
	}

	private static HashMap moneyFlowTypeMap;

	public static HashMap getMoneyFlowTypeMap() {
		if (moneyFlowTypeMap != null) {
			return moneyFlowTypeMap;
		}

		moneyFlowTypeMap = moneyService.getMoneyFlowTypeMap(null);

		return moneyFlowTypeMap;
	}

	public void clearMoneyFlowTypeMap() {
		this.moneyFlowTypeMap = null;
	}

	// liuyi 2007-01-03 现金流统计修改 start
	public static boolean addMoneyFlowRecord(int typeId, long money, int mark,
			int userId) {
		return true;/*
		if (money != 0) {
			MoneyFlowRecordBean bean = new MoneyFlowRecordBean();
			bean.setTypeId(typeId);
			bean.setMoney(money);
			bean.setMark(mark);
			bean.setUserId(userId);
			return moneyService.addMoneyFlowRecord(bean);
		}
		return false;*/
	}

	// liuyi 2007-01-03 现金流统计修改 end

	public void addMoneyFlux() {
		moneyService.calculateEveryDayMoneyFlux();
	}

	public void delMoneyFlowRecord() {
		moneyService
				.delMoneyFlowRecord("to_days(now()) - to_days(create_datetime)>10");
	}

}
