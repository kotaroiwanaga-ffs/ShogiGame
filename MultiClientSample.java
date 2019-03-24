//クライアントサンプルプログラム
//サーバーに接続し、メッセージを送信する。
//サーバーアドレスはコマンドラインの第一引数で指定。
//ポートは30000に固定。先にMultiSampleを起動しておくこと。
//第二引数で、メッセージを指定する。一行送ってサーバーからの
//メッセージ受信、表示後にプログラム終了する。
// コマンドライン例: java MultiClientSample localhost abcdefg
//java MultiClientSample アドレス メッセージ

import java.net.*;
import java.io.*;
import java.util.*;

class MultiClientSample 
{

    
    public static void main(String[] args){



	
	try{

	    //アドレス情報を保持するsocketAddressを作成。
	    //ポート番号は30000
	    InetSocketAddress socketAddress =
		new InetSocketAddress(args[0], 30000);

	    //socketAddressの値に基づいて通信に使用するソケットを作成する。

	    //
	    Socket socket = new Socket();

	    //タイムアウトは10秒(10000msec)
	    socket.connect(socketAddress);

	    //接続先の情報を入れるInetAddress型のinadrを用意する
	    InetAddress inadr;

	    //inadrにソケットの接続先アドレスを入れ、nullで場合には
	    //接続失敗と判断する。
	    //nullでなければ、接続確率している。
	    if((inadr = socket.getInetAddress()) != null){
		System.out.println("Connect to " + inadr);
	    }
	    else{
		System.out.println("Connection failed.");
		return;
	    }

	    //メッセージの送信処理
	    //コマンドライン引数の2番目をメッセージとする。
	    //String message = args[1];

	    /*
	    // PrintStream型のwriterに、ソケットの出力ストリームを渡す。
	    PrintStream writer = new PrintStream(socket.getOutputStream());

	    //ソケットの入力ストリームをBufferedReaderにわたす。
	    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    */
	    Integer message = -1;

            Integer player = -1;

            String printboard = "";

	    //入力と出力ストリームの生成
	    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

	    //サーバーから送られてくるボード情報の格納

	    //サーバーから送られてくるメッセージ情報の格納
	    message = -1;

	    //自分がプレイヤーなんであるか0か1
	    player = -1;
	    

	    //サーバーからのボード情報を読み取る.
	    //board = (Board)(ois.readObject());
	    printboard = (String)(ois.readObject());
	    
	     
	    //サーバーからのメッセージを読み取る.
	    message = (int)(ois.readObject());

	    //playerの情報を格納
	    player = message;

	    System.out.println(player);
	    
	    //プレイヤーのキーボード入力を受け付けるバッファ
	    InputStreamReader isr = new InputStreamReader(System.in);
	    BufferedReader br = new BufferedReader(isr);
	    
	    
	    //ゲームが続くための条件
	    //messageはサーバーからの情報
	    while( (message >= 0 && message <= 4) || (message >= 9 && message <= 21)){

		//キーボードからの入力を格納するもの
		ArrayList<String> MoveChess = new ArrayList<String>();
		ArrayList<Integer> MoveChessInt = new ArrayList<Integer>();

		//クライアントの入力に間違いがないかまでwhileをまわすための条件となるもの
		Boolean ClientInput = true;
		String buf = null;
		//サーバーに送信するもの
		ArrayList<Integer> SubmitInt = new ArrayList<Integer>();
		//一文字入力を受け付けるもの
		int x = -1;

		
		L159:
		switch(message){

		case 0:
		    System.out.println("将棋の対局を開始します。");
		    System.out.println("あなたが先手です。");
		    message = 2;
		    break;
		case 1:
		    System.out.println("将棋の対局を開始します。");
		    System.out.println("あなたが後手です。");
		    message = 3;
		    break;
		case 2:
		    System.out.println(printboard);
		    x = -1;
		    System.out.println("番号を入力してください");
		    System.out.println("0 : 盤面の駒の移動");
		    System.out.println("1 : 持ち駒使用");
		    System.out.println("2 : 投了");
		    while( x != 0 && x != 1 && x != 2){
			System.out.print("input integer: ");
			buf = null;
			try{
			    buf = br.readLine();
			    buf = buf.trim();
			    x = Integer.parseInt(buf);
			}
			catch(Exception e){
			    x = -1;
			}
			finally{
			    if(x != 0 && x != 1 && x!=2){
				System.out.println("指定した番号を入力してください");
			    }
			}
		    }



		    //番号の入力により盤面の駒の移動など選択できる。
		switch(x){

		    case 0:
			System.out.println("盤面の駒の移動を行いします。");
			System.out.println("input : BeforeColumn BeforeRow AfterColumn AfterRow");
			System.out.println("戻りたいときは");
                        System.out.println("Input : back");

			while(ClientInput){
			    System.out.print("input : ");
			    MoveChessInt.clear();
			    MoveChess.clear();
			    SubmitInt.clear();
			    buf = null;
			    try{
				buf = br.readLine();
			    }
			    catch(Exception e){
				System.out.println("error");
			    }
			    finally{
				String[] s = buf.split(" ");

				for(int i = 0; i < s.length; i++){
				    if(!s[i].isEmpty())
					MoveChess.add(s[i]);
				}

				if(MoveChess.size() == 4){

				    Iterator iter = MoveChess.iterator();

				    try{
					while(iter.hasNext()){
					    MoveChessInt.add(Integer.parseInt((String)iter.next())-1);
					}
					ClientInput = false;
				    }
				    catch(Exception e){
					System.out.println("数を入力してください");
					ClientInput = true;
				    }
				    finally{
				    }


				}
				else if(MoveChess.size() == 1){
                                    if(MoveChess.get(0).equals("back")){
                                        System.out.println("戻ります");
                                        message = 2;
					//backと入力されたら最初の選択肢に戻る
					break L159;
                                    }
                                    else{
                                        System.out.println("入力した数がまちがっています");
                                        ClientInput = true;
                                    }
                                }
				else{
				    System.out.println("入力した数がまちがっています");
				    ClientInput = true;
				}

			    }

			}


			SubmitInt.add(0);
                        SubmitInt.addAll(MoveChessInt);


			for(int i = 0; i < SubmitInt.size(); i++){
			    System.out.println(SubmitInt.get(i));
			}

			oos.writeObject(SubmitInt);
			oos.flush();
			break;
		    case 1:

			System.out.println("持ち駒を使用します。");
			System.out.println("Input : Index Columns row");
			System.out.println("戻りたいときは");
			System.out.println("Input : back");


			while(ClientInput){
			    System.out.print("input : ");
			    MoveChess.clear();
			    MoveChessInt.clear();
			    buf = null;
			    try{
				buf = br.readLine();
			    }
			    catch(Exception e){
				System.out.println("error");
			    }
			    finally{
				String[] s = buf.split(" ");
                            
				for(int i = 0; i < s.length; i++){
				    if(!s[i].isEmpty())
					MoveChess.add(s[i]);
				}
                            
				if(MoveChess.size() == 3){
                                
				    try{
					for(int i = 0; i < MoveChess.size(); i++){
					    if(i != 0)
						MoveChessInt.add(Integer.parseInt(MoveChess.get(i))-1);
					    else
						MoveChessInt.add(Integer.parseInt(MoveChess.get(i)));
					}

					ClientInput = false;
				    }
				    catch(Exception e){
					System.out.println("数を入力してください");
					ClientInput = true;
				    }
				    finally{
				    }
                                
                                
				}
				else if(MoveChess.size() == 1){
				    if(MoveChess.get(0).equals("back")){
					System.out.println("戻ります");
					message = 2;
					//backと入力されたらgoto文最初の選択肢に戻る
					break L159;
				    }
				    else{
					System.out.println("入力した数がまちがっています");
					ClientInput = true;
				    }
				}
				else{
				    System.out.println("入力した数がまちがっています");
				    ClientInput = true;
				}
                            
			    }
                        
			}


			SubmitInt.clear();
			SubmitInt.add(1);
			SubmitInt.addAll(MoveChessInt);
			
			oos.writeObject(SubmitInt);
			oos.flush();
			break;
		    case 2:

                        SubmitInt.clear();

			System.out.println("投了します。");
                    
			SubmitInt.add(2);
                    
			oos.writeObject(SubmitInt);
			oos.flush();
			break;
			
		    }


		  
		    printboard = (String)(ois.readObject());
		    message = (Integer)(ois.readObject());
		  
		    break;
		case 3:
		    System.out.println(printboard);
		    System.out.println("相手の番です");
		    System.out.println("しばらくお待ちください");

		    printboard = (String)(ois.readObject());
		    message = (Integer)(ois.readObject());
		    break;
		case 4:

		    //yesなら3　noなら4をおくる。
		    
		    System.out.println("成りますか。");
                    System.out.println("番号を入力してください");
                    System.out.println("3 : yes");
                    System.out.println("4 : no");
		    
                    while( x != 3 && x != 4){
                        System.out.print("input integer: ");
			buf = null;
			SubmitInt.clear();
			x = -1;
                        try{
                            buf = br.readLine();
			    buf = buf.trim();
                            x = Integer.parseInt(buf);
                        }
                        catch(Exception e){
                            x = -1;
                        }
                        finally{
                            if(x != 3 && x != 4){
                                System.out.println("指定した番号を入力してください");
                            }
                        }
                    }

		    SubmitInt.add(x);
		    oos.writeObject(SubmitInt);
		    oos.flush();

		    printboard = (String)(ois.readObject());
                    message = (Integer)(ois.readObject());
		    break;
		case 9:
		    System.out.println("位置指定が不正(盤面外の位置を指定している)");
		    System.out.println("指し直してください");
		    message = 2;
		    break;
		case 10:
		    System.out.println("(駒移動の場合に)指定位置に駒がありません");
		    System.out.println("指し直してください");
		    message = 2;
		    break;
		case 11:
		    System.out.println("(駒移動の場合に)移動先に自分の駒があります");
		    System.out.println("指し直してください");
		    message = 2;
		    break;
		case 12:
		    System.out.println("不正な移動命令(障害物がある/元々そんな移動の仕方はない)");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 13:
		    System.out.println("(持ち駒使用場合に)存在しない持ち駒を指定(添え字が変)");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 14:
		    System.out.println("(駒移動の場合に)移動先に自分の駒があります");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 15:
		    System.out.println("王手(移動することで王手になってしまう/王手を回避できない)");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 16:
		    System.out.println("そこに置くと二歩です");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 17:
		    System.out.println("そこに駒を置くと移動不可なので反則です");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 18:
		    System.out.println("打ち歩詰めです");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 19:
		    System.out.println("千日手(王手)です");
                    System.out.println("指し直してください");
		    message = 2;
		    break;
		case 20:
		    System.out.println("千日手(最初から指し直し)です(先手)");
                    System.out.println("指し直してください");
                    message = 2;
                    break;
		case 21:
		    System.out.println("千日手(最初から指し直し)です(後手)");
                    message = 3;
                    break;
		    
		}

	    }


	    System.out.println(printboard);
	    
	    //ここで勝ち負けがわかる　
	    switch(message){

	    case 5:
		System.out.println("あなたの勝ちです。");
		break;
	    case 6:
		System.out.println("あなたの負けです。");
		break;
	    case 7:
		System.out.println("あなたは投了しました負けです。");
	    break;
	    case 8:
		System.out.println("あいてが投了しました勝ちです。");
	    break;
	    case -1:
		System.out.println("不則な事態がおきました。");
	    defalut:
		break;
	    }


	    

	    
	    
	    	    
	    //System.out.println("Send message " + message);

	    /*
	    //ソケットから出力する。
	    writer.println(message);

	    //もしPrintStreamがAutoFlushでない場合Auto Flushが必要
	    writer.flush();

	    //サーバーからのメッセージ読み取り
	    String getline = rd.readLine();
	    System.out.println("Message from Server:" + getline);

	    */

	    //終了処理
	    ois.close();
	    oos.close();
	    socket.close();

	    return;

	}
	catch( Exception e){
	    System.out.println("接続関係に問題が起きました。");
	    e.printStackTrace();
	}
    }
}



	    
	  
