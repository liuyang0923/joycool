package jc.guest.farmer;

import javax.servlet.http.*;

import net.joycool.wap.util.RandomUtil;
import jc.guest.GuestHallAction;

public class FarmAction extends GuestHallAction{
	public FarmAction (HttpServletRequest request, HttpServletResponse response) {
		super(request,response);
	}
	public static FarmerService service = new FarmerService();
	
	public boolean walk(FarmGameBean bean, int aim){
		if(bean == null) 
			return false;
		int[] farm = bean.getFarm();
		int something = farm[aim];
		int index = bean.getIndex();
		if (something == 1) {
			if(canWalk(farm,index,aim,bean.getSize())) {
				// 走一步种一个麦子,玩家位置改变到aim
				bean.getFarm()[index] = 2;
				bean.getFarm()[aim] = 3;
				bean.setIndex(aim);
				bean.setSteps(bean.getSteps() + 1);
			}
		} else if (something == 0 && bean.getBombs() > 0) {
			if(canWalk(farm,index,aim,bean.getSize())) {
				// 走一步种一个麦子,玩家位置改变到aim
				bean.getFarm()[index] = 2;
				bean.getFarm()[aim] = 3;
				bean.setIndex(aim);
				bean.setSteps(bean.getSteps() + 1);
				// 可走石头次数减1
				bean.setBombs(bean.getBombs() - 1);
			}
		} else {
			return false;
		}
		return true;
	}

	// 从index 是否可以走到aim
	public boolean canWalk (int[] farm, int index, int aim, int size) {
		int temp = aim - index;
		if(index == 0) { // 左上角
			if (temp == 1 || temp == size)
				return true;
		} else if (index == size - 1) { // 右上角
			if (temp == -1 || temp == size)
				return true;
		} else if (index == farm.length - size) { // 左下角
			if (temp == 1 || temp == -size)
				return true;
		} else if (index == farm.length - 1) { // 右下角
			if (temp == -1 || temp == -size)
				return true;
		} else if (index < size) { // 上边
			if (Math.abs(temp) == 1 || temp == size)
				return true;
		} else if (index > farm.length - size) { // 下边
			if (Math.abs(temp) == 1 || temp == -size)
				return true;
		} else if (index % size == 0) { // 左边
			if (temp == 1 || Math.abs(temp) == size)
				return true;
		} else if ((index + 1) % size == 0) { // 右边
			if (temp == -1 || Math.abs(temp) == size)
				return true;
		} else {
			if (Math.abs(temp) == 1 || Math.abs(temp) == size)
				return true;
		}
		return false;
	}
	
	// 获得农场地图  0:空 1:麦 2:石 3:玩家
	public int[] getFarm (int size, int stones, int bombs) {
		int endIndex = size*size; // 数组总大小
		int [] farm = new int [endIndex]; // 保存农场地图
        int[] times = new int[endIndex]; // 记录每个点剩余的可走次数
        int[][] range = new int[endIndex][]; // 记录每个点剩余的可走范围
        int[] from = new int[endIndex]; // 记录每个点的来路点
        // 确定玩家初始地点 // 0 石头, 1空地, 2麦, 3玩家
		farm[size*(size - 1)] = 3;
    	// 左上角走向没减1&减6,右上角走向没减6&加1,左下角走向没减1&加6,右下角走向没加1&加6,上边栏走向没减6,下边栏走向没加6,左边栏走向没减1,右边栏走向没加1
    	for (int i = 0; i < endIndex; i++) {
    		if(i == 0){
    			range[i] = new int[] {1,6,-1,-6};
    			times[i] = 2;
    		} else if(i == size - 1) {
    			range[i] = new int[] {-1,6,1,-6};
    			times[i] = 2;
    		} else if(i == endIndex - size) {
    			range[i] = new int[] {1,-6,-1,6};
    			times[i] = 2;
    		} else if(i == endIndex - 1) {
    			range[i] = new int[] {-6,-1,6,1};
    			times[i] = 2;
    		} else if(i < size - 1) {
    			range[i] = new int[] {1,-1,6,-6};
    			times[i] = 3;
    		} else if(i > endIndex - size) {
    			range[i] = new int[] {1,-1,-6,6};
    			times[i] = 3;
    		} else if(i % size == 0) {
    			range[i] = new int[] {1,-6,6,-1};
    			times[i] = 3;
    		} else if((i + 1) % size == 0) {
    			range[i] = new int[] {-1,-6,6,1};
    			times[i] = 3;
    		} else {
    			range[i] = new int[] {1,-1,6,-6};
    			times[i] = 4;
    		}
    	}
    	designFarm (farm,from,times,range,size,stones,bombs);
    	return farm;
	}
	
