<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏搜索">
<p align="left">
请输入游戏名称：<br/>
    <input name="gameName" maxlength="20" />
    <br/>
         <anchor title ="search Game">查询
         <go href="SearchGame.do" method="post">
             <postfield name="gameName" value="$(gameName)"/>
         </go>
         </anchor>
</p>
</card>
</wml>