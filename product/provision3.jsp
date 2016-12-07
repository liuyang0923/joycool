<%@ page contentType="text/plain;charset=utf-8"%><%@ page import="net.joycool.wap.mont.*"%><%
System.out.println("mont: provision3");
OrderReq req = Provision.getOrderReq(request);
System.out.println("mont getTransactionID: " + req.getTransactionID());
System.out.println("mont getDevicetypeSend: " + req.getDevicetypeSend());
System.out.println("mont getDevicetypeDest: " + req.getDevicetypeDest());
System.out.println("mont getDeviceidSend: " + req.getDeviceidSend());
System.out.println("mont getDeviceidDest: " + req.getDeviceidDest());
System.out.println("mont getUseridtypeFee: " + req.getUseridtypeFee());
System.out.println("mont getUseridtypeDest: " + req.getUseridtypeDest());
System.out.println("mont getIsdnFee: " + req.getIsdnFee());
System.out.println("mont getIsdnDest: " + req.getIsdnDest());
System.out.println("mont getPseudoCodeFee: " + req.getPseudoCodeFee());
System.out.println("mont getPseudoCodeDest: " + req.getPseudoCodeDest());
System.out.println("mont getLinkID: " + req.getLinkID());
System.out.println("mont getActionID: " + req.getActionID());
System.out.println("mont getActionReasonID: " + req.getActionReasonID());
System.out.println("mont getSpID: " + req.getSpID());
System.out.println("mont getSpServiceID: " + req.getSpServiceID());
System.out.println("mont getAccessMode: " + req.getAccessMode());
System.out.println("mont getFeatureStr: " + req.getFeatureStr());
OrderRes res = Provision.getOrderRes(req);
System.out.println("mont getTransactionID: " + res.getTransactionId());
System.out.println("mont getHRet: " + res.getHRet());
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