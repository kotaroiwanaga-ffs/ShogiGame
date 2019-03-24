//SilverGeneral -> 銀将

public class SilverGeneral extends ChessPiece{

   //メソッド

    // 初期化(表にする)
    SilverGeneral(){
	surface = true;
    }

    // ひっくり返せるかどうか
    public Boolean isAbletoChange(){

        return surface;

    }

    // 駒の名前をStringで返す
    public String printChessPiece(){

        if(surface){
            return "銀";
	}
        else{
            return "金";
        }
    }

    // 駒が指定された位置に移動できるかどうか
    public Boolean isAbletoMove(int r1, int c1, int r2, int c2){
	
        int r = r2 -r1;
        int c = c2 -c1;

        if(surface){
	    
            if( ((c == -1 && (r == 0 || Math.abs(r) == 1)) || (c == 1 && Math.abs(r) == 1))){
                return true;
            }
            else{
                return false;
            }
        }
        else{

            if( (r == 0 && (Math.abs(c) == 1))  || ((Math.abs(r) == 1) && ( c == 0 || c == -1))){
                return true;
            }
            else{
                return false;
            }


        }

    }


    
}
