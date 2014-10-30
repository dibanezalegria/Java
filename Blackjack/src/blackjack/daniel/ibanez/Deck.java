package blackjack.daniel.ibanez;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class representing the standard French deck of 52 cards.
 * 
 */
public class Deck {

	private ArrayList<Card> deck;	
	
	public Deck() {
		deck = new ArrayList<Card>(52);	// Standard 52-card deck
		for (Suits suit : Suits.values())
			for (int value = 1; value < 14; ++value) 
				deck.add(new Card(suit, value));	
	}

	/**
	 * Returns the first card and removes it from the deck.
	 * @return	Card reference to the first card in the list
	 */
	public Card getNextCard() {
		Card card = null;
		if (deck.size() > 0) {
			card = deck.get(0);
			deck.remove(0);
		}
		return card;
	}
	
	/**
	 * Shuffles the cards in the deck.
	 */
	public void shuffle() {
		Collections.shuffle(deck);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Card card : deck)
			sb.append(card.toString() + "\n");
		
		return sb.toString();
	}
	
}
