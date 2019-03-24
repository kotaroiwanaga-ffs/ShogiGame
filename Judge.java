
import java.util.*;
import java.io.*;


public class Judge implements Serializable {
    
   // フィールド
    
    // 直前に操作した駒の位置
    int[] lastPosition = new int[3]; // 0:player, 1:行(r), 2:列(c)

    // 指し直しの理由の状態を保持する変数
    private int foul = -1;

    // ゲームの経過記録
    GameRecord gamerecord = new GameRecord();

    // 先手のプレイヤー
    int firstPlayer = 0;

/****************************************************************************/

   // メソッド

    // Judgeのコンストラクタ(gamerecordの初期化)
    Judge(){
	gamerecord.resetStateCountList(0);
    }

    // lastPosition のゲッター
    int[] getLastPosition(){
	return lastPosition;
    }

    // lastPosition のセッター
    void setLastPosition(int player, int r, int c){
	lastPosition[0] = player;
	lastPosition[1] = r;
	lastPosition[2] = c;
    }

    // faulのゲッター
    int getFoul(){
	return foul;
    }

    // faulのセッター
    void setFoul(int foul){
	this.foul = foul;
    }
    
    // firstPlayerのゲッター
    int getFirstPlayer(){
	return firstPlayer;
    }

    // firstPlayerのセッター
    void setFirstPlayer(int firstPlayer){
	this.firstPlayer = firstPlayer;
    }

/*****************************************************************************/
    
    // 盤面の更新をしてメッセージ番号を返す
    public int[] renewBoard(int player, Board board, ArrayList<Integer> input){


	int[] message =  new int[2];
        
	
	Boolean comp = false;
	int r1 = -1; // 移動前の行（row)
	int c1 = -1; // 移動前の列 (column)
	int r2 = -1; // 移動後の行
	int c2 = -1; // 移動後の列


	switch(input.get(0)){

	case 0: // 盤面の駒移動
	    comp = moveChessPiece(player, input.get(1), input.get(2), input.get(3), input.get(4), board);
	    r1 = input.get(1);
	    c1 = input.get(2);
	    r2 = input.get(3);
	    c2 = input.get(4);
	    break;
	case 1: // 持駒を使う
	    comp = putCapturedPiece(player, input.get(1), input.get(2),input.get(3), board);
	    r2 = input.get(2);
	    c2 = input.get(3);
	    break;
	case 2: // 投了
	    comp = true;
	    break;
	case 3: // 直前に使用した駒をひっくり返す
	    int temp[] = getLastPosition();
            board.getPosition(temp[0], temp[1], temp[2]).changePiece();
            comp = true;
            break;
	case 4: // 盤面更新せずに手番を次に回す
	    comp = true;
            break;
	default: //エラー
	    break;
	}



	//レコードの格納
        

	//comp = falseなら消す popStateCountのようなものinputを渡すcountを一個減らす
        if(comp == true && (input.get(0) == 0 || input.get(0) == 1 || input.get(0) == 2)){
            
	    if(player == 0)
		gamerecord.pushRecord(player, input);
	    else
		gamerecord.pushRecord_switch(player, input);
                
	    //renrewStateCountList()の引数をかえるinputを追加
	    //renewStateCountList()JudgeくらすのmoveChessPieceを作る
	    gamerecord.renewStateCountList(player, board);
	    gamerecord.pushStateRecord(player, board);
	    gamerecord.printGameRecordSimple();
	}


	return getMessage(player, input.get(0),  comp,  board, r1, c1, r2, c2);
        
    }




/*****************************************************************************/

