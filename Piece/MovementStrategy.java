package desktop.chess.piece;

public interface MovementStrategy{
	public boolean canMove(int targetCol,int targetRow);
}