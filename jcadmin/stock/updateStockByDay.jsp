<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*, java.io.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.action.stock.*,
                 net.joycool.wap.bean.stock.*"%><%@ page import="java.awt.Color"%>                 <%@ page import="org.jfree.chart.ChartFactory"%><%@ page import="org.jfree.chart.JFreeChart"%><%@ page import="org.jfree.chart.ChartUtilities"%><%@ page import="org.jfree.chart.servlet.ServletUtilities"%><%@ page import="org.jfree.data.time.Day"%><%@ page import="org.jfree.data.time.TimeSeries"%><%@ page import="org.jfree.data.time.TimeSeriesCollection"%><%@ page import="org.jfree.data.xy.XYDataset"%><%!
public boolean drawPlotToFile(TimeSeries series, String fileName){
boolean flag = true;
try{
	TimeSeriesCollection dataset = new TimeSeriesCollection();
	dataset.addSeries(series);
	dataset.setDomainIsPointsInTime(false);

	XYDataset xydataset = (XYDataset) dataset;
	JFreeChart chart = ChartFactory.createTimeSeriesChart(
		    "",
		    "",
		    "",
		    xydataset,
		    true,
		    true,
		    true
	);

	chart.setBackgroundPaint(new Color(0xffffff));

	OutputStream output = new FileOutputStream(new File(fileName));
	ChartUtilities.writeChartAsPNG(output, chart, 250, 250);
	output.flush();
	output.close();
}catch(Exception e){
	flag = false;
}
return flag;
}
%>                
<%
//股票id与pv栏目统计名称映射
Hashtable map = new Hashtable();
map.put("7", "电子书总数");
map.put("2", "女性频道首页");
map.put("6", "打猎游戏");
map.put("5", "聊天室");

//更新每支股票对应的栏目pv
Date date = DateUtil.rollDate(new Date(), 0);

String logDate = DateUtil.formatDate(DateUtil.rollDate(date, -1));
Enumeration enu = map.keys();
while(enu.hasMoreElements()){
	String stockId = (String)enu.nextElement();
	String columnName = (String)map.get(stockId);
	if(columnName!=null){
		//从统计数据库获取pv
		String sql = "select pv from log_column where column_name='" + columnName + "' and log_date='" + logDate + "'";
		int pv = SqlUtil.getIntResult(sql, "statistics");
		//更新股票pv
		sql = "INSERT INTO jc_stock_pv(stock_id,pv,create_datetime) VALUES(" + stockId + "," + pv + "," + "'" + logDate + "')";
		SqlUtil.executeUpdate(sql, Constants.DBShortName);
	}
}
%>
<%
//清除当天原来插入的数据
String deleteSql = "delete from jc_stock_info where to_days(create_datetime)-to_days(now())=0";
SqlUtil.executeUpdate(deleteSql, Constants.DBShortName);
deleteSql = "delete from jc_stock_grail where to_days(create_datetime)-to_days(now())=0";
SqlUtil.executeUpdate(deleteSql, Constants.DBShortName);

