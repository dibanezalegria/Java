package blackjack.daniel.ibanez;

/**
 *	Represents a player in a blackjack game. Base class for PlayerAI.
 *
 */
public class Player {
	private Hand hand; 
	private String name;
	protected int balance;
	protected int bet;
	private Status status;
	private boolean doubleAllowed;
	
	/**
	 * @param name		player's name
	 * @param balance	money left
	 */
	public Player(String name, int balance) {
		hand = new Hand();
		this.name = name;
		this.balance = balance;
		status = Status.ALIVE;
		doubleAllowed = false;
	}

	/**
	 * @return	player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name	player's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return	money left
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * @param balance	total money
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	/**
	 * @return	bet amount 
	 */
	public int getBet() {
		return bet;
	}

	/**
	 * @param bet	bet amount
	 */
	public void setBet(int bet) {
		this.bet = bet;
	}
	
	/**
	 * @return	enumeration representing the status of the player
	 * 			DEAD, ALIVE, BUSTED, STAND, BLACKJACK
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * @param status	DEAD, ALIVE, BUSTED, STAND, BLACKJACK	
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Enables/disables possibility of double down.
	 * @param allow		true: enables, false: disables
	 */
	public void setDoubleAllowed(boolean allow) {
		doubleAllowed = allow;
	}
	
	/**
	 * Checks if player can double down. 
	 * @return	true if player can still double double, false otherwise
	 */
	public boolean doubleIsAllowed() {
		return doubleAllowed;
	}

	/**
	 * Adds a card to the player's hand.
	 * @param card		Card object to be added to the hand 
	 */
	public void addCard(Card card) {
		hand.addCard(card);
	}
	
	/**
	 * Returns the sum of all values in the player's hand.
	 * The value of each card follows the blackjack standard.
	 * @return	total value of the player's hand
	 */
	public int getHandValue() {
		return hand.getValue();
	}
	
	/**
	 * Returns a Hand object containing the cards the player is holding. 
	 * @return	Hand object 
	 */
	public Hand getHand() {
		return hand;
	}
	
	/**
	 * Removes all cards from the player's hand.
	 */
	public void clearHand() {
		hand.clear();
	}

	@Override
	public String toString() {
		return String.format("Player: %-10s Balance: %-6d Bet: %-4d Status: %-10s "
				+ "HandValue: %-3d Hand: %s", name, balance, bet, status, hand.getValue(), hand);
	}
	
}
