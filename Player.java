import java.util.*;
import java.io.*;

public class Player{

   // フィールド
    Random rand = new Random();

   // メソッド

   // 抽象メソッド

    // メッセージ番号と盤面を受け取って次の入力を生成
    public int[]  createInputArray(int player, int mes, Board board){

	switch(mes){
	case 2: // あなたの番
	    return messageAction2(player, board);
	case 4:
	    // 成る
	    return messageAction4(player, board);
	case 9:
	    return messageAction9(player, board);
	case 10: 
	    return messageAction9(player, board);
	case 11:
	    return messageAction9(player, board);
	case 12:
	    return messageAction9(player, board);
	case 13:
	    return messageAction9(player, board);
	case 14:
	    return messageAction9(player, board);
	case 15:
	    return messageAction9(player, board);
	case 16:
	    return messageAction9(player, board);
	case 17:
	    return messageAction9(player, board);
	case 18:
	    return messageAction9(player, board);
	case 19:
	    return messageAction9(player, board);
	case 20:
	    return messageAction9(player, board);
	case 21:
	    return messageAction9(player, board);
	default: // エラー
	    System.out.println("エラーです");
	    return null;
	}

    }

    // 各メッセージ番号に対する処理
    
    // 自分の指す番
    public int[] messageAction2(int player, Board board){
	
	ArrayList<Integer> input = new ArrayList<Integer>();
	int propertySize;
	int temp;

	temp = rand.nextInt(10);
	propertySize = board.getPropertySize(player);

	// 持ち駒を使う
	if( (temp == 0 || temp == 1) && propertySize > 0){
	    input.add(1);

	    input.add(rand.nextInt(propertySize));
		
	    while(true){

		for(int i = 0; i < 2; i++){
		    input.add(rand.nextInt(9));
		}

		if(board.isEmpty(input.get(1), input.get(2) ) ){
		    break;
		}

	    } // while(true)
		
	}

	// 盤面の駒移動
	else{
	    input.add(0);

	    // 移動させる駒を指定
	     while(true){

		for(int i = 0; i < 2; i++){
		    input.add(rand.nextInt(9));
		}

		if(!board.isEmpty(player, input.get(1), input.get(2) ) ){
		    break;
		}

	     } // while(true)

	     // 移動先を指定
	     while(true){

		for(int i = 0; i < 2; i++){
		    input.add(rand.nextInt(9));
		}

		if(!board.isEmpty(player, input.get(3), input.get(4) ) 
		   && board.getPosition(player, input.get(1), input.get(2) )
		   .isAbletoMove(input.get(1), input.get(2), input.get(3), input.get(4)) ){
		    break;
		}

	     } // while(true)

	}

	// 返り値作成
	int[] inputArray = new int[input.size()];

	// ArrayList → int[]
	for(int i = 0; i < inputArray.length; i++){
	    inputArray[i] = input.get(i);
	}
	    
	   
	return inputArray;


    }
   
    // 反則につき指し直し
    public int[] messageAction9(int player, Board board){

	return messageAction2(player, board);

    }

    // 直前に動かした駒を成るか
    public int[] messageAction4(int player, Board board){

	int[] inputArray = new int[1];
	int temp = rand.nextInt(10);

	if(temp > 2){
	    inputArray[0] = 3;
	}
	else{
	    inputArray[0] = 4;
	}

	return inputArray;

    }



}
