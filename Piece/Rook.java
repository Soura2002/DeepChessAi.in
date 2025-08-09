package desktop.chess.piece;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;

public class Rook extends Piece{
	private final String type="Rook";
	private boolean hasMoved=false;
	public Rook(int color,int col,int row,int point){
		super(color,col,row,point);
		
		if(color==0){
			getImage("RookWhite");
		}
		else{
			getImage("RookBlack");

		}
		
	}
	public boolean canMove(int targetCol,int targetRow){
		Board board=Board.getBoard();
		if(board.isWithinBoard(targetCol,targetRow)){
			if(targetCol==getPreCol() || targetRow==getPreRow()){
				if((board.getTilePiece(targetCol,targetRow)==null) || (board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor())){	
					if (targetCol == getPreCol()) {
						for (int i = Math.min(getPreRow(), targetRow) + 1;i < Math.max(getPreRow(), targetRow); i++) {
							if (board.getTilePiece(targetCol, i) != null){
								
                                				return false;
							}

						}
						return true;
					}


					if (targetRow == getPreRow()) {
						for (int i = Math.min(getPreCol(), targetCol) + 1;i < Math.max(getPreCol(), targetCol); i++) {
							if (board.getTilePiece(i,targetRow) != null){
								
                                				return false;
							}

						}
						
						return true;
					}

				}



			}
		}
		return false;
	}
	public String getType(){
		return type; 
	}
	public boolean hasMoved(){
		return hasMoved;
	}
	public void setHasMoved(){
		hasMoved=true;
	}
	public void generateAllPossibleMoves(ArrayList<Move> moves,ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int sourceCol=getCol();
		int sourceRow=getRow();
		int[] step={1,0,-1,0,0,1,0,-1};
		for(int i=0;i<=step.length-2;i+=2){
			int colStep=step[i];
			int rowStep=step[i+1];

			while((sourceRow+rowStep>=0 && sourceCol+colStep<=7) && (sourceRow+rowStep<=7 && sourceCol+colStep>=0) ){
				
				Board.Move move=board.new Move(tiles.get(sourceCol%8+sourceRow*8),tiles.get((sourceCol+colStep)%8+(sourceRow+rowStep)*8),this,this.getColor());
				if(move.isValidMove(sourceCol+colStep,sourceRow+rowStep)){
					moves.add(move);
				
					colStep+=step[i];
					rowStep+=step[i+1];
				}
				else{
					break;
				}
			}

		}
	}
	public boolean hasValidMoves(ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int col=getCol();
		int row=getRow();
		int[] step={1,0,-1,0,0,1,0,-1};
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