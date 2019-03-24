import java.util.*;
import java.io.*;

public class GameRecord{

    /* 盤面の位置番号は先手(プレイヤー0)から見た場合に合わせていると仮定する */


   // フィールド
    ArrayList<Move> record = new ArrayList<Move>();
    ArrayList<StateCount> sclist = new ArrayList<StateCount>();
    ArrayList<State> staterecord = new ArrayList<State>();
    

   // メソッド

/***************** sclist,staterecordを同時に編集するメソッド *****************/

    // 一手分更新
    void renew(int player, Board board){

	renewStateCountList(player, board);
	pushStateRecord(player, board);

    }

    // 一手前に戻す
    void undo(int player, Board board){

	substractStateCount(player, board);
	undoStateRecord(board);

    }

    // リセット
    void reset(int firstPlayer, Board board){

	resetStateCountList(firstPlayer);
	resetStateRecord(firstPlayer, board);

    }

    

/***************** record に関するメソッド ***********************************/

    // 1手分プッシュ
    void pushRecord(int... array){
	record.add(new Move(array));
    }

    // 1手分プッシュ
    void pushRecord(int player, ArrayList<Integer> input){
	int[] array = new int[input.size() + 1];

	array[0] = player;
	for(int i = 1; i < array.length; i++){
	    array[i] = input.get(i - 1);
	}

	record.add(new Move(array));

    }

    // 盤面の位置番号をプレイヤー0/１基準を切り替えてプッシュ
    void pushRecord_switch(int... array){

	int head = 2 + array[1];

	for(int i = head; i < array.length; i++){
	    array[i] = 8 - array[i];
	}

	record.add(new Move(array));
	
    }
    
    // 盤面の位置番号をプレイヤー0/１基準を切り替えてプッシュ
    void pushRecord_switch(int player, ArrayList<Integer> input){
	int[] array = new int[input.size() + 1];

	array[0] = player;
	array[1] = input.get(0);

	if(array[1] <= 1 && array[1] >= 0){

	    int head = 2 + array[1];
	    
	    array[head - 1] = input.get(head - 2);
	    
	    for(int i = head; i < array.length; i++){
		array[i] = 8 - input.get(i - 1);
	    }

	}

	record.add(new Move(array));
	
    }

    // 1手分ポップ
    int[] popRecord(){
	int[] array;

	array = record.get(record.size() - 1).getMove();

	record.remove(record.size() - 1);

	return array;

    }

    // 最新の1手のデータを返す(ポップと違って消えない)
    int[] getLatestMove(){
	return record.get(record.size() - 1).getMove();
    }


    // 初手から最新の1手までの記録を表示
    void printGameRecord(){
	Board board1 = new Board();
	Board board2 = new Board();
	Judge judge = new Judge();
	ArrayList<Integer> list = new ArrayList<Integer>();
	String str;
	
	// 初手から最後の手まで繰り返す
	for(int i = 0; i < record.size(); i++){

	    list.clear();
	    int array[] = record.get(i).getMove();

	    // 配列をArrayListに写す
	    for(int j = 1; j < array.length; j++){
		list.add(array[j]);
	    }
	    
	    // 盤面更新
	    int mes[] = judge.renewBoard(array[0], board1, list);

	    // 盤面更新成功の場合
	    if(mes[Math.abs(array[0] - 1)] == 2 ){

		for(int j = 2; j < array.length; j++){
		    array[j]++;
		}

		i++;

		switch(array[1]){
		case 0:
		    str = i + ": プレイヤー" + array[0] + " " + board2.getPosition(array[0], array[2], array[3]).printChessPiece() + "(" + array[2] + ", " + array[3] + ") → " + board1.getPosition(array[0], array[4], array[5]).printChessPiece() + "(" + array[4] + ", " + array[5] + ")";
		    System.out.println(str);
		    break;
		case 1:
		    str = i + ": プレイヤー" + array[0] + " " + board2.getProperty(array[0], array[2]).printChessPiece() + "(" + array[2] + ") → (" + array[3] + ", " + array[4] + ")";
		    System.out.println(str);
		    break;
		case 2:
		    str = i + ": プレイヤー" + array[0] + "が投了";
		    System.out.println(str);
		    break;
		case 3:
		    str = i + ": プレイヤー" + array[0] + "が直前に移動させた駒をひっくり返す";
		    System.out.println(str);
		    break;
		case 4:
		    str = i + ": プレイヤー" + array[0] + "が直前に移動させた駒をひっくり返さない";
		}

		i--;

		for(int j = 2; j < array.length; j++){
		    array[j]--;
		}

		judge.renewBoard(array[0], board2, list);

	    } // if(mes[Math.abs(array[0] - 1)] == 2 )

	    

	} // for(int i = 0; i < record.size(); i++)	

    }

