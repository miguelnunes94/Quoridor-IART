package quoridor.logic;

import java.util.ArrayList;
import java.util.Collections;

public class MinimaxAlphaBeta {
	public static int numNodes=0;
	private Node raiz;
	private Player player,inimigo;
	private int profundidade, heuristica;
	public MinimaxAlphaBeta(int profundidade,Player player,Player inimigo,String[][] board, int heuristica) {
		this.player=player;
		this.inimigo=inimigo;
		this.profundidade=profundidade;		
		this.heuristica=heuristica;
		raiz = new Node(0,player.x,player.y,null,0,false,board);
	}	
	public Node start()
	{
		return minimaxAlphaBeta(raiz,profundidade,Integer.MIN_VALUE,Integer.MAX_VALUE,true,player,inimigo);
	}

	public Node minimaxAlphaBeta(Node node,int profundidade,int alfa,int beta,Boolean maximizingPlayer,Player p,Player i)
	{	
		numNodes++;
		boolean estadoFinal=((i.dir==-1&&i.y==0) || (i.dir==1 && i.y==16));
		if(profundidade==0 || estadoFinal){
			if(!maximizingPlayer)
				node.f=stateValue(node,i,p);/*the heuristic value of node*/
			else node.f=stateValue(node,p,i);
			return node;			
		}
		int v;		

		if(maximizingPlayer)
		{
			Node ret = null;
			createNodes(node,p,i);			
			v= Integer.MIN_VALUE;
			for(Node next : node.sucessores){
				Player newP =null;			
				if(next.wall){
					newP=new Player(p.x, p.y,p.dir,p.walls,null);
				}else {
					newP= new Player(next.x, next.y, p.dir,p.walls, null);
				}
				Node max=minimaxAlphaBeta(next,profundidade-1,alfa,beta,false,i,newP);				
				if(max.f>v){
					ret=next;
					v=max.f;
				}
				alfa = Math.max(alfa,v);
				if(beta <= alfa){
					break;//beta cut-off
				}
			}
			ret.f=v;
			return ret;
		}
		else
		{
			Node ret = null;
			createNodes(node,p,i);				
			v = Integer.MAX_VALUE;
			for(Node next : node.sucessores){
				Player newP =null;				
				if(next.wall){
					newP=new Player(p.x, p.y, p.dir,p.walls,null);					
				}else{
					newP = new Player(next.x, next.y, p.dir,p.walls, null);
				}
				Node min=minimaxAlphaBeta(next,profundidade-1,alfa,beta,true,i,newP);			
				if(min.f<v){
					v=min.f;
					ret=next;
				}
				beta = Math.min(alfa,v);
				if(beta <= alfa){
					break;//alfa cut-off
				}
			}
			ret.f=v;
			return ret;
		}
	}
	public void createNodes(Node pai,Player player, Player inimigo){
		//all 4 possible moves
		pai.sucessores=Quoridor.getMoves(player, inimigo, new Node(0,player.x,player.y,pai,0,false,null),pai,null);
		for(int i=0;i<pai.sucessores.size();i++){
			// em situcoes muito raras se o player vai para o unico sitio onde o inimigo pode ganhar=>esse movimento ้ invalido
			Node move = pai.sucessores.get(i);
			int [] p2MinPath = Quoridor.getMinPath(inimigo,new Player(move.x, move.y, player.dir, player.walls, null),pai.board);
			if(p2MinPath==null){
				pai.sucessores.remove(i);
				i--;
			}
		}
		pai.sucessores.addAll(walls2(pai,player,inimigo));		
		// TODO				
		// ou por barra num sitio que previna o alogamento do proprio caminho
	}
	private ArrayList<Node> walls2(Node pai,Player p, Player i) {
		String [][] board = pai.board;
		ArrayList<Node>ret=new ArrayList<Node>();
		if(p.walls==0)
			return ret;
		ArrayList<int[]> path=Quoridor.getMinPathArray(i,p,board);
		path.add(new int[]{i.x,i.y});
		Collections.reverse(path);
		for (int j = 0; j < path.size()-1; j++) {
			Player p1 = new Player(p.x, p.y, p.dir,p.walls, null);
			String [][] b = Quoridor.cloneArray(board);
			int x, xNext;
			int y, yNext;
			x=path.get(j)[0];
			y=path.get(j)[1];
			xNext=path.get(j+1)[0];
			yNext=path.get(j+1)[1];
			int xParede,yParede;
			xParede=Quoridor.noMeio(x, xNext);
			yParede=Quoridor.noMeio(y, yNext);

			if(x==xParede){//horizontal
				if(xParede<=14 && Quoridor.poeParede(p1, i, xParede, yParede, b)){
					ret.add(new Node(0, xParede, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);			
				p1.walls=p.walls;
				if(xParede-2>=0 && Quoridor.poeParede(p1, i, xParede-2, yParede, b)){
					ret.add(new Node(0, xParede-2, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);				
				p1.walls=p.walls;
				if(xParede-4>=0 &&Quoridor.poeParede(p1, i, xParede-4, yParede, b)){
					ret.add(new Node(0, xParede-4, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);	
				p1.walls=p.walls;
				if(xParede+2<=14 &&Quoridor.poeParede(p1, i, xParede+2, yParede, b)){
					ret.add(new Node(0, xParede+2, yParede,pai,0,true,b));
				}
			}else if(y==yParede){//vertical
				if(yParede<=14 && Quoridor.poeParede(p1, i, xParede, yParede, b)){
					ret.add(new Node(0, xParede, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);				
				p1.walls=p.walls;
				if(yParede-2>=0 && Quoridor.poeParede(p1, i, xParede, yParede-2, b)){
					ret.add(new Node(0, xParede, yParede-2,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);				
				p1.walls=p.walls;
				if(yParede-4>=0 && Quoridor.poeParede(p1, i, xParede, yParede-4, b)){
					ret.add(new Node(0, xParede, yParede-4,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);	
				p1.walls=p.walls;
				if(yParede+2<=14 && Quoridor.poeParede(p1, i, xParede, yParede+2, b)){
					ret.add(new Node(0, xParede, yParede+2,pai,0,true,b));
				}
			}
		}	
		return ret;
	}

	public int stateValue(Node node,Player player, Player inimigo) {
		String [][] newBoard = Quoridor.cloneArray(node.board);
		Node antecessor = node;

		ArrayList<Node>jogadas = new ArrayList<Node>();
		while(antecessor.parent!=null){// formar o caminho ate a nรณ antes da raiz			
			jogadas.add(antecessor);
			antecessor=antecessor.parent;
		}	
		int [] p1MinPath = null;
		int [] p2MinPath = null;
		p1MinPath = Quoridor.getMinPath(player, inimigo, newBoard);
		p2MinPath = Quoridor.getMinPath(inimigo, player, newBoard);
		if(p1MinPath == null || p2MinPath == null){
			System.out.println("=> gerada uma jogada invalida!");
			System.exit(-1);
		}
		int playerDistToWin = 0;
		if(player.dir==-1){
			playerDistToWin=(player.y)/2;
		}else {
			playerDistToWin = (16-player.y)/2;
		}
		int ret =0;
		switch (heuristica){
		case 0: 
			ret=(p2MinPath[2]-p1MinPath[2]-jogadas.size());
			break;
		case 1: 
			ret=p2MinPath[2]-p1MinPath[2]+(8-playerDistToWin)-jogadas.size();
			break;
		case 2: 
			ret=(p2MinPath[2]-p1MinPath[2])-p1MinPath[2]+player.walls-jogadas.size();
			break;
		case 3: 
			ret=(p2MinPath[2]-p1MinPath[2])+(player.walls-inimigo.walls)-jogadas.size();
			break;
		default:
			System.err.println("heuristica nao existe");
			System.exit(-1);
			break;
		}
		return ret;
	}
	/*
	public ArrayList<Node> walls(Node node,Player p,Player i)
	{
		ArrayList<Node> wallsNodes=new ArrayList<Node>();
		int x = 0,y = 0,direccao=0;
		int [] path;
		Boolean putWall=false;
		Boolean truePlayer=true;
		Player p1,p2;
		if(p.walls==0)
			return null;
		if(i.dir==inimigo.dir)
		{
			path=Quoridor.getMinPath(p, i,node.board);
			direccao=inimigo.dir;
			p1=new Player(p.x,p.y,p.dir,p.walls,p.cor);
			p2=new Player(i.x,i.y,i.dir,i.walls,i.cor);
		}
		else
		{			
			path=Quoridor.getMinPath(i, p,node.board);
			direccao=player.dir;
			p2=new Player(p.x,p.y,p.dir,p.walls,p.cor);
			p1=new Player(i.x,i.y,i.dir,i.walls,i.cor);
		}		
		int ytotal=path[2];
		int yacum=1;
		String [][] auxBoard;		
		while(ytotal>2){
			//System.out.println(ytotal);
			putWall=false;
			auxBoard=Quoridor.cloneArray(node.board);
			if(truePlayer)
			{
				if(Quoridor.poeParede(p1,p2,path[0]-1,path[1] + direccao*yacum,auxBoard))
				{
					x=path[0]-1;
					y=path[1] + direccao*yacum;			
					putWall=true;
					System.out.println("ESQUERDA");
				}			
				else if(Quoridor.poeParede(p1,p2,path[0],path[1] + direccao*yacum,auxBoard)){
					x=path[0];
					y=path[1] + direccao*yacum;
					putWall=true;
					System.out.println("MEIO");
				}	
				else if(Quoridor.poeParede(p1,p2,path[0]+1,path[1] + direccao*yacum,auxBoard)){
					x=path[0]+1;
					y=path[1] + direccao*yacum;
					putWall=true;
					System.out.println("DIREITA");
				}				
				if(putWall){
					//Node n=new Node(0,player.x, player.y,node,0,true);
					//Node n=new Node(0,path[0],(path[1] +1),node,0,true);
					Node n=new Node(0,x,y,node,0,true,auxBoard);
					n.parent=node;
					wallsNodes.add(n);					
				}
				yacum=yacum+1;
				ytotal=ytotal-yacum;
			}
		}
		return wallsNodes;
	}
	public void wall(Node node,Player p,Player i)
	{
		int x = 0,y = 0,direccao=0;
		int [] path;
		Boolean putWall=false;
		Player p1,p2;
		if(p.walls==0)
			return;
		if(i.dir==inimigo.dir)
		{
			path=Quoridor.getMinPath(p, i,node.board);
			direccao=inimigo.dir;
			p1=new Player(p.x,p.y,p.dir,p.walls,p.cor);
			p2=new Player(i.x,i.y,i.dir,i.walls,i.cor);
		}
		else
		{			
			path=Quoridor.getMinPath(i, p,node.board);
			direccao=player.dir;
			p2=new Player(p.x,p.y,p.dir,p.walls,p.cor);
			p1=new Player(i.x,i.y,i.dir,i.walls,i.cor);
		}	
		System.out.println("X:"+i.x+" Y"+i.y);
		System.out.println("X:"+path[0]+" Y"+path[1] );	
		String [][] auxBoard=Quoridor.cloneArray(node.board);
		if(Quoridor.poeParede(p1,p2,path[0]-1,path[1] + direccao,auxBoard)&&path[2]>0){			
			x=path[0]-1;
			y=path[1] + direccao;			
			putWall=true;
			System.out.println("ESQUERDA");
		}
		else if(Quoridor.poeParede(p1,p2,path[0],path[1] + direccao,auxBoard)&&path[2]>0){
			x=path[0];
			y=path[1] + direccao;
			putWall=true;
			System.out.println("MEIO");
		}	
		else if(Quoridor.poeParede(p1,p2,path[0]+1,path[1] + direccao,auxBoard)&&path[2]>0){
			x=path[0]+1;
			y=path[1] + direccao;
			putWall=true;
			System.out.println("DIREITA");
		}	
		else{
			//while()
			//TODO
			//gerar todos os nos de parede possiveis e comparar resultados
		}
		if(putWall){
			//Node n=new Node(0,player.x, player.y,node,0,true);
			//Node n=new Node(0,path[0],(path[1] +1),node,0,true);
			Node n=new Node(0,x,y,node,0,true,auxBoard);
			node.sucessores.add(n);
			n.parent=node;
		}	
<<<<<<< .mine
	}

	private ArrayList<Node> walls2(Node node, Player p, Player i,Node pai) {
		String [][] board = pai.board;
		ArrayList<Node>ret=new ArrayList<Node>();
		if(p.walls==0)
			return ret;
		ArrayList<int[]> path=Quoridor.getMinPathArray(i,p,board);
		path.add(new int[]{i.x,i.y});
		Collections.reverse(path);
		for (int j = 0; j < path.size()-1; j++) {
			Player p1 = new Player(p.x, p.y, p.dir,p.walls, null);
			String [][] b = Quoridor.cloneArray(board);
			int x, xNext;
			int y, yNext;
			x=path.get(j)[0];
			y=path.get(j)[1];
			xNext=path.get(j+1)[0];
			yNext=path.get(j+1)[1];
			int xParede,yParede;
			xParede=noMeio(x, xNext);
			yParede=noMeio(y, yNext);
			if(x==xParede){
				if(Quoridor.poeParede(p1, i, xParede, yParede, b)){
					ret.add(new Node(0, xParede, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);
				//p1 = new Player(p.x, p.y, p.dir,p.walls, null);
				p1.walls=p.walls;
				if(Quoridor.poeParede(p1, i, xParede-2, yParede, b)){
					ret.add(new Node(0, xParede-2, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);
				//p1 = new Player(p.x, p.y, p.dir,p.walls, null);
				p1.walls=p.walls;
				if(Quoridor.poeParede(p1, i, xParede-4, yParede, b)){
					ret.add(new Node(0, xParede-4, yParede,pai,0,true,b));
				}
			}else if(y==yParede){
				if(Quoridor.poeParede(p1, i, xParede, yParede, b)){
					ret.add(new Node(0, xParede, yParede,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);
				//p1 = new Player(p.x, p.y, p.dir,p.walls, null);
				p1.walls=p.walls;
				if(Quoridor.poeParede(p1, i, xParede, yParede-2, b)){
					ret.add(new Node(0, xParede, yParede-2,pai,0,true,b));
				}
				b = Quoridor.cloneArray(board);
				//p1 = new Player(p.x, p.y, p.dir,p.walls, null);
				p1.walls=p.walls;
				if(Quoridor.poeParede(p1, i, xParede, yParede-4, b)){
					ret.add(new Node(0, xParede, yParede-4,pai,0,true,b));
				}
			}
		}	
		return ret;
	}
	private int noMeio(int x1, int x2){
		if(x1>x2){
			return x2+1;
		}else if(x1<x2)
			return x1+1;
		else return x1;
	}
=======
	}*/
}
