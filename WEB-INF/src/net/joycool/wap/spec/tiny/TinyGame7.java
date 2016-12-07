package net.joycool.wap.spec.tiny;

import java.util.LinkedList;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 
 * @datetime:2009-10-24
 */
public class TinyGame7 extends TinyGame {
	public static char[] mark = {'○','①','②','③','④','⑤','⑥','⑦','⑧','★', '●'};	//⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇
	
	int count;		// 有几个地雷
	int width;		// 长宽
	int height;
	int area;		// = width * height
	boolean lost;	// 是否已经输了
	
	int openCount;	// 猜出了几个非地雷
	long startTime;

	byte[][] grid;
	

	public TinyGame7(int width, int height, int count) {
		this.count = count;
		this.width = width;
		this.height = height;
		area = width * height;
	}
	public String getName() {
		return "扫雷" + width + "-" + height + "-" + count;
	}

	// 生成新游戏
	public void reset() {
		startTime = 0;
		lost = false;
		grid = new byte[width][];
		for(byte i = 0;i < width;i++) {
			grid[i] = new byte[height];
		}
		for(int i = 0;i < count;i++) {
			int x = RandomUtil.nextInt(width);
			int y = RandomUtil.nextInt(height);
			if(grid[x][y] != 9) {
				grid[x][y] = 9;
				modGrid(x, y);	// 雷周围数字+1
			} else {
				i--;
			}
		}
	}
	public void modGrid(int x, int y) {
		if(x > 0) {
			addGrid(x - 1, y);
			if(y > 0)
				addGrid(x - 1, y - 1);
			if(y < height - 1)
				addGrid(x - 1, y + 1);
		}
		if(x < width - 1) {
			addGrid(x + 1, y);
			if(y > 0)
				addGrid(x + 1, y - 1);
			if(y < height - 1)
				addGrid(x + 1, y + 1);
		}
		if(y > 0)
			addGrid(x, y - 1);
		if(y < height - 1)
			addGrid(x, y + 1);
	}
	public void addGrid(int x, int y) {
		if(grid[x][y] != 9)
			grid[x][y]++;
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
		return "/tiny/7v.jsp";
	}

	public void init() {
		reset();
	}
	public boolean isOpen(int x, int y) {
		return isOpen(grid[x][y]); 
	}
	// 判断该点是否已经打开
	public static boolean isOpen(int value) {
		return (value & 0x10) != 0; 
	}
	public static boolean isMine(int value) {
		return value == 9; 
	}
	// 获取真正数值
	public static int getValue(byte value) {
		return value & ~0x10; 
	}

	// 返回某个坐标对应的字符表现
	public char getMark(int x, int y) {
		byte value = grid[x][y];
		int v2 = getValue(value);
		if(lost && isMine(v2)) {
			if(isOpen(value))
				return '☆';
			else
				return '★';
		}

		if(!isOpen(value))
			return '■';

		return mark[v2];
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

		if(isMine(v2)) {
			grid[x][y] = (byte) (value | 0x10);
			lost = true;
			return;
		}

		LinkedList list = new LinkedList();
		int[] first = {x, y};
		list.add(first);
		do {
			int[] xy = (int[])list.removeFirst();
			x = xy[0];
			y = xy[1];
			value = grid[x][y];
			if(!isOpen(value)) {
				grid[x][y] = (byte) (value | 0x10);
				openCount++;
				if(value == 0) {
					if(x > 0) {
						list.add(getInt2(x - 1, y));
						if(y > 0)
							list.add(getInt2(x - 1, y - 1));
						if(y < height - 1)
							list.add(getInt2(x - 1, y + 1));
					}
					if(x < width - 1) {
						list.add(getInt2(x + 1, y));
						if(y > 0)
							list.add(getInt2(x + 1, y - 1));
						if(y < height - 1)
							list.add(getInt2(x + 1, y + 1));
					}
					if(y > 0)
						list.add(getInt2(x, y - 1));
					if(y < height - 1)
						list.add(getInt2(x, y + 1));
				}
			}
		} while(list.size() != 0);

		checkGame();
	}
	public static int[] getInt2(int x, int y) {
		int[] r = {x, y};
		return r;
	}
	public static char[] getMark() {
		return mark;
	}
	public static void setMark(char[] mark) {
		TinyGame7.mark = mark;
	}
	public void checkGame() {
		if(area == count + openCount)
			setStatus(1);
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public boolean isLost() {
		return lost;
	}
	public void setLost(boolean lost) {
		this.lost = lost;
	}
	public int getOpenCount() {
		return openCount;
	}
	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
