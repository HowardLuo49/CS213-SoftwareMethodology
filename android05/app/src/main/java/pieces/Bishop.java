package pieces;
/**
 * Bishop is a subclass of <a href="#{@link}">{@link Piece}</a> that handles movement behavior of a bishop.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Bishop extends Piece{
	/**
	 * <code>String</code> that signifies that this <a href="#{@link}">{@link Piece}</a> is a bishop
	 * "wB" signifies a white bishop, while "bB" signifies a black bishop.
	 */
	private String name = "B";
	/**
	 * Constructor that creates a bishop of the specified color.
	 * @param white the desired color of the piece
	 */
	public Bishop(boolean white) {
		super(white);
		
		if(white)
			this.name = "w" + this.name;
		else
			this.name = "b" + this.name;
	}
	/**
	 * Method that determines if the move is legal, following the logic of bishop movement.
	 * 
	 * A legal move can be any number of spaces, but it must be diagonal to the starting position 
	 * and there cannot be an obstructing piece in the path of the bishop.
	 */
	public boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board) {

		boolean upLeft = false;
		boolean upRight = false;
		boolean downLeft = false;
		boolean downRight = false;
		
		int xStart = 8 - rankStart;
		int yStart = fileStart - 1;
		int xEnd = 8 - rankEnd;
		int yEnd = fileEnd - 1;
		
		Piece target = board[xEnd][yEnd];
		
		if(xStart == xEnd && yStart == yEnd)
			return false;
		
		//which direction bishop is moving
		if(fileStart > fileEnd) {
			if(rankStart > rankEnd)
				downLeft = true;
			else
				upLeft = true;
		}
		if(fileStart < fileEnd) {
			if(rankStart > rankEnd)
				downRight = true;
			else
				upRight = true;
		}
		
		//movement must remain on diagonal
		if(Math.abs(xStart - xEnd) != Math.abs(yStart - yEnd))
			return false;
		
		if(target != null) {
			if(white && target.white)
				return false;
			if(!white && !target.white)
				return false;
		}
		
		//check if there are pieces intercepting the bishop and the target square
		if(upRight) {
			int j = yStart + 1;
			for(int i = xStart - 1; i > xEnd; i--) {
				if(board[i][j] != null)
					return false;
				j++;
			}
		}
		if(upLeft) {
			int j = yStart - 1;
			for(int i = xStart - 1; i > xEnd; i--) {
				if(board[i][j] != null)
					return false;
				j--;
			}
		}
		if(downRight) {
			int j = yStart + 1;
			for(int i = xStart + 1; i < xEnd; i++) {
				if(board[i][j] != null)
					return false;
				j++;
			}
		}
		if(downLeft) {
			int j = yStart - 1;
			for(int i = xStart + 1; i < xEnd; i++) {
				if(board[i][j] != null)
					return false;
				j--;
			}
		}
		
		return true;
	}
	/**
	 * Returns a String "wB" (if the bishop is white) or "bB" (if it is black) 
	 */
	public String name() {
		return this.name;
	}
}