    // 设计地图
    public int[] designFarm(int[] farm, int[] from,int[] times, int[][] range, int size, int stones, int bombs) {
    	int steps = 0;
    	int endIndex = size*size;
    	int index = endIndex - size;
    	// 一共挖endIndex - stones - 1个石头,即走了多少步
    	while (steps < endIndex - stones - 1) {
    		int temp = digStone(farm, index,times,range,from);
    		// 返回的temp为下一步走的点;返回-1，表示没路了,返回上一步继续
    		if(temp != -1) {
    			//System.out.println(index+"="+temp);
    			index = temp;
    			steps++;
    		} else {
    			index = from[index];
    		}
    	}
    	// 最后填入bombs个石头
    	dropStone(farm,bombs);
    	return farm;
    }
    
    // 挖石头形成一条路(即完美解)
    public int digStone(int[] farm, int index, int[] times, int[][] range, int[] from) {
    	int gole = 0;
    	while (farm[index + gole] != 0) {
        	if(times[index] < 0)
        		return -1;
        	else if(times[index] == 0) {
        		gole = range[index][0];
            	times[index]--;
        	} else {
            	int random = RandomUtil.nextInt(times[index]);
            	int[] currentRange = range[index];
            	gole = currentRange[random];
            	currentRange[random] = currentRange[times[index] - 1];
            	currentRange[times[index] - 1] = gole;
            	times[index]--;
        	}
    	}
    	farm[index + gole] = 1;
		from[index + gole] = index;
		index += gole;
    	return index;
    }
	
	// 随机放入石头
	public int[] dropStone(int[] farm,int num) {
		while (num > 0) {
			int index = RandomUtil.nextInt(farm.length);
			if(farm[index] == 1) {
				farm[index] = 0;
				num--;
			}
		}
		return farm;
	}
	
	public FarmGameBean initiateFarmGame(int size, int stones, int bombs, int uid, int endIndex){
		FarmGameBean fgbean = new FarmGameBean();
		fgbean.setUid(uid);
		fgbean.setSteps(1);
		fgbean.setSize(size);
		fgbean.setBombs(bombs);
		fgbean.setIndex(endIndex - size);
		fgbean.setFarm(this.getFarm(size,stones,bombs));
		return fgbean;
	}
	
	// 前台把最新一条完美得主信息展示给用户看
	public FarmerBean getNotice() {
		FarmerBean bean = service.getFarmerBean("change_lv=1 order by change_time desc limit 1");
		return bean;
	}
	
	// 完成游戏保存数据库
	public boolean smtFarmer(FarmGameBean bean) {
		if (bean == null || bean.getUid() <= 0)
			return false;
		int steps = bean.getSteps();
		long changeTime = System.currentTimeMillis();
		if (steps == 30) {
			service.upd("insert into farmer_user (uid,lv1_num,change_lv,change_time) values ("+bean.getUid()+",1,1,"+changeTime+") ON DUPLICATE KEY update lv1_num = lv1_num+1,change_lv=1,change_time="+changeTime);
		} else if (steps >= 25 && steps < 30) {
			service.upd("insert into farmer_user (uid,lv2_num,change_lv,change_time) values ("+bean.getUid()+",1,2,"+changeTime+") ON DUPLICATE KEY update lv2_num = lv2_num+1,change_lv=2,change_time="+changeTime);
		} else if (18 <= steps && steps < 25) {
			service.upd("insert into farmer_user (uid,lv3_num,change_lv,change_time) values ("+bean.getUid()+",1,3,"+changeTime+") ON DUPLICATE KEY update lv3_num = lv3_num+1,change_lv=3,change_time="+changeTime);
		} else {
			service.upd("insert into farmer_user (uid,lv4_num,change_lv,change_time) values ("+bean.getUid()+",1,4,"+changeTime+") ON DUPLICATE KEY update lv4_num = lv4_num+1,change_lv=4,change_time="+changeTime);
		}
		return true;
	}
	
}
