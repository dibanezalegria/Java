package blackjack.daniel.ibanez;

/**
 * 	Enumeration representing different strategies. 
 * 
 * 	H (hit), S (stand), Dh (double if allowed, hit otherwise), 
 * 	Ds (double if allowed, stand otherwise) 
 *
 */
enum Strat {
	H, S, Dh, Ds
}

/**
 * 	Represents the player's betting and playing strategies. 
 * 
 */
public interface Strategy {
	public int bettingStrategy(int totalBalance);
	public Strat playingStrategy(int dealer, Hand playerHand);
}

/**
 *	Class that implements a basic betting and playing strategy.
 *
 */
class BasicStrategy implements Strategy {
	/**
	 * 	Bet is always 10.
	 */
	@Override
	public int bettingStrategy(int balance) {
		return 10;
	}

	/**
	 * 	Player stands when hand value is more than 16. 
	 */
	@Override
	public Strat playingStrategy(int dealer, Hand playerHand) {
		if (playerHand.getValue() > 16)
			return Strat.S;
		else 
			return Strat.H;
	}
}

/**
 * 	Class that implements a more complex betting and playing strategy.
 *
 */
class AdvancedStrategy implements Strategy {
	/**
	 * Bet is 30% of the player's balance.
	 */
	@Override
	public int bettingStrategy(int balance) {
		int amount = 10;
		// if balance is bigger than 60 then bet 30% of balance
		if (balance > 60) 
			amount = (balance / 30) * 10;

		return amount;
	}

	/**
	 * 	A more complex playing strategy depending on both, the dealer's card and
	 * 	the player's hand. 
	 */
	@Override
	public Strat playingStrategy(int dealer, Hand playerHand) {
		int player = playerHand.getValue();
		Strat strat = Strat.H;
		
		if (playerHand.isSoft()) {
			// hand contains an Ace
			if (player > 17)
				strat = Strat.S;
			
			if (player == 18 && (dealer == 9 || dealer == 10))
				strat = Strat.H;
			
			if ((player < 18 && (dealer > 3 && dealer < 7)) ||
				(player == 17 && dealer < 7))
				strat = Strat.Dh;
			
			if ((player == 18 && (dealer > 2 && dealer < 7)) ||
					(player == 19 && dealer == 6))
				strat = Strat.Ds;
		}
		else {
			// hand does not contain any Ace
			if ((player > 16) ||
					(player > 12 && dealer < 7) ||
					(player == 12 && (dealer > 3 && dealer < 7)))
				strat = Strat.S;
			
			if ((player == 8 && (dealer == 5 || dealer == 6)) ||
					(player == 9 && dealer < 7) ||
					(player == 10 && dealer < 10) ||
					(player == 11))
				
				strat = Strat.Dh;		
		}
	
		return strat;
	}

}


		