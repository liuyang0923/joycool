/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.service.infc;

import net.joycool.wap.bean.sp.HistoryBean;
import net.joycool.wap.bean.sp.OrderBean;

/**
 * @author lbj
 *
 */
public interface ISpService {
    public boolean addOrder(OrderBean order);
    public boolean addOrderByCount(OrderBean order);
    public boolean updateOrder(String set, String condition);
    public boolean updateOrderByCount(String set, String condition);
    public boolean addHistory(HistoryBean history);
    public OrderBean getOrder(String condition);
    public OrderBean getOrderByCount(String condition);
}
