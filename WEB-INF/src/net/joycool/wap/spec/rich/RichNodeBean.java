package net.joycool.wap.spec.rich;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 大富翁每个结点的数据
 * @datetime:1007-10-24
 */
public class RichNodeBean {
	public final static int TYPE_START = 0;
	public final static int TYPE_HOUSE = 1;		// 房子
	public final static int TYPE_PARK = 2;		// 公园
	public final static int TYPE_POINT = 3;		// 得到点券
	public final static int TYPE_BANK = 4;		// 银行
	public final static int TYPE_SHOP = 5;		// 商店
	public final static int TYPE_HOSPITAL = 6;	// 医院
	public final static int TYPE_JAIL = 7;		// 监狱
	public final static int TYPE_CARD = 8;		// 奖励随机卡片
	public final static int TYPE_NEWS = 9;		// 发生随机事件
	public final static int TYPE_MONEY = 10;		// 拣到钱……
	public final static int TYPE_BUSINESS = 11;	// 商业用地
	public final static int TYPE_MAGIC = 12;		// 命运巫婆
	public final static int TYPE_SPY = 13;		// 间谍点，查看别人财产
	public final static int TYPE_COURT = 14;		// 法院审判
	public final static int TYPE_GAME1 = 15;		// 游戏1，猜数字，16个数字，每次去掉猜的一边，二分法，最后中的奖励点券
	public final static int TYPE_GAME2 = 16;		// 游戏2，赌大小，一人压一人解，谁赢谁得奖励
	public final static int TYPE_GAME3 = 17;		// 游戏3，赌1-4，输的钱加进奖励，赢的拿走
	
	
	public static String[] typeNames = {"空地", "空地", "公园", "点券", "银行", "商店", "医院", "监狱", "卡片", "新闻", "中奖", "空地", "魔法屋", "间谍屋", "法院", "猜数字", "对决屋", "彩票"};
	public static String[] eventNames = {"", "路障", "地雷", "定时炸弹", "财神", "福神", "衰神", "穷神", "土地公公", "天使", "狗", "宝箱", "宝箱", "小偷", "强盗"};
	public static int[] eventTypes = {0, 2, 2, 3, 1, 1, 1, 1, 1, 1, 2, 0, 0, 2, 2};
	
	int id;
	int type = 0;		// 格子类型 0 空地 1 房子
	int event = 0;		// 此格子的随机事件 0 无
	int value;			// 如果这个格子是获得点券，获得数量由这个保存
	
	public HouseBean house = null;
	public RichBuilding building = null;
	
	public List userList = new ArrayList();		// 在这个格子上的所有玩家
	
	public List next = new ArrayList();
	public List prev = new ArrayList();
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean hasNext() {
		return next.size() > 0;
	}
	public boolean hasPrev() {
		return prev.size() > 0;
	}
	
	public RichNodeBean getNext() {
		if(next.size() == 0)
			return null;
		return (RichNodeBean)next.get(0);
	}
	public RichNodeBean getPrev() {
		if(prev.size() == 0)
			return null;
		return (RichNodeBean)prev.get(0);
	}
	public RichNodeBean randomNext() {
		return (RichNodeBean)RandomUtil.randomObject(next);
	}
	public RichNodeBean randomPrev() {
		return (RichNodeBean)RandomUtil.randomObject(prev);
	}
	/**
	 * @return Returns the event.
	 */
	public int getEvent() {
		return event;
	}
	// 检查是否可以放置道具
	public boolean putItem() {
		return event == 0 && userList.size() == 0;
	}
	/**
	 * @param event The event to set.
	 */
	public void setEvent(int event) {
		this.event = event;
	}
	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * 用于显示的名称
	 */
	public String getDesc() {
		StringBuilder sb = new StringBuilder(16);
		sb.append(typeNames[type]);
		switch(type) {
		case TYPE_POINT:
			sb.append(value);
			break;
		case TYPE_HOUSE:
		case TYPE_BUSINESS:
			sb.setLength(0);
			if(house.getOwner() != null) {
				sb.append(house.getLevelName());
				sb.append(house.getOwner().getRoleMark());
			} else if(house.getLevel() > 0) {
				sb.append(house.getLevelName());
			} else {
				sb.append(typeNames[type]);
			}
			break;
		}
		if(next.size() > 1 || prev.size() > 1)
			sb.append("(岔路)");
		if(event > 0) {
			sb.append("[");
			sb.append(eventNames[event]);
			sb.append("]");
		}
		if(userList.size() > 0) {
			sb.append("|");
			sb.append(userList.size());
			sb.append("人");
		}
		return sb.toString();
	}
	
