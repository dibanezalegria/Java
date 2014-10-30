package blackjack.daniel.ibanez;

/**
 *	Represents the dealer in a blackjack game.
 *
 */
public class Dealer {
	private Hand hand;
	private Status status;
	
	public Dealer() {
		hand = new Hand();
	}
	
	/**
	 * @return	dealer's status. Can be BUSTED, STAND or BLACKJACK
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * @param status	status can be BUSTED, STAND or BLACKJACK
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Adds a card to the dealer's hand.
	 * @param card	new Card object to be added
	 */
	public void addCard(Card card) {
		hand.addCard(card);
	}
	
	/**
	 * Returns the sum of all values in the dealer's hand.
	 * The value of each card follows the blackjack standard.
	 * @param allAreVisible 	true: counts all cards,
	 * 							false: counts only visible card
	 * @return					total value
	 */
	public int getHandValue(boolean allAreVisible) {
		if (!allAreVisible) {
			Card card = hand.getCardAt(1);
			return card.getBlackjackValue();
		}
		return hand.getValue();
	}
	
	/**
	 * 	Removes all cards from the dealer's hand.
	 */
	public void clearHand() {
		hand.clear();
	}

	@Override
	public String toString() {
		return String.format("%s HandValue: %-3d", hand.toString(), hand.getValue());
	}
}
