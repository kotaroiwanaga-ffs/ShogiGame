import java.util.*;
import java.io.*;

public class Board{

    // フィールド
    ChessPiece[][] position0 = new ChessPiece[9][9]; // プレイヤー0の盤面
    ChessPiece[][] position1 = new ChessPiece[9][9]; // プレイヤー1の盤面
    ArrayList<ChessPiece> property0 = new ArrayList<ChessPiece>(); // プレイヤー0の持ち駒
    ArrayList<ChessPiece> property1 = new ArrayList<ChessPiece>(); // プレイヤー1の持ち駒

    // メソッド
    
    /**************** 初期化に関するメソッド *************************/

    
    // 盤面の初期化
    Board(){

	for(int i = 0; i <= 1; i++){ // i=0:プレイヤー0, i=1:プレイヤー1

	    // 歩の配置
	    for(int j = 0; j < 9; j++){
		setPosition(i, j, 6, new Pawn());
	    }
	    // 飛車の配置
	    setPosition(i, 1, 7, new Rook());
	    
	    // 角行の配置
	    setPosition(i, 7, 7, new Bishop());

	    // 香車の配置
	    setPosition(i, 0, 8, new Lance());
	    setPosition(i, 8, 8, new Lance());

	    // 桂馬の配置
	    setPosition(i, 1, 8, new Knight());
	    setPosition(i, 7, 8, new Knight());

	    // 銀将の配置
	    setPosition(i, 2, 8, new SilverGeneral());
	    setPosition(i, 6, 8, new SilverGeneral());

	    // 金将の配置
	    setPosition(i, 3, 8, new GoldGeneral());
	    setPosition(i, 5, 8, new GoldGeneral());


	    // 王将・玉将の配置
	    if(i == 0){
		setPosition(i, 4, 8, new KingOu());
	    }
	    else{
		setPosition(i, 4, 8, new KingGyoku());
	    }

	}	
	
    }

    /*************** フィールドのセッターとゲッター ********************/

    // 盤面上の駒のゲッター(possitionのゲッター)
    public ChessPiece getPosition(int player, int r, int c){
	switch(player){
	case 0:
	    return position0[r][c];
	case 1:
	    return position1[r][c];
	default:
	    return null;
	}
    }

    // 盤面上に駒をセット(positionのセッター)
    Boolean setPosition(int player, int r, int c, ChessPiece piece){
	switch(player){
	case 0:
	    position0[r][c] = piece;
	    return true;
	case 1:
	    position1[r][c] = piece;
	    return true;
	default:
	    return false;
	}
    }

    // 持ち駒のゲッター(propertyのゲッター)
    ChessPiece getProperty(int player, int i){

	switch(player){
	case 0:
	    if(i < property0.size() && i >= 0){
		return property0.get(i);
	    }
	    else{
		System.out.println("不正な添字iです");
		return null;
	    }
	case 1:
	    if(i < property1.size() && i >= 0){
		return property1.get(i);
	    }
	    else{
		System.out.println("不正な添字iです");
		return null;
	    }
	default:
	    return null;
	}
    }

    // 持ち駒のセッター(propertyのセッター)
    Boolean setProperty(int player, ChessPiece piece){
	switch(player){
	case 0:
	    property0.add(piece);
	    return true;
	case 1:
	    property1.add(piece);
	    return true;
	default:
	    return false;
	}
    }

    // 持ち駒数を返す
    int getPropertySize(int player){

	switch(player){
	case 0:
	    return property0.size();
	case 1:
	    return property1.size();
	default:
	    System.out.println("存在しないプレイヤーの持ち駒数をリクエスト");
	    return -1;
	}

    }

 /***************** 制約関係 **************************************/

    // 指定された位置に駒がないか(ないまたは盤面外ならtrueを返す)
    Boolean isEmpty(int player, int r, int c){
	if((r >= 0 && r <= 8) && (c >= 0 && c <= 8)){

	    // 駒がない場合
	    if(getPosition(player, r, c) == null){
		return true;
	    }
	    // 駒がある場合
	    else{    
		return false;
	    }
	}
	// 盤面外を指定した場合
	else{
	    String str = "r: " + r + ", c: " + c;
	    System.out.println("盤面外を指定");
	    System.out.println(str);
     	    return true;
	}
    }

   
    // 指定された位置に駒がないか(ないならtrueを返す)
    Boolean isEmpty(int r, int c){
	
	return (isEmpty(0, r, c) && isEmpty(1, r, c));
	
    }

    Boolean isEmptyAllcheck(int player, int r, int c){
	
	return (isEmpty(player,r,c) && isEmpty(Math.abs(1-player),8-r,8-c));
	
    }


