package jc.imglib;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;

public class ImgLibAction extends CustomAction{
	
	public static ImgLibService service = new ImgLibService();
	IUserService userService = ServiceFactory.createUserService();
//	public static String ATTACH_ROOT = Constants.RESOURCE_ROOT_URL + "box";
	public static String ATTACH_ROOT = Constants.RESOURCE_ROOT_PATH + "box";
	public static String ATTACH_URL_ROOT ="/rep/box";
	public static int MAX_COUNT = 30;

	public ImgLibAction(){}
	
	public ImgLibAction(HttpServletRequest request){
		super(request);
		if (SqlUtil.isTest){
			MAX_COUNT = 5;
		}
	}
	
	public ImgLibAction(HttpServletRequest request,HttpServletResponse response){
		super(request,response);
		if (SqlUtil.isTest){
			MAX_COUNT = 5;
		}
	}
	
	public int getLoginUid(){
		return this.getLoginUser()==null?0:this.getLoginUser().getId();
	}
	
	public LibUser createNewUser(){
		LibUser libUser = new LibUser();
		libUser.setUserId(this.getLoginUid());
		libUser.setPrivacy(1);
		libUser.setFlag(0);
		service.addNewLibUser(libUser);
		return libUser;
	}
	
	public boolean canView(LibUser libUser,int userId){
		if (libUser == null){
			this.setAttribute("tip", "该用户还没有开通图库.");
			return false;
		}
		if (libUser.getPrivacy() == 1){
			// 仅自己可见
			if (libUser.getUserId() != userId){
				this.setAttribute("tip", "因为该用户进行了隐私设置,您无法看到该图片.");
				return false;
			}
		} else if (libUser.getPrivacy() == 2){
			// 仅好友可见
			if (libUser.getUserId() == userId){
				return true;
			} else {
				// 检测是否好友
				UserBean user = UserInfoUtil.getUser(libUser.getUserId());
				List list = user.getFriendList();
				if (!list.contains(String.valueOf(userId))){
					this.setAttribute("tip", "因为该用户进行了隐私设置,您无法看到该图片.");
					return false;
				} else {
					// 是否被加到了黑名单里
					boolean isABadGuys = userService.isUserBadGuy(userId,libUser.getUserId());
					if (isABadGuys){
						this.setAttribute("tip", "因为该用户进行了隐私设置,您无法看到该图片.");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// 把别人的图片考到我的图库中
	public boolean copyToMyLib(Lib lib,LibUser libUser){
		if (lib == null || libUser == null){
			this.setAttribute("tip", "图片或用户不存在.");
			return false;
		} else if (libUser.getCount() >= ImgLibAction.MAX_COUNT){
			this.setAttribute("tip", "添加失败!当前图片数已达图库容量上限,请删除一些图片后在进行添加!");
			return false;
		} else if (lib.getUserId() == this.getLoginUid() ){
			this.setAttribute("tip", "本来就是你自己的图片,无需复制.");
			return false;
		} else {
			Lib lib2 = new Lib();
			lib2.setUserId(this.getLoginUid());
			lib2.setTitle(lib.getTitle());
			lib2.setImg(lib.getImg());
			lib2.setFlag(lib.getFlag());
			service.addNewLib(lib2);
			return true;
		}
	}
	
	// 删除图片
	public boolean delPic(Lib lib){
		boolean result = false;
		if (lib == null){
			return result;
		} else {
			// 从硬盘中删除
//			if (!"o.gif".equals(lib.getImg()) && !"x.gif".equals(lib.getImg())){
//				java.io.File pic = new java.io.File(ATTACH_ROOT + "/" + lib.getImg());
//				if (pic.exists()){
//					pic.delete();
//				}
//			}
			service.delLib(" id=" + lib.getId());
			// 总数量－1
			SqlUtil.executeUpdate("update img_lib_user set `count`=`count`-1 where `count`>0 and user_id=" + lib.getUserId(),5);
		}
		return result;
	}
	
	// 上传图片  HttpServletResponse response
	public void upPic(SmartUpload smUpload){
		LibUser libUser = service.getLibUser(" user_id=" + this.getLoginUid());
		if (libUser == null){
			request.setAttribute("tip", "用户不存在.");
			sendError(3);
			return;
		}
		
		if (libUser.getCount() >= ImgLibAction.MAX_COUNT){
			request.setAttribute("tip", "上传失败!当前图片数已达图库容量上限,请删除一些图片后在进行上传!");
			sendError(3);
			return;
		}
		
		
		String title = smUpload.getRequest().getParameter("title").trim();
		
		if (title == null || "".equals(title) || title.length() > 12){
			request.setAttribute("tip", "您没有输入标题或标题过长,请重新输入.");
			sendError(3);
			return;
		}
		
		File upFile = null;
		upFile = smUpload.getFiles().getFile(0);
		if ("".equals(upFile.getFileName()) || upFile.getSize() < 10) {
			this.setAttribute("tip", "您还没有上传图片.");
			sendError(3);
			return;
		} else {

			jc.imglib.ImgPoolBean pool = jc.util.ImageUtil.uploadImage(upFile.getBinaryData(), upFile.getM_startData(), upFile.getSize(), upFile.getFileExt(), getLoginUid(), 6);
			if(pool == null) {
				request.setAttribute("tip", "文件上传失败!");
				sendError(3);
				return;
			}
			
			Lib lib = new Lib();
			lib.setUserId(this.getLoginUid());
			lib.setTitle(title);
			lib.setImg(pool.getUseFileName());
			if(pool.getFlag() == 0)
				lib.setFlag(0);
			else
				lib.setFlag(1);
			service.addNewLib(lib);
			
			jc.util.ImageUtil.insertCheck(pool, 6, lib.getId());
			

		}
		sendError(1);
	}
	
	// 发送错误(upPic专用)type=3跳到错误页面,type=1表示上传成功
	public void sendError(int type){
		try {
			request.getRequestDispatcher("mess.jsp?mid=" + type).forward(request, response);
			return;
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}