package quoridor.gui;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Gui extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	public Gui() {
		this.setTitle("Quoridor");	
		this.setResizable(false);
		this.setMinimumSize(new Dimension(600, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 700);
		JLabel labelWalls_p1 = new JLabel("10");
		labelWalls_p1.setForeground(Color.BLUE);
		
		JLabel labelWalls_p2 = new JLabel("10");
		labelWalls_p2.setForeground(Color.RED);
		JLabel lblPlayer = new JLabel("Player1");
		lblPlayer.setForeground(Color.blue);
		final GamePanel gamePanel = new GamePanel(labelWalls_p1,labelWalls_p2,lblPlayer);
		
		JPanel menuPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(111)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(menuPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(gamePanel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 535, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(138, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(gamePanel, GroupLayout.PREFERRED_SIZE, 533, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
		);
		String [] choices = {"Human","alfa-beta pruning"};
		final JComboBox<?> player1Choice = new JComboBox<Object>(choices);
		final JComboBox<?> player2Choice = new JComboBox<Object>(choices);
		JButton btnNewGame = new JButton("New Game");
		final JSpinner player1heuristica = new JSpinner();
		player1heuristica.setModel(new SpinnerNumberModel(0, 0, 3, 1));
		final JSpinner player2heuristica = new JSpinner();
		player2heuristica.setModel(new SpinnerNumberModel(0, 0, 3, 1));
		final JSpinner player1Profundidade = new JSpinner();
		player1Profundidade.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		final JSpinner player2Profundidade = new JSpinner();
		player2Profundidade.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		btnNewGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				((GamePanel)gamePanel).resetGame(player1Choice.getSelectedIndex(),player2Choice.getSelectedIndex(),(Integer)player1heuristica.getValue(),(Integer)player2heuristica.getValue(),(Integer)player1Profundidade.getValue(),(Integer)player2Profundidade.getValue());
				repaint();
			} 
		});
		
		JLabel lblPlayer_1 = new JLabel("Player1");
		lblPlayer_1.setForeground(Color.BLUE);
		
		JLabel lblPlayer_2 = new JLabel("Player2");
		lblPlayer_2.setForeground(Color.RED);

		JLabel lblWalls_p1 = new JLabel("Walls :");
		
		JLabel lblWalls_p2 = new JLabel("Walls :");
		
		JLabel lblPlayerTurn = new JLabel("Player Turn:");
		
		final JCheckBox showP1MinPath = new JCheckBox("Show Min Path");		
		showP1MinPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				((GamePanel)gamePanel).p1MinPath=showP1MinPath.isSelected();
				repaint();
			}
		});

		final JCheckBox showP2MinPath = new JCheckBox("Show Min Path ");	
		showP2MinPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				((GamePanel)gamePanel).p2MinPath=showP2MinPath.isSelected();
				repaint();
			}
		});
		
		JLabel lblHeuristicas = new JLabel("Heuristic");
		
		JLabel lblProfundidade = new JLabel("Depth");
		
		JButton btnTest = new JButton("Results");
		btnTest.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				new Results(gamePanel,player1heuristica,player2heuristica,player1Profundidade,player2Profundidade);
				player1Choice.setSelectedIndex(1);
				player2Choice.setSelectedIndex(1);
			}
		});
			
		GroupLayout gl_menuPanel = new GroupLayout(menuPanel);
		gl_menuPanel.setHorizontalGroup(
			gl_menuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_menuPanel.createSequentialGroup()
					.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewGame, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_menuPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPlayer_1, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPlayer_2)
								.addComponent(lblPlayerTurn))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPlayer)
								.addGroup(gl_menuPanel.createSequentialGroup()
									.addComponent(player2Choice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblWalls_p2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(labelWalls_p2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(showP2MinPath))
								.addGroup(gl_menuPanel.createSequentialGroup()
									.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_menuPanel.createSequentialGroup()
											.addComponent(player1Choice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(lblWalls_p1)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(labelWalls_p1)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(showP1MinPath))
										.addGroup(gl_menuPanel.createSequentialGroup()
											.addGap(65)
											.addComponent(btnTest)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblHeuristicas)
										.addGroup(gl_menuPanel.createParallelGroup(Alignment.TRAILING)
											.addComponent(player2heuristica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(player1heuristica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblProfundidade)
						.addComponent(player1Profundidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(player2Profundidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(81, Short.MAX_VALUE))
		);
		gl_menuPanel.setVerticalGroup(
			gl_menuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_menuPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_menuPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_menuPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblHeuristicas)
							.addComponent(lblProfundidade))
						.addGroup(gl_menuPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNewGame)
							.addComponent(btnTest)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_menuPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayer_1)
						.addComponent(player1Choice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblWalls_p1)
						.addComponent(labelWalls_p1)
						.addComponent(showP1MinPath)
						.addComponent(player1heuristica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(player1Profundidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_menuPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayer_2)
						.addComponent(player2Choice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblWalls_p2)
						.addComponent(labelWalls_p2)
						.addComponent(showP2MinPath)
						.addComponent(player2heuristica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(player2Profundidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_menuPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayerTurn)
						.addComponent(lblPlayer))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		menuPanel.setLayout(gl_menuPanel);
		getContentPane().setLayout(groupLayout);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui gui = new Gui();
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
