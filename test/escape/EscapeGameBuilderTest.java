/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package escape;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import coordinate.Coordinate;
import coordinate.EscapeCoordinate;
import escape.exception.EscapeException;
import piece.EscapePiece;

import static org.junit.Assert.*;
import static escape.required.Player.*;
import static piece.EscapePiece.PieceName.*;

/**
 * This is a simple test, not really a unit test, to make sure tht the
 * EscapeGameBuilder, in the starting code, is actually working.
 * 
 * @version May 30, 2020
 */
class EscapeGameBuilderTest {
	private EscapeGameManager<Coordinate> manager;

	/**
	 * GameManager creation test
	 */
	void loadGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
	}

	// test when no builder is called
	@Test
	void initializeManagerWithoutBuilder() throws EscapeException {
		EscapeGameManager manager = new EscapeGameManagerImpl();
		assertThrows(EscapeException.class, () -> manager.makeCoordinate(1, 1));
		assertThrows(EscapeException.class, () -> manager.getPieceAt(null));
		assertThrows(EscapeException.class, () -> manager.move(null, null));
	}

	// test fundamental functions of SquareCoordinate
	@Test
	void loadSquareGame() throws Exception {
		EscapeGameBuilder egb1 = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb1.makeGameManager();
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/test2.egc");
		EscapeGameManager<Coordinate> hexManager = egb2.makeGameManager();

		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);
		Coordinate c3 = manager.makeCoordinate(-1, -1);
		Coordinate c4 = manager.makeCoordinate(4, 4);
		Coordinate c5 = manager.makeCoordinate(4, 5);
		Coordinate c6 = hexManager.makeCoordinate(4, 4);

		assertTrue(((EscapeCoordinate) c1).isOnBoard(20, 25));
		assertFalse(((EscapeCoordinate) c3).isOnBoard(20, 25));

		assertTrue(((EscapeCoordinate) c1).equals(c1));
		assertTrue(((EscapeCoordinate) c1).equals(c4));

		assertFalse(((EscapeCoordinate) c1).equals(c2));
		assertFalse(((EscapeCoordinate) c1).equals(c5));
		assertFalse(((EscapeCoordinate) c1).equals(c6));
		assertFalse(((EscapeCoordinate) c1).equals(null));

		assertEquals(((EscapeCoordinate) c1).hashCode(), ((EscapeCoordinate) c4).hashCode());
		assertFalse(((EscapeCoordinate) c3).isOnBoard(20, 25));
		assertNotNull(manager);
	}

	// test for creation of hex game
	@Test
	void loadHexGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test2.egc");
		manager = egb.makeGameManager();
		Coordinate temp = manager.makeCoordinate(1, 1);
		assertNotNull(manager);
	}

	// test for creation of ortho game
	@Test
	void loadOrthoSquareGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test3.egc");
		manager = egb.makeGameManager();
		Coordinate temp = manager.makeCoordinate(1, 1);
		assertNotNull(manager);
	}

	// test for creation of triangle game
	@Test
	void loadTriangleGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test4.egc");
		manager = egb.makeGameManager();
		Coordinate temp = manager.makeCoordinate(1, 1);
		assertNotNull(manager);
	}

	// makeCoordinate() Test
	@Test
	void makeCoordinateTest() throws Exception {
		loadGame();
		assertNotNull(manager.makeCoordinate(1, 2)); // within board
		assertNotNull(manager.makeCoordinate(-1, -1)); // not on board
	}

	// getPieceAt() test
	@Test
	void getPieceAtTest() throws Exception {
		loadGame();
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);
		Coordinate c3 = manager.makeCoordinate(-1, -1);
		Coordinate c4 = manager.makeCoordinate(5, 12);

		assertNotNull(manager.getPieceAt(c1));
		assertNotNull(manager.getPieceAt(c2));
		assertNull(manager.getPieceAt(c3));
		assertNull(manager.getPieceAt(c4));
	}

	// Location initialization test
	@Test
	void locationInitialzationTest() throws Exception {
		loadGame();
		EscapePiece piece1 = manager.getPieceAt(manager.makeCoordinate(4, 4));
		EscapePiece piece2 = manager.getPieceAt(manager.makeCoordinate(10, 12));
		EscapePiece piece3 = manager.getPieceAt(manager.makeCoordinate(3, 3));

		assertEquals(piece1.getName(), SNAIL);
		assertEquals(piece1.getPlayer(), PLAYER1);
		assertEquals(piece2.getName(), HORSE);
		assertEquals(piece2.getPlayer(), PLAYER2);
		assertEquals(piece3.getName(), DOG);
		assertEquals(piece3.getPlayer(), PLAYER1);
	}

	// test for distanceTo() function
	@Test
	void distanceToTest() throws Exception {
		loadGame();
		Coordinate p1 = manager.makeCoordinate(1, 1);
		Coordinate p2 = manager.makeCoordinate(1, 2);
		Coordinate p3 = manager.makeCoordinate(2, 2);
		Coordinate p4 = manager.makeCoordinate(5, 5);

		assertEquals(p1.DistanceTo(p2), 1);
		assertEquals(p1.DistanceTo(p3), 1);
		assertEquals(p1.DistanceTo(p1), 0);
		assertEquals(p1.DistanceTo(p4), 4);
		assertThrows(EscapeException.class, () -> p1.DistanceTo(null));
	}

