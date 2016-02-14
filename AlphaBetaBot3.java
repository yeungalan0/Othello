import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;


    // makeMove gets a current OthelloBoard game state as input
    // and then returns an OthelloMove object
    
    // Your bot knows what color it is playing
    //    because it has a playerColor int field
    
    // Your bot can get an ArrayList of legal moves:
    //    ArrayList<OthelloMove> moves = board.legalMoves(playerColor);
    
    // The constructor for OthelloMove needs the row, col, and player color ints.
    // For example, play your token in row 1, col 2:
    //   OthelloMove m = new OthelloMove(1, 2, playerColor);
    
    // OthelloBoard objects have a public size field defining the
    // USE THIS FOR DEPTH LIMITING!
    // size of the game board:
    //   board.size
    
    // You can ask the OthelloBoard if a particular OthelloMove
    //  flanks in a certain direction.
    // For example:
    //  board.flanksLeft(m) will return true if you can capture pieces to the left of move, m
    
    // You can ask the board what the current score is.
    //  This is just the difference in checker counts
    //  return the point differential in black's favor
    //  +3 means black is up by 3
    //  -5 means white is up by 5
    // int score = board.getBoardScore();

    // OthelloBoard has a toString:
    //  System.out.println(board);
    
    // OthelloPlayer superclass has a method to get the color for your opponent:
    //  int opponentColor = getOpponentColor();


public class AlphaBetaBot3 extends OthelloPlayer {
    Map<Node, Integer> seen = new HashMap<Node, Integer>();
    boolean endGame = false;
    public AlphaBetaBot3(Integer _color) {
        super(_color);
    }
    
    public OthelloMove makeMove(OthelloBoard board) {
        //Reset the moves you've seen
	seen = new HashMap<Node, Integer>();
	//make the best move that was recurssively found
	System.out.println("MADE MOVE");
	java.util.Date date= new java.util.Date();
	System.out.println(new Timestamp(date.getTime()));
	return findMove(board);
    }

    public OthelloMove findMove(OthelloBoard board) {
	//max depth to search down the tree
	final int MAX_DEPTH = 7;
	//figure out if we are near end game play by counting the number of plays still possible on the board
	if (countEmpty(board) < 15) {
	    endGame = true;
	}
	//needed incase multiple games are played
	else {
	    endGame = false;
	}

	//finds the best move recurssively using alpha beta pruning and depth first search given the current board configuration
	Node root = new Node(board);
	//pass the min value for alpha since alpha is supposed to get maximized, and vice versa with beta
	int value = findMoveHelper(root, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, playerColor);
	ArrayList<OthelloMove> options = new ArrayList<OthelloMove>();
	for (Node child : root.children) {
	    if(child.value == value) {
		options.add(child.move);
	    }
	}
	return options.get(options.size()-1);
    }

