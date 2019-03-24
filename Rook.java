//Rook -> 飛車

public class Rook extends ChessPiece{

   //メソッド

    // 初期化
    Rook(){
	surface = true;
    }

    // ひっくり返せるかどうか
    public Boolean isAbletoChange(){

        return surface;

    }

    // 駒の名前をStringで返す
    public String printChessPiece(){

        if(surface){
            return "飛";
	}
        else{
            return "龍";
        }
    }

    // 駒が指定された位置に移動できるかどうか
    public Boolean isAbletoMove(int r1, int c1, int r2, int c2){
	
        int r = r2 - r1;
        int c = c2 - c1;

        if(surface){
	    
            if( ((c == 0 && Math.abs(r) >= 1 && Math.abs(r) <= 8) || (r == 0 && Math.abs(c) >= 1 && Math.abs(c) <= 8)) ){
                return true;
            }
            else{
                return false;
            }
        }
        else{

            if( ((c == 0 && Math.abs(r) >= 1 && Math.abs(r) <= 8) || (r == 0 && Math.abs(c) >= 1 && Math.abs(c) <= 8)) ||
		((Math.abs(r) == 0 && Math.abs(c) == 1) || (Math.abs(r) == 1 && (Math.abs(c) == 1 || Math.abs(c) == 0)))){
                return true;
            }
            else{
                return false;
            }


        }

    }


    
}
