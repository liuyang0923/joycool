/*
 * Created on 2005-12-26
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.guestbook.BoardBean;
import net.joycool.wap.bean.guestbook.ContentBean;
import net.joycool.wap.service.infc.IGuestbookService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *
 */
public class GuestbookServiceImpl implements IGuestbookService{
    
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.IGuestbookService#addContent(net.joycool.wap.bean.guestbook.ContentBean)
     */
    public boolean addContent(ContentBean content) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO sp_guestbook_content("
                + "board_id, nickname, content, create_datetime)"
                + " VALUES(?, ?, ?, now())";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setInt(1, content.getBoardId());
            pstmt.setString(2, content.getNickname());
            pstmt.setString(3, content.getContent());
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
     * @see net.joycool.wap.service.infc.IGuestbookService#getBoard(java.lang.String)
     */
    public BoardBean getBoard(String condition) {
        BoardBean board = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_guestbook_board";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                board = new BoardBean();
                board.setId(rs.getInt("id"));
                board.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return board;
    }
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.IGuestbookService#getContent(java.lang.String)
     */
    public ContentBean getContent(String condition) {
        ContentBean content = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_guestbook_content";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                content = new ContentBean();
                content.setBoardId(rs.getInt("board_id"));
                content.setContent(rs.getString("content"));
                content.setCreateDatetime(rs.getString("create_datetime"));
                content.setId(rs.getInt("id"));
                content.setNickname(rs.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return content;
    }
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.IGuestbookService#getContentList(java.lang.String)
     */
    public Vector getContentList(String condition) {
        Vector contentList = new Vector();
        ContentBean content = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_guestbook_content";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            while (rs.next()) {
                content = new ContentBean();
                content.setBoardId(rs.getInt("board_id"));
                content.setContent(rs.getString("content"));
                content.setCreateDatetime(rs.getString("create_datetime"));
                content.setId(rs.getInt("id"));
                content.setNickname(rs.getString("nickname"));
                contentList.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return contentList;
    }
    
    
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.IGuestbookService#getContentCount(java.lang.String)
     */
    public int getContentCount(String condition) {
        int count = 0;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return 0;
        }

        //查询语句
        String sql = "SELECT count(id) as c_id FROM sp_guestbook_content";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return 0;
        }

        //将结果保存
        try {
            if (rs.next()) {      
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return count;
    }
}
