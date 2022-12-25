package com.example.android05;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import chess.Spot;
import pieces.*;

public class Replay extends AppCompatActivity {

    public static Spot[][] chessBoard;
    public static boolean whiteTurn;

    public TextView[][] displayBoard;
    public TextView[][] displayBoardB;
    public Button move;

    public ArrayList<String> moveList;
    public int moveNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chessBoard = new Spot[8][8];
        displayBoard = new TextView[8][8];
        displayBoardB = new TextView[8][8];
        whiteTurn = true;
        moveNum = 1;
        Intent intent=getIntent();
        moveList = intent.getStringArrayListExtra("moves");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        setupBoard();

        move = (Button) findViewById(R.id.move);

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move.setEnabled(false);
                if (moveNum > moveList.size()) {
                    Toast.makeText(Replay.this,"End of game",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Replay.this,Home.class);
                    startActivity(intent);
                }
                else {
                    String move = moveList.get(moveNum - 1);

                    int parts = 1;
                    for(int i = 0; i < move.length(); i++) {
                        if(move.substring(i, i + 1).equals(","))
                            parts++;
                    }

                    if(parts == 2) {
                        String start = move.substring(0, 2);
                        String end = move.substring(3, 5);

                        int startX = Character.getNumericValue(start.charAt(0));
                        int startY = Character.getNumericValue(start.charAt(1));
                        int endX = Character.getNumericValue(end.charAt(0));
                        int endY = Character.getNumericValue(end.charAt(1));

                        Piece piece = chessBoard[startX][startY].getPiece();
                        Spot endPosition = chessBoard[endX][endY];
                        if(piece instanceof Pawn) {
                            //enpassant
                            if(endY == 2 && whiteTurn && chessBoard[endX][endY].getPiece() == null && endX != startX) {
                                displayBoard[endX][startY].setBackgroundResource(0);
                                chessBoard[endX][startY].setPiece(null);
                            }
                            if(endY == 5 && !whiteTurn && chessBoard[endX][endY].getPiece() == null && endX != startX) {
                                displayBoard[endX][startY].setBackgroundResource(0);
                                chessBoard[endX][startY].setPiece(null);
                            }
                        }
                        
                        //castling
                        if(piece instanceof King) {
                            if(whiteTurn && startX == 4 && startY == 7) {
                                if(endX == 2 && endY == 7) {
                                    displayBoard[0][7].setBackgroundResource(0);
                                    displayBoard[3][7].setBackgroundResource(R.drawable.wrook);
                                    chessBoard[3][7].setPiece(chessBoard[0][7].getPiece());
                                    chessBoard[0][7].setPiece(null);
                                }
                                if(endX == 6 && endY == 7) {
                                    displayBoard[7][7].setBackgroundResource(0);
                                    displayBoard[5][7].setBackgroundResource(R.drawable.wrook);
                                    chessBoard[5][7].setPiece(chessBoard[7][7].getPiece());
                                    chessBoard[7][7].setPiece(null);
                                }
                            }
                            if(!whiteTurn && startX == 4 && startY == 0) {
                                if(endX == 2 && endY == 0) {
                                    displayBoard[0][0].setBackgroundResource(0);
                                    displayBoard[3][0].setBackgroundResource(R.drawable.brook);
                                    chessBoard[3][0].setPiece(chessBoard[0][0].getPiece());
                                    chessBoard[0][0].setPiece(null);
                                }
                                if(endX == 6 && endY == 0) {
                                    displayBoard[7][0].setBackgroundResource(0);
                                    displayBoard[5][0].setBackgroundResource(R.drawable.brook);
                                    chessBoard[5][0].setPiece(chessBoard[7][0].getPiece());
                                    chessBoard[7][0].setPiece(null);
                                }
                            }
                        }

                        endPosition.setPiece(piece);
                        displayBoard[endX][endY].setBackgroundResource(getPicture(piece));
                        chessBoard[startX][startY].setPiece(null);
                        displayBoard[startX][startY].setBackgroundResource(0);
                    }
                    else if(parts == 3) {
                        String start = move.substring(0, 2);
                        String end = move.substring(3, 5);
                        String promotion = move.substring(6, 7);

                        int startX = Character.getNumericValue(start.charAt(0));
                        int startY = Character.getNumericValue(start.charAt(1));
                        int endX = Character.getNumericValue(end.charAt(0));
                        int endY = Character.getNumericValue(end.charAt(1));

                        if (promotion.equals("Q")){
                            chessBoard[endX][endY].setPiece(new Queen(!whiteTurn));
                            if (whiteTurn)
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.wqueen);
                            else
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.bqueen);
                        }
                        else if (promotion.equals("B")){
                            chessBoard[endX][endY].setPiece(new Bishop(!whiteTurn));
                            if (whiteTurn)
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.wbishop);
                            else
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.bbishop);
                        }
                        else if (promotion.equals("R")){
                            chessBoard[endX][endY].setPiece(new Rook(!whiteTurn));
                            if (whiteTurn)
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.wrook);
                            else
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.brook);
                        }
                        else if (promotion.equals("K")) {
                            chessBoard[endX][endY].setPiece(new Knight(!whiteTurn));
                            if (whiteTurn)
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.wknight);
                            else
                                displayBoard[endX][endY].setBackgroundResource(R.drawable.bknight);
                        }

                        chessBoard[startX][startY].setPiece(null);
                        displayBoard[startX][startY].setBackgroundResource(0);
                    }
                }
                whiteTurn = !whiteTurn;
                moveNum++;
                move.setEnabled(true);
            }
        });
    }

    public void createBoard(Spot[][] board) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = new Spot(i, j, null);
            }
        }

        //black
        board[0][0] = new Spot(0, 0, new Rook(false));//left
        board[1][0] = new Spot(1, 0, new Knight(false));
        board[2][0] = new Spot(2, 0, new Bishop(false));
        board[3][0] = new Spot(3, 0, new Queen(false));
        board[4][0] = new Spot(4, 0, new King(false));
        board[5][0] = new Spot(5, 0, new Bishop(false));
        board[6][0] = new Spot(6, 0, new Knight(false));
        board[7][0] = new Spot(7, 0, new Rook(false));//right
        for(int i = 0; i <=7; i++) {
            board[i][1] = new Spot(i, 1, new Pawn(false));
        }

        //white
        board[0][7] = new Spot(0, 7, new Rook(true));//left
        board[1][7] = new Spot(1, 7, new Knight(true));
        board[2][7] = new Spot(2, 7, new Bishop(true));
        board[3][7] = new Spot(3, 7, new Queen(true));
        board[4][7] = new Spot(4, 7, new King(true));
        board[5][7] = new Spot(5, 7, new Bishop(true));
        board[6][7] = new Spot(6, 7, new Knight(true));
        board[7][7] = new Spot(7, 7, new Rook(true));//right
        for(int i = 0; i <=7; i++) {
            board[i][6] = new Spot(i, 6, new Pawn(true));
        }
    }

    private void setupBoard() {
        createBoard(chessBoard);
        displayBoard[0][0] = (TextView) findViewById(R.id.F00);
        displayBoard[1][0] = (TextView) findViewById(R.id.F10);
        displayBoard[2][0] = (TextView) findViewById(R.id.F20);
        displayBoard[3][0] = (TextView) findViewById(R.id.F30);
        displayBoard[4][0] = (TextView) findViewById(R.id.F40);
        displayBoard[5][0] = (TextView) findViewById(R.id.F50);
        displayBoard[6][0] = (TextView) findViewById(R.id.F60);
        displayBoard[7][0] = (TextView) findViewById(R.id.F70);
        displayBoard[0][1] = (TextView) findViewById(R.id.F01);
        displayBoard[1][1] = (TextView) findViewById(R.id.F11);
        displayBoard[2][1] = (TextView) findViewById(R.id.F21);
        displayBoard[3][1] = (TextView) findViewById(R.id.F31);
        displayBoard[4][1] = (TextView) findViewById(R.id.F41);
        displayBoard[5][1] = (TextView) findViewById(R.id.F51);
        displayBoard[6][1] = (TextView) findViewById(R.id.F61);
        displayBoard[7][1] = (TextView) findViewById(R.id.F71);
        displayBoard[0][2] = (TextView) findViewById(R.id.F02);
        displayBoard[1][2] = (TextView) findViewById(R.id.F12);
        displayBoard[2][2] = (TextView) findViewById(R.id.F22);
        displayBoard[3][2] = (TextView) findViewById(R.id.F32);
        displayBoard[4][2] = (TextView) findViewById(R.id.F42);
        displayBoard[5][2] = (TextView) findViewById(R.id.F52);
        displayBoard[6][2] = (TextView) findViewById(R.id.F62);
        displayBoard[7][2] = (TextView) findViewById(R.id.F72);
        displayBoard[0][3] = (TextView) findViewById(R.id.F03);
        displayBoard[1][3] = (TextView) findViewById(R.id.F13);
        displayBoard[2][3] = (TextView) findViewById(R.id.F23);
        displayBoard[3][3] = (TextView) findViewById(R.id.F33);
        displayBoard[4][3] = (TextView) findViewById(R.id.F43);
        displayBoard[5][3] = (TextView) findViewById(R.id.F53);
        displayBoard[6][3] = (TextView) findViewById(R.id.F63);
        displayBoard[7][3] = (TextView) findViewById(R.id.F73);
        displayBoard[0][4] = (TextView) findViewById(R.id.F04);
        displayBoard[1][4] = (TextView) findViewById(R.id.F14);
        displayBoard[2][4] = (TextView) findViewById(R.id.F24);
        displayBoard[3][4] = (TextView) findViewById(R.id.F34);
        displayBoard[4][4] = (TextView) findViewById(R.id.F44);
        displayBoard[5][4] = (TextView) findViewById(R.id.F54);
        displayBoard[6][4] = (TextView) findViewById(R.id.F64);
        displayBoard[7][4] = (TextView) findViewById(R.id.F74);
        displayBoard[0][5] = (TextView) findViewById(R.id.F05);
        displayBoard[1][5] = (TextView) findViewById(R.id.F15);
        displayBoard[2][5] = (TextView) findViewById(R.id.F25);
        displayBoard[3][5] = (TextView) findViewById(R.id.F35);
        displayBoard[4][5] = (TextView) findViewById(R.id.F45);
        displayBoard[5][5] = (TextView) findViewById(R.id.F55);
        displayBoard[6][5] = (TextView) findViewById(R.id.F65);
        displayBoard[7][5] = (TextView) findViewById(R.id.F75);
        displayBoard[0][6] = (TextView) findViewById(R.id.F06);
        displayBoard[1][6] = (TextView) findViewById(R.id.F16);
        displayBoard[2][6] = (TextView) findViewById(R.id.F26);
        displayBoard[3][6] = (TextView) findViewById(R.id.F36);
        displayBoard[4][6] = (TextView) findViewById(R.id.F46);
        displayBoard[5][6] = (TextView) findViewById(R.id.F56);
        displayBoard[6][6] = (TextView) findViewById(R.id.F66);
        displayBoard[7][6] = (TextView) findViewById(R.id.F76);
        displayBoard[0][7] = (TextView) findViewById(R.id.F07);
        displayBoard[1][7] = (TextView) findViewById(R.id.F17);
        displayBoard[2][7] = (TextView) findViewById(R.id.F27);
        displayBoard[3][7] = (TextView) findViewById(R.id.F37);
        displayBoard[4][7] = (TextView) findViewById(R.id.F47);
        displayBoard[5][7] = (TextView) findViewById(R.id.F57);
        displayBoard[6][7] = (TextView) findViewById(R.id.F67);
        displayBoard[7][7] = (TextView) findViewById(R.id.F77);

        displayBoardB[0][0] = (TextView) findViewById(R.id.B00);
        displayBoardB[1][0] = (TextView) findViewById(R.id.B10);
        displayBoardB[2][0] = (TextView) findViewById(R.id.B20);
        displayBoardB[3][0] = (TextView) findViewById(R.id.B30);
        displayBoardB[4][0] = (TextView) findViewById(R.id.B40);
        displayBoardB[5][0] = (TextView) findViewById(R.id.B50);
        displayBoardB[6][0] = (TextView) findViewById(R.id.B60);
        displayBoardB[7][0] = (TextView) findViewById(R.id.B70);
        displayBoardB[0][1] = (TextView) findViewById(R.id.B01);
        displayBoardB[1][1] = (TextView) findViewById(R.id.B11);
        displayBoardB[2][1] = (TextView) findViewById(R.id.B21);
        displayBoardB[3][1] = (TextView) findViewById(R.id.B31);
        displayBoardB[4][1] = (TextView) findViewById(R.id.B41);
        displayBoardB[5][1] = (TextView) findViewById(R.id.B51);
        displayBoardB[6][1] = (TextView) findViewById(R.id.B61);
        displayBoardB[7][1] = (TextView) findViewById(R.id.B71);
        displayBoardB[0][2] = (TextView) findViewById(R.id.B02);
        displayBoardB[1][2] = (TextView) findViewById(R.id.B12);
        displayBoardB[2][2] = (TextView) findViewById(R.id.B22);
        displayBoardB[3][2] = (TextView) findViewById(R.id.B32);
        displayBoardB[4][2] = (TextView) findViewById(R.id.B42);
        displayBoardB[5][2] = (TextView) findViewById(R.id.B52);
        displayBoardB[6][2] = (TextView) findViewById(R.id.B62);
        displayBoardB[7][2] = (TextView) findViewById(R.id.B72);
        displayBoardB[0][3] = (TextView) findViewById(R.id.B03);
        displayBoardB[1][3] = (TextView) findViewById(R.id.B13);
        displayBoardB[2][3] = (TextView) findViewById(R.id.B23);
        displayBoardB[3][3] = (TextView) findViewById(R.id.B33);
        displayBoardB[4][3] = (TextView) findViewById(R.id.B43);
        displayBoardB[5][3] = (TextView) findViewById(R.id.B53);
        displayBoardB[6][3] = (TextView) findViewById(R.id.B63);
        displayBoardB[7][3] = (TextView) findViewById(R.id.B73);
        displayBoardB[0][4] = (TextView) findViewById(R.id.B04);
        displayBoardB[1][4] = (TextView) findViewById(R.id.B14);
        displayBoardB[2][4] = (TextView) findViewById(R.id.B24);
        displayBoardB[3][4] = (TextView) findViewById(R.id.B34);
        displayBoardB[4][4] = (TextView) findViewById(R.id.B44);
        displayBoardB[5][4] = (TextView) findViewById(R.id.B54);
        displayBoardB[6][4] = (TextView) findViewById(R.id.B64);
        displayBoardB[7][4] = (TextView) findViewById(R.id.B74);
        displayBoardB[0][5] = (TextView) findViewById(R.id.B05);
        displayBoardB[1][5] = (TextView) findViewById(R.id.B15);
        displayBoardB[2][5] = (TextView) findViewById(R.id.B25);
        displayBoardB[3][5] = (TextView) findViewById(R.id.B35);
        displayBoardB[4][5] = (TextView) findViewById(R.id.B45);
        displayBoardB[5][5] = (TextView) findViewById(R.id.B55);
        displayBoardB[6][5] = (TextView) findViewById(R.id.B65);
        displayBoardB[7][5] = (TextView) findViewById(R.id.B75);
        displayBoardB[0][6] = (TextView) findViewById(R.id.B06);
        displayBoardB[1][6] = (TextView) findViewById(R.id.B16);
        displayBoardB[2][6] = (TextView) findViewById(R.id.B26);
        displayBoardB[3][6] = (TextView) findViewById(R.id.B36);
        displayBoardB[4][6] = (TextView) findViewById(R.id.B46);
        displayBoardB[5][6] = (TextView) findViewById(R.id.B56);
        displayBoardB[6][6] = (TextView) findViewById(R.id.B66);
        displayBoardB[7][6] = (TextView) findViewById(R.id.B76);
        displayBoardB[0][7] = (TextView) findViewById(R.id.B07);
        displayBoardB[1][7] = (TextView) findViewById(R.id.B17);
        displayBoardB[2][7] = (TextView) findViewById(R.id.B27);
        displayBoardB[3][7] = (TextView) findViewById(R.id.B37);
        displayBoardB[4][7] = (TextView) findViewById(R.id.B47);
        displayBoardB[5][7] = (TextView) findViewById(R.id.B57);
        displayBoardB[6][7] = (TextView) findViewById(R.id.B67);
        displayBoardB[7][7] = (TextView) findViewById(R.id.B77);

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if ((i + j) % 2 == 0){
                    displayBoardB[i][j].setBackgroundResource(R.color.boardWhite);
                }
                else {
                    displayBoardB[i][j].setBackgroundResource(R.color.boardBlack);
                }
            }
        }

        placePieces();
    }

    private void placePieces() {
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                Piece piece = chessBoard[i][j].getPiece();
                displayBoard[i][j].setEnabled(false);
                if (piece != null) {
                    if (piece instanceof King) {
                        if (piece.white)
                            displayBoard[i][j].setBackgroundResource(R.drawable.wking);
                        else
                            displayBoard[i][j].setBackgroundResource(R.drawable.bking);
                    } else if (piece instanceof Queen) {
                        if (piece.white)
                            displayBoard[i][j].setBackgroundResource(R.drawable.wqueen);
                        else
                            displayBoard[i][j].setBackgroundResource(R.drawable.bqueen);
                    } else if (piece instanceof Bishop) {
                        if (piece.white)
                            displayBoard[i][j].setBackgroundResource(R.drawable.wbishop);
                        else
                            displayBoard[i][j].setBackgroundResource(R.drawable.bbishop);
                    } else if (piece instanceof Knight) {
                        if (piece.white)
                            displayBoard[i][j].setBackgroundResource(R.drawable.wknight);
                        else
                            displayBoard[i][j].setBackgroundResource(R.drawable.bknight);
                    } else if (piece instanceof Rook) {
                        if (piece.white)
                            displayBoard[i][j].setBackgroundResource(R.drawable.wrook);
                        else
                            displayBoard[i][j].setBackgroundResource(R.drawable.brook);
                    } else if (piece instanceof Pawn) {
                        if (piece.white)
                            displayBoard[i][j].setBackgroundResource(R.drawable.wpawn);
                        else
                            displayBoard[i][j].setBackgroundResource(R.drawable.bpawn);
                    }
                }
                else{
                    displayBoard[i][j].setBackgroundResource(0);
                }
            }
        }
    }

    private int getPicture(Piece piece) {
        if(whiteTurn) {
            if(piece instanceof Pawn)
                return R.drawable.wpawn;
            else if (piece instanceof Rook)
                return R.drawable.wrook;
            else if (piece instanceof Knight)
                return R.drawable.wknight;
            else if (piece instanceof Bishop)
                return R.drawable.wbishop;
            else if (piece instanceof Queen)
                return R.drawable.wqueen;
            else if (piece instanceof King)
                return R.drawable.wking;
            else
                return -1;
        }
        else {
            if(piece instanceof Pawn)
                return R.drawable.bpawn;
            else if (piece instanceof Rook)
                return R.drawable.brook;
            else if (piece instanceof Knight)
                return R.drawable.bknight;
            else if (piece instanceof Bishop)
                return R.drawable.bbishop;
            else if (piece instanceof Queen)
                return R.drawable.bqueen;
            else if (piece instanceof King)
                return R.drawable.bking;
            else
                return -1;
        }
    }
}