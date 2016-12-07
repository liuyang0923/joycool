package net.joycool.wap.servlet;
/**
 * han_yan
 * 
 * 2006-10-30
 * 
 * ebookServlet
 */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.AdvertiseBean;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IEBookService;

import java.io.*;
import java.util.Date;
import java.util.Vector;

public class EbookServlet extends HttpServlet {

	private static final int LOGON = 1; 				// 登录,这里包含取目录列表和广告列表

	private static final int INTO_NEXT_CATALOG = 2; 	// 进下级目录,取目录列表

	private static final int BACK_TO_CATALOG = 3; 		// 返回上一级目录

	private static final int INTO_BOOK_CATALOG = 4; 	// 进书的章节目录

	private static final int BACKTO_BOOK_CATALOG = 5;	// 出书的章节目录,进列表目录

	private static final int READ_BOOK_CONTENT = 6; 	// 读取书的具体内容

	private static final int POLL = 7;					// 轮寻服务器

	private static final String GET_AD_COUNT = "3"; 	// 要随机取的广告条数

	private static final String IS_CATALOG = "0"; 		// 代表发送的是目录

	private static final String IS_EBOOK = "1"; 		// 代表发送的是图书

	private static final String LOCAL_ADDRESS = "/usr/local/joycool-rep/ebook/";

	//private static final String LOCAL_ADDRESS = "E:/ebook/";

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		int l_num = 0; 					// 取得该类电子书的总数目
		int CatelogCount = 0; 			// 该类电子书的总目录数

		String return_string = ""; 		// 取得的图书文件内容
		String sendUTF = ""; 			// 要发送的字符串
		String l_type = ""; 			// 是图书还是目录,0:目录,1:图书
		String catalog_id = ""; 		// 包含所有目录ID的字符串
		String catalog_name = ""; 		// 包含所有目录名称的字符串
		String l_id = ""; 				// 目录的ID号
		String b_id = ""; 				// 图书的ID号
		String t_id = ""; 				// 图书章节的id号
		String l_page_index = ""; 		// 要显示的第几个分页
		String l_page_big = ""; 		// 每页要显示多少条
		String t_start = ""; 			// 从哪里开始读文件
		String t_length = ""; 			// 要读取文件的长度
		String t_str = ""; 				// 单次要发送的图书内容
		String ebook_condition 	= null; // 图书目录列表查询条件
		String ad_condition 	= null; // 广告列表查询条件
		
		Vector vebookcatelog 	= null; // 所有目录Bean的集合
		Vector vadvertise 		= null; // 所有广告Bean的集合
		
		CatalogBean catalogbean	= null;	// 目录Bean
		EBookBean ebookbean		= null;	// 图书Bean

		ICatalogService catalogService = ServiceFactory.createCatalogService();
		IEBookService ebookService = ServiceFactory.createEBookService();

