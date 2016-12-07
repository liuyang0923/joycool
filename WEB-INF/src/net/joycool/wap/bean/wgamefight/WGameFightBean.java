package net.joycool.wap.bean.wgamefight;
/** 
 * @author guip
 * @explain：
 * @datetime:2007-10-22 13:20:48
 */
public class WGameFightBean {
	//游戏id
public static int FIGHT = 1;
//最大坐庄数量
public static int MAX_BK_COUNT = 5;

int id;

int leftUserId;

String leftNickname;

String leftCardsStr;

int rightUserId;

String rightNickname;

String rightCardsStr;

String content;

long wager;

String startDatetime;

String endDatetime;

int mark;

int winUserId;

int gameId;

String rightViewed;

String leftViewed;

int[] leftDices;

int[] rightDices;

//坐庄
public static int PK_MARK_BKING = 1;

// pk
public static int PK_MARK_PKING = 2;

// 结束
public static int PK_MARK_END = 3;

//坐庄者生命值
public static int LEFT_PK_ACT = 7;

//挑战者生命值
public static int RIGHT_PK_ACT = 7;

//庄输
public static int RESULT_MARK_LOSE = 0;

//庄赢
public static int RESULT_MARK_WIN = 1;

// 平局
public static int RESULT_MARK_DRAW = 2;
/**
 * @return 返回 pK_MARK_BKING。
 */
public static int getPK_MARK_BKING() {
	return PK_MARK_BKING;
}

/**
 * @param pk_mark_bking 要设置的 pK_MARK_BKING。
 */
public static void setPK_MARK_BKING(int pk_mark_bking) {
	PK_MARK_BKING = pk_mark_bking;
}

/**
 * @return 返回 pK_MARK_END。
 */
public static int getPK_MARK_END() {
	return PK_MARK_END;
}

/**
 * @param pk_mark_end 要设置的 pK_MARK_END。
 */
public static void setPK_MARK_END(int pk_mark_end) {
	PK_MARK_END = pk_mark_end;
}

/**
 * @return 返回 pK_MARK_PKING。
 */
public static int getPK_MARK_PKING() {
	return PK_MARK_PKING;
}

/**
 * @param pk_mark_pking 要设置的 pK_MARK_PKING。
 */
public static void setPK_MARK_PKING(int pk_mark_pking) {
	PK_MARK_PKING = pk_mark_pking;
}

/**
 * @return 返回 content。
 */
public String getContent() {
	return content;
}

/**
 * @param content 要设置的 content。
 */
public void setContent(String content) {
	this.content = content;
}

/**
 * @return 返回 endDatetime。
 */
public String getEndDatetime() {
	return endDatetime;
}

/**
 * @param endDatetime 要设置的 endDatetime。
 */
public void setEndDatetime(String endDatetime) {
	this.endDatetime = endDatetime;
}

/**
 * @return 返回 gameId。
 */
public int getGameId() {
	return gameId;
}

/**
 * @param gameId 要设置的 gameId。
 */
public void setGameId(int gameId) {
	this.gameId = gameId;
}

/**
 * @return 返回 id。
 */
public int getId() {
	return id;
}

/**
 * @param id 要设置的 id。
 */
public void setId(int id) {
	this.id = id;
}

/**
 * @return 返回 leftCardsStr。
 */
public String getLeftCardsStr() {
	return leftCardsStr;
}

/**
 * @param leftCardsStr 要设置的 leftCardsStr。
 */
public void setLeftCardsStr(String leftCardsStr) {
	this.leftCardsStr = leftCardsStr;
}

/**
 * @return 返回 leftNickname。
 */
public String getLeftNickname() {
	return leftNickname;
}

/**
 * @param leftNickname 要设置的 leftNickname。
 */
public void setLeftNickname(String leftNickname) {
	this.leftNickname = leftNickname;
}

/**
 * @return 返回 leftUserId。
 */
public int getLeftUserId() {
	return leftUserId;
}

/**
 * @param leftUserId 要设置的 leftUserId。
 */
public void setLeftUserId(int leftUserId) {
	this.leftUserId = leftUserId;
}

/**
 * @return 返回 leftViewed。
 */
public String getLeftViewed() {
	return leftViewed;
}

/**
 * @param leftViewed 要设置的 leftViewed。
 */
public void setLeftViewed(String leftViewed) {
	this.leftViewed = leftViewed;
}

/**
 * @return 返回 mark。
 */
public int getMark() {
	return mark;
}

/**
 * @param mark 要设置的 mark。
 */
public void setMark(int mark) {
	this.mark = mark;
}


/**
 * @return 返回 rightCardsStr。
 */
public String getRightCardsStr() {
	return rightCardsStr;
}

/**
 * @param rightCardsStr 要设置的 rightCardsStr。
 */
public void setRightCardsStr(String rightCardsStr) {
	this.rightCardsStr = rightCardsStr;
}

/**
 * @return 返回 rightNickname。
 */
public String getRightNickname() {
	return rightNickname;
}

/**
 * @param rightNickname 要设置的 rightNickname。
 */
public void setRightNickname(String rightNickname) {
	this.rightNickname = rightNickname;
}

/**
 * @return 返回 rightUserId。
 */
public int getRightUserId() {
	return rightUserId;
}

/**
 * @param rightUserId 要设置的 rightUserId。
 */
public void setRightUserId(int rightUserId) {
	this.rightUserId = rightUserId;
}

/**
 * @return 返回 rightViewed。
 */
public String getRightViewed() {
	return rightViewed;
}

/**
 * @param rightViewed 要设置的 rightViewed。
 */
public void setRightViewed(String rightViewed) {
	this.rightViewed = rightViewed;
}

/**
 * @return 返回 startDatetime。
 */
public String getStartDatetime() {
	return startDatetime;
}

/**
 * @param startDatetime 要设置的 startDatetime。
 */
public void setStartDatetime(String startDatetime) {
	this.startDatetime = startDatetime;
}

/**
 * @return 返回 wager。
 */
public long getWager() {
	return wager;
}

/**
 * @param wager 要设置的 wager。
 */
public void setWager(long wager) {
	this.wager = wager;
}

/**
 * @return 返回 winUserId。
 */
public int getWinUserId() {
	return winUserId;
}

/**
 * @param winUserId 要设置的 winUserId。
 */
public void setWinUserId(int winUserId) {
	this.winUserId = winUserId;
}

/**
 * @return 返回 leftDices。
 */
public int[] getLeftDices() {
	if (leftDices == null) {
		if (leftCardsStr != null) {
			String[] ds = leftCardsStr.split(",");
			if (ds != null && ds.length == 3) {
				leftDices = new int[3];
				leftDices[0] = Integer.parseInt(ds[0]);
				leftDices[1] = Integer.parseInt(ds[1]);
				leftDices[2] = Integer.parseInt(ds[2]);
			}
		}
	}
	return leftDices;
}

/**
 * @param leftDices 要设置的 leftDices。
 */
public void setLeftDices(int[] leftDices) {
	this.leftDices = leftDices;
}

/**
 * @return 返回 rightDices。
 */
public int[] getRightDices() {
	if (rightDices == null) {
		if (rightCardsStr != null) {
			String[] ds = rightCardsStr.split(",");
			if (ds != null && ds.length == 3) {
				rightDices = new int[3];
				rightDices[0] = Integer.parseInt(ds[0]);
				rightDices[1] = Integer.parseInt(ds[1]);
				rightDices[2] = Integer.parseInt(ds[2]);
			}
		}
	}
	return rightDices;
}

/**
 * @param rightDices 要设置的 rightDices。
 */
public void setRightDices(int[] rightDices) {
	this.rightDices = rightDices;
}
public String StringAlter(String stringName,int sub,int act)
    {
	String[] name = stringName.split(",");
	name[sub-1] = String.valueOf(act);
	StringBuffer sb = new StringBuffer();
	for(int i=0;i<name.length;i++)
	{
		if(i>=sub)
		{
			sb.append("0");
		}else{
		sb.append(name[i]);
		}
		if(i!=name.length-1)
		{
			sb.append(",");
		}
	}
	 return sb.toString();
	}
public int PKact(String leftView,String rightView,int LEFT_PK_ACT,int RIGHT_PK_ACT)
{
	String leftStr[] = leftView.split(",");
	String rightStr[] = rightView.split(",");
	for(int i=0;i<leftStr.length;i++)
	{
		
		if(leftStr[i].equals("0") && rightStr[i].equals("1"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-1;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("0") && rightStr[i].equals("2"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-2;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("0") && rightStr[i].equals("3"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-3;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("0") && rightStr[i].equals("4"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("0") && rightStr[i].equals("5"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		//-------------------------------
		if(leftStr[i].equals("1") && rightStr[i].equals("0"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-1;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("1") && rightStr[i].equals("1"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-1;
			RIGHT_PK_ACT = RIGHT_PK_ACT-1;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("1") && rightStr[i].equals("2"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-2;
			RIGHT_PK_ACT = RIGHT_PK_ACT-1;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("1") && rightStr[i].equals("3"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-3;
			RIGHT_PK_ACT = RIGHT_PK_ACT-1;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("1") && rightStr[i].equals("4"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("1") && rightStr[i].equals("5"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		//---------------------------
		if(leftStr[i].equals("2") && rightStr[i].equals("0"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-2;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("2") && rightStr[i].equals("1"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-1;
			RIGHT_PK_ACT = RIGHT_PK_ACT-2;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("2") && rightStr[i].equals("2"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-2;
			RIGHT_PK_ACT = RIGHT_PK_ACT-2;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("2") && rightStr[i].equals("3"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-3;
			RIGHT_PK_ACT = RIGHT_PK_ACT-2;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("2") && rightStr[i].equals("4"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-1;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("2") && rightStr[i].equals("5"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		//=================================
		if(leftStr[i].equals("3") && rightStr[i].equals("0"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-3;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("3") && rightStr[i].equals("1"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-1;
			RIGHT_PK_ACT = RIGHT_PK_ACT-3;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("3") && rightStr[i].equals("2"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-2;
			RIGHT_PK_ACT = RIGHT_PK_ACT-3;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("3") && rightStr[i].equals("3"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-3;
			RIGHT_PK_ACT = RIGHT_PK_ACT-3;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("3") && rightStr[i].equals("4"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-2;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("3") && rightStr[i].equals("5"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		//=============================
		if(leftStr[i].equals("4") && rightStr[i].equals("1"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("4") && rightStr[i].equals("2"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-1;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("4") && rightStr[i].equals("3"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-2;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("4") && rightStr[i].equals("4"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("4") && rightStr[i].equals("5"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		//==================================
		if(leftStr[i].equals("5") && rightStr[i].equals("1"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("5") && rightStr[i].equals("2"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("5") && rightStr[i].equals("3"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("5") && rightStr[i].equals("4"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
		if(leftStr[i].equals("5") && rightStr[i].equals("5"))
		{
			LEFT_PK_ACT = LEFT_PK_ACT-0;
			RIGHT_PK_ACT = RIGHT_PK_ACT-0;
			if(LEFT_PK_ACT<=0 && RIGHT_PK_ACT<=0)
			{
				return 2;
			}
			else if(LEFT_PK_ACT<=0)
			{
				return 0;
			}else if(RIGHT_PK_ACT<=0)
			{
				return 1;
			}
		}
	}
	return 2;
}
public static void main(String arg[])
{
	WGameFightBean fb =new WGameFightBean();
	int a =fb.PKact("0,1,1,1,1,1,3,3,3,0","1,2,3,4,5,1,0,0,0,0",LEFT_PK_ACT,RIGHT_PK_ACT);
	System.out.println(a);
}
}
