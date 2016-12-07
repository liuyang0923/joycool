package net.joycool.wap.spec.castle;

import java.util.Iterator;
import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.util.SqlUtil;

public class MerchantBean {

	static CastleService castleService = CastleService.getInstance();
	static CacheService cacheService = CacheService.getInstance();
	int id;
	int fromCid;	// 注意，不论是出发还是返回，fromcid是商人的拥有方城堡
	int toCid;
	int wood;
	int fe;
	int grain;
	int stone;
	int count;
	long startTime;
	long endTime;
	int type;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public MerchantBean() {}
	public MerchantBean(int fromCid, int toCid, int wood, int fe, int grain,
			int stone, int count, int fromX, int fromY, int toX, int toY, int speed) {
		this.fromCid = fromCid;
		this.toCid = toCid;
		this.wood = wood;
		this.fe = fe;
		this.grain = grain;
		this.stone = stone;
		this.count = count;
		this.startTime = System.currentTimeMillis();
		
		float distance = CastleUtil.calcDistance(fromX - toX, fromY - toY);
		//TODO 计算商人的行走时间
		long interval = (long) (distance / speed * 3600000);
		if(SqlUtil.isTest)	interval = 10000;
		endTime = startTime + interval;
	}
	public MerchantBean(int fromCid, int toCid, int type, int res, int count, int fromX, int fromY, int toX, int toY, int speed) {
		this.fromCid = fromCid;
		this.toCid = toCid;
		switch(type) {
		case 1:
			this.wood = res;
			break;
		case 2:
			this.stone = res;
			break;
		case 3:
			this.fe = res;
			break;
		case 4:
			this.grain = res;
			break;
		}
		this.count = count;
		this.startTime = System.currentTimeMillis();
		
		float distance = (float) Math.sqrt((fromX - toX) * (fromX - toX)
				 + (fromY - toY) * (fromY - toY));
		//TODO 计算商人的行走时间
		long interval = (long) (distance / speed * 3600000);
//		interval = 60000;
		endTime = startTime + interval;
	}
	
	public void execute(){
		UserResBean userRes = CastleUtil.getUserResBeanById(toCid);
		if(type == 0 ) {
			long now = System.currentTimeMillis();
			CastleBean toCastle = CastleUtil.getCastleById(toCid);
			CastleBean fromCastle = CastleUtil.getCastleById(fromCid);
			if(userRes != null && fromCastle != null && toCastle != null) {
				CastleUtil.increaseUserRes(userRes, wood, fe, grain, stone);
				
				String content = fromCastle.getCastleNameWml() + "补给" + toCastle.getCastleNameWml();
				
				StringBuilder sb = new StringBuilder(100);
				
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(fromCid);
				sb.append("\">");
				sb.append(fromCastle.getCastleNameWml());
				sb.append("</a>");
				
				sb.append("补给");
				
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(toCid);
				sb.append("\">");
				sb.append(toCastle.getCastleNameWml());
				sb.append("</a>");
				
				sb.append(":木");
				sb.append(wood);
				sb.append("石");
				sb.append(stone);
				sb.append("铁");
				sb.append(fe);
				sb.append("粮");
				sb.append(grain);
				
				String detail = sb.toString();
				;
				CastleUtil.addDetailMsg(fromCastle.getUid(), 1, content, detail);
				if(toCastle.getUid() != fromCastle.getUid())
					CastleUtil.addDetailMsg(toCastle.getUid(), 1, content, detail);
			}
			long interval = now - startTime;
			startTime = endTime;
			endTime = endTime + interval;
			type = 1;
			cacheService.updateMerchant(this);
		} else {
			
			UserResBean fromUserRes = CastleUtil.getUserResBeanById(fromCid);
			if(fromUserRes != null) {
				synchronized(fromUserRes) {
					fromUserRes.addMerchant(-count);
					castleService.updateUserMerchant(fromCid, fromUserRes.getMerchant());
				}
			}
			cacheService.deleteMerchant(id);
		}
	}
	
	public static void run() {
		List list = CacheService.getAllCacheMerchant();
		Iterator it = list.iterator();
		
		while(it.hasNext()) {
			MerchantBean bean = (MerchantBean)it.next();
			bean.execute();
		}
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getFromCid() {
		return fromCid;
	}
	public void setFromCid(int fromCid) {
		this.fromCid = fromCid;
	}
	public int getToCid() {
		return toCid;
	}
	public void setToCid(int toCid) {
		this.toCid = toCid;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public int getFe() {
		return fe;
	}
	public void setFe(int fe) {
		this.fe = fe;
	}
	public int getGrain() {
		return grain;
	}
	public void setGrain(int grain) {
		this.grain = grain;
	}
	public int getStone() {
		return stone;
	}
	public void setStone(int stone) {
		this.stone = stone;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