    // 記録すべてを列挙
    void printGameRecordSimple(){

	System.out.println("第n手：a[0] a[1] a[2] a[3] a[4] a[5]\n");
	System.out.println("添字 0 : プレイヤー");
	System.out.println("     1 : 動作の種類 || 0:駒移動 | 1:持ち駒使用 | 2:投了 | 3:成る | 4:成らない |");
	System.out.println("     2 :            || 移動前列 |  持ち駒番号  |        |        |            |");
	System.out.println("     3 :            || 移動前行 |   移動後列   |        |        |            |");
	System.out.println("     4 :            || 移動後列 |   移動後行   |        |        |            |");
	System.out.println("     5 :            || 移動後行 |              |        |        |            |\n");

	for(int i = 0; i < record.size(); i++){
	    System.out.print(i+1);
	    System.out.print(": ");
	    record.get(i).printMove();
	}

    }
   
/***************** sclist に関するメソッド ******************************/

    // sclistを更新する
    void renewStateCountList(int player, Board board){

	StateCount sc = new StateCount();
	Boolean comp = false;

	sc.setState(player, board);
	String state = sc.getState();

	//　過去に出現した状況の場合
	for(int i = sclist.size() - 1; i >= 0; i--){
	    if(state.equals(sclist.get(i).getState() ) ){
		sclist.get(i).addCount();
		comp = true;
		break;
	    }
	}

	// 始めて出現した状況だった場合
	if(!comp){
	    sclist.add(sc);
	    sclist.get(sclist.size() - 1).addCount();
	    comp = true;
	}
	
    }
    

    // 指定した状況の出現回数を返す
    int getStateCount(int player, Board board){

	StateCount sc = new StateCount();

	sc.setState(player, board);
	String state = sc.getState();

	for(int i = sclist.size() - 1; i >= 0; i--){
	    if(state.equals(sclist.get(i).getState() ) ){
		return sclist.get(i).getCount();
	    }
	}

	return 0;
    }

    // 千日手を検出(状況を指定)
    Boolean isTreefoldRepetition(int player, Board board){
	
	State st = new State();
	st.setState(player, board);
	String state = st.getState();
	
	for(int i = sclist.size() - 1; i >= 0; i--){

	    if(sclist.get(i).getState().equals(state)){

		if(sclist.get(i).getCount() >= 4){
		    return true;
		}
		else{
		    return false;
		}

	    }

	}

	return false;
    }

    // 千日手を検出(全探索)
    Boolean isTreefoldRepetition(){

	for(int i = sclist.size() - 1; i >= 0; i--){

	    if(sclist.get(i).getCount() >= 3){
		return true;
	    }
	}

	return false;

    }


    // sclistリセット
    void resetStateCountList(int firstPlayer){

	sclist.clear();

	Board board = new Board();
	StateCount sc = new StateCount();
	sc.setState(firstPlayer, board);
	sc.addCount();

	sclist.add(sc);

    }

    // sclistのcountをリセット
    void resetStateCount(){

	for(int i = 0; i < sclist.size(); i++){
	    sclist.get(i).resetCount();
	}

    }

    // 指定したsclistの要素のcountを一つ減らす(0になったら削除)
    void substractStateCount(int player, Board board){

	State st = new State();
	st.setState(player, board);
	String state = st.getState();

	for(int i = sclist.size() - 1; i >= 0; i--){

	    if(sclist.get(i).getState().equals(state)){
		
		if(sclist.get(i).getCount() > 0){
		    sclist.get(i).subCount();
		}
		else{
		    sclist.remove(i);
		}

            //return true;
	    }
	}

        //return false;

    }

/********************* staterecord に関するメソッド **************************/

    // ゲームの状況を一手進める(staterecordのプッシュ)
    void pushStateRecord(int player, Board board){

	State state = new State();

	state.setState(player, board);
	staterecord.add(state);
    }


