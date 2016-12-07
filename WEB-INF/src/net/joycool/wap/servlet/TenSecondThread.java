package net.joycool.wap.servlet;

import net.joycool.wap.action.pet.ClimbAction;
import net.joycool.wap.action.pet.PetAction;
import net.joycool.wap.action.pet.StakeAction;
import net.joycool.wap.action.pet.SwimAction;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.SmsUtil;


public class TenSecondThread extends Thread {

	//线程最小运行间隔 10秒
	public static long SLEEP_TIME = 1000*10;
	
	public void run() {
		try {
			Thread.sleep(50000);
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
			if(count>100000000){
				count = 0;
			}
			LogUtil.logTime("TenSecondThread*****start");
			try{
				SmsUtil.recv();

			}catch(Exception e){
				e.printStackTrace();
			}
			LogUtil.logTime("TenSecondThread*****end");
		}
	}

}