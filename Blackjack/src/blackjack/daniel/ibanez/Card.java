package blackjack.daniel.ibanez;

/**
 * Enumeration that represents the four colors of the standard French deck.
 *
 */
enum Suits {
	CLUBS, DIAMONDS, HEARTS, SPADES;
}

/**
 * Represents a card with value and suit from the standard French deck.
 *
 */
public class Card {
	private Suits suit;
	private int value;
	
	/**
	 * @param suit		color of the card is a constant from Suits enumeration	
	 * @param value		number between 1 and 13
	 * @throws IllegalArgumentException when value is less that 1 or more that 13
	 */
	public Card(Suits suit, int value) throws IllegalArgumentException {
		if (value < 1 || value > 13)
			throw new IllegalArgumentException();

		this.value = value;
		this.suit = suit;
	}

	/**
	 * Returns a constant from Suits enumeration that represents the color of the card.
	 * @return	color of the card
	 */
	public Suits getSuit() {
		return suit;
	}

	/**
	 * Returns card value, a number between 1 and 13.
	 * @return 	value of the card
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @return	11 for Ace,	
	 * 			10 for Jack, Queen and King.
	 * 			Face value for the rest 	   
	 */
	public int getBlackjackValue() {
		int bjValue = 0;
		if (value == 1)
			bjValue = 11;
		else if (value > 10)
			bjValue = 10;
		else
			bjValue = value;
		
		return bjValue;
	}
	
	@Override
	public String toString() {
		// Shows enumeration value in lower case with first letter capitalized
		String strSuit = suit.toString().substring(0, 1);
		strSuit += suit.toString().toLowerCase()
				.substring(1, suit.toString().length());

		return strSuit + value;	
	}

}
