package pieces;
/**
 * Pawn is a subclass of <a href="#{@link}">{@link Piece}</a> that handles movement behavior of a pawn.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Pawn extends Piece {
	/**
	 * <code>String</code> that signifies that this <a href="#{@link}">{@link Piece}</a> is a pawn
	 * "wP" signifies a white pawn, while "bP" signifies a black pawn.
	 */
	private String name = "P";
	/**
	 * Constructor that creates a pawn of the specified color.
	 * @param white the desired color of the piece
	 */
	public Pawn(boolean white) {
		super(white);
		
		if(white)
			this.name = "w" + this.name;
		else
			this.name = "b" + this.name;
	}
	
	/**
	 * Method that determines if the move is legal, following the logic of pawn movement.
	 * 
	 * A legal move can be one of the following:
	 * <ul>
	 * <li> One space directly forward (vertical in the direction of the opposing color), so long as the space is empty.
	 * <li> Two moves forward if it is the pawn's first move.
	 * <li> One space diagonally forward if this space has an opposing piece (this piece is then captured).
	 * <li> One space diagonally forward if the adjacent opposing pawn is en passant-able (the opposing pawn is then captured) 
	 * </ul>
	 * If a pawn moves forward two spaces on its first move, then <a href="#{@link}">{@link Piece#enpassantable}</a> becomes
	 * true.
	 * This method handles en passant, moving the pawn executing en passant and removing the opposing pawn from the board.
	 * A pawn cannot move backward.
	 */
	public boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board) {
		
		int xStart = 8 - rankStart;
		int yStart = fileStart - 1;
		int xEnd = 8 - rankEnd;
		int yEnd = fileEnd - 1;
		
		Piece target = board[xEnd][yEnd];
		
		if(xStart == xEnd && yStart == yEnd)
			return false;
		
		if(target == null && yStart == yEnd) {
			if(white) {
				//white Pawn's first move
				if(rankStart == 2) {
					if((xEnd == xStart - 1) || xEnd == xStart - 2) {
						if((xEnd == (xStart - 2)) && board[xStart - 1][yStart] != null)
							return false;
						firstMove = false;
						if(xEnd == xStart - 2)
                            enpassantable = true;
						return true;
					}
				}
				if(xEnd == xStart - 1) {
					firstMove=false;
					return true;
				}
				return false;
			}
			else {
				//black Pawn's first move
				if(rankStart == 7) {
					if((xEnd == xStart + 1) || xEnd == xStart + 2) {
						if((xEnd == (xStart + 2)) && board[xStart + 1][yStart] != null)
							return false;
						firstMove = false;
						if(xEnd == xStart + 2)
                            enpassantable = true;						
						return true;
					}
				}
				if(xEnd == xStart + 1) {
					firstMove = false;
					return true;
				}
				return false;
			}
		}
		else {
			//enPassant checks
			if(white && xStart == 3 && xEnd == 2 && Math.abs(yStart - yEnd) == 1) {
				if(board[xEnd][yEnd] == null) {
					Piece pawn = board[xEnd + 1][yEnd];
					if(pawn!=null && pawn.enpassantable == true && !pawn.white) {
						board[xEnd + 1][yEnd] = null;
						firstMove=false;
						return true;
					}
				}
			}
			else if(!white && xStart == 4 && xEnd == 5 && Math.abs(yStart - yEnd) == 1) {
				if(board[xEnd][yEnd] == null) {
					Piece pawn = board[xEnd - 1][yEnd];
					if(pawn!=null && pawn.enpassantable == true && pawn.white) {
						board[xEnd - 1][yEnd] = null;
						firstMove=false;
						return true;
					}
				}
			}
			
			if(target == null)
				return false;
			
			if(white && target.white)
				return false;
			if(!white && !target.white)
				return false;
			
			if((yStart + 1) == yEnd || (yStart - 1) == yEnd)
				if((white && ((xStart - 1) == xEnd)) || (!white && ((xStart + 1) == xEnd))) {
					firstMove=false;
					return true;
				}
		}
		
		return false;
	}
	/**
	 * Returns a String "wP" (if the pawn is white) or "bP" (if it is black) 
	 */
	public String name() {
		return this.name;
	}
}