    // 盤面上の2地点間に障害物がないか
    Boolean isClear(int player, int r1, int c1, int r2, int c2){
	
	int r = r2 - r1;
	int c = c2 - c1;
	int a_player = Math.abs(player - 1);

	// 位置指定に問題がある場合
	if((r == 0 && c == 0) || !((r1 >= 0 && r1 <= 8) && (c1 >= 0 && c1 <= 8) 
	   && (r2 >= 0 && r2 <= 8)  && (c2 >= 0 && c2 <= 8)) ){
	    System.out.println("Error\n");
	    return false;
	}

	// 位置指定が正しい場合
	else{

	    System.out.println("Correct position order");

	    // 角・馬
	    if(Math.abs(r) == Math.abs(c) && Math.abs(r) > 1){
		
		System.out.println("角・馬\n");

		int er = r / Math.abs(r);
		int ec = c / Math.abs(c);
		int ri = r1 + er;
		int ci = c1 + ec;
		int a_ri = 8 - ri;
		int a_ci = 8 - ci;
		
		while(ri != r2){
		    if(!(isEmpty(player, ri, ci) && isEmpty(a_player, a_ri, a_ci))){
			System.out.print(ri);
			System.out.print(" ");
			System.out.println(ci);
			return false;
		    }
		    ri += er;
		    ci += ec;
		    a_ri -= er;
		    a_ci -= ec;
		}
	    }

	    // 飛・龍・香
	    else if(r * c == 0 && Math.abs(r + c) > 1 ){

		System.out.println("飛・龍・香\n");

		int er;
		int ec;
		
		if(r == 0){
		er = 0;
		ec = c / Math.abs(c);
		}
		else{
		    er = r / Math.abs(r);
		    ec = 0;
		}
		
		int ri = r1 + er;
		int ci = c1 + ec;
		int a_ri = 8 - ri;
		int a_ci = 8 - ci;
	    
		while(!(ri == r2 && ci == c2)){
		    if(!(isEmpty(player, ri, ci) && isEmpty(a_player, a_ri, a_ci))){
			System.out.print(ri);
			System.out.print(" ");
			System.out.println(ci);
			return false;
		    }
		    ri += er;
		    ci += ec;
		    a_ri -= er;
		    a_ci -= ec;	
		}
	    }
	    return true;
	}
	
    }

    //isDeadEnd 歩、香、桂の場合のみおきる
    //歩と香は１段目(c=0)打つことができない
    //桂は１段目、２段目に(c==0||c==1)打つことができない

    //これ以上動けなくなる位置に設置してないか pieceが存在していることは確定している
    Boolean isDeadEnd(int player, int i, int r, int c){

	//playerのi番目の駒を取得する
	ChessPiece piece = getProperty(player, i);
	//歩と香の場合
	if(piece.printChessPiece().equals("歩") || piece.printChessPiece().equals("香")){

	    if(c == 0){
		return true;
	    }
	    else{
		return false;
	    }
	    
	}
	//桂の場合
	else if(piece.printChessPiece().equals("桂")){

	    if(c == 0 || c == 1){
		return true;
	    }
	    else{
		return false;
	    }

	}
	//それ以外の駒の場合
	else{
	    return false;
	}


    }

    //移動先がこれ以上動けなくなる位置ではないか
    Boolean isDeadEnd(int player, int r2 , int c2){

	//playerのi番目の駒を取得する                                                                                                          
        ChessPiece piece = getPosition(player, r2, c2);
        //歩と香の場合                                                                                                                         
        if(piece.printChessPiece().equals("歩") || piece.printChessPiece().equals("香")){

            if(c2 == 0){
                return true;
            }
            else{
                return false;
            }

        }
        //桂の場合                                                                                                                             
        else if(piece.printChessPiece().equals("桂")){

            if(c2 == 0 || c2 == 1){
                return true;
            }
            else{
                return false;
            }

        }
        //それ以外の駒の場合                                                                                                                   
        else{
            return false;
        }

	

    }

    

    /****************** 駒の操作 ********************************/

    //盤面上の駒を移動
    Boolean moveChessPiece(int player, int r1, int c1, int r2, int c2){
	if(setPosition(player, r2, c2, copyChessPiece(player, r1, c1))){
	    if(deletePosition(player, r1, c1)){
		return true;
	    }
	}

	return false;

    }

    // 持ち駒を盤面に出す
    Boolean putCapturedPiece(int player, int i, int r, int c){
	
	if(setPosition(player, r, c, copyChessPiece(player, i))){
	    if(deleteProperty(player, i)){
		return true;
	    }
	}
	
	return false;

    }
    /************ 指定した条件に合う駒を取得 *******************************/

    // 王または玉の位置を返す
    int[] getKingPosition(int player){
	ChessPiece position;
	int pos[] = {-1, -1};

	for(int r = 0; r < 9; r++){
	    for(int c = 0; c < 9; c++){

		position = getPosition(player, r, c);
		if(position != null){
		    if(position.printChessPiece().equals("王") || position.printChessPiece().equals("玉")){
			pos[0] = r;
			pos[1] = c;
			return pos;
		    }
		}

	    }
	}

	return pos;

    }

