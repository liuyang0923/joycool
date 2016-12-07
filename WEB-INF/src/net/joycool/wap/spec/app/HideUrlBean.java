package net.joycool.wap.spec.app;

import java.util.List;

public class HideUrlBean {
	public char visitC = 'a';
	public String lastURL;
	public List apphul;
	public String lastP;
	public String lastCT;
	public int apphuc;
	public void addVisitC() {
    	visitC++;
    	if(visitC > 'z')
    		visitC = 'a';
	}
}
