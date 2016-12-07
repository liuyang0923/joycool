package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 采集的地图
 * @datetime:1007-10-24
 */
public class LandMapBean extends MapDataBean{
	final public static int MAX_LENGTH = 50;		// 最大边长
	int id;
	String name;
	int width, height;
	String grid;		// 网格，但是用字符串描述
	int rank;		 // 进这个地图需要的等级
	
	String item1, item1Grid;
	String item2, item2Grid;
	List item1GridList;		// 保存会长出来的地点
	List item2GridList;
	
	List item1List;
	List item2List;
	
	int[] item1Rate;		// 用于概率计算 item保存的格式是，id-概率
	int item1RateSum;
	int[] item2Rate;
	int item2RateSum;
	
	LandNodeBean[][] node;
	
	static int[] itemRateDef = {0, 1};
	public void init() {
		String[] grids = grid.split(",");
		int pos = 0;
		if(width > 0 && width <= MAX_LENGTH && height > 0 && height < MAX_LENGTH && width * height <= grids.length) {
			node = new LandNodeBean[width][];
			for(int i = 0;i < width;i++) {
				node[i] = new LandNodeBean[height];
				for(int j = 0;j < height;j++) {
					node[i][j] = new LandNodeBean();
					node[i][j].setFlag(StringUtil.toInt(grids[pos]));
					pos++;
				}
			}
		}
		
		List item1Grids = StringUtil.toIntss(item1Grid, 2, null);
		item1GridList = new ArrayList();
		for(int i = 0;i < item1Grids.size();i++) {
			int[] t = (int[])item1Grids.get(i);
			item1GridList.add(getNode(t[0], t[1]));
		}
		List item2Grids = StringUtil.toIntss(item2Grid, 2, null);
		item2GridList = new ArrayList();
		for(int i = 0;i < item2Grids.size();i++) {
			int[] t = (int[])item2Grids.get(i);
			item2GridList.add(getNode(t[0], t[1]));
		}
		
		item1List = StringUtil.toIntss(item1, 2, itemRateDef);
		item1Rate = new int[item1List.size()];
		for(int i = 0;i < item1Rate.length;i++) {
			int[] t = (int[])item1List.get(i);
			item1Rate[i] = t[1];
		}
		item1RateSum = RandomUtil.sumRate(item1Rate);
		
		item2List = StringUtil.toIntss(item2, 2, itemRateDef);
		item2Rate = new int[item2List.size()];
		for(int i = 0;i < item2Rate.length;i++) {
			int[] t = (int[])item2List.get(i);
			item2Rate[i] = t[1];
		}
		item2RateSum = RandomUtil.sumRate(item2Rate);
	}
	
	public int getRandomItem1() {
		if(item1List.isEmpty())
			return 0;
		int rnd = RandomUtil.randomRateInt(item1Rate, item1RateSum);
		int[] t = (int[])item1List.get(rnd);
		return t[0];
	}
	/**
	 * @return Returns the item1GridList.
	 */
	public List getItem1GridList() {
		return item1GridList;
	}

	/**
	 * @return Returns the item2GridList.
	 */
	public List getItem2GridList() {
		return item2GridList;
	}

	public int getRandomItem2() {
		if(item2List.isEmpty())
			return 0;
		int rnd = RandomUtil.randomRateInt(item2Rate, item2RateSum);
		int[] t = (int[])item2List.get(rnd);
		return t[0];
	}
	
	public LandNodeBean getNode(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height)
			return null;
		return node[x][y];
	}
	
	/**
	 * @return Returns the grid.
	 */
	public String getGrid() {
		return grid;
	}
	/**
	 * @param grid The grid to set.
	 */
	public void setGrid(String grid) {
		this.grid = grid;
	}
	/**
	 * @return Returns the height.
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height The height to set.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the item1.
	 */
	public String getItem1() {
		return item1;
	}

	/**
	 * @param item1 The item1 to set.
	 */
	public void setItem1(String item1) {
		this.item1 = item1;
	}

	/**
	 * @return Returns the item1Grid.
	 */
	public String getItem1Grid() {
		return item1Grid;
	}

	/**
	 * @param item1Grid The item1Grid to set.
	 */
	public void setItem1Grid(String item1Grid) {
		this.item1Grid = item1Grid;
	}

	/**
	 * @return Returns the item2.
	 */
	public String getItem2() {
		return item2;
	}

	/**
	 * @param item2 The item2 to set.
	 */
	public void setItem2(String item2) {
		this.item2 = item2;
	}

	/**
	 * @return Returns the item2Grid.
	 */
	public String getItem2Grid() {
		return item2Grid;
	}

	/**
	 * @param item2Grid The item2Grid to set.
	 */
	public void setItem2Grid(String item2Grid) {
		this.item2Grid = item2Grid;
	}

	public String getLink(HttpServletResponse response) {
		return "A" + "<a href=\"" + ("land.jsp?id=" + id) + "\">" + name + "</a>";
	}
	public String getEditLink(HttpServletResponse response) {
		return "A" + "<a href=\"" + ("editLand.jsp?id=" + id) + "\">" + name + "</a>";
	}
}
