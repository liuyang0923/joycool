package jc.guest.pt;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.StringUtil;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;


public class JigsawAction extends GuestHallAction{

	public static JigsawService service = new JigsawService();
	public static String  winString;
	public static String ATTACH_URL_ROOT = "/rep/jigsaw/";

	public JigsawAction() {

	}

	public JigsawAction(HttpServletRequest request) {
		super(request);
	}

	public JigsawAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	// 得到一个登陆用户的 拼图信息
	public JigsawUserBean getUserDetails(){
		GuestUserInfo guestUser = getGuestUser();
		if(guestUser!=null){
			int uid=guestUser.getId();
			JigsawUserBean ubean=service.selectJigsawUserDetatils(uid);
			if(ubean!=null){
				return ubean;
			}
		}	
		return null;
	}
	
	// 判断图片是否存在
	public boolean picIsExits(int picID){
		JigsawBean jb =service.selectOneJigsaw(picID);
		if(jb!=null){
			return true;
		}
		return false;
	}
	
	//图片模式初始化
	public JigsawBean picInit(){
		
		int picID=getParameterInt("id");
		
		JigsawBean jb = (JigsawBean) session.getAttribute("jigsawBean");// 得到图片的信息
		
		if (jb == null || jb.getPinPic()==null || picID != 0) {// 初始化任选一幅图片拼图
			
			if(jb==null){
				jb = new JigsawBean();
			}
			
			if(jb!=null && picID!=0 && jb.getId()==picID && jb.getPinPic()!=null && jb.getPicGameState()==0 ){
				return jb;
			}
			int next=updateMoney();
			if(next==0){// 游币不够了，不能重新开一局
				return null;
			}
			
				JigsawBean bean=service.selectOneJigsaw(picID);
				if(bean==null){
					return null;
				}
				jb.setId(bean.getId());
				jb.setPicCols(bean.getPicCols());
				jb.setPicRows(bean.getPicRows());
				jb.setPicState(bean.getPicState());
				jb.setPicName(bean.getPicName());
				jb.setPicNum(bean.getPicNum());
				jb.setPicGameLevel(bean.getPicGameLevel());
			
			jb.getInit(1);
		}
		if (request.getParameter("r") != null && request.getParameter("c") != null && jb.getPicGameState() == 0&& jb.getPinPic() != null) {// 拼图移动
			if (jb.getPicGameState() == 0) {// 游戏中允许图片交换位置
				int erow = getParameterInt("r");
				int ecol = getParameterInt("c");
				jb.move(erow, ecol,1);
			}
		}
		
		if (request.getParameter("s") != null && request.getParameter("s").equals("2")) {// 重新开一局
			
			int next=updateMoney();
			if(next==0){// 游币不够了，不能重新开一局
				return null;
			}
			jb.getInit(1);
		}
		
		if (request.getParameter("s") != null) {// 显示原图的开合关
			if (request.getParameter("s").equals("1")) {
				jb.setButton(1);
			}
			if (request.getParameter("s").equals("3")) {
				jb.setButton(0);
			}
		}
		if(session.getAttribute("jigsawBean")!=null){
			session.setAttribute("jigsawBean", jb);
		}
		return jb;
	}
	
