//複数接続Socket通信サンプルプログラム(サーバー)
//クライアントからの接続を待ち、接続が行われたら
//一行のデータを受け取り、コンソールに表示して接続を切断する。
//複数のクライアントとの通信をスレッドにより行う。
//プログラム終了は、コマンドプロンプトでCTRL-C



import java.net.*;
import java.io.*;
import java.util.*;



class MultiServerSample
{


    
    public static void main(String[] args){

	//将棋は二人対戦なので二人からの通信がきたら
	//ゲームスタート

	//クライアントからの通信がきたらそのソケットどんどん
	//ClientSocketListに追加していく
	//LinkedListはpop addが可能
	LinkedList<Socket> ClientSocketList = new LinkedList<Socket>();

	
	//サーバー側のソケット接続口
	ServerSocket serverSoc = null;

	try{
	    //ポート番号は、 30000
	    //ソケットを作成

	    serverSoc = new ServerSocket(30000);
	    boolean flag = true;

	    System.out.println("Waiting for Connection.");

	    while(flag){
		Socket socket =  null;
		socket = serverSoc.accept();

		//accept()は、クライアントからの接続要求があるまでブロックする。
		//接続があれば次の命令に移る。
		//スレッドを起動し、クライアントと通信する。

		//ClientSocketListにクライアントからのsocketを追加していく
		ClientSocketList.add(socket);

		//ClientSocketListの要素数が2以上になったら
		//そのなかからsocketを二つpopして
		//ClientSocketArrayListに追加していく
		//そして将棋のプログラムに遷移させる

		if(ClientSocketList.size() >= 2){

		    ArrayList<Socket> ClientSocketArrayList = new ArrayList<Socket>();

		    ClientSocketArrayList.add(ClientSocketList.pop());
		    ClientSocketArrayList.add(ClientSocketList.pop());
		    
		    new SrvThread(ClientSocketArrayList).start();
		}

		System.out.println("Waiting for New Connection.");

	    }
	}
	catch (IOException e){
	    System.out.println("IOException!");
	    e.printStackTrace();
	}
	finally{
	    try{
		if(serverSoc != null){
		    serverSoc.close();
		}
	    }
	    catch (IOException ioex){
		ioex.printStackTrace();
	    }
	}

    }

}

class SrvThread extends Thread implements Serializable{

    ArrayList<Socket> soc = null;

    //デフォルトコンストラクタ引数にはSocketのArrayListクラス
    public SrvThread(ArrayList<Socket> socket){
	System.out.println("Thread is Generated. Connect to ");
	soc = socket;
	Iterator iter = soc.iterator();

	while(iter.hasNext()){
	    System.out.println(((Socket)iter.next()).getInetAddress());
	}


    }

    /*
    //クライアントからの入力を受け付け返す
    public String GetClientsInput(Socket sc){


	String input = null;
	String resultInput = null;

	try{
	
	    BufferedReader is = new BufferedReader(new InputStreamReader(sc.getInputStream()));
	
	    while((input = is.readLine()) != null){
		resultInput += input;
		System.out.println(input);
	    }
	}
	catch(IOException ex){
	    ex.printStackTrace();
	}
	finally{

	    return resultInput;
	    
	}
	
    }

    */

    //ArrayList<Integer> result = ois.readObject();でコンパイルエラー
    //が出ないようにする
    @SuppressWarnings("unchecked")
    public static <T> T autoCast(Object obj) {
	T castObj = (T) obj;
	return castObj;
    }

