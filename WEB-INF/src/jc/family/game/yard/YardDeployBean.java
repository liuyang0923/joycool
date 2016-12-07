package jc.family.game.yard;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

public class YardDeployBean {

	int id;
	int fmid;
	int protoId;
	String material;// 材料
	List materialList = new ArrayList();
	int step;		// 当前的步骤(不要超过127步)
	long startTime;	// 每步开始的时间
	int isCurrent;	// 是否正确(0:假,1:真)
	int totalPoint;	// 每部得分之合
	int inuse;	// 是否正在使用(0:没使用,1:正在用)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFmid() {
		return fmid;
	}

	public void setFmid(int fmid) {
		this.fmid = fmid;
	}

	public int getProtoId() {
		return protoId;
	}

	public String getProtoName() {
		YardRecipeProtoBean yP = YardAction.getRecipeProto(protoId);
		return yP != null ? yP.getName() : "";
	}

	public void setProtoId(int protoId) {
		this.protoId = protoId;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
		makeMaterialList();
	}

	/**
	 * 生成一个set包含所有的材料id
	 */
	public void makeMaterialList() {
		materialList.clear();
		String[] ms = material.split(",");
		for (int i = 0; i < ms.length; i++) {
			String[] ms2 = ms[i].split("-");
			int[] mat = new int[2];
			mat[0] = StringUtil.toInt(ms2[0]);
			if (ms2.length == 2) {
				mat[1] = StringUtil.toInt(ms2[1]);
				if (mat[1] <= 0)
					mat[1] = 1;
			} else {
				mat[1] = 1;
			}

			if (mat[0] > 0)
				materialList.add(mat);
		}
	}

	public void addItemToList(int itemId, int s) {
		boolean exist = false;
		for (int i = 0; i < materialList.size(); i++) {
			int[] mat = (int[]) materialList.get(i);
			if (mat[0] == itemId) {
				mat[1] += s;
				exist = true;
			}
		}
		if (!exist) {
			int[] mat = new int[2];
			mat[0] = itemId;
			mat[1] = s;
			materialList.add(mat);
		}
		materialListToString();
	}

	private void materialListToString() {
		StringBuilder str = new StringBuilder(materialList.size() * 6);// id(3)-数量(1),
		for (int i = 0; i < materialList.size(); i++) {
			int[] mat = (int[]) materialList.get(i);
			str.append(mat[0]);
			str.append("-");
			str.append(mat[1]);
			str.append(",");
		}
		if (str.length() > 0)
			material = str.substring(0, str.length() - 1);
		else
			material = "";
	}

	public List getMaterialList() {
		return materialList;
	}
	
	public void cutItemToList(int itemId) {
		for (int i = 0; i < materialList.size(); i++) {
			int[] mat = (int[]) materialList.get(i);
			if (mat[0] == itemId) {
				materialList.remove(i);
				break;
			}
		}
		materialListToString();
		SqlUtil.executeUpdate("update fm_yard_deploy set material='" + material + "' where id=" + getId(), 5);
	}
	
	/**
	 * 检查配菜列表是否含有某一材料
	 * @param itemId
	 * @return
	 * 返回数量
	 */
	public int materialCount(int itemId){
		for (int i = 0 ; i < materialList.size() ; i++){
			int[] mat = (int[]) materialList.get(i);
			if (mat[0] == itemId) {
				return mat[1];
			}
		}
		return 0;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getIsCurrent() {
		return isCurrent;
	}
	
	public boolean getIsCurrent2() {
		return isCurrent == 1;
	}

	public void setIsCurrent(int isCurrent) {
		this.isCurrent = isCurrent;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public int getInuse() {
		return inuse;
	}

	public void setInuse(int inuse) {
		this.inuse = inuse;
	}
	
//	// 得分的数组
//	public int[] getPointsArray(){
//		if (point.endsWith(","))
//			point = point.substring(0,point.length() - 1);
//		String[] tmp = point.split("\\,");
//		int[] points = new int[tmp.length];
//		for (int i = 0 ; i < points.length ; i++){
//			points[i] = StringUtil.toInt(tmp[i]);
//		}
//		return points;
//	}
//	
//	/**
//	 * 取得最小值.出错返回-1.
//	 * @return
//	 */
//	public int getMinPoint(){
//		int[] m = getPointsArray();
//		if (m.length < 2)
//			return -1;
//		int min = m[0];
//		for (int i = 1 ; i < m.length ; i++){
//			if (min > m[i])
//				min = m[i];
//		}
//		return min;
//	}
}