//King -> 王将

public class KingOu extends ChessPiece{

   //メソッド

    // 初期化(表にする) 
    KingOu(){
	surface = true;
    }

    // ひっくり返せるかどうか
    public Boolean isAbletoChange(){

        return false;

    }

    // 駒の名前をStringで返す
    public String printChessPiece(){
	
            return "王";

    }

    // 駒が指定された位置に移動できるかどうか
    public Boolean isAbletoMove(int r1, int c1, int r2, int c2){
	
        int r = r2 -r1;
        int c = c2 -c1;

        if(surface){

            if( (Math.abs(r) == 0 && Math.abs(c) == 1) || (Math.abs(r) == 1 && (Math.abs(c) == 1 || Math.abs(c) == 0)) ){
                return true;
            }
            else{
                return false;
            }
        }

	return false;

    }


    
}
