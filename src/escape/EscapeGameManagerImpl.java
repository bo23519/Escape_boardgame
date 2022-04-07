package escape;

import escape.util.*;
import piece.EscapePiece;
import board.Board;
import board.EscapeBoard;
import coordinate.*;
import escape.exception.EscapeException;
import escape.required.*;
import static escape.required.Player.*;

import java.util.function.BiFunction;

class EscapeGameManagerImpl<C extends Coordinate> implements EscapeGameManager<C> {
	private Coordinate.CoordinateType coordinateType;
	private PieceTypeDescriptor[] pieceTypes;
	private RuleDescriptor[] rules;
	private int xMax;
	private int yMax;
	private Board board;
	private ScoreBoard scoreBoard;

	protected EscapeGameManagerImpl() {
	}

	/**
	 * initialize the game
	 * 
	 * @param initializer the initial information for the game
	 */
	protected void initialize(EscapeGameInitializer initializer) {
		coordinateType = initializer.getCoordinateType();

		xMax = initializer.getxMax();
		yMax = initializer.getyMax();
		pieceTypes = initializer.getPieceTypes();
		LocationInitializer[] locations = initializer.getLocationInitializers();
		BiFunction<Integer, Integer, C> makeCoord = (a, b) -> makeCoordinate(a, b);


		rules = initializer.getRules();
		scoreBoard = new ScoreBoardImpl(rules);
		board = new EscapeBoard(xMax, yMax, pieceTypes, locations, makeCoord, 
				scoreBoard.getPointsConflictRule(), scoreBoard.getRemoveRule());
		//only set for remove first
	}
	
	/**
	 * check if the game is still active given rules
	 * @return true if game is still active
	 */
	public void checkAvaliblePieces() {
//		if (goalScore != 0 && player1Score >= goalScore) {
//			endGame(PLAYER1);
//		} else if (goalScore != 0 && player2Score >= goalScore) {
//			endGame(PLAYER2);
//		} else if (turnLimit != 0 && turns > turnLimit) {
//			if (player1Score > player2Score) {
//				endGame(PLAYER1);
//			} else if (player2Score > player1Score) {
//				endGame(PLAYER2);
//			} else {
//				endGame(null);
//			}
//		} else 
		if (board.checkAvaliableMoves() == 1) {
				scoreBoard.endGame(PLAYER2);
		} else if (board.checkAvaliableMoves() == 2) {
				scoreBoard.endGame(PLAYER1);
		}
	}

	/**
	 * Attempt to move the piece from starting coordinate to end coordinate
	 * check if end game condition occurs
	 * update game state according to the move result
	 * 
	 * @param from starting location
	 * @param to   ending location
	 * @return true if the move is legal and executed
	 */
	@Override
	public boolean move(C from, C to) throws EscapeException {
		if (board == null) {
			throw new EscapeException("the board has not been initialized, "
					+ "check if gameManager was geneated from the gameBuilder with correct egc file");
		}

		if(!scoreBoard.checkActiveGame()) { // check to disallow move make after game is end
			scoreBoard.sendEndGameMessage();
			return false;
		}
		
		int score = board.move(from, to);
		if(score == -1) {
			return false;
		}
		else{
			scoreBoard.addScore(score, board.getCurrentPlayer());
		}

		checkAvaliblePieces();
		// second check so even if the player is not moving, msg shows up
		if(!scoreBoard.checkActiveGame()) {
			scoreBoard.sendEndGameMessage();
		}

		board.switchTurn(); 
		return true;
	}

	
	/**
	 * return the piece on given coordinate if any
	 * 
	 * @param coordinate the coordinate to look on
	 * @return the piece on the coordinate, null if there is none
	 */
	@Override
	public EscapePiece getPieceAt(C coordinate) throws EscapeException {
		if (board == null) {
			throw new EscapeException("the board has not been initialized, "
					+ "check if gameManager was geneated from the gameBuilder with correct egc file");
		}
		return board.getPiece(coordinate);
	}

	
	/**
	 * Generate a coordinate with given position values
	 * 
	 * @throws EscapeException if there is no coordinateType initialized
	 * @param x the x position of the coordinate
	 * @param y the y position of the coordinate
	 * @return the coordinate at position
	 */
	@Override
	public C makeCoordinate(int x, int y) {
		if (coordinateType == null) {
			throw new EscapeException("Coordinate type not defined");
		}
		switch (coordinateType) {
		case HEX:
			return (C) new HexCoordinate(x, y);
		case ORTHOSQUARE:
			return (C) new OrthoSquareCoordinate(x, y);
		case SQUARE:
			return (C) new SquareCoordinate(x, y);
		case TRIANGLE:
			return (C) new TriangleCoordinate(x, y);
//		default:
//			throw new EscapeException("Coordinate type not defined");
		}
		return null;
	}

}