		switch (Integer.parseInt(request.getParameter("station"))) {
		case LOGON: {
			String advertise_name = ""; // 包含所有广告名称的字符串
			String advertise_wap = ""; // 包换所有广告网址的字符串
			// 取所有目录
			ebook_condition = "type = 'ebook'  and level = 1 order by id";
			vebookcatelog = catalogService.getCatalogList(ebook_condition);
			for (int i = 0; i < vebookcatelog.size(); i++) {
				catalogbean = (CatalogBean) vebookcatelog.get(i);
				catalog_id = catalog_id + this.littleBind(catalogbean.getId());
				catalog_name = catalog_name
						+ this.littleBind(catalogbean.getName());
				l_type = l_type + this.littleBind(IS_CATALOG);
			}
			// 取三条广告
			ad_condition = "status=1 ORDER BY rand() LIMIT " + GET_AD_COUNT;
			vadvertise = catalogService.getAdvertiseList(ad_condition);
			for (int i = 0; i < vadvertise.size(); i++) {
				AdvertiseBean advertisebean = (AdvertiseBean) vadvertise.get(i);
				advertise_name = advertise_name
						+ this.littleBind(advertisebean.getAd_name());
				advertise_wap = advertise_wap
						+ this.littleBind(advertisebean.getAd_wap());
			}

			sendUTF = this.bigBind("l_id", catalog_id)
					+ this.bigBind("l_name", catalog_name)
					+ this.bigBind("l_type", l_type)
					+ this.bigBind("ad_name", advertise_name)
					+ this.bigBind("ad_wap", advertise_wap);

			this.sendMsg(sendUTF, response);

			break;
		}
		case INTO_NEXT_CATALOG: {
			l_id = request.getParameter("l_id"); // 目录的ID号
			l_page_index = request.getParameter("l_page_index"); // 要显示的第几个分页
			l_page_big = request.getParameter("l_page_big"); // 每页要显示多少条

			ebook_condition = "id =" + l_id;
			catalogbean = catalogService.getCatalog(ebook_condition);

			if (catalogbean.getChild_num() > 0) {// 有下级目录

				ebook_condition = "parent_id=" + l_id + " order by id limit "
						+ (Integer.parseInt(l_page_index) - 1)
						* Integer.parseInt(l_page_big) + "," + l_page_big;

				vebookcatelog = catalogService.getCatalogList(ebook_condition);
				l_num = vebookcatelog.size();
				for (int i = 0; i < vebookcatelog.size(); i++) {
					catalogbean = (CatalogBean) vebookcatelog.get(i);
					catalog_id = catalog_id
							+ this.littleBind(catalogbean.getId());
					catalog_name = catalog_name
							+ this.littleBind(catalogbean.getName());
					l_type = l_type + this.littleBind(IS_CATALOG);
				}
				ebook_condition = "parent_id=" + l_id;
				CatelogCount = catalogService.getCatalogList(ebook_condition)
						.size();
				sendUTF = this.bigBind("l_id", catalog_id)
						+ this.bigBind("l_name", catalog_name)
						+ this.bigBind("l_type", l_type)
						+ this.bigBind("l_num", String.valueOf(l_num))
						+ this.bigBind("l_big", String.valueOf(CatelogCount));
			} else {// 没有下级目录
				ebook_condition = "catalog_id=" + l_id + " order by id limit "
						+ (Integer.parseInt(l_page_index) - 1)
						* Integer.parseInt(l_page_big) + "," + l_page_big;

				Vector vebook = ebookService.getEBooksList(ebook_condition);
				l_num = vebook.size();// 单次传递的电子书目录个数

				for (int i = 0; i < vebook.size(); i++) {
					ebookbean = (EBookBean) vebook.get(i);
					catalog_id = catalog_id
							+ this.littleBind(ebookbean.getId());
					catalog_name = catalog_name
							+ this.littleBind(ebookbean.getName());
					l_type = l_type + this.littleBind(IS_EBOOK);
				}

				ebook_condition = "catalog_id=" + l_id;
				CatelogCount = ebookService.getEBooksList(ebook_condition)
						.size();// 取得该类电子书的总数目

				sendUTF = this.bigBind("l_id", catalog_id)
						+ this.bigBind("l_name", catalog_name)
						+ this.bigBind("l_type", l_type)
						+ this.bigBind("l_num", String.valueOf(l_num))
						+ this.bigBind("l_big", String.valueOf(CatelogCount));
			}
			this.sendMsg(sendUTF, response);
			break;
		}
		case BACK_TO_CATALOG: {
			l_id = request.getParameter("l_id"); // 目录的ID号
			b_id = request.getParameter("b_id"); // 图书的ID号
			l_page_index = request.getParameter("l_page_index"); // 要显示的第几个分页
			l_page_big = request.getParameter("l_page_big"); // 每页要显示多少条
			if (null != l_id) {
				ebook_condition = " type=(select type from pcatalog where id ="
						+ l_id + ") and level =1 order by id limit "
						+ (Integer.parseInt(l_page_index) - 1)
						* Integer.parseInt(l_page_big) + "," + l_page_big;
			}
			if (null != b_id) {
				ebook_condition = " type=(select type from pcatalog where id ="
						+ "(select catalog_id from pebook where id =" + b_id
						+ ")) and level =(select level from pcatalog where id="
						+ "(select catalog_id from pebook where id =" + b_id
						+ "))order by id limit "
						+ (Integer.parseInt(l_page_index) - 1)
						* Integer.parseInt(l_page_big) + "," + l_page_big;
			}

			vebookcatelog = catalogService.getCatalogList(ebook_condition);
			l_num = vebookcatelog.size();// 单次传递的电子书目录个数

			for (int i = 0; i < vebookcatelog.size(); i++) {
				catalogbean = (CatalogBean) vebookcatelog.get(i);
				catalog_id = catalog_id + this.littleBind(catalogbean.getId());
				catalog_name = catalog_name
						+ this.littleBind(catalogbean.getName());
				l_type = l_type + this.littleBind(IS_CATALOG);
			}
			if (null != l_id) {
				ebook_condition = " type=(select type from pcatalog where id ="
						+ l_id + ")and  level =1 order by id";
			}
			if (null != b_id) {
				ebook_condition = " type=(select type from pcatalog where id ="
						+ "(select catalog_id from pebook where id =" + b_id
						+ ")) and level =(select level from pcatalog where id="
						+ "(select catalog_id from pebook where id =" + b_id
						+ ")) order by id";
			}
			CatelogCount = catalogService.getCatalogList(ebook_condition)
					.size();// 取得该类电子书的总目录数

			sendUTF = this.bigBind("l_id", catalog_id)
					+ this.bigBind("l_name", catalog_name)
					+ this.bigBind("l_type", l_type)
					+ this.bigBind("l_num", String.valueOf(l_num))
					+ this.bigBind("l_big", String.valueOf(CatelogCount));

			this.sendMsg(sendUTF, response);
			break;
		}
		case INTO_BOOK_CATALOG: {
			b_id = request.getParameter("b_id");
			ebook_condition = " id =" + b_id;
			ebookbean = ebookService.getEBook(ebook_condition);
			String b_name = ebookbean.getName();// 书名
			String b_description = ebookbean.getDescription();// 书的描述
			String b_author = ebookbean.getAuthor();// 作者

			int chapterscount = this.readInfo(LOCAL_ADDRESS
					+ ebookbean.getFileUrl() + "/txt/");

			sendUTF = this.bigBind("b_num", String.valueOf(chapterscount))
					+ this.bigBind("b_name", b_name)
					+ this.bigBind("b_description", b_description)
					+ this.bigBind("b_author", b_author);

			this.sendMsg(sendUTF, response);
			break;
		}
		case BACKTO_BOOK_CATALOG: {
			b_id = request.getParameter("b_id");
			l_page_index = request.getParameter("l_page_index");
			l_page_big = request.getParameter("l_page_big");
			ebook_condition = " catalog_id = (select catalog_id from pebook where id = "
					+ b_id
					+ ") order by id limit "
					+ (Integer.parseInt(l_page_index) - 1)
					* Integer.parseInt(l_page_big) + "," + l_page_big;
			Vector vebook = ebookService.getEBooksList(ebook_condition);
			l_num = vebook.size();// 单次传递的电子书目录个数

			for (int i = 0; i < vebook.size(); i++) {
				ebookbean = (EBookBean) vebook.get(i);
				catalog_id = catalog_id + this.littleBind(ebookbean.getId());
				catalog_name = catalog_name
						+ this.littleBind(ebookbean.getName());
				l_type = l_type + this.littleBind(IS_EBOOK);
			}

			ebook_condition = "catalog_id=(select catalog_id from pebook where id = "
					+ b_id + ")";
			CatelogCount = ebookService.getEBooksList(ebook_condition).size();// 取得该类电子书的总数目

			sendUTF = this.bigBind("l_id", catalog_id)
					+ this.bigBind("l_name", catalog_name)
					+ this.bigBind("l_type", l_type)
					+ this.bigBind("l_num", String.valueOf(l_num))
					+ this.bigBind("l_big", String.valueOf(CatelogCount));

			this.sendMsg(sendUTF, response);

			break;
		}
		case READ_BOOK_CONTENT: {
			b_id = request.getParameter("b_id");
			t_id = request.getParameter("t_id");
			t_start = request.getParameter("t_start");// 取得从哪开始读文件
			t_length = request.getParameter("t_length");// 取得传输多少个字符

			ebook_condition = "id=" + b_id;

			ebookbean = ebookService.getEBook(ebook_condition);
			String readAddress = LOCAL_ADDRESS + ebookbean.getFileUrl()
					+ "/txt/" + t_id;

			return_string = fillOperation(readAddress);
			t_str = this.mySubString(return_string, Integer.parseInt(t_start),
					Integer.parseInt(t_length));

			sendUTF = this.bigBind("b_id", b_id)
					+ this.bigBind("t_id", t_id)
					+ this.bigBind("t_total", String.valueOf(return_string
							.length()))
					+ this.bigBind("t_length", String.valueOf(t_str.length()))
					+ this.bigBind("t_str", t_str);
			this.sendMsg(sendUTF, response);
			break;
		}
		case POLL: {
			sendUTF = this.bigBind("pool", "right");
			this.sendMsg(sendUTF, response);
			break;
		}
		default: {
			System.out.println("--------------------------------");
			System.out
					.println("The station in the default is ="
							+ request.getParameter("station") + ", time= "
							+ new Date());
			System.out.println("---------------------------------");
			break;
		}
		}
	}
	/**
	 * 截取要发送的内容
	 * 
	 * @param content
	 * @param start
	 * @param length
	 * @return
	 */
	private String mySubString(String content, int start, int length) {
		
		if (start > content.length()) {
			return content;
		} else if (length > content.length()) {
			return content.substring(start, content.length());
		} else {
			if ((start + length) > content.length()) {
				return content.substring(start, content.length());
			} else {
				return content.substring(start, start + length);
			}
		}
	}
	
	/**
	 * 发送消息字符串
	 * 
	 * @param sendUTF
	 * @param response
	 * @throws IOException
	 */
	private void sendMsg(String sendUTF, HttpServletResponse response)
			throws IOException {

		DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		DataOutputStream dataoutputstream1 = new DataOutputStream(
				bytearrayoutputstream);

		dataoutputstream1.writeUTF(sendUTF);
		byte[] param = bytearrayoutputstream.toByteArray();
		System.out.println("-----------------------------------------");
		System.out.println(new String(param, 2, param.length - 2, "UTF-8"));
		System.out.println("-----------------------------------------");
		dos.write(param, 2, param.length - 2);

		bytearrayoutputstream.close();
		dataoutputstream1.close();
		dos.flush();
		dos.close();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private String bigBind(String bind_name, String bind_value) {
		return "&[" + bind_name + "===" + bind_value + "]&";
	}

	private String littleBind(String bind_name) {
		return "{" + bind_name + "}";
	}

	private String littleBind(int number) {
		return "{" + number + "}";
	}
	
	/**
	 * 读取要发送的内容
	 * @param text_url
	 * @return
	 */
	private String fillOperation(String text_url) {
		String contents = "";
		DataInputStream fl;
		try {
			fl = new DataInputStream(new FileInputStream(text_url));
			byte[] bytes = new byte[fl.available()];
			fl.read(bytes);
			contents = new String(bytes, "UTF-8");
			fl.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contents;
	} 

	/**
	 * 取得请求的书籍的章节信息
	 * 
	 * @param directoryName
	 * @return String[] get the chapters'name from the inf.txt
	 * @throws IOException
	 * @throws Exception
	 */
	private int readInfo(String directoryName) throws IOException {

		File currentFile = new File(directoryName);
		String allFiles[] = currentFile.list();
		if (allFiles != null) {
			for (int i = 0; i < allFiles.length; i++) {

				if (allFiles[i].equals("inf.txt")) {

					FileInputStream in = new FileInputStream(directoryName
							+ "inf.txt");
					InputStreamReader reader = new InputStreamReader(in,
							"UTF-8");
					BufferedReader br = new BufferedReader(reader);
					String buffer = br.readLine();
					// while (!buffer.startsWith("章名") && buffer != null) {
					// buffer = br.readLine();
					// }
					if (buffer != null) {
						String[] chapters = buffer.split(",");
						chapters[0] = chapters[0].substring(chapters[0]
								.indexOf(":") + 1);
						return chapters.length;
					}
				}
			}
		}
		return 0;
	}
}
