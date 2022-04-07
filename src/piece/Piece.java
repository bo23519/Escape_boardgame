package piece;

import escape.exception.EscapeException;
import escape.required.Player;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import piece.EscapePiece.PieceName;

public class Piece implements EscapePiece {
	private PieceName pieceName;
	private Player player;

	public Piece(PieceName pieceName, Player player) {
		this.pieceName = pieceName;
		this.player = player;
	}

	/**
	 * @return the name of the piece
	 */
	@Override
	public PieceName getName() {
		return pieceName;
	}

	/**
	 * @return the player
	 */
	@Override
	public Player getPlayer() {
		return player;
	}
}
