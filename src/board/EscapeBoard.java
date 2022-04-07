package board;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.function.BiFunction;

import static escape.required.Player.*;
import static escape.required.LocationType.*;
import static piece.EscapePiece.MovementPattern.*;
import static piece.EscapePiece.PieceName.*;
import static piece.EscapePiece.PieceAttributeID.*;

import escape.exception.EscapeException;
import escape.required.LocationType;
import escape.required.Player;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;
import piece.EscapePiece;
import piece.EscapePiece.PieceName;
import piece.Piece;
import coordinate.*;

public class EscapeBoard<C extends Coordinate> implements Board<C> {
	private final int xMax;
	private final int yMax;
	private Player currentPlayer = PLAYER1;
//	private ArrayList<C> blockLocations;
	private ArrayList<C> exitLocations;
	private HashMap<C, EscapePiece> pieces;
	private HashMap<C, Integer> deductPoints;
	private BiFunction<Integer, Integer, C> makeCoordinate;
	private PieceTypeDescriptor[] pieceTypes;
	private MoveValidationEngine moveEngine;
	private boolean pointConflict;
	private boolean remove;

	/**
	 * @param xMax          the maximum x-coordinate value
	 * @param yMax          the maximum y-coordinate value
	 * @param pieceTypes    information about different piece types
	 * @param locations     initial location information
	 * @param makeCoord     functions from the gameManager
	 * @param pointConflict if game rule include pointConflict
	 * @param remove        if game rule include remove
	 */
	public EscapeBoard(int xMax, int yMax, PieceTypeDescriptor[] pieceTypes, LocationInitializer[] locations,
			BiFunction<Integer, Integer, C> makeCoord, boolean pointConflict, boolean remove) {
		this.xMax = xMax;
		this.yMax = yMax;
		this.pieceTypes = pieceTypes;
		this.pointConflict = pointConflict;
//		if (pointConflict) { // initialize a hashmap to record points_conflicts
//			deductPoints = new HashMap<C, Integer>();
//		}
		this.remove = remove;
		makeCoordinate = makeCoord;
		intializeLocations(locations);
	}

	/**
	 * return the piece on given coordinate if any
	 * 
	 * @param coordinate the coordinate to look on
	 * @return the piece on the coordinate, null if there is none
	 */
	@Override
	public EscapePiece getPiece(C coordinate) {
		if (coordinate != null && pieces.containsKey(coordinate)) {
			return pieces.get(coordinate);
		}
		return null;
	}

	/**
	 * Initialize the board according to the location information iterates through
	 * all the locations, assigned coordinates to EXIT/BLOCK ArrayList if it is a
	 * special location or pieces HashMap if it is a player's piece
	 * 
	 * @throws if location is not within board's range
	 * @param locations the locations information
	 */
	private void intializeLocations(LocationInitializer[] locations) throws EscapeException {
//		blockLocations = new ArrayList<C>();
		exitLocations = new ArrayList<C>();
		pieces = new HashMap<C, EscapePiece>();

		for (int i = 0; i < locations.length; i++) {
			LocationType type = locations[i].locationType;

			C currentLocation = makeCoordinate.apply(locations[i].x, locations[i].y);
			if (!((EscapeCoordinate) currentLocation).isOnBoard(xMax, yMax)) {
				throw new EscapeException("Locations initialiazation fail, Coordinate out of the board");
			}

			if (type == null) {
				type = CLEAR; // null handling
			}

			// categorizing each location information
			switch (type) {
//			case BLOCK:
//				blockLocations.add(currentLocation);
//				break;

			case EXIT:
				exitLocations.add(currentLocation);
				break;

			default: // null(player's piece) or CLEAR
				if (locations[i].player != null) { // check if there is CLEAR but no piece
					EscapePiece piece = new Piece(locations[i].pieceName, locations[i].player);
					pieces.put(currentLocation, piece);
				}
			}
		}
		// initialize the class for path-finding
		moveEngine = new MoveValidationEngineImpl(this);
	}

	/**
	 * @return the max x coordinate, 0 if infinite
	 */
	@Override
	public int getxMax() {
		return xMax;
	}

	/**
	 * @return the max y coordinate, 0 if infinite
	 */
	@Override
	public int getyMax() {
		return yMax;
	}

	/**
	 * @return the current player in the turn
	 */
	@Override
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @return a list contains all the exitLocations
	 */
	@Override
	public ArrayList<C> getExitLocations() {
		return exitLocations;
	}

	/**
	 * @return a hashmap contain info of all coords that contain pieces
	 */
	@Override
	public HashMap<C, EscapePiece> getPieces() {
		return pieces;
	}

	/**
	 * @return a list contain all the pieceTypeDescriptor
	 */
	@Override
	public PieceTypeDescriptor[] getPieceTypes() {
		return pieceTypes;
	}

