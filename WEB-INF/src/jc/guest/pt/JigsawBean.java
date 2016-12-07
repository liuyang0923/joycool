package jc.guest.pt;

import net.joycool.wap.util.RandomUtil;

public class JigsawBean {
	
	private int id;// 所选图片的ID
	private String picName;// 用户看到的图片的中文名字
	
	private int picRows=0;// 图片的行数
	private int picCols=0;// 图片的列数
	
	private int numRows=0;// 图片的行数
	private int numCols=0;// 图片的列数
	
	private int[][] pinPic;// 子图片排列的数组
	private int[][]pinNum;// 拼图数组保存
	
	private int picNullRow;// 不是子图片的图片所在的行数
	private int picNullCol;// 不是子图片的图片所在的列数
	
	private int numNullRow;// 不是子图片的图片所在的行数
	private int numNullCol;// 不是子图片的图片所在的列数
	
	
	private int picCountMove=0;// 图片移动次数
	private int numCountMove=0;// 文字移动次数
	
	private int picGameState=0;// 图片游戏状态
	private int numGameState=0;// 文字游戏状态
	
	private int button=0;// 0为原图关闭状态1为查看
	
	private int picGameLevel=1;// 图片的普通、中等、困难
	private int numGameLevel=1;// 分别代表数字的1~9关
	
	
	private int picState;// 图片状态：0隐藏，1显示
	private int picNum;// 图片编号，与图片的文件名相关联
	private long updateTime;// 图片最后修改时间
	private int UserID=0;// 是否是登陆的人在玩
	
	
	
	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public int getPicNullRow() {
		return picNullRow;
	}

	public void setPicNullRow(int picNullRow) {
		this.picNullRow = picNullRow;
	}

	public int getPicNullCol() {
		return picNullCol;
	}

	public void setPicNullCol(int picNullCol) {
		this.picNullCol = picNullCol;
	}

	public int getNumNullRow() {
		return numNullRow;
	}

	public void setNumNullRow(int numNullRow) {
		this.numNullRow = numNullRow;
	}

	public int getNumNullCol() {
		return numNullCol;
	}

	public void setNumNullCol(int numNullCol) {
		this.numNullCol = numNullCol;
	}

	public int getPicGameLevel() {
		return picGameLevel;
	}

	public void setPicGameLevel(int picGameLevel) {
		this.picGameLevel = picGameLevel;
	}

	public int getNumGameLevel() {
		return numGameLevel;
	}

	public void setNumGameLevel(int numGameLevel) {
		this.numGameLevel = numGameLevel;
	}

	public int getPicCountMove() {
		return picCountMove;
	}

	public void setPicCountMove(int picCountMove) {
		this.picCountMove = picCountMove;
	}

	public int getNumCountMove() {
		return numCountMove;
	}

	public void setNumCountMove(int numCountMove) {
		this.numCountMove = numCountMove;
	}

	public int getPicGameState() {
		return picGameState;
	}

	public void setPicGameState(int picGameState) {
		this.picGameState = picGameState;
	}

	public int getNumGameState() {
		return numGameState;
	}

	public void setNumGameState(int numGameState) {
		this.numGameState = numGameState;
	}

	public int[][] getPinNum() {
		return pinNum;
	}

