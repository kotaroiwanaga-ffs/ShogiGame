import java.util.*;
import java.io.*;

public class TreefoldRepetitionPlayer1 extends Player{

    // フィールド
    int lastKingPos[] = {4, 7};

    // messageAction2 をオーバーライド
    public int[] messageAction2(int player, Board board){
	
	ArrayList<Integer> input = new ArrayList<Integer>();
	int propertySize;
	int temp;

	temp = rand.nextInt(10);
	propertySize = board.getPropertySize(player);

	// 持ち駒を使う
	if(temp < 0){
	    input.add(1);

	    // 持ち駒選択
	    input.add(rand.nextInt(propertySize));

	    int empty[][] = emptyPositionArray(player,board);
	    
	    temp = rand.nextInt(empty.length);
	    
	    input.add(empty[temp][0]);
	    input.add(empty[temp][1]);

	}
	
	// 盤面の駒移動
	else{
	    input.add(0);

	    if(board.getPosition(player, 8, 8) != null){

		input.add(8);
		input.add(8);
		input.add(8);
		input.add(7);

	    }
	    else{
		int kingPos[] = board.getKingPosition(player);
	    
		for(int i = 0; i < kingPos.length; i++){
		    input.add(kingPos[i]);
		}

		for(int i = 0; i < lastKingPos.length; i++){
		    input.add(lastKingPos[i]);
		    lastKingPos[i] = kingPos[i];
		}
	    }
	}


	// 返り値作成
	int[] inputArray = new int[input.size()];

	// ArrayList → int[]
	for(int i = 0; i < inputArray.length; i++){
	    inputArray[i] = input.get(i);
	}
	    
	   
	return inputArray;

    }

   // 条件に該当する盤面上のマスを2次元配列で返す

    // 空マスのリスト
    public int[][] emptyPositionArray(int player, Board board){
	
	ArrayList<Integer> pos = new ArrayList<Integer>();

	for(int r = 0; r < 9; r++){
	    for(int c = 0; c < 9; c++){

		if(board.isEmptyAllcheck(player, r, c) ){
		    pos.add(r);
		    pos.add(c);
		}

	    } // for(int c = 0; c < 9; c++)
	} // for(int r = 0; r < 9; r++

	// 返り値作成
	int[][] posArray = new int[pos.size() / 2][2];

	for(int i = 0; i < (pos.size() / 2); i++){
	    posArray[i][0] = pos.get(i * 2);
	    posArray[i][1] = pos.get(i * 2 + 1);
	}

	return posArray;

    }

    


    // 自分の駒のあるマスのリスト
    public int[][] ownPiecePositionArray(int player, Board board){
	
	ArrayList<Integer> pos = new ArrayList<Integer>();

	for(int r = 0; r < 9; r++){
	    for(int c = 0; c < 9; c++){

		if(!board.isEmpty(player, r, c) ){
		    pos.add(r);
		    pos.add(c);
		}

	    } // for(int c = 0; c < 9; c++)
	} // for(int r = 0; r < 9; r++

	// 返り値作成
	int[][] posArray = new int[pos.size() / 2][2];

	for(int i = 0; i < (pos.size() / 2); i++){
	    posArray[i][0] = pos.get(i * 2);
	    posArray[i][1] = pos.get(i * 2 + 1);
	}

	return posArray;

    }

    // 指定した駒が現在位置から移動できる位置のリスト
    int[][] movablePositionArray(int player, int r0, int c0, Board board){

	ArrayList<Integer> pos = new ArrayList<Integer>();
	ChessPiece piece = board.getPosition(player, r0, c0);

	for(int r = 0; r < 9; r++){
	    for(int c = 0; c < 9; c++){

		if(piece.isAbletoMove(r0, c0, r, c) && board.isEmpty(player, r, c) && board.isClear(player, r0, c0, r, c) ){
		    pos.add(r);
		    pos.add(c);
		}

	    } // for(int c = 0; c < 9; c++)
	} // for(int r = 0; r < 9; r++)

	// 動けるマスがなかった場合
	if(pos.size() == 0){
	    return null;
	}
	else{
	    // 返り値作成
	    int[][] posArray = new int[pos.size() / 2][2];

	    for(int i = 0; i < (pos.size() / 2); i++){
		posArray[i][0] = pos.get(i * 2);
		posArray[i][1] = pos.get(i * 2 + 1);
	    }

	    return posArray;
	}

    }




}
