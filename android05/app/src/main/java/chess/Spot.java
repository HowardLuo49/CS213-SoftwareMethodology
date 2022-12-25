package chess;
import pieces.Piece;

public class Spot {

    int x;
    int y;
    Piece piece;

    public Spot(int x, int y, Piece piece)
    {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public Piece getPiece()
    {
        return this.piece;
    }

    public void setPiece(Piece p)
    {
        this.piece = p;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public int getXCoordinate()
    {
        return this.x;
    }

    public int getYCoordinate()
    {
        return this.y;
    }

}
