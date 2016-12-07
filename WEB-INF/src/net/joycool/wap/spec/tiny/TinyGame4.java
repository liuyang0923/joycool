package net.joycool.wap.spec.tiny;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 格子
 * @datetime:1007-10-24
 */
public class TinyGame4 extends TinyGame {
	public static char[] mark = {'☆','★','○','●','◇','◆','□','■','△','▲'};
	int width;
	int height;		// 格子长宽
	int type;		// 有几个类型，例如是5，那么翻转5次变为原来的
	int moveCount;	// 移动的次数
	byte[][] grid;
	
	
	public TinyGame4(int width, int height) {
		this.width = width;
		this.height = height;
		this.type = 2;
	}
	public TinyGame4(int width, int height, int type) {
		this.width = width;
		this.height = height;
		this.type = type;
	}
	public String getName() {
		return "翻格子" + width + "x" + height + "-" + type;
	}
	
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return Returns the grid.
	 */
	public byte[][] getGrid() {
		return grid;
	}
	public char getGridMark(int x, int y) {
		return mark[grid[x][y]];
	}

	public void init() {
		
		grid = new byte[width][];
		for(int i = 0;i < width;i++) {
			grid[i] = new byte[height];
			for(int j = 0;j < height;j++) {
				grid[i][j] = 0;
			}
		}
		
		int count = width * height / 2;
		for(int i = 0;i < count;i++) {
			int x = RandomUtil.nextInt(width);
			int y = RandomUtil.nextInt(height);
			turn(x, y);
		}
		moveCount = 0;
	}
	
	public void turn(int pos) {
		turn(pos % width, pos / width);
	}
	// 四个方向移动，r未移动的行或者列数
	public void turn(int x, int y) {
		turnOne(x, y);
		if(x > 0)
			turnOne(x - 1, y);
		if(y > 0)
			turnOne(x, y - 1);
		if(x < width - 1)
			turnOne(x + 1, y);
		if(y < height - 1)
			turnOne(x, y + 1);
	}
	public void turnOne(int x, int y) {
		grid[x][y]++;
		if(grid[x][y] >= type)
			grid[x][y] = 0;
	}
	
	// 检查是否赢了
	public void checkGame() {
		moveCount++;
		for(int i = 0;i < width;i++) {
			for(int j = 0;j < height;j++) {
				if(grid[i][j] != 0)
					return;
			}
		}
		setStatus(1);
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

	public int getGameId() {
		return 4;
	}

	public String getGameURL() {
		return "/tiny/4v.jsp";
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
}
