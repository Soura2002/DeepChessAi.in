package desktop.chess;

import desktop.chess.piece.Piece;
import desktop.chess.piece.Pawn;
import desktop.chess.piece.Rook;
import desktop.chess.piece.King;
import desktop.chess.piece.PieceFactory;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
public class Board{
	final int MAX_COL=8;
	final int MAX_ROW=8;
	public static final int SQUARE_SIZE=100;
	public static final int HALF_SQUARE_SIZE=SQUARE_SIZE/2;
	boolean checkMate=false;
	boolean staleMate=false;
	Piece WhiteKing;
	Piece BlackKing;
	Rook WhiteRookRight;
	Rook BlackRookRight;
	Rook WhiteRookLeft;
	Rook BlackRookLeft;
	Pawn promotingPawn;
	int toss;
	int currentColor=0;
	Piece activePiece;
	Piece activePromotionPiece;
	private static Board board;
	private ArrayList<Piece> pieces=new ArrayList<>();
	private ArrayList<Piece> copyPieces=new ArrayList<>();

	private ArrayList<Piece> promotionPieces=new ArrayList<>();
	boolean promoting=false;
	private ArrayList<Tile> tiles=new ArrayList<>();
	Move move;
	public static final class Tile{
		private int col,row;
		private Piece piece;
		Tile(int col,int row){
			this.col=col;
			this.row=row;
			this.piece=null;
		}
		void putPiece(Piece piece){
			this.piece=piece;
		}
		void putPiece(Tile tile,Piece piece){
			tile.piece=piece;
		}
		void setPieceNull(){
			this.piece=null;
		}
		Piece getPiece(){
			return this.piece;
		}
		Piece getPiece(Tile tile){
			return tile.piece;
		}
		public int getCol(){
			return this.col;
		}
		public int getRow(){
			return this.row;
		}
	}
	
	public class Move implements Comparable<Move>{
		protected Tile sourceTile;
		protected Tile destinationTile;
		protected Piece piece;
		protected int color;
		protected int power;
		public Move(Tile sourceTile,Tile destinationTile,Piece piece,int color){
			this.sourceTile=sourceTile;
			this.destinationTile=destinationTile;
			this.piece=piece;
			this.color=color;
			if(getTilePiece(destinationTile.getCol(),destinationTile.getRow())==null){
				power=0;
			}
			else{
				power=getTilePiece(destinationTile.getCol(),destinationTile.getRow()).getPoint();
			}
		}
		public Tile getDestinationTile(){
			return this.destinationTile;
		}
		public Tile getSourceTile(){
			return this.sourceTile;
		}
		public Piece getPiece(){
			return this.piece;
		}
		public int compareTo(Move other){
			return (other.power-this.power);
		}

