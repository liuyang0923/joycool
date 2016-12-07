package net.joycool.wap.spec.buyfriends;
/**
 * 记录某个用户的安抚和惩罚
 * @author leihy
 *
 */
public class BeanFlag {

	public boolean appease = false;//是否安抚过,true表示今天已经安抚过,0表示没安抚,1表示安抚过
	public boolean punish = false;//是否惩罚过,true表示今天已经惩罚过0表示没惩罚,1表示惩罚过
	
	public int masterUid;	//惩罚人的uid
	public int slaveUid;	//	被惩罚人(奴隶的)uid
	public int punishType;	//惩罚类型
	public int money;		//惩罚随机获得的金钱
	public long endTime;	//
	
	public void process(){
		ServiceMaster serviceMaster = ServiceMaster.getInstance();
		ServiceSlave serviceSlave = ServiceSlave.getInstance();
		//if(punish) {
		//if(punishType != 8) {	8表示身价大打折扣,主人的金钱增加 money数量
		if(endTime > 0) {
			serviceMaster.increaseMoneyByUid(masterUid, money, false);
			serviceSlave.deletePunish(slaveUid);
		}
		
			//}
		//}
	}

	
}
