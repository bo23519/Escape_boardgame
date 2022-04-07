package coordinate;

import static coordinate.Coordinate.CoordinateType.*;
import static piece.EscapePiece.MovementPattern.DIAGONAL;
import static piece.EscapePiece.MovementPattern.LINEAR;
import static piece.EscapePiece.MovementPattern.OMNI;
import static piece.EscapePiece.MovementPattern.ORTHOGONAL;

import java.util.ArrayList;

import escape.exception.EscapeException;
import piece.EscapePiece.MovementPattern;

public class OrthoSquareCoordinate extends EscapeCoordinate {

	public OrthoSquareCoordinate(int x, int y) {
		super(x, y);
	}

	
	/**
	 * calculate distance between target coordinate in a SQUARE board
	 * 
	 * @param c the target coordinate
	 * @return the distance (in tiles) between the class and target coordinate
	 */
	public int DistanceTo(Coordinate c) {
		if (c == null) {
			throw new EscapeException("target coordinate is null");
		}
		int hDistance = Math.abs(getX() - ((EscapeCoordinate) c).getX());
		int vDistance = Math.abs(getY() - ((EscapeCoordinate) c).getY());
		return hDistance + vDistance;
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
		ArrayList<EscapeCoordinate> result = new ArrayList<EscapeCoordinate>();

		if (pattern == LINEAR && direction!= null) {
			int hCoordinate = getX() + getUnitDirection(direction.getX() - this.getX());
			int vCoordinate = getY() + getUnitDirection(direction.getY() - this.getY());

			OrthoSquareCoordinate target = new OrthoSquareCoordinate(hCoordinate, vCoordinate);
			result.add(target);
			return result;
		} else {
			result.add(new OrthoSquareCoordinate(getX() + 1, getY()));
			result.add(new OrthoSquareCoordinate(getX() - 1, getY()));
			result.add(new OrthoSquareCoordinate(getX(), getY() + 1));
			result.add(new OrthoSquareCoordinate(getX(), getY() - 1));
		}

		return result;
	}

	
	/**
	 * return a list of potential jumping destinations
	 *@param over the coord to jump over
	 *@return the coordinates that will land on 
	 */
	@Override
	public ArrayList<EscapeCoordinate> getJumpLocation(EscapeCoordinate over) {
		int hCoordinate = over.getX() + getUnitDirection(over.getX() - this.getX());
		int vCoordinate = over.getY() + getUnitDirection(over.getY() - this.getY());

		ArrayList<EscapeCoordinate> result = new ArrayList<EscapeCoordinate>();
		result.add(new OrthoSquareCoordinate(hCoordinate, vCoordinate));
		
		return result;
	}

}