    // 開始位置(r1,c1)から目的位置(r2,c2)へ1マスずつ進んだ場合に
    // 最初にぶつかる駒の持ち主と位置を返す
    // (縦横斜めの関係にある2地点間のみ)
    // ぶつからなかった場合は-1を返す
    int[] closestChessPiece(int player, int r1, int c1, int r2, int c2){
	int r = r2 - r1;
	int c = c2 - c1;
	int a_player = Math.abs(player - 1);
	int[] pos = new int[3];

	// 縦横の場合
	if(r * c == 0 && r + c != 0){

	    int er,ec;

	    if(r == 0){
		er = 0;
		ec = c / Math.abs(c);
	    }
	    else{
		er = r / Math.abs(r);
		ec = 0;
	    }
	    
	    int ri = r1;
	    int ci = c1;
	    int a_ri = 8 - ri;
	    int a_ci = 8 - ci;

	    while(!(ri == r2 && ci == c2)){
		ri += er;
		ci += ec;
		a_ri = 8 - ri;
		a_ci = 8 - ci;

		if(!isEmpty(player, ri, ci)){
		    pos[0] = player;
		    pos[1] = ri;
		    pos[2] = ci;

		    for(int i = 0; i < 3; i++){
			if(i == 0){
			    System.out.print(pos[i]);
			    System.out.print(" ");
			}
			else{
			    System.out.print(pos[i] + 1);
			    System.out.print(" ");
			}
		    }
		    System.out.println();

		    return pos;
		}
		else if(!isEmpty(a_player, a_ri, a_ci)){
		    
		    pos[0] = a_player;
		    pos[1] = a_ri;
		    pos[2] = a_ci;

		    for(int i = 0; i < 3; i++){
			if(i == 0){
			    System.out.print(pos[i]);
			    System.out.print(" ");
			}
			else{
			    System.out.print(8 - pos[i] + 1);
			    System.out.print(" ");
			}
		    }
		    
		    System.out.println();

		    return pos;
		}
		
	    }


	}
	
	// 斜めの場合
	else if(Math.abs(r) == Math.abs(c) && r != 0){
	    int er = r / Math.abs(r);
	    int ec = c / Math.abs(c);
	    int ri = r1;
	    int ci = c1;
	    int a_ri = 8 - ri;
	    int a_ci = 8 - ci;
	    
	    while(ri != r2){
		ri += er;
		ci += ec;
		a_ri = 8 - ri;
		a_ci = 8 - ci;

		if(!isEmpty(player, ri, ci)){
		    pos[0] = player;
		    pos[1] = ri;
		    pos[2] = ci;

		    for(int i = 0; i < 3; i++){
			if(i == 0){
			    System.out.print(pos[i]);
			    System.out.print(" ");
			}
			else{
			    System.out.print(pos[i] + 1);
			    System.out.print(" ");
			}
		    }
		    System.out.println();

		    return pos;
		}
		else if(!isEmpty(a_player, a_ri, a_ci)){
		    pos[0] = a_player;
		    pos[1] = a_ri;
		    pos[2] = a_ci;

		    for(int i = 0; i < 3; i++){
			if(i == 0){
			    System.out.print(pos[i]);
			    System.out.print(" ");
			}
			else{
			    System.out.print(8 - pos[i] + 1);
			    System.out.print(" ");
			}
		    }
		    System.out.println();

		    return pos;
		}

	    }

	}

	// 位置指定が不正(-2を返す)
	else{
	    pos[0] = -2;
	    pos[1] = -2;
	    pos[2] = -2;
	    
	    return pos;
	}

	// 目的位置までずっと空マスだった
	pos[0] = -1;
	pos[1] = -1;
	pos[2] = -1;

	return pos;

    }