    // ゲームの状況を一手前に戻す(staterecordのポップ)
    State popStateRecord(){
	
	State state = staterecord.get(staterecord.size() - 1);
	staterecord.remove(staterecord.size() - 1);

	return state;
	
    }

    // ゲームの状況を一手前に戻して手番を返す(staterecordとboardを一手前に戻す)
    int undoStateRecord(Board board){
        
	Board bb = new Board();
        
	State st = popStateRecord();

	bb = st.restoreBoard();
        
	board.copyBoard(bb);

	return st.restorePlayer();

    }
    
    Board undoStateRecordBoard(){
        
        Board bb = new Board();
        
        State st = popStateRecord();
        
        bb = st.restoreBoard();
        
        return bb;
        
    }

    // staterecordリセット
    void resetStateRecord(int firstPlayer, Board board){

	Board bb = new Board();

	board.copyBoard(bb);
	staterecord.clear();

	pushStateRecord(firstPlayer, board);

    }



}





/*****************************************************************************/


// 1手分のデータクラス
class Move{

    // フィールド
    ArrayList<Integer> move = new ArrayList<Integer>();

    /* 添字 0 : プレイヤー
            1 : 動作の種類 || 0:駒移動 | 1:持ち駒使用 | 2:投了 | 3:成る | 4:成らない |
	    2 :            || 移動前列 |  持ち駒番号  |        |        |            |
	    3 :            || 移動前行 |   移動後列   |        |        |            |
	    4 :            || 移動後列 |   移動後行   |        |        |            |
	    5 :            || 移動後行 |              |        |        |            |
     */

    // 初期化
    Move(int[] array){

	if(array.length >= 2 && (array[1] >= 0 && array[1] <= 4) ){

	    for(int i = 0; i < array.length; i++){
		move.add(array[i]);
	    }
	    
	}
	else{
	    System.out.println("初期化失敗");
	    System.out.println("想定外のパラメータ");
	}
			

    }

    
    // 駒移動を記録
    void setMove(int player, int type, int r1, int c1, int r2, int c2){

	if(type == 0){
	    move.clear();

	    move.add(player);
	    move.add(type);
	    move.add(r1);
	    move.add(c1);
	    move.add(r2);
	    move.add(c2);
	}
	else{
	    System.out.println("駒移動以外で使わないでください");
	}

    }

    // 持ち駒使用を記録
    void setMove(int player, int type, int r, int c){

	if(type == 1){
	    move.clear();

	    move.add(player);
	    move.add(type);
	    move.add(r);
	    move.add(c);
	}
	else{
	    System.out.println("持ち駒使用以外で使わないでください");
	}

    }

    // 投了/成る/成らないを記録
    void setMove(int player, int type){

	if(type >= 2 && type <= 4){
	    move.clear();
	    
	    move.add(player);
	    move.add(type);
	}
	else{
	    System.out.println("投了/成る/成らない以外で使わないでください");
	}	    

    }

    // データをセット
    void setMove(int[] array){

	if(array.length >= 2){

	    switch(array[1]){
	    case 0:
		setMove(array[0], array[1], array[2], array[3], array[4], array[5]);
		break;
	    case 1:
		setMove(array[0], array[1], array[2], array[3]);
		break;
	    case 2:
		setMove(array[0], array[1]);
		break;
	    default:
		System.out.println("想定外の動作番号");
		System.exit(1);
	    }
	}
	else{
	    System.out.println("リストにデータが入ってないですよ");
	    System.exit(1);
	}

    }

    // moveのゲッター
    int[] getMove(){

	int[] array = new int[move.size()];

	for(int i = 0; i < array.length; i++){
	    array[i] = move.get(i);
	}

	return array;

    }

    // moveの中身表示
    void printMove(){

	for(int i = 0; i < move.size(); i++){
	    int move_1 = move.get(i);
	    
	    if(i > 1){
		move_1++;
	    }

	    System.out.print(move_1);
	    System.out.print(" ");
	}

	System.out.println();

    }


}


/****************************************************************************/

// 千日手用：ある状況(駒配置,持ち駒,手番)が現れた回数を記録する
class StateCount{

   // フィールド
    private State state = new State(); // 状況(Boardの情報＋手番)
    private int count; // stateの出現回数

   // メソッド

    // 初期化
    StateCount(){
	count = 0;
    }

    // stateのゲッター
    String getState(){
	return state.getState();
    }

    // stateのセッター
    void setState(int player, Board board){
	state.setState(player, board);
    }
    
