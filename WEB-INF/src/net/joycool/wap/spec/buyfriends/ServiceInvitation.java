package net.joycool.wap.spec.buyfriends;

public class ServiceInvitation {

	private static ServiceInvitation serviceInvita = new ServiceInvitation();
	
	public static ServiceInvitation getInstance(){
		if(serviceInvita == null) {
			synchronized(ServiceInvitation.class) {
				if(serviceInvita == null) 
					serviceInvita = new ServiceInvitation();
			}
		}
		return serviceInvita;
	}
	
	private ServiceInvitation(){}
	
	
	DAOInvitation invitationDAO;
	
	public void addInvite(int userId, int inviteUserId){
		invitationDAO.addInvite(userId, inviteUserId);
	}
		
}
