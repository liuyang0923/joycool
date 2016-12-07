package net.joycool.wap.spec.tiny;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 
 * @datetime:2009-10-24
 */
public class TinyGame8 extends TinyGame {
	public static String[] defaultMark = {"■", "●","○","★","☆","■","△","▲","□","◆","◇","→","←","↑","↓","①","②","③","④","⑤","⑥","⑦","⑧","⑨"};	//⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇
	public String[] mark = defaultMark;
	public static int OPEN_MASK = 0x40;
	
	int count;		// 连续几个相同？一般是2
	int same;		// 相同的有几个
	int width;		// 长宽
	int height;
	int area;		// = width * height
	
	int moveCount;	// 点了几次
	int openCount;
	long startTime;

	byte[][] grid;
	
	int turnCount;		// 正在翻本轮第几个
	int[] turnX;		// 之前翻的
	int[] turnY;

	public TinyGame8(int width, int height, int count, int same) {
		this.count = count;
		this.width = width;
		this.height = height;
		this.same = same;
		area = width * height;
	}
	public String getName() {
		return "翻牌" + width + "-" + height + "-" + count + "-" + same;
	}

	// 生成新游戏
	public void reset() {
		turnX = new int[count];
		turnY = new int[count];
		moveCount = 0;
		turnCount = 0;
		openCount = 0;
		startTime = 0;
		grid = new byte[width][height];
		
		int markCount = area / same;
		byte[] line = new byte[area];
		int start = 0;
		for(byte i = 1;i <= markCount;i++)
			for(int j = 0;j < same;j++) {
				line[start] = i;
				start++;
			}
		for(int i = 0;i < area;i++) {
			int a = RandomUtil.nextInt(area);
			int b = RandomUtil.nextInt(area);
			if(a != b) {
				byte c = line[a];
				line[a] = line[b];
				line[b] = c;
			}
		}
		start = 0;
		for(byte i = 0;i < width;i++)
			for(int j = 0;j < height;j++) {
				grid[i][j] = line[start];
				start++;
			}
	}
	// 返回某个坐标对应的字符表现
	public String getMark(int x, int y) {
		return mark[getValue(grid[x][y])];
	}
	public byte[][] getGrid() {
		return grid;
	}
	public void setGrid(byte[][] grid) {
		this.grid = grid;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getGameId() {
		return 1;
	}

	public String getGameURL() {
		return "/tiny/8v.jsp";
	}

	public void init() {
		reset();
	}
	public boolean isOpen(int x, int y) {
		return isOpen(grid[x][y]); 
	}
	// 判断该点是否已经打开
	public static boolean isOpen(int value) {
		return (value & OPEN_MASK) != 0; 
	}
	// 获取真正数值
	public static int getValue(byte value) {
		return value & ~OPEN_MASK; 
	}
	public int getSecond() {
		if(startTime == 0)
			return 0;
		return (int) ((System.currentTimeMillis() - startTime) / 1000);
	}
	public void open(int x, int y) {
		if(startTime == 0)
			startTime = System.currentTimeMillis();
		byte value = grid[x][y];
		if(isOpen(value))
			return;
		int v2 = getValue(value);
		moveCount++;
		openCount++;

		if(turnCount == count) {	// 本轮没有翻完
			boolean iden = true;	// 都相同
			for(int i = 1;i < count;i++) {
				if(grid[turnX[i]][turnY[i]] != grid[turnX[i - 1]][turnY[i - 1]]) {
					iden = false;
					break;
				}
			}
			if(!iden) {
				openCount -= count;
				for(int i = 0;i < count;i++) {
					grid[turnX[i]][turnY[i]] = (byte) (grid[turnX[i]][turnY[i]] & ~OPEN_MASK);
				}
			}
			turnCount = 0;
		}
		
		turnX[turnCount] = x;
		turnY[turnCount] = y;
		turnCount++;
		grid[x][y] = (byte) (value | OPEN_MASK);
		checkGame();
	}
	public static int[] getInt2(int x, int y) {
		int[] r = {x, y};
		return r;
	}
	public String[] getMark() {
		return mark;
	}
	public static void setMark(char[] mark) {
		TinyGame7.mark = mark;
	}
	public void checkGame() {
		if(area == openCount)
			setStatus(1);
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}

	public int getMoveCount() {
		return moveCount;
	}
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getOpenCount() {
		return openCount;
	}
	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
	public String getBlankMark() {
		return mark[0];
	}
	public void setMark(String[] mark) {
		this.mark = mark;
	}
}
