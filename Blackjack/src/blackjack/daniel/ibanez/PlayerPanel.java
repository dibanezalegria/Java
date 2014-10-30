package blackjack.daniel.ibanez;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is responsible for the graphical representation of cards, hand count value,
 * name, balance and money for a player. It contains one inner class that controls 
 * the drawing of the cards.
 *
 */
class PlayerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private CardPanel cardPanel;	
	private JLabel count;
	private JLabel name;
	private JLabel bet;
	private JLabel balance;

	public PlayerPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(new Color(50, 160, 100)); // green
		count = new JLabel("Count");
		count.setFont(new Font("Arial", Font.PLAIN, 20));
		add(count);
		cardPanel = new CardPanel();
		add(cardPanel);
		name = new JLabel("Player");
		name.setFont(new Font("Arial", Font.PLAIN, 20));
		add(name); 
		bet = new JLabel("Bet: ");
		bet.setFont(new Font("Arial", Font.PLAIN, 20));
		add(bet);
		balance = new JLabel("Balance: ");
		balance.setFont(new Font("Arial", Font.PLAIN, 20));
		add(balance);
	}
	
	/**
	 * Sets player's name label. 
	 * @param name		player's name
	 */
	public void setPlayerName(String name) {
		this.name.setText(name);
	}
	
	/**
	 * Sets player's count label. 
	 * @param count		sum of all cards values in player's hand
	 */
	public void setCount(int count) {
		this.count.setText(count + "");
	}

	/**
	 * Sets player's bet label. 
	 * @param bet	amount of money to bet
	 */
	public void setBet(int bet) {
		this.bet.setText("Bet: " + bet);
	}

	/**
	 * Sets player's balance label.
	 * @param balance	amount of money
	 */
	public void setBalance(int balance) {
		this.balance.setText("Balance: " + balance);
	}

	/**
	 * Adds a card image to the list of cards images that represent the player's hand.
	 * @param cardName		name of the png file containing the graphical
	 * 						representation of the card
	 */
	public void addCard(String cardName) {
		cardPanel.addCard(cardName);
	}

	/**
	 * Removes all card images from the list representing the player's hand.
	 */
	public void removeCards() {
		cardPanel.clearCardPanel();
	}
	
	/**
	 * Changes the color of the player's label when he is playing.
	 */
	public void activate() {
		name.setForeground(Color.YELLOW);
	}
	
	/**
	 * Changes the color of the player's label when other players or dealer are playing.
	 */
	public void inactivate() {
		name.setForeground(Color.BLACK);
	}

	/**
	 * Inner class that draws the card images representing the player's hand.
	 *
	 *
	 */
	class CardPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ArrayList<BufferedImage> cardImages;
		
		public CardPanel() {
			cardImages = new ArrayList<BufferedImage>();
			setBackground(new Color(50, 160, 100)); // green
		}

		/**
		 * Adds a card image to the list of cards images that represent
		 * the player's hand.
		 * @param cardName		name of the png file containing the graphical
		 * 						representation of the card
		 */
		public void addCard(String cardName) {
			BufferedImage img = null;
			try {
				img = ImageIO.read(getClass().getResource("/" + cardName + ".png"));
			} catch (IOException ex) {
				System.err.println("Error: Unable to load image: " +  cardName);
				System.exit(1);
			}
			cardImages.add(img);
		}
		
		/**
		 * Draws overlapped cards
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int overlap = 0;
			if (cardImages.size() > 0)
				overlap = cardImages.get(0).getWidth() / 6;
		
			for (int i = 0; i < cardImages.size(); i++) {
				int x = overlap * i;
				g.drawImage(cardImages.get(i), x, 0, this);
			}
		}
		
		/**
		 * Removes card images from list representing the player's hand
		 */
		public void clearCardPanel() {
			cardImages.clear();
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(400, 200);
		}

	}
}
