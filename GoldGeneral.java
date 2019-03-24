//GoldGeneral -> 銀将

public class GoldGeneral extends ChessPiece{

    // メソッド

     // 初期化(表にする)
    GoldGeneral(){
	surface = true;
    }


     // ひっくり返せるかどうか 
    public Boolean isAbletoChange(){

        return false;

    }

    // 駒の名前をStringで返す
    public String printChessPiece(){

	return "金";
    }

    // 駒が指定された位置に移動できるかどうか
    public Boolean isAbletoMove(int r1, int c1, int r2, int c2){
	
        int r = r2 -r1;
        int c = c2 -c1;

        if(surface){

            if( (r == 0 && (Math.abs(c) == 1))  || ((Math.abs(r) == 1) && ( c == 0 || c == -1)) ){
                return true;
            }
            else{
                return false;
            }
	    
        }

	return false;

    }


    
}
