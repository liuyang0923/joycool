package jc.family.photo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import jc.family.FamilyAction;
import jc.family.FamilyUserBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;

public class FmPhotoAction extends FamilyAction {
	public FmPhotoAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	public static FmPhotoService service = new FmPhotoService(); 
	public static String FM_PHOTO_UPLOAD_URL = Constants.RESOURCE_ROOT_PATH + "family/photo/";
	public static String FM_PHOTO_UPLOAD_URL_SHORT = "family/photo/";
	public static String[] permissions = {"所有人可见","本家族人可见"};
	
	public void addPhoto(PageContext pageContext,FmAlbumBean albumBean,UserBean ub) {
		if (albumBean == null) {
			request.setAttribute("tip", "已没有该相册!");
			return;
		}
		if (ub == null){
			request.setAttribute("tip", "尚未登录,无权操作!");
			return;
		} else { // 权限判断
			FamilyUserBean fmUserBean = getFmUserByID(ub.getId());
			if (fmUserBean == null || fmUserBean.getFm_id() != albumBean.getFid()) {
				request.setAttribute("tip","非本家族用户无权上传!");
				return;// 没家族,或者用户访问的不是自己家族
			}
		}
		SmartUpload smUpload = new SmartUpload();
		try {
			smUpload.initialize(pageContext);
			smUpload.setAllowedFilesList("GIF,gif,JPG,jpg,PNG,png");
			smUpload.setMaxFileSize(50*1024);
			smUpload.upload();
		} catch (Exception e) {
			tip = "文件类型不正确,或体积太大.";
			request.setAttribute("tip", tip);
			return;
		}
		File file = smUpload.getFiles().getFile(0);
		String name = smUpload.getRequest().getParameter("pn");
		/**
		 * 检查参数，如果格式有误直接返回用户输入页并给出提示信息!
		 */
		if (name == null || name.trim().equals("")) {
			tip = "标题不能为空!";
			request.setAttribute("tip", tip);
			return;
		} else if (name.length() > 18){
			tip = "标题太长!";
			request.setAttribute("tip", tip);
			return;
		} else if (file == null || file.getSize() == 0) {
			tip = "请上传图片!";
			request.setAttribute("tip", tip);
			return;
		}
		// 检查文件大小
		int fileSize = file.getSize();
		if (fileSize == 0 || fileSize > Constants.MAX_ALBUMPHOTO_SIZE) {
			tip = "文件不能为空，或者文件大小太大!";
			request.setAttribute("tip", tip);
			return;
		}

		// 检查扩展名
		String fileExt = file.getFileExt();
		if (fileExt.equals("") || Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确!";
			request.setAttribute("tip", tip);
			return;
		}
		jc.imglib.ImgPoolBean pool = jc.util.ImageUtil.uploadImage(file.getBinaryData(), file.getM_startData(), file.getSize(), file.getFileExt(), ub.getId(), 9);
		if(pool == null) {
			request.setAttribute("tip", "文件上传失败!请重试!");
			return;
		}
//		String fileName = System.currentTimeMillis() + "." + fileExt;
//		// 上传文件
//		try {
//			file.saveAs(FM_PHOTO_UPLOAD_URL + fileName);
//			tip = "文件上传成功!";
//			request.setAttribute("tip", tip);
//		} catch (IOException e) {
//			tip = "文件上传失败!请重试!";
//			request.setAttribute("tip", tip);
//			e.printStackTrace();
//			return;
//		} catch (SmartUploadException e) {
//			tip = "文件上传失败!请重试!";
//			request.setAttribute("tip", tip);
//			e.printStackTrace();
//			return;
//		}			
//		try {
//			// 调整图片
//			ImageUtil.fitImage(FM_PHOTO_UPLOAD_URL + fileName, 300, 400, FM_PHOTO_UPLOAD_URL + fileName);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		}
		FmPhotoBean photoBean = new FmPhotoBean();
		photoBean.setPhotoName(name);
		photoBean.setUrl(pool.getUseFileName());
		photoBean.setFid(albumBean.getFid());
		photoBean.setAlbumId(albumBean.getId());
		photoBean.setUploadUid(ub.getId());
		service.insertPhotoBean(photoBean);
		
		jc.util.ImageUtil.insertCheck(pool, 9, photoBean.getId());
		
		service.upd("update fm_album set `count`=`count`+1 where id=" + albumBean.getId());
		request.setAttribute("result", "success");
	}
	
	/**
	 * 删除照片
	 * 
	 * @param photoBean
	 * @return
	 */
	public boolean delPhoto (FmPhotoBean photoBean, FamilyUserBean fmUserBean, int uid) {
		if (photoBean == null || photoBean.getFid() == 0) {
			request.setAttribute("tip", "照片已不存在!");
			return false;
		}
		if (photoBean.getUploadUid() != uid && !hasManagePower(fmUserBean,photoBean.getFid())) {
			request.setAttribute("tip", "您还没有该相册管理权限!");
			return false;
		}	
		service.upd("delete from fm_album_photo_comment where photo_id="+photoBean.getId()); // 删除照片评论
		service.upd("delete from fm_album_photo where id="+photoBean.getId()); // 删除照片
		service.upd("update fm_album set `count`=`count`-1 where id="+photoBean.getAlbumId()); // 相册照片数量减1
//		if (photoBean.getUrl().length() > 7) {
//			java.io.File file = new java.io.File(FM_PHOTO_UPLOAD_URL + photoBean.getUrl());
//			if (file.exists() && !file.isDirectory()) {
//				file.delete();
//			}
//		} else {// 未审核图片不删除,直接放到默认相册下
////			service.upd("update fm_album_photo set album_id=0 where id="+photoBean.getId());
//		}
		request.setAttribute("tip", "相片删除成功!");
		return true;
	}
	/**
	 * 修改相册
	 * 
	 * @param albumBean
	 * @return
	 */
	public boolean alterAlbum (FmAlbumBean albumBean, FamilyUserBean fmUserBean) {
		if (albumBean == null) {
			request.setAttribute("tip", "相册已不存在!");
			return false;
		}
		if (!hasManagePower(fmUserBean, albumBean.getFid())) {
			request.setAttribute("tip", "您还没有该相册管理权限!");
			return false;
		}
		int permission = this.getParameterInt("perm");
		String albumName = this.getParameterString("aln");
		if (albumName == null || albumName.length() < 1){
			request.setAttribute("tip", "相册名字不能为空!");
			return false;
		} 
		if (albumName.length() > 9){
			request.setAttribute("tip", "相册名字不能超过9个字!");
			return false;
		}
		albumBean.setAlbumName(albumName);
		albumBean.setPermission(permission);
		service.alterAlbumBean(albumBean);
		request.setAttribute("tip", "相册已经更改!");
		return true;
	}
	
