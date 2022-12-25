package com.example.android05;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import chess.*;
import pieces.*;

public class MainActivity extends AppCompatActivity {

    public static Spot[][] chessBoard;
    public static boolean whiteTurn;

    public TextView[][] displayBoard;
    public TextView[][] displayBoardB;
    public TextView checkmate;

    public ArrayList<String> moveList;

    public Spot endSpot;
    public Spot startSpot;
    public boolean firstClick;
    public Spot lastPlayedStart;
    public Spot lastPlayedEnd;

    public LinearLayout pawnPromotion;
    public String promotedPiece;

    public Piece capturedPiece = null;
    public boolean capturedEP;
    public boolean castled;
    public boolean kingFirstMove;
    public boolean rookFirstMove;
    public boolean promoted;
    public boolean AI;
    public boolean AIPromotion;
    public boolean undoable;

    public Button ai;
    public Button undo;
    public Button draw;
    public Button resign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chessBoard = new Spot[8][8];
        displayBoard = new TextView[8][8];
        displayBoardB = new TextView[8][8];
        endSpot = chessBoard[0][0];
        startSpot = chessBoard[0][0];
        firstClick = true;
        whiteTurn = true;
        promotedPiece = "";
        capturedEP = false;
        castled = false;
        kingFirstMove = false;
        rookFirstMove = false;
        promoted = false;
        AI = false;
        AIPromotion = false;
        undoable = false;
        moveList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBoard();

        checkmate = (TextView) findViewById(R.id.checkmate);
        pawnPromotion = (LinearLayout) findViewById(R.id.pawnPromotion);

