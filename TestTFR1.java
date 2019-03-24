import java.util.*;
import java.io.*;

public class TestTFR1{
    public static void main(String[] args){

	Board board = new Board();
	Judge judge = new Judge();
	Player player[] = {new TreefoldRepetitionPlayer1(), new TreefoldRepetitionPlayer1()};

	ArrayList<Integer> input = new ArrayList<Integer>();
	Boolean[] gameset = {false, false};
	int[] mes = {0, 1};

	// ゲーム終了までループ
	while(!gameset[0] || !gameset[1]){

	    input.clear();

	    // プレイヤー0,1の各メッセージに対する処理
	    for(int i = 0; i < 2; i++){

		board.printBoard(i);

		System.out.print("プレイヤー");
		System.out.print(i);
		System.out.println("の動作");

		int a_i = Math.abs(i - 1);
		String str;

		switch(mes[i]){
		case 20:
		    System.out.println("千日手になったので初手から指し直しです");
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
		
		case 21:
		    System.out.println("千日手になったので初手から指し直しです");

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
		    System.out.println("盤面外を位置指定しています");
		    int temp9[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp9.length; j++){
			input.add(temp9[j]);
		    }
		    break;
		case 10:
		    System.out.println("指定位置に自分の駒がありません");
		    int temp10[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp10.length; j++){
			input.add(temp10[j]);
		    }
		    break;
		case 11:
		    System.out.println("移動先に自分の駒があります");
		    int temp11[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp11.length; j++){
			input.add(temp11[j]);
		    }
		    break;

		case 12:
		    System.out.println("不正な移動命令です");
		    int temp12[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp12.length; j++){
			input.add(temp12[j]);
		    }
		    break;

		case 13:
		    System.out.println("存在しない持ち駒を指定しています");
		    int temp13[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp13.length; j++){
			input.add(temp13[j]);
		    }
		    break;

		case 14:
		    System.out.println("設置位置が不正です");
		    int temp14[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp14.length; j++){
			input.add(temp14[j]);
		    }
		    break;

		case 15:
		    System.out.println("王手です");
		    int temp15[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp15.length; j++){
			input.add(temp15[j]);
		    }
		    break;

		case 16:
		    System.out.println("二歩です");
		    int temp16[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp16.length; j++){
			input.add(temp16[j]);
		    }
		    break;

		case 17:
		    System.out.println("これ以上動けなくなる位置には置けません");
		    int temp17[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp17.length; j++){
			input.add(temp17[j]);
		    }
		    break;

		case 18:
		    System.out.println("打ち歩詰めです");
		    int temp18[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp18.length; j++){
			input.add(temp18[j]);
		    }
		    break;

		case 19:
		    System.out.println("千日手(王手)です");
		    int temp19[] = player[i].createInputArray(i, mes[i], board);
		    for(int j = 0; j < temp19.length; j++){
			input.add(temp19[j]);
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
	    System.out.print("input = ");
	    System.out.print(input.get(0));
	    System.out.print(" ");

	    if(input.get(0) == 0){
		for(int j = 1; j < input.size(); j++){
		    System.out.print(input.get(j) + 1);
		    System.out.print(" ");
		}
		System.out.println();
	    }
	    else if(input.get(0) == 1){
		for(int j = 1; j < input.size(); j++){
		    if(j == 1){
			System.out.println(input.get(j));
		    }
		    else{
			System.out.print(input.get(j) + 1);
			System.out.print(" ");
		    }
                }
                System.out.println();
	    }

	    String a = new Scanner(System.in).nextLine();

	    if(a.equals("end")){
		System.exit(1);
	    }
	    
	    for(int i = 0; i < 2; i++){


		if(mes[i] == 0 || mes[i] == 2 || mes[i] == 4 || mes[i] >= 9){
		    int temp[] = judge.renewBoard(i, board, input);
		    mes[0] = temp[0];
		    mes[1] = temp[1];
		    break;
		}

	    }
	    

	} // while(!gameset)



    }

}
