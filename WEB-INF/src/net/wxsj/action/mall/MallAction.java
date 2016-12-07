/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.action.mall;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.Encoder;
import net.joycool.wap.util.PageUtil;
import net.wxsj.bean.mall.AreaTagBean;
import net.wxsj.bean.mall.BackBean;
import net.wxsj.bean.mall.InfoBean;
import net.wxsj.bean.mall.ReplyBean;
import net.wxsj.bean.mall.TagBean;
import net.wxsj.framework.JoycoolInfc;
import net.wxsj.framework.mall.MallFrk;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IMallService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class MallAction {
    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：首页
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("mallInfoListCondition");
        session.removeAttribute("mallInfoListQuery");
        session.removeAttribute("mallInfoBack");
        
        //取得热门标签
        ArrayList hotTagList = MallFrk.getHotTags();
        //取得热门地区标签
        ArrayList hotAreaTagList = MallFrk.getAreaTags();
        //取得最新的出售信息，4条
        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        String condition = "info_type = " + InfoBean.SELL;
        ArrayList newSellList = service.getInfoList(condition, 0, 4, "id desc");
        //取得最新的求购信息，4条
        condition = "info_type = " + InfoBean.BUY;
        ArrayList newBuyList = service.getInfoList(condition, 0, 4, "id desc");

        service.releaseAll();

        request.setAttribute("hotTagList", hotTagList);
        request.setAttribute("hotAreaTagList", hotAreaTagList);
        request.setAttribute("newSellList", newSellList);
        request.setAttribute("newBuyList", newBuyList);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：发表帖子
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void post(HttpServletRequest request, HttpServletResponse response) {
        //检查是否登录
        if (!JoycoolInfc.checkLogin(request, response, true)) {
            return;
        }

        UserBean user = JoycoolInfc.getLoginUser(request);

        //进入页面
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return;
        }
        //处理页面
        else {
            String act = StringUtil.dealParam(request.getParameter("act"));
            HttpSession session = request.getSession();
            //第一步
            if ("step1".equals(act)) {
                int infoType = StringUtil.toInt(request
                        .getParameter("infoType"));
                if (infoType < 0) {
                    infoType = InfoBean.BUY;
                }
                String name = StringUtil
                        .dealParam(request.getParameter("name"));
                String price = StringUtil.dealParam(request
                        .getParameter("price"));
                String intro = StringUtil.dealParam(request
                        .getParameter("intro"));

                if (StringUtil.isNull(name)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请输入物品名称");
                    return;
                }

                if (StringUtil.isNull(price)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请输入期望价格");
                    return;
                }

                if (StringUtil.isNull(intro)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请输入物品描述");
                    return;
                }

                InfoBean info = new InfoBean();
                info.setInfoType(infoType);
                info.setPrice(price);
                info.setName(name);
                info.setIntro(intro);

                session.setAttribute("toAddInfo", info);
                request.setAttribute("result", "toStep2");
                return;
            }
            //第二步
            else if ("step2".equals(act)) {
                InfoBean info = (InfoBean) session.getAttribute("toAddInfo");
                if (info == null) {
                    request.setAttribute("result", "timeout");
                    return;
                }

                String buyMode = StringUtil.dealParam(request
                        .getParameter("buyMode"));
                String telephone = StringUtil.dealParam(request
                        .getParameter("telephone"));
                String address = StringUtil.dealParam(request
                        .getParameter("address"));

                if (StringUtil.isNull(buyMode)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请输入交易方式");
                    return;
                }

//                if (StringUtil.isNull(telephone)) {
//                    request.setAttribute("result", "failure");
//                    request.setAttribute("tip", "请输入联系电话");
//                    return;
//                }
//                if (!StringUtil.checkTelephone(telephone)) {
//                    request.setAttribute("result", "failure");
//                    request.setAttribute("tip", "请输入正确的联系电话");
//                    return;
//                }
//
//                if (StringUtil.isNull(address)) {
//                    request.setAttribute("result", "failure");
//                    request.setAttribute("tip", "请输入您的地区");
//                    return;
//                }

                info.setBuyMode(buyMode);
                info.setTelephone(telephone);
                info.setAddress(address);
                info.setUserId(user.getId());
                info.setCreateDatetime(DateUtil.getNow());
                info.setLastReplyTime(DateUtil.getNow());
                info.setAreaTags("");
                info.setTags("");
                info.setUserNick(user.getNickName());
                
                //验证相关
                if(telephone.equals(user.getMobile())){
                    info.setValidated(1);
                    request.setAttribute("validated", "true");
                }

                IMallService service = ServiceFactory.createMallService(
                        IBaseService.CONN_IN_SERVICE, null);
                //开始事务
                service.getDbOp().startTransaction();
                int infoId = service.getNumber("id", "wxsj_info", "max",
                        "id > 0") + 1;
                info.setId(infoId);
                if (!service.addInfo(info)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "发表失败。");
                    service.releaseAll();
                    return;
                }

                //提交事务
                service.getDbOp().commitTransaction();
                service.releaseAll();

                session.removeAttribute("toAddInfo");
                request.setAttribute("result", "success");
                request.setAttribute("infoId", "" + infoId);
            }
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：信息列表页
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void infoList(HttpServletRequest request,
            HttpServletResponse response) {
        int COUNT_PER_PAGE = 8;

        HttpSession session = request.getSession();

        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));

        //信息类型
        int infoType = StringUtil.toInt(request.getParameter("infoType"));
        //名称
        String name = StringUtil.dealParam(request.getParameter("name"));
        if (!"post".equalsIgnoreCase(request.getMethod())) {
            if (name != null) {
                name = Encoder.decrypt(name);
            }
        }
        //id
        int id = StringUtil.toInt(request.getParameter("id"));
        //user id
        int userId = StringUtil.toInt(request.getParameter("userId"));
        //tagId 为999999表示“其他”
        int tagId = StringUtil.toInt(request.getParameter("tagId"));
        //areaTagId 为999999表示“其他”
        int areaTagId = StringUtil.toInt(request.getParameter("areaTagId"));

        if (infoType >= 0) {
            request.setAttribute("infoType", "" + infoType);
        }
        request.setAttribute("name", name);
        if (id > 0) {
            request.setAttribute("id", "" + id);
        }
        if (userId > 0) {
            request.setAttribute("userId", "" + userId);
        }
        if (tagId > 0) {
            request.setAttribute("tagId", "" + tagId);
        }
        if (areaTagId > 0) {
            request.setAttribute("areaTagId", "" + areaTagId);
        }

        int totalCount = 0;
        PagingBean paging = null;
        ArrayList list = null;
        String condition = null;
        String query = null;
        String prefixUrl = null;

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        
        //back
        String url = PageUtil.getCurrentPageURL(request);

        //分类标签
        if (tagId > 0 && tagId != MallFrk.OTHER_TAG) {
            TagBean tag = MallFrk.getTag(tagId);
            request.setAttribute("tag", tag);
            
            //back
            BackBean back = new BackBean(tag.getName(), url);
            session.setAttribute("mallInfoBack", back);
            
            //只是标签
            if (infoType <= 0) {
                query = "select count(id) from wxsj_tag_info where tag_id = "
                        + tagId;
            }
            //买卖信息
            else {
                query = "select count(wxsj_info.id) from wxsj_info join wxsj_tag_info on wxsj_info.id = wxsj_tag_info.info_id and wxsj_tag_info.tag_id = "
                        + tagId + " and info_type = " + infoType;
            }

            totalCount = service.getNumber(query);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);

            query = "select wxsj_info.* from wxsj_info join wxsj_tag_info on wxsj_info.id = wxsj_tag_info.info_id and wxsj_tag_info.tag_id = "
                    + tagId;
            if (infoType >= 0) {
                query += " and info_type = " + infoType;
            }

            //为了取上下文
            session.setAttribute("mallInfoListQuery", query);

            query += " order by is_top desc, last_reply_time desc, wxsj_info.id desc limit "
                    + paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE
                    + ", "
                    + COUNT_PER_PAGE;

            list = service.getInfoList(query, "wxsj_info.");

            prefixUrl = "infoList.jsp?tagId=" + tagId;
            if (infoType >= 0) {
                prefixUrl += "&amp;infoType=" + infoType;
            }
            paging.setPrefixUrl(prefixUrl);
        }
        //分类标签 其他
        else if (tagId > 0 && tagId == MallFrk.OTHER_TAG) {
            //back
            BackBean back = new BackBean("其他", url);
            session.setAttribute("mallInfoBack", back);
            
            condition = "has_tag = 0";
            if (infoType >= 0) {
                condition += " and info_type = " + infoType;
            }

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp?tagId=" + tagId;
            if (infoType >= 0) {
                prefixUrl += "&amp;infoType=" + infoType;
            }
            paging.setPrefixUrl(prefixUrl);
        }
        //地区标签
        else if (areaTagId > 0 && areaTagId != MallFrk.OTHER_TAG) {
            AreaTagBean tag = MallFrk.getAreaTag(areaTagId);
            request.setAttribute("areaTag", tag);
            
            //back
            BackBean back = new BackBean(tag.getName(), url);
            session.setAttribute("mallInfoBack", back);
            
            //只是标签
            if (infoType <= 0) {
                query = "select count(id) from wxsj_area_tag_info where tag_id = "
                        + areaTagId;
            }
            //买卖信息
            else {
                query = "select count(wxsj_info.id) from wxsj_info join wxsj_area_tag_info on wxsj_info.id = wxsj_area_tag_info.info_id and wxsj_area_tag_info.tag_id = "
                        + areaTagId + " and info_type = " + infoType;
            }

            totalCount = service.getNumber(query);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);

            query = "select wxsj_info.* from wxsj_info join wxsj_area_tag_info on wxsj_info.id = wxsj_area_tag_info.info_id and wxsj_area_tag_info.tag_id = "
                    + areaTagId;
            if (infoType >= 0) {
                query += " and info_type = " + infoType;
            }

            //为了取上下文
            session.setAttribute("mallInfoListQuery", query);

            query += " order by is_top desc, last_reply_time desc, wxsj_info.id desc limit "
                    + paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE
                    + ", "
                    + COUNT_PER_PAGE;

            list = service.getInfoList(query, "wxsj_info.");

            prefixUrl = "infoList.jsp?areaTagId=" + areaTagId;
            if (infoType >= 0) {
                prefixUrl += "&amp;infoType=" + infoType;
            }
            paging.setPrefixUrl(prefixUrl);
        }
        //地区标签 其他
        else if (areaTagId > 0 && areaTagId == MallFrk.OTHER_TAG) {
            //back
            BackBean back = new BackBean("其他", url);
            session.setAttribute("mallInfoBack", back);
            
            condition = "has_area_tag = 0";
            if (infoType >= 0) {
                condition += " and info_type = " + infoType;
            }

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp?areaTagId=" + areaTagId;
            if (infoType >= 0) {
                prefixUrl += "&amp;infoType=" + infoType;
            }
            paging.setPrefixUrl(prefixUrl);
        }
        //按名称搜索
        else if (!StringUtil.isNull(name)) {
            condition = "name like '%" + name + "%'";
            if (infoType >= 0) {
                condition += " and info_type = " + infoType;
            }

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp?name=" + Encoder.encrypt(name);
            if (infoType >= 0) {
                prefixUrl += "&amp;infoType=" + infoType;
            }
            paging.setPrefixUrl(prefixUrl);
            
            //back
            BackBean back = new BackBean("搜索", prefixUrl.replace("&amp;", "&"));
            session.setAttribute("mallInfoBack", back);
        }
        //按ID搜索
        else if (id > 0) {
            condition = "id = " + id;

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp?id=" + id;
            paging.setPrefixUrl(prefixUrl);
            
            //back
            BackBean back = new BackBean("搜索", prefixUrl.replace("&amp;", "&"));
            session.setAttribute("mallInfoBack", back);            
        }
        //用户ID
        else if (userId > 0) {
            condition = "user_id = " + userId;

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp?userId=" + userId;
            paging.setPrefixUrl(prefixUrl);
            
            //back
            BackBean back = new BackBean("搜索", prefixUrl.replace("&amp;", "&"));
            session.setAttribute("mallInfoBack", back);            
        }
        //按买/卖排列
        else if (infoType >= 0) {
            condition = "info_type = " + infoType;

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp?infoType=" + infoType;
            paging.setPrefixUrl(prefixUrl);
            
            //back
            String backTitle = null;
            if(infoType == InfoBean.BUY){
                backTitle = "求购类";
            }
            else {
                backTitle = "出售类";
            }
            BackBean back = new BackBean(backTitle, url);
            session.setAttribute("mallInfoBack", back);            
        }
        //所有
        else {
            condition = "id > 0";

            //为了取上下文
            session.setAttribute("mallInfoListCondition", condition);

            totalCount = service.getInfoCount(condition);
            paging = new PagingBean(pageIndex, totalCount, COUNT_PER_PAGE);
            list = service.getInfoList(condition, paging.getCurrentPageIndex()
                    * COUNT_PER_PAGE, COUNT_PER_PAGE,
                    "is_top desc, last_reply_time desc, id desc");
            prefixUrl = "infoList.jsp";
            paging.setPrefixUrl(prefixUrl);
            
            //back
            BackBean back = new BackBean("全部信息", url);
            session.setAttribute("mallInfoBack", back);            
        }

        service.releaseAll();

        request.setAttribute("paging", paging);
        request.setAttribute("list", list);

        //取得热门标签
        ArrayList hotTagList = MallFrk.getHotTags();
        //取得热门地区标签
        ArrayList hotAreaTagList = MallFrk.getAreaTags();

        request.setAttribute("hotTagList", hotTagList);
        request.setAttribute("hotAreaTagList", hotAreaTagList);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：信息列表
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void info(HttpServletRequest request, HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));

        int COUNT_PER_PAGE = 8;

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        InfoBean info = service.getInfo("id = " + id);

        //回复列表
        int totalReplyCount = service.getReplyCount("parent_id = " + id);
        PagingBean paging = new PagingBean(pageIndex, totalReplyCount,
                COUNT_PER_PAGE);
        ArrayList replyList = service.getReplyList("parent_id = " + id, paging
                .getCurrentPageIndex()
                * COUNT_PER_PAGE, COUNT_PER_PAGE, "id");
        String prefixUrl = "info.jsp?id=" + id;
        paging.setPrefixUrl(prefixUrl);

        //取上一个，下一个
        HttpSession session = request.getSession();
        String listQuery = (String) session.getAttribute("mallInfoListQuery");
        String listCondition = (String) session
                .getAttribute("mallInfoListCondition");
        InfoBean next = null;
        InfoBean prev = null;
        if (listQuery != null) {
            ArrayList tempList = null;
            String query = null;
            //下一个
            query = listQuery + " and wxsj_info.last_reply_time < '" + info.getLastReplyTime()
                    + "' order by wxsj_info.last_reply_time desc limit 0, 1";
            tempList = service.getInfoList(query, "wxsj_info.");
            if (tempList.size() > 0) {
                next = (InfoBean) tempList.get(0);
            }
            //上一个
            query = listQuery + " and wxsj_info.last_reply_time > '" + info.getLastReplyTime()
                    + "' order by wxsj_info.last_reply_time limit 0, 1";
            tempList = service.getInfoList(query, "wxsj_info.");
            if (tempList.size() > 0) {
                prev = (InfoBean) tempList.get(0);
            }
        } else {
            if (listCondition == null) {
                listCondition = "id > 0";
            }

            //下一个
            String condition = listCondition + " and last_reply_time < '" + info.getLastReplyTime()
                    + "' order by last_reply_time desc limit 0, 1";
            next = service.getInfo(condition);

            //上一个
            condition = listCondition + " and last_reply_time > '" + info.getLastReplyTime()
                    + "' order by last_reply_time limit 0, 1";
            prev = service.getInfo(condition);
        }

        service.updateInfo("hits = (hits + 1)", "id = " + id);
        service.releaseAll();

        request.setAttribute("prev", prev);
        request.setAttribute("next", next);
        request.setAttribute("info", info);
        request.setAttribute("replyList", replyList);
        request.setAttribute("paging", paging);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：回复帖子
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void postReply(HttpServletRequest request,
            HttpServletResponse response) {
        //检查是否登录
        if (!JoycoolInfc.checkLogin(request, response, true)) {
            return;
        }

        UserBean user = JoycoolInfc.getLoginUser(request);

        //进入页面
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return;
        }
        //处理页面
        else {
            String content = StringUtil.dealParam(request.getParameter("content"));
            int parentId = StringUtil.toInt(request.getParameter("parentId"));

            if (StringUtil.isNull(content)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入内容");
                return;
            }

            IMallService service = ServiceFactory.createMallService(
                    IBaseService.CONN_IN_SERVICE, null);
            //开始事务
            service.getDbOp().startTransaction();
            int replyId = service.getNumber("id", "wxsj_reply", "max", "id > 0") + 1;
            ReplyBean reply = new ReplyBean();
            reply.setId(replyId);
            reply.setContent(content);
            reply.setCreateDatetime(DateUtil.getNow());
            reply.setParentId(parentId);
            reply.setUserId(user.getId());
            reply.setUserNick(user.getNickName());
            if (!service.addReply(reply)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "发表失败。");
                service.releaseAll();
                return;
            }
            
            service.updateInfo("last_reply_time = now(), reply_count = (reply_count + 1)", "id = " + parentId);

            //提交事务
            service.getDbOp().commitTransaction();
            service.releaseAll();
            
            request.setAttribute("result", "success");
        }

    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：回复帖子
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void myPost(HttpServletRequest request,
            HttpServletResponse response) {
        //检查是否登录
        if (!JoycoolInfc.checkLogin(request, response, true)) {
            return;
        }
    
        UserBean user = JoycoolInfc.getLoginUser(request);            
    
        try {
            response.sendRedirect(("infoList.jsp?userId=" + user.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：回复帖子
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void tagList(HttpServletRequest request,
            HttpServletResponse response) {
        //取得热门标签
        ArrayList hotTagList = MallFrk.getTags();
        //取得热门地区标签
        ArrayList hotAreaTagList = MallFrk.getAreaTags();
        
        request.setAttribute("hotTagList", hotTagList);
        request.setAttribute("hotAreaTagList", hotAreaTagList);
    }
}
