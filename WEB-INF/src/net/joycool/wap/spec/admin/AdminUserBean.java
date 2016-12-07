package net.joycool.wap.spec.admin;

public class AdminUserBean {
	public int id;
	String name;
	String password;
	public int userId;

	public int groupId;
	public int groupId2;
	public int groupId3;
	
	AdminGroupBean group;


	public void setGroup(AdminGroupBean group) {
		this.group = group;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId2() {
		return groupId2;
	}

	public void setGroupId2(int groupId2) {
		this.groupId2 = groupId2;
	}

	public int getGroupId3() {
		return groupId3;
	}

	public void setGroupId3(int groupId3) {
		this.groupId3 = groupId3;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public AdminGroupBean getGroup(){
		if(group == null) {
			group = AdminAction.getAdminGroup(groupId).copy();
			if(groupId2 > 0)
				group.mergeFlags(AdminAction.getAdminGroup(groupId2));
			if(groupId3 > 0)
				group.mergeFlags(AdminAction.getAdminGroup(groupId3));
		}
		return group;
	}
}
