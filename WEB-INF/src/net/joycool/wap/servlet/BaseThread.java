package net.joycool.wap.servlet;

import net.joycool.wap.action.fs.FSAction;
import net.joycool.wap.action.job.fish.FishAction;
import net.joycool.wap.action.pk.PKAction;
import net.joycool.wap.action.dhh.DhhAction;
import net.joycool.wap.action.dhh.DHHWorld;
import net.joycool.wap.action.pet.PetAction;
import net.joycool.wap.action.wgamehall.JinhuaAction;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.spec.castle.CastleUtil;
import net.joycool.wap.spec.farm.*;
import net.joycool.wap.spec.friend.PkgAction;
import net.joycool.wap.util.LogUtil;


public class BaseThread extends Thread {

	//线程最小运行间隔
	public static long SLEEP_TIME = 1000*60;
	
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		while(true){
			count++;
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				return;
			}
			LogUtil.logTime("BaseThread*****start");
			try{
				//复活怪兽
				PKAction.monstersRevive();
				//五分钟定时任务
				if(count%5==0){
					PKAction.timeTask();
					//浮生记
					FSAction.timeTask();

					FishAction.getWorld().timeTask();
					
					try{
						//大航海,恢复城市物品数量,价格
						DHHWorld.getWorld().resetCityMap();
						//大航海
						DhhAction.timeTask();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				//每一个小时清空砸金花游戏中的catch
				if(count%60 == 0)
				{
					JinhuaAction.gcCatch();
				}

				//每半小时对宠物更新数据库
				if(count%60 == 0)
				{
					PetAction.task();
				}
				
				// farm use
				try{
					FarmWorld.one.task(count);
				} catch(Exception e) {e.printStackTrace();}
				
				try{
					CacheManage.timeCheck();
				} catch(Exception e) {e.printStackTrace();}
				
				try{
					PkgAction.task();
				} catch(Exception e) {e.printStackTrace();}
				
				try{
					CastleUtil.task(count);
				} catch(Exception e) {e.printStackTrace();}
				
				//重置计算器
				if(count>100000000){
					count = 0;
				}

			}catch(Exception e){
				e.printStackTrace();
			}
			LogUtil.logTime("BaseThread*****end");
		}
	}

}
