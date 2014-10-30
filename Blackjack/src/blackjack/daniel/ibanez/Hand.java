package blackjack.daniel.ibanez;

import java.util.ArrayList;

/**
 *	Represents the cards that dealer and players are holding. 
 *
 */
public class Hand {
	private ArrayList<Card> cardList;
	
	public Hand() {
		cardList = new ArrayList<Card>();
	}
	
	/**
	 * Adds a card to the hand.
	 * @param card	Card object to be added to the hand. 
	 */
	public void addCard(Card card) {
		cardList.add(card);
	}
	
	/**
	 * Returns a card from the given position on the list.
	 * @param index		position
	 * @return			Card object at given position or null if index bigger than list
	 * 				 
	 */
	public Card getCardAt(int index) {
		if (index < cardList.size())
			return cardList.get(index);
		else
			return null;
	}
	
	/**
	 * Calculates the sum of values of all cards in the hand. The values follow
	 * the standard blackjack rules. 
	 * @return		sum of values of all cards in the hand
	 */
	public int getValue() {
		// calculate sum and count aces
		int sum = 0;
		int nAces = 0;
		for (Card card : cardList) {
			sum += card.getBlackjackValue();
			if (card.getValue() == 1)
				nAces++;
		}
		// count each ace as 1, while total is bigger than 21
		while (nAces > 0 && sum > 21) {
			nAces--;
			sum = sum - 10; 
		}
		return sum;
	}
	
	/**
	 * Number of cards in the hand.
	 * @return		number of cards
	 */
	public int getSize() {
		return cardList.size();
	}

	/**
	 * Empties hand.
	 */
	public void clear() {
		cardList.clear();
	}
	
	/**
	 * Check whether there is an Ace among the cards in the hand.
	 * @return 	true if hand contains an Ace,
	 * 			false otherwise
	 */
	public boolean isSoft() {
		for (Card card : cardList)
			if (card.getValue() == 1)
				return true;
		return false;
	}

	@Override
	public String toString() {
		return "Hand [cardList=" + cardList + "]";
	}
	
}
