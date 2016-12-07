/*
 * Created on 2005-11-30
 *
 */
package net.joycool.wap.action.image;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.WapServiceBean;
import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.WapServiceUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class ImageInfoAction extends Action {
    static IImageService imageService = ServiceFactory.createImageService();
    static ICatalogService cataService = ServiceFactory.createCatalogService();
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
         * 取得参数 code 图片code pageIndex 分页码 backTo 返回上一级 orderBy 按xxx排序
         */
        int imageId = StringUtil.toInt(request.getParameter("imageId"));
        ImageBean image = imageService.getImage("id = " + imageId);
        if(image == null)
        	return mapping.findForward(Constants.ACTION_SUCCESS_KEY);

        String code = image.getCode();
        /*
        String backTo = request.getParameter("backTo");
        if ((backTo == null) || (backTo.equals("")))
            backTo = "ImageCataList.do?id=" + image.getCatalogId();
        */
        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || !(orderBy.equals("id")))
            orderBy = "hits";
        int pageIndex = 0;
        if (request.getParameter("pageIndex") != null) {
            pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        }

        int jaLineId = StringUtil.toInt(request.getParameter("jaLineId"));
        if (jaLineId == -1) {
            jaLineId = 0;
        }

        //根返回节点
        String rootBackTo = null;
        JaLineBean originLine = null;
        if (jaLineId != 0) {
            IJaLineService jaLineService = ServiceFactory.createJaLineService();
            originLine = jaLineService.getLine(jaLineId);
            if(originLine != null) {
	            rootBackTo = JoycoolSpecialUtil.getRootBackTo(originLine);
	            //业务类型
	            int wapType = JoycoolSpecialUtil.getWapType(originLine);
	            if(wapType != 0){
	                request.setAttribute("wapType", new Integer(wapType));
	            }
            } else {
            	rootBackTo = BaseAction.getBottom(request, response);
            }
        } else {
            rootBackTo = BaseAction.getBottom(request, response);
        }
        String prefixUrl = null;

        //取得根据code得到的图片文件；
        String condition = "code = '" + code + "'";
        int catalogId = image.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = cataService.getCatalog(buffCondition);

        //取得上一条和下一条
        String prevCondition = null;
        String nextCondition = null;
        ImageBean prevImage = null;
        ImageBean nextImage = null;
        if (orderBy.equals("id")) {
            prevCondition = "catalog_id = " + image.getCatalogId() + " and "
                    + orderBy + " > " + image.getId() + " ORDER BY id ASC";
            nextCondition = "catalog_id = " + image.getCatalogId() + " and "
                    + orderBy + " < " + image.getId() + " ORDER BY id DESC";
            prevImage = imageService.getImage(prevCondition);
            nextImage = imageService.getImage(nextCondition);
        } else if (orderBy.equals("hits")) {
            prevCondition = "catalog_id = " + image.getCatalogId() + " and "
                    + orderBy + " >= " + image.getHits() + " and id != "
                    + image.getId() + " ORDER BY hits ASC, id DESC";
            prevImage = imageService.getImage(prevCondition);
            if (prevImage != null) {
                nextCondition = "catalog_id = " + image.getCatalogId()
                        + " and " + orderBy + " <= " + image.getHits()
                        + " and id != " + image.getId() + " and id != "
                        + prevImage.getId() + " ORDER BY hits DESC, id DESC";
            } else {
                nextCondition = "catalog_id = " + image.getCatalogId()
                        + " and " + orderBy + " <= " + image.getHits()
                        + " and id != " + image.getId()
                        + " ORDER BY hits DESC, id DESC";
            }
            nextImage = imageService.getImage(nextCondition);
        }
        //从后台过来
        else if (orderBy.equals("line_index")) {
            IJaLineService jaLineService = ServiceFactory.createJaLineService();
            JaLineBean line = jaLineService.getLine(jaLineId);
            String prevLineCondition = "(link_type = " + JaLineBean.LT_IMAGE
                    + " or link_type = " + JaLineBean.LT_IMAGE_NO_THUMBNAIL
                    + ") and parent_id = " + line.getParentId()
                    + " and line_index <= " + line.getLineIndex()
                    + " and id != " + jaLineId + " ORDER BY line_index DESC";
            String nextLineCondition = "(link_type = " + JaLineBean.LT_IMAGE
                    + " or link_type = " + JaLineBean.LT_IMAGE_NO_THUMBNAIL
                    + ") and parent_id = " + line.getParentId()
                    + " and line_index >= " + line.getLineIndex()
                    + " and id != " + jaLineId + " ORDER BY line_index ASC";
            JaLineBean prevLine = jaLineService.getLine(prevLineCondition);
            JaLineBean nextLine = jaLineService.getLine(nextLineCondition);

            if (prevLine != null) {
                prevImage = imageService.getImage("id = " + prevLine.getLink());
            }
            if (nextLine != null) {
                nextImage = imageService.getImage("id = " + nextLine.getLink());
            }

            if (prevImage != null) {
                /*
                   String prevImageLink = response
                        .encodeURL("/image/ImageInfo.do?imageId="
                                + prevImage.getId()
                                + "&amp;backTo="
                                + URLEncoder.encode(backTo, "UTF-8")
                                + "&amp;orderBy="
                                + orderBy
                                + "&amp;jaLineId="
                                + prevLine.getId());
                */
                String prevImageLink = response
                .encodeURL("/image/ImageInfo.do?imageId="
                        + prevImage.getId()
                        + "&amp;orderBy="
                        + orderBy
                        + "&amp;jaLineId="
                        + prevLine.getId());
                /**
                 * wap业务
                 */
                int wapType = JoycoolSpecialUtil.getWapType(originLine);
                if (wapType != 0) {
                    String unique = null;
                    WapServiceBean wapService = WapServiceUtil
                            .getWapServiceById(wapType);
                    if (wapService != null) {
                        unique = StringUtil.getUnique();
                        Hashtable urlMap = JoycoolSessionListener
                                .getUrlMap(request.getSession().getId());
                        if (urlMap != null) {
                            urlMap.put(unique, prevImageLink.replace(
                                    "&amp;", "&"));
                        }
                        prevImageLink = wapService.getOrderAddress()
                                + "?unique=" + unique;
                    }
                }
                prevImage.setLinkUrl(prevImageLink);
            }
            if (nextImage != null) {
                /*
                   String nextImageLink = response
                        .encodeURL("/image/ImageInfo.do?imageId="
                                + nextImage.getId()
                                + "&amp;backTo="
                                + URLEncoder.encode(backTo, "UTF-8")
                                + "&amp;orderBy="
                                + orderBy
                                + "&amp;jaLineId="
                                + nextLine.getId());
                */
                String nextImageLink = response
                .encodeURL("/image/ImageInfo.do?imageId="
                        + nextImage.getId()
                        + "&amp;orderBy="
                        + orderBy
                        + "&amp;jaLineId="
                        + nextLine.getId());
                /**
                 * wap业务
                 */
                int wapType = JoycoolSpecialUtil.getWapType(originLine);
                if (wapType != 0) {
                    String unique = null;
                    WapServiceBean wapService = WapServiceUtil
                            .getWapServiceById(wapType);
                    if (wapService != null) {
                        unique = StringUtil.getUnique();
                        Hashtable urlMap = JoycoolSessionListener
                                .getUrlMap(request.getSession().getId());
                        if (urlMap != null) {
                            urlMap.put(unique, nextImageLink.replace(
                                    "&amp;", "&"));
                        }
                        nextImageLink = wapService.getOrderAddress()
                                + "?unique=" + unique;
                    }
                }
                nextImage.setLinkUrl(nextImageLink);
            }

           /* prefixUrl = ("ImageInfo.do?imageId="
                    + image.getId() + "&amp;backTo="
                    + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
                    + orderBy + "&amp;jaLineId=" + line.getId());*/
            prefixUrl = ("ImageInfo.do?imageId="
                    + image.getId() + "&amp;orderBy="
                    + orderBy + "&amp;jaLineId=" + line.getId());
        }

        if (!orderBy.equals("line_index")) {
            if (prevImage != null) {
               /* 
                  String prevImageLink = response
                        .encodeURL("/image/ImageInfo.do?imageId="
                                + prevImage.getId()
                                + "&amp;backTo="
                                + URLEncoder.encode(backTo, "UTF-8")
                                + "&amp;orderBy="
                                + orderBy
                                + "&amp;jaLineId="
                                + jaLineId);
                */
                String prevImageLink = response
                .encodeURL("/image/ImageInfo.do?imageId="
                        + prevImage.getId()
                        + "&amp;orderBy="
                        + orderBy
                        + "&amp;jaLineId="
                        + jaLineId);
                /**
                 * wap业务
                 */
                int wapType = JoycoolSpecialUtil.getWapType(originLine);
                if (wapType != 0) {
                    String unique = null;
                    WapServiceBean wapService = WapServiceUtil
                            .getWapServiceById(wapType);
                    if (wapService != null) {
                        unique = StringUtil.getUnique();
                        Hashtable urlMap = JoycoolSessionListener
                                .getUrlMap(request.getSession().getId());
                        if (urlMap != null) {
                            urlMap.put(unique, prevImageLink.replace(
                                    "&amp;", "&"));
                        }
                        prevImageLink = wapService.getOrderAddress()
                                + "?unique=" + unique;
                    }
                }
                prevImage.setLinkUrl(prevImageLink);
            }
            if (nextImage != null) {
               /*
            	String nextImageLink = response
                        .encodeURL("/image/ImageInfo.do?imageId="
                                + nextImage.getId()
                                + "&amp;backTo="
                                + URLEncoder.encode(backTo, "UTF-8")
                                + "&amp;orderBy="
                                + orderBy
                                + "&amp;jaLineId="
                                + jaLineId);
                */                
                String nextImageLink = response
                .encodeURL("/image/ImageInfo.do?imageId="
                        + nextImage.getId()
                        + "&amp;orderBy="
                        + orderBy
                        + "&amp;jaLineId="
                        + jaLineId);
                /**
                 * wap业务
                 */
                int wapType = JoycoolSpecialUtil.getWapType(originLine);
                if (wapType != 0) {
                    String unique = null;
                    WapServiceBean wapService = WapServiceUtil
                            .getWapServiceById(wapType);
                    if (wapService != null) {
                        unique = StringUtil.getUnique();
                        Hashtable urlMap = JoycoolSessionListener
                                .getUrlMap(request.getSession().getId());
                        if (urlMap != null) {
                            urlMap.put(unique, nextImageLink.replace(
                                    "&amp;", "&"));
                        }
                        nextImageLink = wapService.getOrderAddress()
                                + "?unique=" + unique;
                    }
                }
                nextImage.setLinkUrl(nextImageLink);
            }
            /*
               prefixUrl = ("ImageInfo.do?imageId="
                    + image.getId() + "&amp;jaLineId=" + jaLineId
                    + "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
                    + "&amp;orderBy=" + orderBy);
            */
            prefixUrl = ("ImageInfo.do?imageId="
                    + image.getId() + "&amp;jaLineId=" + jaLineId
                    + "&amp;orderBy=" + orderBy);
        }

        //更新浏览数
        if (pageIndex == 0) {
            String set = "hits = (hits + 1)";
            condition = "code = '" + code + "'"; 
            imageService.updateImage(set, condition);
        }
        //mcq_1_增加用户经验值  时间:2006-6-11
        //增加用户经验值
        HttpSession session =  request.getSession();
        UserBean user= (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
        RankAction.addPoint(user,Constants.RANK_GENERAL);
        //mcq_end
        
        request.setAttribute("image", image);
        request.setAttribute("prefixUrl", prefixUrl);
        //request.setAttribute("backTo", backTo);
        request.setAttribute("rootBackTo", rootBackTo);
        request.setAttribute("catalog", catalog);
        request.setAttribute("prevImage", prevImage);
        request.setAttribute("nextImage", nextImage);

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }

}
