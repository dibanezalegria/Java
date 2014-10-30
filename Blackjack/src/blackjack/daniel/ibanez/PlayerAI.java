package blackjack.daniel.ibanez;

/**
 * Represents a computer player in a blackjack game. Different players 
 * can implement different strategies.
 *
 */
public class PlayerAI extends Player{
	
	private Strategy strategy;
	
	/**
	 * @param name		player's name
	 * @param balance	money left
	 * @param strategy	playing and betting strategy
	 */
	public PlayerAI(String name, int balance, Strategy strategy) {
		super(name, balance);
		this.strategy = strategy;
	}

	/**
	 * @return	amount of money to bet
	 */
	public int getBettingStrategy() {
		return strategy.bettingStrategy(balance);
	}
	
	/**
	 * @param dealerCard	value of the card that the dealer is showing
	 * @param playerHand	Hand object contain the cards that the player is holding
	 * @return				enumeration representing player's next move: 
	 * 						H (hit), S(stand), Dh(double or hit), Ds(double or stand)
	 */
	public Strat getPlayingStrategy(int dealerCard, Hand playerHand) {
		return strategy.playingStrategy(dealerCard, playerHand);
	}

}
