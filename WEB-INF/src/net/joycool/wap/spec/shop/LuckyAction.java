package net.joycool.wap.spec.shop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.framework.CustomAction;

public class LuckyAction extends CustomAction {

	public LuckyAction() {
	}

	public LuckyAction(HttpServletRequest request) {
		super(request);
	}

	public LuckyAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	
	public void beLucky(){
		
	}
}