		public boolean isValidMove(int col,int row){
						
			
			if(!piece.canMove(col,row)){
				
				return false;
			}
			Piece currentKing=null;
			if(color==0){
				currentKing=WhiteKing;
			}
			else{
				currentKing=BlackKing;
			}
			
			Piece temp=destinationTile.getPiece();
			sourceTile.putPiece(null);
			destinationTile.putPiece(piece);
			if(temp!=null){
				pieces.remove(temp);
			}
			
			if(piece==currentKing){
				if(!isAttacked(destinationTile.getCol(),destinationTile.getRow(),currentKing)){
					if(temp!=null){
					
						pieces.add(temp);
					
					}
					sourceTile.putPiece(piece);
					destinationTile.putPiece(temp);
					return true;

				}
								
			}
			else{
				if(!isAttacked(currentKing.getCol(),currentKing.getRow(),currentKing)){
				
					if(temp!=null){
						pieces.add(temp);
					
					}
					sourceTile.putPiece(piece);
					destinationTile.putPiece(temp);
					return true;
				}	
			}
			sourceTile.putPiece(piece);
			destinationTile.putPiece(temp);	
			if(temp!=null){
				pieces.add(temp);
			}

			return false;
		}
		public void makeFinalMove(){
			if(isValidMove(destinationTile.getCol(),destinationTile.getRow())){
				piece.updatePosition(destinationTile.getCol(),destinationTile.getRow());
				Piece p=destinationTile.getPiece();
				sourceTile.putPiece(null);
				destinationTile.putPiece(piece);
				if(p!=null){
					pieces.remove(p);
					p.captured();
				}
				if(piece.getType().equals("Pawn")){
					
					Pawn pawn=(Pawn)piece;
					if(pawn.isPromoted()){
						promotingPawn=pawn;
						promoting=true;
					}
					
				}
				if(isCheckMate(currentColor)){
					checkMate=true;
					return;
				}
				if(isStaleMate(currentColor)){
					staleMate=true;
					return;
				}
				//System.out.println("no checkmates");
				if(promoting==false){
					if(currentColor==0){
						currentColor=1;
					}
					else{
						currentColor=0;
					}
				}
				piece.setHasMoved();
				//System.out.println("moved");
				
			}
			else if(isValidCastle()){
				King castlingKing=(King)piece;
				Rook rook=null;
				destinationTile.putPiece(castlingKing);
				sourceTile.putPiece(null);

				if(castlingKing.isKingSideCastle(castlingKing.getCol(),castlingKing.getRow())){
					rook=getRookRight();
					tiles.get(rook.getCol()%8+rook.getRow()*8).putPiece(null);
					tiles.get((castlingKing.getCol()-1)%8+castlingKing.getRow()*8).putPiece(rook);
					rook.updatePosition(castlingKing.getCol()-1,castlingKing.getRow());
					
				}
				else{
					rook=getRookLeft();
					tiles.get(rook.getCol()%8+rook.getRow()*8).putPiece(null);
					tiles.get((castlingKing.getCol()+1)%8+castlingKing.getRow()*8).putPiece(rook);
					rook.updatePosition(castlingKing.getCol()+1,castlingKing.getRow());

				}
				castlingKing.updatePosition();
				castlingKing.setHasMoved();
				rook.setHasMoved();
				if(isCheckMate(currentColor)){
					checkMate=true;
					return;
				}
				if(isStaleMate(currentColor)){
					staleMate=true;
					return;
				}

				if(currentColor==0){
					currentColor=1;
				}
				else{
					currentColor=0;
				}
				
			}
			else{
				//System.out.println("move rejected");
				piece.rejectMove();
			}
		}
		public boolean isValidCastle(){
			if(!piece.getType().equals("king")){return false;}
			King castlingKing=(King)piece;
			if(!castlingKing.canCastle(castlingKing.getCol(),castlingKing.getRow())){return false;}
			if(isAttacked(castlingKing.getPreCol(),castlingKing.getPreRow(),castlingKing)){return false;}
			if(isAttacked(castlingKing.getCol(),castlingKing.getRow(),castlingKing)){return false;}

			
			return true;
		}
		public Piece makeMove(){
			Piece capturedPiece=getTilePiece(destinationTile.getCol(),destinationTile.getRow());
			destinationTile.putPiece(this.piece);
			sourceTile.putPiece(null);
			if(capturedPiece!=null){
				pieces.remove(capturedPiece);
			}
			if(isCheckMate(piece.getColor())){
				checkMate=true;
			}
			if(isStaleMate(piece.getColor())){
				staleMate=true;
			}
			return capturedPiece;
		}
		public void undoMove(Piece capturedPiece){
			sourceTile.putPiece(this.piece);
			destinationTile.putPiece(capturedPiece);
			if(capturedPiece!=null){

				pieces.add(capturedPiece);
			}
			checkMate=false;
			staleMate=false;
		}
		
	}
	public class PromotionMove extends Move{
		static int white=0;
		static int black=1;
		Piece promotionPiece;
		public PromotionMove(Tile sourceTile,Tile destinationTile,Piece pawn,int color){
			super(sourceTile,destinationTile,pawn,color);
			if(color==0){
				this.promotionPiece=promotionPieces.get(white);
				white=(white+2)%8;
			}
			else{
				this.promotionPiece=promotionPieces.get(black);
				black=(black+2)%8;
			}
			power=promotionPiece.getPoint();
			
		}
		public Piece makeMove(){
			Piece promotedPawn=getTilePiece(sourceTile.getCol(),sourceTile.getRow());
			sourceTile.putPiece(null);
			destinationTile.putPiece(promotionPiece);
			pieces.remove(promotedPawn);
			pieces.add(promotionPiece);
			if(isCheckMate(piece.getColor())){
				checkMate=true;
			}
			if(isStaleMate(piece.getColor())){
				staleMate=true;
			}

			return promotedPawn;

		}
		public void undoMove(Piece promotedPawn){
			destinationTile.putPiece(null);
			sourceTile.putPiece(promotedPawn);
			pieces.add(promotedPawn);
			pieces.remove(promotionPiece);
			checkMate=false;
			staleMate=false;

			
		}
		
	}
	public ArrayList<Move> generateAllPossibleBoardMoves(int color){
		ArrayList<Move> moves=new ArrayList<>();		
		for(Piece p:pieces){
			if(p.getColor()==color){
				p.generateAllPossibleMoves(moves,tiles);
			}
		}
		return moves;

	}
	public void clonePieces(){
		copyPieces.clear();
		for(Piece p:pieces){
			System.out.println(p.getType());
			copyPieces.add(PieceFactory.produce(p.getType(),p.getColor(),p.getCol(),p.getRow()));
		}
	}



 
	private Board(){
		setPieces();
		putPieces();
		setPromotionPieces();
	}
	public void setPromotionPieces(){
		promotionPieces.add(PieceFactory.produce("Queen",0,10,1));
		promotionPieces.add(PieceFactory.produce("Queen",1,10,1));
		promotionPieces.add(PieceFactory.produce("Rook",0,10,2));
		promotionPieces.add(PieceFactory.produce("Rook",1,10,2));
		promotionPieces.add(PieceFactory.produce("Bishop",0,10,3));
		promotionPieces.add(PieceFactory.produce("Bishop",1,10,3));
		promotionPieces.add(PieceFactory.produce("Knight",0,10,4));
		promotionPieces.add(PieceFactory.produce("Knight",1,10,4));

	}
	public static Board getBoard(){
		if(board==null){
			board=new Board();
		}
		return board;
	}
	public boolean isWithinBoard(int targetCol,int targetRow){
		if(targetCol>=0 && targetCol<=7 && targetRow>=0 && targetRow<=7){
			return true;
		}
		return false;
	}

