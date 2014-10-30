package blackjack.daniel.ibanez;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

/**
 * Enumeration representing the outcome of a round. The winner of the hand.
 * 
 */
enum Winner {
	PUSH, DEALER, PLAYER
}

/**
 *	The GameLogic class runs the logic of the blackjack game.
 */
public class GameLogic {
	private Gui gui;
	private Deck deck;
	private Player[] players;
	private Dealer dealer;
	private boolean onlyAI;
	private static final int SLEEP = 500;	// slow down animation (ms)
	
	/**
	 * Initializes dealer and players, and waits for the player to click the DEAL/STAND/BET buttons OR
	 * calls 'newHand' method when no human is in the game (this happens when the user enters 'ai' as 
	 * name in the input dialog).
	 */
	public GameLogic() {
		players = new Player[3];
		gui = new Gui("Blackjack", this);
		String playerName = gui.inputNameDialog();
		if (playerName.trim().toLowerCase().equals("ai")) {
			players[0] = new PlayerAI(playerName, 50, new BasicStrategy());
			onlyAI = true;
		}
		else {
			players[0] = new Player(playerName, 50);
			players[0].setBet(10);	// minimum bet is 10
			players[0].setBalance(players[0].getBalance() - 10);
			onlyAI = false;
		}
			
		players[1] = new PlayerAI("Hal", 50, new AdvancedStrategy());
		players[2] = new PlayerAI("Bishop", 50, new AdvancedStrategy());
		dealer = new Dealer();
		
		// file that will contain every hand played
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Results.txt"))) {
			bw.write("-- New game: " + playerName);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		// reset details for players and dealer on display
		for (int i = 0; i < players.length; ++i) {
			gui.updatePlayerCount(i, players[i].getHandValue());
			gui.updatePlayerName(i, players[i].getName());
			gui.updatePlayerBalance(i, players[i].getBalance());
			gui.updatePlayerBet(i, players[i].getBet());
		}
		
		gui.updateDealerCount(0);
		
		if (onlyAI)
			newHand();
		else 
			// enable DEAL and Bet buttons and waits for event
			gui.buttonSwitcher(true, true, false, false, false);
	}
	
	/**
	 * Resets GUI and hands for dealers and players. It calls 'placeBets' method when done.
	 */
	private void newHand() {
		// reset GUI player details
		gui.updateDealerCount(0);
		gui.updateDealerName("Dealer");
		for (int i = 0; i < players.length; ++i) {
			gui.updatePlayerCount(i, 0);
			gui.updatePlayerName(i, players[i].getName());
		}
		
		gui.clearAllHands(); // clean GUI of cards
		for (int i = 0; i < players.length; ++i) {
			players[i].setDoubleAllowed(true); // so players can double down 
			players[i].clearHand();
			if (players[i].getStatus() != Status.DEAD)
				players[i].setStatus(Status.ALIVE);
			else 
				gui.addCardToPanel(i, "Empty"); // silhouette
		}
		
		dealer.clearHand();
		placeBets();
	}
	
	/**
	 * Sets bets for all AI players according to their betting strategies. 
	 * It call 'dealCards' method when done.
	 */
	private void placeBets() {
		for (int i = 0; i < players.length; ++i) {	
			// only ai players that are not dead place bet
			if (players[i].getStatus() != Status.DEAD && players[i] instanceof PlayerAI) {
				// if player is AI place bet 		
				int bet = ((PlayerAI)players[i]).getBettingStrategy();
				players[i].setBalance(players[i].getBalance() - bet);
				players[i].setBet(bet);
				gui.updatePlayerBet(i, players[i].getBet());
				gui.updatePlayerBalance(i, players[i].getBalance());
			}
		}
		
		dealCards();
	}
	
