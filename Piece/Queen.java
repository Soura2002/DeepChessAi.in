package desktop.chess.piece;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;

public class Queen extends Piece {
    private final String type = "Queen";

    public Queen(int color, int col, int row,int point) {
        super(color, col, row, point);

        if (color == 0) {
            getImage("QueenWhite");
        } else {
            getImage("QueenBlack");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        Board board = Board.getBoard();

        if (board.isWithinBoard(targetCol, targetRow)) {
            if (targetCol == getPreCol() || targetRow == getPreRow() ||
                Math.abs(targetCol - getPreCol()) == Math.abs(targetRow - getPreRow())) {

                if ((board.getTilePiece(targetCol, targetRow) == null) ||
                    (board.getTilePiece(targetCol, targetRow).getColor() != this.getColor())) {

                    // Vertical move
                    if (targetCol == getPreCol()) {
                        for (int i = Math.min(getPreRow(), targetRow) + 1;
                             i < Math.max(getPreRow(), targetRow); i++) {
                            if (board.getTilePiece(targetCol, i) != null)
                                return false;
                        }
                        return true;
                    }

                    // Horizontal move
                    if (targetRow == getPreRow()) {
                        for (int i = Math.min(getPreCol(), targetCol) + 1;
                             i < Math.max(getPreCol(), targetCol); i++) {
                            if (board.getTilePiece(i, targetRow) != null)
                                return false;
                        }
                        return true;
                    }

                    // Diagonal move
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
    @Override
    public String getType() {
        return type;
    }
	public void generateAllPossibleMoves(ArrayList<Move> moves,ArrayList<Tile> tiles){
		Board board=Board.getBoard();
		int sourceRow = getRow();
        	int sourceCol = getCol();
		
		//diagonal
		int[] stepD={1,-1,-1,-1,-1,1,1,1};
		for(int i=0;i<=stepD.length-2;i+=2){
			int colStep=stepD[i];
			int rowStep=stepD[i+1];
			
			while((sourceRow+rowStep>=0 && sourceCol+colStep<=7) && (sourceRow+rowStep<=7 && sourceCol+colStep>=0) ){
				
				Board.Move move=board.new Move(tiles.get(sourceCol%8+sourceRow*8),tiles.get((sourceCol+colStep)%8+(sourceRow+rowStep)*8),this,this.getColor());
				if(move.isValidMove(sourceCol+colStep,sourceRow+rowStep)){
					moves.add(move);
				
					colStep+=stepD[i];
					rowStep+=stepD[i+1];
				}
				else{
					break;
				}
			}
		}
		//straight
		int[] stepS={1,0,-1,0,0,1,0,-1};
		for(int i=0;i<=stepS.length-2;i+=2){
			int colStep=stepS[i];
			int rowStep=stepS[i+1];

			while((sourceRow+rowStep>=0 && sourceCol+colStep<=7) && (sourceRow+rowStep<=7 && sourceCol+colStep>=0) ){
				
				Board.Move move=board.new Move(tiles.get(sourceCol%8+sourceRow*8),tiles.get((sourceCol+colStep)%8+(sourceRow+rowStep)*8),this,this.getColor());
				if(move.isValidMove(sourceCol+colStep,sourceRow+rowStep)){
					moves.add(move);
				
					colStep+=stepS[i];
					rowStep+=stepS[i+1];
				}
				else{
					break;
				}
			}

		}

		
                
	}

	public boolean hasValidMoves(ArrayList<Tile> tiles){
		//diagonal
		Board board=Board.getBoard();
		int row = getRow();
        	int col = getCol();
		int[] stepD={1,-1,-1,-1,-1,1,1,1};
		for(int i=0;i<=stepD.length-2;i+=2){
			if((col+stepD[i])%8+(row+stepD[i+1])*8>=0 && (col+stepD[i])%8+(row+stepD[i+1])*8<64){

				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+stepD[i])%8+(row+stepD[i+1])*8),this,this.getColor());
				if(move.isValidMove(col+stepD[i],row+stepD[i+1])){
					
					return true;
				}

			}
		}
		//straight
		int[] stepS={1,0,-1,0,0,1,0,-1};
		for(int i=0;i<=stepS.length-2;i+=2){
			if((col+stepS[i])%8+(row+stepS[i+1])*8>=0 && (col+stepS[i])%8+(row+stepS[i+1])*8<64){

				Board.Move move=board.new Move(tiles.get(col%8+row*8),tiles.get((col+stepS[i])%8+(row+stepS[i+1])*8),this,this.getColor());
				if(move.isValidMove(col+stepS[i],row+stepS[i+1])){
					
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
