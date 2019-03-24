import java.util.*;
import java.io.*;

public class Test2{
    public static void main(String[] args){

	Board board = new Board();
	int action, r1, c1, r2, c2, i;

	while(true){
	    board.printBoard(0);

	    // プレイヤー0の手番
	    System.out.println("プレイヤー0の手番です");
	    System.out.println("action : なにをするか");
	    System.out.println("0 : 駒を動かす, 1 : 持ち駒を使う, -1 : やめる");
	    System.out.print("action = ");
	    action = new Scanner(System.in).nextInt();

	    switch(action){
	    case 0:
		System.out.println("action : 駒を動かす");
		System.out.println("r1 : 移動させる駒の現在位置の列番号");
		System.out.println("c1 : 移動させる駒の現在位置の行番号");
		System.out.print("r1 = ");
		r1 = new Scanner(System.in).nextInt() - 1;
		System.out.print("c1 = ");
		c1 = new Scanner(System.in).nextInt() - 1;

		System.out.println("r2 : 移動先の列番号");
		System.out.println("c2 : 移動先の行番号");
		System.out.print("r2 = ");
		r2 = new Scanner(System.in).nextInt() - 1;
		System.out.print("c2 = ");
		c2 = new Scanner(System.in).nextInt() - 1;

		board.moveChessPiece(0, r1, c1, r2, c2);

		break;
	    case 1:
		System.out.println("action : 持ち駒を使う");
		System.out.println("i : 持ち駒の番号");
		System.out.print("i = ");
		i = new Scanner(System.in).nextInt();

		System.out.println("r2 : 設置位置の列番号");
		System.out.println("c2 : 設置位置の行番号");
		System.out.print("r2 = ");
		r2 = new Scanner(System.in).nextInt() - 1;
		System.out.print("c2 = ");
		c2 = new Scanner(System.in).nextInt() - 1;

		board.putCapturedPiece(0, i, r2, c2);

		break;

	    case -1:
		System.exit(1);
		break;
	    default:
		break;


	    }

	    board.printBoard(0);



	}



    }


}