        ai = (Button) findViewById(R.id.ai);
        undo = (Button) findViewById(R.id.undo);
        draw = (Button) findViewById(R.id.draw);
        resign = (Button) findViewById(R.id.resign);

    }

    public Piece[][] getPieceGrid(Spot[][] board) {
        Piece[][] result = new Piece[8][8];
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                result[j][i] = board[i][j].getPiece();
            }
        }
        return result;
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

    public void onClick(View v) {
        Spot lastStart = startSpot;
        Spot lastEnd = endSpot;
        switch (v.getId()){
            case R.id.F00:
                endSpot = chessBoard[0][0];
                break;
            case R.id.F10:
                endSpot = chessBoard[1][0];
                break;
            case R.id.F20:
                endSpot = chessBoard[2][0];
                break;
            case R.id.F30:
                endSpot = chessBoard[3][0];
                break;
            case R.id.F40:
                endSpot = chessBoard[4][0];
                break;
            case R.id.F50:
                endSpot = chessBoard[5][0];
                break;
            case R.id.F60:
                endSpot = chessBoard[6][0];
                break;
            case R.id.F70:
                endSpot = chessBoard[7][0];
                break;
            case R.id.F01:
                endSpot = chessBoard[0][1];
                break;
            case R.id.F11:
                endSpot = chessBoard[1][1];
                break;
            case R.id.F21:
                endSpot = chessBoard[2][1];
                break;
            case R.id.F31:
                endSpot = chessBoard[3][1];
                break;
            case R.id.F41:
                endSpot = chessBoard[4][1];
                break;
            case R.id.F51:
                endSpot = chessBoard[5][1];
                break;
            case R.id.F61:
                endSpot = chessBoard[6][1];
                break;
            case R.id.F71:
                endSpot = chessBoard[7][1];
                break;
            case R.id.F02:
                endSpot = chessBoard[0][2];
                break;
            case R.id.F12:
                endSpot = chessBoard[1][2];
                break;
            case R.id.F22:
                endSpot = chessBoard[2][2];
                break;
            case R.id.F32:
                endSpot = chessBoard[3][2];
                break;
            case R.id.F42:
                endSpot = chessBoard[4][2];
                break;
            case R.id.F52:
                endSpot = chessBoard[5][2];
                break;
            case R.id.F62:
                endSpot = chessBoard[6][2];
                break;
            case R.id.F72:
                endSpot = chessBoard[7][2];
                break;
            case R.id.F03:
                endSpot = chessBoard[0][3];
                break;
            case R.id.F13:
                endSpot = chessBoard[1][3];
                break;
            case R.id.F23:
                endSpot = chessBoard[2][3];
                break;
            case R.id.F33:
                endSpot = chessBoard[3][3];
                break;
            case R.id.F43:
                endSpot = chessBoard[4][3];
                break;
            case R.id.F53:
                endSpot = chessBoard[5][3];
                break;
            case R.id.F63:
                endSpot = chessBoard[6][3];
                break;
            case R.id.F73:
                endSpot = chessBoard[7][3];
                break;
            case R.id.F04:
                endSpot = chessBoard[0][4];
                break;
            case R.id.F14:
                endSpot = chessBoard[1][4];
                break;
            case R.id.F24:
                endSpot = chessBoard[2][4];
                break;
            case R.id.F34:
                endSpot = chessBoard[3][4];
                break;
            case R.id.F44:
                endSpot = chessBoard[4][4];
                break;
            case R.id.F54:
                endSpot = chessBoard[5][4];
                break;
            case R.id.F64:
                endSpot = chessBoard[6][4];
                break;
            case R.id.F74:
                endSpot = chessBoard[7][4];
                break;
            case R.id.F05:
                endSpot = chessBoard[0][5];
                break;
            case R.id.F15:
                endSpot = chessBoard[1][5];
                break;
            case R.id.F25:
                endSpot = chessBoard[2][5];
                break;
            case R.id.F35:
                endSpot = chessBoard[3][5];
                break;
            case R.id.F45:
                endSpot = chessBoard[4][5];
                break;
            case R.id.F55:
                endSpot = chessBoard[5][5];
                break;
            case R.id.F65:
                endSpot = chessBoard[6][5];
                break;
            case R.id.F75:
                endSpot = chessBoard[7][5];
                break;
            case R.id.F06:
                endSpot = chessBoard[0][6];
                break;
            case R.id.F16:
                endSpot = chessBoard[1][6];
                break;
            case R.id.F26:

                endSpot = chessBoard[2][6];
                break;
            case R.id.F36:
                endSpot = chessBoard[3][6];
                break;
            case R.id.F46:
                endSpot = chessBoard[4][6];
                break;
            case R.id.F56:
                endSpot = chessBoard[5][6];
                break;
            case R.id.F66:
                endSpot = chessBoard[6][6];
                break;
            case R.id.F76:
                endSpot = chessBoard[7][6];
                break;
            case R.id.F07:
                endSpot = chessBoard[0][7];
                break;
            case R.id.F17:
                endSpot = chessBoard[1][7];
                break;
            case R.id.F27:
                endSpot = chessBoard[2][7];
                break;
            case R.id.F37:
                endSpot = chessBoard[3][7];
                break;
            case R.id.F47:
                endSpot = chessBoard[4][7];
                break;
            case R.id.F57:
                endSpot = chessBoard[5][7];
                break;
            case R.id.F67:
                endSpot = chessBoard[6][7];
                break;
            case R.id.F77:
                endSpot = chessBoard[7][7];
                break;
        }

        //canceling selection
        if(endSpot != null && startSpot != null && startSpot.getXCoordinate() == endSpot.getXCoordinate() && startSpot.getYCoordinate() == endSpot.getYCoordinate()) {

            //unhighlight square
            int temp = startSpot.getXCoordinate() + startSpot.getYCoordinate();
            if (temp % 2 == 0)
                displayBoardB[startSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(R.color.boardWhite);
            else
                displayBoardB[startSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(R.color.boardBlack);

            startSpot = null;
            endSpot = null;
            undo.setEnabled(true);
            ai.setEnabled(true);
            firstClick = true;
            return;
        }

        if (firstClick) {
            AI = false;
            AIPromotion = false;
            undo.setEnabled(false);
            ai.setEnabled(false);
            if (chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].isEmpty()) {
                Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
                startSpot = lastStart;
                endSpot = lastEnd;
                undo.setEnabled(true);
                ai.setEnabled(true);
                return;
            }
            else if (chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].getPiece().white != whiteTurn) {
                Toast.makeText(this, "Wrong color turn", Toast.LENGTH_SHORT).show();
                startSpot = lastStart;
                endSpot = lastEnd;
                undo.setEnabled(true);
                ai.setEnabled(true);
                return;
            }
            else{
                displayBoardB[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.color.highlightPiece);
                firstClick = false;
                startSpot = endSpot;
            }
        }
        else{
            if(!AI && startSpot.getPiece() instanceof King) {
                if (startSpot.getPiece().firstMove)
                    kingFirstMove = true;
                else
                    kingFirstMove = false;
            }
            if(!AI && startSpot.getPiece() instanceof Rook) {
                if (startSpot.getPiece().firstMove)
                   rookFirstMove = true;
                else
                    rookFirstMove = false;
            }
            if(AI || Chess.validMove(startSpot.getXCoordinate()+1, 8-startSpot.getYCoordinate(), endSpot.getXCoordinate()+1, 8-endSpot.getYCoordinate(), whiteTurn, getPieceGrid(chessBoard))) {

                capturedEP = false;
                castled = false;
                promoted = false;

                if(startSpot.getPiece() instanceof Pawn) {
                    //enpassant
                    if(endSpot.getYCoordinate() == 2 && whiteTurn && endSpot.getPiece() == null && endSpot.getXCoordinate() != startSpot.getXCoordinate()) {
                        displayBoard[endSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(0);
                        capturedPiece = chessBoard[endSpot.getXCoordinate()][startSpot.getYCoordinate()].getPiece();
                        chessBoard[endSpot.getXCoordinate()][startSpot.getYCoordinate()].setPiece(null);
                        capturedEP = true;
                    }
                    if(endSpot.getYCoordinate() == 5 && !whiteTurn && endSpot.getPiece() == null && endSpot.getXCoordinate() != startSpot.getXCoordinate()) {
                        displayBoard[endSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(0);
                        capturedPiece = chessBoard[endSpot.getXCoordinate()][startSpot.getYCoordinate()].getPiece();
                        chessBoard[endSpot.getXCoordinate()][startSpot.getYCoordinate()].setPiece(null);
                        capturedEP = true;
                    }

                    //promotion
                    if(endSpot.getYCoordinate() == 0 && whiteTurn) {
                        capturedPiece = endSpot.getPiece();
                        if(AI) {
                            chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setPiece(new Queen(true));
                            promotedPiece="Q";
                            displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.wqueen);
                            AIPromotion = true;
                            moveList.add(Integer.toString(startSpot.getXCoordinate())+Integer.toString(startSpot.getYCoordinate())+ "," + Integer.toString(endSpot.getXCoordinate()) + Integer.toString(endSpot.getYCoordinate()) + "," + promotedPiece);
                        }
                        else {
                            pawnPromotion.setVisibility(View.VISIBLE);
                        }
                        promoted = true;
                    }
                    if(endSpot.getYCoordinate() == 7 && !whiteTurn) {
                        capturedPiece = endSpot.getPiece();
                        if(AI) {
                            chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setPiece(new Queen(false));
                            promotedPiece="Q";
                            displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.bqueen);
                            AIPromotion = true;
                            moveList.add(Integer.toString(startSpot.getXCoordinate())+Integer.toString(startSpot.getYCoordinate())+ "," + Integer.toString(endSpot.getXCoordinate()) + Integer.toString(endSpot.getYCoordinate()) + "," + promotedPiece);
                        }
                        else {
                            pawnPromotion.setVisibility(View.VISIBLE);
                        }
                        promoted = true;
                    }
                }

                //castling
                if(startSpot.getPiece() instanceof King && !AIPromotion) {
                    if(!AI)
                        rookFirstMove = true;
                    if(whiteTurn && startSpot.getXCoordinate() == 4 && startSpot.getYCoordinate() == 7) {
                        if(endSpot.getXCoordinate() == 2 && endSpot.getYCoordinate() == 7) {
                            displayBoard[0][7].setBackgroundResource(0);
                            displayBoard[3][7].setBackgroundResource(R.drawable.wrook);
                            chessBoard[3][7].setPiece(chessBoard[0][7].getPiece());
                            chessBoard[0][7].setPiece(null);
                            castled = true;
                        }
                        if(endSpot.getXCoordinate() == 6 && endSpot.getYCoordinate() == 7) {
                            displayBoard[7][7].setBackgroundResource(0);
                            displayBoard[5][7].setBackgroundResource(R.drawable.wrook);
                            chessBoard[5][7].setPiece(chessBoard[7][7].getPiece());
                            chessBoard[7][7].setPiece(null);
                            castled = true;
                        }
                    }
                    if(!whiteTurn && startSpot.getXCoordinate() == 4 && startSpot.getYCoordinate() == 0) {
                        if(endSpot.getXCoordinate() == 2 && endSpot.getYCoordinate() == 0) {
                            displayBoard[0][0].setBackgroundResource(0);
                            displayBoard[3][0].setBackgroundResource(R.drawable.brook);
                            chessBoard[3][0].setPiece(chessBoard[0][0].getPiece());
                            chessBoard[0][0].setPiece(null);
                            castled = true;
                        }
                        if(endSpot.getXCoordinate() == 6 && endSpot.getYCoordinate() == 0) {
                            displayBoard[7][0].setBackgroundResource(0);
                            displayBoard[5][0].setBackgroundResource(R.drawable.brook);
                            chessBoard[5][0].setPiece(chessBoard[7][0].getPiece());
                            chessBoard[7][0].setPiece(null);
                            castled = true;
                        }
                    }
                }

                //updates board visual
                if(!AIPromotion) {
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(getPicture(startSpot.getPiece()));
                }
                displayBoard[startSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(0);

                if(!capturedEP && !promoted) {
                    capturedPiece = endSpot.getPiece();
                }

                if(!AIPromotion) {
                    endSpot.setPiece(startSpot.getPiece());
                }
                startSpot.setPiece(null);

                //unhighlight square
                int temp = startSpot.getXCoordinate() + startSpot.getYCoordinate();
                if (temp % 2 == 0)
                    displayBoardB[startSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(R.color.boardWhite);
                else
                    displayBoardB[startSpot.getXCoordinate()][startSpot.getYCoordinate()].setBackgroundResource(R.color.boardBlack);

                if(!promoted) {
                    moveList.add(Integer.toString(startSpot.getXCoordinate()) + Integer.toString(startSpot.getYCoordinate()) + "," + Integer.toString(endSpot.getXCoordinate()) + Integer.toString(endSpot.getYCoordinate()));
                }

                //checkmate check
                for (int i = 0; i <= 7; i++) {
                    for (int j = 0; j <= 7; j++) {
                        if(Chess.isCheck(1 + j, 8 - i, true) && !whiteTurn) {
                            if(Chess.checkmate(true)) {
                                checkmate.setText("Checkmate, Black Wins!");
                                checkmate.setVisibility(View.VISIBLE);
                                endGame();
                                return;
                            }
                        }
                        else if(Chess.isCheck(1 + j, 8 - i, false) && whiteTurn) {
                            if(Chess.checkmate(false)) {
                                checkmate.setText("Checkmate, White Wins!");
                                checkmate.setVisibility(View.VISIBLE);
                                endGame();
                                return;
                            }
                        }
                    }
                }
                lastPlayedStart = startSpot;
                lastPlayedEnd = endSpot;
                undoable = true;
                firstClick = true;
                whiteTurn = !whiteTurn;
                undo.setEnabled(true);
                ai.setEnabled(true);
                AI = false;
                if(promoted && !AIPromotion) {
                    undo.setEnabled(false);
                    ai.setEnabled(false);
                }
                AIPromotion = false;
            }
            else {
                Toast.makeText(this, "Illegal move!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void promotePawn(View v){
        pawnPromotion.setVisibility(View.INVISIBLE);
        switch (v.getId()){
            case R.id.queen:
                chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setPiece(new Queen(!whiteTurn));
                promotedPiece="Q";
                if (!whiteTurn)
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.wqueen);
                else
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.bqueen);
                break;
            case R.id.rook:
                chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setPiece(new Rook(!whiteTurn));
                promotedPiece="R";
                if (!whiteTurn)
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.wrook);
                else
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.brook);
                break;
            case R.id.bishop:
                chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setPiece(new Bishop(!whiteTurn));
                promotedPiece="B";
                if (!whiteTurn)
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.wbishop);
                else
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.bbishop);
                break;
            case R.id.knight:
                chessBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setPiece(new Knight(!whiteTurn));
                promotedPiece="K";
                if (!whiteTurn)
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.wknight);
                else
                    displayBoard[endSpot.getXCoordinate()][endSpot.getYCoordinate()].setBackgroundResource(R.drawable.bknight);
                break;
        }
        undo.setEnabled(true);
        ai.setEnabled(true);
        moveList.add(Integer.toString(startSpot.getXCoordinate())+Integer.toString(startSpot.getYCoordinate())+ "," + Integer.toString(endSpot.getXCoordinate()) + Integer.toString(endSpot.getYCoordinate()) + "," + promotedPiece);
    }

    public void AI(View v) {
        ai.setEnabled(false);
        AI = true;
        while(true) {
            int a = (int) (Math.random() * 8);
            int b = (int) (Math.random() * 8);
            int c = (int) (Math.random() * 8);
            int d = (int) (Math.random() * 8);
            startSpot = chessBoard[a][b];
            endSpot = chessBoard[c][d];
            if(startSpot.getPiece() instanceof King) {
                if (startSpot.getPiece().firstMove)
                    kingFirstMove = true;
                else
                    kingFirstMove = false;
            }
            if(startSpot.getPiece() instanceof Rook) {
                if (startSpot.getPiece().firstMove)
                    rookFirstMove = true;
                else
                    rookFirstMove = false;
            }
            if(Chess.validMove(startSpot.getXCoordinate()+1, 8-startSpot.getYCoordinate(), endSpot.getXCoordinate()+1, 8-endSpot.getYCoordinate(), whiteTurn, getPieceGrid(chessBoard))) {
                firstClick = false;
                onClick(v);
                if(resign.isEnabled())
                    ai.setEnabled(true);
                return;

            }
            kingFirstMove = false;
            rookFirstMove = false;
        }
    }

    public void undo(View v) {
        if(undoable && lastPlayedStart !=null) {
            moveList.remove(moveList.size()-1);
            whiteTurn = !whiteTurn;
            displayBoard[lastPlayedStart.getXCoordinate()][lastPlayedStart.getYCoordinate()].setBackgroundResource(getPicture(lastPlayedEnd.getPiece()));
            lastPlayedStart.setPiece(lastPlayedEnd.getPiece());
            whiteTurn = !whiteTurn;
            if(kingFirstMove && lastPlayedEnd.getPiece() instanceof King){
                lastPlayedEnd.getPiece().firstMove = true;
                kingFirstMove = false;
            }
            if(rookFirstMove && lastPlayedEnd.getPiece() instanceof Rook){
                lastPlayedEnd.getPiece().firstMove = true;
                rookFirstMove = false;
            }
            if (capturedEP) {
                displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedStart.getYCoordinate()].setBackgroundResource(getPicture(capturedPiece));
                chessBoard[lastPlayedEnd.getXCoordinate()][lastPlayedStart.getYCoordinate()].setPiece(capturedPiece);

                displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(0);
                lastPlayedEnd.setPiece(null);
                whiteTurn = !whiteTurn;
            }
            else if (castled) {
                lastPlayedStart.getPiece().firstMove = true;
                if (lastPlayedEnd.getYCoordinate() == 7) {//white
                    if (lastPlayedEnd.getXCoordinate() == 2) {
                        displayBoard[3][7].setBackgroundResource(0);
                        chessBoard[3][7].setPiece(null);

                        displayBoard[0][7].setBackgroundResource(R.drawable.wrook);
                        chessBoard[0][7].setPiece(new Rook(true));
                    }
                    else if (lastPlayedEnd.getXCoordinate() == 6) {
                        displayBoard[5][7].setBackgroundResource(0);
                        chessBoard[5][7].setPiece(null);

                        displayBoard[7][7].setBackgroundResource(R.drawable.wrook);
                        chessBoard[7][7].setPiece(new Rook(true));
                    }
                }
                else if (lastPlayedEnd.getYCoordinate() == 0) {//black
                    if (lastPlayedEnd.getXCoordinate() == 2) {
                        displayBoard[3][0].setBackgroundResource(0);
                        chessBoard[3][0].setPiece(null);

                        displayBoard[0][0].setBackgroundResource(R.drawable.brook);
                        chessBoard[0][0].setPiece(new Rook(false));
                    }
                    else if (lastPlayedEnd.getXCoordinate() == 6) {
                        displayBoard[5][0].setBackgroundResource(0);
                        chessBoard[5][0].setPiece(null);

                        displayBoard[7][0].setBackgroundResource(R.drawable.brook);
                        chessBoard[7][0].setPiece(new Rook(false));
                    }
                }
                displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(0);
                lastPlayedEnd.setPiece(null);
                whiteTurn = !whiteTurn;
            }
            else if(promoted) {
                if(lastPlayedStart.getYCoordinate() == 1) {
                    displayBoard[lastPlayedStart.getXCoordinate()][lastPlayedStart.getYCoordinate()].setBackgroundResource(R.drawable.wpawn);
                    lastPlayedStart.setPiece(new Pawn(true));

                    if(capturedPiece != null) {
                        displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(getPicture(capturedPiece));
                        lastPlayedEnd.setPiece(capturedPiece);
                    }
                    else{
                        displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(0);
                        chessBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setPiece(null);
                    }

                }
                else if(lastPlayedStart.getYCoordinate() == 6) {
                    displayBoard[lastPlayedStart.getXCoordinate()][lastPlayedStart.getYCoordinate()].setBackgroundResource(R.drawable.bpawn);
                    lastPlayedStart.setPiece(new Pawn(false));

                    if(capturedPiece != null) {
                        displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(getPicture(capturedPiece));
                        lastPlayedEnd.setPiece(capturedPiece);
                   }
                    else{
                        displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(0);
                        chessBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setPiece(null);
                    }
                }
                whiteTurn = !whiteTurn;
            }
            else {
                if(capturedPiece == null)
                    displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(0);
                else
                    displayBoard[lastPlayedEnd.getXCoordinate()][lastPlayedEnd.getYCoordinate()].setBackgroundResource(getPicture(capturedPiece));
                whiteTurn = !whiteTurn;
                lastPlayedEnd.setPiece(capturedPiece);
                capturedPiece = null;
            }

            undoable = false;
        }
        else {
            Toast.makeText(this, "Unable to undo", Toast.LENGTH_SHORT).show();
        }
    }

    public void draw(View v) {
        checkmate.setText("Draw! Nobody Wins!");
        checkmate.setVisibility(View.VISIBLE);
        endGame();
    }

    public void resign(View v) {
        if(whiteTurn) {
            checkmate.setText("Resignation! Black Wins!");
            checkmate.setVisibility(View.VISIBLE);
        }
        else {
            checkmate.setText("Resignation! White Wins!");
            checkmate.setVisibility(View.VISIBLE);
        }
        endGame();
    }

    public void endGame(){
        ai.setEnabled(false);
        undo.setEnabled(false);
        draw.setEnabled(false);
        resign.setEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Record this game?").setTitle("GAME OVER");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainActivity.this, SaveGame.class);
                intent.putExtra("moves", moveList);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}