    public int findMoveHelper(Node node, int depth, int alpha, int beta, int player) {
	int opponent;
	if (player == 1) {
	    opponent = 2;
	}
	else {
	    opponent = 1;
	}
	ArrayList<OthelloMove> moves = node.board.legalMoves(player);
	ArrayList<OthelloMove> opponentMoves = node.board.legalMoves(opponent);
	boolean haveMoves = moves.size() > 0;
	boolean opponentHaveMoves = opponentMoves.size() > 0;
	//BASE CASE: the node is a leaf/you are out of moves and your opponent is out of moves!...
	//only use this evaluation for end game board evaluation or when the game is over
	if (!haveMoves && !opponentHaveMoves) {
	    return evaluateEndGame(node);
	}
	//use the heuristic if the depth has been reached and the bot is not in endGame mode
	if (depth == 0 && !endGame) {
	    //set the value of the node to the internal boardValue
	    node.value = node.boardValue;
	    return node.boardValue;
	}
	//if you're out of moves, but your opponent is not, then it will be the opponents move with the board
	if (!haveMoves) {
	    moves = opponentMoves;
	    player = opponent;
	}
	    
	Node child = null;
	int child_value = 0;
	Integer valueFound = 0;
	ArrayList<Node> children = new ArrayList<Node>();
	if (player == playerColor) {
	    node.value = Integer.MIN_VALUE;
	    for (OthelloMove m : moves) {
		OthelloBoard newboard = new OthelloBoard(node.board.size, false);
		//reset the board for each child
		newboard.board = deepCopy(node.board.board);
		//do the move on the board
		newboard.addPiece(m);
		//create a child node from the move and recursively run the function
		child = new Node(newboard);
		if (!endGame) {
		    child.setBoardValue();
		}
		//EFFICIENCY: this may be inefficient, I just might be able to set an isroot boolean in each node instead...
		node.addChild(child);
		children.add(child);
		//if root node then set children and moves of children
		if(node.parent == null) {
		    child.move = m;
		    node.children.add(child);
		}
	    }
	    //Sort the children based on their board value (in ascending order), and also updates the child boardValues
	    Collections.sort(children, Collections.reverseOrder());
	    for (Node childNode : children) {
		valueFound = seen.get(childNode);
		if (valueFound != null) {
		    child_value =  valueFound;
		}
		else {
		    child_value = findMoveHelper(childNode, depth-1, alpha, beta, getOpponentColor());
		}
		node.value = Math.max(node.value, child_value);
		alpha = Math.max(node.value, alpha);
		if (beta < alpha) {
		    break;
		}
		//Add child to seen
		if (valueFound == null) {
		    seen.put(childNode, child_value);
		}
 	    }
	    return node.value;
	}

	else {
	    node.value = Integer.MAX_VALUE;
	    //set the value to the smallest possible integer
	    for (OthelloMove m : moves) {
		OthelloBoard newboard = new OthelloBoard(node.board.size, false);
		//reset the board for each child
		newboard.board = deepCopy(node.board.board);
		//do the move on the board
		newboard.addPiece(m);
		//create a child node from the move and recursively run the function
		child = new Node(newboard);
		if (!endGame) {
		    child.setBoardValue();
		}
		//EFFICIENCY: this may be inefficient, I just might be able to set an isroot boolean in each node instead...
		node.addChild(child);
		children.add(child);
	    }
	    //Sort the children based on their board value (in descending order), and also updates the child boardValues
	    Collections.sort(children);
	    for (Node childNode : children) {
		valueFound = seen.get(childNode);
		if (valueFound != null) {
		    child_value = valueFound;
		}
		else {
		    child_value = findMoveHelper(childNode, depth-1, alpha, beta, playerColor);
		}
		node.value = Math.min(node.value, child_value);
		beta = Math.min(node.value, beta);
		if (beta < alpha) {
		    break;
		}
		if (valueFound == null) {
		    seen.put(childNode, child_value);
		}
 	    }
	    return node.value;
	}
    }

    //Returns the value of the board in the node
    public int evaluationFunction(Node node) {
	int value = mobilityAndPieces(node.board) + stability(node.board) + cornerScore(node.board) * 500;
	return value;
    }