    // 指定した位置に移動可能な駒の位置の2次元配列を返す
    int[][] getMovablePiecePosition(int player, int r, int c){
	int r0, c0;
	int front[], back[], right[], left[], right_front[], left_front[], 
	    right_back[], left_back[], knight_right[], knight_left[];
	ArrayList<Integer> poslist = new ArrayList<Integer>();
	ChessPiece piece;

	// 指定位置に自身の駒が置かれていないか
	if(!isEmpty(player, r, c)){
	    return null;
	}

	// 前 closestChessPiece(int player, int r1, int c1, int r2, int c2)
	// 開始位置(r1,c1)から目的位置(r2,c2)へ1マスずつ進んだ場合に
	// 最初にぶつかる駒の持ち主と位置を返す
	// (縦横斜めの関係にある2地点間のみ)
	// ぶつからなかった場合は-1を返す
	// int[3]

	front = closestChessPiece(player, r, c, r, 0);

	//最初にぶつかった駒が自分の駒だったら
	if(front[0] == player){
	    
	    //最初にぶつかった駒の情報を入手
	    piece = getPosition(front[0], front[1], front[2]);

	    //Pieceが指定された位置に移動できるとすると
	    if(piece.isAbletoMove(front[1], front[2],  r, c)){
		poslist.add(front[1]);
		poslist.add(front[2]);
	    }
	    
	}
	
	// 後ろ
	back = closestChessPiece(player, r, c, r, 8);
	
	if(back[0] == player){
	    piece = getPosition(back[0], back[1], back[2]);
	    
	    if(piece.isAbletoMove(back[1], back[2], r, c)){
		poslist.add(back[1]);
		poslist.add(back[2]);
	    }
	    
	}
	
	// 右
	right = closestChessPiece(player, r, c, 0, c);
	
	if(right[0] == player){
	    piece = getPosition(right[0], right[1], right[2]);
	    
	    if(piece.isAbletoMove(right[1], right[2], r, c)){
		poslist.add(right[1]);
		poslist.add(right[2]);
	    }

	}

	// 左
	left = closestChessPiece(player, r, c, 8, c);

	if(left[0] == player){
	    piece = getPosition(left[0], left[1], left[2]);
	    
	    if(piece.isAbletoMove(left[1], left[2], r, c)){
		poslist.add(left[1]);
		poslist.add(left[2]);
	    }

	}

	// 右前
	for(r0 = r, c0 = c; r0 > 0 && c0 > 0; r0--, c0--){}

	right_front = closestChessPiece(player, r, c, r0, c0);

	if(right_front[0] == player){
	    piece = getPosition(right_front[0], right_front[1], right_front[2]);
	    
	    if(piece.isAbletoMove(right_front[1], right_front[2], r, c)){
		poslist.add(right_front[1]);
		poslist.add(right_front[2]);
	    }

	}

	// 左前
	for(r0 = r, c0 = c; r0 < 8 && c0 > 0; r0++, c0--){}

	left_front = closestChessPiece(player, r, c, r0, c0);

	if(left_front[0] == player){
	    piece = getPosition(left_front[0], left_front[1], left_front[2]);
	    
	    if(piece.isAbletoMove(left_front[1], left_front[2], r, c)){
		poslist.add(left_front[1]);
		poslist.add(left_front[2]);
	    }

	}

	// 右後ろ
	for(r0 = r, c0 = c; r0 > 0 && c0 < 8; r0--, c0++){}

	right_back = closestChessPiece(player, r, c, r0, c0);

	if(right_back[0] == player){
	    piece = getPosition(right_back[0], right_back[1], right_back[2]);
	    
	    if(piece.isAbletoMove(right_back[1], right_back[2], r, c)){
		poslist.add(right_back[1]);
		poslist.add(right_back[2]);
	    }

	}

	// 左後ろ
	for(r0 = r, c0 = c; r0 < 8 && c0 < 8; r0++, c0++){}

	left_back = closestChessPiece(player, r, c, r0, c0);

	if(left_back[0] == player){
	    piece = getPosition(left_back[0], left_back[1], left_back[2]);
	    
	    if(piece.isAbletoMove(left_back[1], left_back[2], r, c)){
		poslist.add(left_back[1]);
		poslist.add(left_back[2]);
	    }

	}

	// 桂馬位置右
	if(r != 0 && c != 7 && c != 8){
	    if(!isEmpty(player, r - 1, c + 2)){
		piece = getPosition(player, r - 1, c + 2);
	    
		if(piece.isAbletoMove(r - 1, c + 2, r, c)){
		    poslist.add(r - 1);
		    poslist.add(c + 2);
		}
    
	    }
	}

	// 桂馬位置左
	if(r != 8 && c != 7 && c!= 8){
	    if(!isEmpty(player, r + 1, c + 2)){
		piece = getPosition(player, r + 1, c + 2);
	    
		if(piece.isAbletoMove(r + 1, c + 2, r, c)){
		    poslist.add(r + 1);
		    poslist.add(c + 2);
		}

	    }
	}

	// GetterPieceが1つ以上ある場合
	//rが0の時エラーが出る
	if(poslist.size() > 0){


	    
	    int[][]  pos = new int[poslist.size() / 2][3];


	    // ArrayListを配列の配列に写す

	    /*
	    if(poslist.size() == 2){
		
		pos = new int[1][3];
		
		pos[0][0] = player;
		pos[0][1] = poslist.get(0);
		pos[0][2] = poslist.get(1);

		
	    }
	    else if(r == 0){
		

	    }
	    else{
	    
		for(int i = 0; i < pos.length; i++){
		    pos[i][0] = player;
		    pos[i][1] = poslist.get(i * 2);
		    pos[i][2] = poslist.get(i * 2 + 1);
		}

	    }
	    */

	    for(int i = 0; i < pos.length; i++){
		pos[i][0] = player;                                                                                          
		pos[i][1] = poslist.get(i * 2);                                                                              
		pos[i][2] = poslist.get(i * 2 + 1);                                                                          
	    }
	    
	    return pos;

	}
	// 該当する駒がなかった場合
	else{
	    return null;
	}

    }

