package escape;

import static escape.required.Player.PLAYER1;
import static escape.required.Player.PLAYER2;

import escape.required.Player;
import escape.util.RuleDescriptor;

public interface ScoreBoard {

	boolean checkActiveGame();
	
	boolean getRemoveRule();
	
	boolean getPointsConflictRule();

	void addScore(int score, Player player);

	void endGame(Player winner);
	
	void sendEndGameMessage();

}
