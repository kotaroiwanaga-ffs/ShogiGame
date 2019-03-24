import java.util.*;
import java.io.*;

public class Test1{
    public static void main(String[] args){

	Board board = new Board();
	
	board.setProperty(0, new Pawn());
	board.deletePosition(1, 3, 6);

	ChessPiece piece = board.getPosition(1, 1, 7);
	ChessPiece cp = board.copyChessPiece(piece);
	board.setProperty(0, cp);
	board.deletePosition(1, 1, 7);

	board.getPosition(0, 8, 6).changePiece();
	board.getPosition(0, 2, 8).changePiece();

	board.setProperty(1, board.copyChessPieceReset(0, 2, 8));

	Boolean bool = board.moveChessPiece(0, 8, 6, 8, 5);
	//board.setPosition(0, 8, 5, board.copyChessPiece(0, 8, 6));
	//board.deletePosition(0, 8, 6);

	//board.putCapturedPiece(0, 1, 3, 4);
	//board.setPosition(0, 3, 4, board.copyChessPiece(0, 1));
	//Boolean bool = board.deleteProperty(0, 1);

	board.printBoard(0);
	
	System.out.println();

	board.printBoard(1);

	System.out.println(bool);

    }


}
