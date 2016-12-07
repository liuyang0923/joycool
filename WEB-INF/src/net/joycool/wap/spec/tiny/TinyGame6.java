package net.joycool.wap.spec.tiny;

/**
 * @author zhouj
 * @explain： 
 * @datetime:1007-10-24
 */
public class TinyGame6 extends TinyGame {
	public static char[] mark = {' ','①','②','③','④','⑤','⑥','⑦','⑧','⑨','⑩',
		'⑾', '⑿', '⒀', '⒁', '⒂','⒃', '⒄', '⒅', '⒆', '⒇'};	//⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇
	
	int count;		// 有几个柱子
	int count2;
	
	int moveCount;

	int[][] tower;
	

	public TinyGame6(int count, int count2) {
		this.count = count;
		this.count2 = count2;
	}
	public String getName() {
		return "汉诺塔" + count + "-" + count2;
	}

	// 生成新的题目
	public void reset() {
		moveCount = 0;
		tower = new int[count][];
		for(int i = 0;i < count;i++) {
			tower[i] = new int[count2];
		}
		for(int i = 0;i < count2;i++) {
			tower[1][i] = i + 1;
		}
	}

	public int getCount2() {
		return count2;
	}
	public void setCount2(int count2) {
		this.count2 = count2;
	}
	public int[][] getTower() {
		return tower;
	}
	public void setTower(int[][] tower) {
		this.tower = tower;
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
		return "/tiny/6v.jsp";
	}

	public void init() {
		reset();
	}

	public int getTowerLast(int line) {
		for(int i = 0;i < count2;i++) {
			if(tower[line][i] == 0)
				return i - 1;
		}
		return count2 - 1;
	}
	public void move(int from, int to) {
		if(from == to)
			return;
		int fromPos = getTowerLast(from);
		int toPos = getTowerLast(to);
		if(fromPos < 0)
			return;
		if(toPos != -1 && tower[from][fromPos] < tower[to][toPos])
			return;
		
		tower[to][toPos + 1] = tower[from][fromPos];
		tower[from][fromPos] = 0;
		moveCount++;
	}
	public int getLastMark(int line) {
		for(int i = 0;i < count2;i++) {
			if(tower[line][i] == 0) {
				if(i == 0)
					return 0;
				else
					return tower[line][i - 1];
			}
		}
		return tower[line][count2 - 1];
	}
	public int getMoveCount() {
		return moveCount;
	}
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	public void checkGame() {
		if(getTowerLast(0) == count2 - 1)
			setStatus(1);
	}
}
