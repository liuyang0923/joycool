package net.joycool.wap.spec.buyfriends;

import java.util.List;


public class ServiceVisit {

	private static ServiceVisit serviceVisit = new ServiceVisit();
	
	DAOVisit visitDAO = new DAOVisit();
	
	private ServiceVisit(){}
	
	public static ServiceVisit getInstance(){
		if(serviceVisit == null) {
			synchronized(ServiceVisit.class) {
				if(serviceVisit == null)
					serviceVisit = new ServiceVisit();
			}
		}
		return serviceVisit;
	}
	
	public boolean addVisit(BeanVisit visit) {
		
		this.visitDAO.deleteVisit(visit.getFromUid(), visit.getToUid());
		return this.visitDAO.addVisit(visit);
	}
	
	public List getVisitByToUid(int toUid, int start, int limit) {
		return this.visitDAO.getVisitByToUid(toUid, start, limit);
	}
	
	public int getCountVisitByToUid(int toUid) {
		return this.visitDAO.getCountVisitByToUid(toUid);
	}
}