	/**
	 * @param piece the piece to search for pieceTypeDescriptor
	 * @return the corresponding pieceType
	 */
	@Override
	public PieceTypeDescriptor getPieceType(EscapePiece piece) {
		if (piece != null) {
			for (int i = 0; i < pieceTypes.length; i++) {
				if (pieceTypes[i].getPieceName().equals(piece.getName())) {
					return pieceTypes[i];
				}
			}
		}
		throw new EscapeException("Piece type cannot be found");
	}

	/**
	 * Attempt to move the piece from starting coordinate to end coordinate, there
	 * are three options here: before all, check if the move is legal 1. if target
	 * has pieces, take it or result in point conflict according to the rule 2. if
	 * target is exit, return corresponding points 3. after all/ or it is clear,
	 * move the piece, update HashMaps for locations and pointConflict
	 * 
	 * @param from starting location
	 * @param to   ending location
	 * @return -1 if the move is not legal, 0 if no score added from this move,
	 *         points if the the piece gets to exit
	 */
	@Override
	public int move(C from, C to) {
		// check for legal move
		if (!moveEngine.checkForValidMove(from, to)) {
			return -1;
		}

		// move is valid, and execute based on situation
		if (pieces.containsKey(to)) { // encounter opponent piece
			if (remove) { // if remove rule present
				pieces.remove(to);

				// save for later, no need for BETA
//			} else if (pointConflict) { // if pointConflict present
//				int pointFrom = calculatePoints(from);
//				int pointTo = calculatePoints(to);
//
//				if (pointFrom > pointTo) { // compare points
//					pieces.remove(to);
//					pieces.put(to, pieces.get(from)); // to gets removed
//				} else if (pointFrom < pointTo) {
//					pieces.remove(from); // from gets removed
//				}
//
//				deductPoints.remove(from); // remove old record
//				deductPoints.remove(to);
//
//				if (pointFrom == pointTo) { // both get removed
//					pieces.remove(to);
//					pieces.remove(from);
//				} else { // update deductPoints if not equal
//					deductPoints.put(to, Math.abs(pointFrom - pointTo));
//				}
//				return 0;
//
//			} else {
//				return -1; // case when remove and pointConflict not present
//			}
			}
		}

		if (exitLocations.contains(to)) { // if exit, return points for manager to add up
			int score = calculatePoints(from);
//			if (pointConflict && deductPoints.containsKey(from)) {
//				deductPoints.remove(from); // update deducPoints under pointConflict
//			}
			return score;

		} else { // target is clear
			pieces.put(to, pieces.get(from));
			pieces.remove(from);
			// update deducPoints under pointConflict
//			if (pointConflict && deductPoints.containsKey(from)) {
//				deductPoints.put(to, deductPoints.get(from));
//				deductPoints.remove(from);
//			}
		}

		return 0;
	}

	/**
	 * helper method for switching player's Turn
	 */
	@Override
	public void switchTurn() {
		switch (currentPlayer) {
		case PLAYER1:
			currentPlayer = PLAYER2;
			break;
		case PLAYER2:
			currentPlayer = PLAYER1;
			break;
		}
	}

	/**
	 * check if there are any available moves for current player Simplified for
	 * BETA, save for later
	 * 
	 * @return 1 if p1 has nopieces, 2 for p2, 0 if all have
	 */
	@Override
	public int checkAvaliableMoves() {
		int avaliablePiecesP1 = 0;
		int avaliablePiecesP2 = 0;

		for (C coord : pieces.keySet()) { // check through all pieces
			EscapePiece piece = getPiece(coord);
//			PieceTypeDescriptor type = getPieceType(pieces.get(coord));

			if (piece.getPlayer() == PLAYER1) { // get movable neighbor
				avaliablePiecesP1++;
//				avaliablePositions += moveEngine.validNeighbors(coord, null, type.getAttribute(JUMP) != null,
//						type.getMovementPattern(), null, null).size();
			}
			else {
				avaliablePiecesP2++;
			}
		}

		if (avaliablePiecesP1 == 0) {
			return 1;
		}
		else if (avaliablePiecesP2 == 0){
			return 2;
		}
		
		return 0;
	}

	/**
	 * point calculator for poinConflict Save for later, no need for BETA
	 * 
	 * @param coord to calculate points on
	 * @return points after deduction if any
	 */
	private int calculatePoints(C coord) {
		PieceTypeDescriptor type = getPieceType(pieces.get(coord));
		int score = 0;
//		
//		if (type.getAttribute(VALUE) == null) {
//			return 0;
//		}
//		else {
//			score = type.getAttribute(VALUE).getValue();
//		}
//		
//		if (pointConflict) {
//			score = score - deductPoints.get(coord);
//		}
		return score;

	}
}
