package quoridor.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Astar {
	private static class MyComparator implements Comparator<Node>
	{
		public int compare( Node x, Node y )
		{
			return x.f - y.f;
		}
	}
	private String [][] board;	
	public Astar(String [][] b){
		board=b;
	}
	/**
	 * Calcula o caminho minimo at� � vitoria
	 * @param x coord x inicical
	 * @param y coord y inicical
	 * @param dir dire��o em que se deve mover para ganhar (y)
	 * @return retorna um array com 3 inteiros com o seguinte formato 
	 			int[0] = coordenada x do 1� movimento
	 			int[1] = coordenada y do 1� movimento
	 			int[2] = distancia do caminho
	 */
	public int [] aStart(Player player, Player inimigo){
		ArrayList<int []> ret= aStartArray(player, inimigo);
		if(ret==null){
			return null;
		}else return ret.get(ret.size()-1);
	}
	public ArrayList<int []> aStartArray(Player player, Player inimigo){
		PriorityQueue<Node> lAberta = new PriorityQueue<Node>(new MyComparator());
		Node inicial = new Node(0,player.x,player.y,null,Quoridor.fEvalAstar(player.y,player.dir,0),false,null);
		lAberta.add(inicial);
		ArrayList<Node>lFechada = new ArrayList<Node>();

		while(!lAberta.isEmpty()){
			Node melhor = lAberta.poll();// tira o melhor
			if((player.dir==-1 && melhor.y==0) || (player.dir==1 && melhor.y==board.length-1))
				return melhorMoveArray(melhor);
			lFechada.add(melhor);
			ArrayList<Node> sucessores = Quoridor.getMoves(player,inimigo,melhor,null, board);
			melhor.sucessores=sucessores;

			for(Node filho : sucessores){
				int newG = melhor.g+1;
				Node existente = getNode(filho, lAberta);
				if(existente!=null){
					if(newG < existente.g){
						existente.parent=melhor;
						existente.f-=(existente.g-newG);
						existente.g=newG;
					}
				}else {
					existente = getNode(filho, lFechada);
					if(existente!=null){
						if(newG<existente.g){
							existente.parent=melhor;
							existente.f-=(existente.g-newG);
							existente.g=newG;
							propagar(existente.g-newG,existente, lAberta);
						}
					}else { // nao pertence a nenhuma lista
						lAberta.add(filho);
					}
				}
			}
		}
		return null;
	}

	private void propagar(int diff, Node node, PriorityQueue<Node> lAberta) {
		for(Node filho: node.sucessores){
			filho.g-=diff;
			filho.f-=diff;
			if(!lAberta.contains(filho)){
				propagar(diff, filho, lAberta);
			}
		}
	}

	/**
	 * retorna um n� com as coordenadas dadas
	 */
	private Node getNode(Node nodeToFind, Collection<Node> lAbtera) {
		for (Iterator<Node> iterator = lAbtera.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			if(node.x==nodeToFind.x && node.y==nodeToFind.y)
				return node;
		}
		return null;
	}

	private ArrayList<int[]> melhorMoveArray(Node melhor) {
		ArrayList<int []> moves = new ArrayList<int[]>();
		int dist = melhor.g;
		do{
			int move[]=new int[]{melhor.x,melhor.y,dist};
			melhor=melhor.parent;
			moves.add(move);
			if(melhor==null){
				break;
			}
		}while(melhor.parent!=null);
		return moves;
	}
}