    // メッセージ番号生成
    int[] getMessage(int player, int status, Boolean comp, Board board, int r1, int c1, int r2, int c2){
	int[] mes = new int[2];
	int a_player = Math.abs(player - 1);

	System.out.println("メッセージ番号取得");
	System.out.print("comp = ");
	System.out.println(comp);

	//盤面更新が正常に行われた場合
	if(comp){

	    System.out.print("status = ");
	    System.out.println(status);

	    switch(status){
	    case 2: // 投了
		mes[0] = 7 + player;
		mes[1] = 8 - player;
		break;
	    case 0: // 盤面上の駒移動

		Boolean change = board.getPosition(player, r2, c2).isAbletoChange();

		System.out.print("change = ");
		System.out.println(change);
		
		// 駒がひっくり返せる場合は確認のメッセージを送る
		if(( (c1 >= 0 && c1 <= 2) || (c2 >= 0 && c2 <= 2) ) && change){
		    System.out.println("成れます");
		    mes[0] = 4 - player;
		    mes[1] = 3 + player;
		    
		    setLastPosition(player, r2, c2);

		    break;
		}

		/* わざとbreak書いてません */

	    default:
		mes[0] = 3 - player;
		mes[1] = 2 + player;

		// どっちかの王が詰んだ場合
		System.out.println("相手を詰ませられていないか");
		if(isCheckmate(a_player, board)){
		    mes[0] = 5 + player;
		    mes[1] = 6 - player;
		}
		break;
	    }

	}
	// 盤面更新ができなかった場合
	else{
	    
	    // 反則の手だった場合
	    if(status == 0 || status == 1){

		// 直前の一手指し直し
		if(getFoul() != 11){
		    mes[0] = (9 + getFoul()) * (1 - player) + 3 * player;
		    mes[1] = (9 + getFoul()) * player + 3 * (1 - player);
		}
		// 千日手(最初から指し直し)
		else{
		    mes[0] = (9 + getFoul()) + getFirstPlayer();
		    mes[1] = (9 + getFoul()) + Math.abs(getFirstPlayer() - 1);
		}

	    }
	    // 不測の事態
	    else{
		mes[0] = -1;
		mes[0] = -1;
	    }

	}
	
	System.out.print("mes[0] = ");
	System.out.print(mes[0]);
	System.out.print(", mes[1] = ");
	System.out.println(mes[1]);

	return mes;

    }

	 



/*****************************************************************************/

