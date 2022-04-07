#### Alpha Release
### 3-Nov-2020

Finished Module 5 today, start to work on Escape.

Thoughts today: Interface is more useful than it was in my impression,
try to use Interface for objects to allow future expansion

Information from Developer's guide:
	4 main points for Alpha Release:
* Create new game, only SQUARE coordinate
* getPieceAt()
* makeCoordinate()
* move()
		
		
### 5-Nov-2020

Finished going through the starting code, hard to understand from the first look.
Started to write some initial test cases for the gameManager using `assertNotNull()`


### 7-Nov-2020

New Developer's guide come out today, part to be highlighted:

* Either coordinate is not on the board or is null -> false
* Destination is aBLOCKlocationfalseDestination is an EXIT -> true and remove piece
* There is no piece on the source location -> false
* A moving player’s is piece on the destination -> false 
* An opponent’s piece is on the destination -> true and remove opponent’s place,place moving piece on destination)
* The move violates the pieces movement pattern or attributes, but not any of the above violations (e.g. movement patternisLINEARbut the destination is not in line with the source) -> true


### 8-Nov-2020

[New help video for the starting of the project](https://canvas.wpi.edu/courses/22080/pages/approaching-coordinates)

Understanding more ideas of the project, and create `Board` `Piece` classes for the implementation of the interfaces


### 9-Nov-2020

Spend hours trying to learn about XML code and the project's attribute
and realized we don't need to parse the input....


## 10-Nov-2020

Start to work on `SquareCoordinate.java`, everything is smooth


## 11-Nov-2020

Start to work on `Board.java` as the inter-connetion between coordinates and pieces
Using the same old approach: having a `Coordinate[][]` 2-D array to store all the coordinates on the board. Store all the pieces and location information in Coordinate's implementation call 'EscapeCoordinate.java'

Then I realize : why don't I just store them in 'EscapeGameManagerImpl.java'? Does one agree on the SOLID principal more?

ended up removing `Board.java`
and categorized all the classes, create a few new packages like `coordinate` `piece`


## 12-Nov-2020

Reading the slack discussion, realized `Coordinate'`is only supposed to be a reference of location on the board, not supposed to contain all the location info..

Deleted `EscapeCoordinate.java`, add back `Board.java`

Another problem leave unsolved, how to return same coordinate object with same X/Y values?
Looked at hashCode() and equals() but didn't come up with a good solution. In my idea it only works for single int value but not for two ints values.

[HashCode](https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/)

Understand how `HashMap.contains()` work -> comparing hashCode first, then `equals(Object o)`


## 14-Nov-2020

Ask question on Slack, and got response about auto-generated HashCode and equals Method, nice!

```
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SquareCoordinate other = (SquareCoordinate) obj;
		if (type != other.type)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
```

Now the `HashMap.contains()` correctly understands the coordinate with same x and y values


## 15-Nov-2020

Focusing on move() implementation, listed down all possible `move()`, and write the tests for them

final result end up as:

```
	@Override
	public boolean move(C from, C to) {
		// check for invalid movement
		if (from == null || to == null ||  // param being null or not on the board
				!((SquareCoordinate) from).isOnBoard(xMax, yMax)|| 
				!((SquareCoordinate) to).isOnBoard(xMax, yMax)) {
			return false;
		} else if (blockLocations.contains(to)) { // target is BLOCK
			return false;
		} else if (!pieces.containsKey(from)) { // no piece at starting position
			return false;
		} else if (pieces.get(from).getPlayer() != currentPlayer) { // moving opponent's piece
			return false;
		} else if (pieces.containsKey(to) && pieces.get(to).getPlayer() == currentPlayer) {
			return false; //target contain the piece from same side(also covers not moving)
		}

		// move is valid, and execute based on situation
		if (pieces.containsKey(to)) {
			pieces.remove(to); // check if opponent piece exist
		}
		if (!exitLocations.contains(to)) { // if end coordinate is CLEAR, put the piece there
			pieces.put(to, pieces.get(from));
		}
//		else {  //reserved condition for EXIT for future release 
//		}

		// finish moving and switch turn
		pieces.remove(from);
		switchTurn();
		return true;
	}
```

leaving up some spaces for the future update, considering have a way to shrink it down or have a nice way of handling all the false check ins 

Main assignments done, Upload once just in case, future perfection required


## 17-Nov-2020

After carefully reading all of the requirements again, decide to add different coordinate classes just in case (need to do that in future release anyway)

passing a lambda function become useful here, I passed in a [BiFunction](https://stackoverflow.com/questions/28336350/can-bifunction-references-be-passed-to-methods-expecting-a-functional-interface) into the board class,
so that it can make the type of coordinate according to the type

```
	BiFunction<Integer, Integer, C> makeCoord = (a, b) -> makeCoordinate(a, b);
	...
	makeCoord.apply(x,y);
```
		
I also shift our a bunch of boolean statements out of `move()`, so now it is more readable

```
	/**
	 * the False condition contains: starting/end coordinate being null/not on the
	 * board or the end coordinate is BLOCK or occupied by the piece from the
	 * current Player
	 * 
	 * @param from
	 * @param to
	 * @return if the move is valid
	 */
	private boolean checkForValidMove(C from, C to) {
		if (from == null || to == null // param being null
				|| !((EscapeCoordinate) from).isOnBoard(xMax, yMax) // not on the board
				|| !((EscapeCoordinate) to).isOnBoard(xMax, yMax) || blockLocations.contains(to) // target is BLOCK
				|| !pieces.containsKey(from) // no piece at starting position
				|| pieces.get(from).getPlayer() != currentPlayer // moving opponent's piece
				|| (pieces.containsKey(to) && pieces.get(to).getPlayer() == currentPlayer)) {
			// target contain the piece from same side(also covers not moving)
			return false;
		} else {
			return true;
		}
	}
```

Extra catch on not moving any pieces or move opponent's piece, hope that is all

### ---------------------------------------------------------------
### Beta Release

## 26-Nov-2020

Finish up all the modules that I missed because of some other issues going on
Good time to start and spend a lot of time on beta

Good to be noticed that no "unblocked" and "Linear in Triangle"
(Have been looking at the graph in ALPHA and get very confused

First of all, catch everything that was assumed right in ALPHA but not BETA:
* Moving to original location now returns false
* Adapt new configuration file
* Add TriangleCoordinate Class
* Winning Rule: no pieces is considered lose
* Commented out all the unblock/block part


## 27-Nov-2020

Today, I spend a lot of time reading the rules/developer's guide/source code
Understand how `PieceTypeDeciptor.java ` along with `RuleDescriptor.java`

```
boolean jumpable = type.getAttribute(JUMP) != null;
...
int distanceLimit = type.getAttribute(FLY).getValue();
...
type.getMovementType();
```
getAttribute(attributeName) will give your the value of it, or null if it doesn't have the attribute
movmentType is easy

Getting more understanding of the game:
* the game rule decides if board's range, and if allowed the land on some coordinate
* the gameboard sets the ground rule for moving
* on top of it, movementType can move without violates the board's rule
* attribute restrict more (jump) or less(fly) for a piece

that is the layer of decision decided in the order


## 28-Nov-2020

Setting layers of decisions of move in the class hierarchy:
* Game Manager checks if it the game is ended
* Board checks if the exist game rule allows it
* MoveEngine check if a path exists
* EscapeCoordinate class will just give out all possible neighbors

Starting BFS path-find, after consider all legal moves in the board:
create a method return all possible neighbors of a piece:
```

	public ArrayList<EscapeCoordinate> getNeighbours(EscapeCoordinate direction, MovementPattern pattern) {
		ArrayList<EscapeCoordinate> result = new ArrayList<EscapeCoordinate>();

		if (pattern == LINEAR && direction != null) {
			int hCoordinate = getX() + getUnitDirection(direction.getX() - this.getX());
			int vCoordinate = getY() + getUnitDirection(direction.getY() - this.getY());

			SquareCoordinate target = new SquareCoordinate(hCoordinate, vCoordinate);
			result.add(target);
			return result;
		}

		if (pattern == OMNI || pattern == DIAGONAL) {
			result.add(new SquareCoordinate(getX() + 1, getY() + 1));
			result.add(new SquareCoordinate(getX() - 1, getY() - 1));
			result.add(new SquareCoordinate(getX() + 1, getY() - 1));
			result.add(new SquareCoordinate(getX() - 1, getY() + 1));
		}
		if (pattern == OMNI || pattern == ORTHOGONAL) {
			result.add(new SquareCoordinate(getX() + 1, getY()));
			result.add(new SquareCoordinate(getX() - 1, getY()));
			result.add(new SquareCoordinate(getX(), getY() + 1));
			result.add(new SquareCoordinate(getX(), getY() - 1));
		}
		return result;
	}

This might be lengthy but can't think of a way to shorten it


## 30-Nov-2020

After consideration, whether a neighbor is valid(in the broad, not occupied or exit) will be checked in moveEngine

Starting by creating a method that creates the jumping destination:
it also require to provide a direction to jump over, again, moveEngine will valid the coordinates
```

	public EscapeCoordinate getJumpLocation(EscapeCoordinate over) {
		int hCoordinate = over.getX() + getUnitDirection(over.getX() - this.getX());
		int vCoordinate = over.getY() + getUnitDirection(over.getY() - this.getY());
		
		return new SquareCoordinate(hCoordinate, vCoordinate));
	}
	

Starting to build the BFS with very basic loops, long time since last time, feel like I forgot everything:
* check for current List
* check for checked List
* check for not valid locations(jump is painful)
* and forgot to keep track of current distance so I use this

```				
currentNeighbor--;
if (currentNeighbor == 0) { // keep track of distance from origin
	currentDistance++;
	currentNeighbor = currentNextNeighbor;
	currentNextNeighbor = 0;
}
...
currentNextNeighbor += result.size;
```

in this way, the when the currentList run out of coord of distance X, it will update the distance and keep track of coords that are X + 1


## 01-Dec-2020

Well, I just noticed the triangle jump can have 2 dests
gotta fix everything
and the checking for valid neighbor method gets too messy:

```
			if ((!checkClean(i, ((EscapeCoordinate) to))&& !jumpable) 
					// remove it if location occupied and the piece can't jump
					
					|| ((jumpable && !checkClean(jump, ((EscapeCoordinate) to))
							&& (jump2 == null || !checkClean(jump2, ((EscapeCoordinate) to)))))
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
```

The logic almost got me there, but the central idea is clear for the jump part: remove it if jump is not available


## 02-Dec-2020 to 03-Dec-2020

Today: extra game rules stuff, but they all need to be commented out for code coverage since I only have time to start not finish, and the tests of them are saved somewhere else

Triangle's distanceTo() hurts a little, but the table certainly helps

might checked it with more test cases for tomorrow since I am not confident on that
submit a draft and hope I get good luck like the last time











