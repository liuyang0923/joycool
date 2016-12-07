<%@ page contentType="image/vnd.wap.wbmp"%><%@ page import="java.awt.image.BufferedImage,java.io.*"%><%@ page import="net.joycool.wap.util.GifEncoder" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomeUserImageBean" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="java.util.HashMap" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.framework.BaseAction" %><%
try{
String userId=request.getParameter("userId");
Vector userImageList =(Vector)LoadResource.getHomeUserImageList(StringUtil.toInt(userId));
if(userImageList!=null){
HomeUserImageBean userImage=null;
HashMap map=null;
BufferedImage image=null;
BufferedImage ret=null;
String fileName=null;
for(int i=0;i<userImageList.size();i++){
   userImage=(HomeUserImageBean)userImageList.get(i);
   map=LoadResource.getHomImageBigFileMap();
   image=(BufferedImage)map.get(userImage.getImageId()+"");
   if(i==0){
		ret = new BufferedImage(image.getWidth(), image.getHeight(), image.TYPE_BYTE_INDEXED);
	}
ret.getGraphics().drawImage(image, 0, 0, null);
	if(fileName==null){
	fileName=userImage.getImageId()+"";
	}else{
	fileName=fileName+"_"+userImage.getImageId();
	}
}
//mcq_2006-11-30_更改生成图片展现方式(保存在本地)_start
FileOutputStream output = new FileOutputStream("E:/temp/"+fileName+".gif");
new GifEncoder(ret,output,true).encode();  
output.close ();
//OutputStream output = response.getOutputStream ();  
//new GifEncoder(ret,output,false).encode();  
//output.close ();
//mcq_2006-11-30_更改生成图片展现方式(保存在本地)_end
}else{
//response.sendRedirect(("/home/viewAllHome.jsp"));
BaseAction.sendRedirect("/home/viewAllHome.jsp", response);
}
}catch(Exception e)
{
   e.printStackTrace();
}
%>