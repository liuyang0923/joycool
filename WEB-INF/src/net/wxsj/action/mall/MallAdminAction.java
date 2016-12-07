/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.action.mall;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.PagingBean;
import net.wxsj.bean.mall.AreaTagBean;
import net.wxsj.bean.mall.AreaTagInfoBean;
import net.wxsj.bean.mall.InfoBean;
import net.wxsj.bean.mall.ReplyBean;
import net.wxsj.bean.mall.TagBean;
import net.wxsj.bean.mall.TagInfoBean;
import net.wxsj.framework.mall.MallFrk;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IMallService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.IntUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class MallAdminAction {
    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：分类标签列表
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void tagList(HttpServletRequest request, HttpServletResponse response) {
        ArrayList tagList = MallFrk.getTags();
        request.setAttribute("tagList", tagList);
    }

    public void addTag(HttpServletRequest request, HttpServletResponse response) {
        if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入名称");
                return;
            }

            int mark = StringUtil.toInt(request.getParameter("mark"));

            IMallService service = ServiceFactory.createMallService(
                    IBaseService.CONN_IN_SERVICE, null);
            if (service.getTagCount("name = '" + name + "'") > 0) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "这个分类标签已经存在");
                return;
            }

            //开始事务
            service.getDbOp().startTransaction();
            int tagId = service.getNumber("id", "wxsj_tag", "max", "id > 0") + 1;
            int displayOrder = service.getNumber("display_order", "wxsj_tag",
                    "max", "id > 0") + 10;
            TagBean tag = new TagBean();
            tag.setCreateDatetime(DateUtil.getNow());
            tag.setCreateUserId(0);
            tag.setDisplayOrder(displayOrder);
            tag.setId(tagId);
            tag.setMark(mark);
            tag.setName(name);

            if (!service.addTag(tag)) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "添加失败");
                return;
            }

            service.getDbOp().commitTransaction();

            service.releaseAll();

            MallFrk.reloadTag();

            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：修改一个标签
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void editTag(HttpServletRequest request, HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        TagBean tag = MallFrk.getTag(id);
        if (tag == null) {
            return;
        }

        if ("get".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("tag", tag);
            return;
        } else if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入名称");
                return;
            }

            int mark = StringUtil.toInt(request.getParameter("mark"));

            IMallService service = ServiceFactory.createMallService(
                    IBaseService.CONN_IN_SERVICE, null);
            if (service.getTagCount("name = '" + name + "' and id != " + id) > 0) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "这个分类标签已经存在");
                return;
            }

            int displayOrder = StringUtil.toInt(request
                    .getParameter("displayOrder"));
            if (displayOrder <= 0) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入正确的顺序！");
                return;
            }

            if (tag.getName().equals(name) && tag.getMark() == mark
                    && tag.getDisplayOrder() == displayOrder) {
                service.releaseAll();
                request.setAttribute("result", "success");
                return;
            }

            String set = "name = '" + name + "', mark = " + mark
                    + ", display_order = " + displayOrder;
            service.updateTag(set, "id = " + id);

            service.releaseAll();

            MallFrk.reloadTag();

            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：删除一个标签
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void deleteTag(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        TagBean tag = MallFrk.getTag(id);
        if (tag == null) {
            return;
        }

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        String query = "select wxsj_info.* from wxsj_info join wxsj_tag_info on wxsj_info.id = wxsj_tag_info.info_id and wxsj_tag_info.tag_id = "
                + id;
        ArrayList infoList = service.getInfoList(query, "wxsj_info.");

        //开始事务
        service.getDbOp().startTransaction();

        //更新消息
        Iterator itr = infoList.iterator();
        InfoBean info = null;
        ArrayList intList = null;
        String tagsStr = null;
        String set = null;
        while (itr.hasNext()) {
            info = (InfoBean) itr.next();
            if (StringUtil.isNull(info.getTags())) {
                continue;
            }
            intList = IntUtil.getIntList(info.getTags(), MallFrk.SEPERATOR);
            intList = IntUtil.removeInt(intList, id);
            tagsStr = IntUtil.getIntStr(intList, MallFrk.SEPERATOR);
            if (StringUtil.isNull(tagsStr)) {
                set = "tags = '" + tagsStr + "', has_tag = 0";
            } else {
                set = "tags = '" + tagsStr + "'";
            }
            service.updateInfo(set, "id = " + info.getId());
        }

        ///删除标签、信息对应记录
        service.deleteTagInfo("tag_id = " + id);

        //删除标签
        service.deleteTag("id = " + id);

        //提交事务
        service.getDbOp().commitTransaction();

        service.releaseAll();

        //清空缓存
        MallFrk.reloadTag();
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：删除一个地区标签
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void deleteAreaTag(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        AreaTagBean tag = MallFrk.getAreaTag(id);
        if (tag == null) {
            return;
        }

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        String query = "select wxsj_info.* from wxsj_info join wxsj_area_tag_info on wxsj_info.id = wxsj_area_tag_info.info_id and wxsj_area_tag_info.tag_id = "
                + id;
        ArrayList infoList = service.getInfoList(query, "wxsj_info.");

        //开始事务
        service.getDbOp().startTransaction();

        //更新消息
        Iterator itr = infoList.iterator();
        InfoBean info = null;
        ArrayList intList = null;
        String tagsStr = null;
        String set = null;
        while (itr.hasNext()) {
            info = (InfoBean) itr.next();
            if (StringUtil.isNull(info.getAreaTags())) {
                continue;
            }
            intList = IntUtil.getIntList(info.getAreaTags(), MallFrk.SEPERATOR);
            intList = IntUtil.removeInt(intList, id);
            tagsStr = IntUtil.getIntStr(intList, MallFrk.SEPERATOR);
            if (StringUtil.isNull(tagsStr)) {
                set = "area_tags = '" + tagsStr + "', has_area_tag = 0";
            } else {
                set = "area_tags = '" + tagsStr + "'";
            }
            service.updateInfo(set, "id = " + info.getId());
        }

        ///删除标签、信息对应记录
        service.deleteAreaTagInfo("tag_id = " + id);

        //删除标签
        service.deleteAreaTag("id = " + id);

        //提交事务
        service.getDbOp().commitTransaction();

        service.releaseAll();

        //清空缓存
        MallFrk.reloadAreaTag();
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：修改一个标签
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void editAreaTag(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        AreaTagBean tag = MallFrk.getAreaTag(id);
        if (tag == null) {
            return;
        }

        if ("get".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("tag", tag);
            return;
        } else if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入名称");
                return;
            }

            int mark = StringUtil.toInt(request.getParameter("mark"));

            IMallService service = ServiceFactory.createMallService(
                    IBaseService.CONN_IN_SERVICE, null);
            if (service
                    .getAreaTagCount("name = '" + name + "' and id != " + id) > 0) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "这个分类标签已经存在");
                return;
            }

            int displayOrder = StringUtil.toInt(request
                    .getParameter("displayOrder"));
            if (displayOrder <= 0) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入正确的顺序！");
                return;
            }

            if (tag.getName().equals(name) && tag.getMark() == mark
                    && tag.getDisplayOrder() == displayOrder) {
                service.releaseAll();
                request.setAttribute("result", "success");
                return;
            }

            String set = "name = '" + name + "', mark = " + mark
                    + ", display_order = " + displayOrder;
            service.updateAreaTag(set, "id = " + id);

            service.releaseAll();

            MallFrk.reloadAreaTag();

            request.setAttribute("result", "success");
        }
    }

    public void addAreaTag(HttpServletRequest request,
            HttpServletResponse response) {
        if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入名称");
                return;
            }

            int mark = StringUtil.toInt(request.getParameter("mark"));

            IMallService service = ServiceFactory.createMallService(
                    IBaseService.CONN_IN_SERVICE, null);
            if (service.getAreaTagCount("name = '" + name + "'") > 0) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "这个分类标签已经存在");
                return;
            }

            //开始事务
            service.getDbOp().startTransaction();
            int tagId = service.getNumber("id", "wxsj_area_tag", "max",
                    "id > 0") + 1;
            int displayOrder = service.getNumber("display_order",
                    "wxsj_area_tag", "max", "id > 0") + 10;
            AreaTagBean tag = new AreaTagBean();
            tag.setCreateDatetime(DateUtil.getNow());
            tag.setCreateUserId(0);
            tag.setDisplayOrder(displayOrder);
            tag.setId(tagId);
            tag.setMark(mark);
            tag.setName(name);

            if (!service.addAreaTag(tag)) {
                service.releaseAll();
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "添加失败");
                return;
            }

            service.getDbOp().commitTransaction();

            service.releaseAll();

            MallFrk.reloadAreaTag();

            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：分类标签列表
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void areaTagList(HttpServletRequest request,
            HttpServletResponse response) {
        ArrayList tagList = MallFrk.getAreaTags();
        request.setAttribute("tagList", tagList);
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
    public void infoList(HttpServletRequest request,
            HttpServletResponse response) {
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        int COUNT_PER_PAGE = 50;

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        String condition = "id > 0";
        //取得总数
        int totalCount = service.getInfoCount(condition);
        PagingBean paging = new PagingBean(pageIndex, totalCount,
                COUNT_PER_PAGE);
        pageIndex = paging.getCurrentPageIndex();
        //取得本页列表
        ArrayList list = service.getInfoList(condition, paging
                .getCurrentPageIndex()
                * COUNT_PER_PAGE, COUNT_PER_PAGE,
                "is_top desc, last_reply_time desc, id desc");
        String prefixUrl = "infoList.jsp";
        paging.setPrefixUrl(prefixUrl);

        service.releaseAll();

        request.setAttribute("list", list);
        request.setAttribute("paging", paging);
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

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        InfoBean info = service.getInfo("id = " + id);
        //回复列表
        ArrayList replyList = service.getReplyList("parent_id = " + id, 0, -1,
                "id");

        service.releaseAll();

        request.setAttribute("info", info);
        request.setAttribute("replyList", replyList);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：修改一个标签
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void editInfo(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        InfoBean info = service.getInfo("id = " + id);
        if (info == null) {
            service.releaseAll();
            return;
        }

        request.setAttribute("info", info);

        if ("get".equalsIgnoreCase(request.getMethod())) {
            ArrayList tagList = MallFrk.getTags();
            request.setAttribute("tagList", tagList);
            ArrayList areaTagList = MallFrk.getAreaTags();
            request.setAttribute("areaTagList", areaTagList);
            service.releaseAll();
            return;
        } else if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入名称");
                return;
            }
            String userNick = StringUtil.dealParam(request
                    .getParameter("userNick"));
            if (StringUtil.isNull(userNick)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入用户昵称");
                return;
            }
            String intro = StringUtil.dealParam(request.getParameter("intro"));
            if (StringUtil.isNull(intro)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入介绍");
                return;
            }
            String price = StringUtil.dealParam(request.getParameter("price"));
            if (StringUtil.isNull(price)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入期望价格");
                return;
            }
            String buyMode = StringUtil.dealParam(request
                    .getParameter("buyMode"));
            if (StringUtil.isNull(buyMode)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入交易方式");
                return;
            }
            String telephone = StringUtil.dealParam(request
                    .getParameter("telephone"));
            if (StringUtil.isNull(telephone)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入联系电话");
                return;
            }
            String address = StringUtil.dealParam(request
                    .getParameter("address"));
            if (StringUtil.isNull(address)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "请输入所在地区");
                return;
            }

            int infoType = StringUtil.toInt(request.getParameter("infoType"));
            int validated = StringUtil.toInt(request.getParameter("validated"));
            int isTop = StringUtil.toInt(request.getParameter("isTop"));
            int isJinghua = StringUtil.toInt(request.getParameter("isJinghua"));

            //开始事务
            service.getDbOp().startTransaction();

            String set = "name = '" + name + "', user_nick = '" + userNick
                    + "', intro = '" + intro + "', price = '" + price
                    + "', buy_mode = '" + buyMode + "', telephone = '"
                    + telephone + "', address = '" + address
                    + "', info_type = " + infoType + ", validated = "
                    + validated + ", is_top = " + isTop + ", is_jinghua = "
                    + isJinghua;
            if (!service.updateInfo(set, "id = " + id)) {
                request.setAttribute("result", "failure");
                service.releaseAll();
                request.setAttribute("tip", "修改失败");
                return;
            }

            //分类标签
            String[] tagsStr = request.getParameterValues("tags");
            ArrayList tagIntList = IntUtil.getIntList(tagsStr);
            Iterator itr = null;
            if (!info.getTags().equals(
                    IntUtil.getIntStr(tagIntList, MallFrk.SEPERATOR))) {
                //删除旧的
                if (!service.deleteTagInfo("info_id = " + id)) {
                    request.setAttribute("result", "failure");
                    service.releaseAll();
                    request.setAttribute("tip", "修改失败");
                    return;
                }

                Integer ii = null;
                TagInfoBean tagInfo = null;
                itr = tagIntList.iterator();
                while (itr.hasNext()) {
                    ii = (Integer) itr.next();
                    int tagInfoId = service.getNumber("id", "wxsj_tag_info",
                            "max", "id > 0") + 1;
                    tagInfo = new TagInfoBean();
                    tagInfo.setAdminId(0);
                    tagInfo.setCreateDatetime(DateUtil.getNow());
                    tagInfo.setId(tagInfoId);
                    tagInfo.setInfoId(id);
                    tagInfo.setTagId(ii.intValue());
                    if (!service.addTagInfo(tagInfo)) {
                        request.setAttribute("result", "failure");
                        service.releaseAll();
                        request.setAttribute("tip", "修改失败");
                        return;
                    }
                }

                set = "tags = '"
                        + IntUtil.getIntStr(tagIntList, MallFrk.SEPERATOR)
                        + "'";
                if (tagIntList.size() == 0) {
                    set += ", has_tag = 0";
                } else {
                    set += ", has_tag = 1";
                }
                if (!service.updateInfo(set, "id = " + id)) {
                    request.setAttribute("result", "failure");
                    service.releaseAll();
                    request.setAttribute("tip", "修改失败");
                    return;
                }
            }

            //地区标签
            String[] areaTagsStr = request.getParameterValues("areaTags");
            ArrayList areaTagIntList = IntUtil.getIntList(areaTagsStr);
            if (!info.getAreaTags().equals(
                    IntUtil.getIntStr(areaTagIntList, MallFrk.SEPERATOR))) {
                //删除旧的
                if (!service.deleteAreaTagInfo("info_id = " + id)) {
                    request.setAttribute("result", "failure");
                    service.releaseAll();
                    request.setAttribute("tip", "修改失败");
                    return;
                }

                Integer ii = null;
                AreaTagInfoBean tagInfo = null;
                itr = areaTagIntList.iterator();
                while (itr.hasNext()) {
                    ii = (Integer) itr.next();
                    int tagInfoId = service.getNumber("id",
                            "wxsj_area_tag_info", "max", "id > 0") + 1;
                    tagInfo = new AreaTagInfoBean();
                    tagInfo.setAdminId(0);
                    tagInfo.setCreateDatetime(DateUtil.getNow());
                    tagInfo.setId(tagInfoId);
                    tagInfo.setInfoId(id);
                    tagInfo.setTagId(ii.intValue());
                    if (!service.addAreaTagInfo(tagInfo)) {
                        request.setAttribute("result", "failure");
                        service.releaseAll();
                        request.setAttribute("tip", "修改失败");
                        return;
                    }
                }

                set = "area_tags = '"
                        + IntUtil.getIntStr(areaTagIntList, MallFrk.SEPERATOR)
                        + "'";
                if (tagIntList.size() == 0) {
                    set += ", has_area_tag = 0";
                } else {
                    set += ", has_area_tag = 1";
                }
                if (!service.updateInfo(set, "id = " + id)) {
                    request.setAttribute("result", "failure");
                    service.releaseAll();
                    request.setAttribute("tip", "修改失败");
                    return;
                }
            }

            //提交事务
            service.getDbOp().commitTransaction();

            service.releaseAll();

            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：删除一个信息
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void deleteInfo(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));

        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);

        //开始事务
        service.getDbOp().startTransaction();

        ///删除标签、信息对应记录
        service.deleteTagInfo("info_id = " + id);
        service.deleteAreaTagInfo("info_id = " + id);

        //删除信息
        service.deleteInfo("id = " + id);
        //删除回复
        service.deleteReply("parent_id = " + id);

        //提交事务
        service.getDbOp().commitTransaction();

        service.releaseAll();
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：修改一个回复
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void editReply(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        ReplyBean reply = service.getReply("id = " + id);
        if (reply == null) {
            service.releaseAll();
            return;
        }

        request.setAttribute("reply", reply);
        
        if ("get".equalsIgnoreCase(request.getMethod())) {            
            service.releaseAll();
            return;
        } else if ("post".equalsIgnoreCase(request.getMethod())) {
            String content = StringUtil.dealParam(request
                    .getParameter("content"));
            if (StringUtil.isNull(content)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入内容");
                service.releaseAll();
                return;
            }
            String userNick = StringUtil.dealParam(request
                    .getParameter("userNick"));
            if (StringUtil.isNull(userNick)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入用户昵称");
                service.releaseAll();
                return;
            }

            String set = "content = '" + content + "', user_nick = '"
                    + userNick + "'";
            service.updateReply(set, "id = " + id);

            service.releaseAll();
            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：删除一个回复
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void deleteReply(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        ReplyBean reply = service.getReply("id = " + id);
        if (reply == null) {
            service.releaseAll();
            return;
        }
        
        request.setAttribute("reply", reply);
        
        service.deleteReply("id = " + id);
        
        service.releaseAll();
    }
    
    public void validate(HttpServletRequest request,
            HttpServletResponse response){
        int id = StringUtil.toInt(request.getParameter("id"));
        String mobile = StringUtil.dealParam(request.getParameter("mobile"));
        if(id <= 0){
            request.setAttribute("result", "failure");
            return;
        }
        if(!StringUtil.checkTelephone(mobile)){
            request.setAttribute("result", "failure");
            return;
        }
        IMallService service = ServiceFactory.createMallService(
                IBaseService.CONN_IN_SERVICE, null);
        InfoBean info = service.getInfo("id = " + id);
        if(info == null){
            request.setAttribute("result", "failure");
            service.releaseAll();
            return;
        }
        if(info.getValidated() == 1){
            request.setAttribute("result", "success");
            service.releaseAll();
            return;
        }
        if(info.getTelephone().equals(mobile)){
            service.updateInfo("validated = 1", "id = " + id);
            request.setAttribute("result", "success");
            service.releaseAll();
            return;
        }
        else {
            request.setAttribute("result", "failure");
            service.releaseAll();
            return;
        }
    }
}
