package desktop.chess;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

class Mouse extends MouseAdapter{
	int x,y;
	boolean pressed;

	public void mousePressed(MouseEvent e){
		pressed=true;
	}
	public void mouseReleased(MouseEvent e){
		pressed=false;
	}
	public void mouseDragged(MouseEvent e){
		x=e.getX();
		y=e.getY();
	}
	public void mouseMoved(MouseEvent e){
		x=e.getX();
		y=e.getY();

	}


}



class GamePanel extends JPanel implements Runnable{
	public static final int width=1100;
	public static final int height=800;
	final int FPS=60;
	Mouse mouse=new Mouse();
	Thread gameThread;
	Board board;
	int HumanPlayer;
	DeepChessAi engine;
	GamePanel(){
		setPreferredSize(new Dimension(width,height));
		setBackground(Color.black);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		board=Board.getBoard();
		if(board.toss==0){
			HumanPlayer=0;
			engine=new DeepChessAi(board,1); 
		}
		else{
			HumanPlayer=1;
			engine=new DeepChessAi(board,0);
		}
	}
	public void launchGame(){
		gameThread=new Thread(this);
		gameThread.start();
	}
	public void run(){
		double drawInterval=1000000000/FPS;
		double delta=0;
		long lastTime=System.nanoTime();
		long currentTime;

		while(gameThread!=null){
			currentTime=System.nanoTime();
			delta+=(currentTime-lastTime)/drawInterval;
			if (delta >= 1) {
    				update();
   			 	repaint();
   				delta--;
			}
			
		}
	}

	private void update(){
		if(!board.checkMate && !board.staleMate){
			if(board.currentColor==HumanPlayer){
				if(mouse.pressed==true){
				
					board.updateBoard(mouse.x,mouse.y);
			
				}
				else{
					board.updateBoardState();

				}
			}
			else{
				engine.playMove();
			}
		}				
				
		
	
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		board.draw(g2);
		
	}
}