package net.joycool.wap.spec.castle;

import java.util.Date;
import java.util.LinkedList;

public class TongBean {
	static CastleService service = CastleService.getInstance();
	
	int id;
	String name;
	int uid;
	Date createTime;
	int count;
	String info;
	int people;		// 总人口数
	
	public final static int  REPORT_LIMIT = 30;
	LinkedList reports;		// 联盟战报，最多保存20条

	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void addPeople(int people2) {
		people += people2;
		if(people < 0)
			people = 0;
	}
	// 联盟战报
	public void addReport(CastleMessage bean) {
		getReports();
		synchronized(this) {
			if(reports.size() >= REPORT_LIMIT)
				reports.removeLast();
			reports.addFirst(bean);
		}
	}
	public LinkedList getReports() {
		if(reports == null) {
			synchronized(this) {
				if(reports == null)
					reports = service.getTongReports(id, 0, REPORT_LIMIT);
			}
		}
		return reports;
	}
	public void setReports(LinkedList reports) {
		this.reports = reports;
	}
}
