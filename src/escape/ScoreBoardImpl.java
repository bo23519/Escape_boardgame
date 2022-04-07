package escape;

import static escape.required.Player.PLAYER1;
import static escape.required.Player.PLAYER2;

import board.Board;
import escape.required.Player;
import escape.util.RuleDescriptor;

public class ScoreBoardImpl implements ScoreBoard {
	private int turns = 1;
	private int player1Score = 0;
	private int player2Score = 0;
	private int goalScore = 0;
	private int turnLimit = 0;
	private boolean remove = false;
	private boolean pointConflict = false;
	private boolean activeGame = true;
	private Player winningPlayer = null;
	private String endGameMessage = "";

	/**
	 * @param rules for initialize the scoreboard
	 */
	public ScoreBoardImpl(RuleDescriptor[] rules) {
		for (RuleDescriptor rule : rules) {
			switch (rule.id) {
//		case POINT_CONFLICT:
//			pointConflict = true;
//			break;
			case REMOVE:
				remove = true;
				break;
			case SCORE:
				goalScore = rule.value;
				break;
			case TURN_LIMIT:
				turnLimit = rule.value;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * check if the game is still active
	 * 
	 * @return true if the game is still active
	 */
	@Override
	public boolean checkActiveGame() {
		return activeGame;
	}

	/**
	 * add score to a player
	 *
	 * @param score  to be added
	 * @param player player to receive the score
	 */
	@Override
	public void addScore(int score, Player player) {
		if (player == PLAYER1) {
			player1Score += score;
		} else {
			player2Score += score;
		}
	}

	/**
	 * end the game with setting winner, null if draw
	 * 
	 * @param winner the winner of the game, null if draw
	 */
	@Override
	public void endGame(Player winner) {
		activeGame = false;
		winningPlayer = winner;

		if (winner == null) {
			endGameMessage = "Game is over and reults in a draw";
		} else if (winner == PLAYER1) {
			endGameMessage = "Game is over and PLAYER1 won";
		} else {
			endGameMessage = "Game is over and PLAYER2 won";
		}

		sendEndGameMessage();
	}

	/**
	 * @return true if the remove rule is present
	 */
	@Override
	public boolean getRemoveRule() {
		return remove;
	}

	/**
	 * @return true if the conflict rule is present
	 */
	@Override
	public boolean getPointsConflictRule() {
		return pointConflict;
	}

	/**
	 * send the end game message
	 */
	@Override
	public void sendEndGameMessage() {
		System.out.println(endGameMessage);
	}
}
