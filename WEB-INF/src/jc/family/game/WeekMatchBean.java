package jc.family.game;

import java.util.Date;

public class WeekMatchBean {
	int id;
	int weekDay; // 星期几
	int type; // 1龙舟 , 2雪仗 , 3问答
	int effect; // 是否生效
	Date starttime;
	Date endtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	public Date getStarttime() {
		return starttime;
	}

	public java.sql.Timestamp getEndSQLtime() {
		return new java.sql.Timestamp(endtime.getTime());
	}

	public java.sql.Timestamp getStartSQLtime() {
		return new java.sql.Timestamp(starttime.getTime());
	}

	public void setStarttime(Date startTime) {
		starttime = startTime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEnd_time(Date endTime) {
		endtime = endTime;
	}
}
