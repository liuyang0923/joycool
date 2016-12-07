package net.joycool.wap.spec.tiny;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhouj
 * @explain： 2D魔方
 * @datetime:1007-10-24
 */
public class Game2Action extends TinyAction{

	public Game2Action(HttpServletRequest request) {
		super(request);
	}
	
	public Game2Action(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	// 显示和……转
	public void view() {
		int id = getParameterInt("id");	// 调用方id
	}
}
