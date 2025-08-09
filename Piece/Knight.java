package desktop.chess.piece;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;

public class Knight extends Piece{
	private final String type="knight";

	public Knight(int color,int col,int row,int point){
		super(color,col,row,point);
		

		if(color==0){
			getImage("KnightWhite");
		}
		else{
			getImage("KnightBlack");

		}
		
	}
	public boolean canMove(int targetCol,int targetRow){
		Board board=Board.getBoard();
		if(board.isWithinBoard(targetCol,targetRow)){
			if((board.getTilePiece(targetCol,targetRow)==null) || (board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor())){	
				if(Math.abs(targetCol-getPreCol())==2 && Math.abs(targetRow-getPreRow())==1){
					
					return true;
				

				}
				else if(Math.abs(targetCol-getPreCol())==1 && Math.abs(targetRow-getPreRow())==2){
					
					return true;
				

				}
			}
		}
		return false;
	}
	public String getType(){
		return type; 
	}
	public void generateAllPossibleMoves(ArrayList<Move> moves,ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int col=getCol();
		int row=getRow();
		int[] step={2,1,-2,1,2,-1,-2,-1,1,2,-1,2,1,-2,-1,-2};
		for(int i=0;i<=step.length-2;i+=2){
			if((col+step[i])%8+(row+step[i+1])*8>=0 && (col+step[i])%8+(row+step[i+1])*8<64){
				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+step[i])%8+(row+step[i+1])*8),this,this.getColor());
					if(move.isValidMove(col+step[i],row+step[i+1])){
					
						moves.add(move);
					}
			}
		}
		

		
	}
	public boolean hasValidMoves(ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int col=getCol();
		int row=getRow();
		int[] step={2,1,-2,1,2,-1,-2,-1,1,2,-1,2,1,-2,-1,-2};
		for(int i=0;i<=step.length-2;i+=2){
			if((col+step[i])%8+(row+step[i+1])*8>=0 && (col+step[i])%8+(row+step[i+1])*8<64){
				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+step[i])%8+(row+step[i+1])*8),this,this.getColor());
				if(move.isValidMove(col+step[i],row+step[i+1])){
					
					return true;
				}
			}
		}
		return false;

	}
	public int getPoint(){
		return this.point;
	}


}