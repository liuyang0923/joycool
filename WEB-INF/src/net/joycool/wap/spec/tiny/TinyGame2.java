package net.joycool.wap.spec.tiny;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏: 转格子
 * @datetime:1007-10-24
 */
public class TinyGame2 extends TinyGame {
	public static char[] mark = {'☆','★','○','●','◇','◆','□','■','△','▲'};
	int width;
	int height;		// 格子长宽
	int moveCount;		// 移动的次数
	char[][] grid;
	
	
	public TinyGame2(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public String getName() {
		return "魔方" + width + "x" + height;
	}

	/**
	 * @return Returns the grid.
	 */
	public char[][] getGrid() {
		return grid;
	}

	public void init() {
		grid = new char[width][];
		for(int i = 0;i < width;i++) {
			grid[i] = new char[height];
			for(int j = 0;j < height;j++) {
				grid[i][j] = mark[j];
			}
		}
		
		int count = width * height / 2;
		int sum = (width + height) * 2;
		for(int i = 0;i < count;i++) {
			int r = RandomUtil.nextInt(sum);
			if(r < width) {
				moveUp(r);
			} else {
				r -= width;
				if(r < width){
					moveDown(r);
				} else {
					r -= width;
					if(r < height){
						moveLeft(r);
					} else {
						r -= height;
						if(r < height){
							moveRight(r);
						}
					}
				}
			}
		}
		moveCount = 0;
	}
	
	// 四个方向移动，r未移动的行或者列数
	public void moveUp(int r) {
		char tmp = grid[r][0];
		for(int i = 0;i < height - 1;i++) {
			grid[r][i] = grid[r][i + 1];
		}
		grid[r][height - 1] = tmp;
	}
	public void moveDown(int r) {
		char tmp = grid[r][height - 1];
		for(int i = height - 1;i > 0 ;i--) {
			grid[r][i] = grid[r][i - 1];
		}
		grid[r][0] = tmp;
	}
	public void moveLeft(int r) {
		char tmp = grid[0][r];
		for(int i = 0;i < width - 1;i++) {
			grid[i][r] = grid[i + 1][r];
		}
		grid[width - 1][r] = tmp;
	}
	public void moveRight(int r) {
		char tmp = grid[width - 1][r];
		for(int i = width - 1;i > 0 ;i--) {
			grid[i][r] = grid[i - 1][r];
		}
		grid[0][r] = tmp;
	}
	// 检查是否赢了
	public void checkGame() {
		moveCount++;
		for(int i = 0;i < width;i++) {
			for(int j = 0;j < height - 1;j++) {
				if(grid[i][j] != grid[i][j + 1])
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
		return 2;
	}

	public String getGameURL() {
		return "/tiny/2v.jsp";
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
}
