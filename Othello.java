import java.awt.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class OthelloMove {
    
    public int row;
    public int col;
    public int player;
    
    public OthelloMove(int _row, int _col, int _player) {
        row = _row;
        col = _col;
        player = _player;
    }
    
    public String toString() {
        return row + ", " + col + " : " + player;
    }
    
}


class OthelloPlayer {
    
    public int playerColor;
    
    public OthelloPlayer(Integer _color) {
        playerColor = _color;
    }
    
    public int getOpponentColor() {
        if (playerColor == 1) {
            return 2;
        }
        else if (playerColor == 2) {
            return 1;
        }
        return -1;
    }
    
    /*
     *override this method! :P
     */
    public OthelloMove makeMove(OthelloBoard board)  throws InterruptedException {
        //Move m = new Move(0, 0, BLACK)
        //board.addPiece(m);
        return null;
    }
    
}


class OthelloClicker extends MouseAdapter {
    OthelloBoard ob;
    
    public OthelloClicker(OthelloBoard _ob) {
        super();
        ob = _ob;
    }

    @Override
    public void mousePressed ( MouseEvent e )
    {
	if (ob.clicked == false) {
	    ob.y_coord = e.getPoint().getY();
	    ob.x_coord = e.getPoint().getX();
	    ob.clicked = true;
	}
    }
}

class OthelloBoardViewer extends JComponent {

    OthelloBoard ob = null;
    
    public OthelloBoardViewer(OthelloBoard _ob) {
        super();
        ob = _ob;
    }

    public void paintComponent(Graphics g) {
        int checkerBuffer = 5;
        int squareWidth = getWidth() / ob.size;
        int squareHeight= getHeight() / ob.size;
        int checkerWidth = getWidth() / ob.size - checkerBuffer*2;
        int checkerHeight = getHeight() / ob.size - checkerBuffer*2;
        for (int r = 0; r < ob.size; r++) {
            for (int c = 0; c < ob.size; c++) {
                // square
                g.setColor(Color.BLACK);
                g.drawRect(c * squareWidth, r * squareHeight, squareWidth, squareHeight);

                if (ob.board[r][c] == 1) {
                    // black piece
                    g.setColor(Color.BLACK);
                    g.fillOval(c * squareWidth+ checkerBuffer , r * squareHeight+ checkerBuffer, checkerWidth, checkerHeight);
                }
                else if (ob.board[r][c] == 2) {
                    // white piece
                    g.setColor(Color.WHITE);
                    g.fillOval(c * squareWidth + checkerBuffer, r * squareHeight+ checkerBuffer, checkerWidth, checkerHeight);
                }
            }
        }
    }
	
}




class OthelloBoard {
    
