package pieces;

import chess.Chess;
/**
 * King is a subclass of <a href="#{@link}">{@link Piece}</a> that handles movement behavior of a king.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class King extends Piece {
	/**
	 * <code>String</code> that signifies that this <a href="#{@link}">{@link Piece}</a> is a king
	 * "wK" signifies a white king, while "bK" signifies a black king.
	 */
	private String name = "K";
	/**
	 * Constructor that creates a king of the specified color.
	 * @param white the desired color of the piece
	 */
	public King(boolean white) {
		super(white);
		
		if(white)
			this.name = "w" + this.name;
		else
			this.name = "b" + this.name;
	}
	/**
	 * Method that determines if the move is legal, following the logic of king movement.
	 * 
	 * The king can legally move one space in any direction.
	 * If it is the king's first move, it is not under check, there are no pieces between
	 * the king and a rook who has not yet moved, and the king moving to any of the squares
	 * between itself and rook would not result in check, then the king can castle. 
	 * If it is a short castle, then the king moves two spaces to the right, 
	 * and the rook moves to the space directly to the left of the king.
	 * If it is a long castle, then the king moves two spaces to the right,
	 * with the rook moving to the space directly on the right of the king.
	 * This method sets <a href="#{@link}">{@link Piece#firstMove}</a> to false
	 * to signify that it is no longer a candidate for castling.
	 */
	public boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board) {

		int xStart = 8 - rankStart;
		int yStart = fileStart - 1;
		int xEnd = 8 - rankEnd;
		int yEnd = fileEnd - 1;
		
		Piece target = board[xEnd][yEnd];
		
		if(xStart == xEnd && yStart == yEnd)
			return false;
		
		//castling
		if(firstMove && xStart == xEnd && !Chess.checkStatus()) {
			if(yEnd == yStart + 2) {
				for(int i = yStart + 1; i < 7; i++) {
					Piece interception = board[xEnd][i];
					if(interception != null)
						return false;
				}
				
				for(int k = 1; k <= 2; k++) {
					for(int i = 0; i <= 7; i++) {
						for(int j = 0; j <= 7; j++) {
							Piece attackingPiece = board[i][j];
							if(attackingPiece != null && this.white != attackingPiece.white && attackingPiece.legal(1 + j, 8 - i, 1 + (yStart + k), 8 - xStart, board))
								return false;
						}
					}
				}
				
				Piece rook = board[xEnd][7];
				if(rook == null)
                    return false;
				if(rook.firstMove == false)
					return false;
				
				board[xEnd][yEnd-1] = rook;
				board[xEnd][7] = null;
				firstMove = false;
				rook.firstMove = false;
				return true;
			}
			else if(yEnd == yStart - 2) {
				for(int i = yStart - 1; i > 0; i--){
					Piece interception = board[xEnd][i];
					if(interception != null)
						return false;
				}
				
				for(int k = 1; k <= 2; k++) {
					for(int i = 0; i <= 7; i++) {
						for(int j = 0; j <= 7; j++) {
							Piece attackingPiece = board[i][j];
							if(attackingPiece != null && this.white != attackingPiece.white && attackingPiece.legal(1 + j, 8 - i, 1 + (yStart - k), 8 - xStart, board))
								return false;
						}
					}
				}
				
				Piece rook = board[xEnd][0];
				if(rook == null)
                    return false;
				if(rook.firstMove == false)
					return false;
				
				board[xEnd][yEnd + 1] = rook;
				board[xEnd][0] = null;
				firstMove = false;
				rook.firstMove = false;
				return true;
			}
		}
		
		if(target != null) {
			if(white && target.white)
				return false;
			if(!white && !target.white)
				return false;
		}
		
		//move stays within range of 1 square
		if(Math.abs(xStart - xEnd) > 1 || Math.abs(yStart - yEnd) > 1)
			return false;
		
		firstMove=false;
		return true;
	}
	/**
	 * Returns a String "wK" (if the king is white) or "bK" (if it is black) 
	 */
	public String name() {
		return this.name;
	}
}
