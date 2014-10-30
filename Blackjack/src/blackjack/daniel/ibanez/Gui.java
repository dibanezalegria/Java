package blackjack.daniel.ibanez;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *	Graphical user interface representing a game of blackjack.  
 *
 */
public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	private GameLogic game;
	private PlayerPanel[] playerPanels;
	private DealerPanel dealerPanel;
	private JButton hitButton, standButton, dealButton, exitButton, doubleButton;
	private JButton betDownButton, betUpButton;
	private JTextField betText, balanceAmount;
	
	/**
	 * Draws all the components in the screen.
	 * @param title		frame title
	 * @param game		reference to a Game object that runs the logic of the progam 
	 */
	public Gui(String title, GameLogic game) {
		super(title);
		this.game = game; 
		try {
			setIconImage(ImageIO.read(getClass().getResource("/Icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		getContentPane().setBackground(new Color(50, 160, 100)); // green
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1300, 800);
		setResizable(false);
		setLocationRelativeTo(null);
		
		// ==================== Center panel
		Box centerPanel = Box.createVerticalBox();
		// empty space
		centerPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		// Dealer panel
		dealerPanel = new DealerPanel();
		Box container = Box.createHorizontalBox();
		container.setOpaque(true);
		container.setBackground(new Color(50, 160, 100)); // green
		container.add(Box.createHorizontalGlue());
		container.add(dealerPanel);
		centerPanel.add(container);
		// deal button
		Box dealPanel = Box.createHorizontalBox();
		dealButton = new JButton("Deal");
		dealButton.setFont(new Font("Arial", Font.PLAIN, 30));
		dealPanel.add(dealButton);
		exitButton = new JButton("Exit");
		dealPanel.add(Box.createRigidArea(new Dimension(10, 80)));
		exitButton.setFont(new Font("Arial", Font.PLAIN, 30));
		dealPanel.add(exitButton);
		centerPanel.add(dealPanel);
		// Players panel (user plays on the right side and has panel with index 0)
		JPanel playersContainer = new JPanel();
		playersContainer.setBackground(new Color(50, 160, 100)); // green
		playerPanels = new PlayerPanel[3];
		for (int i = playerPanels.length - 1; i > - 1; --i) {	
			playerPanels[i] = new PlayerPanel();
			playerPanels[i].setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
			playersContainer.add(playerPanels[i]);
		}
		centerPanel.add(playersContainer);
		// table empty 
		showEmptyTable();
		// empty space
		centerPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		add(centerPanel, BorderLayout.CENTER);
		
		// ==================== Bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(150, 180, 190)); // blue
		// empty space 
		bottomPanel.add(Box.createRigidArea(new Dimension(500, 0)));
		// double button
		doubleButton = new JButton("Double");
		doubleButton.setFont(new Font("Arial", Font.PLAIN, 30));
		bottomPanel.add(doubleButton);
		// hit button
		hitButton = new JButton("Hit  ");
		hitButton.setFont(new Font("Arial", Font.PLAIN, 30));
		bottomPanel.add(hitButton);
		// stand button
		standButton = new JButton("Stand");
		standButton.setFont(new Font("Arial", Font.PLAIN, 30));
		bottomPanel.add(standButton);
		// empty space 
		bottomPanel.add(Box.createRigidArea(new Dimension(60, 0)));
		// betting buttons
		Box betPanel = Box.createHorizontalBox();
		JLabel betLabel = new JLabel("Bet: ");
		betLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		betPanel.add(betLabel);
		betText = new JTextField();
		betText.setColumns(3);
		betText.setEditable(false);
		betText.setBackground(new Color(150, 180, 190));
		betText.setFont(new Font("Arial", Font.PLAIN, 30));
		betText.setHorizontalAlignment(JTextField.RIGHT);
		betPanel.add(betText);
		betDownButton = new JButton("-");
		betDownButton.setFont(new Font("Arial", Font.PLAIN, 30));
		betPanel.add(betDownButton);
		betUpButton = new JButton("+");
		betUpButton.setFont(new Font("Arial", Font.PLAIN, 30));
		betPanel.add(betUpButton);
		bottomPanel.add(betPanel);
		// empty space 
		bottomPanel.add(Box.createRigidArea(new Dimension(60, 0)));
		// balance
		JLabel balanceLabel = new JLabel(" Balance: $");
		balanceLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		bottomPanel.add(balanceLabel);
		balanceAmount = new JTextField();
		balanceAmount.setEditable(false);
		balanceAmount.setColumns(5);
		balanceAmount.setBackground(new Color(150, 180, 190));
		balanceAmount.setFont(new Font("Arial", Font.PLAIN, 30));
		bottomPanel.add(balanceAmount);
		
		add(bottomPanel, BorderLayout.PAGE_END);
		pack();
		setupButtonListeners();
		// enable/disable buttons 
		buttonSwitcher(true, false, true, true, true); // bet, deal, hit, stand, double
		setVisible(true);
	}
	
	/**
	 * Listeners for all buttons (HIT, STAND, DEAL, DOUBLE, EXIT, BETup and BETdown).
	 */
	private void setupButtonListeners() {
		hitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.hitButtonClicked();
			}		
		});
		
		standButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				game.standButtonClicked();
			}		
		});
		
		dealButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				game.dealButtonClicked();
			}		
		});
		
		betUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				game.betUpButtonClicked();
			}		
		});
		
		betDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				game.betDownButtonClicked();
			}		
		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				game.exitButtonClicked();
			}		
		});
		
		doubleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				game.doubleButtonClicked();
			}		
		});
	}
	
	/**
	 * Input dialog at the start of the game where the user can enter his/her name.
	 * @return	the string entered by the user
	 */
	public String inputNameDialog() {
		JLabel label = new JLabel("Enter your name: ");
		label.setFont(new Font("Arial", Font.PLAIN, 20));
		String str = JOptionPane.showInputDialog(this, label, "New Player", JOptionPane.QUESTION_MESSAGE);
		if (str == null || str.equals(""))
			return "Player";
	
		return str;
	}
	
	/**
	 * Shows a message dialog. 
	 * @param msg	string to be display
	 */
	public void showMessage(String msg) {
		JLabel label = new JLabel(msg);
		label.setFont(new Font("Arial", Font.PLAIN, 20));
		JOptionPane.showMessageDialog(this, label, "",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Updates player's name label for a given player.
	 * @param nplayer	player index
	 * @param name		string 
	 */
	public void updatePlayerName(int nplayer, String name) {
		playerPanels[nplayer].setPlayerName(name);
	}
	
	/**
	 * Updates player's count label for a given player. 
	 * @param nplayer	player index
	 * @param count		value of cards in player's hand
	 */
	public void updatePlayerCount(int nplayer, int count) {
		playerPanels[nplayer].setCount(count);
	}
	
	/**
	 * Updates player's bet label for a given player.
	 * @param nplayer	player index
	 * @param bet		amount of money to bet
	 */
	public void updatePlayerBet(int nplayer, int bet) {
		playerPanels[nplayer].setBet(bet);
		// updates player score board
		if (nplayer == 0)
			betText.setText(bet + "");
	}
	
	/**
	 * Updates player's balance label for a given player. 
	 * @param nplayer	player index
	 * @param balance	amount of money left
	 */
	public void updatePlayerBalance(int nplayer, int balance) {
		playerPanels[nplayer].setBalance(balance);
		// updates player score board
		if (nplayer == 0)
			balanceAmount.setText(balance + "");
	}
	
	/**
	 * Updates dealer's count label. 
	 * @param count		dealer's hand value
	 */
	public void updateDealerCount(int count) {
		dealerPanel.setCount(count);
	}
	
	/**
	 * Updates dealer's name label. 
	 * @param name		string representing dealer's name
	 */
	public void updateDealerName(String name) {
		dealerPanel.setDealerName(name);
	}

	/**
	 * Informs dealerPanel object whether the dealer's hidden card  
	 * should be visible or on its back.
	 * @param hide		true - hidden card is on its back
	 * 					false - hidden card is not hidden any longer
	 */					
	public void setDealerHidesCard(boolean hide) {
		dealerPanel.setCardHidden(hide);
		revalidate();
		repaint();
	}
	
	/**
	 * Activates (changes color) label for active player. 
	 * @param nplayer	player index
	 */	
	public void activatePlayer(int nplayer) {
		playerPanels[0].inactivate();
		playerPanels[1].inactivate();
		playerPanels[2].inactivate();
		playerPanels[nplayer].activate();
		dealerPanel.inactivate();
	}
	
	/**
	 * Activates (changes color) dealer's label when it is his turn to play.
	 */
	public void activateDealer() {
		playerPanels[0].inactivate();
		playerPanels[1].inactivate();
		playerPanels[2].inactivate();
		dealerPanel.activate();
	}
		
	/**
	 * Adds an image to the panel representing the cards.
	 * @param panelNum		human (0), AI players (1-2) 
	 * @param cardName		name of the file containing the graphical 
	 * 						representation of the card
	 */
	public void addCardToPanel(int panelNum, String cardName) {
		playerPanels[panelNum].addCard(cardName);
		revalidate();
		repaint();
	}
	
	/**
	 * Adds an image to the panel representing the cards.
	 * @param cardName		name of the file containing the graphical 
	 * 						representation of the card
	 */
	public void addCardToDealer(String cardName) {
		dealerPanel.addCard(cardName);
		revalidate();
		repaint();
	}
	
	/**
	 * Removes images from the list of images representing the cards.
	 * in players hands and dealer.
	 */
	public void clearAllHands() {
		for (PlayerPanel ppanel : playerPanels) 
			ppanel.removeCards();
	
		dealerPanel.removeCards();
		revalidate();
		repaint();
	}

	/**
	 * Add images to the panels representing an empty table. These images 
	 * represent the placeholder for the cards (empty silhouette).
	 */
	public void showEmptyTable() {
		for (PlayerPanel ppanel : playerPanels) 
			ppanel.addCard("Empty");
		
		dealerPanel.addCard("Empty");		
	}

	/**
	 * Enables and disables the buttons in the screen.
	 * @param bet		bet buttons (up and down)
	 * @param deal		deal button
	 * @param hit		hit button
	 * @param stand		stand button
	 * @param ddown		double button
	 */
	public void buttonSwitcher(boolean bet, boolean deal, boolean hit, boolean stand, boolean ddown) {
		betUpButton.setEnabled(bet);
		betDownButton.setEnabled(bet);
		dealButton.setVisible(deal);
		exitButton.setVisible(deal); // exitButton is visible when dealButton is
		hitButton.setEnabled(hit);
		standButton.setEnabled(stand);
		doubleButton.setEnabled(ddown);
	}
}
