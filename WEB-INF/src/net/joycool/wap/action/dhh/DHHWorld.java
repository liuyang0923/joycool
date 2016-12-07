package net.joycool.wap.action.dhh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

;

/**
 * @author liq
 * @explain：
 * @datetime:2007-4-20 16:18:10
 */
public class DHHWorld {
	public static DHHService service = new DHHService();
	public static DHHWorld world = new DHHWorld();
	
	public static HashMap userMap = new HashMap();

	public HashMap cityMap = null;
	public List cityList = null;

	public HashMap shipMap = null;
	public List shipList = null;

	// 物品信息
	public HashMap productMap = null;

	public static int cityNumber = 12;

	public static int[][] cityDist = {
//	     	 大连	天津 	 烟台 	上海 	 杭州 	 温州 	福州 	台北 	泉州 	 厦门 	 广州 	 香港
			{ 0,    100,	 200,     0,	  0,  	  0,      0,   	  0,	  0,	   0, 	    0,      0},//大连

			{ 100,	0,	     100,	 200,     0,      0,      0,      0,      0,       0,       0,      0},//天津

			{ 200,	100,	 0,	     100,     200,    0,      0,      0,      0,       0,       0,      0},//烟台

			{ 0,	200,	 100,	 0,	      100,    200,    0,      0,      0,       0,       0,      0},//上海

			{ 0,    0,	     200,	 100,	  0,	  100,    200,    0,      0,       0,       0,      0},//杭州

			{ 0,    0,       0,	     200,	  100,	  0,	  100,    200,    0,       0,       0,      0},//温州 
			
			{ 0,    0,	     0,      0,       200,	  100,	  0,	  100,    200,     0,       0,      0},//福州 
			 
			{ 0,    0,	     0,      0,       0,      200,	  100,	  0,	  100,     200,     0,      0},//台北
			
			{ 0,    0,	     0,		 0,		  0,	  0,	  200,	  100,	  0,	   100,     200,    0},//泉州
			
			{ 0,    0,	     0,		 0,		  0,	  0,	  0,	  200,    100,     0,       100,  200},//厦门
			
			{ 0,    0,	     0,		 0,		  0,	  0,	  0,	  0,	  200,     100,     0,    100},//广州
			
			{ 0,    0,	     0,		 0,		  0,	  0,	  0,	  0,	  0,	   200,     100,    0},//香港
	};

	public static DHHService getService() {
		return service;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取浮生记用户属性
	 * @datetime:2007-3-27 12:11:41
	 * @param pkUserId
	 * @return
	 * @return PKUserBean
	 */
	public static DhhUserBean getDhhUser(int UserId) {
		DhhUserBean User = (DhhUserBean) getDHHUserMap().get(
				new Integer(UserId));
		if (User == null) {
			User = getService().getUser("user_id=" + UserId + " limit 0 , 1");
			if (User != null) {
				getDHHUserMap().put(new Integer(User.getUserId()), User);
			}
		}
		return User;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取所有浮生记在线用户信息
	 * @datetime:2007-3-26 17:49:48
	 * @return
	 * @return HashMap
	 */

	public static HashMap getDHHUserMap() {
		return userMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取城市信息map
	 * @datetime:2007-3-26 17:37:16
	 * @return
	 * @return HashMap
	 */
	static byte[] lock1 = new byte[0];

	public void loadCityMap() {
		if (cityMap != null) {
			return;
		}
		cityMap = new HashMap();
		cityList = new ArrayList();
		productMap = new HashMap();

		Vector productList = getService().getProductList(" 1=1 ");
		for (int j = 0; j < productList.size(); j++) {
			DhhProductBean productbean = (DhhProductBean) productList.get(j);
			productMap.put(new Integer(productbean.getId()), productbean);
		}
		Vector CityList = getService().getCityList(" 1=1");
		for (int j = 0; j < CityList.size(); j++) {
			DhhCityBean citybean = (DhhCityBean) CityList.get(j);
			// 城市物品信息
			Vector CitProList = getService().getCitProList(
					"city_id = " + citybean.getId());

			HashMap citpromap = new HashMap();
			for (int i = 0; i < CitProList.size(); i++) {
				DhhCitProBean CitPro = (DhhCitProBean) CitProList.get(i);
				if (CitPro != null) {
					CitPro.setProductname(((DhhProductBean) productMap
									.get(new Integer(CitPro.getProductid())))
									.getName());
					citpromap.put(new Integer(CitPro.getProductid()), CitPro);
				}
			}
			citybean.setProductMap(citpromap);
			// 城市物品信息
			// // 城市交通信息
			// int[] newCity = new int[cityNumber];
			// for (int i = 0; i < cityNumber; i++)
			// newCity[i] = City[citybean.getId()-1][i];
			// citybean.setTransportation(newCity);
			// // 城市交通信息
			cityMap.put(new Integer(citybean.getId()), citybean);
			cityList.add(citybean);
		}

	}

	public void resetCityMap() {
		Iterator iter = cityList.iterator();
		while(iter.hasNext()) {
			DhhCityBean citybean = (DhhCityBean) iter.next();

			Iterator iter2 = citybean.getProductMap().values().iterator();
			while(iter2.hasNext()) {
				DhhCitProBean pro = (DhhCitProBean)iter2.next();

				pro.refresh();	//	恢复数量和价格
			}
		}
		
	}

	
	public void loadShipMap() {
		if (shipMap != null) {
			return;
		}
		shipMap = new HashMap();
		shipList = new ArrayList();
		Vector ShipList = getService()
				.getShipList(" 1=1 order by id");
		for (int j = 0; j < ShipList.size(); j++) {
			DhhShipBean shipbean = (DhhShipBean) ShipList.get(j);
			shipMap.put(new Integer(shipbean.getId()), shipbean);
			shipList.add(shipbean);
		}
	}
//
//	public static int loadMaxProduct() {
//		if (Product_Max != 0)
//			return Product_Max;
//		else
//			Product_Max = SqlUtil.getIntResult(
//					"Select max(id) from dhh_product ", Constants.DBShortName);
//		return Product_Max;
//	}

	/**
	 * 
	 * @author macq
	 * @explain：清空浮生记缓存
	 * @datetime:2007-3-28 18:48:46
	 * @return void
	 */
	public void clear() {
		userMap.clear();
		cityMap = null;
		shipMap = null;
        productMap = null;
	}
	
	public int getProductPrice(DhhCityBean city, UserBagBean pro) {
		Integer id = new Integer(pro.getProductid());
		DhhCitProBean cp = (DhhCitProBean)city.getProductMap().get(id);
		if(cp != null)
			return cp.getSellrate();
		
		DhhProductBean product = (DhhProductBean)productMap.get(id);
		if(product != null)
			return product.getSell();
		
		return 0;
	}

	public static DHHWorld getWorld() {
		return world;
	}

	private void load() {
		loadShipMap();
		loadCityMap();
	}

	public DHHWorld() {
		load();
	}
}
