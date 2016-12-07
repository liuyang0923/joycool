/*
 * Created on 2005-11-25
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.AdvertiseBean;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.UrlMapBean;
import net.joycool.wap.bean.UrlSortBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class CatalogServiceImpl implements ICatalogService {
	private String catalogTableName = "pcatalog";

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICatalogService#getCatalog(java.lang.String)
	 */
	public CatalogBean getCatalog(String condition) {
		CatalogBean catalog = null;
		// 判断是否 调用getCatalog(inr)方法
		String formatSet = condition.replace(" ", "");
		int index = formatSet.indexOf("id=");
		if (index == 0) {

			int offSet = formatSet.indexOf(",");
			int offSet2 = formatSet.indexOf("and");
			if ((offSet == -1) && (offSet2 == -1)) {

				String sub = formatSet.substring(index + 3);
				if (sub != null) {
					int id = Integer.parseInt(sub);
					catalog = getCatalog(id);
					return catalog;

				}
			}
		}

		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询语句
		String sql = "SELECT * FROM " + catalogTableName;
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (sql.indexOf("LIMIT") == -1) {
			sql = sql + " LIMIT 0, 1";
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			if (rs.next()) {
				catalog = new CatalogBean();
				catalog.setId(rs.getInt("id"));
				catalog.setDescription(rs.getString("description"));
				catalog.setLevel(rs.getInt("level"));
				catalog.setName(rs.getString("name"));
				catalog.setOrder(rs.getInt("order"));
				catalog.setParentId(rs.getInt("parent_id"));
				catalog.setType(rs.getString("type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return catalog;

	}

	// 按 ID 加缓存
	public CatalogBean getCatalog(int id) {
		CatalogBean catalog = null;
		catalog = (CatalogBean) OsCacheUtil.get(id + "",
				OsCacheUtil.CATALOG_GROUP, OsCacheUtil.CATALOG_FLUSH_PERIOD);
		if (catalog == null) {

			// 数据操作类
			DbOperation dbOp = new DbOperation();
			// 初始化
			if (!dbOp.init()) {
				return null;
			}

			// 查询语句
			String sql = "SELECT * FROM " + catalogTableName + " where id="
					+ id;

			// if (sql.indexOf("LIMIT") == -1) {
			// sql = sql + " LIMIT 0, 1";
			// }

			// 查询
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
				// 释放资源
				dbOp.release();
				return null;
			}

			// 将结果保存
			try {
				if (rs.next()) {
					catalog = new CatalogBean();
					catalog.setId(rs.getInt("id"));
					catalog.setDescription(rs.getString("description"));
					catalog.setLevel(rs.getInt("level"));
					catalog.setName(rs.getString("name"));
					catalog.setOrder(rs.getInt("order"));
					catalog.setParentId(rs.getInt("parent_id"));
					catalog.setType(rs.getString("type"));
					catalog.setChild_num(rs.getInt("child_num"));
					OsCacheUtil
							.put(id + "", catalog, OsCacheUtil.CATALOG_GROUP);

				} else {
					catalog = new CatalogBean();
					catalog.setId(-1);
					OsCacheUtil.put(id + "", catalog, OsCacheUtil.SPIRIT_GROUP);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
		}
		if (catalog.getId() == -1)
			catalog = null;
		// 返回结果
		return catalog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICatalogService#getCatalogList(java.lang.String)
	 */
	public Vector getCatalogList(String condition) {
		Vector catalogList = new Vector();

		CatalogBean catalog = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询语句
		String sql = "SELECT * FROM " + catalogTableName;
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				catalog = new CatalogBean();
				catalog.setId(rs.getInt("id"));
				catalog.setDescription(rs.getString("description"));
				catalog.setLevel(rs.getInt("level"));
				catalog.setName(rs.getString("name"));
				catalog.setOrder(rs.getInt("order"));
				catalog.setParentId(rs.getInt("parent_id"));
				catalog.setType(rs.getString("type"));
				catalogList.add(catalog);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return catalogList;
	}
	
	/**han_yan_2006-10-24该方法用于电子书的项目,读取广告列表的功能_start**/
	 public Vector getAdvertiseList(String condition){
		Vector advertiseList = new Vector();
		AdvertiseBean ad = null;
		
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}
		
		// 查询语句
		String sql = "SELECT * FROM padvertise";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}
		
		   try {
				while (rs.next()) {
					ad =new AdvertiseBean();
					ad.setID(rs.getInt("id"));
					ad.setAd_name(rs.getString("ad_name"));
					ad.setAd_wap(rs.getString("ad_wap"));
					ad.setStatus(rs.getInt("status"));
					advertiseList.add(ad);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 // 释放资源
		 dbOp.release();
		 return advertiseList;
	 }
	/** han_yan_2006-10-24该方法用于电子书的项目,读取广告列表的功能_end**/
	
	 // *************
	// ***得到资源的id，type为该资源的类别，parentId为该资源的父id
	public int getId(String type, int parentId) {
		CatalogBean catalog;
		String condition = " parent_id = " + parentId + " AND type = '" + type
				+ "'";
		catalog = getCatalog(condition);
		if (catalog == null) {
			return -1;
		} else {
			return catalog.getId();
		}
	}

	public String getTitle(int id) {
		String condition = "id =" + id;
		CatalogBean catalog = getCatalog(condition);
		if (catalog != null) {
			return catalog.getName();
		} else
			return "";
	}

	// **************
	// **得到资源类别列表，parentId是资源的父id ，ICatalogService是处理类；
	public Vector getList(int parentId) {
		Vector list = new Vector();
		String condition = " parent_id = " + parentId
				+ " order by `order`, id desc LIMIT 0,100";
		list = getCatalogList(condition);
		return list;
	}

	// mcq_2007-7-28_增加页面返回上一级的方法_start
	public UrlMapBean getUrlMap(String condition) {
		UrlMapBean urlMap = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_url_map";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				urlMap = this.getUrlMap(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return urlMap;
	}

	public Vector getUrlMapList(String condition) {
		Vector urlMapListList = new Vector();
		UrlMapBean urlMap = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_url_map";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				urlMap = this.getUrlMap(rs);
				urlMapListList.add(urlMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return urlMapListList;
	}

	public boolean addUrlMap(UrlMapBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_url_map(sort_id,module,catalog_id,return_url,title) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSortId());
			pstmt.setString(2, bean.getModule());
			pstmt.setInt(3, bean.getCatalogId());
			pstmt.setString(4, bean.getReturnUrl());
			pstmt.setString(5, bean.getTitle());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}

	public boolean delUrlMap(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_url_map WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateUrlMap(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_url_map SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getUrlMapCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_url_map WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public UrlSortBean getUrlSort(String condition) {
		UrlSortBean urlSort = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_url_sort";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				urlSort = this.getUrlSort(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return urlSort;
	}

	private UrlSortBean getUrlSort(ResultSet rs) throws SQLException {
		UrlSortBean urlSort = new UrlSortBean();
		urlSort.setId(rs.getInt("id"));
		urlSort.setSortName(rs.getString("sort_name"));
		return urlSort;
	}

	private UrlMapBean getUrlMap(ResultSet rs) throws SQLException {
		UrlMapBean urlMap = new UrlMapBean();
		urlMap.setId(rs.getInt("id"));
		urlMap.setSortId(rs.getInt("sort_id"));
		urlMap.setModule(rs.getString("module"));
		urlMap.setCatalogId(rs.getInt("catalog_id"));
		urlMap.setReturnUrl(rs.getString("return_url"));
		urlMap.setTitle(rs.getString("title"));
		return urlMap;
	}

	// mcq_2007-7-28_增加页面返回上一级的方法_end
}
