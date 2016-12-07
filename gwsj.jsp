<%@page import="jc.user.UserAction2"%>
<%@page import="net.joycool.wap.action.user.UserAction"%>
<%@page import="net.joycool.wap.util.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ page import="other.util.*"%>
<%
     //国网手机请求这个地址，带过来两个参数一个是栏目id：froumId;另一个是token
     String forumId=request.getParameter("forumId");
     String token  =request.getParameter("token");
     if(null==token||"".equals(token)){//1如果token为空说明用户没有登录，则直接跳转到相应的论坛版块即可
    	 response.sendRedirect("http://gd.joycool.net/jcforum/forum.jsp?forumId="+forumId);
     }else{//2token不为空，此时需要到国网手机服务端验证token的真实性
    	 //目前测试地址ip为：http://124.207.34.148:8080；后面要改为正式的
    	 String requestUrl="http://124.207.34.148:8080/rest/community/checkToken.json";
    	 HttpResponse httpResponse= Http.doRequest(requestUrl,"POST", "token="+token, "UTF-8");
         if (httpResponse == null) {//2.1如果委会结果为null，则直接跳转到论坛版块
        	 response.sendRedirect("http://gd.joycool.net/jcforum/forum.jsp?forumId="+forumId);
         } else {//返回内容2.2
             String responseStr=new String(httpResponse.getContent());
             if("".equals(responseStr)){//2.2.1如果返回的内容为空，则直接跳转到相应的论坛版块即可
            	 response.sendRedirect("http://gd.joycool.net/jcforum/forum.jsp?forumId="+forumId);
             }else{//2.2.2返回的值不为空，国网手机返回一个手机号的字符串如13655161319
            	 String phone=responseStr;//手机号
            	 String nickName=phone.substring(0,3)+"****"+phone.substring(7);//昵称是通过手机号生成的
            	 //2.2.2.1.校验改手机号是否注册过
            	 if(!Util.sendAccount(phone)){//判断该手机号是否是注册过，返回false是没有注册过
            		 Util.autoRegister(phone);//自动注册
            		 response.sendRedirect("http://gd.joycool.net/jcforum/forum.jsp?forumId="+forumId);
            	 }else{//2.注册过
                     //先登录在跳转
                     response.sendRedirect("http://gd.joycool.net/jcforum/forum.jsp?forumId="+forumId);
            	 }
             }
         }
     }

%>