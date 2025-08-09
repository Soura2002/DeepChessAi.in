package desktop.chess;


import javax.swing.JFrame;

class MyChessApplication{
	public static void main(String[] args){
		JFrame gameFrame=new JFrame("DeepChessAI.in");
		gameFrame.setDefaultCloseOperation(gameFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		
		//adding game panel

		GamePanel gp=new GamePanel();
		gameFrame.add(gp);
		gameFrame.pack();

		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);

		gp.launchGame();
	}
}