package net.joycool.wap.service.infc;

import java.util.Vector;
import net.joycool.wap.bean.huangye.JCLinkHuangYeBean;



/**
 * @author mcq
 *
 */
public interface IHuangYeService {
	//通过条件获取jc_link_huangye记录
	public Vector getJCLinkHuangYeList(String condition);
	//查询jc_link_huangye的记录数
	public int getHuangYeRecords(int mark);
	//按number进行排序，分页查询黄页信息
	public Vector getHuangYeList(int pageNo,int perPageRecords, int mark);
	//获取jc_link_huangye中最大number
	public int getHYMaxNum(int mark);
	//判断number是否已经存在
	public JCLinkHuangYeBean numExist(int num, int mark);
	//判断输入的link_id是否在link_record中存在
	public boolean linkIdExist(String table, String condition);
	//向数据库中添加新的记录
	public boolean addHY(JCLinkHuangYeBean hy);
	//修改记录
	public boolean alterHY(JCLinkHuangYeBean hy);
	//删除记录
	public boolean deleteHY(int id);
	//交换排名
	public boolean changNumber(JCLinkHuangYeBean one,JCLinkHuangYeBean two);
}
