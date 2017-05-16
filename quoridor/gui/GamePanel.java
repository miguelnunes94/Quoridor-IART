package quoridor.gui;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import quoridor.logic.Player;
import quoridor.logic.Quoridor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	public int hWinner=-1;
	public static long delay=500;
	public static long tempoPrimeiraJogada=0;
	public static long nodesPrimeiraJogada=0;
	private Quoridor jogo;
	private int  alturaParede;
	private int quadriculaSize;
	private int raio;
	private int meioRaio;
	private JLabel lblWalls_p1;
	private JLabel lblWalls_p2;
	private JLabel lblPlayer;
	public int fimDoJogo;
	private int player1Mode,player2Mode;
	protected boolean p1MinPath,p2MinPath;
	private boolean sleep =false;
	private GameLoop gameLoop;
	public GamePanel(JLabel lblWalls_p1, JLabel lblWalls_p2, JLabel lblPlayer) 
	{
		delay=500;
		setLayout(null);
		player1Mode=0;
		player2Mode=0;
		fimDoJogo=0;
		this.lblWalls_p1=lblWalls_p1;
		this.lblWalls_p2=lblWalls_p2;
		this.lblPlayer=lblPlayer;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(fimDoJogo==0){
					int xTab,yTab;
					xTab=calcTabCoord(e.getX());
					yTab=calcTabCoord(e.getY());
					fimDoJogo=jogo.processPlay(xTab,yTab);
					updateScreen();
				}
				if(fimDoJogo!=0){
					showWinner();
					return;
				}
			}

			private int calcTabCoord(int x) {
				int acc=0;
				for(int i=0;i<17;i++){
					if(i%2==0){
						acc+=quadriculaSize;
					}else acc+=alturaParede;
					if(x<acc){
						return i;
					}
				}
				return -1;
			}
		});		
		alturaParede=10;
		quadriculaSize=50;
		jogo= new Quoridor(0,0,0,0);
		raio = quadriculaSize/2;
		meioRaio=raio/2;
		this.gameLoop = new GameLoop();
		gameLoop.start();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		String [][] board = jogo.getBoard();
		int x=0,y=0;//posiï¿½ï¿½o no ecra em que vai a desenhar
		for (int i = 0; i < board.length; i++) {
			x=0;
			for (int j = 0; j < board[i].length; j++) {
				if(i%2==0){// linha de quadriculas
					if(j%2==0){//quadricula
						g.drawRect(x, y, quadriculaSize, quadriculaSize);
						x+=quadriculaSize;
					}else {//parede
						desenhaParede(board[i][j], x, y, alturaParede, quadriculaSize, g);
						x+=alturaParede;
					}
				}else {// linha para paredes
					if(j%2==0){//parede
						desenhaParede(board[i][j], x, y, quadriculaSize, alturaParede, g);
						x+=quadriculaSize;
					}else {//parede
						desenhaParede(board[i][j], x, y, alturaParede, alturaParede, g);
						x+=alturaParede;
					}
				}
			}
			if(i%2==0){
				y+=quadriculaSize;
			}else y+= alturaParede;
		}
		g.setColor(Quoridor.player1.cor);
		g.fillOval(Quoridor.player1.coordToScreen("x", quadriculaSize, alturaParede)+meioRaio,Quoridor.player1.coordToScreen("y", quadriculaSize, alturaParede)+meioRaio, raio, raio);
		g.setColor(Quoridor.player2.cor);
		g.fillOval(Quoridor.player2.coordToScreen("x", quadriculaSize, alturaParede)+meioRaio,Quoridor.player2.coordToScreen("y", quadriculaSize, alturaParede)+meioRaio, raio, raio);
		if(p1MinPath){
			debugShowPlayerMinPath(Quoridor.player1, Quoridor.player2, g,Color.CYAN);
		}
		if(p2MinPath){
			debugShowPlayerMinPath(Quoridor.player2, Quoridor.player1, g, Color.orange);
		}
	}
	public void desenhaParede(String parede,int x, int y, int largura, int altura,Graphics g){
		if(parede.equals("x")){
			g.setColor(Color.black);
			g.fillRect(x, y, largura, altura);
		}
	}
	public void updateScreen(){
		lblWalls_p1.setText(Quoridor.player1.walls+"");
		lblWalls_p2.setText(Quoridor.player2.walls+"");
		if(Quoridor.playerTurn==0){
			lblPlayer.setForeground(Quoridor.player1.cor);
			lblPlayer.setText("Player1");
		}else {
			lblPlayer.setForeground(Quoridor.player2.cor);
			lblPlayer.setText("Player2");
		}
		SwingUtilities.getWindowAncestor(this).repaint();
	}
	@SuppressWarnings("deprecation")
	public void resetGame(int player1Mode, int player2Mode, int p1heuristica, int p2heuristica, int player1Profundiade, int player2Profundidade) {
		gameLoop.stop();
		fimDoJogo=0;
		sleep=false;
		jogo= new Quoridor(p1heuristica,p2heuristica, player1Profundiade, player2Profundidade);
		this.player1Mode=player1Mode;
		this.player2Mode=player2Mode;
		gameLoop = new GameLoop();
		gameLoop.start();
		updateScreen();
		tempoPrimeiraJogada=-1;
	}
	private void showWinner(){
		if(delay!=0)// se está a gerar as tabelas não queremos delay
			JOptionPane.showMessageDialog(this, "Player "+fimDoJogo+" win!");
		//		int h = -1;
		if(fimDoJogo==1){// usado para testes
			hWinner = jogo.p1Heuristica;
		}else hWinner = jogo.p2Heuristica;
		//		JOptionPane.showMessageDialog(this, "Player "+fimDoJogo+"(h"+h+") win!");
	}
	private void debugShowPlayerMinPath(Player player, Player inimigo, Graphics g,Color c){
		ArrayList<int[]>p1;	
		Player p = new Player(player.x, player.y,player.dir,player.walls, c);
		p1 = Quoridor.getMinPathArray(p, inimigo,jogo.getBoard());	
		for (int i = 0; i < p1.size(); i++) {
			p.x=p1.get(i)[0];
			p.y=p1.get(i)[1];
			g.setColor(p.cor);
			g.fillOval((p.x*quadriculaSize+p.x*alturaParede)/2+meioRaio,(p.y*quadriculaSize+p.y*alturaParede)/2+meioRaio, raio, raio);	
		}
	}
	private class GameLoop extends Thread {
		@Override
		public void run(){
			while(true){
				int player = -1;
				while(sleep){
					try {
						Thread.sleep(GamePanel.delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(Quoridor.playerTurn==0 && player1Mode!=0)
						break;
					else if(Quoridor.playerTurn==1 && player2Mode!=0)
						break;
				}
				sleep=true;
				if(Quoridor.playerTurn==0 && player1Mode!=0)
					player=0;
				else if(Quoridor.playerTurn==1 && player2Mode!=0)
					player=1;
				else continue;
				fimDoJogo=jogo.play(player);
				if(fimDoJogo!=0){
					player1Mode=0;
					player2Mode=0;
					updateScreen();
					showWinner();
					continue;
				}
				updateScreen();
			}
		}
	}
}
