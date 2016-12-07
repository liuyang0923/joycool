<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%

TinyAction tiny = new TinyAction(request, response);
TinyGame game = tiny.getGame();		// 当前游戏，如果结束，会自动删除，当前页面必须自己处理这个game，例如保存
if(game != null){
if(game.isStatusPlay()){
tiny.redirect(game.getGameURL());
return;
}
String uri = tiny.getURI();
if(!uri.equals(game.getReturnURL())){
tiny.redirect(game.getReturnURL());
return;
}
tiny.setUserGame(null);
}
%>