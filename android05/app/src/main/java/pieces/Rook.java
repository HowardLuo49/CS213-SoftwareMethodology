package pieces;
/**
 * Rook is a subclass of <a href="#{@link}">{@link Piece}</a> that handles movement behavior of a rook.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Rook extends Piece{
	/**
	 * <code>String</code> that signifies that this <a href="#{@link}">{@link Piece}</a> is a rook
	 * "wR" signifies a white rook, while "bR" signifies a black rook.
	 */
	private String name = "R";
	/**
	 * Constructor that creates a rook of the specified color.
	 * @param white the desired color of the piece
	 */
	public Rook(boolean white) {
		super(white);
		
		if(white)
			this.name = "w" + this.name;
		else
			this.name = "b" + this.name;
	}
	/**
	 * Method that determines if the move is legal, following the logic of rook movement.
	 * 
	 * A legal move can be of any distance, but it must be either 
	 * horizontal or vertical from the starting position, and
	 * there cannot be an obstructing piece in the path of the rook.
	 * This method sets <a href="#{@link}">{@link Piece#firstMove}</a> to false
	 * to signify that it is no longer a candidate for castling.
	 * 
	 */
	public boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board) {
		
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		
		int xStart = 8 - rankStart;
		int yStart = fileStart - 1;
		int xEnd = 8 - rankEnd;
		int yEnd = fileEnd - 1;
		
		Piece target = board[xEnd][yEnd];
		
		if(xStart == xEnd && yStart == yEnd)
			return false;
		if(xStart != xEnd && yStart != yEnd)
			return false;
		
		//which direction rook is moving
		if(fileStart > fileEnd)
			left = true;
		if(fileStart < fileEnd)
			right = true;
		if(rankStart > rankEnd)
			down = true;
		if(rankStart < rankEnd)
			up = true;
		
		if(target != null) {
			if(white && target.white)
				return false;
			if(!white && !target.white)
				return false;
		}
		
		//check if there are pieces intercepting the rook and the target square
		if(up) {
			for(int i = xStart - 1; i > xEnd; i--) {
				if(board[i][yEnd] != null)
					return false;
			}
		}
		if(down) {
			for(int i = xStart + 1; i < xEnd; i++) {
				if(board[i][yEnd] != null)
					return false;
			}
		}
		if(left) {
			for(int i = yStart - 1; i > yEnd; i--) {
				if(board[xEnd][i] != null)
					return false;
			}
		}
		if(right) {
			for(int i = yStart + 1; i < yEnd; i++) {
				if(board[xEnd][i] != null)
					return false;
			}
		}
		
		firstMove = false;
		return true;
	}
	/**
	 * Returns a String "wR" (if the rook is white) or "bR" (if it is black) 
	 */
	public String name() {
		return this.name;
	}
}