	// 数字模式初始化
	public JigsawBean numInit(){
		
		JigsawBean jb = (JigsawBean) session.getAttribute("jigsawBean");// 得到图片的信息
		GuestUserInfo guestUser = getGuestUser();
		JigsawUserBean jub=getUserDetails();
		
		if(jb!=null && guestUser!=null && jb.getUserID()!=guestUser.getId()){// 用户从未登录到登陆状态
			jb.setNumCols(3);
			jb.setNumRows(3);
			if(jub==null){
				int next=updateMoney();
				if(next==0){// 游币不够了，不能重新开一局
					jb.setPinNum(null);
					return null;
				}
				jb.setNumGameLevel(1);
			}else{
				int level=jub.getGameLevel();
				if(level>3&&level<7){
					jb.setNumCols(4);
					jb.setNumRows(4);
				}else if(level>6){
					jb.setNumCols(5);
					jb.setNumRows(5);
				}
				jb.setNumGameLevel(level);
			}
			jb.getInit(0);
			jb.setUserID(guestUser.getId());
			return jb;
		}

		if (jb == null || jb.getPinNum() == null) {// 初始化拼图
			if(jb==null){
				jb = new JigsawBean();
			}
			if(jub==null){
				jb.setNumCols(3);
				jb.setNumRows(3);
				jb.setNumGameLevel(1);
				if(guestUser!=null){//已注册用户扣钱
					int next=updateMoney();
					if(next==0){// 游币不够了，不能重新开一局
						jb.setPinNum(null);
						return null;
					}
				}
			}else{
				jb.setNumCols(3);
				jb.setNumRows(3);
				int level=jub.getGameLevel();
				if(level>3&&level<7){
					jb.setNumCols(4);
					jb.setNumRows(4);
				}else if(level>6){
					jb.setNumCols(5);
					jb.setNumRows(5);
				}
				jb.setNumGameLevel(level);
			}
			jb.getInit(0);
			
			if(guestUser!=null){
				jb.setUserID(guestUser.getId());
			}
			if(session.getAttribute("jigsawBean")!=null){
				session.setAttribute("jigsawBean", jb);
			}
			return jb;
		}
		
		if (request.getParameter("r") != null && request.getParameter("c") != null && jb.getNumGameState() == 0&& jb.getPinNum() != null) {// 移动
			if (jb.getNumGameState() == 0) {// 游戏中允许数字交换位置
				int erow = getParameterInt("r");
				int ecol = getParameterInt("c");
				jb.move(erow, ecol,0);
			}
		}
		
		if (request.getParameter("s") != null && request.getParameter("s").equals("2")) {// 开一局新的
			int next=updateMoney();
			if(next==0){// 游币不够了，不能重新开一局
				jb.setPinNum(null);
				return null;
			}
			if(jb.getNumGameState()==1){// 下一关
				jb.setNumGameLevel(jb.getNumGameLevel()+1);
				if(jb.getNumGameLevel()>3&&jb.getNumGameLevel()<7){
					jb.setNumCols(4);
					jb.setNumRows(4);
				}else if(jb.getNumGameLevel()>6){
					jb.setNumCols(5);
					jb.setNumRows(5);
				}
			}
			jb.getInit(0);
			
		}
		
		if (request.getParameter("s") != null && request.getParameter("s").equals("4")) {// 继续本关
			
			int next=updateMoney();
			if(next==0){// 游币不够了，不能重新开一局
				jb.setPinNum(null);
				return null;
			}
			
			jb.setNumCols(3);
			jb.setNumRows(3);
			int level=jub.getGameLevel();
			if(level>3&&level<7){
				jb.setNumCols(4);
				jb.setNumRows(4);
			}else if(level>6){
				jb.setNumCols(5);
				jb.setNumRows(5);
			}
			jb.setNumGameLevel(level);
			jb.getInit(0);
		}
		
		if (request.getParameter("s") != null && (request.getParameter("s").equals("5")||request.getParameter("s").equals("6"))) {// 回到最低关
			
			if(request.getParameter("s").equals("6")){
				int next=updateMoney();
				if(next==0){// 游币不够了，不能重新开一局
					jb.setPinNum(null);
					return null;
				}
			}
			jb.setNumGameLevel(1);
			jb.setNumCols(3);
			jb.setNumRows(3);
			if(guestUser!=null&&jub!=null){
				service.updateJigsawUserDetails(jub.getUid());
			}
			jb.getInit(0);
		}
		
		if(guestUser!=null){
			jb.setUserID(guestUser.getId());
		}
		
		return jb;
	}
	
	//增加用户的游戏经验值
	public void addPoint(){
		GuestUserInfo guestUser = getGuestUser();
		if(guestUser!=null){
			addPoint(guestUser,1);
		}
		return;
	}
	
	//扣除用户游币
	public int updateMoney(){
		GuestUserInfo guestUser = getGuestUser();
		if(guestUser!=null){
			if(guestUser.getMoney()<1){
				return 0;
			}
		  boolean update= updateMoney(guestUser.getId(),-1);
		  if(update){
			  return 1;
		  }
		}
		return 2;
	}

	/**
	 *未隐藏图片信息列表
	 * 
	 * @return
	 */
	public List getJigsawList(int level) {// 得到所有拼图的内容
		int c =service.selectIntResult("select count(id) from jigsaw where pic_state=1 and game_level="+level);
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "y");
		
