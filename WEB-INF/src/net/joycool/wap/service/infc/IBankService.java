package net.joycool.wap.service.infc;

import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.bean.bank.AccountsBean;
import net.joycool.wap.bean.bank.LoadBean;
import net.joycool.wap.bean.bank.LoadLimitBean;
import net.joycool.wap.bean.bank.MoneyLogBean;
import net.joycool.wap.bean.bank.StoreBean;

public interface IBankService {
	// 存款
	public boolean addStore(StoreBean storeBean);

	public StoreBean getStore(String condition);

	public Vector getStoreList(String condition);

	public boolean deleteStore(String condition);

	public boolean updateStore(String set, String condition);

	public int getStoreCount(String condition);

	// 贷款
	public boolean addLoad(LoadBean loadBean, MoneyLogBean moneyLogBean);

	public LoadBean getLoad(String condition);

	public Vector getLoadList(String condition);

	public boolean deleteLoad(String condition);

	// fanys 2006-08-03
	public boolean deleteLoad(LoadBean loadBean, MoneyLogBean moneyLogBean);

	public boolean updateLoad(String set, String condition);

	public int getLoadCount(String condition);

	// 用户银行操作
	public boolean addMoneyLog(MoneyLogBean moneyLogBean);

	public MoneyLogBean getMoneyLog(String condition);

	public boolean deleteMoneyLog(String condition);

	public boolean updateMoneyLog(String set, String condition);

	public int getMoneyLogCount(String condition);

	// 银行每日帐目
	public boolean addAccounts(AccountsBean accountsBean);

	public AccountsBean getAccounts(String condition);

	public Vector getAccountsList(String condition);

	public boolean deleteAccounts(String condition);

	public boolean updateAccounts(String set, String condition);

	public int getAccountsCount(String condition);

	// 等级贷款上限
	public boolean addLoadLimit(LoadLimitBean loadLimitBean);

	public LoadLimitBean getLoadLimit(String condition);

	public Vector getLoadLimitList(String condition);

	public boolean deleteLoadLimit(String condition);

	public boolean updateLoadLimit(String set, String condition);

	public int getLoadLimitCount(String condition);

	// other special
	public HashMap getRankLoadMoneyMap();

	public int getLoadHour(String condition);
	
	//银行每日现金流
	public void addMoneyFlux(String date);
}
