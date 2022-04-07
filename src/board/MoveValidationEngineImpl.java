package board;

import static escape.required.Player.PLAYER1;
import static escape.required.Player.PLAYER2;

import static piece.EscapePiece.PieceAttributeID.*;
import static piece.EscapePiece.MovementPattern.*;

import java.util.ArrayList;
import java.util.HashMap;

import coordinate.Coordinate;
import coordinate.EscapeCoordinate;
import escape.exception.EscapeException;
import escape.required.Player;
import escape.util.PieceTypeDescriptor;
import piece.EscapePiece;
import piece.EscapePiece.MovementPattern;
import piece.EscapePiece.PieceName;

public class MoveValidationEngineImpl<C extends EscapeCoordinate> implements MoveValidationEngine<C> {
	Board board;

	private final int xMax;
	private final int yMax;
	private final PieceTypeDescriptor[] pieceTypes;
	private final ArrayList<C> exitLocations;
	private HashMap<C, EscapePiece> pieces;
	private int currentDistance;
	private int currentNeighbor;
	private int currentNextNeighbor;

	/**
	 * @param board         board to run the path-finding
	 * @param pointConflict if point
	 * @param remove
	 */
	public MoveValidationEngineImpl(Board board) {
		this.board = board;
		this.pieceTypes = board.getPieceTypes();
		this.exitLocations = board.getExitLocations();
		this.xMax = board.getxMax();
		this.yMax = board.getyMax();
		pieces = board.getPieces();
	}

	/**
	 * the False condition contains: 
	 * starting/end coordinate being null/not on the board 
	 * moving no pieces/ opponent's piece 
	 * move to the position with self's piece on it
	 * 
	 * @param from
	 * @param to
	 * @param currentPlayer the current player for the turn
	 * @return if the move is valid
	 */
	private boolean checkValidCoordinates(C from, C to, Player currentPlayer) {
		if (from == null || to == null // param being null
				|| !(from).isOnBoard(xMax, yMax) // not on the board
				|| !(to).isOnBoard(xMax, yMax) 
				|| !pieces.containsKey(from) // no piece at starting
				|| pieces.get(from).getPlayer() != currentPlayer // moving opponent's piece
				|| (pieces.containsKey(to) && pieces.get(to).getPlayer() == currentPlayer)) {
			// target contain the piece from same side (also covers not moving
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @param from
	 * @param to
	 * @return true if there is a legal path
	 */
	@Override
	public boolean checkForValidMove(C from, C to) {
		Player currentPlayer = board.getCurrentPlayer();
		pieces = board.getPieces();

		if (!checkValidCoordinates(from, to, currentPlayer)) { // locations check
			return false;
		}

		return findPath(from, to, board.getPieceType(pieces.get(from))); // execute path-find
	}

	/**
	 * @param from
	 * @param to
	 * @param type the type of moving piece
	 * @return true if there is a legal path
	 */
	private boolean findPath(C from, C to, PieceTypeDescriptor type) {
		// initialize variables used in path-find
		boolean flyable = false;
		boolean jumpable = false;
		int distanceLimit;
		MovementPattern pattern;
		jumpable = type.getAttribute(JUMP) != null; // attribute check
		flyable = type.getAttribute(FLY) != null;
		int vDistance = Math.abs(from.getX() - to.getX());
		int hDistance = Math.abs(from.getY() - to.getY());

		if (flyable) {
			distanceLimit = type.getAttribute(FLY).getValue();
		} else {
			distanceLimit = type.getAttribute(DISTANCE).getValue();
		}
		pattern = type.getMovementPattern();

		// extra-checking conditions to test if there are any theory path exist
		// uncomment it for large-scale path-find/ slow computer
		switch (pattern) {
		case DIAGONAL:
			if ((vDistance + hDistance) % 2 != 0 || (distanceLimit < Math.max(vDistance, hDistance))) {
				return false;
			}
			else if (flyable) {
				return true;
			}
			break;

		case LINEAR:
			if (vDistance != hDistance && vDistance != 0 && hDistance != 0) {
				return false;
			}
			else if (flyable) {
				return true;
			}
			break;

		case OMNI:
			if (distanceLimit < from.DistanceTo(to)) {
				return false;
			}
			else if (flyable) {
				return true;
			}
			break;

		case ORTHOGONAL:
			if (distanceLimit < vDistance + hDistance) {
				return false;
			}
			else if (flyable) {
				return true;
			}
			break;
		}

		ArrayList<C> checkedList = new ArrayList<C>();// list to be checked
		ArrayList<C> list = new ArrayList<C>();// list of checked coord
		list.add(from); // initial node

		currentDistance = 0; // currentDistance from origin
		currentNeighbor = 1; // neighbor with same distance
		currentNextNeighbor = 0; // neighbor with +1 distance

		while (!list.isEmpty()) {
			C current = list.get(0);

			if (current.equals(to)) {
				return true; // target reached
			} else if (currentDistance > distanceLimit || list.isEmpty()) {
				return false; // out of range
			} else {
				checkedList.add(current);
				list.remove(current);
				list.addAll(validNeighbors(current, to, jumpable, pattern, checkedList, list));
				// add neighbors


				currentNeighbor--;
				if (currentNeighbor == 0) { // keep track of distance from origin
					currentDistance++;
					currentNeighbor = currentNextNeighbor;
					currentNextNeighbor = 0;
				}
			}
		}
		return false;
	}

	/**
	 * get a set of valid Neighbors to be checked, 
	 * all neighbors if two lists are null
	 * 
	 * @param from
	 * @param to
	 * @param jumpable    if the piece has jump attribute
	 * @param pattern     the movement pattern of the piece
	 * @param checkedList list already checked
	 * @param currentList list waited to be checked
	 * @return a set of valid Neighbors to be checked
	 */
	@Override
	public ArrayList<C> validNeighbors(C from, C to, boolean jumpable, MovementPattern pattern,
			ArrayList<C> checkedList, ArrayList<C> currentList) {

		ArrayList<C> result = (ArrayList<C>) from.getNeighbours(to, pattern);
		// result arraylist to return
		C jump = null;
		C jump2 = null;

		for (EscapeCoordinate i : from.getNeighbours(to, pattern)) {

			if (jumpable) { // if the piece can jump
				jump = (C) from.getJumpLocation(i).get(0);
				if (from.getJumpLocation(i).size() == 2) {
					// if it is a triangle jump(2-dests)
					jump2 = (C) from.getJumpLocation(i).get(1);
				} else {
					jump2 = null;
				}
			}

			if ((!checkClean((C) i, to) && !jumpable) 
					// remove it if location occupied and the piece can't jump
					
					|| ((jumpable && !checkClean(jump, to)
							&& (jump2 == null || !checkClean(jump2, to))))
						// or able to jump but jumping dest is occupied

					|| (checkedList != null && checkedList.contains(i))
					// being checked already
					
					|| (currentList != null && currentList.contains(i))) {
					// waiting to be checked
				result.remove(i);
			}
		}

		currentNextNeighbor += result.size();

		return result;
	}

	/**
	 * @param piece the piece to check
	 * @param target the final destination of it
	 * @return true if the piece is legal to land on
	 */
	private boolean checkClean(C piece, C target) {
		return (piece.equals(target) || //valid if it is target
				(piece.isOnBoard(xMax, yMax) 
						&& (!pieces.containsKey(piece))
						&& (!exitLocations.contains( piece))));
				// or it is on board and no pieces and no exit
	}
}
