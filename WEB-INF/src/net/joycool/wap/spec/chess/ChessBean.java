package net.joycool.wap.spec.chess;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

/**
 * @author zhouj
 * @explain： 中国象棋一局
 * @datetime:1007-10-24
 */
public class ChessBean {
	public static int WIDTH = 9;		// 棋盘宽度
	public static int HEIGHT = 10;		// 棋盘长度
	public static int GRID = WIDTH * HEIGHT;		// 总格子数量
	
	public static String[] statusNames = {"未开始", "进行中", "未知", "未知", "未知", "已结束"};
	public static char[] numbers = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'};
	
	//TODO 是否被将军
	public boolean isBind = false;
	public static int interupt = 300;		// 600秒超时
	public static int STATUS_READY = 0;
	public static int STATUS_PLAY = 1;
	public static int STATUS_END = 5;	// 棋局结束
	public static int[] defBoard = {
		11,12,14,15,16,15,14,12,11,
		0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 13,0, 0, 0, 0, 0, 13,0,
		17,0, 17,0, 17,0, 17,0, 17,
		0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0,
		27,0, 27,0, 27,0, 27,0, 27,
		0, 23,0, 0, 0, 0, 0, 23,0,
		0, 0, 0, 0, 0, 0, 0, 0, 0,
		21,22,24,25,26,25,24,22,21,
	};	//		默认棋盘
	public static String[] backBoard = {
		"┌", "┬", "┬", "┬", "┬", "┬", "┬", "┬", "┐", 
		"├", "┼", "┼", "┼", "┼", "┼", "┼", "┼", "┤",
		"├", "┼", "┼", "┼", "┼", "┼", "┼", "┼", "┤",
		"├", "┼", "┼", "┼", "┼", "┼", "┼", "┼", "┤",
		"└", "┴", "┴", "┴", "┴", "┴", "┴", "┴", "┘", 
		"┌", "┬", "┬", "┬", "┬", "┬", "┬", "┬", "┐",
		"├", "┼", "┼", "┼", "┼", "┼", "┼", "┼", "┤",
		"├", "┼", "┼", "┼", "┼", "┼", "┼", "┼", "┤",
		"├", "┼", "┼", "┼", "┼", "┼", "┼", "┼", "┤",
		"└", "┴", "┴", "┴", "┴", "┴", "┴", "┴", "┘", 
	};	//		背景
	public static String[] backBoard2 = {
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"十", "十", "十", "十", "十", "十", "十", "十", "十", 
	};	//		背景2，为了某些非等宽字体的手机，制表符无法使用
	public static String[] chessNames = {"十", 
		"一", "十", "十", "十", "十", "十", "十", "十", "十", "十", 
		"车", "马", "炮", "相", "仕", "帅", "兵", "十", "十", "十",
		"车", "马", "炮", "象", "士", "将", "卒", "十", "十", "十"};	// 棋子名称
	
	int id;
	int[] board;		// 棋盘，包含每个子和子的所属，每个数0表示无子，11-20表示红色子，21-30表示黑色子
	int userId1;
	int userId2;	// 参战双方，userId1先手
	
	int moveCount;		// 走到第几步
	long startTime;		// 本局开始时间
	long endTime;		// 本局结束时间
	int status = 0;		// 0 未开始 1 进行中 2 已经结束
	int winSide = 0;	// 0 未结束 1 红胜 2 黑胜 3平局
	int redGeneralPosition = 4;	//	红旗帅的位置 added by leihy
	int blackGeneralPosition = 85;  //黑棋将的位置  added by leihy
	
	int flag;
	int point;			// 积分大于的才能进入
	int drawSide;		// 求和方，0表示无人求和
	
	List history = new ArrayList(64);		// 历史记录，保存int[2]
	List history2 = new ArrayList(64);		// 保存string
	
	long lastMoveTime;
	
	public String getChessName(int i) {
		return chessNames[i];
	}
	
	public ChessBean() {
		reset();
	}
	public ChessBean(int id) {
		this.id = id;
		reset();
	}

