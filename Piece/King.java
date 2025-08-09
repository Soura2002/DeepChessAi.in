package desktop.chess.piece;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;
public class King extends Piece{
	private boolean hasMoved=false;
	private final String type="king";
	public King(int color,int col,int row,int point){
		super(color,col,row,point);
		if(color==0){
			getImage("KingWhite");
		}
		else{
			getImage("KingBlack");

		}
		
	}
	public boolean canMove(int targetCol,int targetRow){
		Board board=Board.getBoard();
		if(board.isWithinBoard(targetCol,targetRow)){
			if(((Math.abs(targetCol-getPreCol())+Math.abs(targetRow-getPreRow()))==1) || ((Math.abs(targetCol-getPreCol())*Math.abs(targetRow-getPreRow()))==1)){
				if((board.getTilePiece(targetCol,targetRow)==null) || (board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor())){
									
					return true;
					

				}
				
			}
		}
		return false;
	}
	public String getType(){
		return type; 
	}
	public boolean canCastle(int targetCol,int targetRow){
		Board board=Board.getBoard();
		Rook rookLeft=board.getRookLeft();
		Rook rookRight=board.getRookRight();
		if(targetRow==getPreRow() &&( targetCol==getPreCol()+2 || targetCol==getPreCol()-2)){
			if(targetCol==getPreCol()+2){
				if(!this.hasMoved && !rookRight.hasMoved()){
					for(int i=getPreCol()+1;i<7;i++){
						if(board.getTilePiece(i,targetRow)!=null){
							return false;
						}
					}
					return true;
				}
				
			}
			if(targetCol==getPreCol()-2){
				if(!this.hasMoved && !rookLeft.hasMoved()){
					for(int i=getPreCol()-1;i>0;i--){
						if(board.getTilePiece(i,targetRow)!=null){
							return false;
						}

					}
					return true;
				}
				
			}
			
		}
		return false;
	}
	public boolean isKingSideCastle(int targetCol,int targetRow){
		if(targetCol==getPreCol()+2){
			return true;
		}
		return false;
	}
	public boolean isQueenSideCastle(int targetCol,int targetRow){
		if(targetCol==getPreCol()-2){
			return true;
		}
		return false;
	}

	public boolean hasMoved(){
		return this.hasMoved;
	}
	public void setHasMoved(){
		hasMoved=true;
	}
	public void generateAllPossibleMoves(ArrayList<Move> moves,ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int[] arr={1,0,-1,0,0,1,0,-1,1,1,1,-1,-1,1,-1,-1,2,0,-2,0};
		int col=getCol();
		int row=getRow();
		for(int i=0;i<=arr.length-2;i+=2){
			
			if((col+arr[i])%8+(row+arr[i+1])*8>=0 && (col+arr[i])%8+(row+arr[i+1])*8<64){
				
				
				
				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor());
				if(move.isValidMove(col+arr[i],row+arr[i+1])){
					
					moves.add(move);
				}
				
			}
					  			
		}
		

	}
	public boolean hasValidMoves(ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int[] arr={1,0,-1,0,0,1,0,-1,1,1,1,-1,-1,1,-1,-1,2,0,-2,0};
		int col=getCol();
		int row=getRow();
		for(int i=0;i<=arr.length-2;i+=2){
			
			if((col+arr[i])%8+(row+arr[i+1])*8>=0 && (col+arr[i])%8+(row+arr[i+1])*8<64){
				
				
				
				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor());
				if(move.isValidMove(col+arr[i],row+arr[i+1])){
					
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