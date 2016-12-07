/*
 * Created on 2006-2-18
 *
 */
package net.joycool.wap.action.video;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.video.VideoBean;
import net.joycool.wap.bean.video.VideoFileBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IVideoService;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mcq
 *
 */
public class VideoInfoAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	IVideoService videoService = ServiceFactory.createVideoService();
        ICatalogService cataService = ServiceFactory.createCatalogService();

        /*
         * 取得参数VideoId 视频id pageIndex 分页码 orderBy 按xxx排序
         */
        int videoId = Integer.parseInt(request.getParameter("videoId"));
        VideoBean video = videoService.getVideo("id = " + videoId);
        if(video == null)
        	return null;
        VideoFileBean videoFile=videoService.getVideoFile("video_id="+video.getId());

//        String backTo = request.getParameter("backTo");
//        if ((backTo == null) || (backTo.equals("")))
//            backTo = "VideoCataList.do?id=" + video.getCatalogId();
        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || !(orderBy.equals("id")))
            orderBy = "download_sum";
        int pageIndex = 0;
        if (request.getParameter("pageIndex") != null) {
            pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        }

        //取得根据videoId得到的视频文件；
        String condition = "id = " + videoId;
        int catalogId = video.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = cataService.getCatalog(buffCondition);

        //取得上一条和下一条
        String prevCondition = null;
        String nextCondition = null;
        VideoBean prevVideo = null;
        VideoBean nextVideo = null;
        if (orderBy.equals("id")) {
            prevCondition = "catalog_id = " + video.getCatalogId() + " and "
                    + orderBy + " > " + video.getId() + " ORDER BY id ASC";
            nextCondition = "catalog_id = " + video.getCatalogId() + " and "
                    + orderBy + " < " + video.getId() + " ORDER BY id DESC";
            prevVideo = videoService.getVideo(prevCondition);
            nextVideo  = videoService.getVideo(nextCondition);
        } else if (orderBy.equals("download_sum")) {
            prevCondition = "catalog_id = " + video.getCatalogId() + " and "
                    + orderBy + " >= " + video.getDownloadSum()+ " and id != "
                    + video.getId() + " ORDER BY download_sum ASC, id DESC";
            prevVideo = videoService.getVideo(prevCondition);
            if (prevVideo != null) {
                nextCondition = "catalog_id = " + video.getCatalogId()
                        + " and " + orderBy + " <= " + video.getDownloadSum()
                        + " and id != " + video.getId() + " and id != "
                        + prevVideo.getId() + " ORDER BY download_sum DESC, id DESC";
            } else {
                nextCondition = "catalog_id = " + video.getCatalogId()
                        + " and " + orderBy + " <= " + video.getDownloadSum()
                        + " and id != " + video.getId()
                        + " ORDER BY download_sum DESC, id DESC";
            }
            nextVideo = videoService.getVideo(nextCondition);
        }

        if (prevVideo != null) {
//            String prevImageLink = ("VideoInfo.do?videoId="
//                    + prevVideo.getId() + "&amp;backTo="
//                    + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
//                    + orderBy);
//            prevVideo.setLinkUrl(prevImageLink);
            String prevImageLink = ("VideoInfo.do?videoId="
                    + prevVideo.getId() +  "&amp;orderBy="
                    + orderBy);
            prevVideo.setLinkUrl(prevImageLink);
        }
        if (nextVideo != null) {
//            String nextImageLink = ("VideoInfo.do?videoId="
//                    + nextVideo.getId() + "&amp;backTo="
//                    + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
//                    + orderBy);
//            nextVideo.setLinkUrl(nextImageLink);
            String nextImageLink = ("VideoInfo.do?videoId="
                    + nextVideo.getId() +"&amp;orderBy="
                    + orderBy);
            nextVideo.setLinkUrl(nextImageLink);
        }

        //prefixUrl
//        String prefixUrl = ("VideoInfo.do?videoId="
//                + video.getId() + "&amp;backTo="
//                + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
//                + orderBy);
        String prefixUrl = ("VideoInfo.do?videoId="
                + video.getId()+"&amp;orderBy="+ orderBy);

        //      更新浏览数
        if (pageIndex == 0) {
            String set = "download_sum = (download_sum + 1)";
            condition = "id = " + videoId;
           videoService.updateVideo(set, condition);
        }

        request.setAttribute("video", video);
        request.setAttribute("videoFile",videoFile);
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("catalog", catalog);
        request.setAttribute("prevVideo", prevVideo);
        request.setAttribute("nextVideo", nextVideo);

        return mapping.findForward("success");
    }

}