    public int mobilityAndPieces(OthelloBoard board) {
	//Idea for potential mobility from: http://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/RUSSIA/Final_Paper.pdf
	int playerMob = board.legalMoves(playerColor).size();
	int opponentMob = board.legalMoves(getOpponentColor()).size();
	int pieces = board.getBoardScore();
	if (playerColor == 2) {
	    pieces = -pieces;
	}
	int count = 0;
	int pieceCount = 0;
	int size = board.size;
	int potMob = 0;
	int[] response;
	for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
		if (board.board[r][c] == 0) {
		    response = checkEmpty(board, r, c);
		    potMob += response[0];
		    count += response[1];
		}
		else {
		    pieceCount += 1;
		}
	    }
	}

	int total = 0;
	if (count != 0) {
	    total += (int)((potMob/(double)count)*100);
	}
	if ((playerMob+opponentMob) != 0) {
	    total += (int)(((playerMob-opponentMob)/(double)(playerMob+opponentMob)) * 300);
	}
	if (pieceCount != 0) {
	    total += (int)((pieces/(double)pieceCount)*10);
	}
	
	return total;
    }

    //These stability measures came from http://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/RUSSIA/Final_Paper.pdf
    int[][] stableBoard = { {4, -3, 2, 2, 2, 2, -3, 4},
    			    {-3, -4, -1, -1, -1, -1, -4, -3},
			    {2, -1, 1, 0, 0, 1, -1, 2},
			    {2, -1, 0, 1, 1, 0, -1, 2},
			    {2, -1, 0, 1, 1, 0, -1, 2},
			    {2, -1, 1, 0, 0, 1, -1, 2},
			    {-3, -4, -1, -1, -1, -1, -4, -3},
			    {4, -3, 2, 2, 2, 2, -3, 4},};
    public int stability(OthelloBoard board) {
	if (board.size != 8) {
	    return 0;
	}
	int score = 0;
	//int total = 0;
	for (int r = 0; r < board.size; r++) {
            for (int c = 0; c < board.size; c++) {
		if (board.board[r][c] != 0) {
		    if (board.board[r][c] == playerColor) {
			score += stableBoard[r][c];
		    }
		    else {
			score -= stableBoard[r][c];
		    }
		}
	    }
	}

	return score;
    }

    //Checks the empty spaces for the nearby tokens to determine player potential mobility
    public int[] checkEmpty(OthelloBoard board, int row, int col) {
	//Add one if you find an opponents piece, subtract one if you find your own piece
	int size = board.size - 1;
	int score = 0;
	int total = 0;
	int opponent = getOpponentColor();
	if (col-1 >= 0) {
	    if (board.board[row][col-1] == playerColor) {
		score -= 1;
		total += 1;
	    }
	    else if (board.board[row][col-1] == opponent) {
		score += 1;
		total += 1;
	    }
	}
	if (row-1 >= 0) {
	    if (board.board[row-1][col] == playerColor) {
		score -= 1;
		total += 1;
	    }
	    else if (board.board[row-1][col] == opponent) {
		score += 1;
		total += 1;
	    }
	}
	if (col+1 <= size) {
	    if (board.board[row][col+1] == playerColor) {
		score -= 1;
		total += 1;
	    }
	    else if (board.board[row][col+1] == opponent) {
		score += 1;
		total += 1;
	    }
	}
	if (row+1 <= size) {
	    if (board.board[row+1][col] == playerColor) {
		score -= 1;
		total += 1;
	    }
	    else if (board.board[row+1][col] == opponent) {
		score += 1;
		total += 1;
	    }
	}
	int[] returns = new int[2];
	returns[0] = score;
	returns[1] = total;
	return returns;
    }

    //Returns the value of the board in the node and sets the nodes value
    public int evaluateEndGame(Node node) {
	//Weight the values the same as the evaluationFunction values
	int value = node.board.getBoardScore() * 500;
	if (playerColor == 2) {
	    node.value = -value;
	    return -value;
	}
	node.value = value;
	return value;
    }

    public int cornerScore(OthelloBoard board) {
	int score = 0;
	int size = board.size;
	if (board.board[0][0] == playerColor) {
	    score += 10;
	}
	else if(board.board[0][0] == getOpponentColor()) {
	    score -= 10;
	}
	if (board.board[0][size-1] == playerColor) {
	    score += 10;
	}
	else if(board.board[0][size-1] == getOpponentColor()) {
	    score -= 10;
	}
	if (board.board[size-1][0] == playerColor) {
	    score += 10;
	}
	else if(board.board[size-1][0] == getOpponentColor()) {
	    score -= 10;
	}
	if (board.board[size-1][size-1] == playerColor) {
	    score += 10;
	}
	else if(board.board[size-1][size-1] == getOpponentColor()) {
	    score -= 10;
	}
	return score;
    }

    public static int[][] deepCopy(int[][] original) {
	//deepcopy code heaily borrowed from http://stackoverflow.com/questions/1564832/how-do-i-do-a-deep-copy-of-a-2d-array-in-java
	if (original == null) {
	    return null;
	}
	int[][] result = new int[original.length][];
	for (int i = 0; i < original.length; i++) {
	    result[i] = Arrays.copyOf(original[i], original[i].length);
	}
	return result;
    }
    
    //Returns the count of squares on the board that are empty
    public int countEmpty(OthelloBoard board) {
	int count = 0;
	int size = board.size;
	for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
		if (board.board[r][c] == 0) {
		    count += 1;
		}
	    }
	}
	return count;
    }

    public static void main(String[] args) {
	OthelloBoard b = new OthelloBoard(4, false);
    }
    
    class Node implements Comparable {
	Node parent = null;
	OthelloMove move = null;
	OthelloBoard board = null;
	ArrayList<Node> children = new ArrayList();
	//Map<Node, Integer> seen = new HashMap<Node, Integer>();
	//set the default value of the node to the smallest possible int
	int value;
	int boardValue;
    
	public Node(OthelloBoard b) {
	    board = b;
	}

	public void addChild(Node c) {
	    c.parent = this;
	}

	public void setBoardValue() {
	    boardValue = evaluationFunction(this);
	}

	@Override
	public boolean equals(Object other) {
	    Node otherNode = (Node)other;
	    for (int r = 0; r < board.size; r++) {
		for (int c = 0; c < board.size; c++) {
		    if (board.board[r][c] != otherNode.board.board[r][c]) {
			return false;
		    }
		}
	    }
	    return true;
	}

	@Override
	public int hashCode() {
	    return java.util.Arrays.deepHashCode(board.board);
	}

	//Allows you to sort nodes based on the board "goodness"
	@Override
	public int compareTo(Object other) {
	    Node otherNode = (Node) other;
	    return boardValue - otherNode.boardValue;
	}

	@Override
	public String toString() {
	    if (parent != null && move != null) {
		return "Node Value: " + value + " " + "Parent Value: " + parent.value + " Move: " + move;
	    }
	    else if(move != null) {
		return "Node Value: " + value + " Move: " + move;
	    }
	    
	    return "Node Value: " + value;
	}
    }
}