    // クライアントから盤面の駒移動命令が来た場合の処理
    Boolean moveChessPiece(int player, int r1, int c1, int r2, int c2, Board board){
	int a_player = Math.abs(player - 1);
	int a_r2 = 8 - r2;
	int a_c2 = 8 - c2;

	
	// 位置指定に問題がないか
	if( !(r1 == r2 && c1 == c2) && (r1 >= 0 && r1 <= 8) && (c1 >= 0 && c1 <= 8)  
	    && (r2 >= 0 && r2 <= 8)  && (c2 >= 0 && c2 <= 8)){

	    // (r1, c1)に自分の駒があるか
	    Boolean bool1 = !board.isEmpty(player, r1, c1);
	    
	    // (r2, c2)に自分の駒がないか
	    Boolean bool2 = board.isEmpty(player, r2, c2);

	    // 移動前と移動後の2地点間に障害物がないか
	    Boolean bool3 = board.isClear(player, r1, c1, r2, c2);

	    
	    
	    if(bool1 && bool2 && bool3){

		// 指定された駒が目的位置に(駒の種類的に)移動可能か
		Boolean bool4 = board.getPosition(player, r1, c1).isAbletoMove(r1, c1, r2, c2);

		// 移動後時点で詰んでないか
		System.out.println("移動後時点で詰んでないか");
		Boolean bool5 = !board.isCheckmate(player, r1, c1, r2, c2);

		// 移動後時点で千日手になってないか
		//System.out.println("移動後時点で千日手になっていないか");
		//Boolean bool7 = !isSennitite(player, r1, c1, r2, c2, board);
		
		if(bool4 && bool5){

		    // 駒移動
		    Boolean bool6 = board.moveChessPiece(player, r1, c1, r2, c2);
		    		    
		    // 移動できたか
		    if(bool6){
			
			//移動できなくなってないか
			//移動できなくなっていたら無理やり成る
			if(board.isDeadEnd(player, r2 , c2)){
			    board.getPosition(player, r2 , c2).changePiece();
			}

			// lastPosition更新
			setLastPosition(player, r2, c2);

			// 移動先に相手の駒がある
			if(!board.isEmpty(a_player, a_r2, a_c2)){
			    
			    board.setProperty(player, board.copyChessPieceReset(a_player, a_r2, a_c2));
			    board.deletePosition(a_player, a_r2, a_c2);
			}

			//　gamerecordを更新
			gamerecord.renew(a_player, board);

			// 千日手になってないか
			if(gamerecord.isTreefoldRepetition(a_player, board) ){

			    // 反則により一手前から指し直しの場合
			    if(board.isCheckmate(a_player) ){

				// 一手前に戻す
				gamerecord.undo(a_player, board);

				// ひっくり返せるか
				if( ( (c1 >= 0 && c1 <= 2) || (c2 >= 0 && c2 <= 2) ) && board.getPosition(player, r2, c2).isAbletoChange() ){
				    board.getPosition(player, r2, c2).changePiece();

				    //　gamerecordを更新
				    gamerecord.renew(a_player, board);
				    
				    // 千日手になっていたので反則により一手前から指し直し
				    if(gamerecord.isTreefoldRepetition(a_player, board) ){
					
					// 一手前に戻す
					gamerecord.undo(a_player, board);

					setFoul(10);
					return false;
					
				    }
				    // 千日手が回避できたので強制で成る
				    else{
					return true;
				    }

				}
				// 成ることができないので反則により一手前から指し直し
				else{

				    setFoul(10);
				    return false;
				}

			    } // if(board.isCheckmate(a_player) )

			    // 最初から指し直しの場合
			    else{
				
				// 一手前に戻す
				gamerecord.undo(a_player, board);

				// ひっくり返せる場合
				if( ( (c1 >= 0 && c1 <= 2) || (c2 >= 0 && c2 <= 2) ) 
				    && board.getPosition(player, r2, c2).isAbletoChange() ){
				    
				    board.getPosition(player, r2, c2).changePiece();

				    // gamerecordを更新
				    gamerecord.renew(a_player, board);

				    // 回避できた場合
				    if(gamerecord.isTreefoldRepetition(a_player, board) ){
					return true;
				    }
				    // 回避できなかった場合
				    else{

					// 先手のプレイヤーを更新
					setFirstPlayer(Math.abs(getFirstPlayer() - 1) );
					
					// gamerecordリセット
					gamerecord.reset(getFirstPlayer(), board);

					setFoul(11);

					return false;
				    }

				}
				// ひっくり返せない場合
				else{

				    // gamerecordリセット
				    gamerecord.reset(getFirstPlayer(), board);
				    
				    setFoul(11);

				    return false;
				}

			    } // else


			} // if(gamerecord.isTreefoldRepetition(a_player, board) )

			// 千日手じゃなかった場合
			else{
			    return true;
			}

		    }
		    else{
			System.out.println("指定通りに移動できませんでした");
			//不測事態発生
			setFoul(-10);
			return false;
		    }

		}
		else{
		    System.out.print("指定された駒が目的位置に(駒の種類的に)移動可能か\n    ");
		    System.out.println(bool4);
		    
		    System.out.println("移動後に詰んでないか\n   ");
		    System.out.println(bool5);

		    //不正な移動命令(障害物/そんな移動の仕方はない)
		    if(!bool4){
			setFoul(3);
		    }
		    //王手なんだけどそれはいかん
		    else if(!bool5){
			setFoul(6);
		    }
			
		    return false;
		}


	    }
	    else{
		System.out.println("不正な移動命令です");
		System.out.print("(r1, c1)に自分の駒があるか:\n    ");
		System.out.println(bool1);
		System.out.print("(r2, c2)に自分の駒がないか:\n    ");
		System.out.println(bool2);
		System.out.print("移動前と移動後の2地点間に障害物がないか:\n    ");
		System.out.println(bool3);

		if(!bool1){
		    setFoul(1);
		}
		else if(!bool2){
		    setFoul(2);
		}
		else if(!bool3){
		    setFoul(3);
		}
			  		
		return false;
	    }

	}

	else{
	    System.out.println("盤面外の位置を指定しています");
	    System.out.print("r1 = ");
	    System.out.print(r1);
	    System.out.print(", c1 = ");
	    System.out.print(c1);
	    System.out.print(", r2 = ");
	    System.out.print(r2);
	    System.out.print(", c2 = ");
	    System.out.println(c2);

	    setFoul(0);
	    
	    return false;
	}

    }






/*****************************************************************************/

