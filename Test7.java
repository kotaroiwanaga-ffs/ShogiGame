import java.util.*;
import java.io.*;


public class Test7{
    public static void main(String[] args){

	int i = -1;
	Board board = new Board();
	Judge judge = new Judge();
	GameRecord gr = new GameRecord();

	Player player[] = {new SamplePlayer(), new SamplePlayer()};

	ArrayList<Integer> input = new ArrayList<Integer>();
	Boolean[] gameset = {false, false};
	int[] mes = {0, 1};

	// ゲーム終了までループ
	while(!gameset[0] || !gameset[1]){

	    input.clear();

	    // プレイヤー0,1の各メッセージに対する処理
	    for(i = 0; i < 2; i++){

		board.printBoard(i);

		System.out.print("プレイヤー");
		System.out.print(i);
		System.out.println("の動作");

		int a_i = Math.abs(i - 1);
		String str;
		int temp2[] = new int[5];

		switch(mes[i]){

		case 0:
		    str = "プレイヤー" + i + "が先手です"; 
		    mes[i] = 2;
		    System.out.println(str);

		case 2:
		    str = "プレイヤー" + i + "の手番です";
		    System.out.println(str);

		    int temp0[] = player[i].createInputArray(i, mes[i], board);

		    for(int j = 0; j < temp0.length; j++){
			input.add(temp0[j]);
		    }

		    System.out.println("action : なにをするか");
		    System.out.println("0 : 駒を動かす, 1 : 持ち駒を使う, 2 : 投了");
		    System.out.print("action = ");
		    System.out.println(input.get(0));

		    while(input.get(0) != 0 && input.get(0) != 1 && input.get(0) != 2){
			
			    System.out.println("入力が不正です");
			    System.out.println("やり直してください");
			    int temp1[] = player[i].createInputArray(i, mes[i], board);
			    
			    for(int j = 0; j < temp1.length; j++){
				input.add(temp1[j]);
			    }

		    }

		
		    break;
		
		case 1:
		    str = "プレイヤー" + i + "が後手です"; 
		    System.out.println(str);

		case 3:
		    str = "プレイヤー" + a_i + "の手番です\nしばらくお待ちください";
		    System.out.println(str);
		    break;

		case 4:
		    System.out.println("成りますか？");
                    int temp3[] = player[i].createInputArray(i, mes[i], board);

                    for(int j = 0; j < temp3.length; j++){
                        input.add(temp3[j]);
                    }

		    break;

		case 5:
		    System.out.println("詰みです");
		    System.out.println("あなたの勝ちです");
		    gameset[i] = true;
		    break;

		case 6:
		    System.out.println("詰みです");
		    System.out.println("あなたの負けです");
		    gameset[i] = true;
		    break;

		case 7:
		    str = "プレイヤー" + i + "が投了しました";
		    System.out.println("あなたの負けです");
		    System.out.println(str);
		    gameset[i] = true;
		    break;

		case 8:
		    str = "プレイヤー" + a_i + "が投了しました";
		    System.out.println("あなたの勝ちです");
		    gameset[i] = true;
		    break;
		case 9:
                    System.out.println("位置指定が不正(盤面外の位置を指定している)");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 10:
                    System.out.println("(駒移動の場合に)指定位置に駒がありません");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 11:
                    System.out.println("(駒移動の場合に)移動先に自分の駒があります");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 12:
                    System.out.println("不正な移動命令(障害物がある/元々そんな移動の仕方はない)");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
		    case 13:
                    System.out.println("(持ち駒使用場合に)存在しない持ち駒を指定(添え字が変)");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 14:
                    System.out.println("(駒移動の場合に)移動先に自分の駒があります");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 15:
                    System.out.println("王手(移動することで王手になってしまう/王手を回避できない)");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 16:
                    System.out.println("そこに置くと二歩です");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
		case 17:
                    System.out.println("そこに駒を置くと移動不可なので反則です");
                    System.out.println("指し直してください");
                    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 18:
                    System.out.println("打ち歩詰めです");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 19:
                    System.out.println("千日手(王手)です");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 20:
                    System.out.println("千日手(最初から指し直し)です(先手)");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
                case 21:
                    System.out.println("千日手(最初から指し直し)です(後手)");
                    System.out.println("指し直してください");
		    temp2 = player[i].createInputArray(i, mes[i], board);
                    for(int j = 0; j < temp2.length; j++){
                        input.add(temp2[j]);
                    }
                    break;
		case -1:
		    gameset[i] = true;
		    break;
		default:
		    System.exit(1);
		    break;

		} // switch(mes[i])

	    } // for(int i = 0; i < 2; i++)

	    
	    //String a = new Scanner(System.in).nextLine();


	    // inputの中身表示

	    System.out.print("input = ");
	    System.out.print(input.get(0));
	    System.out.print(" ");
	    for(int j = 1; j < input.size(); j++){
		System.out.print(input.get(j) + 1);
		System.out.print(" ");
	    }

	    System.out.println("\n");

	    /*
	    int lastMes[] = new int[2];
	    lastMes[0] = mes[0];
	    lastMes[1] = mes[1];
	    */

	    for(i = 0; i < 2; i++){


		if(mes[i] == 0 || mes[i] == 2 || mes[i] == 4 || mes[i] >= 9 && mes[i] <= 21 ){
		    int temp[] = judge.renewBoard(i, board, input);
		    //System.out.println("temp: "+temp[0]+" "+temp[1]);
		    mes[0] = temp[0];
		    mes[1] = temp[1];

		    if(mes[1] == 2){
			gr.pushRecord(0, input);
		    }

		    if(mes[0] == 2){
			gr.pushRecord_switch(1, input);
		    }

		    break;
		}

	    } // for(i = 0; i < 2; i++)
	    

	} // while(!gameset)

	
	// 棋譜表示
	gr.printGameRecordSimple();


    }

}