	public void setPinNum(int[][] pinNum) {
		this.pinNum = pinNum;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getPicState() {
		return picState;
	}

	public void setPicState(int picState) {
		this.picState = picState;
	}

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPicRows() {
		return picRows;
	}

	public void setPicRows(int picRows) {
		this.picRows = picRows;
	}

	public int getPicCols() {
		return picCols;
	}

	public void setPicCols(int picCols) {
		this.picCols = picCols;
	}

	public int[][] getPinPic() {
		return pinPic;
	}

	public void setPinPic(int[][] pinPic) {
		this.pinPic = pinPic;
	}
	
	public boolean checkEnd(int playType){// 判断是否拼对了
		int [][]pin = null;
		if(playType==0){
			pin=pinNum;
		}else if(playType==1){
			pin=pinPic;
		}
		if(pin==null){
			return false;
		}
		for(int i=0;i<pin.length;i++){
			for(int j=0;j<pin[0].length;j++){
					if(pin[i][j]!=i*pin[0].length+j+1){
						if(pin[i][j]==0&&i*pin[0].length+j+1==pin[0].length*pin.length){
							if(playType==0){
								setNumGameState(1);// 修改文字游戏状态
							}else if(playType==1){
								setPicGameState(1);// 修改图片游戏状态
							}
							
							return true;
						}
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 对选中的图片子图进行随机排序，并且可拼回去的排序
	 * 
	 * @param jigsaw
	 * @return
	 */
	public int[][] getInit(int playType) { // 初始化拼图，得出一个可以拼得回去的拼图秩序
		
		int count = 0;
		button=0;
		
		if(playType==0){

			numNullCol = numCols - 1;
			numNullRow = numRows - 1;
			numGameState = 0;
			numCountMove = 0;
			count = numCols * numRows;// 得到数组的大小
			
		}else if(playType==1){

			picNullCol = picCols - 1;
			picNullRow = picRows - 1;
			picGameState = 0;
			picCountMove = 0;
			count = picCols * picRows;// 得到数组的大小
			
		}
		if(count==0){
			return null;
		}

		int[] a = new int[count];// 里面存放1~count的值
		int[] b = new int[count];

		int sum;

		{
			sum = 0;
			for (int i = 0; i < count - 1; i++) {
				a[i] = i + 1;

			}

			for (int i = count - 1; i > 0; i--) {
				int c = RandomUtil.nextInt(i);
				b[i - 1] = a[c];
				a[c] = a[i - 1];
			}

			for (int i = 0; i < b.length - 1; i++) {
				for (int j = i + 1; j < b.length - 1; j++) {
					if (b[i] > b[j]) {
						sum++;
					}
				}
			}
		} 
		if (sum % 2 != 0) {	// 不为偶数的时候不能回复到原来的图片，交换位置变成偶数
			int tmp = b[count - 2];
			b[count - 2] = b[count - 3];
			b[count - 3] = tmp;
		}
		
		if(playType==0){
			pinNum = new int[numRows][numCols];
			
			for (int i = 0; i < numRows; i++) {// 一维数组变成二维
				for (int j = 0; j < numCols; j++) {
					pinNum[i][j] = b[numCols * i + j];
				}
			}
			return pinNum;	
		}else if(playType==1){
			pinPic = new int[picRows][picCols];
			
			for (int i = 0; i < picRows; i++) {// 一维数组变成二维
				for (int j = 0; j < picCols; j++) {
					pinPic[i][j] = b[picCols * i + j];
				}
			}
			return pinPic;
		}
		return null;
	}

	public void move(int erow, int ecol,int playType) {
		if (near(erow, ecol,playType)) {
			
			if(playType==0){
				// 交换位置
				int temp = pinNum[numNullRow][numNullCol];
				pinNum[numNullRow][numNullCol] = pinNum[erow][ecol];
				pinNum[erow][ecol] = temp;
				// 修改拼图bean里的一些参数，将移动次数加一
				setNumNullCol(ecol);
				setNumNullRow(erow);
				numCountMove++;
				
			}else if(playType==1){
				// 交换位置
				int temp = pinPic[picNullRow][picNullCol];
				pinPic[picNullRow][picNullCol] = pinPic[erow][ecol];
				pinPic[erow][ecol] = temp;
				// 修改拼图bean里的一些参数，将移动次数加一
				setPicNullCol(ecol);
				setPicNullRow(erow);
				picCountMove++;
			}

		}
	}

	public boolean near(int erow, int ecol,int playType) {// 判断两格之间是否相邻
		int nullRow=0;
		int nullCol=0;
		if(playType==0){
			nullRow = numNullRow;
			nullCol = numNullCol;
		}else if(playType==1){
			nullRow = picNullRow;
			nullCol = picNullCol;
		}
		return (nullRow == erow && (nullCol - ecol == -1 || nullCol - ecol == 1))|| (nullCol == ecol && (nullRow - erow == -1 || nullRow - erow == 1));
	}
}