    // クライアントから持ち駒を盤面に出す命令が来た場合の処理
    Boolean putCapturedPiece(int player, int i, int r, int c, Board board){

	int a_player = Math.abs(player - 1);

	// 設置位置が盤面上か、空マスか
	if( (r >= 0 && r <= 8) && (c >= 0 && c <= 8) && board.isEmptyAllcheck(player, r, c) ){
			     
	    
	    ChessPiece piece = board.getProperty(player, i);

	    // 存在する持ち駒か
	    if(piece != null){

		
		// 指定した持ち駒が歩の場合、二歩になってないか
		if(piece.printChessPiece().equals("歩") && board.isDoublePawn(player, r) ){
		    System.out.println("二歩です");
		    //二歩
		    setFoul(7);
		    return false;
		}

		//これ以上動けなくなる位置に設置してないか
		if(board.isDeadEnd(player, i, r, c)){
		    System.out.println("これ以上動けなくなる位置には設置できません");
		    setFoul(8);
		    return false;
		}

		//打ち歩詰めではないか
		if( isPlacingPawnCheckmate(player, i, r, c, board) ){
		    System.out.println("打ち歩詰めです");
		    setFoul(9);
		    return false;
		}


		// 持ち駒設置時点で詰んでないか
		System.out.println("持ち駒設置時点で詰んでないか");
		if(!board.isCheckmate(player, i, r, c)){
		    
		    // lastPosition更新
		    setLastPosition(player, r, c);

		    // 持ち駒が設置できたか
		    Boolean putpiece = board.putCapturedPiece(player, i, r, c);

		    if(putpiece){
			System.out.println("getStatecount->"+gamerecord.getStateCount(player,board));

			// gamerecord更新
			gamerecord.renew(a_player, board);

			// 千日手じゃない場合
			if(!gamerecord.isTreefoldRepetition(a_player, board)){
			    return true;
			}
			// 千日手の場合
			else{
			    
			    // 王手の場合(反則により指し直し)
			    if(board.isCheckmate(a_player) ){

				// 一手前に戻す
				gamerecord.undo(a_player, board);
				setFoul(10);

			    }
			    // 王手でない場合(初手から指し直し)
			    else{

				// 先手のプレイヤーを更新
				setFirstPlayer(Math.abs(getFirstPlayer() - 1) );

				// board,gamerecordリセット
				gamerecord.reset(getFirstPlayer(), board);

			    }

			    return false;

			}

		    }
		    else{
			return false;
		    }
		}
		else{
		    System.out.println("詰みです");
		    //王手
		    setFoul(6);
		    return false;
		}

	    }
	    else{
		System.out.println("存在しない持ち駒を指定しています");
		setFoul(4);
		return false;
	    }

	}
	else{
	    System.out.println("設置位置の指定が不正です");
	    setFoul(5);
	    return false;
	}

    }







/*****************************************************************************/

