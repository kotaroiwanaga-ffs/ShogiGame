import java.io.*;

public abstract class ChessPiece implements Serializable{

   // フィールド

    // 表->true 裏->false
    Boolean surface = true;


   // メソッド

    // 駒が表を向いている場合trueを返す(surfaceのゲッター)
    public Boolean getSurface(){
	return surface;
    }

    // surfaceのセッター
    public void setSurface(Boolean surface){
	this.surface = surface;
    }

    // 表にする
    public void reset(){
	setSurface(true);
    }

    // 駒をひっくり返す
    public void changePiece(){
	if(isAbletoChange()){
	    setSurface(false);
	}
    }



   // 抽象メソッド

    // ひっくり返せるかどうか
    public abstract Boolean isAbletoChange();

    // 駒の名前をStringで返す
    public abstract String printChessPiece();

    // 駒が指定された位置に移動できるかどうか
    public abstract Boolean isAbletoMove(int r1, int c1, int r2, int c2);
}

