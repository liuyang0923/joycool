<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="图片搜索">
<p align="left">
请输入图片名称：<br/>
    <input name="imageName" maxlength="20" value="v"/>
    <br/>
         <anchor title ="search Image">查询
         <go href="SearchImage.do" method="post">
             <postfield name="imageName" value="$(imageName)"/>
         </go>
         </anchor>
</p>
</card>
</wml>