//	 move() test
	@Test
	void moveTest() throws Exception {
		loadGame();
		Coordinate p1 = manager.makeCoordinate(4, 4);
		Coordinate p2 = manager.makeCoordinate(10, 12);
		Coordinate p3 = manager.makeCoordinate(3, 3);
		Coordinate p4 = manager.makeCoordinate(3, 5);
		
		Coordinate exit = manager.makeCoordinate(3, 10);
		Coordinate errorCoordinateOne = manager.makeCoordinate(-1, -1);
		Coordinate errorCoordinateTwo = manager.makeCoordinate(30, 30);
		Coordinate clear = manager.makeCoordinate(5, 5);
		Coordinate clear2 = manager.makeCoordinate(4, 5);
		Coordinate clear3 = manager.makeCoordinate(8, 8);
		Coordinate clear4 = manager.makeCoordinate(6, 6);

		// moving null
		assertFalse(manager.move(null, p1));
		assertFalse(manager.move(p1, null));

		// move to error location(out the the board)
		assertFalse(manager.move(p1, errorCoordinateOne));
		assertFalse(manager.move(p1, errorCoordinateTwo));

		// move no piece
		assertFalse(manager.move(exit, clear));

		// move opponent's piece
		assertFalse(manager.move(p2, clear));

		// move to the place with self piece on it
		assertFalse(manager.move(p1, p3));

		// move to self
		assertFalse(manager.move(p1, p1));
		
		//moving out of the distance
		assertFalse(manager.move(p1, exit));
		
		//moving to get around
		assertFalse(manager.move(p3, clear3));
		
		// LINEAR getting turned
		assertFalse(manager.move(p3, clear2));

		// valid move
		assertTrue(manager.move(p1, clear));
		assertEquals(manager.getPieceAt(clear).getName(), SNAIL);
		assertEquals(manager.getPieceAt(clear).getPlayer(), PLAYER1);

		// switch turn
		assertTrue(manager.move(p2, clear4));
		assertNull(manager.getPieceAt(p2));
		assertEquals(manager.getPieceAt(clear4).getName(), HORSE);
		assertEquals(manager.getPieceAt(clear4).getPlayer(), PLAYER2);
		

		// move on to oppent's piece
		assertTrue(manager.move(clear, clear4));
		assertEquals(manager.getPieceAt(clear4).getName(), SNAIL);
		assertEquals(manager.getPieceAt(clear4).getPlayer(), PLAYER1);


		// move on to oppent's piece
		assertTrue(manager.move(p4, clear));
		assertEquals(manager.getPieceAt(clear).getName(), BIRD);
		assertEquals(manager.getPieceAt(clear).getPlayer(), PLAYER2);
		

		// move
		// endgame message will be printed
		assertTrue(manager.move(clear4, clear));
		assertEquals(manager.getPieceAt(clear).getName(), SNAIL);
		assertEquals(manager.getPieceAt(clear).getPlayer(), PLAYER1);
		

		// game is over
		// endgame message will be printed
		assertFalse(manager.move(clear4, exit));
		
	}
	
	//Triangle move test
	@Test
	void triangleMoveTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test4.egc");
		manager = egb.makeGameManager();
		
		Coordinate p1 = manager.makeCoordinate(4, 4);
		Coordinate p2 = manager.makeCoordinate(3, 3);
		Coordinate p3 = manager.makeCoordinate(3, 4);
		Coordinate p4 = manager.makeCoordinate(3, 5);
		Coordinate p5 = manager.makeCoordinate(4, 5);
		
		Coordinate exit = manager.makeCoordinate(4, 6);
		
		Coordinate clear = manager.makeCoordinate(2, 4);
		Coordinate clear2 = manager.makeCoordinate(3, 6);
		Coordinate clear3 = manager.makeCoordinate(4, 7);
		Coordinate clear4 = manager.makeCoordinate(-10, 0);
		

		//jumping not across edge
		assertFalse(manager.move(p1, clear));
		
		//not allow to jump across 2 pieces
		assertFalse(manager.move(p1, clear2));
		assertFalse(manager.move(p1, clear3));

		// jump over one piece and land on another
		assertTrue(manager.move(p1, p4));
		assertEquals(manager.getPieceAt(p4).getName(), FROG);
		assertEquals(manager.getPieceAt(p4).getPlayer(), PLAYER1);
		
		//normal move
		assertTrue(manager.move(p3, clear4));
		assertEquals(manager.getPieceAt(clear4).getName(), SNAIL);
		assertEquals(manager.getPieceAt(clear4).getPlayer(), PLAYER2);
		

		// jump over one piece and land on empty
		assertTrue(manager.move(p4, p1));
		assertEquals(manager.getPieceAt(p1).getName(), FROG);
		assertEquals(manager.getPieceAt(p1).getPlayer(), PLAYER1);
		

		//normal move
		assertTrue(manager.move(clear4, p3));
		

		// jump over one piece and land on exit
		// test if jump can reach both directions
		assertTrue(manager.move(p1, exit));
		assertNull(manager.getPieceAt(exit));
		
	}
	
	//Ortho move test
	@Test
	void OrthoMoveTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test3.egc");
		manager = egb.makeGameManager();
		
		Coordinate p1 = manager.makeCoordinate(3, 3);
		Coordinate p2 = manager.makeCoordinate(3, 5);
		Coordinate p3 = manager.makeCoordinate(4, 4);
		
		Coordinate exit = manager.makeCoordinate(3, 8);
		
		Coordinate clear = manager.makeCoordinate(8, 8);
		Coordinate clear2 = manager.makeCoordinate(4, 5);
		Coordinate clear3 = manager.makeCoordinate(8, 10);
		Coordinate clear4 = manager.makeCoordinate(8, 3);
		Coordinate clear5 = manager.makeCoordinate(5, 5);
		
		//cannot move diagonally
		assertFalse(manager.move(p1, clear));
		assertFalse(manager.move(p2, clear3));
		
		//cannot turn
		assertFalse(manager.move(p1, clear2));

		assertTrue(manager.move(p1, clear4));
		assertEquals(manager.getPieceAt(clear4).getName(), DOG);
		assertEquals(manager.getPieceAt(clear4).getPlayer(), PLAYER1);
		
		assertTrue(manager.move(p2, clear2));
		assertEquals(manager.getPieceAt(clear2).getName(), BIRD);
		assertEquals(manager.getPieceAt(clear2).getPlayer(), PLAYER2);
		

		assertTrue(manager.move(p3, clear5));
		assertEquals(manager.getPieceAt(clear5).getName(), FROG);
		assertEquals(manager.getPieceAt(clear5).getPlayer(), PLAYER1);
		

		assertTrue(manager.move(clear2, exit));
		assertNull(manager.getPieceAt(exit)); //end game message will be printed
	}
	
	//Triganle distanceTo() test
	@Test
	void triangleDistanceToTest() throws Exception{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test4.egc");
		manager = egb.makeGameManager();
	}

}
