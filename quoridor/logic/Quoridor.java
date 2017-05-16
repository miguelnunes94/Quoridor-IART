package quoridor.logic;

import java.awt.Color;
import java.util.ArrayList;

import quoridor.gui.GamePanel;

public class Quoridor{
	public static  Player player1;
	public static Player player2;
	public static int playerTurn;
	private static String[][] board;
	public int p1Heuristica;
	public int p2Heuristica;
	private int p1Profundidade;
	private int p2Profundidade;	

	public Quoridor(int p1Heuristica, int p2Heuristica, int player1Profundiade, int player2Profundidade) {
		player1 = new Player(8,16,-1,10, Color.blue);
		player2 = new Player(8,0,1,10, Color.red);	
		playerTurn=0;
		this.p1Heuristica= p1Heuristica;
		this.p2Heuristica=p2Heuristica;
		this.p1Profundidade = player1Profundiade;
		this.p2Profundidade = player2Profundidade;
		resetBoard();
	}
	/**
	 * fazer jogada automatica
	 * @param playerNo jogador
	 * @return 
	 */
	public int play(int playerNo) {
		Player player = null,inimigo = null;
		int heuristica=0, profundidade=0;
		if(playerNo==0){
			player=player1;
			inimigo=player2;
			heuristica=p1Heuristica;
			profundidade=p1Profundidade;
		}else if(playerNo==1){
			player=player2;
			inimigo=player1;
			heuristica=p2Heuristica;
			profundidade=p2Profundidade;
		}	
		MinimaxAlphaBeta.numNodes=0;
		MinimaxAlphaBeta minimaxalphabeta= new MinimaxAlphaBeta(profundidade, player, inimigo,cloneArray(board),heuristica);
		long ti = System.currentTimeMillis();
		Node node=minimaxalphabeta.start();
		if(GamePanel.tempoPrimeiraJogada==-1){
			GamePanel.tempoPrimeiraJogada= System.currentTimeMillis()-ti;
			GamePanel.nodesPrimeiraJogada=MinimaxAlphaBeta.numNodes;
		}
		processPlay(node.x, node.y);
		return checkGameOver();
	}

	public String[][] getBoard() {
		return board;
	}
	private  int checkGameOver() {		
		if(player1.y==0)
			return 1;
		else if (player2.y==16)
			return 2;
		return 0;
	}
	public void resetBoard() {
		board = new String[][]{
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"},
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
				{"0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"," ","0"}
		};
	}
	public int processPlay(int x, int y) {
		Player player,inimigo;
		if(playerTurn==0){
			player=player1;
			inimigo=player2;
		}else {
			player=player2;
			inimigo=player1;
		}
		if(board[y][x].equals(" ")){// clicou numa parede
			if(player.walls>0){
				if(poeParede(player,inimigo,x, y,board))
					playerTurn=(playerTurn+1)%2;
				else System.err.println("[ProcessPlay]Parede em posicao invalida: "+x+","+y);
			}
		}else {// clicou numa quadricula
			if(playerTurn==0){
				if(player2.x==x && player2.y==y){
					System.err.println("[ProcessPlay]Já está nesssa posição: "+x+","+y);
					return 0;
				}
			}else if(playerTurn==1){
				if(player1.x==x && player1.y==y){
					System.err.println("[ProcessPlay]Já está nesssa posição: "+x+","+y);
					return 0;
				}
			}
			validMove(x,y,player,inimigo);
		}
		return checkGameOver();
	}
	public static Boolean poeParede(Player player,Player inimigo,int x, int y, String [][] b){
		if(y>16||y<0)
			return false;
		if(x>16||x<0)
			return false;
		int xI=0,yI=0;
		if(x%2==0){//parede horizontal
			xI=1;
			if(y<=0 || y>15 || x>14)
				return false;
		}else if(y%2==0){//parede vertical
			yI=1;
			if(x<0 || x>15 || y>14){
				return false;
			}
		}else return false;
		boolean livre=true;
		for (int i = 0; i < 3; i++) {
			if(!b[y+i*yI][x+i*xI].equals(" ")){
				livre=false;
				break;
			}
		}
		if(livre){
			for (int i = 0; i < 3; i++) {
				b[y+i*yI][x+i*xI]="x";
			}
			int [] p1 = getMinPath(player,inimigo,b);
			int [] p2 = getMinPath(inimigo,player,b);
			if(p1==null || p2==null){//jogador fica preso => nao permitir a jogada
				for (int i = 0; i < 3; i++) {
					b[y+i*yI][x+i*xI]=" ";
				}
				livre=false;
			}
		}
		if(livre){
			player.walls--;
		}
		return livre;
	}
	/**
	 * Executa movimento se for valido
	 * @param x coord x do tabuleiro onde clicou
	 * @param y coord y do tabuleiro onde clicou
	 * @param player jogador que fez a jogada
	 */
	private void validMove(int x, int y, Player player, Player inimigo) {
		int accY =0,accX=0;

		if(player.y>y)accY=-1;
		else if(player.y<y)accY=1;

		if(player.x>x)accX=-1;
		else if(player.x<x)accX=1;
		

		/**
		 * simpleMove = quadricula esta livre, so esta mover-se uma quadricula e nao ha paredes pelo meio
		 */
		boolean simpleMove = 
				((Math.abs(player.x-x)==2 && Math.abs(player.y-y)==0) 
						||(Math.abs(player.x-x)==0 && Math.abs(player.y-y)==2))
						&& board[noMeio(player.y, y)][noMeio(player.x, x)].equals(" ");
		/**
		 * doubleMove = esta numa quadricula adjacente e por isso pode saltar por cima do inimigo, sem saltar paredes
		 */
		boolean doubleMove = 
		(((Math.abs(player.x-x)==4 && Math.abs(player.y-y)==0 && inimigo.x==x+(player.x-x+accX*2)) 
				||(Math.abs(player.x-x)==0 && Math.abs(player.y-y)==4 && inimigo.y==y+(player.y-y+accY*2)))//andar em reta
				&& board[y+(player.y-y+accY)][x+(player.x-x+accX)].equals(" ")// e nao ha parede no meio
				&& board[y+(player.y-y+accY*3)][x+(player.x-x+accX*3)].equals(" "))
				|| 
				((Math.abs(player.x-x)==2 && Math.abs(player.y-y)==2)
						&&((Math.abs(inimigo.x-x)==2 && inimigo.y==y)
								||(Math.abs(inimigo.y-y)==2 && inimigo.x==x))
								&& board[noMeio(player.y, inimigo.y)][noMeio(player.x, inimigo.x)].equals(" ")
								&& board[noMeio(y, inimigo.y)][noMeio(x, inimigo.x)].equals(" ")
						);
		if(simpleMove || doubleMove){
			player.x=x;
			player.y=y;
			playerTurn=(playerTurn+1)%2;
		}else System.err.println("[validMove] Movimento invalido: "+x+","+y);
	}

