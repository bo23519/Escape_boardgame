package board;

import java.util.ArrayList;
import java.util.HashMap;

import coordinate.Coordinate;
import escape.required.Player;
import escape.util.PieceTypeDescriptor;
import piece.EscapePiece;

public interface Board<C extends Coordinate> {

	EscapePiece getPiece(C coordinate);

	int move(C from, C to);

	int checkAvaliableMoves();

	int getxMax();

	int getyMax();

	Player getCurrentPlayer();

	ArrayList<C> getExitLocations();

	HashMap<C, EscapePiece> getPieces();

	PieceTypeDescriptor[] getPieceTypes();
	
	PieceTypeDescriptor getPieceType(EscapePiece piece);

	void switchTurn();
}