	public void reset() {
		board = (int[])defBoard.clone();
		isBind = false;
		moveCount = 0;
		userId1 = userId2 = 0;
		startTime = 0;
		status = STATUS_READY;
		winSide = 0;
		redGeneralPosition = 4;
		blackGeneralPosition = 85;
		history.clear();
		history2.clear();
		lastMoveTime = System.currentTimeMillis();
	}
	// 选择一个子
	public String getBoardString(int userId, HttpServletResponse response) {
		int side = getUserSide(userId);
		int turn = getTurnSide();
		if(turn != side || isStatusEnd())	// 不是轮到自己下棋
			side = -1;
		StringBuilder sb = new StringBuilder(256);
		boolean USD = (userId == userId1 && userId != userId2);	// 上下颠倒
		int ch = 0;
		for(int i = 0;i < HEIGHT;i++) {
			for(int j = 0;j < WIDTH;j++) {
				int ch2 = USD ? GRID - 1 - ch : ch;
				int chess = board[ch2];
				if(chess == 0) {
					sb.append(backBoard[ch]);
				} else if(chess / 10 == side ) {
					sb.append("<a href=\"");
					sb.append(("v.jsp?p=" + ch2));
					sb.append("\">");
					sb.append(chessNames[chess]);
					sb.append("</a>");
				} else if(chess / 10 == turn) {	// 没有轮到的人
					sb.append("<a href=\"#c\">");
					sb.append(chessNames[chess]);
					sb.append("</a>");
				} else {
					sb.append(chessNames[chess]);
				}
				ch++;
			}
			sb.append("<br/>");
		}
		return sb.toString();
	}
	// 选择一个子到达的位置
	public String getBoardString2(int userId, int move, HttpServletResponse response) {
		int side = getUserSide(userId);
		
		boolean[] moves = calcMoves(move);	// 可移动位置的棋盘
		
		StringBuilder sb = new StringBuilder(256);
		boolean USD = (userId == userId1 && userId != userId2);	// 上下颠倒
		int ch = 0;
		for(int i = 0;i < HEIGHT;i++) {
			for(int j = 0;j < WIDTH;j++) {
				int ch2 = USD ? GRID - 1 - ch : ch;
				int chess = board[ch2];
				if(moves[ch2]) {		// 可以移动到这里
					sb.append("<a href=\"");
					sb.append(("g.jsp?p=" + ch2 + "&amp;f=" + move));
					sb.append("\">");
					sb.append(chess != 0 ? chessNames[chess] : backBoard[ch]);
					sb.append("</a>");
				} else if(chess == 0) {
					sb.append(backBoard[ch]);
				} else if(chess / 10 == side) {
					sb.append("<a href=\"");
					sb.append(("v.jsp?p=" + ch2));
					sb.append("\">");
					sb.append(chessNames[chess]);
					sb.append("</a>");
				} else {
					sb.append(chessNames[chess]);
				}
				ch++;
			}
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	//TODO 是否能从一个位置移动到另一个
	public boolean canMove(int startPos, int endPos) {
		int moveChess = board[startPos] % 10;	// 移动的棋子类型
		
		int x1 = startPos % WIDTH;
		int y1 = startPos / WIDTH;
		int x2 = endPos % WIDTH;
		int y2 = endPos / WIDTH;
		
		
		switch(moveChess) {
		case 1:	//车
			int count2 = 0;
			if(x1 == x2) {
				if(y1 < y2) {
					for(int i = y1; i < y2; i++) {
						if(board[i * WIDTH + x1] != 0) {
							count2++;
							if(count2 > 1)
								return false;
						}
					}
				} else {
					for(int i = y2; i < y1; i++) {
						if(board[i * WIDTH + x1] != 0) {
							count2++;
							if(count2 > 1)
								return false;
						}
					}
				}
				return true;
			}
			if(y1 == y2) {
				if(startPos < endPos) {
					for(int i = startPos; i < endPos; i++) {
						if(board[i] != 0) {
							count2++;
							if(count2 > 1)
								return false;
						}
					}
				} else {
					for(int i = endPos; i < startPos; i++) {
						if(board[i] != 0) {
							count2++;
							if(count2 > 1)
								return false;
						}
					}
				}
				return true;
			}
			break;
		case 2:	//马
			if(x1 + 1 < WIDTH && y1 + 2 < HEIGHT && board[startPos + WIDTH] == 0)
				if(endPos == startPos + WIDTH + WIDTH + 1)
					return true;
			if(x1 > 0 && y1 + 2 < HEIGHT && board[startPos + WIDTH] == 0)
				if(endPos == startPos + WIDTH + WIDTH - 1)
					return true;
			if(x1 + 1 < WIDTH && y1 > 1 && board[startPos - WIDTH] == 0)
				if(startPos - WIDTH - WIDTH + 1 == endPos)
					return true;
			if(x1 > 0 && y1 > 1 && board[startPos - WIDTH] == 0)
				if(startPos - WIDTH - WIDTH - 1 == endPos)
					return true;			
			if(x1 + 2 < WIDTH && y1 + 1 < HEIGHT && board[startPos + 1] == 0)
				if(startPos + WIDTH + 2 == endPos)
					return true;
			if(x1 > 1 && y1 + 1 < HEIGHT && board[startPos - 1] == 0)
				if(startPos + WIDTH - 2 == endPos)
					return true;
			if(x1 + 2 < WIDTH && y1 > 0 && board[startPos + 1] == 0)
				if(startPos - WIDTH + 2 == endPos)
					return true;
			if(x1 > 1 && y1 > 0 && board[startPos - 1] == 0)
				if(startPos - WIDTH - 2 == endPos)
					return true;
			break;
		case 3:	//炮
			int count = 0;
			if(x1 == x2) {
				if(y1 < y2) {
					for(int i = y1 + 1; i < y2; i++) {
						if(board[i * WIDTH + x1] != 0){
							count++;
							if(count >= 2) {
								return false;
							}
						}
					}
				} else {
					for(int i = y2 + 1; i < y1; i++) {
						if(board[i * WIDTH + x1] != 0){
							count++;
							if(count >= 2) {
								return false;
							}
						}
					}
				}
			}
				
			if(y1 == y2)
				if(startPos < endPos) {
					for(int i = startPos + 1; i < endPos; i++) {
						if(board[i] != 0){
							count++;
							if(count >= 2) {
								return false;
							}
						}
					}
				} else {
					for(int i = endPos + 1; i < startPos; i++) {
						if(board[i] != 0) {
							count++;
							if(count >= 2) {
								return false;
							}
						}
					}
				}
			if(count == 1) {
				return true;
			}
			break;
		case 7:
			if(startPos - 1 == endPos || startPos + 1 == endPos) {
				return true;
			}else if(board[startPos] == 17 && startPos < endPos && startPos + WIDTH == endPos) {
				return true;
			}if(board[startPos] == 27 && startPos > endPos && startPos - WIDTH == endPos) {
				return true;
			}
			break;
		}
		return false;
	}

	public boolean[] calcMoves(int pos) {
		int moveChess = board[pos] % 10;	// 移动的棋子类型
		int side = board[pos] / 10;
		int x = pos % WIDTH;
		int y = pos / WIDTH;
		
		boolean moveFlag = canMoveAway(x, y);
		
		boolean[] moves = new boolean[WIDTH * HEIGHT];
		switch(moveChess) {
		case 1: {		// 车
			int start = pos;
			
			// added by leihy start
			if(moveFlag){ 
			//add by leihy end
			
				for(int i = 1;i <= x;i++) {
					start--;
					if(board[start] == 0)
						moves[start] = true;
					else {
						if(board[start] / 10 != side)
							moves[start] = true;
						break;
					}
				}
				start = pos;
				for(int i = 1;i < WIDTH - x;i++) {
					start++;
					if(board[start] == 0)
						moves[start] = true;
					else {
						if(board[start] / 10 != side)
							moves[start] = true;
						break;
					}
				}
			// added by leihy start	
			}
			//add by leihy end
			
			start = pos;
			for(int i = 1;i <= y;i++) {
				start -= WIDTH;
				if(board[start] == 0)
					moves[start] = true;
				else {
					if(board[start] / 10 != side)
						moves[start] = true;
					break;
				}
			}
			start = pos;
			for(int i = 1;i < HEIGHT - y;i++) {
				start += WIDTH;
				if(board[start] == 0)
					moves[start] = true;
				else {
					if(board[start] / 10 != side)
						moves[start] = true;
					break;
				}
			}
		} break;
		case 2: {		// 马
			
			if(moveFlag) {
				if(x + 1 < WIDTH && y + 2 < HEIGHT && board[pos + WIDTH] == 0 && (board[pos + WIDTH + WIDTH + 1] / 10 != side))
					moves[pos + WIDTH + WIDTH + 1] = true;
				if(x > 0 && y + 2 < HEIGHT && board[pos + WIDTH] == 0 && (board[pos + WIDTH + WIDTH - 1] / 10 != side))
					moves[pos + WIDTH + WIDTH - 1] = true;
				if(x + 1 < WIDTH && y > 1 && board[pos - WIDTH] == 0 && (board[pos - WIDTH - WIDTH + 1] / 10 != side))
					moves[pos - WIDTH - WIDTH + 1] = true;
				if(x > 0 && y > 1 && board[pos - WIDTH] == 0 && (board[pos - WIDTH - WIDTH - 1] / 10 != side))
					moves[pos - WIDTH - WIDTH - 1] = true;
				
				if(x + 2 < WIDTH && y + 1 < HEIGHT && board[pos + 1] == 0 && (board[pos + WIDTH + 2] / 10 != side))
					moves[pos + WIDTH + 2] = true;
				if(x > 1 && y + 1 < HEIGHT && board[pos - 1] == 0 && (board[pos + WIDTH - 2] / 10 != side))
					moves[pos + WIDTH - 2] = true;
				if(x + 2 < WIDTH && y > 0 && board[pos + 1] == 0 && (board[pos - WIDTH + 2] / 10 != side))
					moves[pos - WIDTH + 2] = true;
				if(x > 1 && y > 0 && board[pos - 1] == 0 && (board[pos - WIDTH - 2] / 10 != side))
					moves[pos - WIDTH - 2] = true;
			}
		} break;
		case 3: {		// 炮
			int start = pos;
			
			if(moveFlag) {
				for(int i = 1;i <= x;i++) {
					start--;
					if(board[start] == 0)
						moves[start] = true;
					else {
						for(i++;i <= x;i++) {
							start--;
							if(board[start] != 0) {
								if(board[start] / 10 != side)
									moves[start] = true;
								break;
							}
						}
						break;
					}
				}
				start = pos;
				for(int i = 1;i < WIDTH - x;i++) {
					start++;
					if(board[start] == 0)
						moves[start] = true;
					else {
						for(i++;i < WIDTH - x;i++) {
							start++;
							if(board[start] != 0) {
								if(board[start] / 10 != side)
									moves[start] = true;
								break;
							}
						}
						break;
					}
				}
			}
			
			start = pos;
			for(int i = 1;i <= y;i++) {
				start -= WIDTH;
				if(board[start] == 0)
					moves[start] = true;
				else {
					if(moveFlag) {
						for(i++;i <= y;i++) {
							start -= WIDTH;
							if(board[start] != 0) {
								if(board[start] / 10 != side)
									moves[start] = true;
								break;
							}
						}
					}
					break;
				}
			}
			start = pos;
			for(int i = 1;i < HEIGHT - y;i++) {
				start += WIDTH;
				if(board[start] == 0)
					moves[start] = true;
				else {
					if(moveFlag) {
						for(i++;i < HEIGHT - y;i++) {
							start += WIDTH;
							if(board[start] != 0) {
								if(board[start] / 10 != side)
									moves[start] = true;
								break;
							}
						}
					}
					break;
				}
			}
		} break;
		case 4: {		// 相
			
			if(moveFlag) {
			
				if(x + 1 < WIDTH && y + 2 < HEIGHT && board[pos + WIDTH + 1] == 0 && board[pos + WIDTH + WIDTH + 2] / 10 != side)
					if(side == 2 || y < 4) 
						moves[pos + WIDTH + WIDTH + 2] = true;
				if(x > 0 && y + 2 < HEIGHT && board[pos + WIDTH - 1] == 0 && board[pos + WIDTH + WIDTH - 2] / 10 != side)
					if(side == 2 || y < 4)
						moves[pos + WIDTH + WIDTH - 2] = true;
				if(x + 1 < WIDTH && y > 1 && board[pos - WIDTH + 1] == 0 && board[pos - WIDTH - WIDTH + 2] / 10 != side)
					if(side == 1 || y > 5)
						moves[pos - WIDTH - WIDTH + 2] = true;
				if(x > 0 && y > 1 && board[pos - WIDTH - 1] == 0 && board[pos - WIDTH - WIDTH - 2] / 10 != side)
					if(side == 1 || y > 5)
						moves[pos - WIDTH - WIDTH - 2] = true;
				
			}
		} break;
		case 5: {		// 士
			
			if(moveFlag) {
			
				if(x + 4 < WIDTH && y + 1 < HEIGHT && board[pos + WIDTH + 1] / 10 != side)
					if(side == 1 && x < 5 && y < 2) {
						moves[pos + WIDTH + 1] = true;
					} else if(side == 2 && x < 5 && y > 6) {
						moves[pos + WIDTH + 1] = true;
					}
				if(x > 3 && y + 1 < HEIGHT && board[pos + WIDTH - 1] / 10 != side)
					if(side == 1 && x > 3 && y < 2) {
						moves[pos + WIDTH - 1] = true;
					} else if(side == 2 && x > 3 && y > 6) {
						moves[pos + WIDTH - 1] = true;
					}
				if(x + 4 < WIDTH && y > 0 && board[pos - WIDTH + 1] / 10 != side)
					if(side == 1 && x > 2 && y < 3) {
						moves[pos - WIDTH + 1] = true;
					} else if(side == 2 && x > 2 && y > 7) {
						moves[pos - WIDTH + 1] = true;
					}
				if(x > 3 && y > 0 && board[pos - WIDTH - 1] / 10 != side)
					if(side == 1 && x > 3 && y < 3) {
						moves[pos - WIDTH - 1] = true;
					} else if(side == 2 && x > 3 && y > 7) {
						moves[pos - WIDTH - 1] = true;
					}
			}
		} break;
		case 6: {		// 将
			//if(x + 1 < WIDTH && board[pos + 1] / 10 != side)
			if(x < 5 && board[pos + 1] / 10 != side){
				moves[pos + 1] = true;
				//下面为判断帅和将是否是在一条直线上
				if(side == 1) {
					int tmp = x + 1 + y * WIDTH;
					for(int i = y+1;i < HEIGHT; i++) {
						tmp += WIDTH;
						if(board[tmp] != 0) {
							if(board[tmp] != 26)
								break;
							else
								moves[pos + 1] = false;
						}
					}
				}
				if(side == 2) {
					int tmp = x + 1 + y * WIDTH;
					for(int i = y - 1;i >= 0; i--) {
						tmp -= WIDTH;
						if(board[tmp] != 0) {
							if(board[tmp] != 16)
								break;
							else
								moves[pos + 1] = false;
						}
					}
				}
			}
				
			//if(x > 0 && board[pos - 1] / 10 != side)
			if(x > 3 && board[pos - 1] / 10 != side) {
				moves[pos - 1] = true;
				//下面为判断帅和将是否是在一条直线上
				if(side == 1) {
					int tmp = x - 1 + y * WIDTH;
					for(int i = y+1;i < HEIGHT; i++) {
						tmp += WIDTH;
						if(board[tmp] != 0) {
							if(board[tmp] != 26)
								break;
							else
								moves[pos - 1] = false;
						}
					}
				}
				if(side == 2) {
					int tmp = x - 1 + y * WIDTH;
					for(int i = y - 1;i >= 0; i--) {
						tmp -= WIDTH;
						if(board[tmp] != 0) {
							if(board[tmp] != 16)
								break;
							else
								moves[pos - 1] = false;
						}
					}
				}
			}
				
			if(y + 1 < HEIGHT && board[pos + WIDTH] / 10 != side) {
				int redX = redGeneralPosition % WIDTH;
				int blackX = blackGeneralPosition % WIDTH;
				if(redX == blackX) {
					int redY = redGeneralPosition / WIDTH;
					int blackY = blackGeneralPosition / WIDTH;
					if(y < 2) {
						if(board[pos + WIDTH] != 0) {
							for(int i = redY + 2;i < blackY ;i++) {
								if(board[i * WIDTH + x] != 0) {
									moves[pos + WIDTH] = true;
									break;
								}
							}
						} else {
							moves[pos + WIDTH] = true;
						}
					} else if(side == 2) {
						moves[pos + WIDTH] = true;
					}
				}else if(side == 2 || y < 2) {
					moves[pos + WIDTH] = true;
				}
			}
			if(y > 0 && board[pos - WIDTH] / 10 != side) {
				int redX = redGeneralPosition % WIDTH;
				int blackX = blackGeneralPosition % WIDTH;
				if(redX == blackX) {
					int redY = redGeneralPosition / WIDTH;
					int blackY = blackGeneralPosition / WIDTH;
					if(y > 7) {
						if(board[pos - WIDTH] != 0) {
							for(int i = redY + 1;i < blackY - 1;i++) {
								if(board[i * WIDTH + x] != 0) {
									moves[pos - WIDTH] = true;
									break;
								}
							}
						} else {
							moves[pos - WIDTH] = true;
						}
					} else if(side == 1) {
						moves[pos - WIDTH] = true;
					}
				}else if(side == 1 || y > 7) {
					moves[pos - WIDTH] = true;
				}					
			}
				
		} break;
		case 7: {		// 兵
			if(side == 1) {
				if(y + 1 < HEIGHT && board[pos + WIDTH] / 10 != side)
					moves[pos + WIDTH] = true;
				if(y > 4) {
					if(moveFlag) {
						if(x + 1 < WIDTH && board[pos + 1] / 10 != side)
							moves[pos + 1] = true;
						if(x > 0 && board[pos - 1] / 10 != side)
							moves[pos - 1] = true;
					}
				}
			} else {
				if(y > 0 && board[pos - WIDTH] / 10 != side)
					moves[pos - WIDTH] = true;
				if(y <= 4) {
					if(moveFlag) {
						if(x + 1 < WIDTH && board[pos + 1] / 10 != side)
							moves[pos + 1] = true;
						if(x > 0 && board[pos - 1] / 10 != side)
							moves[pos - 1] = true;
					}
				}
			}

		} break;
		}
		return moves;
	}
	// 检查并移动一个棋子
	public void move(int from, int pos) {
		if(from < 0 || from >= GRID || pos < 0 || pos >= GRID)
			return;
		
		//检测是否移动了将或者帅并且记录将或帅的位置 added by leihy=>start
		int moveChess = board[from] % 10;
		int side = board[from] / 10;
		if(moveChess == 6) {
			if(side == 1) {
				redGeneralPosition = pos;
			} else if(side == 2) {
				blackGeneralPosition = pos;
			}
		}
		//检测是否移动了将或者帅并且记录将或帅的位置 added by leihy=>end
		
		boolean[] moves = calcMoves(from);
		if(!moves[pos])		// 不可移动
			return;
		if(board[pos] != 0) {		// 吃子
			int kill = board[pos] % 10;
			if(kill == 6) {		// 吃了将
				end(side);
			}
		}
		int[] his = {from, pos};
		history.add(his);
		history2.add(getHistoryString(from, pos));

		board[pos] = board[from];
		board[from] = 0;
		moveCount++;
		lastMoveTime = System.currentTimeMillis();
		
		
		
		//TODO 加入判断是否将军 start
		//if(moveChess != 4 && moveChess != 5 && moveChess != 6) {
			for(int i = 0; i < board.length; i++) {
				if(board[i] != 0 && board[i] / 10 == side) {
					if(side == 1) {
						isBind = canMove(i, blackGeneralPosition);
						if(isBind)
							break;
					} else if(side == 2) {
						isBind = canMove(i, redGeneralPosition);
						if(isBind)
							break;
					}
				}
			}
		//}
		
		
	}
	// 载入记录后的移动，不判断正确性
	private void innerMove(int from, int pos) {
		int side = board[from] / 10;
		if(board[from] % 10 == 6) {
			if(side == 1) {
				redGeneralPosition = pos;
			} else if(side == 2) {
				blackGeneralPosition = pos;
			}
		}
		
		history2.add(getHistoryString(from, pos));
		board[pos] = board[from];
		board[from] = 0;
	}
	// 本局结束，side方胜，如果side为负数，side的对方胜，side = 3表示和局
	public void end(int side) {
		if(side == 0)
			return;
		if(side > 0)
			winSide = side;
		else if(side < 0)
			winSide = 3 + side;
		status = STATUS_END;
		endTime = System.currentTimeMillis();
		
		SqlUtil.executeUpdate("update mcoolgame.chess set win_side=" + 
				winSide + ",status=" + status + ",end_time=now() where id=" + id, 4);
		
		ChessAction.endChess(this);
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isStatusReady() {
		return status == STATUS_READY;
	}
	public boolean isStatusEnd() {
		return status == STATUS_END;
	}
	public boolean isStatusPlay() {
		return status == STATUS_PLAY;
	}
	public int getUserSide(int userId) {	// 判断该玩家是本局的哪方，1为红方，2为黑方，-1为旁观
		if(userId1 == userId) {
			if(userId1 == userId2)	// 如果双方是同一人，返回当前方
				return getTurnSide();
			return 1;
		}
		if(userId2 == userId)
			return 2;
		return -1;
	}

	public int getUserId1() {
		return userId1;
	}

	public void setUserId1(int userId1) {
		this.userId1 = userId1;
	}

	public int getUserId2() {
		return userId2;
	}

	public void setUserId2(int userId2) {
		this.userId2 = userId2;
	}
	// 是轮到哪边下
	public int getTurnSide() {
		return moveCount % 2 + 1;
	}
	// 轮到哪个玩家下
	public int getTurnUser() {
		if(moveCount % 2 == 0)
			return userId1;
		else
			return userId2;
	}
	
	public String getStatusString() {
		if(status == STATUS_PLAY)
			return "第" + getCurrentTurnCount() + "回合";
		if(status == STATUS_END)
			return "共" + getTurnCount() + "回合";
		return statusNames[status];
	}
	
	//判断是否因为将和帅在同一位置而不允许其中的棋子移动
	public boolean canMoveAway(int x, int y) {
		int redX = redGeneralPosition % WIDTH;
		int blackX = blackGeneralPosition % WIDTH;
		if(x != redX || x != blackX)
			return true;
		int redY = redGeneralPosition / WIDTH;
		int blackY = blackGeneralPosition / WIDTH;
		if(y < redY || y > blackY)
			return true;
		int count = 0;
		
		for(int start = redGeneralPosition + WIDTH; start < blackGeneralPosition; start += WIDTH) {
			if(board[start] != 0) {
				count++;
			}
		}
		if(count == 1) {
			return false;
		}
		return true;
	}

	public List getHistory() {
		return history;
	}
	public void setHistory(List history) {
		this.history = history;
	}
	public List getHistory2() {
		return history2;
	}
	public int getMoveCount() {
		return moveCount;
	}
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	// 返回总回合数
	public int getTurnCount() {
		return (moveCount + 1) / 2;
	}
	// 返回当前回合数
	public int getCurrentTurnCount() {
		return moveCount / 2 + 1;
	}
	public void setHistory2(List history2) {
		this.history2 = history2;
	}
	public String getTurnString(int count) {
		int mc = (count - 1) * 2;
		if(mc >= moveCount)
			return "";
		StringBuilder sb = new StringBuilder(16);
		sb.append(count);
		sb.append('.');
		sb.append(history2.get(mc));
		if(mc + 1 < moveCount) {
			sb.append(',');
			sb.append(history2.get(mc + 1));
		}
		return sb.toString();
	}
	
	public String getWinString(int side) {
		if(this.isStatusEnd()) {
			if(winSide == 3) {	// 平局
				return "(平)";
			} else if(winSide == side) {
				return "(胜)";
			} else {
				return "(负)";
			}
		}	
		return "";
	}
	// 历史记录＋中文记谱
	public String getHistoryString(int from , int pos) {
		String chessName = chessNames[board[from]];
		int side = board[from] / 10;
		int fromX = from % WIDTH;
		int fromY = from / WIDTH;
		int posX = pos % WIDTH;
		int posY = pos / WIDTH;

		int[] countArray = new int[5];
		int count = 0;
		for (int position = fromX; position < GRID; position += WIDTH) {
			if (board[from] == board[position]) {
				countArray[count] = position;
				count++;
			}
		}
		StringBuilder sb = new StringBuilder();

		if (count == 1) {
			sb.append(chessName);
			if (side == 1)
				sb.append(numbers[fromX + 1]);
			else
				sb.append(WIDTH - fromX);
		} else if (count == 2) {
			if (countArray[0] == from) {
				if(side == 1) {
					sb.append("后");
				} else {
					sb.append("前");
				}
			} else {
				if(side == 1) {
					sb.append("前");
				} else {
					sb.append("后");
				}
			}
			sb.append(chessName);
		} else if (count == 3) {
			if (countArray[0] == from) {
				if (side == 1) {
					sb.append("后");
				} else {
					sb.append("前");
				}
			} else if (countArray[1] == from) {
				sb.append("中");
			} else {
				if (side == 1) {
					sb.append("前");
				} else {
					sb.append("后");
				}
			}
			sb.append(chessName);
		} else if (count == 4) {
			if (countArray[0] == from) {
				if (side == 1) {
					sb.append("后");
				} else {
					sb.append("前");
				}
			} else if (countArray[1] == from) {
				if (side == 1) {
					sb.append("中后");
				} else {
					sb.append("中前");
				}
			} else if (countArray[2] == from) {
				if (side == 1) {
					sb.append("中前");
				} else {
					sb.append("中后");
				}
			} else {
				if (side == 1) {
					sb.append("前");
				} else {
					sb.append("后");
				}
			}
			sb.append(chessName);
		} else if (count == 5) {
			if (countArray[0] == from) {
				if (side == 1) {
					sb.append("后");
				} else {
					sb.append("前");
				}
			} else if (countArray[1] == from) {
				if (side == 1) {
					sb.append("中后");
				} else {
					sb.append("中前");
				}
			} else if (countArray[2] == from) {
				sb.append("中");
			} else if (countArray[3] == from) {
				if (side == 1) {
					sb.append("中前");
				} else {
					sb.append("中后");
				}
			} else {
				if (side == 1) {
					sb.append("前");
				} else {
					sb.append("后");
				}
			}
			sb.append(chessName);
		}

		if (fromY != posY) {
			int action = posY - fromY;
			if (fromX == posX) {
				if (action < 0) {
					if (side == 1) {
						sb.append("退");
						sb.append(numbers[-action]);
					} else {
						sb.append("进");
						sb.append(-action);
					}

				} else if (action > 0) {
					if (side == 1) {
						sb.append("进");
						sb.append(numbers[action]);
					} else {
						sb.append("退");
						sb.append(action);
					}

				}
			} else {
				if (action < 0) {
					if (side == 1) {
						sb.append("退");
						sb.append(numbers[posX + 1]);
					} else {
						sb.append("进");
						sb.append(WIDTH - posX);
					}
				} else if (action > 0) {
					if (side == 1) {
						sb.append("进");
						sb.append(numbers[posX + 1]);
					} else {
						sb.append("退");
						sb.append(WIDTH - posX);
					}
				}
			}

		} else {
			sb.append("平");
			if (side == 1)
				sb.append(numbers[posX + 1]);
			else
				sb.append(WIDTH - posX);
		}
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public int getTimeLeft() {
		int elapsed = (int) ((System.currentTimeMillis() - lastMoveTime) / 1000);
		if(elapsed >= interupt)
			return 0;
		else
			return interupt - elapsed;
	}
	// 从数据库载入后需要执行，保持棋盘在服务器重启后不变
	public void load() {
		Iterator iterator = history.iterator();
		while(iterator.hasNext()) {
			int[] record = (int[])(iterator.next());
			innerMove(record[0], record[1]);
		}
		moveCount = history.size();
		lastMoveTime = System.currentTimeMillis() + 60000;	// 多等待1分钟
	}
	// 返回history2全部用-连接
	public String concatHistory2() {
		StringBuilder sb = new StringBuilder(moveCount * 5);
		
		sb.append(history2.get(0));
		for(int i = 1;i < moveCount;i++) {
			sb.append('-');
			sb.append(history2.get(i));
		}
		
		return sb.toString();
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(long lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	public int getWinSide() {
		return winSide;
	}
	public int getWinUser() {
		if(winSide == 1)
			return userId1;
		else
			return userId2;
	}

	public void setWinSide(int winSide) {
		this.winSide = winSide;
	}

	public int getDrawSide() {
		return drawSide;
	}

	public void setDrawSide(int drawSide) {
		this.drawSide = drawSide;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