    /************* チェックメイト(詰み)に関するメソッド ********************/

    // (そこに置いたら)二歩になってないか
    Boolean isDoublePawn(int player, int r){
	ChessPiece position;

	for(int i = 0; i < 9; i++){
	    position = getPosition(player, r, i);

	    if(position != null){
		if(position.printChessPiece().equals("歩")){
		    return true;
		}
	    }
	}

	return false;

    }

    // 指定した駒が一手で奪られる可能性はないか
    Boolean isGettable(int player, int r, int c){
	int a_player = Math.abs(player - 1);
	int front[], back[], right[], left[], right_front[], left_front[], 
	    right_back[], left_back[];
	int r0, c0;
	int a_r = 8 - r;
	int a_c = 8 - c;
	ChessPiece piece;

	// 前
	front = closestChessPiece(player, r, c, r, 0);

	if(front[0] == a_player){
	    piece = getPosition(front[0], front[1], front[2]);
	    
	    if(piece.isAbletoMove(front[1], front[2],  a_r, a_c)){
		System.out.println("前");
		String str = piece.printChessPiece() + front[0] + ": (" + front[1] + ", " + front[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 後ろ
	back = closestChessPiece(player, r, c, r, 8);

	if(back[0] == a_player){
	    piece = getPosition(back[0], back[1], back[2]);
	    
	    if(piece.isAbletoMove(back[1], back[2], a_r, a_c)){
		System.out.println("後ろ: ");
		String str = piece.printChessPiece() + back[0] + ": (" + back[1] + ", " + back[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 右	
	right = closestChessPiece(player, r, c, 0, c);

	if(right[0] == a_player){
	    piece = getPosition(right[0], right[1], right[2]);
	    
	    if(piece.isAbletoMove(right[1], right[2], a_r, a_c)){
		System.out.println("右: ");
		String str = piece.printChessPiece() + right[0] + ": (" + right[1] + ", " + right[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 左
	left = closestChessPiece(player, r, c, 8, c);

	if(left[0] == a_player){
	    piece = getPosition(left[0], left[1], left[2]);
	    
	    if(piece.isAbletoMove(left[1], left[2], a_r, a_c)){
		System.out.println("左: ");
		String str = piece.printChessPiece() + left[0] + ": (" + left[1] + ", " + left[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 右前
	for(r0 = r, c0 = c; r0 > 0 && c0 > 0; r0--, c0--){}

	right_front = closestChessPiece(player, r, c, r0, c0);

	if(right_front[0] == a_player){
	    piece = getPosition(right_front[0], right_front[1], right_front[2]);
	    
	    if(piece.isAbletoMove(right_front[1], right_front[2], a_r, a_c)){
		System.out.println("右前: ");
		String str = piece.printChessPiece() + right_front[0] + ": (" + right_front[1] + ", " + right_front[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 左前
 	for(r0 = r, c0 = c; r0 < 8 && c0 > 0; r0++, c0--){}

	left_front = closestChessPiece(player, r, c, r0, c0);

	if(left_front[0] == a_player){
	    piece = getPosition(left_front[0], left_front[1], left_front[2]);
	    
	    if(piece.isAbletoMove(left_front[1], left_front[2], a_r, a_c)){
		System.out.println("左前: ");
		String str = piece.printChessPiece() + left_front[0] + ": (" + left_front[1] + ", " + left_front[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 右後ろ
	for(r0 = r, c0 = c; r0 > 0 && c0 < 8; r0--, c0++){}

	right_back = closestChessPiece(player, r, c, r0, c0);

	if(right_back[0] == a_player){
	    piece = getPosition(right_back[0], right_back[1], right_back[2]);
	    
	    if(piece.isAbletoMove(right_back[1], right_back[2], a_r, a_c)){
		System.out.println("右後ろ: ");
		String str = piece.printChessPiece() + right_back[0] + ": (" + right_back[1] + ", " + right_back[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	// 左後ろ
	for(r0 = r, c0 = c; r0 < 8 && c0 < 8; r0++, c0++){}

	left_back = closestChessPiece(player, r, c, r0, c0);

	if(left_back[0] == a_player){
	    piece = getPosition(left_back[0], left_back[1], left_back[2]);
	    
	    if(piece.isAbletoMove(left_back[1], left_back[2], a_r, a_c)){
		System.out.println("左後ろ: ");
		String str = piece.printChessPiece() + left_back[0] + ": (" + left_back[1] + ", " + left_back[2] + ")";
		System.out.println(str);
		return true;
	    }

	}

	//r==0の場合領域外になる c==0 c==1
	int knight_right[] = {a_player, 8-(r-1), 8-(c-2)};


	// 桂馬位置右

	if( r != 0 && c != 0 && c != 1){
	    
	    if(!isEmpty(knight_right[0], knight_right[1], knight_right[2])){
		piece = getPosition(knight_right[0], knight_right[1], knight_right[2]);

		if(piece.isAbletoMove(r + 1, c + 2, r, c)){
		    System.out.println("桂馬位置右: ");
		    String str = piece.printChessPiece() + knight_right[0] + ": (" + knight_right[1] + ", " + knight_right[2] + ")";
		    System.out.println(str);
		    return true;
		}

	    }
	

	}

	//r==8の場合領域外エラーになるc==0 c==1

	// 桂馬位置左
	int knight_left[] = {a_player, 8-(r+1), 8-(c-2)};

	
	if(r != 8 && c != 0 && c != 1){
	    if(!isEmpty(knight_left[0], knight_left[1], knight_left[2])){
		piece = getPosition(knight_left[0], knight_left[1], knight_left[2]);

		if(piece.isAbletoMove(r - 1, c + 2, r, c)){
		    System.out.println("桂馬位置左: ");
		    String str = piece.printChessPiece() + knight_left[0] + ": (" + knight_left[1] + ", " + knight_left[2] + ")";
		    System.out.println(str);
		    return true;
		}

	    }
	}

	System.out.println("\n");

	// 王手になってなかったのでfalse
	return false;

    }

    // 今の盤面で自分が王手にされてないか
    Boolean isCheckmate(int player){
	int king[] = getKingPosition(player);
	
	return isGettable(player, king[0], king[1]);

	
    }

    // (そこに移動したら)自分が詰んじゃわないか
    Boolean isCheckmate(int player, int r1, int c1, int r2, int c2){
	Boolean checkmate;
	int a_player = Math.abs(player - 1);
	int a_r2 = 8 - r2;
	int a_c2 = 8 - c2;
	ChessPiece a_piece;

	if(!isEmpty(player, r2, c2)){
	    System.out.println("移動先に自分の駒があります");
	    return isCheckmate(player);
	}

	moveChessPiece(player, r1, c1, r2, c2);

	// 駒移動で相手の駒を奪った場合
	if(!isEmpty(a_player, a_r2, a_c2)){
	    a_piece = copyChessPiece(a_player, a_r2, a_c2);
	    deletePosition(a_player, a_r2, a_c2);
	
	    checkmate = isCheckmate(player);

	    setPosition(a_player, a_r2, a_c2, a_piece);
	}
	// 空マスへの駒移動の場合
	else{
	    checkmate = isCheckmate(player);
	}

	moveChessPiece(player, r2, c2, r1, c1);

	return checkmate;
    }

    // (そこに置いたら)自分が詰んじゃわないか
    Boolean isCheckmate(int player, int i, int r, int c){
	Boolean checkmate;
	ChessPiece piece = getProperty(player, i);
	
	if(isEmpty(player, r, c)){
	    setPosition(player, r, c, piece);

	    checkmate = isCheckmate(player);
	    
	    deletePosition(player, r, c);
	}
	else{
	    System.out.println("駒のある位置には置けません");

	    checkmate = false;
	}

	return checkmate;

    }



    // 王手をかけている相手の駒の位置の2次元配列を返す(王手でないならnullを返す)
    int[][] getCheckmatePiecePosition(int player){
	
	//playerのkingの位置を返す
	int king[] = getKingPosition(player);

	System.out.print("king[]:");

	for(int i = 0; i < king.length; i++)
	    System.out.print(" "+king[i]);

	System.out.println();
	
	//
	return getMovablePiecePosition(Math.abs(player - 1), 8 - king[0], 8 - king[1]);
    }

    /******************** 消去関係のメソッド **********************/

    // 盤面上の駒を消去
    Boolean deletePosition(int player, int r, int c){
	if(!isEmpty(player, r, c)){

	    switch(player){
	    case 0:
		position0[r][c] = null;
		return true;
	    case 1:
		position1[r][c] = null;
		return true;
	    default:
		return false;
	    }
	}
	else{
	    return false;
	}
    }

    // 持ち駒を消去
    Boolean deleteProperty(int player, int i){
	switch(player){
	case 0:
	    if(i < property0.size() && i >= 0){
		property0.remove(i);
		return true;
	    }
	case 1:
	    if(i < property1.size() && i >= 0){
		property1.remove(i);
		return true;
	    }
	default:
	    break;
	}
	return false;

    }

    /*********** 盤面のコピーに関するメソッド *******************/


    //p0の位置
    //ChessPiece[][] position0 = new ChessPiece[9][9];
    //p1の位置
    //ChessPiece[][] position1 = new ChessPiece[9][9];
    //p0の持ち駒
    //ArrayList<ChessPiece> property0 = new ArrayList<ChessPiece>();
    //p1の持ち駒
    //ArrayList<ChessPiece> property1 = new ArrayList<ChessPiece>();
    
    //自分のインスタンスのコピーを返す
    //return boardとしたら参照渡しになってしまう
    
    void copyBoard(Board board){

	
	int copypropertysize0 = board.getPropertySize(0);
	int copypropertysize1 = board.getPropertySize(1);


	for(int i = 0; i < 9; i++){
	    for(int j = 0; j < 9; j++){
		setPosition(0,i,j,board.copyChessPiece(0,i,j));
	    }
	}

	for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
		setPosition(1,i,j,board.copyChessPiece(1,i,j));
            }
        }

	for(int i = 0; i < copypropertysize0; i++){
	    setProperty(0,board.copyChessPiece(0,i));
	}

	for(int i = 0; i < copypropertysize1; i++){
	    setProperty(1,board.copyChessPiece(1,i));
	}


    }
    

    /*********** 駒のコピーに関するメソッド *********************/

    // 駒のコピーを返す
    ChessPiece copyChessPiece(ChessPiece piece){
	Boolean surface = piece.getSurface();
	ChessPiece cp;

	piece.reset();

	switch(piece.printChessPiece()){
	case "歩":
	    cp = new Pawn();
	    break;
	case "香":
	    cp = new Lance();
	    break;
	case "桂":
	    cp = new Knight();
	    break;
	case "銀":
	    cp = new SilverGeneral();
	    break;
	case "金":
	    cp = new GoldGeneral();
	    break;
	case "飛":
	    cp = new Rook();
	    break;
	case "角":
	    cp = new Bishop();
	    break;
	case "王":
	    cp = new KingOu();
	    break;
	case "玉":
	    cp = new KingGyoku();
	    break;
	default:
	    return null;
	}

	cp.setSurface(surface);
	piece.setSurface(surface);

	return cp;

    }

    // 盤面上の駒のコピーを返す
    ChessPiece copyChessPiece(int player, int r, int c){

	if(getPosition(player, r, c) == null)
	    return null;
	else{
	    ChessPiece cp =  copyChessPiece(getPosition(player, r, c));
	    return cp;
	}
    }

    // 盤面上の駒をコピー・初期化して(表にして)返す
    ChessPiece copyChessPieceReset(int player, int r, int c){

	if(getPosition(player, r, c) == null)
	    return null;
	else{
	    ChessPiece cp = copyChessPiece(getPosition(player, r, c));
	    cp.reset();
	    return cp;
	}
    }

    // 持ち駒のコピーを返す
    ChessPiece copyChessPiece(int player, int i){

	if(getProperty(player, i) == null)
	    return null;
	else{
	    ChessPiece cp = copyChessPiece(getProperty(player, i));
	    return cp;
	}
    }

    /****************** 画面表示関係のメソッド ************************/

    // 指定されたマスの駒表示
    void printPosition(int player, int r, int c){
	switch(player){
	case 0:
	    System.out.print(position0[r][c].printChessPiece());
	    System.out.print("0");
	    break;
	case 1:
	    System.out.print(position1[r][c].printChessPiece());
	    System.out.print("1");
	    break;
	default :
	    break;
	}
    }

    // 持ち駒表示
    int printProperty(int player){
	System.out.print(" ");

	switch(player){
	case 0:
	    for(int i = 0; i < property0.size(); i++){
		System.out.print(" ");
		System.out.print(i);
		System.out.print("  ");
	    }
	    
	    System.out.println();

	    for(int i = 0; i < property0.size(); i++){
		System.out.print("  ");
		System.out.print(property0.get(i).printChessPiece());
	    }
	    return property0.size();

	case 1:
	    for(int i = 0; i < property1.size(); i++){
		System.out.print(" ");
		System.out.print(i);
		System.out.print("  ");
	    }
	    
	    System.out.println();

	    for(int i = 0; i < property1.size(); i++){
		System.out.print("  ");
		System.out.print(property1.get(i).printChessPiece());
	    }
	    return property1.size();

	default:
	    return -1;
	}

    }

    // 盤面を表示
    void printBoard(int player){
	int a_player = Math.abs(player - 1);
	int a_r, a_c;

	// 相手の持ち駒
	System.out.print(" 相手の持ち駒(プレイヤー");
	System.out.print(a_player);
	System.out.println(")");

	if(printProperty(a_player) == 0){
	    System.out.println("なし\n");
	}
	else{
	    System.out.println("\n");
	}

	System.out.print("    ");
	
	// 列番号
	for(int r = 9; r > 0; r--){
	    System.out.print(" ");
	    System.out.print(r);
	    System.out.print("  ");
	}

	System.out.println();
	System.out.print("   ");

	// 横仕切り線
	for(int r = 0; r < 9; r++){
	    System.out.print("----");
	}

	for(int c = 0; c < 9; c++){
	    if(c == 0){
		System.out.println("");
	    }
	    else{
		System.out.println("|");
	    }
	    System.out.print("   ");

	    for(int r = 8; r >= 0; r--){
		a_r = 8 - r;
		a_c = 8 - c;

		System.out.print("|");

		// playerの駒がある場合
		if(!isEmpty(player, r, c)){
		    printPosition(player, r, c);
		}
		// a_playerの駒がある場合
		else if(!isEmpty(a_player, a_r, a_c)){
		    printPosition(a_player, a_r, a_c);
		}
		// なにもないマスの場合
		else{
		    System.out.print("   ");
		}

	    }
	    
	    // 縦仕切り線
	    System.out.print("| ");

	    // 行番号
	    System.out.println(c + 1);
	    System.out.print("   ");

	    // 横仕切り線
	    for(int r = 0; r < 9; r++){
		System.out.print("----"); 
	    }

	}

	System.out.println("\n");

	// 自分の持ち駒
	System.out.print(" 自分の持ち駒(プレイヤー");
	System.out.print(player);
	System.out.println(")");
	if(printProperty(player) == 0){
	    System.out.println("なし\n");
	}
	else{
	    System.out.println("\n");
	}

    }

    /*************** 画面表示用のStringを返す *********************/

    // 盤面の状況をStringで返す
    public String GetPrintBoard(int player){
        
        String PrintBoard = "\n";
        int a_player = Math.abs(player - 1);
        int a_r, a_c;
        
        // 相手の持ち駒
        PrintBoard += " 相手の持ち駒(プレイヤー";
        PrintBoard += String.valueOf(a_player);
        PrintBoard += ")\n";
        
        PrintBoard += GetPrintProperty(a_player);
        
        if(getPropertySize(a_player) == 0){
            PrintBoard += "なし\n\n";
        }
        else{
            PrintBoard += "\n\n";
        }
        
        PrintBoard += "   ";
        
        // 列番号
        for(int r = 9; r > 0; r--){
            PrintBoard += " ";
            PrintBoard += String.valueOf(r);
            PrintBoard += "  ";
        }
        
        PrintBoard += "\n";
        PrintBoard += "   ";
        
        
        // 横仕切り線
        for(int r = 0; r < 9; r++){
            PrintBoard += "----";
        }
        
        for(int c = 0; c < 9; c++){
            if(c == 0){
                PrintBoard += "\n";
            }
            else{
                PrintBoard += "|\n";
            }
            
            PrintBoard += "   ";
            
            for(int r = 8; r >= 0; r--){
                a_r = 8 - r;
                a_c = 8 - c;
                
                PrintBoard += "|";
                
                // playerの駒がある場合
                if(!isEmpty(player, r, c)){
                    PrintBoard += GetPrintPosition(player, r, c);
                }
                // a_playerの駒がある場合
                else if(!isEmpty(a_player, a_r, a_c)){
                    PrintBoard += GetPrintPosition(a_player, a_r, a_c);
                }
                // なにもないマスの場合
                else{
                    PrintBoard += "   ";
                }
                
            }
            
            // 縦仕切り線
            PrintBoard += "| ";
            
            // 行番号
            PrintBoard += String.valueOf(c+1);
            PrintBoard += "\n";
            PrintBoard += "   ";
            
            // 横仕切り線
            for(int r = 0; r < 9; r++){
                PrintBoard += "----";
            }
            
        }
        
        PrintBoard += "\n\n";
        
        // 自分の持ち駒
        PrintBoard += " 自分の持ち駒(プレイヤー";
        PrintBoard += String.valueOf(player);
        PrintBoard += ")\n";
        PrintBoard += GetPrintProperty(player);
        
        if(getPropertySize(player) == 0){
            PrintBoard += "なし\n\n";
        }
        else{
            PrintBoard += "\n\n";
        }
        
        return PrintBoard;
    }

    
    // 持ち駒をStringで返す
    public String GetPrintProperty(int player){
        
        String PrintProperty = " ";
        
        switch(player){
                
            case 0:
                for(int i = 0; i < property0.size(); i++){
                    PrintProperty += " ";
                    PrintProperty += String.valueOf(i);
                    PrintProperty += "  ";
                }
                
                PrintProperty += "\n";
                
                for(int i = 0; i < property0.size(); i++){
                    PrintProperty += "  ";
                    PrintProperty += property0.get(i).printChessPiece();
                }
                return PrintProperty;
            case 1:
                for(int i = 0; i < property1.size(); i++){
                    PrintProperty += " ";
                    PrintProperty += String.valueOf(i);
                    PrintProperty += "  ";
                }
                
                PrintProperty += "\n";
                
                for(int i = 0; i < property1.size(); i++){
                    PrintProperty += "  ";
                    PrintProperty += property1.get(i).printChessPiece();
                }
                
                return PrintProperty;
            default:
                return PrintProperty;
                
        }
        
    }

    // 盤面の1マス分の状態をStringで返す
    public String GetPrintPosition(int player, int r, int c){
        
        String ChessPrint = "";
        
        switch(player){
            case 0:
                ChessPrint += position0[r][c].printChessPiece();
                ChessPrint += "0";
                break;
            case 1:
                ChessPrint += position1[r][c].printChessPiece();
                ChessPrint += "1";
                break;
            default:
                break;
        }
        
        return ChessPrint;
        
    }


    
}
