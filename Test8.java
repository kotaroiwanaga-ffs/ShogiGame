//copyBoard()のテスト

import java.util.*;
import java.io.*;

public class Test8{


    public static void main(String[] args){

	Board board = new Board();
	
	Board copyboard;

	Random rnd = new Random();
	
	int r1 = -1;
	int c1 = -1;
	int r2 = -1;
	int c2 = -1;

	/*
	for(int k = 0; k <= 1; k++){
	    for(int i = 1; i <= 20; i++){
	    
		r1 = rnd.nextInt(9);
		c1 = rnd.nextInt(9);
		r2 = rnd.nextInt(9);
		c2 = rnd.nextInt(9);

		board.moveChessPiece(k,r1,c1,r2,c2);
	    }
	}
	*/

	

	board.moveChessPiece(0,6,6,6,5);
	board.moveChessPiece(0,6,5,6,4);
	board.moveChessPiece(0,6,4,6,3);
	board.moveChessPiece(0,6,3,6,2);


	//copyboard = board.copyBoard();

	board.printBoard(0);

	board.printBoard(1);


	board.moveChessPiece(1,6,6,6,5);
        board.moveChessPiece(1,6,5,6,4);
        board.moveChessPiece(1,6,4,6,3);
        board.moveChessPiece(1,6,3,6,2);

	board.printBoard(0);

	board.printBoard(1);




	
	
    }

}
