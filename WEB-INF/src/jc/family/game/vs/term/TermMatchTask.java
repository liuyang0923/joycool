package jc.family.game.vs.term;

import java.util.TimerTask;

/**
 * 用来定时开始,结束游戏
 * 
 * @author qiuranke
 * 
 */
public class TermMatchTask extends TimerTask {
	TermBean term;
	TermMatchBean termMatch;

	public TermMatchTask(final TermBean term, final TermMatchBean termMatch) {// 最终不可以改变
		this.term = term;
		this.termMatch = termMatch;
	}

	public void run() {
		TermAction.termStart(term, termMatch);
	}
}