	void getAttackingPieces(ArrayList<Piece> attackingPieces,int color,int kingCol,int kingRow){
		for(Piece p:pieces){
			if(!p.getType().equals("king") && p.getColor()!=color && p.canMove(kingCol,kingRow)){
				attackingPieces.add(p);
			}
		}
	} 
	public boolean isCheckMate(int color){
		ArrayList<Piece> attackingPieces=new ArrayList<>();
		Piece currentKing=null;
		if(color==0){
			currentKing=BlackKing;
		}
		else{
			currentKing=WhiteKing;
		}
		
		getAttackingPieces(attackingPieces,currentKing.getColor(),currentKing.getCol(),currentKing.getRow());
	
		if(attackingPieces.size()==0){return false;}
		if(currentKing.hasValidMoves(tiles)){return false;}
		if(attackingPieces.size()<2){
			ArrayList<Move> moves=generateAllPossibleBoardMoves(currentKing.getColor());
			if(moves.size()!=0){return false;}
						
		}
		return true;
	}
	public boolean isStaleMate(int color){
		Piece currentKing=null;
		if(color==0){
			currentKing=BlackKing;
		}
		else{
			currentKing=WhiteKing;
		}

		ArrayList<Move> moves=generateAllPossibleBoardMoves(currentKing.getColor());
		if(moves.size()==0){return true;}
		return false;

	}	
	public void putPieces(){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				tiles.add(new Tile(j,i));
			}
		}
		for(Piece p:pieces){
			tiles.get(p.getCol()%8+p.getRow()*8).putPiece(p);
			if(p.getType().equals("king")){
				if(p.getColor()==0){
					WhiteKing=p;
				}
				else{
					BlackKing=p;
				}
				
			
			}
			else if(p.getType().equals("Rook")){
				if(p.getColor()==0){
					if(p.getCol()==7){
						WhiteRookRight=(Rook)p;
					}
					else{
						WhiteRookLeft=(Rook)p;
					}
				}
				else{
					if(p.getCol()==7){
						BlackRookRight=(Rook)p;
					}
					else{
						BlackRookLeft=(Rook)p;
					}

				}
			}
		}
		
	}
	public Rook getRookRight(){
		if(currentColor==0){
			return WhiteRookRight;
		}
		return BlackRookRight;
	}
	public Rook getRookLeft(){
		if(currentColor==0){
			return WhiteRookLeft;
		}
		return BlackRookLeft;
	}

	public Piece getTilePiece(int targetCol,int targetRow){
		return tiles.get(targetCol%8+targetRow*8).getPiece();
	}
	public boolean isAttacked(int targetCol,int targetRow,Piece currentKing){
		
		int color=currentKing.getColor();
		for(Piece p:pieces){
			if(p.getType().equals("Pawn")){
				Pawn pawn=(Pawn)p;
				if(pawn.getColor()!=color && pawn.canCapture(targetCol,targetRow)){
					//System.out.println(p.getType()+p.getColor());
					return true;
				}

			}
			else{
				if(p.getColor()!=color && p.canMove(targetCol,targetRow)){
					//System.out.println(p.getType()+p.getColor());
					return true;
				}
			}
		}
		return false;
	}
	public void setPieces(){
		toss=new Random().nextInt(2);
		if(toss==0){
			//White Team

			pieces.add(PieceFactory.produce("King",0,4,7));
			pieces.add(PieceFactory.produce("Queen",0,3,7));
			pieces.add(PieceFactory.produce("Bishop",0,2,7));
			pieces.add(PieceFactory.produce("Knight",0,1,7));
			pieces.add(PieceFactory.produce("Rook",0,0,7));
			pieces.add(PieceFactory.produce("Bishop",0,5,7));
			pieces.add(PieceFactory.produce("Knight",0,6,7));
			pieces.add(PieceFactory.produce("Rook",0,7,7));
			for(int i=0;i<=7;i++){
				pieces.add(PieceFactory.produce("Pawn",0,i,6));
			}


			//Black Team
		
			pieces.add(PieceFactory.produce("King",1,4,0));
			pieces.add(PieceFactory.produce("Queen",1,3,0));
			pieces.add(PieceFactory.produce("Bishop",1,2,0));
			pieces.add(PieceFactory.produce("Knight",1,1,0));
			pieces.add(PieceFactory.produce("Rook",1,0,0));
			pieces.add(PieceFactory.produce("Bishop",1,5,0));
			pieces.add(PieceFactory.produce("Knight",1,6,0));
			pieces.add(PieceFactory.produce("Rook",1,7,0));
			for(int i=0;i<=7;i++){
				pieces.add(PieceFactory.produce("Pawn",1,i,1));
			}

		}
		else{
			//White team
			pieces.add(PieceFactory.produce("King",0,4,0));
			pieces.add(PieceFactory.produce("Queen",0,3,0));
			pieces.add(PieceFactory.produce("Bishop",0,2,0));
			pieces.add(PieceFactory.produce("Knight",0,1,0));
			pieces.add(PieceFactory.produce("Rook",0,0,0));
			pieces.add(PieceFactory.produce("Bishop",0,5,0));
			pieces.add(PieceFactory.produce("Knight",0,6,0));
			pieces.add(PieceFactory.produce("Rook",0,7,0));
			for(int i=0;i<=7;i++){
				pieces.add(PieceFactory.produce("Pawn",0,i,1));
			}
			//Black Team
			pieces.add(PieceFactory.produce("King",1,4,7));
			pieces.add(PieceFactory.produce("Queen",1,3,7));
			pieces.add(PieceFactory.produce("Bishop",1,2,7));
			pieces.add(PieceFactory.produce("Knight",1,1,7));
			pieces.add(PieceFactory.produce("Rook",1,0,7));
			pieces.add(PieceFactory.produce("Bishop",1,5,7));
			pieces.add(PieceFactory.produce("Knight",1,6,7));
			pieces.add(PieceFactory.produce("Rook",1,7,7));
			for(int i=0;i<=7;i++){
				pieces.add(PieceFactory.produce("Pawn",1,i,6));
			}


			
		}
		
		
		
	}
	public ArrayList<Piece> getCopyPieces(){
		
		return copyPieces;
	}
	public void draw(Graphics2D g2){
		
		for(int i=0;i<MAX_ROW;i++){
			for(int j=0;j<MAX_COL;j++){
				if(i%2==0){
					if(j%2==0){
						g2.setColor(new Color(210,165,125));

					}
					else{
						g2.setColor(new Color(175,115,70));

					}

				}
				else{
					if(j%2==0){
						g2.setColor(new Color(175,115,70));

					}
					else{
						g2.setColor(new Color(210,165,125));

					}

				}
				g2.fillRect(j*SQUARE_SIZE,i*SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
				
			}

		}
		for (Piece p : pieces) {
    			if (p != null) {
        			p.draw(g2);
    			}
		}
		if(promoting){
			for(Piece p:promotionPieces){
				if(p.getColor()==currentColor){
					p.draw(g2);
				}
			}
		}
		if(checkMate){
			String a="";
			if(currentColor==0){
				a="White Wins!!";
			}
			else{
				a="black Wins!!";
			}
			g2.setFont(new Font("Arial",Font.PLAIN,90));
			g2.setColor(Color.green);
			g2.drawString(a,200,400);
		}
		if(staleMate){
			String a="Match Drawn";
			g2.setFont(new Font("Arial",Font.PLAIN,90));
			g2.setColor(Color.green);
			g2.drawString(a,200,400);


		}
		if(activePiece!=null){
			g2.setColor(Color.white);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
			g2.fillRect(activePiece.getCol()*SQUARE_SIZE,activePiece.getRow()*SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
			activePiece.draw(g2);
		}
	}
	public void updateBoard(int x,int y){
		if(promoting){
			promote(x,y);
		}
		if(activePiece==null){
			for(Piece p: pieces){
				if(p.getColor()==currentColor && p.getCol()==x/SQUARE_SIZE && p.getRow()==y/SQUARE_SIZE){
					activePiece=p;

				}
			}
		}
		else{
			activePiece.set_X(x-HALF_SQUARE_SIZE);
			activePiece.set_Y(y-HALF_SQUARE_SIZE);
			activePiece.setCol(activePiece.getCol());
			activePiece.setRow(activePiece.getRow());
			
			
		}
	}
	public void promote(int x,int y){
		if(activePromotionPiece==null){
			for(Piece p: promotionPieces){
				if(p.getColor()==currentColor && p.getCol()==x/SQUARE_SIZE && p.getRow()==y/SQUARE_SIZE){
					activePromotionPiece=p;

				}
			}

		}
		else{
			//activePromotionPiece.updatePosition(promotingPawn.getCol(),promotingPawn.getRow());
			Piece p=PieceFactory.produce(activePromotionPiece.getType(),currentColor,promotingPawn.getCol(),promotingPawn.getRow());
			tiles.get(promotingPawn.getCol()%8+promotingPawn.getRow()*8).putPiece(p);
			promotingPawn.captured();
			pieces.add(p);
			pieces.remove(promotingPawn);
			promotingPawn=null;
			activePromotionPiece=null;
			promoting=false;
			if(currentColor==0){
				currentColor=1;
			}
			else{
				currentColor=0;
			}
		}
	}
	public void updateBoardState(){
		if(activePiece!=null){
		       if(isWithinBoard(activePiece.getCol(),activePiece.getRow())){
		       		Move move=new Move(tiles.get(activePiece.getPreCol()%8+activePiece.getPreRow()*8),tiles.get(activePiece.getCol()%8+activePiece.getRow()*8),activePiece,currentColor);
				move.makeFinalMove();

		       }
		       activePiece=null;
			
		}
	}
	
		
	
}