    public int size;
    public boolean hasViewWindow;
    public int[][] board = null;
    JFrame viewer = null;
    public boolean clicked;
    public double y_coord;
    public double x_coord;    

    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    
    public OthelloBoard(int _size, boolean _hasViewWindow) {
        size = _size;
        hasViewWindow = _hasViewWindow;
        if (hasViewWindow) {
            viewer = new JFrame();
            viewer.setTitle("Game Viewer");
            viewer.setSize(WIDTH + 50, HEIGHT + 50);
            viewer.add(new OthelloBoardViewer(this));
	    viewer.getContentPane().addMouseListener(new OthelloClicker(this));
            viewer.setVisible(true);
            viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
       
        board = new int[size][size];
        
        // init the middle starting grid
        int row = size / 2 - 1;
        int col = size / 2 - 1;
        board[row][col] = 2;
        board[row+1][col+1] = 2;
        board[row][col+1] = 1;
        board[row+1][col] = 1;
        
    }
    
    public void repaint() {
        viewer.repaint();
    }

    public boolean flanksUp(OthelloMove m) {
        int currRow = m.row - 1;
        boolean atLeastOne = false;
        while (currRow >= 0 && board[currRow][m.col] != m.player && board[currRow][m.col] != 0) {
            currRow--;
            atLeastOne = true;
            
        }
        if (currRow >= 0 && board[currRow][m.col] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    
    public boolean flanksDown(OthelloMove m) {
        int currRow = m.row + 1;
        boolean atLeastOne = false;
        while (currRow < size && board[currRow][m.col] != m.player && board[currRow][m.col] != 0) {
            currRow++;
            atLeastOne = true;
            
        }
        if (currRow < size && board[currRow][m.col] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    public boolean flanksRight(OthelloMove m) {
        int currCol = m.col + 1;
        boolean atLeastOne = false;
        while (currCol < size && board[m.row][currCol] != m.player && board[m.row][currCol] != 0) {
            currCol++;
            atLeastOne = true;
        }
        if (currCol < size && board[m.row][currCol] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    
    public boolean flanksLeft(OthelloMove m) {
        int currCol = m.col - 1;
        boolean atLeastOne = false;
        
        while (currCol >= 0 && board[m.row][currCol] != m.player && board[m.row][currCol] != 0) {
            currCol--;
            atLeastOne = true;
            
        }
        if (currCol >= 0 && board[m.row][currCol] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    public boolean flanksUpRight(OthelloMove m) {
        int currRow = m.row - 1;
        int currCol = m.col + 1;
        boolean atLeastOne = false;
        
        while (currCol < size && currRow >= 0 && board[currRow][currCol] != m.player && board[currRow][currCol] != 0) {
            currCol++;
            currRow--;
            atLeastOne = true;
            
        }
        if (currCol < size && currRow >= 0 && board[currRow][currCol] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    
    public boolean flanksUpLeft(OthelloMove m) {
        int currRow = m.row - 1;
        int currCol = m.col - 1;
        boolean atLeastOne = false;
        
        while (currCol >= 0 && currRow >= 0 && board[currRow][currCol] != m.player && board[currRow][currCol] != 0) {
            currCol--;
            currRow--;
            atLeastOne = true;
            
        }
        if (currCol >= 0 && currRow >= 0 && board[currRow][currCol] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    
    public boolean flanksDownRight(OthelloMove m) {
        int currRow = m.row + 1;
        int currCol = m.col + 1;
        boolean atLeastOne = false;
        
        while (currCol < size && currRow < size && board[currRow][currCol] != m.player && board[currRow][currCol] != 0) {
            currCol++;
            currRow++;
            atLeastOne = true;
            
        }
        if (currCol < size && currRow < size && board[currRow][currCol] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    
    public boolean flanksDownLeft(OthelloMove m) {
        int currRow = m.row + 1;
        int currCol = m.col - 1;
        boolean atLeastOne = false;
        
        while (currCol >= 0 && currRow < size && board[currRow][currCol] != m.player && board[currRow][currCol] != 0) {
            currCol--;
            currRow++;
            atLeastOne = true;
            
        }
        if (currCol >= 0 && currRow < size && board[currRow][currCol] == m.player && atLeastOne) {
            return true;
        }
        return false;
    }
    
    
    public boolean flanksEnemy(OthelloMove m) {
        // There must be a line from move point to another friendly checker with
        // only enemy checkers in between (no spaces)
               
        return flanksUp(m) || flanksDown(m) || flanksRight(m) || flanksLeft(m) || flanksUpLeft(m) || flanksUpRight(m) || flanksDownLeft(m) || flanksDownRight(m);
    }
    
    
    public boolean isLegalMove(OthelloMove m) {
        
        // can't play on a space with a piece already there
        if (board[m.row][m.col] != 0) {
            return false;
        }
        
        // can't play on a space that doesn't flank enemies and lead to your own piece
        return flanksEnemy(m);
    }
    
    public void flipUp(OthelloMove m) {
        int currRow = m.row - 1;
        while (board[currRow][m.col] != m.player) {
            board[currRow][m.col] = m.player;
            currRow--;
        }
    }
    
    public void flipDown(OthelloMove m) {
        int currRow = m.row + 1;
        while (board[currRow][m.col] != m.player) {
            board[currRow][m.col] = m.player;
            currRow++;
        }
    }
    
    public void flipRight(OthelloMove m) {
        int currCol = m.col + 1;
        while (board[m.row][currCol] != m.player) {
            board[m.row][currCol] = m.player;
            currCol++;
        }
    }
    
    public void flipLeft(OthelloMove m) {
        int currCol= m.col - 1;
        while (board[m.row][currCol] != m.player) {
            board[m.row][currCol] = m.player;
            currCol--;
        }
    }
    
    public void flipUpRight(OthelloMove m) {
        int currRow = m.row - 1;
        int currCol = m.col + 1;
        while (board[currRow][currCol] != m.player) {
            board[currRow][currCol] = m.player;
            currCol++;
            currRow--;
        }

    }
    
    public void flipUpLeft(OthelloMove m) {
        int currRow = m.row - 1;
        int currCol = m.col - 1;
        while (board[currRow][currCol] != m.player) {
            board[currRow][currCol] = m.player;
            currCol--;
            currRow--;
        }
    }
    
    public void flipDownRight(OthelloMove m) {
        int currRow = m.row + 1;
        int currCol = m.col + 1;
        while (board[currRow][currCol] != m.player) {
            board[currRow][currCol] = m.player;
            currCol++;
            currRow++;
        }
    }
    
    
    public void flipDownLeft(OthelloMove m) {
        int currRow = m.row + 1;
        int currCol = m.col - 1;
        while (board[currRow][currCol] != m.player) {
            board[currRow][currCol] = m.player;
            currCol--;
            currRow++;
        }
    }
    
    
    public void flipCheckers(OthelloMove m) {
        if (flanksUp(m)) { flipUp(m); }
        if (flanksDown(m)) { flipDown(m); }
        if (flanksRight(m)) { flipRight(m); }
        if (flanksLeft(m)) { flipLeft(m); }
        if (flanksUpLeft(m)) { flipUpLeft(m); }
        if (flanksUpRight(m)) { flipUpRight(m); }
        if (flanksDownLeft(m)) { flipDownLeft(m); }
        if (flanksDownRight(m)) { flipDownRight(m); }
    }
    
    public void addPiece(OthelloMove m) {
        board[m.row][m.col] = m.player;
        
        flipCheckers(m);
    }
    
    public boolean gameOver() {
        return !playerHasLegalMove(BLACK) && !playerHasLegalMove(WHITE);
    }
    
    public int getBoardScore() {
        // return the point differential in black's favor
        // +3 means black is up by 3
        // -5 means white is up by 5
        int blackAdvantage = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == BLACK) {
                    blackAdvantage++;
                }
                else if (board[r][c] == WHITE) {
                    blackAdvantage--;
                }
            }
        }
        return blackAdvantage;
    }
    
    public ArrayList<OthelloMove> legalMoves(int p) {
        ArrayList<OthelloMove> moves = new ArrayList<OthelloMove>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                OthelloMove m = new OthelloMove(r, c, p);
                if (isLegalMove(m)) {
                    moves.add(m);
                }
            }
        }
        return moves;
    }
    
    public boolean playerHasLegalMove(int p) {
        return legalMoves(p).size() > 0;
    }
    
    public String toString() {
        String result = "";
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                result += board[r][c] + " ";
            }
            result += "\n";
        }
        return result;
    }
}


class OthelloGame {
    
    
    public static int playGame(int boardSize, OthelloPlayer whitePlayer, OthelloPlayer blackPlayer, boolean viewPlayback, int playbackDelay)  throws InterruptedException {
        int moves = 0;
        
        OthelloBoard ob = new OthelloBoard(boardSize, viewPlayback);
        OthelloPlayer[] players = {blackPlayer, whitePlayer};
        
        int i = 0;
        while (!ob.gameOver()) {
            int currPlayer = (i % 2) + 1;
            if (ob.playerHasLegalMove(currPlayer)) {

                //System.out.println("move #" + i);
                //System.out.println("  has legal move");
                OthelloMove m = players[i%2].makeMove(ob);
                if (ob.isLegalMove(m)) {
                    ob.addPiece(m);
                }
                
                if (ob.hasViewWindow) {
                    ob.repaint();
                }
                
                try {
                    Thread.sleep(playbackDelay);
                } catch (InterruptedException e) {
                    System.out.println("Execution interrupted!");
                }
                
            }
            else {
                //System.out.println("Turn skipped because of no legal moves");
            }
            i++;
        }
        
        return ob.getBoardScore();
    }
    
    
    
    
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {

        
        if (args.length < 5) {
            System.out.println("USAGE:\n\t java OthelloGame BlackBotClassName WhiteBotClassName BOARD_SIZE NUMBER_GAMES PLAYBACK_DISPLAY PLAYBACK_DELAY\n");
            System.out.println("EXAMPLE:\n\t java OthelloGame RandomBot RandomBot 8 10 0 0");
            System.exit(1);
        }
        
        String bot1ClassName = args[0];
        String bot2ClassName = args[1];

        int BOARD_SIZE = Integer.parseInt(args[2]);
        int NUMBER_GAMES = Integer.parseInt(args[3]);
        boolean viewPlayback = Integer.parseInt(args[4]) > 0;
        int PLAYBACK_DELAY = Integer.parseInt(args[5]);      
        
        Integer BLACK = 1;
        Integer WHITE = 2;
        //OthelloBoard ob = new OthelloBoard(BOARD_SIZE, viewPlayback);

        Class c1 = Class.forName(bot2ClassName);
        Constructor con1 = c1.getConstructor(Integer.class);
        OthelloPlayer player1 = (OthelloPlayer)con1.newInstance(WHITE);
        
        Class c2 = Class.forName(bot1ClassName);
        Constructor con2 = c2.getConstructor(Integer.class);
        OthelloPlayer player2 = (OthelloPlayer)con2.newInstance(BLACK);        

        int blackVictories = 0;
        for (int i = 0; i < NUMBER_GAMES; i++) {
            int gameScore = playGame(BOARD_SIZE, player1, player2, viewPlayback, PLAYBACK_DELAY);
            System.out.println("Game score: " + gameScore);
            if (gameScore > 0) {
                blackVictories++;
            }
        }
        int whiteVictories = NUMBER_GAMES - blackVictories;
        System.out.println(bot1ClassName + " as Black: " + blackVictories + " " + ((float)blackVictories) / NUMBER_GAMES);
        System.out.println(bot2ClassName + " as White: " + whiteVictories + " " + ((float)whiteVictories) / NUMBER_GAMES);
    }    
}
