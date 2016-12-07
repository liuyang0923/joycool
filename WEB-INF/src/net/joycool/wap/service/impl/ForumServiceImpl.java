/*
 * Created on 2005-12-24
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.forum.ForumBean;
import net.joycool.wap.service.infc.IForumService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *
 */
public class ForumServiceImpl implements IForumService{
    
    /* (non-Javadoc)
     * @see net.joycool.sp.service.infc.IForumService#addForum(net.joycool.sp.bean.ForumBean)
     */
    public boolean addForum(ForumBean forum) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO sp_forum(name, back_to) VALUES(?, ?, now())";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setString(1, forum.getName());
            pstmt.setString(2, forum.getBackTo());
       } catch (SQLException e) {
            e.printStackTrace();
            dbOp.release();
            return false;
        }
        //执行
        dbOp.executePstmt();

        //释放资源
        dbOp.release();

        return true;
    }
    /* (non-Javadoc)
     * @see net.joycool.sp.service.infc.IForumService#getForum(java.lang.String)
     */
    public ForumBean getForum(String condition) {
        ForumBean forum = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM sp_forum";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }
        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                forum = new ForumBean();
                forum.setId(rs.getInt("id"));
                forum.setName(rs.getString("name"));
                forum.setBackTo(rs.getString("back_to"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return forum;
    }
    /* (non-Javadoc)
     * @see net.joycool.sp.service.infc.IForumService#getForumList(java.lang.String)
     */
    public Vector getForumList(String condition) {
        Vector forumList = new Vector();
        ForumBean forum = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM sp_forum";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }        

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                forum = new ForumBean();
                forum.setId(rs.getInt("id"));
                forum.setName(rs.getString("name"));
                forum.setBackTo(rs.getString("back_to"));
                forumList.add(forum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return forumList;
    }
}
