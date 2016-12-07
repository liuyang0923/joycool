<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="炫铃搜索">
<p align="left">
请输入炫铃名称：<br/>
    <input name="gameName" maxlength="20" />
    <br/>
         <anchor title ="search Ring">查询
         <go href="SearchRing.do" method="post">
             <postfield name="ringName" value="$(ringName)"/>
         </go>
         </anchor>
</p>
</card>
</wml>