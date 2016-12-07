package net.joycool.wap.call;

import java.util.List;

import jc.match.MatchAction;
import jc.match.MatchRank;
import jc.match.MatchUser;

public class Match {
	// 取得2张参赛者照片
	public static String getMatchUserPhoto(CallParam callParam) {
		MatchAction action = new MatchAction();
		MatchRank rank = null;
		MatchUser matchUser = null;
		List list = MatchAction.getTopTenList();
		if (list == null || list.size() == 0){
			return "";
		} else {
			int start = MatchAction.getRandomInt(0, list.size(), 2);
			StringBuilder girlsShow = new StringBuilder();
			rank = (MatchRank)list.get(start);
			if (rank != null){
				matchUser = MatchAction.getMatchUser(rank.getUserId());
				if (matchUser != null){
					girlsShow.append("<a href=\"/friend/match/vote.jsp?uid=" + matchUser.getUserId() + "\"><img src=\"" + action.getCurrentPhoto(matchUser,true) + "\" alt=\"o\" /></a>");
				}
			}
			rank = (MatchRank)list.get(start + 1);
			if (rank != null){
				matchUser = MatchAction.getMatchUser(rank.getUserId());
				if (matchUser != null){
					girlsShow.append("<a href=\"/friend/match/vote.jsp?uid=" + matchUser.getUserId() + "\"><img src=\"" + action.getCurrentPhoto(matchUser,true) + "\" alt=\"o\" /></a><br/>");
				}
			}
			return girlsShow.toString();
		}
	}
}
