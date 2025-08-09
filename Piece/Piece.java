package desktop.chess.piece;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import desktop.chess.Board;
import java.util.ArrayList;
import desktop.chess.Board.Move;
import desktop.chess.Board.Tile;
public abstract class Piece implements MovementStrategy{
	private BufferedImage image;
	private int x,y;
	private int col,row,preCol,preRow;
	private int color;
	protected int point;
	Piece(int color,int col,int row,int point){
		this.color=color;
		this.col=col;
		this.row=row;
		this.point=point;
		preCol=col;
		preRow=row;
		x=getX(col);
		y=getY(row);
		
		
	}
	public abstract int getPoint();
	public void setHasMoved(){
	}
	public static int getX(int col){
		return col*Board.SQUARE_SIZE;
	}
	public static int getY(int row){
		return row*Board.SQUARE_SIZE;
	}
	public int get_X(){
		return this.x;
	}
	public int get_Y(){
		return this.y;
	}
	public int getPreCol(){
		return preCol;
	}
	public int getPreRow(){
		return preRow;
	}

	
	public int getCol(){
		return (x+Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
	}
	public int getRow(){
		return (y+Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;

	}
	public int getColor(){
		return this.color;
	}
	public void setCol(int col){
		this.col=col;
	}
	public void setRow(int row){
		this.row=row;
	}
	public void updateTemporary(int targetCol,int targetRow){
		x=getX(targetCol);
		y=getY(targetRow);
	}
	public void updatePosition(){
		x=getX(col);
		y=getY(row);
		preCol=col;
		preRow=row;
	}
	public void updatePosition(int targetCol,int targetRow){
		x=getX(targetCol);
		y=getY(targetRow);
		col=targetCol;
		row=targetRow;
		preCol=col;
		preRow=row;
	}
	public void captured(){
		x=-100;
		y=-100;
		row=-1;
		col=-1;
		preRow=-1;
		preCol=-1;
	}
	public void rejectMove(){
		col=preCol;
		row=preRow;
		x=getX(col);
		y=getY(row);

	}
	public void getImage(String imagePath){
		try{
			image=ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void draw(Graphics2D g2){
		g2.drawImage(image,x+10,y+5,Board.SQUARE_SIZE-20,Board.SQUARE_SIZE-20,null);
	}
	public void set_X(int x){this.x=x;}
	public void set_Y(int y){this.y=y;}
	public abstract String getType();
	public abstract void generateAllPossibleMoves(ArrayList<Move> moves,ArrayList<Tile> tiles);
	public abstract boolean hasValidMoves(ArrayList<Tile> tiles);
}