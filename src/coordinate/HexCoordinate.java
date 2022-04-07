package coordinate;

import static coordinate.Coordinate.CoordinateType.*;
import static piece.EscapePiece.MovementPattern.DIAGONAL;
import static piece.EscapePiece.MovementPattern.LINEAR;
import static piece.EscapePiece.MovementPattern.OMNI;
import static piece.EscapePiece.MovementPattern.ORTHOGONAL;

import java.util.ArrayList;

import escape.exception.EscapeException;
import piece.EscapePiece.MovementPattern;

public class HexCoordinate extends EscapeCoordinate {

	public HexCoordinate(int x, int y) {
		super(x, y);
	}

	@Override
	public int DistanceTo(Coordinate c) {
		return 0;
	}

	/**
	 * find the neighbors that are valid for current movement pattern
	 * 
	 * @param direction the direction for linear movement type
	 * @param pattern   the movement pattern
	 * @return the list contain all the possible neighbor to move to
	 */
	@Override
	public ArrayList<EscapeCoordinate> getNeighbours(EscapeCoordinate direction, MovementPattern pattern) {
//		ArrayList<EscapeCoordinate> result = new ArrayList<EscapeCoordinate>();
//
//		if (pattern == LINEAR && direction != null) {
//			int hCoordinate = getX() + getUnitDirection(direction.getX() - this.getX());
//			int vCoordinate = getY() + getUnitDirection(direction.getY() - this.getY());
//
//			HexCoordinate target = new HexCoordinate(hCoordinate, vCoordinate);
//			result.add(target);
//			return result;
//		}
//		else {
//			result.add(new HexCoordinate(getX() + 1, getY()));
//			result.add(new HexCoordinate(getX() - 1, getY()));
//			result.add(new HexCoordinate(getX(), getY() + 1));
//			result.add(new HexCoordinate(getX(), getY() - 1));
//			result.add(new HexCoordinate(getX() + 1, getY() - 1));
//			result.add(new HexCoordinate(getX() + -1, getY() + 1));
//		}

		return null;
	}

	
	/**
	 * return a list of potential jumping destinations
	 *@param over the coord to jump over
	 *@return the coordinates that will land on 
	 */
	@Override
	public ArrayList<EscapeCoordinate> getJumpLocation(EscapeCoordinate over) {
//		int hCoordinate = over.getX() + getUnitDirection(over.getX() - this.getX());
//		int vCoordinate = over.getY() + getUnitDirection(over.getY() - this.getY());
//		
//		ArrayList<EscapeCoordinate> result = new ArrayList<EscapeCoordinate>();
//		result.add(new HexCoordinate(hCoordinate, vCoordinate));
//		
//		return result;
		return null;
	}
}