    // countのゲッター
    int getCount(){
	return count;
    }

    // countのセッター(インクリメント)
    void addCount(){
	count++;
    }

    // countのセッター(デクリメント)
    void subCount(){
	count--;
    }

    // countのリセット
    void resetCount(){
	count = 0;
    }


}

class State{

   // フィールド
    String state;

    // stateのゲッター
    String getState(){
	return state;
    }

    // stateのセッター
    void setState(int player, Board board){
	state = compressState(player, board);
    }

   // メソッド
    // stateに記録するために状態情報を圧縮してStringにする
    String compressState(int player, Board board){
	
	String str = compressBoard(board) + "*" + player;

	return str;
    }

    // Boardインスタンスを圧縮してStringにする
    String compressBoard(Board board){

	String str = "";
	ChessPiece piece;

	// 盤面の駒配置を記録
	for(int r = 0; r < 9; r++){
	    for(int c = 0; c < 9; c++){

		piece = board.getPosition(0, r, c);

		// プレイヤー0の駒がある場合
		if(piece != null){
		    str += "0" + compressChessPiece(piece);
		}
		else{
		    piece = board.getPosition(1, 8-r, 8-c);

		// プレイヤー1の駒がある場合
		    if(piece != null){
			str += "1" + compressChessPiece(piece);
		    }
		// 空マスの場合
		    else{
			str += "_";
		    }

		}

	    }// for(int c = 0; c < 9; c++)
	}// for(int r = 0; r < 9; r++)


	// 持ち駒
	int[][] hand = new int[2][7];

	for(int p = 0; p <= 1; p++){
	    for(int i = 0; i < board.getPropertySize(p); i++){
		
		switch(board.getProperty(p, i).printChessPiece() ){

		case "歩":
		    hand[p][0]++;
		    break;
		case "香":
		    hand[p][1]++;
		    break;
		case "桂":
		    hand[p][2]++;
		    break;
		case "銀":
		    hand[p][3]++;
		    break;
		case "金":
		    hand[p][4]++;
		    break;
		case "飛":
		    hand[p][5]++;
		    break;
		case "角":
		    hand[p][6]++;
		    break;
		}
	    
	    } // for(int i = 0; i < board.getPropertySize(p); i++)
	} // for(int p = 0; p <= 1; p++)
	
	String str_p[] = {"p", "l", "n", "s", "g", "r", "b"};

	for(int p = 0; p <= 1; p++){

	    str += "*";

	    for(int j = 0; j < 7; j++){	
		if(hand[p][j] > 0){
		    str += str_p[j] + hand[p][j];
		}
	    }

	}

	return str;
    }


    // ChessPieceインスタンスを圧縮してStringにする
    String compressChessPiece(ChessPiece piece){

	Boolean surface = piece.getSurface();
	String str_surface;
	String str;

	if(surface){
	    str_surface = "t";
	}
	else{
	    str_surface = "f";
	}

	piece.reset();

	switch(piece.printChessPiece()){
	case "歩":
	    str = "p" + str_surface;
	    break;
	case "香":
	    str = "l" + str_surface;
	    break;
	case "桂":
	    str = "n" + str_surface;
	    break;
	case "銀":
	    str = "s" + str_surface;
	    break;
	case "金":
	    str = "g";
	    break;
	case "飛":
	    str = "r" + str_surface;
	    break;
	case "角":
	    str = "b" + str_surface;
	    break;
	case "王":
	    str = "k";
	    break;
	case "玉":
	    str = "j";
	    break;
	default:
	    return null;
	}

	piece.setSurface(surface);

	return str;

    }

    // 手番の復元
    int restorePlayer(){

	String st = getState();

	switch(st.charAt(st.length() - 1) ){
	case '0':
	    return 0;
	case '1':
	    return 1;
	default:
	    return -1;
	}
	    
    }


