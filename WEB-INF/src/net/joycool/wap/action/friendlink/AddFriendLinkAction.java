package net.joycool.wap.action.friendlink;

/*
 * Created on 2006-2-24
 *
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.friendlink.LinkRecordBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendLinkService;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class AddFriendLinkAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IFriendLinkService friendlinkService = ServiceFactory.createFriendLinkService();
		String friendlinkName=request.getParameter("friendlinkName");
		String friendlinkaddress=request.getParameter("friendlinkaddress");
		String linktype=request.getParameter("linktype");
		String tip = null;
	    boolean flag = true;
	    int b=friendlinkaddress.indexOf("http://",0);
		if (friendlinkName == null || friendlinkName.equals("")) {
            tip = "网站名称不能为空！";
            flag = false;
        } else if (friendlinkaddress == null || friendlinkaddress.equals("")) {
            tip = "网站地址不能为空！";
            flag = false;
        } else if(b==-1){
        	tip = "网站地址请按照下列格式书写：http://wap.joycool.net";
            flag = false;
        }else if(friendlinkService.chenkLinkRecordName("name='"+ StringUtil.toSql(friendlinkName)+"'")){
        	tip = "您申请的网站名称已经存在，请输入其他名称！";
            flag = false;
        }

        if (flag == false) {
            request.setAttribute("result", "failure");
            request.setAttribute("tip", tip);
            return mapping.findForward("check");
        } else {
		int linktypeid=StringUtil.toInt(linktype);
		
	//	int linktypeid=friendlinkService.getLinkTypesId("name="+linktype);
		int linkid = friendlinkService.getLinkId();  
		friendlinkService.InsertLinkRecord(linkid,friendlinkName,friendlinkaddress,linktypeid);
		LinkRecordBean linkrecord=friendlinkService.getLinkRecord("name='"+StringUtil.toSql(friendlinkName)+"'");
		request.setAttribute("linkrecord",linkrecord);
        }
		return mapping.findForward("success");
	}
}
