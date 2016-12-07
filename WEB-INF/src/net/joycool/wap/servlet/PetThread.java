package net.joycool.wap.servlet;

import net.joycool.wap.action.pet.ClimbAction;
import net.joycool.wap.action.pet.PetAction;
import net.joycool.wap.action.pet.StakeAction;
import net.joycool.wap.action.pet.SwimAction;
import net.joycool.wap.spec.rich.RichAction;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.SmsUtil;


/**
 * @author bomb
 * 宠物线程
 */
public class PetThread extends Thread {

	//线程最小运行间隔 15秒
	public static long SLEEP_TIME = 1000*10;
	
	public void run() {
		try {
			Thread.sleep(55000);
		} catch (InterruptedException e) {
			return;
		}
		int count = 0;
		while(true){
			count++;
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				return;
			}
			LogUtil.logTime("PetThread*****start");
			if(count % 2 == 0) {
	//			10秒跑一次,宠物游戏线程
				try{
					PetAction.runtask();
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					SwimAction.swimtask();
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					StakeAction.staketask();
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					ClimbAction.climbtask();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			try{
				RichAction.timer();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try {
				Runtime rt = Runtime.getRuntime();
				LogUtil.logDebug(rt.freeMemory()/1024/1024 + " - " + rt.totalMemory()/1024/1024 + " - " + rt.maxMemory()/1024/1024);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(count>100000000){
				count = 0;
			}
			LogUtil.logTime("PetThread*****end");
		}
	}

}