    //ゲーム終了(詰み)を知らせる
    Boolean isCheckmate(int player, Board board){
 
	// 現在の盤面状態でplayerが王手にされてないか
	if(board.isCheckmate(player)){
	    int king[] = board.getKingPosition(player);
	    
	    System.out.println("\n王手です");
	    System.out.println("回避方法はないか\n");

	    // 王(玉)を移動して王手を回避できるか
	    
	    System.out.println("王(玉)を移動して王手を回避できるか");

	    for(int r = -1; r <= 1; r++){
		for(int c = -1; c <= 1; c++){

		    int move_r = king[0] + r;
		    int move_c = king[1] + c;

		    if( !(move_r >= 0 && move_r <= 8 && move_c >= 0 && move_c <= 8) || (r == 0 && c == 0) ) {
			continue;
		    }


		    ChessPiece KING = board.getPosition(player, king[0], king[1]);

		    /* プリント用 */
		    String str = KING.printChessPiece();
		    move_r++; move_c++;
		    str += "(" + move_r +", " + move_c + ")";
		    System.out.println(str);
		    move_r--; move_c--;
		    /* プリント用 */


		    if(board.isEmpty(player, move_r, move_c) && !board.isCheckmate(player, king[0], king[1], move_r, move_c)){
			str = KING.printChessPiece();

			king[0]++;
			king[1]++;
			move_r++;
			move_c++;

			str += "(" + king[0] + ", " + king[1] + ") → (" + move_r + ", " + move_c + ")"; 
			System.out.println(str);

			return false;
		    }

		}
	    }


	    // 王(玉)以外の駒移動または持ち駒を盤面に置くことで王手を回避できるか
	    // 王手と言われているが王手と判断されていない
	    int checkmatePiece[][] = board.getCheckmatePiecePosition(player);

	    for(int i = 0; i < checkmatePiece.length; i++){
		
		// 王手をかけている相手の駒位置を自陣基準の駒位置に変換
		int cm_r = 8 - checkmatePiece[i][1];
		int cm_c = 8 - checkmatePiece[i][2];

		int getterPiece[][] = board.getMovablePiecePosition(player, cm_r, cm_c);

		// 王手をとっている相手の駒を奪ることができるか

		System.out.println("王手をとっている相手の駒を奪ることができるか");

		if(getterPiece != null){
		    for(int j = 0; j < getterPiece.length; j++){
			int g_player = getterPiece[j][0];
			int g_r = getterPiece[j][1];
			int g_c = getterPiece[j][2];

			if(!board.isCheckmate(player, g_r, g_c, cm_r, cm_c)){
			    String str = board.getPosition(player, g_r, g_c).printChessPiece();

			    g_r++;
			    g_c++;
			    cm_r++;
			    cm_c++;

			    str += "(" + g_r + ", " + g_c + ") → (" + cm_r + ", " + cm_c + ")"; 
			    System.out.println(str);

			    return false;
			}

		    }
		}

		// 王手をとっている相手の駒と自分の王の間に自分の駒を挟めないか

		System.out.println("王手をとっている相手の駒と自分の王の間に自分の駒を挟めないか");

		if(board.isClear(player, king[0], king[1], cm_r, cm_c)){

		    int er, ec; // 行・列における単位移動方向

		    // erの値を算出
		    if(cm_r - king[0] == 0){
			er = 0;
		    }
		    else{
			er = Math.abs(cm_r - king[0]) / (cm_r - king[0]);
		    }

		    // ecの値を算出
		    if(cm_c - king[1] == 0){
			ec = 0;
		    }
		    else{
			ec = Math.abs(cm_c - king[1]) / (cm_c - king[1]);
		    }

		    // 間のマスを1マスずつ調べる
		    for(int r = king[0] + er, c = king[1] + ec; 
			!(r == cm_r && c == cm_c); r += er, c += ec){

			int movePiece[][] = board.getMovablePiecePosition(player, r, c);

			// 王と王手の駒の間に移動できる駒はあるか
			if(movePiece != null){
			    for(int k = 0; k < movePiece.length; k++){

				int m_player = movePiece[k][0];
				int m_r = movePiece[k][1];
				int m_c = movePiece[k][2];
				
				// 移動したら王手が防げるか
				if(!board.isCheckmate(player, m_r, m_c, r, c)){
				    String str = board.getPosition(player, m_r, m_c).printChessPiece();
				    
				    m_r++;
				    m_c++;
				    r++;
				    c++;
				    
				    str += "(" + m_r + ", " + m_c + ") → (" + r + ", " + c + ")"; 
				    System.out.println(str);
				    
				    return false;
				}
				
			    }
			}
			// 王と王手の駒の間に設置できる持ち駒はあるか
			// (そもそも持ち駒はあるか)
			else if(board.getPropertySize(player) > 0){

			    // 二歩の制約がある
			    if(board.isDoublePawn(player, r)){

				for(int k = 0; k < board.getPropertySize(player); k++){

				    String pieceType = board.getProperty(player, k).printChessPiece();

				    // 歩以外の持ち駒がある
				    if(!pieceType.equals("歩")){

					// 王手回避できるか
					if(!board.isCheckmate(player, k, r, c) ){

					    r++;
					    c++;
					
					    String str = pieceType + "(持ち駒[" + k + "]) → (" + r + ", " + c + ")"; 
					    System.out.println(str);
				    
					    return false;
					}
					else{
					    break;
					}

				    }

				} // for(int k = 0; k < board.getPropertySize(player); k++)
 
			    } // if(isDoublePawn(player, r))


			    // 二歩の制約がない
			    else{

				// 王手回避できるか
				if(!board.isCheckmate(player, 0, r, c) ){
				    
				    String str = board.getProperty(player, 0).printChessPiece();

				    r++;
				    c++;
				    
				    str += "(持ち駒[0]) → (" + r + ", " + c + ")"; 
				    System.out.println(str);
				    
				    return false;
				}

			    }
			    
			} // else if(board.getPropertySize(player) > 0)

		    } // for(int r = king[0] + er, c = king[1] + ec; … 



		    

		} // if(board.isClear(player, king[0], king[1], cm_r, cm_c))


	    } // for(int i = 0; i < checkmatePiece.length; i++)

	    // 詰んでいる場合
	    return true;

	} // if(board.isCheckmate(player))
	
	// 現在の盤面で王手にされていない場合
	else{
	    System.out.println("王手でない");
	    return false;
	}

    }

    //打ち歩詰めをしていないか(ただし、設置位置が盤面内かつ空マスである前提)
    Boolean isPlacingPawnCheckmate(int player, int i, int r, int c, Board board){
	
	if(board.getProperty(player, i).printChessPiece().equals("歩")){

	    board.setPosition(player, r, c, new Pawn());

	    Boolean checkmate = isCheckmate(Math.abs(player-1), board);

	    board.deletePosition(player, r, c);

	    //詰みだった場合(打ち歩詰め)
	    if(checkmate){
		return true;
	    }
	    else{
		return false;
	    }
	    

	}
	else{
	    return false;
	}


    }

}






    
