<%@ page contentType="text/plain;charset=utf-8"%><%@ page import="net.joycool.wap.mont.*"%><%
System.out.println("mont: provision1");
OrderRes res = Provision.getOrderRes(Provision.getOrderReq(request));
%><?xml version="1.0" encoding="utf-8"?>
<SOAP-ENV:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/">
<SOAP-ENV:Header>
<TransactionID xmlns="http://www.monternet.com/dsmp/schemas/"><%=res.getTransactionId()%></TransactionID>
</SOAP-ENV:Header>
<SOAP-ENV:Body>
<SyncOrderRelationResp xmln='12345.com'>
<Version>1.5.0</Version>
<MsgType>SyncOrderRelationResp</MsgType>
<hRet><%=res.getHRet()%></hRet>
</SyncOrderRelationResp>
</SOAP-ENV:Body>
</SOAP-ENV:Envelope>