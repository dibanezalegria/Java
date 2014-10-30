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
 * This class is responsible for the graphical representation of the dealer's cards,
 * hand count value and name. It contains one inner class that controls the drawing of
 * the cards.
 *
 */
class DealerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private CardPanel cardPanel;
	private JLabel count;
	private JLabel name;
	private boolean cardHidden;	

	public DealerPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(new Color(50, 160, 100)); // green
		count = new JLabel("Count");
		count.setFont(new Font("Arial", Font.PLAIN, 20));
		add(count);
		cardPanel = new CardPanel();
		add(cardPanel);
		name = new JLabel("Dealer");
		name.setFont(new Font("Arial", Font.PLAIN, 20));
		add(name); 
		cardHidden = true; // one card is hidden
	}
	
	/**
	 * Changes name label for dealer. 
	 * @param name
	 */
	public void setDealerName(String name) {
		this.name.setText(name);
	}
	
	/**
	 * Changes count label for dealer. 
	 * @param count		value of all the cards in the hand
	 */
	public void setCount(int count) {
		if (cardHidden)
			this.count.setText(count + "+");
		else 
			this.count.setText(count + "");
	}

	/**
	 * Adds a card image to the list of cards images that represents
	 * the dealer's hand.
	 * @param cardName		name of the png file containing the graphical
	 * 						representation of the card
	 */
	public void addCard(String cardName) {
		cardPanel.addCard(cardName);
	}

	/**
	 * Removes all card images from the list representing the dealer's hand
	 */
	public void removeCards() {
		cardPanel.clearCardPanel();
	}

	/**
	 * The dealer shows only one card before the players have finished playing
	 * their hands. After that the hidden card becomes visible. 
	 * @param hidden	true if the hidden card remains hidden
	 * 					false if the hidden card becomes visible
	 */
	public void setCardHidden(boolean hidden) {
		cardHidden = hidden;
	}
	
	/**
	 * Changes the color of the dealer's label when he is playing. 
	 */
	public void activate() {
		name.setForeground(Color.YELLOW);
	}
	
	/**
	 * Changes the color of the dealer's label when players are playing.
	 */
	public void inactivate() {
		name.setForeground(Color.BLACK);
	}

	/**
	 * Inner class that draws the card images representing the dealer's hand.
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
		 * the dealer's hand.
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
				if (cardHidden && i == 0) {
					BufferedImage img = null;
					try {
						img = ImageIO.read(getClass().getResource("/Back.png"));
					} catch (IOException ex) {
						System.err.println("Error: Unable to load image: Back.png");
						System.exit(1);
					}
					g.drawImage(img, 0, 0, this);
				}
				else {
					int x = overlap * i;
					g.drawImage(cardImages.get(i), x, 0, this);
				}
			}
		}
		
		/**
		 * Removes card images from list representing the dealer's hand
		 */
		public void clearCardPanel() {
			cardImages.clear();
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(400, 200);
		}

	}
}