    public void run(){
	try{

	    
	    //socketからのデータはInputStreamReaderに送り、さらに
	    //BufferedReaderによってバッファリングする。

	    /*
	    //クライアントからのデータを読み込むためのもの
	    BufferedReader[] is = new BufferedReader[2];

	    //クライアントへデータを送るためのもの主としてString
	    PrintStream[] os = new PrintStream[2];

	    */

	    //クライアントからのデータを読み込むためのものClass
	    ObjectInputStream[] ois = new ObjectInputStream[2];

	    //クライアントへのデータを送るためのものClass
	    ObjectOutputStream[] oos = new ObjectOutputStream[2];

	    /*
	    
	    //クライアント0からのデータを読み込むためのもの
	    is[0] = new BufferedReader(new InputStreamReader(soc.get(0).getInputStream()));

	    //クライアント1からのデータを読み込むためのもの
	    is[1] = new BufferedReader(new InputStreamReader(soc.get(1).getInputStream()));

	    //クライアント0からのデータを送るためのもの
	    os[0] = new PrintStream(soc.get(0).getOutputStream());

	    //クライアント1からのデータを送るためのもの
	    os[1] = new PrintStream(soc.get(1).getOutputStream());

	    */


	   
	    //ArrayList<Board> dtCon = new ArrayList<Board>();

	    /*
	    dtCon.add(new DataContainer());
	    dtCon.add(new DataContainer());

	    */
	    
	    oos[0] = new ObjectOutputStream((soc.get(0)).getOutputStream());

            oos[1] = new ObjectOutputStream((soc.get(1)).getOutputStream());

	    ois[0] = new ObjectInputStream((soc.get(0)).getInputStream());

	    ois[1] = new ObjectInputStream((soc.get(1)).getInputStream());

	    

	    /*
	    oos[0].writeObject(dtCon.get(0));
            oos[1].writeObject(dtCon.get(1));

	    dtCon.add(0,(DataContainer)(ois[0].readObject()));
	    dtCon.add(1,(DataContainer)(ois[1].readObject()));


	    
	    System.out.println("message from client:"+(dtCon.get(0)).mesStr);
	    System.out.println("message from client:"+(dtCon.get(1)).mesStr);
	    
	    */
	    
	    //System.out.println(dtCon.mesStr);
	    
	    // メッセージ
	    // スタート合図 先手　0
	    // スタート合図 後手　1
	    // あなたの手番       2
	    // 相手の手番         3
	    // ゲーム終了 (勝ち)  5
	    // ゲーム終了 (負け)  6
	    // 挿し直してください(反則) 9～21
	    // 投了(自分が)       7
	    // 投了(相手が)       8
	    // エラー(強制終了)   -1
	    // ひっくり返せますよ? 4
	    
	    

	    
	    //ゲームレコードの生成
	    GameRecord gamerecord = new GameRecord();

	    //同じ盤面状態の回数
	    int statecount = -1;

	    //どちらの手番か
	    int phase = -1;

	    
	    // ボードの生成
	    Board board = new Board();
	    
	    //早く来た方が先制　遅く来た方が後攻 つまり0が先制　1が後攻

	    //審判生成
	    Judge judge = new Judge();

	    // Boolean gameset = false; 試合終了->true
	    Boolean gameset = false;

	    //ArrayList<Board> dtcon 
	    //ArrayList<Board> dtcon = new ArrayList<Board>();

	    //ArrayList<int> input 相手からの情報
	    ArrayList<Integer> input = new ArrayList<Integer>();

	    //int m[2] m[0]->プレイヤー0に対するメッセージ m[1]->プレイヤー1に対するメッセージ
	    int[] m = new int[2];

	    //プレイヤーm[0]=0 -> スタート合図　先手
	    m[0] = 0;

	    //プレイヤーm[1]=1 -> スタート合図　後手
	    m[1] = 1;


	    //dtcon.add(board);
	    
	    // プレイヤー0にメッセージ0とボードを送る
	    oos[0].writeObject(board.GetPrintBoard(0));
	    oos[0].writeObject(m[0]);
	    oos[0].flush();
	    
	    // プレイヤー1にメッセージ1とボードを送る
	    oos[1].writeObject(board.GetPrintBoard(1));
	    oos[1].writeObject(m[1]);
	    oos[1].flush();
	    
	    while(true){
		    

		//はじめはプレイヤー0から初まる。

		input.clear();


		//プレイヤー1からの入力待ち
		if(m[1] == 2 || m[1] == 4 || (m[1] >= 9 && m[1] <= 20)){
		    input = SrvThread.autoCast(ois[1].readObject());
                    m = judge.renewBoard(1, board, input);
		    
		}
		//プレイヤー0からの入力待ち
                else {
		    input = SrvThread.autoCast(ois[0].readObject());
                    m = judge.renewBoard(0, board, input);
		}



	    // int型のArraList->inputをうけとる 3,4にある歩を4,4など　持ち駒 場所と番号
	    // input[0] -> 0:盤面の駒移動    |  1:持ち駒使用
	    // input[1] -> 移動前の位置(列)  |  持ち駒リストの添字
	    // input[2] -> 移動前の位置(行)  |  設置位置(行)
	    // input[3] -> 移動後の位置(列)  |　設置位置(列)
	    // input[4] -> 移動後の位置(行)  |

		System.out.println("1 m[0]: "+m[0]+" m[1]: "+m[1]);

		//Gamerecord格納
		/*
		if(m[0] == 2 || m[0] == 4 || m[1] == 2 || m[1] == 4){

		    if(m[0] == 2){
			gamerecord.pushRecord(1,input);
			gamerecord.renewStateCountList(1, board);
			gamerecord.pushStateRecord(1, board);
			statecount = getStateCount(1,board);
		    }
		    else if(m[0] == 4){
			gamerecord.pushRecord(0,input);
			gamerecord.renewStateCountList(0, board);
			gamerecord.pushStateRecord(0, board);
			statecount = getStateCount(0,board);
		    }
		    else if(m[1] == 2){
			gamerecord.pushRecord(0,input);
			gamerecord.renewStateCountList(0, board);
			gamerecord.pushStateRecord(0, board);
			statecount = getStateCount(0,board);
		    }
		    else{
			gamerecord.pushRecord(1,input);
			gamerecord.renewStateCountList(1, board);
			gamerecord.pushStateRecord(1, board);
			statecount = getStateCount(1,board);
		    }
		}

		*/
		//int m0 -> message番号

		// m = judge.renewBoard(board, input);
		
		// プレイヤー0にメッセージ0とボードを送る

		//
		/*
		if(statecount == 4){

		    int pl = -1;
		    Board bo = new Board();

		    int pla = -1;
		    Board boa = new Board();
		    
		    State state = gamerecord.popStateRecord();
		    
		    pl = state.restorePlayer();
		    bo = state.restoreBoard();

		    //ボードがチェックメイトではない
		    if(!bo.ischeckmate(pl)){
			//ただの千日手
			mes[pl] = 20;
			mes[Math.abs(1-pl)] = 21;
		    }
		    else{
			//チェックメイトだったら
			//pushしていく連続王手を行なっているか調べる
			int counter = statecount;
			State statecompare = gamerecord.popStateRecord();
			pla = statecompare.restorePlayer();
			boa = statecompare.restoreBoard();
			
			while(counter > 0){
			    
			    

			}
			
		    }
		    
		    
		    
		}
		*/

		
		oos[0].writeObject(board.GetPrintBoard(0));
                oos[0].flush();
                oos[1].writeObject(board.GetPrintBoard(1));
                oos[1].flush();

                oos[0].writeObject(m[0]);
                oos[0].flush();
                oos[1].writeObject(m[1]);
                oos[1].flush();
		
		
		if((m[0] >= 5 && m[0] <= 8) || (m[1] >= 5 && m[1] <= 8) || m[0] == -1 || m[1] == -1)
		    break;


		board.printBoard(0);
		
		//input.clear();

		//プレイヤー0からの入力待ち
		/*
		if(m[0] == 2 || m[0] == 4 || (m[0] >= 9 && m[0] <= 19)){
		    input = SrvThread.autoCast(ois[0].readObject());
                    m = judge.renewBoard(0, board, input);
		    
		}
		*/
		//プレイヤー1からの入力待ち
		/*
		else{
		    input = SrvThread.autoCast(ois[1].readObject());
                    m = judge.renewBoard(1, board, input);
		}
		
		System.out.println("2 m[0]: "+m[0]+" m[1]: "+m[1]);
		
		*/
		


		// プレイヤー1にメッセージ1とボードを送る


		/*
		
		oos[0].writeObject(board.GetPrintBoard(0));
                oos[0].flush();
                oos[1].writeObject(board.GetPrintBoard(1));
                oos[1].flush();

		oos[1].writeObject(m[1]);
		oos[1].flush();
                oos[0].writeObject(m[0]);
		oos[0].flush();
		
		*/
				
		// プレイヤー0にメッセージ0とボードを送る
		/*
                oos[0].writeObject(dtcon.get(0));
		oos[0].flush();
                oos[0].writeObject(m[0]);
		oos[0].flush();
		*/

		/*
		if((m[0] >= 5 && m[0] <= 8) || (m[1] >= 5 && m[1] <= 8) || m[0] == -1 || m[1] == -1)
		    break;
		*/

	    }
	    


	    System.out.println("Game End");
	    

	    ois[0].close();
	    ois[1].close();
	    oos[0].close();
	    oos[1].close();
	}
	catch(Exception ioex){
	    ioex.printStackTrace();
	}
	finally{
	    try{
		if(soc != null){

		    soc.get(0).close();
                    soc.get(1).close();
		}
	    }
	    catch(IOException ioex){
		ioex.printStackTrace();
	    }
	}
    }
}
