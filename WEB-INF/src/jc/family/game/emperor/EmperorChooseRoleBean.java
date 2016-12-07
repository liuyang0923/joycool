package jc.family.game.emperor;

public class EmperorChooseRoleBean {
	boolean beChoose;
	String chooseUserNameWml;
	EmperorRoleBean Role = null;

	public boolean isBeChoose() {
		return beChoose;
	}

	public void setBeChoose(boolean beChoose) {
		this.beChoose = beChoose;
	}

	public String getChooseUserNameWml() {
		return chooseUserNameWml;
	}

	public void setChooseUserNameWml(String chooseUserNameWml) {
		this.chooseUserNameWml = chooseUserNameWml;
	}

	public EmperorRoleBean getRole() {
		return Role;
	}

	public void setRole(EmperorRoleBean role) {
		Role = role;
	}

}
