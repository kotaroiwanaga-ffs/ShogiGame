//Bishop -> 角行

public class Bishop extends ChessPiece{

   // メソッド

    // 初期化(表にする) 
    Bishop(){
	surface = true;
    }

    
    // ひっくり返せるかどうか
    public Boolean isAbletoChange(){

        return surface;

    }

    // 駒の名前をStringで返す
    public String printChessPiece(){

        if(surface){
            return "角";
	}
        else{
            return "馬";
        }
    }

    // 駒が指定された位置に移動できるかどうか
    public Boolean isAbletoMove(int r1, int c1, int r2, int c2){
	
        int r = r2 - r1;
        int c = c2 - c1;

        if(surface){
	    
            if( Math.abs(r) == Math.abs(c) && r != 0 && Math.abs(r) < 9 && Math.abs(c) < 9){
                return true;
            }
            else{
                return false;
            }
        }
        else{

            if((Math.abs(r) == Math.abs(c) && r != 0
		 && Math.abs(r) < 9 && Math.abs(c) < 9) || ((Math.abs(r) == 0 && Math.abs(c) == 1) ||
							  (Math.abs(r) == 1 && (Math.abs(c) == 1 ||Math.abs(c) == 0)))){
                return true;
            }
            else{
                return false;
            }


        }

    }


    
}