	/**
	 * 删除相册
	 * 
	 * @param albumBean
	 * @return
	 */
	public boolean delAlbum (FmAlbumBean albumBean, FamilyUserBean fmUserBean, int uid) {
		if (albumBean == null) {
			request.setAttribute("tip", "相册已不存在!");
			return false;
		}
		if (!hasManagePower(fmUserBean, albumBean.getFid())) {
			request.setAttribute("tip", "您还没有该相册管理权限!");
			return false;
		}
		// 先删除相册下所有照片
		List photoList = service.getPhotoList("album_id=" + albumBean.getId());
		for (int i = 0 ; i < photoList.size(); i++) {
			FmPhotoBean photoBean = (FmPhotoBean) photoList.get(i);
			delPhoto(photoBean,fmUserBean,uid);
		}
		service.upd("delete from fm_album where id="+albumBean.getId());
		request.setAttribute("tip","相册已经删除!");
		return true;
	}
	
	/**
	 * 增加相册
	 * 
	 * @param fid
	 * @param uid
	 * @return
	 */
	public boolean addCat (FamilyUserBean fmUserBean, int fid, int uid) {
		if (!hasManagePower(fmUserBean, fid)) {
			request.setAttribute("tip", "您还没有该相册管理权限!");
			return false;
		}
		int permission = this.getParameterInt("permi");
		String albumName = this.getParameterString("aln");
		if (albumName == null || albumName.length() < 1){
			request.setAttribute("tip", "相册名字不能为空!");
			return false;
		} 
		if (albumName.length() > 9){
			request.setAttribute("tip", "相册名字不能超过9个字!");
			return false;
		} 
		FmAlbumBean albumBean = new FmAlbumBean();
		albumBean.setFid(fid);
		albumBean.setCreateUid(uid);
		albumBean.setAlbumName(albumName);
		albumBean.setPermission(permission);
		service.insertAlbumBean(albumBean);
		request.setAttribute("result", "sucsess");
		return true;
	}
	
	/**
	 * 添加评论
	 * 
	 * @param photoBean
	 * @param uid
	 * @return
	 */
	public boolean addComment (FmPhotoBean photoBean,int uid) {
		if (photoBean == null) {
			request.setAttribute("tip", "照片已不存在!");
			return false;
		}
		if (uid == 0) {
			request.setAttribute("tip", "您尚未登录,无法评论!");
			return false;
		}
		String content = this.getParameterString("con");
		if (content == null || content.length() < 1) {
			request.setAttribute("tip", "评论内容不能为空!");
			return false;
		}
		if (content.length() > 60) {
			request.setAttribute("tip", "评论内容不能超过60个字!");
			return false;
		}
		FmCommentBean commentBean = new FmCommentBean();
		commentBean.setContent(content);
		commentBean.setPhotoId(photoBean.getId());
		commentBean.setUid(uid);
		service.insertCommentBean(commentBean);
		service.upd("update fm_album_photo set comment_time=comment_time + 1 where id="+photoBean.getId()); // 评论数+1
		request.setAttribute("tip", "评论成功!");
		request.setAttribute("result", "sucsess");
		return true;
	}
	
	/**
	 * 用户上传的照片后台审核通过后,调用此方法向用户开放
	 * 
	 * @param id
	 * @param fileName
	 */
	public static void checkOver(int id ,String fileName) {
		service.upd("update fm_album_photo set photo_url='"+StringUtil.toSql(fileName)+"' where id="+id);
		//service.upd("update fm_album_photo set photo_use_url='"+StringUtil.toSql(fileName)+"' where id="+id);
	}
	
	/**
	 * 是否是管理员
	 * 
	 * @param fmUserBean
	 * @param fid
	 * @return
	 */
	public boolean hasManagePower (FamilyUserBean fmUserBean, int fid) {
		if (fmUserBean != null && fmUserBean.getFm_id() == fid && fmUserBean.isflagPhoto())
			return true;
		else 
			return false;
	}
	
	/**
	 * 相册是否可见
	 * 
	 * @param albumBean
	 * @param uid
	 * @return
	 */
	public boolean canSee(FmAlbumBean albumBean, int uid) {
		if (albumBean != null && albumBean.getPermission() == 1){ // 非默认相册,并且设置为本家族可见的相册
			FamilyUserBean fmUserBean = getFmUserByID(uid);
			if (fmUserBean == null || fmUserBean.getFm_id() != albumBean.getFid())
				return false;
		}
		return true;
	}
	
}
