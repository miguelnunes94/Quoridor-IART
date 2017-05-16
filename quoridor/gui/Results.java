package quoridor.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;
import javax.swing.LayoutStyle.ComponentPlacement;


public class Results extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable tableWins;
	public GamePanel gamePanel;
	private JTable tableTempos;
	private JTable tableNodes;

	public Results(final GamePanel gamePanel, final JSpinner player1heuristica, final JSpinner player2heuristica,final  JSpinner player1Profundidade, final JSpinner player2Profundidade){
		this.gamePanel=gamePanel;
		setTitle("Results");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 600));
		pack();

		tableWins = new JTable();
		tableWins.setFillsViewportHeight(true);
		tableWins.setModel(new DefaultTableModel(
				new Object[][] {
						{"Heur\u00EDstica por ", "jogador", "Vencedor", "por", "profundidade", null},
						{"p1", "p2", "2", "3", "4", "5"},
				},
				new String[] {
						"Heur\u00EDstica por ", "jogador", "New column", "New column", "New column", "New column"
				}
				));
		tableWins.getColumnModel().getColumn(0).setPreferredWidth(160);
		tableWins.getColumnModel().getColumn(1).setPreferredWidth(130);

		tableTempos = new JTable();
		tableTempos.setModel(new DefaultTableModel(
				new Object[][] {
						{"Tempo da primeira jogada", null, null, "Profundidade", null},
						{"por heur\u00EDstica (media de 3 jogos)", "2", "3", "4", "5"},
				},
				new String[] {
						"New column", "New column", "New column", "New column", "New column"
				}
				) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableTempos.getColumnModel().getColumn(0).setPreferredWidth(223);

		tableNodes = new JTable();
		tableNodes.setModel(new DefaultTableModel(
				new Object[][] {
						{"Num n\u00F3s ", "", "Profundidade", "", ""},
						{"da primeira jogada", "2", "3", "4", "5"},
				},
				new String[] {
						"New column", "New column", "New column", "New column", "New column"
				}
				) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});


		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tableWins, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(tableTempos, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tableNodes, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(tableWins, GroupLayout.PREFERRED_SIZE, 439, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(tableTempos, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(tableNodes, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)))
				);
		getContentPane().setLayout(groupLayout);
		repaint();
		Thread t = new Thread(){
			@Override
			public void run(){
				ArrayList<ArrayList<Long>> tempos = new ArrayList<ArrayList<Long>>();
				for(int i=0;i<6-2;i++){
					tempos.add(new ArrayList<Long>());
				}
				GamePanel.delay=0;
				int linha = 1, col = 2;
				int lin_tempos=2 , col_tempos=1;
				for(int p1_h=0;p1_h<4;p1_h++){
					((DefaultTableModel) tableNodes.getModel()).addRow(new Object[]{"h"+p1_h, "", "", "", ""});
					player1heuristica.setValue(p1_h);
					for(int p2_h=0;p2_h<4;p2_h++){
						player2heuristica.setValue(p2_h);
						for(int profundidade=2;profundidade<6;profundidade++){
							if(p1_h==p2_h)
								continue;
							if(profundidade==2){
								((DefaultTableModel) tableWins.getModel()).addRow(new Object[]{"h"+p1_h, "h"+p2_h, "", "", "", ""});
								linha++;
							}

							player1Profundidade.setValue(profundidade);
							player2Profundidade.setValue(profundidade);
							gamePanel.hWinner=-1;
							gamePanel.resetGame(1, 1, p1_h, p2_h, profundidade, profundidade);
							while(gamePanel.hWinner==-1){
								try {
									sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							tableNodes.setValueAt(GamePanel.nodesPrimeiraJogada, p1_h+2, profundidade-1);

							tableWins.setValueAt("h"+gamePanel.hWinner, linha, col);
							repaint();
							col++;
							tempos.get(profundidade-2).add(GamePanel.tempoPrimeiraJogada);
						}
						col=2;
					}
					((DefaultTableModel) tableTempos.getModel()).addRow(new Object[]{"h"+p1_h, "", "", "", ""});
					for(ArrayList<Long> t : tempos){
						long acc=0;
						for(Long l : t){
							acc+=l;
						}
						acc/=t.size();
						tableTempos.setValueAt(""+acc, lin_tempos, col_tempos);
						col_tempos++;
						t.clear();
					}
					lin_tempos++;
					col_tempos=1;
				}
				GamePanel.delay=500;
			}
		};
		t.start();
	}
}
