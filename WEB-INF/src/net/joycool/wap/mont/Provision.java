/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.mont;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.joycool.wap.bean.WapServiceBean;
import net.joycool.wap.bean.sp.HistoryBean;
import net.joycool.wap.bean.sp.OrderBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ISpService;
import net.joycool.wap.util.WapServiceUtil;

import org.w3c.dom.Document;

/**
 * @author lbj
 *  
 */
public class Provision {
    /**
     * 根据请求得到一个OrderReq对象。
     * 
     * @param request
     * @return
     */
    //    public static OrderReq getOrderReq(HttpServletRequest request) {
    //        try {
    //            InputStream inputStream = request.getInputStream();
    //            XMLParser parser = new XMLParser(inputStream);
    //            Element root = parser.getReqRoot();
    //            Namespace ns = Namespace.getNamespace("SOAP-ENV",
    //                    "http://schemas.xmlsoap.org/soap/envelope/");
    //
    //            String transactionId = root.getChild("Header", ns).getChild(
    //                    "TransactionID").getText();
    //            Element body = root.getChild("Body", ns);
    //            Element syncOrderRelationReq = body
    //                    .getChild("SyncOrderRelationReq");
    //            String version = syncOrderRelationReq.getChild("Version").getText();
    //            String messageType = syncOrderRelationReq.getChild("MsgType")
    //                    .getText();
    //            Address sendAddress = new Address();
    //            sendAddress.setDeviceType(syncOrderRelationReq.getChild(
    //                    "Send_Address").getChild("DeviceType").getText());
    //            sendAddress.setDeviceId(syncOrderRelationReq.getChild(
    //                    "Send_Address").getChild("DeviceID").getText());
    //            Address destAddress = new Address();
    //            destAddress.setDeviceType(syncOrderRelationReq.getChild(
    //                    "Dest_Address").getChild("DeviceType").getText());
    //            destAddress.setDeviceId(syncOrderRelationReq.getChild(
    //                    "Dest_Address").getChild("DeviceID").getText());
    //            User feeUser = new User();
    //            feeUser.setUserIdType(syncOrderRelationReq.getChild("FeeUser_ID")
    //                    .getChild("UserIDType").getText());
    //            feeUser.setMsisdn(syncOrderRelationReq.getChild("FeeUser_ID")
    //                    .getChild("MSISDN").getText());
    //            feeUser.setPseudoCode(syncOrderRelationReq.getChild("FeeUser_ID")
    //                    .getChild("PseudoCode").getText());
    //            User destUser = new User();
    //            destUser.setUserIdType(syncOrderRelationReq.getChild("DestUser_ID")
    //                    .getChild("UserIDType").getText());
    //            destUser.setMsisdn(syncOrderRelationReq.getChild("DestUser_ID")
    //                    .getChild("MSISDN").getText());
    //            destUser.setPseudoCode(syncOrderRelationReq.getChild("DestUser_ID")
    //                    .getChild("PseudoCode").getText());
    //            int actionId = Integer.parseInt(syncOrderRelationReq.getChild(
    //                    "ActionID").getText());
    //            int actionReasonId = Integer.parseInt(syncOrderRelationReq
    //                    .getChild("ActionReasonID").getText());
    //            String spId = syncOrderRelationReq.getChild("SPID").getText();
    //            String spServiceId = syncOrderRelationReq.getChild("SPServiceID")
    //                    .getText();
    //
    //            OrderReq orderReq = new OrderReq();
    //            orderReq.setActionId(actionId);
    //            orderReq.setActionReasonId(actionReasonId);
    //            orderReq.setDestAddress(destAddress);
    //            orderReq.setDestUser(destUser);
    //            orderReq.setFeeUser(feeUser);
    //            orderReq.setMessageType(messageType);
    //            orderReq.setSendAddress(sendAddress);
    //            orderReq.setSpId(spId);
    //            orderReq.setSpServiceId(spServiceId);
    //            orderReq.setTransactionId(transactionId);
    //            orderReq.setVersion(version);
    //
    //            return orderReq;
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //    }
    /**
     * 根据一个OrderReq取得一个Response。 注：实际逻辑没做处理。
     * 
     * @param orderReq
     * @return
     */
    //  public static String getOrderRes(OrderReq orderReq) {
    //        XMLParser parser = new XMLParser("/mont/provision.xml");
    //        Document res = parser.getReqDoc();
    //        Element root = parser.getReqRoot();
    //        Namespace ns = Namespace.getNamespace("SOAP-ENV",
    //                "http://schemas.xmlsoap.org/soap/envelope/");
    //        if (orderReq == null) {
    //            root.getChild("Body", ns).getChild("SyncOrderRelationResp")
    //                    .getChild("hRet").setText("1");
    //        } else {
    //            root.getChild("Header", ns).getChild("TransactionID").setText(
    //                    orderReq.getTransactionId());
    //            root.getChild("Body", ns).getChild("SyncOrderRelationResp")
    //                    .getChild("hRet").setText("0");
    //        }
    //
    //        XMLOutputter outp = new XMLOutputter("", false, "UTF-8");
    //        String response = outp.outputString(res);
    //        return null;
    //   }
    /**
     * 根据请求得到一个OrderReq对象。
     * 
     * @param request
     * @return
     */
    public static OrderReq getOrderReq(HttpServletRequest request) {
        try {
            InputStream in = request.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(in);

            String transactionID = null;
            String devicetype_send = null;
            String devicetype_dest = null;
            String deviceid_send = null;
            String deviceid_dest = null;
            String useridtype_Fee = null;
            String useridtype_Dest = null;
            String isdn_Fee = null;
            String isdn_Dest = null;
            String pseudoCode_Fee = null;
            String pseudoCode_Dest = null;
            String linkID = null;
            String actionID = null;
            String actionReasonID = null;
            String spID = null;
            String spServiceID = null;
            String accessMode = null;
            String featureStr = null;

            //TransactionID
            if (document.getElementsByTagName("TransactionID") != null
                    && document.getElementsByTagName("TransactionID").item(0) != null
                    && document.getElementsByTagName("TransactionID").item(0)
                            .getFirstChild() != null) {
                transactionID = document.getElementsByTagName("TransactionID")
                        .item(0).getFirstChild().getNodeValue();
                //System.out.println("ProvisionCmcc get TransactionID="
                //        + transactionID);
            } else {
                //System.out.println("ProvisionCmcc get TransactionID ==
                // null");
            }

            //DeviceType
            if (document.getElementsByTagName("DeviceType") != null
                    && document.getElementsByTagName("DeviceType").item(0) != null
                    && document.getElementsByTagName("DeviceType").item(0)
                            .getFirstChild() != null) {
                devicetype_send = document.getElementsByTagName("DeviceType")
                        .item(0).getFirstChild().getNodeValue();
                //System.out.println("ProvisionCmcc get send
                // address.DeviceType="
                //        + devicetype_send);
            } else {
                //System.out
                //        .println("ProvisionCmcc get send address.DeviceType ==null");
            }
            if (document.getElementsByTagName("DeviceType") != null
                    && document.getElementsByTagName("DeviceType").item(1) != null
                    && document.getElementsByTagName("DeviceType").item(1)
                            .getFirstChild() != null) {
                devicetype_dest = document.getElementsByTagName("DeviceType")
                        .item(1).getFirstChild().getNodeValue();
                //System.out.println("ProvisionCmcc get dest
                // address.DeviceType=" + devicetype_dest);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get dest address.DeviceType ==null");
            }

            //DeviceID
            if (document.getElementsByTagName("DeviceID") != null
                    && document.getElementsByTagName("DeviceID").item(0) != null
                    && document.getElementsByTagName("DeviceID").item(0)
                            .getFirstChild() != null) {
                deviceid_send = document.getElementsByTagName("DeviceID").item(
                        0).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get send address.DeviceID="
                //                        + deviceid_send);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get send address.DeviceID ==null");
            }
            if (document.getElementsByTagName("DeviceID") != null
                    && document.getElementsByTagName("DeviceID").item(1) != null
                    && document.getElementsByTagName("DeviceID").item(1)
                            .getFirstChild() != null) {
                deviceid_dest = document.getElementsByTagName("DeviceID").item(
                        1).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get dest address.DeviceID="
                //                        + deviceid_dest);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get dest address.DeviceID ==null");
            }

            //UserIDType
            if (document.getElementsByTagName("UserIDType") != null
                    && document.getElementsByTagName("UserIDType").item(0) != null
                    && document.getElementsByTagName("UserIDType").item(0)
                            .getFirstChild() != null) {
                useridtype_Fee = document.getElementsByTagName("UserIDType")
                        .item(0).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get send
                // FeeUser_ID.UserIDType="
                //                        + useridtype_Fee);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get send FeeUser_ID.UserIDType
                // ==null");
            }
            if (document.getElementsByTagName("UserIDType") != null
                    && document.getElementsByTagName("UserIDType").item(1) != null
                    && document.getElementsByTagName("UserIDType").item(1)
                            .getFirstChild() != null) {
                useridtype_Dest = document.getElementsByTagName("UserIDType")
                        .item(1).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get dest
                // DestUser_ID.UserIDType="
                //                        + useridtype_Fee);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get dest DestUser_ID.UserIDType
                // ==null");
            }

            //MSISDN
            if (document.getElementsByTagName("MSISDN") != null
                    && document.getElementsByTagName("MSISDN").item(0) != null
                    && document.getElementsByTagName("MSISDN").item(0)
                            .getFirstChild() != null) {
                isdn_Fee = document.getElementsByTagName("MSISDN").item(0)
                        .getFirstChild().getNodeValue();
                if (isdn_Fee == null) {
                    //                    System.out
                    //                            .println("ProvisionCmcc get send FeeUser_ID.MSISDN
                    // ==null");
                } else {
                    //                    System.out.println("ProvisionCmcc get send
                    // FeeUser_ID.MSISDN="
                    //                            + isdn_Fee);
                }
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get send FeeUser_ID.MSISDN ==null");
            }
            if (document.getElementsByTagName("MSISDN") != null
                    && document.getElementsByTagName("MSISDN").item(1) != null
                    && document.getElementsByTagName("MSISDN").item(1)
                            .getFirstChild() != null) {
                isdn_Dest = document.getElementsByTagName("MSISDN").item(1)
                        .getFirstChild().getNodeValue();
                if (isdn_Dest == null) {
                    //                    System.out
                    //                            .println("ProvisionCmcc get dest DestUser_ID.MSISDN
                    // ==null");
                } else {
                    //                    System.out.println("ProvisionCmcc get dest
                    // DestUser_ID.MSISDN="
                    //                            + isdn_Dest);
                }
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get dest DestUser_ID.MSISDN ==null");
            }

            //PseudoCode
            if (document.getElementsByTagName("PseudoCode") != null
                    && document.getElementsByTagName("PseudoCode").item(0) != null
                    && document.getElementsByTagName("PseudoCode").item(0)
                            .getFirstChild() != null) {
                pseudoCode_Fee = document.getElementsByTagName("PseudoCode")
                        .item(0).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get send
                // FeeUser_ID.PseudoCode="
                //                        + pseudoCode_Fee);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get send FeeUser_ID.PseudoCode
                // ==null");
            }
            if (document.getElementsByTagName("PseudoCode") != null
                    && document.getElementsByTagName("PseudoCode").item(1) != null
                    && document.getElementsByTagName("PseudoCode").item(1)
                            .getFirstChild() != null) {
                pseudoCode_Dest = document.getElementsByTagName("PseudoCode")
                        .item(1).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get dest
                // DestUser_ID.PseudoCode="
                //                        + pseudoCode_Dest);
            } else {
                //                System.out
                //                        .println("ProvisionCmcc get dest DestUser_ID.PseudoCode
                // ==null");
            }

            //LinkID
            if (document.getElementsByTagName("LinkID") != null
                    && document.getElementsByTagName("LinkID").item(0) != null
                    && document.getElementsByTagName("LinkID").item(0)
                            .getFirstChild() != null) {
                linkID = document.getElementsByTagName("LinkID").item(0)
                        .getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get LinkID=" + linkID);
            } else {
                //                System.out.println("ProvisionCmcc get LinkID ==null");
            }

            //ActionID
            if (document.getElementsByTagName("ActionID") != null
                    && document.getElementsByTagName("ActionID").item(0) != null
                    && document.getElementsByTagName("ActionID").item(0)
                            .getFirstChild() != null) {
                actionID = document.getElementsByTagName("ActionID").item(0)
                        .getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get ActionID=" + actionID);
            } else {
                //                System.out.println("ProvisionCmcc get ActionID ==null");
            }

            //ActionReasonID
            if (document.getElementsByTagName("ActionReasonID") != null
                    && document.getElementsByTagName("ActionReasonID").item(0) != null
                    && document.getElementsByTagName("ActionReasonID").item(0)
                            .getFirstChild() != null) {
                actionReasonID = document
                        .getElementsByTagName("ActionReasonID").item(0)
                        .getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get ActionReasonID="
                //                        + actionReasonID);
            } else {
                //                System.out.println("ProvisionCmcc get ActionReasonID
                // ==null");
            }

            //SPID
            if (document.getElementsByTagName("SPID") != null
                    && document.getElementsByTagName("SPID").item(0) != null
                    && document.getElementsByTagName("SPID").item(0)
                            .getFirstChild() != null) {
                spID = document.getElementsByTagName("SPID").item(0)
                        .getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get SPID=" + spID);
            } else {
                //                System.out.println("ProvisionCmcc get SPID ==null");
            }

            //SPServiceID
            if (document.getElementsByTagName("SPServiceID") != null
                    && document.getElementsByTagName("SPServiceID").item(0) != null
                    && document.getElementsByTagName("SPServiceID").item(0)
                            .getFirstChild() != null) {
                spServiceID = document.getElementsByTagName("SPServiceID")
                        .item(0).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get SPServiceID=" +
                // spServiceID);
            } else {
                //                System.out.println("ProvisionCmcc get SPServiceID ==null");
            }

            //AccessMode
            if (document.getElementsByTagName("AccessMode") != null
                    && document.getElementsByTagName("AccessMode").item(0) != null
                    && document.getElementsByTagName("AccessMode").item(0)
                            .getFirstChild() != null) {
                accessMode = document.getElementsByTagName("AccessMode")
                        .item(0).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get AccessMode=" +
                // accessMode);
            } else {
                //                System.out.println("ProvisionCmcc get AccessMode ==null");
            }

            //FeatureStr
            if (document.getElementsByTagName("FeatureStr") != null
                    && document.getElementsByTagName("FeatureStr").item(0) != null
                    && document.getElementsByTagName("FeatureStr").item(0)
                            .getFirstChild() != null) {
                featureStr = document.getElementsByTagName("FeatureStr")
                        .item(0).getFirstChild().getNodeValue();
                //                System.out.println("ProvisionCmcc get FeatureStr=" +
                // featureStr);
            } else {
                //                System.out.println("ProvisionCmcc get FeatureStr ==null");
            }

            OrderReq req = new OrderReq();
            req.setTransactionID(transactionID);
            req.setDevicetypeSend(devicetype_send);
            req.setDevicetypeDest(devicetype_dest);
            req.setDeviceidSend(deviceid_send);
            req.setDeviceidDest(deviceid_dest);
            req.setUseridtypeFee(useridtype_Fee);
            req.setUseridtypeDest(useridtype_Dest);
            req.setIsdnFee(isdn_Fee);
            req.setIsdnDest(isdn_Dest);
            req.setPseudoCodeFee(pseudoCode_Fee);
            req.setPseudoCodeDest(pseudoCode_Dest);
            req.setLinkID(linkID);
            req.setActionID(actionID);
            req.setActionReasonID(actionReasonID);
            req.setSpID(spID);
            req.setSpServiceID(spServiceID);
            req.setAccessMode(accessMode);
            req.setFeatureStr(featureStr);

            return req;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据一个OrderReq取得一个OrderRes。 注：实际逻辑没做处理。
     * 
     * @param req
     * @return
     */
    public static OrderRes getRealOrderRes(OrderReq req) {
        OrderRes res = new OrderRes();
        if (req == null) {
            res.setHRet(1);
            res.setTransactionId("0");
        } else {
            res.setHRet(0);
            res.setTransactionId(req.getTransactionID());
        }
        return res;
    }

    public static OrderRes getOrderRes(OrderReq req) {
        ISpService service = ServiceFactory.createSpService();

        OrderRes res = new OrderRes();
        if (req == null) {
            res.setHRet(1);
            System.out.println("请求为空！");
            res.setTransactionId("0");
        } else {
            res.setTransactionId(req.getTransactionID());
            //取得所操作的服务
            WapServiceBean wapService = WapServiceUtil
                    .getWapServiceByServiceId(req.getSpServiceID());
            if (wapService == null) {
                System.out.println("服务ID没找到：" + req.getSpServiceID());
                res.setHRet(1);
                return res;
            }

            OrderBean order = null;
            //开通服务
            if ("1".equals(req.getActionID())) {
                //包月服务
                if (wapService.getType() == WapServiceBean.BY_MONTH) {
                    order = service.getOrder("sp_service_id = "
                            + wapService.getId());
                    //第一次订购
                    if (order == null) {
                        order = new OrderBean();
                        order.setMid(req.getPseudoCodeFee());
                        order.setMobile(req.getIsdnDest());
                        order.setServiceId(req.getSpServiceID());
                        order.setSpServiceId(wapService.getId());
                        order.setStatus(OrderBean.ORDER);
                        service.addOrder(order);
                    } else {
                        service.updateOrder("status = " + OrderBean.ORDER,
                                "id = " + order.getId());
                    }

                    //增加一个操作记录
                    HistoryBean history = new HistoryBean();
                    history.setMid(req.getPseudoCodeFee());
                    history.setOperType(OrderBean.ORDER);
                    history.setServiceId(req.getSpServiceID());
                    service.addHistory(history);
                }
                //按次服务
                else {
                    order = new OrderBean();
                    order.setMid(req.getPseudoCodeFee());
                    order.setMobile(req.getIsdnDest());
                    order.setServiceId(req.getSpServiceID());
                    order.setSpServiceId(wapService.getId());
                    order.setStatus(OrderBean.ORDER);
                    service.addOrderByCount(order);
                }
            }
            //取消订购
            else if ("2".equals(req.getActionID())) {
                //包月服务
                if (wapService.getType() == WapServiceBean.BY_MONTH) {
                    order = service.getOrder("sp_service_id = "
                            + wapService.getId());
                    //之前没有订购，没法取消
                    if (order == null) {
                        res.setHRet(1);
                        System.out.println("之前没有定购无法取消！");
                        return res;
                    } else {
                        service.updateOrder("status = " + OrderBean.CANCEL,
                                "id = " + order.getId());
                    }

                    //增加一个操作记录
                    HistoryBean history = new HistoryBean();
                    history.setMid(req.getPseudoCodeFee());
                    history.setOperType(OrderBean.CANCEL);
                    history.setServiceId(req.getSpServiceID());                    
                    service.addHistory(history);
                }
            }

            res.setHRet(0);
        }
        return res;
    }
}