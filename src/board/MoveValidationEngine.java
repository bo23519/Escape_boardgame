package board;

import java.util.ArrayList;

import coordinate.Coordinate;
import coordinate.EscapeCoordinate;
import piece.EscapePiece.MovementPattern;

public interface MoveValidationEngine<C extends Coordinate>{
	
	ArrayList<C> validNeighbors(C from, C to, boolean jumpable, MovementPattern pattern,
			ArrayList<C> checkedList, ArrayList<C> currentList) ;
	
	boolean checkForValidMove(C from, C to);
}