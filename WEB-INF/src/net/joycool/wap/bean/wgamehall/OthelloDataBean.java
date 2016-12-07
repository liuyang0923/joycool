/*
 * Created on 2006-3-9
 *
 */
package net.joycool.wap.bean.wgamehall;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.joycool.wap.action.wgamehall.OthelloAction;


/**
 * @author lbj
 *  
 */
public class OthelloDataBean {
	public static int GS_INPLAY = 0;
	public static int GS_BLACKWIN = 1;
	public static int GS_WHITEWIN = 2;
	public static int GS_DRAW = 3;
	
    public static int ACTION_CONTINUE = 0;

    public static int ACTION_GIVE_UP = 1;

    public static int ACTION_SUE_FOR_PEACE = 2;
    
    public static int ACTION_DENY_PEACE = 6;

    public static int ACTION_INVITE = 3;

    public static int ACTION_ACCEPT_INVITATION = 4;

    public static int ACTION_DENY_INVITATION = 5;

    long lastActiveTime;

    int lastActiveUserId;

    String uniqueMark;

    int actionType;

    BufferedImage image;
    
    int sueForPeaceUserId1;
    
    int sueForPeaceUserId2;
    
    int moveCount;

    /**
     * @return Returns the moveCount.
     */
    public int getMoveCount() {
        return moveCount;
    }
    /**
     * @param moveCount The moveCount to set.
     */
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }
    /**
     * @return Returns the sueForPeaceUserId1.
     */
    public int getSueForPeaceUserId1() {
        return sueForPeaceUserId1;
    }
    /**
     * @param sueForPeaceUserId1 The sueForPeaceUserId1 to set.
     */
    public void setSueForPeaceUserId1(int sueForPeaceUserId1) {
        this.sueForPeaceUserId1 = sueForPeaceUserId1;
    }
    /**
     * @return Returns the sueForPeaceUserId2.
     */
    public int getSueForPeaceUserId2() {
        return sueForPeaceUserId2;
    }
    /**
     * @param sueForPeaceUserId2 The sueForPeaceUserId2 to set.
     */
    public void setSueForPeaceUserId2(int sueForPeaceUserId2) {
        this.sueForPeaceUserId2 = sueForPeaceUserId2;
    }
    /**
     * @return Returns the image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image
     *            The image to set.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return Returns the actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType
     *            The actionType to set.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }


    /**
     * @return Returns the uniqueMark.
     */
    public String getUniqueMark() {
        return uniqueMark;
    }

    /**
     * @param uniqueMark
     *            The uniqueMark to set.
     */
    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    /**
     * @return Returns the lastActiveTime.
     */
    public long getLastActiveTime() {
        return lastActiveTime;
    }

    /**
     * @param lastActiveTime
     *            The lastActiveTime to set.
     */
    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    /**
     * @return Returns the lastActiveUserId.
     */
    public int getLastActiveUserId() {
        return lastActiveUserId;
    }

    /**
     * @param lastActiveUserId
     *            The lastActiveUserId to set.
     */
    public void setLastActiveUserId(int lastActiveUserId) {
        this.lastActiveUserId = lastActiveUserId;
    }


	public OthelloDataBean(){
		board = new int[8][8];
		reset();
	}

	final static int BLACK = -1;

	final static int WHITE = 1;

	private int colorStatus;

	private int[][] board = null;

	private int chessNum;

	private int whiteCount = 0;

	private int blackCount = 0;

	private int gameStatus;

	public int[][] getBoard() {
		return board;
	}

	public int getStatus() {
		return gameStatus;
	}
	
	public int getTurn()		// 返回当前的玩家（BLACK or WHITE) 
	{
		return colorStatus;
	}

	public void reset() {
		gameStatus = GS_INPLAY;
		chessNum = 4;
		colorStatus = BLACK;

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				board[i][j] = 0;
		
		board[3][3] = BLACK;
		board[4][4] = BLACK;
		board[3][4] = WHITE;
		board[4][3] = WHITE;
	}

	public boolean addMove(int x, int y, int side) {
		if (check(x, y, colorStatus) > 0) {

			manulChess(x, y, side);

			if (isPass()) {
				if (isPass()) {
					blOver();
				}
			}
			return true;
		}
		return false;
	}

	private boolean change(int i, int j, int sta) {
		if(board[i][j] != 0)
			return false;
		boolean flag = false;
		board[i][j] = colorStatus;
		//north
		if (j != 0) {
			if (-sta == board[i][j - 1]) {
				int m = j - 1;
				while (-sta == board[i][m] && m > 0) {
					m--;
				}
				if (board[i][m] == sta) {
					flag = true;
					for (int n = j - 1; n > m; n--) {
						board[i][n] = sta;
					}
				}
			}
		}

		//south
		if (j != 7) {
			if (-sta == board[i][j + 1]) {
				int m = j + 1;
				while (-sta == board[i][m] && m < 7) {
					m++;
				}
				if (board[i][m] == sta) {
					flag = true;
					for (int n = j + 1; n < m; n++) {
						board[i][n] = sta;
					}
				}

			}
		}

		//west
		if (i != 0) {
			if (-sta == board[i - 1][j]) {
				int m = i - 1;
				while (-sta == board[m][j] && m > 0) {
					m--;
				}
				if (board[m][j] == sta) {
					flag = true;
					for (int n = i - 1; n > m; n--) {
						board[n][j] = sta;
					}
				}
			}
		}

		//east
		if (i != 7) {
			if (-sta == board[i + 1][j]) {
				int m = i + 1;
				while (-sta == board[m][j] && m < 7) {
					m++;
				}
				if (board[m][j] == sta) {
					flag = true;
					for (int n = i + 1; n < m; n++) {
						board[n][j] = sta;
					}
				}

			}
		}

		//northwest
		if (j != 0 && i != 0) {
			if (-sta == board[i - 1][j - 1]) {
				int m = i - 1;
				int n = j - 1;
				while (-sta == board[m][n] && m > 0 && n > 0) {
					m--;
					n--;
				}
				if (board[m][n] == sta) {
					flag = true;
					for (int x = i - 1, y = j - 1; x > m; x--, y--) {
						board[x][y] = sta;

					}
				}
			}
		}

		//southeast
		if (j != 7 && i != 7) {
			if (-sta == board[i + 1][j + 1]) {
				int m = i + 1;
				int n = j + 1;
				while (-sta == board[m][n] && m < 7 && n < 7) {
					m++;
					n++;
				}
				if (board[m][n] == sta) {
					flag = true;
					for (int x = i + 1, y = j + 1; x < m; x++, y++) {
						board[x][y] = sta;
					}
				}
			}
		}

		//northeast
		if (j != 0 && i != 7) {
			if (-sta == board[i + 1][j - 1]) {
				int m = i + 1;
				int n = j - 1;
				while (-sta == board[m][n] && m < 7 && n > 0) {
					m++;
					n--;
				}
				if (board[m][n] == sta) {
					flag = true;
					for (int x = i + 1, y = j - 1; x < m; x++, y--) {
						board[x][y] = sta;
					}
				}
			}
		}

		//southwest
		if (j != 7 && i != 0) {
			if (-sta == board[i - 1][j + 1]) {
				int m = i - 1;
				int n = j + 1;
				while (-sta == board[m][n] && m > 0 && n < 7) {
					m--;
					n++;
				}
				if (board[m][n] == sta) {
					flag = true;

					for (int x = i - 1, y = j + 1; x > m; x--, y++) {
						board[x][y] = sta;
					}
				}
			}
		}
		if(!flag)
			board[i][j] = 0;
		return flag;

	}

	private int check(int i, int j, int sta) {

		int flag = 0;
		if (board[i][j] != 0) {
			return 0;
		}

		//north
		if (j != 0) {
			int m = j - 1;
			if (-sta == board[i][m]) {
				while (-sta == board[i][m] && m > 0) {
					m--;
				}
				if (board[i][m] == sta) {
					flag = flag + (j - 1 - m);
				}
			}
		}

		//south
		if (j != 7) {
			int m = j + 1;
			if (-sta == board[i][m]) {
				while (-sta == board[i][m] && m < 7) {
					m++;
				}
				if (board[i][m] == sta) {
					flag = flag + (m - j - 1);
				}

			}
		}

		//west
		if (i != 0) {
			if (-sta == board[i - 1][j]) {
				int m = i - 1;
				while (-sta == board[m][j] && m > 0) {
					m--;
				}
				if (board[m][j] == sta) {
					flag = flag + (i - 1 - m);

				}
			}
		}

		//east
		if (i != 7) {
			if (-sta == board[i + 1][j]) {
				int m = i + 1;
				while (-sta == board[m][j] && m < 7) {
					m++;
				}
				if (board[m][j] == sta) {
					flag = flag + (m - i - 1);

				}

			}
		}

		//northwest
		if (j != 0 && i != 0) {
			if (-sta == board[i - 1][j - 1]) {
				int m = i - 1;
				int n = j - 1;
				while (-sta == board[m][n] && m > 0 && n > 0) {
					m--;
					n--;
				}
				if (board[m][n] == sta) {
					flag = flag + (i - 1 - m);

				}
			}
		}

		//southeast
		if (j != 7 && i != 7) {
			if (-sta == board[i + 1][j + 1]) {
				int m = i + 1;
				int n = j + 1;
				while (-sta == board[m][n] && m < 7 && n < 7) {
					m++;
					n++;
				}
				if (board[m][n] == sta) {
					flag = flag + (m - i - 1);

				}
			}
		}

		//northeast
		if (j != 0 && i != 7) {
			if (-sta == board[i + 1][j - 1]) {
				int m = i + 1;
				int n = j - 1;
				while (-sta == board[m][n] && m < 7 && n > 0) {
					m++;
					n--;
				}
				if (board[m][n] == sta) {
					flag = flag + (j - 1 - n);

				}
			}
		}

		//southwest
		if (j != 7 && i != 0) {
			if (-sta == board[i - 1][j + 1]) {
				int m = i - 1;
				int n = j + 1;
				while (-sta == board[m][n] && m > 0 && n < 7) {
					m--;
					n++;
				}
				if (board[m][n] == sta) {
					flag = flag + (i - 1 - m);

				}
			}
		}
		return flag;

	}

	private boolean judge(int color) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (check(i, j, color) > 0)
					return true;

		return false;
	}

	private void blOver() {
		whiteCount = 0;
		blackCount = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == BLACK)
					blackCount++;
				else if (board[i][j] == WHITE)
					whiteCount++;

			}
		}

		if (whiteCount > blackCount) 
			gameStatus = GS_WHITEWIN;
		else if(whiteCount < blackCount)
			gameStatus = GS_BLACKWIN;
		else
			gameStatus = GS_DRAW;
			
	}

	private boolean isPass() {
		if (chessNum >= 64) {
			return false;
		}
		if (!judge(colorStatus)) {
			colorStatus = -colorStatus;
			return true;
		} else {
			return false;
		}
	}

	private void manulChess(int x, int y, int color) {
		if (colorStatus == color && change(x, y, colorStatus)) {
			chessNum++;
			colorStatus = -colorStatus;
		}
	}

    static BufferedImage cross = null;
    static BufferedImage white = null;
    static BufferedImage black = null;
    static BufferedImage letter[] = null;
	
    //liuyi 2006-12-01 程序优化 start
    public void updateImage() {
    	int cols = 8, rows = 8, length = 11;

        try {

            int width = (cols + 1) * length, height = (rows + 1) * length;
            if(cross == null)
            {
            	cross = ImageIO.read(OthelloAction.class
                    .getResourceAsStream("/img/othello/cross.wbm"));

            	white = ImageIO.read(OthelloAction.class	
                    .getResourceAsStream("/img/othello/white.wbm"));

            	black = ImageIO.read(OthelloAction.class
                    .getResourceAsStream("/img/othello/black.wbm"));

	            letter = new BufferedImage[cols];
	            letter[0] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/A.wbm"));
	            letter[1] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/B.wbm"));
	            letter[2] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/C.wbm"));
	            letter[3] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/D.wbm"));
	            letter[4] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/E.wbm"));
	            letter[5] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/F.wbm"));
	            letter[6] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/G.wbm"));
	            letter[7] = ImageIO.read(OthelloAction.class
	                    .getResourceAsStream("/img/othello/H.wbm"));
	            
            }
            if(image == null)
            	image = new BufferedImage(width, height,
                    BufferedImage.TYPE_BYTE_BINARY);

            //获取图形上下文
            Graphics g = image.getGraphics();

            //设定背景色
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            int i, j, point;
            for(i = 0;i < cols;i++)
            {
            	g.drawImage(letter[cols - i - 1], 0, i * length, null);
            	g.drawImage(letter[i], i * length + length, cols * length, null);
            }

            //画点
            for (i = 0; i < cols; i++) {
                for (j = 0; j < rows; j++) {
                    point = board[i][j];
                    switch (point) {
                    case 0:
                        g.drawImage(cross, (i + 1) * length, j * length, null);
                        break;
                    case -1:
                        g.drawImage(black, (i + 1) * length, j * length, null);
                        break;
                    case 1:
                        g.drawImage(white, (i + 1) * length, j * length, null);
                        break;
                    }
                }
            }
            g.dispose();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //liuyi 2006-12-01 程序优化 end
}
