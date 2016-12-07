package net.joycool.wap.spec.tiny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 四则运算游戏
 * @datetime:1007-10-24
 */
public class Game1Action extends TinyAction{

	public Game1Action(HttpServletRequest request) {
		super(request);
	}
	
	public Game1Action(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	// 提问
	public void question() {
		int id = getParameterInt("id");	// 调用方id
	}
	
	// 回答
	public void answer() {
		int id = getParameterInt("id");	// 调用方id
	}
}
