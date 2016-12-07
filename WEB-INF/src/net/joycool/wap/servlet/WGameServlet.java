/*
 * 创建日期 2006-5-6 作者李北金。
 */
package net.joycool.wap.servlet;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import jc.family.FamilyAction;
import jc.family.photo.FmPhotoAction;
import jc.imglib.ImgLibAction;
import jc.util.ImageUtil;

import net.joycool.wap.action.fs.FSWorld;
import net.joycool.wap.action.jcforum.ForumAction;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.cache.CacheAdmin;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.spec.castle.CastleUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RoomUtil;
import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.util.SqlUtil;

public class WGameServlet extends HttpServlet {
	private static final long serialVersionUID = 7695061712289902477L;

	public static long startTime = System.currentTimeMillis();	// 本次启动时间
	public static WGameThread thread1;
	public static FiveMinuteThread thread2;
	public static TenMinuteThread thread3;
	public static BaseThread thread4;
//	liq 2007-06-18 10秒线程 start
	public static TenSecondThread thread5;
	public static PetThread thread6;
	public static SqlThread thread7;
	 //liq 2007-06-18 10秒线程 end
	Timer ht = new Timer("joycoolTimer");
	
	public static String testServer;
	
    public void init(ServletConfig config) throws ServletException {
        DbOperation.loadConfig(config.getServletContext());
        loadOther(config.getServletContext());
        SqlUtil.setTestServer(DbOperation.testServer);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.add(Calendar.MINUTE, -cal.get(Calendar.MINUTE));
        cal.add(Calendar.SECOND, -cal.get(Calendar.SECOND));
        HourTask task = new HourTask();
        ht.scheduleAtFixedRate(task, cal.getTime(), 1000 * 60 * 60);

    	thread1 = new WGameThread();
    	thread2 = new FiveMinuteThread();
    	thread3 = new TenMinuteThread();
    	thread4 = new BaseThread();
    	thread5 = new TenSecondThread();
    	thread6 = new PetThread();
    	thread7 = new SqlThread();
        
    	
    	thread1.setName("WGameThread");
    	thread2.setName("FiveMinuteThread");
    	thread3.setName("TenMinuteThread");
    	thread4.setName("BaseThread");
    	thread5.setName("TenSecondThread");
    	thread6.setName("PetThread");
    	thread7.setName("SqlThread");
        
        thread1.start();
        //macq_2006-11-15_新建五分钟线程_start
        thread2.start();
        //macq_2006-11-15_新建五分钟线程_end
        //macq_2006-11-15_新建30分钟线程_start
        thread3.start();
        //macq_2006-11-15_新建30分钟线程_end
        //liuyi 2007-02-02 一分钟线程 start
        thread4.start();
        //liuyi 2007-02-02 一分钟线程 end
        //liq 2007-06-18 10秒线程 start
        thread5.start();
        //liq 2007-06-18 10秒线程 end
        thread6.start();
        thread7.start();
        
        CacheAdmin.scanner.setName("CacheScanThread");
        OnlineUtil.thread.setName("OnlineThread");
        RoomUtil.scanner.setName("RoomScanner");
        
        CacheAdmin.scanner.start();
        OnlineUtil.thread.start();
        RoomUtil.scanner.start();
        
        CastleUtil.startThread();
        FamilyAction.init();
    }

	private void loadOther(ServletContext context) {
		String rep = context.getInitParameter("rep");
		if(rep != null) {
			Constants.RESOURCE_ROOT_PATH = rep + "/";
			
			ForumAction.ATTACH_ROOT = rep + "/jcforum";
			JCRoomContentBean.ATTACH_ROOT = rep + "/chat";
			Constants.HOME_IMAGE_SMALL_WINDOWS_ROOT = rep + "/home/small";
			Constants.HOME_IMAGE_BIG_WINDOWS_ROOT = rep + "/home/big";
			Constants.CREATE_HOME_IMAGE_BIG_WINDOWS_ROOT = rep + "/home/small";
			Constants.CREATE_HOME_IMAGE_SMALL_WINDOWS_ROOT = rep + "/home/big";
			
			Constants.MYALBUM_FILE_PATH= rep + "/home/myalbum";
			Constants.FRIEND_FILE_PATH= rep + "/friend/attach";
			
			ForumAction.ATTACH_ROOT = rep + "/jcforum";
			ImgLibAction.ATTACH_ROOT = rep + "/box";
			FmPhotoAction.FM_PHOTO_UPLOAD_URL = rep + "/family/photo/";
			ImageUtil.ATTACH_ROOT = rep + "/box/";
		}
		String rep2 = context.getInitParameter("rep2");
		if(rep2 != null) {
			Constants.RESOURCE_ROOT_PATH_OLD = rep2 + "/";
		}
		String imgrep = context.getInitParameter("imgrep");
		if(imgrep != null)
			Constants.IMG_ROOT_URL = imgrep;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		
		FSWorld.saveallgames();		// 保存浮生记的所有未完成游戏
		
		ht.cancel();
		thread1.interrupt();
		thread2.interrupt();
		thread3.interrupt();
		thread4.interrupt();
		thread5.interrupt();
		thread6.interrupt();
		thread7.interrupt();
		
		CastleUtil.endThread();
		
		CacheAdmin.scanner.interrupt();
		OnlineUtil.thread.interrupt();
		RoomUtil.scanner.interrupt();
		
		FamilyAction.destroy();
		
		super.destroy();
	}
}

