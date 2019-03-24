import java.util.*;
import java.io.*;


public class Test6{
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
		    System.out.println("入力が不正です指し直してください");
		    int temp2[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp2.length; j++){
			input.add(temp2[j]);
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
		    System.out.println("成りますか？");
		    int temp3[] = player[i].createInputArray(i, mes[i], board);

		    for(int j = 0; j < temp3.length; j++){
			input.add(temp3[j]);
		    }
		    break;

		case -1:
		    
		    gameset[i] = true;
		    break;

		default:

		    System.exit(1);

		} // switch(mes[i])

	    } // for(int i = 0; i < 2; i++)

	    
	    //String a = new Scanner(System.in).nextLine();


	    // inputの中身表示
	    /*
	    System.out.print("input = ");
	    System.out.print(input.get(0));
	    System.out.print(" ");
	    for(int j = 1; j < input.size(); j++){
		System.out.print(input.get(j) + 1);
		System.out.print(" ");
	    }
	    */
	    System.out.println("\n");

	    /*
	    int lastMes[] = new int[2];
	    lastMes[0] = mes[0];
	    lastMes[1] = mes[1];
	    */

	    for(i = 0; i < 2; i++){


		if(mes[i] == 0 || mes[i] == 2 || mes[i] == 4 || mes[i] == 9){
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