IStockService stockService = ServiceFactory.createStockService();
//macq_2006-11-10_每天10点计算当天股票的最新数据_start
	// DecimalFormat df = new DecimalFormat("0.##");
	StockAction action = new StockAction();
	// 获取所有股票的昨日pv
	Vector stockPvList = stockService
			.getStockPvList("to_days(now())-to_days(create_datetime)=1");
	StockPvBean stockPv = null;
	// 循环取得每只股票的最新基线
	HashMap stockPvMap = new HashMap();
	for (int i = 0; i < stockPvList.size(); i++) {
		// 获取最新的一条股票数据基线
		stockPv = (StockPvBean) stockPvList.get(i);
		// 向map里添加元素,key为股票id,values为昨日股票栏目pv
		stockPvMap.put(new Integer(stockPv.getStockId()),
				new Integer(stockPv.getPv()));
		// 获取一支股票在昨天(10点前)的最新信息
	}
	// 获取昨天(10点前)所有个股信息集合
	Vector stockList = stockService.getStockList("price>0");
	StockBean stock = null;
	// 循环取得每只股票在昨天(10点前)的最新信息
	HashMap stockMap = new HashMap();
	for (int i = 0; i < stockList.size(); i++) {
		stock = (StockBean) stockList.get(i);
		// 向map里添加元素,key为股票id,values为昨日股票记录
		stockMap.put(new Integer(stock.getId()), stock);
	}
	// 获取每只股票的当日开盘跌涨幅度
	Iterator it = stockPvMap.keySet().iterator();
	StockInfoBean stockInfo = new StockInfoBean();
	float totalPirce = 0;
	int count = 0;
	while (it.hasNext()) {
		Integer stockPvKey = (Integer) it.next();
		// 基线
		Integer stockPvValues = (Integer) stockPvMap
				.get(stockPvKey);
		// 昨日最后价格
		stock = (StockBean) stockMap.get(stockPvKey);
		if (stock != null) {// 今日开盘涨幅比率
			int baseLine = 0;
			int base=0;
			//liuyi 2006-12-27 股票基线修改 start
			if(stock.getId()==7)//书城
			    base=500000/45;
			else if(stock.getId()==2)//女性
			    base=100/18;
			else if(stock.getId()==6)//狩猎
			    base=600000/60;
			else if(stock.getId()==5)//聊天
			    base=260000/50;
//			liuyi 2006-12-27 股票基线修改 end
			if(base>0)
			    baseLine=stockPvValues.intValue()/base;
		    //while(baseLine>2000){
		    //	baseLine = baseLine/2;
		    //}
			float rate = (float) action.getRate(stock.getPrice(), baseLine);
			// 今日开盘涨幅额度
			float ratePirce = (stock.getPrice() * rate) / 100;
			// 今日开盘涨幅额度
			float newPirce = stock.getPrice() + ratePirce;
			// 累加所有股票价格
			totalPirce = totalPirce + newPirce;
			// 计数股票数量
			count = count + 1;
			// set数据到stockInfoBean中
			stockInfo.setStockId(stockPvKey.intValue());
			stockInfo.setTodayPrice(newPirce);
			stockInfo.setYesterdayPrice(stock.getPrice());
			stockInfo.setRateValue(ratePirce);
			stockInfo.setRate(rate);
			stockInfo.setHighPrice(newPirce);
			stockInfo.setLowPrice(newPirce);
			stockInfo.setBaseLine(baseLine);
			// 添加一条个股信息
			stockService.addStockInfo(stockInfo);
		    // 更新股票价格
		    stockService.updateStock("price="+newPirce,"id="+stockPvKey.intValue());
		}
	}
	// 获取昨日最后大盘信息
	StockGrailBean stockGrail = stockService
			.getStockGrail("1=1 order by create_datetime desc limit 0,1");
	if (stockGrail == null) {
		stockGrail = new StockGrailBean();
	}
	// 实时指数
	float nowPrice = totalPirce;
	// 今天开盘价格
	float todayPrice = 0;
	// 昨天收盘价格
	float yesterdayPrice = 0;

	if (count != 0) {
		todayPrice = totalPirce / (float) count;
		yesterdayPrice = stockGrail.getNowPrice() / (float) count;
	}

	stockGrail.setNowPrice(nowPrice);
	stockGrail.setTodayPrice(todayPrice);
	stockGrail.setYesterdayPrice(yesterdayPrice);
	// 添加今天大盘信息
	stockService.addStockGrail(stockGrail);
// macq_2006-11-10_每天10点计算当天股票的最新数据_end

OsCacheUtil.flushGroup(OsCacheUtil.STOCK_GROUP);
OsCacheUtil.flushGroup(OsCacheUtil.STOCK_TIME_GROUP);
OsCacheUtil.flushGroup(OsCacheUtil.STOCK_TOP_GROUP);
%>
<%
//生成每支股票的曲线图
    String sql = "select distinct create_datetime from jc_stock_info order by create_datetime desc limit 0,50";
    List dateList = SqlUtil.getObjectList(sql, null, Constants.DBShortName);
    if(dateList==null){
	    out.print("没有数据。");
	    return;
    }
    
    int listSize = dateList.size();
    Date fromDate = (Date)dateList.get(listSize-1);
    Date toDate = (Date)dateList.get(0);
    
    boolean isWindows = false;
    String osName = System.getProperty("os.name");
	if(osName!=null){
		osName = osName.toLowerCase();
		if(osName.indexOf("windows")!=-1){
			isWindows = true;
		}
	}
	
String imagePath = (isWindows)?"E:/eclipse/workspace/joycool-portal/img/stock/":"/usr/local/joycool-rep/stock/";	
for(int k=0;k<stockList.size();k++){   
try{
	StockBean stockBean = (StockBean)stockList.get(k);
	int id = stockBean.getId();
    String condition = " create_datetime<='" + toDate + "' and create_datetime>='" + fromDate + "' and stock_id=" + id + " order by create_datetime ";
    List beanList = stockService.getStockInfoList(condition);   
    
    String label = "";
    TimeSeries series = new TimeSeries(label, Day.class);
    for(int i=0;i<beanList.size();i++){
    	StockInfoBean stockInfoBean = (StockInfoBean)beanList.get(i);
    	Day day = new Day(DateUtil.parseDate(stockInfoBean.getCreateDatetime(), DateUtil.normalDateFormat));
    	series.add(day, stockInfoBean.getYesterdayPrice());
    }   
    String fileName = imagePath + id + ".jpg";
    drawPlotToFile(series, fileName);   
    ImageUtil.scale(90, fileName, fileName);
}catch(Exception e){
	e.printStackTrace(System.out);
}
} 

//生成大盘的曲线图
String condition = " create_datetime<='" + toDate + "' and create_datetime>='" + fromDate + "'";
List beanList = stockService.getStockGrailList(condition);
String label = "";
TimeSeries series = new TimeSeries(label, Day.class);
for(int i=0;i<beanList.size();i++){
	StockGrailBean stockGrailBean = (StockGrailBean)beanList.get(i);
	Day day = new Day(DateUtil.parseDate(stockGrailBean.getCreateDatetime(), DateUtil.normalDateFormat));
	series.add(day, stockGrailBean.getYesterdayPrice());
}
String fileName = imagePath + "stockgrail.jpg";
drawPlotToFile(series, fileName); 
ImageUtil.scale(90, fileName, fileName);
%>
done.