package chess;

import java.util.ArrayList;
import java.util.Scanner;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
/**
 * Class that is responsible for the simulation of the chess game and the enforcement of its rules.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Chess {
	
	/**
	 * 2D <a href="#{@link}">{@link Piece}</a>s array that represents the board
	 */
	static Piece[][] board;
	/**
	 * <code>boolean</code> value that represents the color whose turn it is
	 * True is white's turn, false is black's turn.
	 */
	static boolean whiteTurn;
	/**
	 * <code>boolean</code> value that is used to determine if the opposing king is under check
	 */
	static boolean check = false;
	
	/**
	 * Main method of the <a href="#{@link}">{@link Chess} class that simulates a chess game.
	 * 
	 * This method takes in move input from the user, and plays it if it is legal.
	 * This method is also responsible for notifying the user of illegal
	 * inputs, passing turn priority, notifying the user if the opposing king is under check,
	 * notifying the user of the winner of the game, whether it be through checkmate or
	 * resignation, and handling draws.
	 */
	public static void main(String[] args) {
		
		boolean whiteTurn = true;
		boolean drawOffer = false;
		
		Scanner s = new Scanner(System.in);
		
		createBoard();
		
		while(true) {
			printBoard();
			
			updateEnPassant(whiteTurn);
			if(whiteTurn) {
				System.out.print("\nWhite's move: ");
			}
			else {
				System.out.print("\nBlack's move: ");
			}
			
			String input = s.nextLine().toLowerCase();
			
			while(!validMoveInput(input)) {
				System.out.println("Illegal move, try again. ");
				if(whiteTurn)
					System.out.println("White's move: ");
				else
					System.out.println("Black's move: ");
				
				input = s.nextLine().toLowerCase();
			}
			
			if(input.equals("resign")) {
				if(whiteTurn)
					System.out.println("Black wins");
				else
					System.out.println("White wins");
				s.close();
				return;
			}
			else if(input.contains("draw")) {
				if(drawOffer) {
					s.close();
					return;
				}
				else
					drawOffer = true;
			}
			else {
				drawOffer = false;
			}
			
			String start = input.substring(0, 2);
			String end = input.substring(3);
			
			int fileStart = start.charAt(0) - 96;
			int rankStart = Integer.valueOf(start.substring(1, 2));
			int fileEnd = end.charAt(0) - 96;
			int rankEnd = Integer.valueOf(end.substring(1, 2));
						
			while(!validMove(fileStart, rankStart, fileEnd, rankEnd, whiteTurn)) {
				System.out.println("Illegal move, try again. ");
				if(whiteTurn)
					System.out.println("White's move: ");
				else
					System.out.println("Black's move: ");
				
				input = s.nextLine().toLowerCase();
				while(!validMoveInput(input)) {
					System.out.println("Illegal move, try again. ");
					if(whiteTurn)
						System.out.println("White's move: ");
					else
						System.out.println("Black's move: ");
					input = s.nextLine().toLowerCase();
				}
				
				if(input.equals("resign")) {
					if(whiteTurn)
						System.out.println("Black wins");
					else
						System.out.println("White wins");
					s.close();
					return;
				}
				else if(input.contains("draw")) {
					if(drawOffer) {
						System.out.println("draw");
					}
					else
						drawOffer = true;
				}
				else {
					drawOffer = false;
				}
				
				start = input.substring(0, 2);
				end = input.substring(3);
				
				fileStart = start.charAt(0) - 96;
				rankStart = Integer.valueOf(start.substring(1, 2));
				fileEnd = end.charAt(0) - 96;
				rankEnd = Integer.valueOf(end.substring(1, 2));
			}
			
			
			Piece piece = board[8 - rankEnd][fileEnd - 1];
			if(piece instanceof Pawn) {
				if((whiteTurn && 8 - rankEnd == 0) || (!whiteTurn && 8 - rankEnd == 7)) {
					char type = 'Q';
					if(input.length() == 7)
						type = input.charAt(6);
					Piece promotion = promotion(type, whiteTurn);
					if(promotion != null)
						board[8 - rankEnd][fileEnd - 1] = promotion;
				}
			}
			
			
			for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    if(isCheck(1 + j, 8 - i, true) && !whiteTurn) {
                        System.out.println();
                        System.out.println("Check");
                        if(checkmate(true)) {
                            System.out.println();
                            printBoard();
                            System.out.println();
                            System.out.println("Checkmate\nBlack wins");
                            s.close();
                            return;
                        }
                        check = true;
                    }
                    else if(isCheck(1 + j, 8 - i, false) && whiteTurn) {
                        System.out.println();
                        System.out.println("Check");
                        if(checkmate(false)) {
                            System.out.println();
                            printBoard();
                            System.out.println();
                            System.out.println("Checkmate\nWhite wins");
                            s.close();
                            return;
                        }
                        check = true;
                    }
                }
            }
			
			
			
			System.out.println();
			whiteTurn = !whiteTurn;
		}
		
	}
	/**
	 * Instantiates a 2D <a href="#{@link}">{@link Piece}</a> array  that represents the board.
	 * Each element of the array represents a space on the board, with
	 * null elements signifying an empty space.
	 * Populates the array with pieces in their respective starting
	 * positions.
	 * 
	 * @see Bishop
	 * @see King
	 * @see Knight
	 * @see Piece
	 * @see Queen
	 * @see Rook
	 */
	public static void createBoard() {
		
		board = new Piece[8][8];
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++)
				board[i][j] = null;
		}
		
		//black
		board[0][7] = new Rook(false); //right
		board[0][6] = new Knight(false);
		board[0][5] = new Bishop(false);
		board[0][4] = new King(false);
		board[0][3] = new Queen(false);
		board[0][2] = new Bishop(false);
		board[0][1] = new Knight(false);
		board[0][0] = new Rook(false); //left
		for(int i = 0; i <=7; i++) {
			board[1][i] = new Pawn(false);
		}
		
		//white
		board[7][7] = new Rook(true); //right
		board[7][6] = new Knight(true);
		board[7][5] = new Bishop(true);
		board[7][4] = new King(true);
		board[7][3] = new Queen(true);
		board[7][2] = new Bishop(true);
		board[7][1] = new Knight(true);
		board[7][0] = new Rook(true); //left
		for(int i = 0; i <=7; i++) {
			board[6][i] = new Pawn(true);
		}
	}
	
	/**
	 * Prints the board.
	 * Empty spaces are printed, with "  " for black and "##" for white.
	 * Pieces are printed with their name.
	 * @see isWhite
	 */
	public static void printBoard() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 9; j++) {
				if(j == 8) {
					System.out.print((8 - i));
				}
				else if(board[i][j] == null) {
					if(isWhite(i, j))
						System.out.print("  ");
					else
						System.out.print("##");
				}
				else {
					System.out.print(board[i][j].name());
				}
				if(j < 8)
					System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}
	
	/**
	 * Determines if the specified space is white or black.
	 * For use in <a href="#{@link}">{@link printBoard}</a>
	 * 
	 * @param i row number of board array
	 * @param j column number of board array
	 * @return true if the space is white; false if the space is black
	 */
	public static boolean isWhite(int i, int j) {
		
		if((i == 0 || i == 2 || i == 4 || i == 6) && (j == 0 || j == 2 || j == 4 || j == 6)) {
			return true;
		}
		else if ((i == 1 || i == 3 || i == 5 || i == 7) && (j == 1 || j == 3 || j == 5 || j == 7)) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * Determines if the user's inputed move is valid, then plays the move if it is.
	 * 
	 * Checks if move is valid through <a href="#{@link}">{@link Piece#legal}</a>,
	 * in addition to checking if the move will place the friendly king under check.
	 * The move is invalid, and thus illegal, if <a href="#{@link}">{@link Piece#legal}</a> returns false
	 * or if the move would place the friendly king under check.
	 * If the move is legal and the specified destination space has an enemy piece on it, then the selected
	 * piece captures the enemy piece.
	 * The board is updated to reflect a valid move.
	 * 
	 * @param fileStart file(column) of the piece to be moved
	 * @param rankStart rank(row) of the piece to be moved
	 * @param fileEnd file(column) of the space that the specified piece is to be moved to
	 * @param rankEnd rank(row) of the space that the specified piece is to be moved to 
	 * @param whiteTurn the color whose turn it is
	 * @return true if specified move is valid, false if it is not
	 * @see Piece#legal
	 */
	public static boolean validMove(int fileStart, int rankStart, int fileEnd, int rankEnd, boolean whiteTurn) {
		if(board[8 - rankStart][fileStart - 1] == null)
			return false;
		
		Piece piece = board[8 - rankStart][fileStart - 1];
		if((piece.white && !whiteTurn) || (!piece.white && whiteTurn))
			return false;
		
		if(piece.legal(fileStart, rankStart, fileEnd, rankEnd, board)) {
			
			Piece hitPiece = board[8 - rankEnd][fileEnd - 1];
			
			board[8 - rankStart][fileStart - 1] = null;
			board[8 - rankEnd][fileEnd - 1] = piece;
			
			//make sure move doesn't place your own king under check
			for(int i = 1; i <= 8; i++) {
				for(int j = 1; j <= 8; j++) {
					if(isCheck(i, j, whiteTurn)) {
						check = false;
						board[8 - rankStart][fileStart - 1] = piece;
						board[8 - rankEnd][fileEnd - 1] = hitPiece;
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if the opposing king is under check
	 * 
	 * This method first finds the position of the opposing king. Then it determines if
	 * the specified piece could legally move to this position, which is check if true.
	 * Returns false if the specified position is an empty square or a piece that is friendly
	 * to the king.
	 * @param filePiece file(column) of specified piece
	 * @param rankPiece rank(row) of specified piece
	 * @param white color of the opposing king
	 * @return true if the specified piece puts the opposing king under check, false if not
	 * @see kingLocation
	 */
	public static boolean isCheck(int filePiece, int rankPiece, boolean white) {
		int[] kingLocation = kingLocation(white);
		Piece piece = board[8 - rankPiece][filePiece - 1];
		int fileEnd = kingLocation[1] + 1;
		int rankEnd = 8 - kingLocation[0];
		
		if(piece == null || piece.white == white)
			return false;
		boolean pieceFM = piece.firstMove;
		if(piece.legal(filePiece, rankPiece, fileEnd, rankEnd, board)) {
			check = true;
			piece.firstMove=pieceFM;
			return true;
		}
		check = false;
		return false;
	}
	
	/**
	 * Determines the position of the king of specified color.
	 * 
	 * Searches through the board array until the specified king is found.
	 * Returns null if there is no king on the board,
	 * though this situation is not possible in a legal game.
	 * @param white the color of the king 
	 * @return array with rank and file of the specified king, null if there is no king
	 */
	public static int[] kingLocation(boolean white) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Piece piece = board[i][j];
				if(piece != null && piece instanceof King) {
					int[] result = new int[2];
					result[0] = i;
					result[1] = j;
					if((white && piece.white) || (!white && !piece.white))
						return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the value of the check field.
	 * @return true if check is true, false if not
	 * @see Chess#check
	 */
	public static boolean checkStatus() {
		return check;
	}
	
	/**
	 * Determines if the input from the user is valid.
	 * 
	 * Valid inputs are: resign, draw?, draw, 
	 * and any move (a-h)(1-8) (a-h)(1-8).
	 * This method does not determine if the move is actually legal,
	 * only if it has the correct format.
	 * This method prevents the user from moving a piece to a space
	 * that is outside of the board.
	 * @param s input from the user
	 * @return true if the input is valid, false if not
	 */
	public static boolean validMoveInput(String s) {
		
		if(s.equals("resign") || s.contains("draw"))
			return true;
		
		if(s.length() == 0 || s.length() < 5)
			return false;
		
		if(s.charAt(0) < 'a' || s.charAt(0) > 'h')
			return false;
		if(s.charAt(3) < 'a' || s.charAt(3) > 'h')
			return false;
		
		if(s.charAt(1) < '1' || s.charAt(1) > '8')
			return false;
		if(s.charAt(4) < '1' || s.charAt(4) > '8')
			return false;
		
		return true;
	}
	/**
	 * Determines if the opposing king is under checkmate.
	 * 
	 * The method finds the position of the specified king. Then, determines
	 * if the king can make any legal moves. A legal move cannot put the king
	 * in check, so if the king has a legal move, then it is not under checkmate 
	 * and returns false. Otherwise, finds positions(rank & file) of all pieces that are checking
	 * the king (attackers). If there are 2+ attackers, then it must be checkmate.
	 * Then, determines if the opponent can prevent checkmate 
	 * by capturing or blocking the attacker by testing all possible legal moves 
	 * that the opponent can make and returning false if such a move exists. 
	 * Returns true at the end of the method, because at this point there are no legal moves
	 * for the opponent to play, so it is checkmate. 
	 * 
	 * @param white color of the opposing king
	 * @return true if opposing king is under checkmate, false if not
	 * @see kingLocation
	 * @see isCheck
	 * @see legalKingMove
	 */
	public static boolean checkmate(boolean white) {
		int[] kingLocation = kingLocation(white);

		for (int i = kingLocation[0] - 1; i <= kingLocation[0] + 1; i++) {
			for (int j = kingLocation[1] - 1; j <= kingLocation[1] + 1; j++) {
				if (legalKingMove(1 + kingLocation[1], 8 - kingLocation[0], 1 + j, 8 - i, white))
					return false;
			}
		}

		ArrayList<int[]> attackers = new ArrayList<>();

		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				int[] attackingPieceLocation = new int[2];
				attackingPieceLocation[0] = i;
				attackingPieceLocation[1] = j;
				Piece attackingPiece = board[i][j];

				if (attackingPiece != null) {
					boolean aPFM = attackingPiece.firstMove;
					if(attackingPiece.legal(1 + j, 8 - i, 1 + kingLocation[1], 8 - kingLocation[0], board)) {
						attackingPiece.firstMove = aPFM;
						attackers.add(attackingPieceLocation);
					}
				}
			}
		}
		if (attackers.size() > 1)
			return true;
		// double check
		if (!attackers.isEmpty()) {
            int[] temp = attackers.get(0);

            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    Piece attackerOfAttackingPiece = board[i][j];
                    if(attackerOfAttackingPiece instanceof King && attackerOfAttackingPiece.white == white) {
                        Piece attackingPiece = board[temp[0]][temp[1]];
                        board[i][j] = null;
                        board[temp[0]][temp[1]] = attackerOfAttackingPiece;
                        if(isCheck(1 + j, 8 - i, white)) {
                            board[i][j] = attackerOfAttackingPiece;
                            board[temp[0]][temp[1]] = attackingPiece;
                            return false;
                        }
                        board[i][j] = attackerOfAttackingPiece;
                        board[temp[0]][temp[1]] = attackingPiece;
                    }
                    else if (attackerOfAttackingPiece != null) {
                    	boolean aAPFM = attackerOfAttackingPiece.firstMove;
                    	if(attackerOfAttackingPiece.legal(1 + j, 8 - i, 1 + temp[1], 8 - temp[0], board)) {
                        	attackerOfAttackingPiece.firstMove = aAPFM;
                    		return false;
                    	}
                    }
                }
            }
        }

		// checks for pieces that can block check
				for (int i = 0; i <= 7; i++) {
					for (int j = 0; j <= 7; j++) {
						Piece blockerOfAttackingPiece = board[i][j];
						
						if (blockerOfAttackingPiece != null && blockerOfAttackingPiece.white == white&& !(blockerOfAttackingPiece instanceof King)) {
							for (int a = 0; a <= 7; a++) {
								for (int b = 0; b <= 7; b++) {
									Piece square = board[a][b];
									boolean tempenpassant = false;
		                            if(square instanceof Pawn && square.enpassantable)
		                                tempenpassant = true;
									int xAttacker = attackers.get(0)[0];
									int yAttacker = attackers.get(0)[1];
									
									
									
									Piece[][] tempBoard = new Piece[8][8];
			                    	for(int c = 0; c < 8; c++) {
			                    		for(int d = 0; d < 8; d++) {
			                    			tempBoard[c][d] = board[c][d];
			                    		}
			                    	}
			                    	boolean bAPFM = blockerOfAttackingPiece.firstMove;
			                    	if(blockerOfAttackingPiece.legal(1 + j, 8 - i, 1 + b, 8 - a, board)) {
				                    	blockerOfAttackingPiece.firstMove = bAPFM;
			                    		board[a][b] = blockerOfAttackingPiece;
										board[i][j] = null;
										if (!isCheck(1 + yAttacker, 8 - xAttacker, white)) {
											board[a][b] = square;
											board[i][j] = blockerOfAttackingPiece;
											board = tempBoard;
											return false;
										}
										board = tempBoard;
										board[a][b] = square;
										board[i][j] = blockerOfAttackingPiece;
									}
									if(square instanceof Pawn && !tempenpassant)
		                                square.enpassantable = false;
								}
							}
						}
					}
				}

				return true;
			}
	
	/**
	 * Determines if the king can make a legal move to the specified position.
	 * 
	 * Attempting to move the king beyond the limits of the board is illegal.
	 * Not moving the king at all is illegal.
	 * Uses king's <a href="#{@link}">{@link King#legal}</a> to temporarily play the move
	 * if it is legal movement.
	 * If this movement places the king under check, then it is illegal.
	 * @param fileStart current file of the king
	 * @param rankStart current rank of the king
	 * @param fileEnd file of the position that king is to be moved to
	 * @param rankEnd rank of the position that king is to be moved to
	 * @param white the color of the king
	 * @return true if the specified move is legal, false if not.
	 * @see King
	 * @see isCheck
	 */
	public static boolean legalKingMove(int fileStart, int rankStart, int fileEnd, int rankEnd, boolean white) {
		
		if(fileStart == fileEnd && rankStart == rankEnd)
			return false;
		if(fileEnd < 1 || fileEnd > 8 || rankEnd < 1 || rankEnd > 8)
			return false;
		
		Piece king = board[8 - rankStart][fileStart - 1];
		Piece square = board[8 - rankEnd][fileEnd - 1];
		
		if(square != null && square.white == white)
			return false;
		boolean kFirstMove = king.firstMove;
		if(king.legal(fileStart, rankStart, fileEnd, rankEnd, board)) {
			board[8 - rankStart][fileStart - 1] = null;
			board[8 - rankEnd][fileEnd - 1] = king;
			check = false;
			king.firstMove = kFirstMove;
			for(int i = 1; i <= 8; i++) {
				for(int j = 1; j <= 8; j++) {
					if(isCheck(i, j, white)) {
						board[8 - rankStart][fileStart - 1] = king;
						board[8 - rankEnd][fileEnd - 1] = square;
						return false;
					}
				}
			}
			
			board[8 - rankStart][fileStart - 1] = king;
			board[8 - rankEnd][fileEnd - 1] = square;
			check = false;
			return true;
		}
		else {
			check = false;
			return false;
		}
	}
	
	/**
	 * Updates the status of all previously en passant-able <a href="#{@link}">{@link Piece}</a>s of the specified color.
	 * 
	 * This method ensures that a <a href="#{@link}">{@link Pawn}</a> is en passant-able for exactly one turn:
	 * Any <a href="#{@link}">{@link Pawn}</a> that bcomes en passant-able during a turn is no longer
	 * en passant-able at the start of the next friendly turn.
	 * Only <code>en passant</code> of <a href="#{@link}">{@link Pawn}</a>s w/ file 4(white) or file 5(black) are set to false 
	 * because an en passant-able <a href="#{@link}">{@link Pawn}</a> can only be found in these two rows.
	 * This method does not have to check if the piece is actually a pawn, because all pieces have
	 * <a href="#{@link}">{@link Piece#enpassantable}</a>, which creates no issue for non-pawn pieces
	 * while maintaining correct en passant logic for pawns.
	 * @param whiteTurn the color whose turn it is
	 * @see Pawn
	 */
	public static void updateEnPassant(boolean whiteTurn) {
		if(whiteTurn) {
			for(int i = 0; i < 8; i++) {
				if(board[4][i] != null && board[4][i].white)
					board[4][i].enpassantable = false;
			}
		}
		else {
			for(int i = 0; i < 8; i++) {
				if(board[3][i] != null && !board[3][i].white)
					board[3][i].enpassantable = false;
			}
		}
	}
	
	/**
	 * Creates a new piece of specified color that a pawn will be promoted to.
	 * 
	 * This method will return a Queen unless otherwise specified.
	 * The returned piece will be of the same color as the promoting pawn.
	 * @param pieceType the type of piece that the pawn will be promoted to
	 * @param white the color of the promoting pawn
	 * @return a <a href="#{@link}">{@link Queen}</a>, or
	 * a <a href="#{@link}">{@link Knight}</a>,
	 * <a href="#{@link}">{@link Rook}</a>,
	 * or <a href="#{@link}">{@link Bishop}</a> if specified
	 * @see Piece
	 * @see Queen
	 * @see Knight
	 * @see Rook
	 * @see Bishop
	 */
	public static Piece promotion(char pieceType, boolean white) {
		
		if(pieceType == 'N' || pieceType == 'n')
			return new Knight(white);
		if(pieceType == 'R' || pieceType == 'r')
			return new Rook(white);
		if(pieceType == 'B' || pieceType == 'b')
			return new Bishop(white);
		
		return new Queen(white);
		
	}
	
}
