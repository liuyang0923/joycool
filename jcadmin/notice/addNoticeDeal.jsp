<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="java.sql.SQLException"%><%@ page import="java.sql.PreparedStatement"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.NoticeCacheUtil" %><%@ page import="net.joycool.wap.framework.*"%><%// 结果页面提示参数
			String hint = "";
			// 获取并处理参数

			String userType = StringUtil.convertNull(request
					.getParameter("userType"));
			String area = StringUtil.convertNull(request.getParameter("area"));
			String gender = StringUtil.convertNull(request
					.getParameter("gender"));
			String roomId = StringUtil.convertNull(request
					.getParameter("roomId"));
			String days = StringUtil.convertNull(request.getParameter("days"));
			String type = StringUtil.convertNull(request.getParameter("type"));
			String rank1 = StringUtil.convertNull(request.getParameter("rank1"));
			String rank2 = StringUtil.convertNull(request.getParameter("rank2"));

			String title = StringUtil
					.convertNull(request.getParameter("title"));
			String content = StringUtil.convertNull(request
					.getParameter("content"));
			String link = StringUtil.convertNull(request.getParameter("link"));
			String hideLink = StringUtil.convertNull(request
					.getParameter("hideLink"));

			// 参数后台验证

			if ("".equals(userType)) {
				hint = "01";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}

			if ("".equals(area)) {
				hint = "02";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}
			if ("".equals(gender)) {
				hint = "03";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}
			if ("".equals(roomId)) {
				hint = "04";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}
			if ("".equals(days)) {
				hint = "05";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}

			if ("".equals(type)) {
				hint = "06";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}
			if ("".equals(title)) {
				hint = "07";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}
			if ("".equals(content) && "".equals(link)) {
				hint = "08";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}

			// 去掉两边空格
			title = title.trim();
			content = content.trim();
			link = link.trim();
			hideLink = hideLink.trim();

			// 根据类型生成对应取得userid的sql
			String sql = "";

			if ("01".equals(userType)) {
				// 按地区
				sql = "SELECT id as user_id FROM user_info where cityname = '"
						+ area + "'";

			}
			if ("02".equals(userType)) {
				// 按性别
				sql = "SELECT id as user_id FROM user_info where gender = "
						+ gender;
			}
			if ("03".equals(userType)) {
				// 所有在线用户
				sql = "SELECT user_id FROM jc_online_user";
			}
			if ("04".equals(userType)) {
				// 某个聊天室的用户
				sql = "select user_id from jc_room_user where room_id = "
						+ roomId;
			}
			if ("05".equals(userType)) {
				// 所有的聊天室创建者
				sql = "select distinct creator_id as user_id from jc_room";
			}
			if ("06".equals(userType)) {
				// 在小黑屋里的用户
				sql = "select user_id from jc_room_forbid";
			}
			if ("07".equals(userType)) {
				// 给X天没来过，目前在线的用户
				sql = "select a.user_id from user_status a ,jc_room_online b "
						+ "where (TO_DAYS(NOW())-TO_DAYS(a.last_logout_time)) >= "
						+ days + " and a.user_id=b.user_id";
			}
			//增加按等级发布通知
			if ("08".equals(userType)) {
				// 按等级区间划分出来的用户
				sql = "select user_id,rank from user_status where rank>="+rank1+" and rank <="+rank2;
			}

			// 取得满足条件的用户，向通表中添加信息
			//liuyi 2006-10-17 程序优化bug修改 start
			int addCount = 0;
			ArrayList errorList = null;
		
			try {				
				// 查询
                List userIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
							
				hint = "09";

				boolean result = false;
				int userId;

				// 有结果，向通表中添加信息
			for(int i=0;userIdList!=null && i<userIdList.size();i++){
				Integer id = (Integer)userIdList.get(i);
				if(id==null)continue;
					
				result = false;
				addCount++;
				userId = id.intValue();
				sql = "INSERT INTO jc_notice set user_id=?, title=?, content=?, link=?, hide_url=?, status=?, type=?, create_datetime=now()";

				DbOperation dbOp = null;
				try{
					dbOp = new DbOperation();
					dbOp.init();
				
					// 准备
					if (!dbOp.prepareStatement(sql)) {
						dbOp.release();
						//response.sendRedirect("addNoticeFailure.jsp?hint="
						//		+ hint);
						BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint="
								+ hint, response);
						return;
					}
					// 传递参数
					PreparedStatement pstmt = dbOp.getPStmt();

					pstmt.setInt(1, userId);
					pstmt.setString(2, title);
					pstmt.setString(3, content);
					pstmt.setString(4, link);
					pstmt.setString(5, hideLink);
					pstmt.setInt(6, 0);
					pstmt.setInt(7, Integer.parseInt(type));

					// 执行
					result = dbOp.executePstmt();
					if (!result) {
						if (errorList == null) {
							errorList = new ArrayList();
						}
						errorList.add(new Integer(userId));
					}
				} catch (Exception ex) {
						ex.printStackTrace();
				} finally {
					if(dbOp!=null){
						dbOp.release();
					}					
				}
									
				NoticeCacheUtil.removeNoNoticeUserId(userId);
			    NewNoticeCacheUtil.addUserNoticeById(userId);
			}
			} catch (Exception e) {
				e.printStackTrace();
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}
            
            if (addCount == 0) {
				// 没有满足条件的用户
				hint = "10";
				//response.sendRedirect("addNoticeFailure.jsp?hint=" + hint);
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeFailure.jsp?hint=" + hint, response);
				return;
			}

			// 添加成功时
			if (errorList == null) {
				//response.sendRedirect(response
				//		.encodeURL("addNoticeSuccess.jsp?addCount="
				//				+ addCount));
				BaseAction.sendRedirect("/jcadmin/notice/addNoticeSuccess.jsp?addCount="
						+ addCount, response);
				return;
			} else {
				// 有个别用户没有发送到的情况
				out.print("发送通知失败！已发关给" + addCount
						+ "个用户，可能有如下用户没有发送到<br><br>");
				for (int i = 0; i < errorList.size(); i++) {
					out.print("user_id:" + (String) errorList.get(i)
							+ "<br>");
				}
			}
			//liuyi 2006-10-17 程序优化bug修改 end
		%>