	/**
	 * Shuffle deck and deals the first two cards to all players and dealer. If the human 
	 * player is still in the game, enables the HIT, STAND and BET buttons. Otherwise 
	 * it calls the 'aiPlayersTurn' method. 
	 */
	private void dealCards() {
		deck = new Deck();
		deck.shuffle();	
		gui.setDealerHidesCard(true); // dealer hides his first card for now
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception { 
				for (int ncard = 0; ncard < 2; ++ncard) {	
					// players get card
					for (int i = 0; i < players.length; ++i) {
						if (players[i].getStatus() == Status.ALIVE) {
							Card card = deck.getNextCard();
							players[i].addCard(card);
							gui.addCardToPanel(i, card.toString()); // display card
							gui.updatePlayerCount(i, players[i].getHandValue());
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					// dealer gets card
					Card card = deck.getNextCard();
					dealer.addCard(card);
					gui.addCardToDealer(card.toString()); // add card image to dealers panel
				
					if (ncard < 1)
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
				
				// display players information 
				for (int i = 0; i < players.length; ++i) {
					gui.updatePlayerCount(i, players[i].getHandValue());
				}
				
				// display dealers information
				gui.updateDealerCount(dealer.getHandValue(false));
				return null;
			}
			
			@Override
			public void done() {
				// let human play
				if (onlyAI || players[0].getStatus() == Status.DEAD)
					aiPlayersTurn();
				else {
					if (players[0].getHandValue() == 21) {
						players[0].setStatus(Status.BLACKJACK);
						gui.updatePlayerName(0, players[0].getName() + " Blackjack!");
						aiPlayersTurn();
					}
					else {
						gui.activatePlayer(0);
						// disable: bet, deal -- enable: hit, stand, double
						gui.buttonSwitcher(false, false, true, true, true);	
					}
				}
			}	
		};
		
		worker.execute();
	}
	
	/**
	 * Computer players double, hit or stand according to their implemented strategies. 
	 * Once all players are BUSTED or STAND, it calls the 'dealerTurn' method.
	 */
	private void aiPlayersTurn() {
		// Disable all buttons while AI plays
		gui.buttonSwitcher(false, false, false, false, false);	
			
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < players.length; ++i ) {
					// skip human and dead ai players
					if ( !(players[i] instanceof PlayerAI) || players[i].getStatus() == Status.DEAD)
						continue;
					
					// show active player
					gui.activatePlayer(i);
					
					// blackjack
					if (players[i].getHandValue() == 21) {
						players[i].setStatus(Status.BLACKJACK);
						gui.updatePlayerName(i, players[i].getName() + " Blackjack!");
					}
										
					while (players[i].getStatus() == Status.ALIVE) {	// money left?
						// pause between cards
						try {
							Thread.sleep(SLEEP);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Strat strat = ((PlayerAI)players[i]).
								getPlayingStrategy(dealer.getHandValue(false), players[i].getHand());
						
						switch (strat) {
							case S:
								// stand
								players[i].setStatus(Status.STAND);
								gui.updatePlayerName(i, players[i].getName() + " STAND");
								break;
							case H:
								// hit
								aiHits(i);
								break;
							case Dh:
								// Double if allowed, Hit otherwise
								if ( !aiDoubles(i)) 
									aiHits(i);
								break;
							case Ds:
								// Double if allowed, Stand otherwise
								if ( !aiDoubles(i)) {
									players[i].setStatus(Status.STAND);
									gui.updatePlayerName(i, players[i].getName() + " STAND");
								}
						}	
					
						players[i].setDoubleAllowed(false); // double down not allowed in this round 
					}
					// pause between players
					try {
						Thread.sleep(SLEEP);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
		
				return null;
			}
			
			@Override
			public void done() {
				dealerTurn();
			}		
		};
		
		worker.execute();
	}
	
	/**
	 * A computer player with index 'i' has hit so he gets a new card. 
	 * @param i		player index
	 */
	private void aiHits(int i) {
		Card card = deck.getNextCard();
		players[i].addCard(card);
		gui.addCardToPanel(i, card.toString());
		gui.updatePlayerCount(i, players[i].getHandValue());
	
		if (players[i].getHandValue() > 21) {
			players[i].setStatus(Status.BUSTED);
			gui.updatePlayerName(i, players[i].getName() + " BUSTED!");
		}
		else if (players[i].getHandValue() == 21) {
			players[i].setStatus(Status.STAND);
			gui.updatePlayerName(i, players[i].getName() + " STAND");
		}
	}
	
	/**
	 * Computer player has doubled down. His bet gets doubled and he gets one last card.
	 * Double down is only possible as first move after the initial two cards, 
	 * and the player has enough balance to double his initial bet.
	 * @param i		player index
	 * @return		true if player can double down,
	 * 				false when player is not allowed to double down
	 */
	private boolean aiDoubles(int i) {
		if (!players[i].doubleIsAllowed() || players[i].getBalance() < players[i].getBet())
			return false;
		
		players[i].setBalance(players[i].getBalance() - players[i].getBet());
		gui.updatePlayerBalance(i, players[i].getBalance());
		players[i].setBet(players[i].getBet() * 2);
		gui.updatePlayerBet(i, players[i].getBet());
		// get one more card
		Card card = deck.getNextCard();
		players[i].addCard(card);
		gui.addCardToPanel(i, card.toString());
		gui.updatePlayerCount(i, players[i].getHandValue());
	
		if (players[i].getHandValue() > 21) {
			players[i].setStatus(Status.BUSTED);
			gui.updatePlayerName(i, players[i].getName() + " BUSTED!");		
		}
		else {
			players[i].setStatus(Status.STAND);
			gui.updatePlayerName(i, players[i].getName() + " STAND");
		} 	
		
		return true;
	}

	/**
	 * All players have STAND or BUSTED status, so it is time for the dealer 
	 * to play his hand. Once the dealer has BUSTER or STAND it calls 
	 * the 'calculateBalanceForAllPlayers' method.
	 */
	private void dealerTurn() {
		gui.activateDealer();
		dealer.setStatus(Status.ALIVE);
		gui.setDealerHidesCard(false); // dealer shows all cards now
		gui.updateDealerCount(dealer.getHandValue(true));
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				// pause after showing hidden card
				try {
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// any player holding a valid hand? 
				if (	players[0].getStatus() == Status.STAND ||
						players[1].getStatus() == Status.STAND ||
						players[2].getStatus() == Status.STAND ) {
					
					if (dealer.getHandValue(true) == 21) {
						dealer.setStatus(Status.BLACKJACK);
						gui.updateDealerName("Dealer Blackjack!");
						
					}
					else if (dealer.getHandValue(true) > 16) {
						dealer.setStatus(Status.STAND);
						gui.updateDealerName("Dealer STAND");			
					}
					else {
						// draw more cards
						while (dealer.getStatus() != Status.BUSTED && 
								dealer.getStatus() != Status.STAND) {
							Card card = deck.getNextCard();
							dealer.addCard(card);
							gui.addCardToDealer(card.toString());
							gui.updateDealerCount(dealer.getHandValue(true));
							if (dealer.getHandValue(true) > 21) {
								dealer.setStatus(Status.BUSTED);
								gui.updateDealerName("Dealer BUSTED!");
							}
							else if (dealer.getHandValue(true) > 16) {
								dealer.setStatus(Status.STAND);
								gui.updateDealerName("Dealer STAND");
							}
							else 
								// pause before getting new card
								try {
									Thread.sleep(SLEEP);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
						}	
					}
				}
			
				return null;
			}
			
			@Override
			public void done() {	
				calculateBalanceForAllPlayers();
			}		
		};
		
		worker.execute();	
	}
	
	/**
	 * The dealer has STAND or BUSTED status. It is time to compare every player's hand with 
	 * the dealer's hand and adjust their balances. Once the balances have been updated, it 
	 * calls the 'playAnotherHand' unless the human player is still in the game. In that 
	 * case, enables the DEAL and BET buttons and waits for a button event.
	 */
	private void calculateBalanceForAllPlayers() {
		// output hand results to file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Results.txt", true))) {
			bw.write("-- NEW hand --------------------------------------------------");
			bw.newLine();
			for (int i = 0; i < players.length; ++i) {
				bw.append(players[i].toString());
				bw.newLine();
			}
			bw.write("Dealer: " + dealer.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// compare every player's hand with dealer's hand 
		for (int i = 0; i < players.length; ++i) {
			// jump dead players
			if (players[i].getStatus() == Status.DEAD)
				continue;
			
			Winner winner = Winner.PUSH;
			if (players[i].getStatus() != Status.BUSTED) {
				if (dealer.getStatus() == Status.BUSTED)
					winner = Winner.PLAYER;
				else {
					if (players[i].getHandValue() > dealer.getHandValue(true))
						winner = Winner.PLAYER;
					
					if (dealer.getHandValue(true) > players[i].getHandValue())
						winner = Winner.DEALER;
					
					if (players[i].getStatus() == Status.BLACKJACK && 
							dealer.getStatus() != Status.BLACKJACK)
						winner = Winner.PLAYER;
					
					if (dealer.getStatus() == Status.BLACKJACK &&
							players[i].getStatus() != Status.BLACKJACK)
						winner = Winner.DEALER;
				}
			}
			else 
				winner = Winner.DEALER;
			
			// balance and bet adjustments
			if (winner == Winner.DEALER) {				
				if (players[i].getBalance() == 0) {
					players[i].setStatus(Status.DEAD);
					gui.updatePlayerName(i, players[i].getName() + " OUT");
					if (i == 0) 
						onlyAI = true;
				}
				else {
					gui.updatePlayerName(i, players[i].getName() + " LOSES (-$" + 
						players[i].getBet() + ")");
				}
			}
			else if (winner == Winner.PLAYER) {
				players[i].setBalance(players[i].getBalance() + (players[i].getBet() * 2));
				gui.updatePlayerName(i, players[i].getName() + " WINS (+$" +
						players[i].getBet() + ")");
			}
			else {
				players[i].setBalance(players[i].getBalance() + players[i].getBet());
				gui.updatePlayerName(i, players[i].getName() + " PUSH (+$0)");
			}
			
			// forces human player to bet minimum $10  
			if (i == 0 && !onlyAI) {
				players[i].setBalance(players[i].getBalance() - 10);
				players[i].setBet(10);
			}
			else 
				players[i].setBet(0);
		}
		
		// updates balance and bet GUI
		for (int i = 0; i < players.length; ++i) {
			gui.updatePlayerBalance(i, players[i].getBalance());
			gui.updatePlayerBet(i, players[i].getBet());
		}
		
		// output hand results to file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Results.txt", true))) {
			bw.newLine();
			bw.write("-- Update balance");
			bw.newLine();
			for (int i = 0; i < players.length; ++i) {
				bw.append(players[i].toString());
				bw.newLine();
			}
			bw.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
			
		if (onlyAI) {
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			playAnotherHand();
		}
		else 
			// Enable bet and deal buttons
			gui.buttonSwitcher(true, true, false, false, false);
	}

	/**
	 * Checks if any player has money left before playing another hand.
	 * In that case, it calls the 'newHand' method.
	 */
	private void playAnotherHand() {
		boolean allAreDead = true;
		for (int i = 0; i < players.length; ++i)
			if (players[i].getStatus() != Status.DEAD)
				allAreDead = false;
		
		if (allAreDead) {
			gui.showMessage("Game Over");
			System.exit(1);
		}
		
		newHand();
	}
	
	/**
	 *	Button handler for DEAL button. It calls the 'playAnotherHand' method.
	 */
	public void dealButtonClicked() {
		gui.buttonSwitcher(false, false, false, false, false);
		playAnotherHand();
	}
	
	/**
	 * Button handler for Bet+ 
	 */
	public void betUpButtonClicked() {
		if (players[0].getBalance() > 0) {
			players[0].setBet(players[0].getBet() + 10);
			gui.updatePlayerBet(0, players[0].getBet());
			players[0].setBalance(players[0].getBalance() - 10);
			gui.updatePlayerBalance(0, players[0].getBalance());
		}
	}
	
	/**
	 * Button handler for Bet-
	 */
	public void betDownButtonClicked() {
		if (players[0].getBet() > 10) {
			players[0].setBet(players[0].getBet() - 10);
			gui.updatePlayerBet(0, players[0].getBet());
			players[0].setBalance(players[0].getBalance() + 10);
			gui.updatePlayerBalance(0, players[0].getBalance());
		}	
	}

	/**
	 * Button handler for HIT button
	 */
	public void hitButtonClicked() {	
		gui.buttonSwitcher(false, false, true, true, false); // bet, deal, hit, stand, double
		Card card = deck.getNextCard();
		players[0].addCard(card);
		gui.addCardToPanel(0, card.toString());
		gui.updatePlayerCount(0, players[0].getHandValue());
	
		if (players[0].getHandValue() > 21) {
			players[0].setStatus(Status.BUSTED);
			gui.updatePlayerName(0, players[0].getName() + " BUSTED!");	
			aiPlayersTurn();
		}
		
		if (players[0].getHandValue() == 21) {
			players[0].setStatus(Status.STAND);
			gui.updatePlayerName(0, players[0].getName() + " STAND");
			aiPlayersTurn();
		} 	
	}
	
	/**
	 * Button handler for DOUBLE button
	 */
	public void doubleButtonClicked() {
		// double bet
		if (players[0].getBalance() >= players[0].getBet()) {
			gui.buttonSwitcher(false, false, false, false, false);	// disable all buttons
			players[0].setBalance(players[0].getBalance() - players[0].getBet());
			gui.updatePlayerBalance(0, players[0].getBalance());
			players[0].setBet(players[0].getBet() * 2);
			gui.updatePlayerBet(0, players[0].getBet());
			// get one more card
			Card card = deck.getNextCard();
			players[0].addCard(card);
			gui.addCardToPanel(0, card.toString());
			gui.updatePlayerCount(0, players[0].getHandValue());
		
			if (players[0].getHandValue() > 21) {
				players[0].setStatus(Status.BUSTED);
				gui.updatePlayerName(0, players[0].getName() + " BUSTED!");		
			}
			else {
				players[0].setStatus(Status.STAND);
				gui.updatePlayerName(0, players[0].getName() + " STAND");
			} 	
			aiPlayersTurn();
		}
		else
			gui.showMessage("You do not have enough money to double your bet! ");
	}
	
	/**
	 * Button handler for STAND button. It calls the 'aiPlayersTurn' method.
	 */
	public void standButtonClicked() {
		players[0].setStatus(Status.STAND);
		gui.updatePlayerName(0, players[0].getName() + " STAND");
		aiPlayersTurn();
	}
	
	/**
	 * Button handler for EXIT button
	 */
	public void exitButtonClicked() {
		System.exit(1);
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		new GameLogic();
	}

}
