package coordinate;

import java.util.ArrayList;

import piece.EscapePiece.MovementPattern;

public abstract class EscapeCoordinate implements Coordinate {
	private final int x;
	private final int y;

	public EscapeCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y value
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EscapeCoordinate other = (EscapeCoordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public abstract ArrayList<EscapeCoordinate> getNeighbours(EscapeCoordinate direction, MovementPattern pattern);

	public abstract ArrayList<EscapeCoordinate> getJumpLocation(EscapeCoordinate over);

	/**
	 * method to check if the coordinate is with given range
	 * 
	 * @param xMax the range for x
	 * @param yMax the range for y
	 * @return true if x and y are both in (0,max) or if one is infinite(set = 0)
	 */
	public boolean isOnBoard(int xMax, int yMax) {
		return (((x <= xMax && x > 0) || (xMax == 0)) && (y <= yMax && y > 0) || (yMax == 0));
	}

	/**
	 * get the unit direction
	 * @param direction the direction to move to
	 * @return one unit of the direction
	 */
	protected int getUnitDirection(int direction) {
		if (direction > 0) {
			return 1;
		} else if (direction < 0) {
			return -1;
		}
		return 0;
	}

}
