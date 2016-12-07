package jc.friend.stranger;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.util.SqlUtil;

public class InitServlet extends HttpServlet {
	
	public static String testServer;
	
    public void init(ServletConfig config) throws ServletException {
        DbOperation.loadConfig(config.getServletContext());
        SqlUtil.setTestServer(DbOperation.testServer);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.add(Calendar.MINUTE, -cal.get(Calendar.MINUTE));
        cal.add(Calendar.SECOND, -cal.get(Calendar.SECOND));
//        HourTask task = new HourTask();
//        ht.schedule(task, cal.getTime(), 1000 * 60 * 60);
//
//        CastleUtil.startThread();
    }

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		
//		CastleUtil.endThread();
//		ht.cancel();
		super.destroy();
	}
}

