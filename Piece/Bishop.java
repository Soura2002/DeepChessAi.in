package desktop.chess.piece;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;

public class Bishop extends Piece{
	private final String type="Bishop";

	public Bishop(int color,int col,int row,int point){
		super(color,col,row,point);
		
		if(color==0){
			getImage("BishopWhite");
		}
		else{
			getImage("BishopBlack");

		}
		
	}
	public boolean canMove(int targetCol,int targetRow){
		Board board=Board.getBoard();
		if(board.isWithinBoard(targetCol,targetRow)){
			if(Math.abs(targetCol-getPreCol())==Math.abs(targetRow-getPreRow())){
				if((board.getTilePiece(targetCol,targetRow)==null) || (board.getTilePiece(targetCol,targetRow).getColor()!=this.getColor())){	
					int sourceRow = getPreRow();
                    			int sourceCol = getPreCol();

                    			int rowStep = (targetRow > sourceRow) ? 1 : -1;
                    			int colStep = (targetCol > sourceCol) ? 1 : -1;

                    			sourceRow += rowStep;
                    			sourceCol += colStep;

                    			while (sourceRow != targetRow && sourceCol != targetCol) {
                        			if (board.getTilePiece(sourceCol, sourceRow) != null)
                            				return false;
                        			sourceRow += rowStep;
                        			sourceCol += colStep;
                    			}
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
		int sourceRow = getRow();
        	int sourceCol = getCol();
		
		
		int[] step={1,-1,-1,-1,-1,1,1,1};
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
		int row = getRow();
        	int col = getCol();
		int[] step={1,-1,-1,-1,-1,1,1,1};
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