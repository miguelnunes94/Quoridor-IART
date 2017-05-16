package quoridor.logic;
import java.awt.Color;

public class Player {
		public int x;
		public int y;
		public int walls;
		public int dir;// incremento no Y que até ao lado em que ganha
		public Color cor;
		public Player(int x, int y, int dir,int walls, Color cor){
			this.x=x;
			this.y=y;
			this.dir=dir;
			this.cor=cor;
			this.walls=walls;
		}
		// converte coordenadas x,y em coordenadas de quadriculas no ecra
		public int coordToScreen(String coord,int quadriculaLargura, int paredeLargura){
			int tmp=0;
			if(coord.equals("x"))
				tmp=x/2;
			else tmp=y/2;
			return tmp*quadriculaLargura+tmp*paredeLargura;

		}
	}