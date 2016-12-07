package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.dummy.DummyTypeBean;

/**
 * @author macq
 * @datetime 2006-12-12 下午04:52:43
 * @explain 虚拟物品接口
 */
public interface IDummyService {
    //虚拟物品类型
	public boolean addDummyType(DummyTypeBean dummyType);
	public DummyTypeBean getDummyType(String condition);
	public Vector getDummyTypeList(String condition);
	public boolean deleteDummyType(String condition);
	public boolean updateDummyType(String set, String condition);
	public int getDummyTypeCount(String condition);
	
    //虚拟物品
	public boolean addDummyProduct(DummyProductBean dummyType);
	public DummyProductBean getDummyProduct(String condition);
	public Vector getDummyProductList(String condition);
	public boolean deleteDummyProduct(String condition);
	public boolean updateDummyProduct(String set, String condition);
	public int getDummyProductCount(String condition);
	//道具商店
	public Vector getDummyProductLists(String condition);
	public DummyProductBean getDummyProducts(int dummyId);
	//道具商店后台
	public void addDummyProducts(DummyProductBean bean);
}
