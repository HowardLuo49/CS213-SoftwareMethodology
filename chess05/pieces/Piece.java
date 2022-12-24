package pieces;

/**
 * Abstract base class of all chess pieces, which defines the basic
 * attributes and functionalities of the pieces.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public abstract class Piece {

	/**
	 * <code>boolean</code> value that represents the color of the piece.
	 * True means the piece is white, false means it is black.
	 */
	public boolean white;
	/**
	 * <code>boolean</code> value that shows if the piece has made its first move.
	 * If true, the piece has yet to move. If false, the piece has already been moved on a previous turn.
	 * For use in <a href="#{@link}">{@link King}</a> and <a href="#{@link}">{@link Rook}</a> (castling)
	 * and <a href="#{@link}">{@link Pawn}</a> (en passant)
	 */
	public boolean firstMove = true;
	/**
	 * <code>boolean</code> that determines if the piece is an eligible target for en passant.
	 * @see Pawn
	 */
	public boolean enpassantable = false;
	/**
	 * Constructor that creates a piece of the specified color.
	 * @param white the desired color of the piece
	 */
	public Piece(boolean white) {
		this.white = white;
	}
	/**
	 * Abstract method that determines if the specified move is legal for the selected piece.
	 * 
	 * The move is illegal if the inputed move selects an empty square instead of a piece.
	 * The move is illegal if the move is attempting to capture a friendly piece.
	 * 
	 * @param fileStart file(column) of the piece to be moved
	 * @param rankStart rank(row) of the piece to be moved
	 * @param fileEnd file(column) of the space that the specified piece is to be moved to
	 * @param rankEnd rank(row) of the space that the specified piece is to be moved to 
	 * @param board the board state
	 * @return true if the move is legal, false if it is not
	 */
	public abstract boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board);
	/**
	 * Abstract method that returns a <code>String</code> with the color and type of the piece
	 * @return String formatted "colorType"
	 */
	public abstract String name();
}
