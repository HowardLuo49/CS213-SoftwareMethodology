package pieces;
/**
 * Knight is a subclass of <a href="#{@link}">{@link Piece}</a> that handles movement behavior of a knight.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Knight extends Piece{
	/**
	 * <code>String</code> that signifies that this <a href="#{@link}">{@link Piece}</a> is a knight
	 * "wN" signifies a white knight, while "bN" signifies a black knight.
	 */
	private String name = "N";
	/**
	 * Constructor that creates a knight of the specified color.
	 * @param white the desired color of the piece
	 */
	public Knight(boolean white) {
		super(white);
		
		if(white)
			this.name = "w" + this.name;
		else
			this.name = "b" + this.name;
	}
	/**
	 * Method that determines if the move is legal, following the logic of knight movement.
	 * 
	 * A legal move must be either two horizontal spaces and one vertical space
	 * or two vertical spaces and one horizontal space. A knight is able to move
	 * over pieces, so it does not need to check for obstructing pieces.
	 */
	public boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board) {

		int xStart = 8 - rankStart;
		int yStart = fileStart - 1;
		int xEnd = 8 - rankEnd;
		int yEnd = fileEnd - 1;
		
		Piece target = board[xEnd][yEnd];
		
		if(xStart == xEnd && yStart == yEnd)
			return false;
		
		if(target != null) {
			//Checking if attacking same color piece
			if(white && target.white)
				return false;
			if(!white && !target.white)
				return false;
		}
		
		//Checks legal knight moves
		if(xStart + 1 == xEnd || xStart -1 == xEnd) {
			if(yStart - 2 == yEnd || yStart + 2 == yEnd)
				return true;
		}
		else if(xStart + 2 == xEnd || xStart -2 == xEnd) {
			if(yStart -1 == yEnd || yStart +1 == yEnd)
				return true;
		}
		
		return false;
	}
	/**
	 * Returns a String "wN" (if the knight is white) or "bN" (if it is black) 
	 */
	public String name() {
		return this.name;
	}
}