	public static int[] getMinPath(Player player, Player inimigo,String [][] b){
		Astar astar = new Astar(b);
		return astar.aStart(player, inimigo);
	}
	public static ArrayList<int[]> getMinPathArray(Player player, Player inimigo, String[][] b){
		Astar astar = new Astar(b);
		return astar.aStartArray(player, inimigo);
	}
		public static String[][] cloneArray(String[][] src) {
		int length = src.length;
		String[][] target = new String[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}
	/**
	 * gera os movimentos (sucessores) possiveis
	 */
	public static ArrayList<Node> getMoves(Player player,Player inimigo,Node raiz,Node pai, String [][] b) {
		ArrayList<Node> moves = new ArrayList<Node>();
		verifyMove(player,inimigo,raiz,"y",-1,moves,pai,b);//add estado se pode recuar no y
		verifyMove(player,inimigo,raiz,"y",1,moves,pai,b);//add estado se pode avancar no y
		verifyMove(player,inimigo,raiz,"x",-1,moves,pai,b);//add estado se pode recuar no x
		verifyMove(player,inimigo,raiz,"x",1,moves,pai,b);//add estado se pode avancar no x
		return moves;
	}
	/**
	 * gera e adicona sucessor se um determinado movimento for possivel
	 * @param node pai
	 * @param dir direccao para a vitoria
	 * @param generatedNodes nos ja gerados
	 * @param coordName coordenada a avaliar
	 * @param inc sentido do movimento (1 = avancar, -1 = recuar)
	 * @param moves sucessores gerados
	 */
	public static void verifyMove(Player player, Player inimigo,Node node, String coordName, int inc,ArrayList<Node> moves, Node pai, String [][] b){
		String [][] board = null;
		if(pai==null){
			board=b;
		}else board = pai.board ;
		int coord=0,incX=inc,incY=inc;
		if(coordName.equals("x")){
			coord=node.x;
			incY=0;
		}else if(coordName.equals("y")){
			coord=node.y;
			incX=0;
		}
		if(node.x+2*incX==player.x && node.y+2*incY==player.y)
			return;
		if((coord>=2&&inc<0) || (coord<=board.length-2&&inc>0)){// nao sai fora do tabuleiro
			if(board[node.y+incY][node.x+incX].equals(" ")){// parede nao ocupada
				int newX,newY;// coords da quadricula
				newX = node.x+incX*2;
				newY =node.y+incY*2;
				Node novoFilho;
				if(pai==null)
					novoFilho= new Node(node.g+1,newX, newY,node,fEvalAstar(newY,node.g+1 ,player.dir),false,null);
				else novoFilho= new Node(node.g+1,newX, newY,pai,fEvalAstar(newY,node.g+1 ,player.dir),false,pai.board);
				if(newX==inimigo.x && newY==inimigo.y){
					ArrayList<Node> sucessores = getMoves(player,inimigo,novoFilho,null,board);
					for (int i = 0; i < sucessores.size(); i++) {
						if(sucessores.get(i).x==player.x && sucessores.get(i).y==player.y)
							continue;
						if(!sucessores.get(i).equals(node)){
							if(pai==null)
								sucessores.get(i).parent=node;
							else sucessores.get(i).parent=pai;//para uso no minimax
							sucessores.get(i).f=fEvalAstar(sucessores.get(i).y,novoFilho.g,player.dir);
							sucessores.get(i).g=novoFilho.g;
							sucessores.get(i).board=novoFilho.board;
							moves.add(sucessores.get(i));
						}
					}
					return;	
				}
				if(!(novoFilho.y==inimigo.y && novoFilho.x==inimigo.x))
					moves.add(novoFilho);
			}
		}
	}
	/** funcao de avaliacao
	 */
	public static int fEvalAstar(int y,int g, int dir) {
		if(dir==-1){
			return (y/2)+g;//y/2 porque o y tambem conta as paredes
		}else {
			return (board.length/2) -1 -(y/2) +g;//board.length/2 pelo mesmo motivo que o y
		}
	}
	public static int noMeio(int x1, int x2){
		if(x1>x2){
			return x2+1;
		}else if(x1<x2)
			return x1+1;
		else return x1;
	}
}
