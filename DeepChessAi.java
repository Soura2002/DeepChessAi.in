package desktop.chess;
import desktop.chess.piece.Piece;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import java.util.Random;
import java.util.Collections;
class MoveWrapper{
	Move move;
}
class DeepChessAi{
	int color;
	int opponentColor;
	Board board;
	int numMoves=0;
	int depth=6;
	DeepChessAi(Board board,int color){
		this.board=board;
		this.color=color;
		if(color==0){
			opponentColor=1;
		}
		else{
			opponentColor=0;
		}
	}
	public void playMove(){
		MoveWrapper bestMove=new MoveWrapper();
		//board.clonePieces();
		int temp=miniMax(bestMove,board,depth,Integer.MIN_VALUE,Integer.MAX_VALUE,true);
		bestMove.move.makeFinalMove();		
	}
	public int miniMax(MoveWrapper bestMove,Board board,int depth,int alpha,int beta,boolean isMaximizingPlayer){
		if(board.checkMate || board.staleMate || depth==0){
			return evaluateBoard(board);
		}
		if(isMaximizingPlayer){
			ArrayList<Move> moves=board.generateAllPossibleBoardMoves(color);
			Collections.sort(moves);
			int maxEval=Integer.MIN_VALUE;
			for(Move move:moves){
				Piece capturedPiece=move.makeMove();
				int eval=miniMax(bestMove,board,depth-1,alpha,beta,false);
				if(eval>=maxEval){
					//System.out.println("hfhv");
					maxEval=eval;
					if(depth==this.depth){
						bestMove.move=move;
					}
				}
				move.undoMove(capturedPiece);
				alpha=Math.max(alpha,eval);
				if(beta<=alpha){
					break;
				}
			}
			return maxEval;
		}
		else{
			ArrayList<Move> moves=board.generateAllPossibleBoardMoves(opponentColor);
			Collections.sort(moves);
			int minEval=Integer.MAX_VALUE;
			for(Move move:moves){
				Piece capturedPiece=move.makeMove();
				int eval=miniMax(bestMove,board,depth-1,alpha,beta,true);
				move.undoMove(capturedPiece);
				minEval=Math.min(minEval,eval);
				beta=Math.min(beta,eval);
				if(beta<=alpha){
					break;
				}

			}
			return minEval;
		}
	}
	public int evaluateBoard(Board board){
		int materialScore=0;
		ArrayList<Piece> pieces=board.getCopyPieces();
		for(Piece p:pieces){
			if(p.getColor()==color){
				materialScore+=p.getPoint();
			}
			else{
				materialScore-=p.getPoint();
			}
		}
		//System.out.println(materialScore);
		return materialScore;
	}

}