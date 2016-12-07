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

public class GameAction extends CustomAction{

	public static GameService service = new GameService();
	
	public String gamePath = Constants.RESOURCE_ROOT_PATH + "game/";
	public String gameUrlPath = Constants.RESOURCE_ROOT_URL + "game/";
	
	public String getGamePath() {
		return gamePath;
	}

	public void setGamePath(String gamePath) {
		this.gamePath = gamePath;
	}

	public String getGameUrlPath() {
		return gameUrlPath;
	}

	public void setGameUrlPath(String gameUrlPath) {
		this.gameUrlPath = gameUrlPath;
	}

	public GameAction(){
		
	}
	
	public GameAction(HttpServletRequest request){
		super(request);
	}
	
	// 上传
	public boolean updateGame(SmartUpload smUpload){
		
		String gname = smUpload.getRequest().getParameter("gname").trim();
		String description = smUpload.getRequest().getParameter("description");
		int cata = StringUtil.toInt(smUpload.getRequest().getParameter("cata"));
			
		if (cata <= 0){
			cata = 31;	// 如果分类错误就让它=射击游戏
		}
		
		if (gname == null || "".equals(gname)){
			this.setAttribute("tip", "没有输入标题?");
			return false;
		}
		if (gname.length() > 100){
			this.setAttribute("tip", "标题太长了.");
			return false;
		}
		if (description == null || "".equals(description)){
			this.setAttribute("tip", "没有输入简介?");
			return false;
		}
		
		File upPicFile = null;	// 游戏图片
		File upGameFile = null; // 游戏本身
		
		upPicFile = smUpload.getFiles().getFile(0);
		upGameFile = smUpload.getFiles().getFile(1);
		if ("".equals(upPicFile.getFileName())){
			this.setAttribute("tip", "没有选择图片?");
			return false;
		}
		if ("".equals(upGameFile.getFileName())){
			this.setAttribute("tip", "没有选择图片?");
			return false;
		}
		
		GameBean game = new GameBean();
		game.setName(gname);
		game.setDescription(description);
		game.setFitMobile("");
		game.setKb(upGameFile.getSize() / 1024);
		game.setFileUrl("");
		game.setRemoteUrl("");
		game.setPicUrl("");
		game.setHits(0);
		game.setCatalogId(cata);
		game.setCreateUserId(0);
		game.setUpdateUserId(0);
		game.setProviderId(0);
		game.setMark(0);
		
		int lastId = service.addGame(game);
		
		if (lastId > 0){
			String picName = lastId + "." + upPicFile.getFileExt();
			String gameName = lastId + "." + upGameFile.getFileExt();
			try {
				// 将图片上传之
				upPicFile.saveAs(gamePath + "pic/" + picName ,SmartUpload.SAVE_PHYSICAL);
				// 将游戏上传之
				upGameFile.saveAs(gamePath + "jar/" + gameName ,SmartUpload.SAVE_PHYSICAL);
				SqlUtil.executeUpdate("update jc_game set file_url='" + gameName + "',pic_url='" + picName + "' where id=" + lastId,0);
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
	
	// 修改游戏
	public boolean modifyGame(SmartUpload smUpload,GameBean bean){
		if (bean == null){
			return false;
		}
		String gname = smUpload.getRequest().getParameter("gname").trim();
		String description = smUpload.getRequest().getParameter("description");
		int cata = StringUtil.toInt(smUpload.getRequest().getParameter("cata"));
		
		if (cata <= 0){
			cata = 31;	// 如果分类错误就让它=射击游戏
		}
		
		if (gname == null || "".equals(gname)){
			this.setAttribute("tip", "没有输入标题?");
			return false;
		}
		if (gname.length() > 100){
			this.setAttribute("tip", "标题太长了.");
			return false;
		}
		if (description == null || "".equals(description)){
			this.setAttribute("tip", "没有输入简介?");
			return false;
		}
		
		bean.setName(gname);
		bean.setCatalogId(cata);
		bean.setDescription(description);
		
		File upPicFile = null;	// 游戏图片
		File upGameFile = null; // 游戏本身
		
		java.io.File file = null;

		upPicFile = smUpload.getFiles().getFile(0);
		upGameFile = smUpload.getFiles().getFile(1);
		
		if (!"".equals(upPicFile.getFileName())){
			// 如果上传了新图，要先删除旧图
			file = new java.io.File(gamePath + bean.getPicUrl());
			if (file.exists()){
				file.delete();
			}
			// 再传入新图
			try {
				upPicFile.saveAs(gamePath + "pic/" + bean.getPicUrl() ,SmartUpload.SAVE_PHYSICAL);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}
		}
		
		if (!"".equals(upGameFile.getFileName())){
			file = new java.io.File(gamePath + bean.getFileUrl());
			if (file.exists()){
				file.delete();
			}
			bean.setKb(upGameFile.getSize() / 1024);
			try {
				upGameFile.saveAs(gamePath + "jar/" + bean.getFileUrl() ,SmartUpload.SAVE_PHYSICAL);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}
		}
		
		SqlUtil.executeUpdate("update jc_game set `name`='" + bean.getName() + "',description='" + bean.getDescription() + "',update_datetime=now(),kb=" + bean.getKb() + ",catalog_id=" + bean.getCatalogId() + ",provider_id=" + bean.getProviderId() + ",mark=" + bean.getMark() + " where id=" + bean.getId(), 0);
		return true;
	}
}