    // Boardインスタンスの復元
    Board restoreBoard(){

	String st = getState();
	Board board = new Board();
    System.out.println(st);
	String str_p = "";
	int i = 0;

	// 盤面の復元
	for(int r = 0; r < 9; r++){
	    for(int c = 0; c < 9; c++){
            System.out.println("st.charAt(i)->"+st.charAt(i));
            System.out.println("str_p->"+str_p);
		switch(st.charAt(i)){
		    
		case '0':
		    i++;
		    str_p = st.substring(i, i + 1);
		    i++;

		    if(!(st.charAt(i) == '0' || st.charAt(i) == '1' 
			 || st.charAt(i) == '*' || st.charAt(i) == '_') ){
			str_p += st.substring(i, i + 1);
			i++;
		    }

		    board.setPosition(0, r, c, restoreChessPiece(str_p));
		    board.deletePosition(1, 8 - r, 8 - c);
		    break;

		case '1':
		    i++;
		    str_p = st.substring(i, i + 1);
		    i++;

		    if(!(st.charAt(i) == '0' || st.charAt(i) == '1' 
			 || st.charAt(i) == '*' || st.charAt(i) == '_') ){
			str_p += st.substring(i, i + 1);
			i++;
		    }

		    board.deletePosition(0, r, c);
		    board.setPosition(1, 8 - r, 8 - c, restoreChessPiece(str_p));
		    break;

		case '_':
		    board.deletePosition(0, r, c);
		    board.deletePosition(1, 8 - r, 8 - c);
		    i++;
		    break;

		default:
		    System.out.println("default:圧縮データが正しくありません");
		    System.exit(1);
		    
		} // switch(st.charAt(i))
	    } // for(int c = 0; c < 9; c++)
	} // for(int r = 0; r < 9; r++)

	if(st.charAt(i) == '*'){
	    i++;
	}
	else{
	    System.out.println("圧縮データが正しくありません");
	    System.exit(1);
	}

	// 持ち駒の復元(未完成)
	for(int p = 0; p <= 1; p++){
	    while(st.charAt(i) != '*'){

		switch(st.charAt(i)){
		case 'p':
		    i++;

		    int digit; // 桁数

		    for(digit = 0; Character.isDigit(st.charAt(i)); digit++, i++){}

		    int num = Integer.parseInt(st.substring(i - digit, i) );
		    
		    for(int j = 0; j < num; j++){
			board.setProperty(p, new Pawn());
		    }

		    break;
		case 'l':
		    i++;

		    for(int j = 0; j < Integer.parseInt(st.substring(i, i + 1)); j++){
			board.setProperty(p, new Lance());
		    }

		    i++;
		    break;
		case 'n':
		    i++;

		    for(int j = 0; j < Integer.parseInt(st.substring(i, i + 1)); j++){
			board.setProperty(p, new Knight());
		    }

		    i++;
		    break;
		case 's':
		    i++;

		    for(int j = 0; j < Integer.parseInt(st.substring(i, i + 1)); j++){
			board.setProperty(p, new SilverGeneral());
		    }

		    i++;
		    break;
		case 'g':
		    i++;

		    for(int j = 0; j < Integer.parseInt(st.substring(i, i + 1)); j++){
			board.setProperty(p, new GoldGeneral());
		    }

		    i++;
		    break;
		case 'r':
		    i++;

		    for(int j = 0; j < Integer.parseInt(st.substring(i, i + 1)); j++){
			board.setProperty(p, new Rook());
		    }

		    i++;
		    break;
		case 'b':
		    i++;

		    for(int j = 0; j < Integer.parseInt(st.substring(i, i + 1)); j++){
			board.setProperty(p, new Bishop());
		    }

		    i++;
		    break;
		}

	    } // while(st.charAt(i) != '*')

	    i++;

	} // for(int p = 0; p <= 1; p++)



	return board;

    }

    // ChessPieceインスタンスの復元
    ChessPiece restoreChessPiece(String str_p){

	ChessPiece piece;

	switch(str_p.charAt(0)){
	case 'p':
	    piece = new Pawn();
	    break;
	case 'l':
	    piece = new Lance();
	    break;
	case 'n':
	    piece = new Knight();
	    break;
	case 's':
	    piece = new SilverGeneral();
	    break;
	case 'g':
	    piece = new GoldGeneral();
	    break;
	case 'r':
	    piece = new Rook();
	    break;
	case 'b':
	    piece = new Bishop();
	    break;
	case 'k':
	    piece = new KingOu();
	    break;
	case 'j':
	    piece = new KingGyoku();
	    break;
	default:
	    return null;
	}

	if(str_p.length() == 2){
	    switch(str_p.charAt(1)){
	    case 't':
		piece.setSurface(true);
		break;
	    case 'f':
		piece.setSurface(false);
		break;
	    default:
		return null;
	    }
	}

	return piece;

    }


}
