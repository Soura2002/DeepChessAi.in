package desktop.chess.piece;

public class PieceFactory{
	public static Piece produce(String type,int color,int col,int row){
		if(type.equals("King")){
			return new King(color,col,row,1000);
		}
		else if(type.equals("Queen")){
			return new Queen(color,col,row,800);
		}
		else if(type.equals("Pawn")){
			return new Pawn(color,col,row,row,100);

		}
		else if(type.equals("Bishop")){
			return new Bishop(color,col,row,300);
		}
		else if(type.equals("Rook")){
			return new Rook(color,col,row,400);

		}
		else if(type.equals("Knight")){
			return new Knight(color,col,row,300);

		}
		else{
			throw new IllegalArgumentException("Invalid Piece Type");
		}

	}
}