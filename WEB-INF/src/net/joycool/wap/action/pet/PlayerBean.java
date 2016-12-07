package net.joycool.wap.action.pet;

public class PlayerBean implements Comparable{
	
	//当前成绩
	int position;
	//因子
	int factor;
	
	PetUserBean petUser;
	
	int stage[] = new int[2];
	
	public PlayerBean(PetUserBean bean){
		this.petUser = bean;
		position = 0;
	}
	
	public int getPetid() {
		return petUser.getId();
	}
	public int getUserid() {
		return petUser.getUser_id();
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return petUser.getName();
	}
	
	public int getRank() {
		return petUser.getRank();
	}
	
	public int getType() {
		return petUser.getType();
	}
	
	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	public int[] getStage() {
		return stage;
	}

	public void setStage(int[] stage) {
		this.stage = stage;
	}

	public void inputStage(int stage){
		if(this.stage[0] == 0){
			this.stage[0] = stage;
		}else if(this.stage[1] == 0){
			this.stage[1] = stage;
		}
	}
	
	public void changeStage(){
		this.stage[0] = 0;
		if(this.stage[1] != 0){
			this.stage[0] = this.stage[1];
			this.stage[1] = 0;
	}
	}

	public int compareTo(Object arg) {
		PlayerBean p = (PlayerBean)arg;
		if(position < p.getPosition())
			return 1;
		else if(position == p.getPosition())
			return 0;
			
		return -1;
	}

	public int getYesterday() {
		return petUser.getYesterday();
	}

	/**
	 * @return Returns the petUser.
	 */
	public PetUserBean getPetUser() {
		return petUser;
	}

	/**
	 * @param petUser The petUser to set.
	 */
	public void setPetUser(PetUserBean petUser) {
		this.petUser = petUser;
	}
}
