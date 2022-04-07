package coordinate;

import static coordinate.Coordinate.CoordinateType.*;
import static piece.EscapePiece.MovementPattern.*;

import java.util.ArrayList;

import escape.exception.EscapeException;
import piece.EscapePiece.MovementPattern;

public class TriangleCoordinate extends EscapeCoordinate {
	public TriangleCoordinate(int x, int y) {
		super(x, y);
	}

	@Override
	public int DistanceTo(Coordinate c) {
		int targetX = ((TriangleCoordinate) c).getX();
		int targetY = ((TriangleCoordinate) c).getY();
		int hDistance = getX() - targetX;
		int vDistance = getY() - targetY;
		boolean targetFacingUp = ((TriangleCoordinate) c).facingUp();
		boolean fromFacingUp = this.facingUp();

		if (vDistance == 0) {
			if (fromFacingUp == targetFacingUp) {
				return Math.abs(hDistance) * 2;
			} else if ((hDistance > 0 && !fromFacingUp)||(hDistance < 0 && fromFacingUp)) {
				return 2 * Math.abs(hDistance) + 1;
			} else {
				return 2 * Math.abs(hDistance) - 1;
			}
		}
		else if (Math.abs(hDistance) < Math.abs(vDistance)) {
			return Math.abs(hDistance) + Math.abs(vDistance);
		}
		else if (Math.abs(hDistance) >= Math.abs(vDistance) 
				&& fromFacingUp == targetFacingUp) {
			return 2 * Math.abs(hDistance);
		}
		else if (Math.abs(hDistance) == Math.abs(vDistance)
				&& fromFacingUp != targetFacingUp) {
			return 2 * Math.abs(hDistance) + 1;
		}
		return 2 * Math.abs(hDistance) + 1;
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

		if ((getX() + getY()) % 2 == 0) { // check for r
			result.add(new TriangleCoordinate(getX() + 1, getY()));
		} else {
			result.add(new TriangleCoordinate(getX() - 1, getY()));
		}

		// 2 static ones
		result.add(new TriangleCoordinate(getX(), getY() + 1));
		result.add(new TriangleCoordinate(getX(), getY() - 1));

		return result;
	}

	/**
	 * return a list of potential jumping destinations
	 * 
	 * @param over the coord to jump over
	 * @return the coordinates that will land on
	 */
	@Override
	public ArrayList<EscapeCoordinate> getJumpLocation(EscapeCoordinate over) {
		ArrayList<EscapeCoordinate> result = over.getNeighbours(this, OMNI);
		result.remove(this);

		return result;
	}

	/**
	 * helper method to identify the facing direction
	 * 
	 * @return true if it is facing up
	 */
	private boolean facingUp() {
		return (this.getX() + this.getY()) % 2 == 1;
	}
}
