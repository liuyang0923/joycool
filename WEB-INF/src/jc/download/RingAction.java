package jc.download;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

public class RingAction extends CustomAction{

	public static RingService service = new RingService();
	
	public String ringPath = Constants.RESOURCE_ROOT_PATH + "ring/";
	public String ringUrlPath = Constants.RESOURCE_ROOT_URL + "ring/";
	
	public String getRingPath() {
		return ringPath;
	}

	public void setRingPath(String ringPath) {
		this.ringPath = ringPath;
	}

	public String getRingUrlPath() {
		return ringUrlPath;
	}

	public void setRingUrlPath(String ringUrlPath) {
		this.ringUrlPath = ringUrlPath;
	}

	public RingAction(){
		
	}
	
	public RingAction(HttpServletRequest request){
		super(request);
	}
	
	// 上传
	public boolean updateRing(SmartUpload smUpload){
		
		String sname = smUpload.getRequest().getParameter("sname").trim();
		String singer = smUpload.getRequest().getParameter("singer");
		int cata = StringUtil.toInt(smUpload.getRequest().getParameter("cata"));
			
		if (cata <= 0){
			cata = 642;	// 如果分类错误就让它=本周推荐
		}
		
		if (sname == null || "".equals(sname)){
			this.setAttribute("tip", "没有输入歌名?");
			return false;
		}
		if (sname.length() > 50){
			this.setAttribute("tip", "歌名太长了.");
			return false;
		}
		if (singer == null || "".equals(singer)){
			this.setAttribute("tip", "没有输入歌手?");
			return false;
		}
		if (singer.length() > 50){
			this.setAttribute("tip", "歌手名太长了.");
			return false;
		}
		
		File upRingFile = null;	// 上传的铃声
		
		upRingFile = smUpload.getFiles().getFile(0);
		if ("".equals(upRingFile.getFileName())){
			this.setAttribute("tip", "没有选择铃声?");
			return false;
		}
		
		PringBean pring = new PringBean();
		pring.setCatalogId(cata);
		pring.setName(sname);
		pring.setSinger(singer);
		pring.setTypeId(0);
		pring.setFile("");
		pring.setRemoteUrl("");
		pring.setUserId(0);
		pring.setDownloadSum(0);
		
		int lastId = service.addRing(pring);
		
		if (lastId > 0){
//			String ringName = cata + "/" + lastId + "." + upRingFile.getFileExt();
			String ringName = lastId + "." + upRingFile.getFileExt();
			// 还要插入pring_file表
			PringFileBean pringFile = new PringFileBean();
			pringFile.setPringId(lastId);
			pringFile.setSize(upRingFile.getSize() / 1024);
			pringFile.setFileType(upRingFile.getFileExt());
			pringFile.setFile(ringName);
			service.addRingFile(pringFile);
			try {
				// 将铃声上传之
				upRingFile.saveAs(ringPath + "/" + ringName ,SmartUpload.SAVE_PHYSICAL);
				SqlUtil.executeUpdate("update pring set file='" + ringName + "' where id=" + lastId,0);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}
		} else {
			// 没插进去
			this.setAttribute("tip", "上传出错.");
			return false;
		}
		return true;
	}
	
	// 修改
	public boolean modifyRing(SmartUpload smUpload,PringBean bean){
		
		if (bean == null){
			return false;
		}
		
		String sname = smUpload.getRequest().getParameter("sname").trim();
		String singer = smUpload.getRequest().getParameter("singer");
		int cata = StringUtil.toInt(smUpload.getRequest().getParameter("cata"));
			
		if (cata <= 0){
			cata = 642;	// 如果分类错误就让它=本周推荐
		}
		
		if (sname == null || "".equals(sname)){
			this.setAttribute("tip", "没有输入歌名?");
			return false;
		}
		if (sname.length() > 50){
			this.setAttribute("tip", "歌名太长了.");
			return false;
		}
		if (singer == null || "".equals(singer)){
			this.setAttribute("tip", "没有输入歌手?");
			return false;
		}
		if (singer.length() > 50){
			this.setAttribute("tip", "歌手名太长了.");
			return false;
		}
		
		bean.setName(sname);
		bean.setSinger(singer);
		
		java.io.File file = null;
		
		File upRingFile = null;	// 上传的铃声
		
		upRingFile = smUpload.getFiles().getFile(0);
		if (!"".equals(upRingFile.getFileName())){
			// 如果上传了新铃声，要先删除原来的
			file = new java.io.File(ringPath + bean.getFile());
			if (file.exists()){
				file.delete();
			}
			// 再传入新有
			try {
				upRingFile.saveAs(ringPath + "/" + bean.getFile() ,SmartUpload.SAVE_PHYSICAL);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}
		}
		
		PringFileBean pringFile = service.getPringFile("pring_id=" + bean.getId());
		
		if (pringFile == null){
			return false;
		}
		// 重新向pring_file表写入size和扩展名
		pringFile.setSize(upRingFile.getSize() / 1024);
		pringFile.setFileType(upRingFile.getFileExt());
		
		SqlUtil.executeUpdate("update pring set catalog_id=" + cata + ",`name`='" + StringUtil.toSql(bean.getName()) + "',singer='" + StringUtil.toSql(bean.getSinger()) + "' where id=" + bean.getId(), 0);
		SqlUtil.executeUpdate("update pring_file set size=" + pringFile.getSize() + ",file_type='" + pringFile.getFileType() + "' where id=" + pringFile.getId(), 0);
		return true;
	}
}