	/**
	 * 用于显示详细信息
	 */
	public String getDetail(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder(64);
		switch(type) {
		case TYPE_POINT:
			sb.append(typeNames[type]);
			sb.append(value);
			break;
		case TYPE_HOUSE:
		case TYPE_BUSINESS:
			if(house.noOwner())
				sb.append(house.getLevelName());
			else if(house.getLevel() > 0)
				sb.append(house.getLevelName());
			else
				sb.append(typeNames[type]);
			break;
		default:
			sb.append(typeNames[type]);
		}
		if(next.size() > 1)
			sb.append("(岔路)");
		if(event > 0) {
			sb.append("[");
			sb.append(eventNames[event]);
			sb.append("]");
		}
		sb.append("<br/>");
		if(type == TYPE_HOUSE) {
			sb.append(building.getName());
			sb.append("<br/>");
			if(!house.noOwner()) {
				sb.append("地主:<a href=\"");
				sb.append(("/chat/post.jsp?toUserId="+ house.getOwner().getUserId()));
				sb.append("\">");
				sb.append(house.getOwner().getWmlName());
				sb.append("</a><br/>租金:");
				sb.append(getRent());
				sb.append("<br/>");
			}
		} else if(type == TYPE_BUSINESS) {
			if(!house.noOwner()) {
				sb.append("地主:<a href=\"");
				sb.append(("/chat/post.jsp?toUserId="+ house.getOwner().getUserId()));
				sb.append("\">");
				sb.append(house.getOwner().getWmlName());
				sb.append("</a><br/>");
			}
		}
			
		if(userList.size() > 0) {
			sb.append("此处玩家");
			for(int i = 0;i < userList.size();i++) {
				RichUserBean bean = (RichUserBean)userList.get(i);
				sb.append(",<a href=\"");
				sb.append(("/chat/post.jsp?toUserId="+ bean.getUserId()));
				sb.append("\">");
				sb.append(bean.getFullName());
				sb.append("</a>");
			}
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	public void addHouse(RichUserBean user) {
		if(house.getOwner() != user)
			house.setOwner(user);
		else
			house.addLevel();
	}
	
	public void addHouseType(RichUserBean user, int type) {
		if(house.getOwner() != user)
			house.setOwner(user);
		else
			house.addLevel();
		house.setType(type);
	}
	
	public void addHouse(RichUserBean user, int level) {
		house.setOwner(user);
		house.setLevel(level);
	}
	
	public void addBusiness(RichUserBean user, int type) {
		if(house.getOwner() != user)
			house.setOwner(user);
		else
			house.addLevel();
		house.setType(type);
	}
	public void addBusiness(RichUserBean user) {
		if(house.getOwner() != user)
			house.setOwner(user);
		else
			house.addLevel();
	}
	/**
	 * @return Returns the building.
	 */
	public RichBuilding getBuilding() {
		return building;
	}
	/**
	 * @param building The building to set.
	 */
	public void setBuilding(RichBuilding building) {
		this.building = building;
	}
	public int getPrice() {
		if(building.getType() == 0) {
			if(house.getOwner() == null)
				return building.getPrice() * 6 + 25 * house.getLevel();
			else
				return 25;
		} else {
			if(house.getOwner() == null)
				return 500 + 50 * house.getLevel();
			else
				return 50;
		}
	}
	// 计算房产价值
	public int getPriceValue() {
		if(building.getType() == 0) {
			return building.getPrice() * 6 + 25 * house.getLevel();
		} else {
			return 500 + 50 * house.getLevel();
		}
	}

	// 是否是该用户的地
	public boolean isUserOwner(RichUserBean user) {
		return house != null && house.getOwner() == user;
	}
	// 判断是否为其他人所有
	public boolean isOtherUserOwner(RichUserBean user) {
		return house != null && house.getOwner() != user && house.getOwner() != null;
	}
	public boolean canBuyHouse(RichUserBean user) {
		return (house.noOwner() || house.getOwner() == user && !house.isMaxLevel());
	}
	public int getRent() {
		return building.getRent(house.getLevel());
	}
	/**
	 * @return Returns the house.
	 */
	public HouseBean getHouse() {
		return house;
	}
	/**
	 * @param house The house to set.
	 */
	public void setHouse(HouseBean house) {
		this.house = house;
	}
	
	public String getPattern() {
		if(type == TYPE_HOUSE) {
			if(house.noOwner())
				return "□";
			else
				return "■";
		}
		if(type == TYPE_BUSINESS) {
			if(house.noOwner())
				return "◇";
			else
				return "◆";
		}
		if(userList.isEmpty())
			return "○";
		else
			return "●";
	}
	
	public void addUser(RichUserBean user) {
		userList.add(user);
	}
	public void removeUser(RichUserBean user) {
		userList.remove(user);
	}
	public int getEventType() {
		return eventTypes[event];
	}
	public void reset() {
		setEvent(0);
		if(house != null)
			house.reset();
		userList.clear();
	}
	public boolean isHouse() {
		return house.getType() == 0;
	}
}
