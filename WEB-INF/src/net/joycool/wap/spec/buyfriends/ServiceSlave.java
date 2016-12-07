package net.joycool.wap.spec.buyfriends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;

public class ServiceSlave {

	private static ServiceSlave serviceSlave = new ServiceSlave();
	
	public static ServiceSlave getInstance(){
		
		if(serviceSlave == null) {
			synchronized(ServiceSlave.class) {
				if(serviceSlave == null)
					serviceSlave = new ServiceSlave();
			}
		}
		return serviceSlave;
	}
	
	private ServiceSlave(){}
	
	DAOSlave slaveDAO = new DAOSlave();
	
	static ICacheMap buyFriendSlave = CacheManage.buyFriendSlave;
	
	
	public boolean updateSlaveFrozenTime(int uid, long frozenTime) {
		return slaveDAO.updateSlaveFrozenTime(uid, frozenTime);
	}
	
	/**
	 * 删除临时奴隶，即超过24小时的奴隶
	 * @return
	 */
	public boolean deleteTempSlave(){
		return slaveDAO.deleteTempSlave();
	}
	
	/**
	 * 增加一个奴隶
	 * @param slave
	 * @return
	 */
	public boolean addSlave(BeanSlave slave) {
		buyFriendSlave.spt(new Integer(slave.getSlaveUid()), slave);
		
		return slaveDAO.addSlave(slave);
	}
	
	/**
	 * 删除masterUid的一个奴隶
	 * @param masterUid
	 * @param slaveUid
	 * @return
	 */
	public boolean deleteSlave(int masterUid, int slaveUid) {
		
		buyFriendSlave.srm(new Integer(slaveUid));
		
		return slaveDAO.deleteSlave(masterUid, slaveUid);
	}
	
	
	/**
	 * 得到我的所有奴隶
	 * @param masterUid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getSlavesByUid(int masterUid, int start, int limit) {
		ServiceMaster serviceMaster = ServiceMaster.getInstance();
		List list = slaveDAO.getSlavesByUid(masterUid, start, limit);
		
		List slaveList = new ArrayList();
		
		Iterator it = list.iterator();
		
		while(it.hasNext()) {
			int slaveUid = ((Integer)it.next()).intValue();
			
			BeanSlave slave = this.getSlaveBySlaveUid(slaveUid);
			
			BeanMaster master = serviceMaster.getMasterByUid(slaveUid);
			
			slave.setMoney(master.getMoney());
			slave.setPrice(master.getPrice());
			
			slaveList.add(slave);
		}
		
		return slaveList;
	}
	
	public int getSlavesCountByUid(int masterUid) {
		return slaveDAO.getSlavesCountByUid(masterUid);
	}
	
	public BeanSlave getSlaveBySlaveUid(int slaveUid){
		
		BeanSlave slave = (BeanSlave)buyFriendSlave.sgt(new Integer(slaveUid));
		
		if(slave == null) {
			slave = slaveDAO.getSlaveBySlaveUid(slaveUid);
			if( slave != null && slave.getRank() > 0) {
				buyFriendSlave.spt(new Integer(slaveUid), slave);
			}
			
		}
		
		return slave;
	}
	
	public boolean deletePunish(int slaveUid){
		return slaveDAO.deletePunish(slaveUid);
	}
	
	public List getAllPunish() {
		return slaveDAO.getAllPunish();
	}
	
	public boolean isPunish(int masterUid, int slaveUid) {
		return slaveDAO.isPunish(masterUid, slaveUid);
	}
	
	
	public boolean isAppease(int masterUid, int slaveUid) {
		return slaveDAO.isAppease(masterUid, slaveUid);
	}
	
	public boolean updatePunish(BeanFlag bean) {
		return slaveDAO.updatePunish(bean);
	}
	
	public boolean addPunish(BeanFlag bean) {
		return slaveDAO.addPunish(bean);
	}
	
	public boolean isPunish(int slaveUid) {
		return slaveDAO.isPunish(slaveUid);
	}
	
	public BeanFlag getBeanFlag(int slaveUid) {
		return slaveDAO.getBeanFlag(slaveUid);
	}
}
