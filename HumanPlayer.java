import java.util.Scanner;
import javax.swing.JOptionPane;

public class HumanPlayer extends OthelloPlayer {
    
    private Scanner s;
    
    public HumanPlayer(Integer _color) {
        super(_color);
        s = new Scanner(System.in);
    }

    public OthelloMove makeMove(OthelloBoard board) {
        //System.out.println(board);
        
        while (true) {
	    synchronized(this){
		if (board.viewer != null) {
		    if (board.clicked) {
			board.clicked = false;
			// Map a click on the jframe to a board move
			int frame_width = board.viewer.getContentPane().getWidth();
			int frame_height = board.viewer.getContentPane().getHeight();
			int x_pos = (int)(board.x_coord / (frame_width / (double)board.size));
			int y_pos = (int)(board.y_coord / (frame_height / (double)board.size));
			OthelloMove m = new OthelloMove(y_pos, x_pos, playerColor);
			if (board.isLegalMove(m)) {
			    return m;
			}
			else {
			    JOptionPane.showMessageDialog(null, "That was an illegal move! Please try again.");
			}
		    }
		}
		else {
		    System.out.println("Human players should be using the PLAYBACK_DISPLAY!");
		    System.exit(1);
		}

	    }
            // String line = s.nextLine();
            // String tokens[] = line.split(" ");
            // OthelloMove m = new OthelloMove(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), playerColor);
            // if (board.isLegalMove(m)) {
            //     return m;
            // }
        }

    }
}
