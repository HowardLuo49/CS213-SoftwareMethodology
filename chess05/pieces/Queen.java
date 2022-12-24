package pieces;
/**
 * Queen is a subclass of <a href="#{@link}">{@link Piece}</a> that handles movement behavior of a queen.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Queen extends Piece{
	/**
	 * <code>String</code> that signifies that this <a href="#{@link}">{@link Piece}</a> is a queen
	 * "wQ" signifies a white queen, while "bQ" signifies a black queen.
	 */
	private String name = "Q";
	/**
	 * Constructor that creates a queen of the specified color.
	 * @param white the desired color of the piece
	 */
	public Queen(boolean white) {
		super(white);
		
		if(white)
			this.name = "w" + this.name;
		else
			this.name = "b" + this.name;
	}
	/**
	 * Method that determines if the move is legal, following the logic of queen movement.
	 * 
	 * A legal move, of any distance, can be diagonal, horizontal, or vertical to the starting position, 
	 * but there cannot be an obstructing piece in the path of the queen.
	 */
	public boolean legal(int fileStart, int rankStart, int fileEnd, int rankEnd, Piece[][] board) {
		
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		boolean upLeft = false;
		boolean upRight = false;
		boolean downLeft = false;
		boolean downRight = false;
		
		int xStart = 8 - rankStart;
		int yStart = fileStart - 1;
		int xEnd = 8 - rankEnd;
		int yEnd = fileEnd - 1;
		
		Piece target = board[xEnd][yEnd];
		
		if(fileStart == fileEnd && rankStart == rankEnd)
			return false;
		
		//lateral movement
		if(fileStart > fileEnd && rankStart == rankEnd)
			left = true;
		if(fileStart < fileEnd && rankStart == rankEnd)
			right = true;
		if(rankStart > rankEnd && fileStart == fileEnd)
			down = true;
		if(rankStart < rankEnd && fileStart == fileEnd)
			up = true;
		
		//diagonal movement
		if(Math.abs(fileStart - fileEnd) == Math.abs(rankStart - rankEnd)) {
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
		}
		
		if(!(up||down||left||right||downLeft||upLeft||downRight||upRight))
			return false;
		
		if(target != null) {
			if(white && target.white)
				return false;
			if(!white && !target.white)
				return false;
		}
		
		//check for intercepting pieces between queen and target square
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
	 * Returns a String "wQ" (if the queen is white) or "bQ" (if it is black) 
	 */
	public String name() {
		return this.name;
	}
}
