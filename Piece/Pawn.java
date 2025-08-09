package desktop.chess.piece;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;
public class Pawn extends Piece{
	private final String type="Pawn";
	private int firstRow;
	private boolean isFirstMove=true;
	public Pawn(int color,int col,int row,int firstRow,int point){
		super(color,col,row,point);
		this.firstRow=firstRow;
		

		if(color==0){
			getImage("PawnWhite");
		}
		else{
			getImage("PawnBlack");

		}
		
	}
	public boolean canMove(int targetCol,int targetRow){
		Board board=Board.getBoard();
		if(board.isWithinBoard(targetCol,targetRow)){
			
			if((firstRow==1) &&(targetCol==getPreCol()+1 || targetCol==getPreCol()-1) && (targetRow==getPreRow()+1) && (board.getTilePiece(targetCol,targetRow)!=null)){
				if(board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor()){
					return true;
				}
			}
			if((firstRow==6) &&(targetCol==getPreCol()-1 || targetCol==getPreCol()+1) && (targetRow==getPreRow()-1) && (board.getTilePiece(targetCol,targetRow)!=null)){
				if(board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor()){
					return true;
				}

			}
			if(board.getTilePiece(targetCol,targetRow)==null){
				if(firstRow==1){
					if(isFirstMove){
						if(targetCol==getPreCol() && (targetRow==getPreRow()+1 || targetRow==getPreRow()+2)){
							if(targetRow==getPreRow()+2){
								if(board.getTilePiece(targetCol,targetRow-1)!=null){
									return false;
								}
							}
							
							return true;
						}
					}
					else{
						if(targetCol==getPreCol() && targetRow==getPreRow()+1){
							
							return true;
						}
					}
				}
				else{
					if(isFirstMove){
						if(targetCol==getPreCol() && (targetRow==getPreRow()-1 || targetRow==getPreRow()-2)){
							if(targetRow==getPreRow()-2){
								if(board.getTilePiece(targetCol,targetRow+1)!=null){
									return false;
								}
							}

							
							return true;
						}
					}
					else{
						if(targetCol==getPreCol() && targetRow==getPreRow()-1){
							
							return true;
						}
					}

				}
			}
		}
		return false;
	}
	public boolean canCapture(int targetCol,int targetRow){
		Board board=Board.getBoard();
		if((firstRow==1) &&(targetCol==getPreCol()+1 || targetCol==getPreCol()-1) && (targetRow==getPreRow()+1) && (board.getTilePiece(targetCol,targetRow)!=null)){
			if(board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor()){
				return true;
			}
			
		}
		if((firstRow==6) &&(targetCol==getPreCol()-1 || targetCol==getPreCol()+1) && (targetRow==getPreRow()-1) && (board.getTilePiece(targetCol,targetRow)!=null)){
			if(board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor()){
				return true;
			}

		}
		return false;
	}
	public boolean isPromoted(){
		if(firstRow==1){
			if(getRow()==7){
				return true;
			}
		}
		else{
			if(getRow()==0){
				return true;
			}
		}
		return false;
	}
	public String getType(){
		return type; 
	}
	public void setHasMoved(){
		isFirstMove=false;
	}
	public void generateAllPossibleMoves(ArrayList<Move> moves,ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int[] arr={0,2,0,-2,0,1,0,-1,1,1,1,-1,-1,1,-1,-1};
		int col=getCol();
		int row=getRow();
		for(int i=0;i<=arr.length-2;i+=2){
			
			if((col+arr[i])%8+(row+arr[i+1])*8>=0 && (col+arr[i])%8+(row+arr[i+1])*8<64){
				
				
				
				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor());
				if(move.isValidMove(col+arr[i],row+arr[i+1])){
					if(row+arr[i+1]==0 || row+arr[i+1]==7){
						moves.add(board.new PromotionMove(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor()));
						moves.add(board.new PromotionMove(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor()));
						moves.add(board.new PromotionMove(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor()));
						moves.add(board.new PromotionMove(tiles.get(col%8+row*8),tiles.get((col+arr[i])%8+(row+arr[i+1])*8),this,this.getColor()));
					}
					else{
						moves.add(move);
					}
				}
				
			}
					  			
		}

	}
	public boolean hasValidMoves(ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int[] arr={0,1,0,-1,1,1,1,-1,-1,1,-1,-1};
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