		List list = service.selectJigsawLists(level,paging.getStartIndex(), paging.getCountPerPage());
        String s = paging.shuzifenye("piclist.jsp?l="+level, true, "|", response);
        setAttribute("PicList", s);
		return list;
	}

	/**
	 * 后台得到所有图片的信息列表
	 * 
	 * @return
	 */
	public List getJigsawLists() {// 得到所有拼图的内容
		int c =service.selectIntResult("select count(id) from jigsaw ");
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		
		List list = service.selectJigsawDetailsLists(paging.getStartIndex(), paging.getCountPerPage());
        String s = paging.shuzifenye("index.jsp", false, "|", response);
        setAttribute("JigsawList", s);
		return list;
	}
	
	
	
	/**
	 * 得到数据库中随便一幅拼图，默认为按主键排序得到的第一幅图
	 * 
	 * @return
	 */
	public JigsawBean getRandJigsaw() {
		return service.selectOneJigsaw();
	}

	/**
	 * 换图玩
	 * 
	 */
	public void changePic() {
		int pid = getParameterInt("p");
		JigsawBean bean = new JigsawBean();
		if (pid == 0) {
			bean = service.selectOneJigsaw();
		} else {
			bean = service.selectOneJigsaw(pid);
			bean.setPicGameLevel(1);
		}
		session.setAttribute("jigsawBean", bean);// 覆盖掉session中原来的bean
		return;
	}
	
	
	// 查看游戏是否结束
	public boolean checkEnd(JigsawBean jb,int playType){
		if(playType == 0){
			if(jb.getNumGameState()==1){
				return true;
			}
		}else if(playType == 1){
			if(jb.getPicGameState()==1){
				return true;
			}
		}
		
		boolean end=jb.checkEnd(playType);

		if(end){
			
			JigsawUserBean bean=new JigsawUserBean();
			GuestUserInfo guestUser = getGuestUser();
			String winName="路人甲";
		
			if(guestUser!=null){
				if(guestUser.getLevel() < GuestHallAction.point.length){
					addPoint();// 增加游戏经验
				}
				winName=guestUser.getUserNameWml();

				if(playType == 0){
					
					bean.setUid(guestUser.getId());
					int score = 2;
					if(jb.getNumGameLevel() > 3 && jb.getNumGameLevel() < 7){
						score = 3;
					} else if (jb.getNumGameLevel() > 6){
						score = 5;
					}
					bean.setMaxScore(score);
					if(jb.getNumGameLevel()<9){
						bean.setGameLevel(jb.getNumGameLevel()+1);
					}else if(jb.getNumGameLevel()==9){
						bean.setGameLevel(1);
					}
					service.insertJigsawUserDetails(bean);
					winString="祝贺"+winName+"完成了文字模式第"+jb.getNumGameLevel()+"关";
				}else if(playType == 1){
					String str="普通";
					if(jb.getPicGameLevel()==2){
						str="中等";
					}
					if(jb.getPicGameLevel()==3){
						str="困难";
					}
					winString="祝贺"+winName+"完成了图片模式"+str+"-"+jb.getPicName();
				}
			}
		}
		return end;
	}
	
	// 游戏积分排行榜
	public List getScoreList(){
		
		GuestUserInfo guestUser = getGuestUser();
		
		List list = service.selectUserList();
		if(guestUser!=null){
			for(int i=0; i<list.size();i++){
				JigsawUserBean bean =(JigsawUserBean) list.get(i);
				if(bean.getUid() == guestUser.getId()){
					request.setAttribute("MyPTScore", new Integer(i+1));
				}
			}
		}
		int c = list.size();
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		int start = paging.getStartIndex(), end = paging.getStartIndex()+ paging.getCountPerPage();
		if (list.size() < 10) {
			list = list.subList(paging.getStartIndex(), list.size());
		} else if (list.size() % 10 < 10) {
			if (start == (list.size() / 10) * 10) {
				end = list.size();
			}
			list = list.subList(start, end);
	    }
		
        String s = paging.shuzifenye("jigsawlist.jsp", false, "|", response);
        setAttribute("ScoreList", s);
		return list;
	}
	
	 //增加拼图的功能
	 public boolean addJigsawBean(){
	  JigsawBean bean=new JigsawBean();
	  int ps = getParameterInt("ps");
//	  int picState = getParameterInt("pics");
	  int pr = getParameterInt("pr");
	  int pc = getParameterInt("pc");
	  String pn = getParameterString("pn");
	  int picNum = getParameterInt("picn");
	  if (pn == null || "".equals(pn)){
	   request.setAttribute("tip", "没有输入图片名.<br/>");
	   return false;
	  }
	  if(ps==0||pr==0||pc==0||pr>5||pc>5||pn.length()>10){
	   request.setAttribute("tip", "填写项目不完整.<br/>");
	   return false;
	  }
	  boolean  isEixts=service.selectJigsawByNum(picNum);
	  if(isEixts){
		  request.setAttribute("tip",  "该图片编号已经存在.<br/>");
		  return false;
	  }
	  bean.setPicGameLevel(ps);
	  bean.setPicState(0);// 默认图片为隐藏
	  bean.setPicRows(pr);
	  bean.setPicCols(pc);
	  bean.setPicName(pn);
	  bean.setPicNum(picNum);
	  return service.intserJigsawBean(bean);
	 }
	
	 // 修改拼图的内容
	 public  boolean updateJigsawBean(){
	  
	  JigsawBean bean = null;
	  int id = this.getParameterInt("id");
	  if (id <= 0){
	   request.setAttribute("tip", "要修改的BEAN错误.<br/>");
	   return false;
	  } else {
	   bean = service.selectOneJigsaw2(id);
	   if (bean == null){
	    request.setAttribute("tip", "要修改的BEAN错误.<br/>");
	    return false;
	   }
	  }
	  int ps = getParameterInt("ps"); // 难度
	  int picState = getParameterInt("pics"); // 显示与否
	  int pr = getParameterInt("pr"); // 行
	  int pc = getParameterInt("pc"); // 列
	  String pn = getParameterString("pn"); // 名
	  int picNum = getParameterInt("picn"); // 编号
	  
	  if (ps < 1 || ps > 3){
	   request.setAttribute("tip", "难度选择错误.<br/>");
	   return false;
	  }
	  if (pn == null || "".equals(pn)){
	   request.setAttribute("tip", "没有输入图片名.<br/>");
	   return false;
	  }
	  if(ps==0||pr==0||pc==0||pn.length()>10){
	   request.setAttribute("tip", "填写项目不完整.<br/>");
	   return false;
	  }
	  bean.setPicGameLevel(ps);
	  bean.setPicState(picState);
	  bean.setPicRows(pr);
	  bean.setPicCols(pc);
	  bean.setPicName(pn);
	  bean.setPicNum(picNum);
	  return service.updateJigsawBean(bean);
	 }
	
	// 返回图片ID的最大值
	public int getMaxPicID(){
		return service.selectIntResult("select max(id) from jigsaw");
	}
	
	// 删除一个拼图信息
	public boolean deleteJigsawBean(){
		int ID=getParameterInt("id");
		 if(ID==0){
			 return false;
		 }
		 boolean del=service.deletePicDetatils(ID);
		 return  del;
	}
	
	// 搜索拼图信息
	public List searchJigsaw(){
		
		String str="";
		String cmd=getParameterString("s");
		String textn = "";
		
		String search="";
		if(cmd.equals("i")){// 按id搜索
				textn="sid";
				int id=getParameterInt("sid");
				if(id==0){
					return null;
				}
				str=""+id;
				search="id ="+id;
			
		}else if(cmd.equals("n")){// 按名字搜索
				textn="sn";
				str=getParameterString("sn");
				if(str==null||cmd==null||str.equals("")){
					return null;
				}
				str = StringUtil.toSqlLike(str.trim());
	
				search=" pic_name like '%"+str+"%' ";
		}
		
		int c =service.selectIntResult("select count(id) from jigsaw where "+search);
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		
		List list = service.searchJigsaw(cmd, str,paging.getStartIndex(), paging.getCountPerPage());
        String s = paging.shuzifenye("index.jsp?cmd=s&s="+cmd+"&" + textn + "="+str,true, "|", response);
        setAttribute("JigsawList", s);
		return list;
	}
	
	// 修改拼图的显示和隐藏
	public boolean updateJigsawState(String cmd){
		int ID=getParameterInt("id");
		JigsawBean bean=service.selectOneJigsaw2(ID);
		if(bean==null){
			return false;
		}
		int state =0;
		if(cmd.equals("x")){
			state=1;
		}
		boolean update=service.updateJigsawState(ID, state);
		return update;
	}
	
	
}
