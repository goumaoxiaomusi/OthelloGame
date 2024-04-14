package ss.othello.game;

import java.util.*;

public class Board {

	/**
	 * Board of the Othello Game
	 */

	public static final int DIM = 8;

	public static final String DELIM = "    ";

	private static final String[] NUMBERING = {
			"  0 |  1 |  2 |  3 |  4 |  5 |  6 |  7 ",
			"----+----+----+----+----+----+----+----",
			"  8 |  9 | 10 | 11 | 12 | 13 | 14 | 15 ",
			"----+----+----+----+----+----+----+----",
			" 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 ",
			"----+----+----+----+----+----+----+----",
			" 24 | 25 | 26 | 27 | 28 | 29 | 30 | 31 ",
			"----+----+----+----+----+----+----+----",
			" 32 | 33 | 34 | 35 | 36 | 37 | 38 | 39 ",
			"----+----+----+----+----+----+----+----",
			" 40 | 41 | 42 | 43 | 44 | 45 | 46 | 47 ",
			"----+----+----+----+----+----+----+----",
			" 48 | 49 | 50 | 51 | 52 | 53 | 54 | 55 ",
			"----+----+----+----+----+----+----+----",
			" 56 | 57 | 58 | 59 | 60 | 61 | 62 | 63 ",
			"----+----+----+----+----+----+----+----",};
	private static final String LINE = NUMBERING[1];


	private List<Mark> fields;


	/**
	 * The constructor of the game.At the beginning of a game, a board will be created.4 positions: 27,36,28,and 35
	 * will be filled with marks. And the hashMap that is used to convert coordinate into index is created.
	 */
	//@ ensures (\forall int i; (i >= 0 && i < DIM*DIM); fields.get(i) == Mark.EMPTY);
	public Board() {
		this.fields = new ArrayList<>(64);
		for (int i = 0; i < DIM * DIM; i++) {
			this.fields.add(Mark.EMPTY);
		}
		setField(27, Mark.OO);
		setField(36, Mark.OO);
		setField(28, Mark.XX);
		setField(35, Mark.XX);
	}


	/**
	 * Convert index of a field into form of a pair
	 *
	 * @param index
	 * @return a pair that represents the index, on the first position of this array is the row;
	 * the second position is the col;
	 */
	public int[] pair(int index) {
		int row = index / 8;
		int col = index - row * 8;
		int[] pair = new int[2];
		pair[0] = row;
		pair[1] = col;
		return pair;
	}

	/**
	 * Convert a pair into an index on the board
	 *
	 * @param i row
	 * @param j column
	 * @return
	 */
	public int index(int i, int j) {
		return i * DIM + j;
	}


	/**
	 * Returns true if an index is a valid index on the board.
	 *
	 * @return true if 0 <= index < DIM*DIM
	 */
	//@ ensures index >= 0 && index < DIM*DIM ==> \result == true;
	public boolean isField(int index) {
		if (index < (DIM * DIM) && index >= 0) {
			return true;
		}
		return false;
	}


	/**
	 * Get the Mark on the field that the index refers to
	 *
	 * @param i index of the board fields
	 * @return Mark
	 */
	/*@ requires isField(i);
    ensures \result == Mark.EMPTY || \result == Mark.OO || \result == Mark.XX;
     @*/
	public Mark getField(int i) {
		return fields.get(i);
	}

	/**
	 * Get the Mark on the field that expressed in pairs
	 *
	 * @param i row
	 * @param j column
	 * @return Mark of that field
	 */
	public Mark getField(int i, int j) {
		return fields.get(i * 8 + j);
	}


	/**
	 * Check if a field is an empty field by using its index
	 *
	 * @param i index on the board
	 * @return true if the field is empty; false if it is not
	 */
	/*@ requires isField(i);
    ensures getField(i) == Mark.EMPTY ==> \result == true;
     @*/
	public boolean isEmptyField(int i) {
		if (getField(i) == Mark.EMPTY) {
			return true;
		}
		return false;
	}


	/**
	 * Tests if the whole board is full.
	 *
	 * @return true if all fields are occupied
	 */
	//@ ensures (\forall int i; (i >= 0 && i < DIM*DIM); getField(i) == Mark.XX || getField(i) == Mark.OO);
	public boolean isFull() {
		int counter = 0;
		for (int i = 0; i < fields.size(); i++) {
			if (isEmptyField(i) == true) {
				counter += 1;
			}
		}
		if (counter != 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Empties all fields of this board (i.e., let them refer to the value
	 * Mark. EMPTY).
	 */
	//@ ensures (\forall int i; (i >= 0 && i < DIM*DIM); getField(i) == Mark.EMPTY);
	public void reset() {
		for (int i = 0; i < Board.DIM * Board.DIM; i++) {
			if (i == 27 || i == 36 || i == 28 || i == 35) {
				continue;
			}
			setField(i, Mark.EMPTY);
		}
		setField(27, Mark.OO);
		setField(36, Mark.OO);
		setField(28, Mark.XX);
		setField(35, Mark.XX);
	}

	/**
	 * Sets the content of field i to the mark m.
	 *
	 * @param i the field number (see NUMBERING)
	 * @param m the mark to be placed
	 */
	/*@ requires isField(i);
    ensures getField(i) == m;
     @*/
	public void setField(int i, Mark m) {

		fields.set(i, m);
	}


	/**
	 * Get the fields on the board
	 *
	 * @return
	 */
	public List<Mark> getFields() {
		return fields;
	}


	// Creates a copy of the existing board.
	public Board deepCopy() {
		Board board = new Board();
		for (int i = 0; i < DIM * DIM; i++) {
			board.setField(i, this.getField(i));
		}
		return board;
	}

	/**
	 * check if the board is empty;
	 *
	 * @return
	 */
	public boolean isEmpty() {
		int counter = 0;
		for (Mark mark : fields) {
			if (!(mark == Mark.EMPTY)) {
				counter += 1;
			}
		}
		if (counter != 0) {
			return false;
		}
		return true;
	}

	/**
	 * Obtain a list of empty fields on the baord
	 *
	 * @return
	 */
	public List<Integer> emptyFields() {
		List<Integer> emptyFields = new ArrayList<>();
		//i is the field index that is tested;
		for (int i = 0; i < (DIM * DIM); i++) {
            /*if the field to the right of field i has the opposite mark and it is not empty, we can continue to check
            if it is a valid move
            */
			if (isEmptyField(i)) {
				emptyFields.add(i);
			}
		}
		return emptyFields;
	}


	/**
	 * Check and get all possible move of a mark in a row from the left to right
	 *
	 * @param mark that desire to be checked
	 * @return a list of possible moves in this row for this mark
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer a; 0 < a && a <\result.size();  isField(\result.get(a)));
    ensures (\forall Integer a; 0 < a && a <\result.size();  isEmptyField(\result.get(a)));
    */
	public List<Integer> checkRowFromLeft(Mark mark) {
		List<Integer> possibleMoves = new ArrayList<>();

		List<Integer> emptyFields = emptyFields();

		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int nextCol = thisCol + 1;

			//if the thisRow is greater than 5 or the previous thisRow is empty or the previous thisRow has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (thisCol > DIM - 3 || isEmptyField(index(thisRow, nextCol)) || getField(index(thisRow, nextCol)) == mark) {
				continue;
			}

            /*starting from the empty field that is being checked, check every field from left to right. If the next
            field's mark is the opposite of mark being checked, continue checking; if no, break this loop and move on
            to the next empty field on the board; if yes, check the next next field until hit the field that has the
            mark being checked or reach the border of the row.
             */
			for (; nextCol < DIM; nextCol++) {

				if (getField(index(thisRow, nextCol)) != mark && !(isEmptyField(index(thisRow, nextCol)))) {
					counter += 1;
					if (!(isField(index(thisRow, nextCol + 1)))) {
						break;
					}
					if (isEmptyField(index(thisRow, nextCol + 1))) {
						break;
					}
					continue;
				}

				if (getField(index(thisRow, nextCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;

	}

	/**
	 * Check and get all possible move of a mark in a row from the right to left
	 *
	 * @param mark that desire to be checked
	 * @return a list of possible moves in this row for this mark
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer a; 0 < a && a <\result.size();  isField(\result.get(a)));
    ensures (\forall Integer a; 0 < a && a <\result.size();  isEmptyField(\result.get(a)));
    */
	public List<Integer> checkRowFromRight(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();

		List<Integer> emptyFields = emptyFields();


		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int previousCol = thisCol - 1;

			//if the thisCol is smaller than 2 or the previous thisRow is empty or the previous thisCol has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (thisCol < 2 || isEmptyField(index(thisRow, previousCol)) || getField(index(thisRow, previousCol)) == mark) {
				continue;
			}

			for (; previousCol >= 0; previousCol--) {

				if (getField(index(thisRow, previousCol)) != mark && !(isEmptyField(index(thisRow, previousCol)))) {
					counter += 1;
					if (!(isField(index(thisRow, previousCol - 1)))) {
						break;
					}
					if (isEmptyField(index(thisRow, previousCol - 1))) {
						break;
					}
					continue;
				}

				if (getField(index(thisRow, previousCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}

		return possibleMoves;
	}

	/**
	 * Check and obtain valid moves in a column from up to down
	 *
	 * @param mark that is checked
	 * @return a list of valid moves
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer index; 0 < index && index <DIM*DIM;  isField(\result.get(index)));
    */
	public List<Integer> checkDown(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();

		List<Integer> emptyFields = emptyFields();


		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int nextRow = thisRow + 1;

			//if the thisRow is greater than 5 or the previous thisRow is empty or the previous thisRow has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (thisRow > DIM - 3 || isEmptyField(index(nextRow, thisCol)) || getField(index(nextRow, thisCol)) == mark) {
				continue;
			}

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; nextRow < DIM; nextRow++) {

				if (getField(index(nextRow, thisCol)) != mark && !(isEmptyField(index(nextRow, thisCol)))) {
					counter += 1;
					if (!(isField(index(nextRow + 1, thisCol)))) {
						break;
					}
					if (isEmptyField(index(nextRow + 1, thisCol))) {
						break;
					}
					continue;
				}

				if (getField(index(nextRow, thisCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;
	}


	/**
	 * Check and obtain valid moves in a column from up to down
	 *
	 * @param mark that is checked
	 * @return a list of valid moves
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer index; 0 < index && index <DIM*DIM;  isField(\result.get(index)));
    */
	public List<Integer> checkUp(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();


		List<Integer> emptyFields = emptyFields();


		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);
			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int previousRow = thisRow - 1;

			//if the thisRow is smalle than 2 or the previous thisRow is empty or the previous thisRow has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (thisRow < 2 || isEmptyField(index(previousRow, thisCol)) || getField(index(previousRow, thisCol)) == mark) {
				continue;
			}

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; previousRow >= 0; previousRow--) {

				if (getField(index(previousRow, thisCol)) != mark && !(isEmptyField(index(previousRow, thisCol)))) {
					counter += 1;
					if (!(isField(index(previousRow - 1, thisCol)))) {
						break;
					}
					if (isEmptyField(index(previousRow - 1, thisCol))) {
						break;
					}
					continue;
				}

				if (getField(index(previousRow, thisCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;
	}


	/**
	 * This method check and obtain valid moves in all diagonal of the left part of the board in the form of list
	 *
	 * @param mark that is checking
	 * @return a list of valid moves
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer index; 0 < index && index <DIM*DIM;  isField(\result.get(index)));
    */
	public List<Integer> checkLeftDiagonalDown(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();


		List<Integer> emptyFields = emptyFields();


		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int nextRow = thisRow + 1;

			int nextCol = thisCol + 1;

			//if the thisRow is smalle than 2 or the previous thisRow is empty or the previous thisRow has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (thisRow > DIM - 3 || isEmptyField(index(nextRow, nextCol)) || getField(index(nextRow, nextCol)) == mark) {
				continue;
			}

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; nextRow < DIM; nextRow++) {

				if (getField(index(nextRow, nextCol)) != mark && !(isEmptyField(index(nextRow, nextCol)))) {
					counter += 1;
					if (!(isField(index(nextRow + 1, nextCol + 1)))) {
						break;
					}
					if (isEmptyField(index(nextRow + 1, nextCol + 1))) {
						break;
					}
					nextCol += 1;
					continue;
				}

				if (getField(index(nextRow, nextCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;

	}

	//TODO

	/**
	 * This method check and obtain valid moves in all diagonal of the left part of the board in the form of list from
	 * down to up
	 *
	 * @param mark is checked
	 * @return a list of possible valid moves
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer index; 0 < index && index <DIM*DIM;  isField(\result.get(index)));
    */
	public List<Integer> checkLeftDiagonalUp(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();


		List<Integer> emptyFields = emptyFields();


		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int previousRow = thisRow - 1;

			int previousCol = thisCol - 1;

			//if the thisRow is smalle than 2 or the previous thisRow is empty or the previous thisRow has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (previousRow <= 0 || previousCol < 0 || isEmptyField(index(previousRow, previousCol)) || getField(index(previousRow, previousCol)) == mark) {
				continue;
			}

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; previousRow >= 0; previousRow--) {

				if (getField(index(previousRow, previousCol)) != mark && !(isEmptyField(index(previousRow, previousCol)))) {
					counter += 1;
					if (!(isField(index(previousRow - 1, previousCol - 1)))) {
						break;
					}
					if (isEmptyField(index(previousRow - 1, previousCol - 1))) {
						break;
					}
					previousCol -= 1;
					continue;
				}

				if (getField(index(previousRow, previousCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;


	}


	/**
	 * This method check and obtain a list of possible valid moves in a diagonal on the right part of the board
	 *
	 * @param mark that is checked
	 * @return a list of possible valid moves
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer index; 0 < index && index <DIM*DIM;  isField(\result.get(index)));
    */
	public List<Integer> checkRightDiagonalDown(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();


		List<Integer> emptyFields = emptyFields();


		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int nextRow = thisRow + 1;

			int nextCol = thisCol - 1;

			//if the thisRow is smalle than 2 or the previous thisRow is empty or the previous thisRow has the same mark as the
			//intended to be checked thisRow, skip this round and continue looping
			if (thisRow > DIM - 3 || thisCol < 2 || isEmptyField(index(nextRow, nextCol)) || getField(index(nextRow, nextCol)) == mark) {
				continue;
			}

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; nextRow < DIM; nextRow++) {

				if (getField(index(nextRow, nextCol)) != mark && !(isEmptyField(index(nextRow, nextCol)))) {
					counter += 1;

					if (!(isField(index(nextRow + 1, nextCol - 1)))) {
						break;
					}
					if (isEmptyField(index(nextRow + 1, nextCol - 1))) {
						break;
					}
					nextCol -= 1;
					continue;
				}


				if (getField(index(nextRow, nextCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;

	}


	/**
	 * This method obtain all possible valid moves of diagonals on the right part of the board, checking from down to up
	 *
	 * @param mark
	 * @return a list of possible moves in diagonals
	 */
	/*@requires mark == Mark.OO || mark == Mark.XX;
    requires mark != null;
    ensures (\forall Integer index; 0 < index && index <DIM*DIM;  isField(\result.get(index)));
    */
	public List<Integer> checkRightDiagonalUp(Mark mark) {

		List<Integer> possibleMoves = new ArrayList<>();


		List<Integer> emptyFields = emptyFields();

		for (int j = 0; j < emptyFields.size(); j++) {

			int index = emptyFields.get(j);

			int[] pair = pair(index);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int counter = 0;

			int previousRow = thisRow - 1;

			int previousCol = thisCol + 1;


			if (thisRow < 1 || thisCol > DIM - 3 || isEmptyField(index(previousRow, previousCol)) || getField(index(previousRow, previousCol)) == mark) {
				continue;
			}

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; previousCol < DIM; previousCol++) {

				if (getField(index(previousRow, previousCol)) != mark && !(isEmptyField(index(previousRow, previousCol)))) {
					counter += 1;
					if (!(isField(index(previousRow - 1, previousCol + 1)))) {
						break;
					}
					if (isEmptyField(index(previousRow - 1, previousCol + 1))) {
						break;
					}
					previousRow -= 1;
					continue;
				}

				if (getField(index(previousRow, previousCol)) == mark && counter != 0) {
					possibleMoves.add(index(thisRow, thisCol));
					break;
				}
			}
		}


		return possibleMoves;

	}


	/**
	 * Returns a String representation of this board. In addition to the current
	 * situation, the String also shows the numbering of the fields.
	 *
	 * @return the game situation as String
	 */
	public String toString() {
		String s = "";
		for (int i = 0; i < DIM; i++) {
			String row = "";
			for (int j = 0; j < DIM; j++) {
				row += "  " + getField(i, j).toString().substring(0, 1).replace("E", " ") + " ";
				if (j < DIM - 1) {
					row = row + "|";
				}
			}
			s = s + row + DELIM + NUMBERING[i * 2];
			if (i < DIM - 1) {
				s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
			}
		}
		return s;
	}


	/**
	 * This method count the score of a player.
	 *
	 * @param mark represents a player
	 * @return the score of the player
	 */
	public int countScore(Mark mark) {
		int score = 0;
		for (int i = 0; i < DIM * DIM; i++) {
			if (getField(i) == mark) {
				score += 1;
			}
		}
		return score;
	}

	/**
	 * This method determines whether a player is a winner. If the score of the opponents is greater than or equal to
	 * the score of this player, return fasle.If all the mark on the board belongs to opponent.This player lose.
	 *
	 * @param mark
	 * @return true if the player is a winner; false if the player is not a winner
	 */
	/*requires mark == Mark.XX || mark == Mark.OO;
	 */
	public boolean isWinner(Mark mark) {
		int playerScore = countScore(mark);

		Mark[] allMarks = {Mark.OO, Mark.XX};
		Mark opponentMark;
		if (allMarks[0] == mark) {
			opponentMark = allMarks[1];
		} else {
			opponentMark = allMarks[0];
		}

		int opponentScore = countScore(opponentMark);
		if (opponentScore >= playerScore) {
			return false;
		}
		return true;
	}


	public void setFields(List<Mark> fields) {
		this.fields = fields;
	}

	/**
	 * This method flip discs between a mark by checking if the index that is intended to be filled by the mark has opposite discs between it.
	 */
	public void flipDisc(Move move) {

		int moveIndex = move.getIndex();
		Mark moveMark = move.getMark();


		if (checkRightDiagonalDown(moveMark).contains(moveIndex)) {
			int[] pair = pair(moveIndex);

			int thisRow = pair[0];

			int thisCol = pair[1];

			int nextRow = thisRow + 1;

			int nextCol = thisCol - 1;


			for (; nextRow < DIM; nextRow++) {

				if (getField(index(nextRow, nextCol)) != moveMark &&
						!(isEmptyField(index(nextRow, nextCol)))) {

					setField(index(nextRow, nextCol), moveMark);
					nextCol -= 1;
					continue;
				}

				if (getField(index(nextRow, nextCol)) == moveMark) {
					break;
				}
			}
		}


		if (checkRightDiagonalUp(moveMark).contains(moveIndex)) {

			int[] pair = pair(moveIndex);


			int thisRow = pair[0];


			int thisCol = pair[1];


			int previousRow = thisRow - 1;

			int previousCol = thisCol + 1;


			for (; previousRow >= 0; previousRow--) {

				if (getField(index(previousRow, previousCol)) != moveMark &&
						!(isEmptyField(index(previousRow, previousCol)))) {
					setField(index(previousRow, previousCol), moveMark);
					previousCol += 1;
					continue;
				}

				if (getField(index(previousRow, previousCol)) == moveMark) {
					break;
				}
			}

		}


		if (checkLeftDiagonalUp(moveMark).contains(moveIndex)) {

			int[] pair = pair(moveIndex);


			int thisRow = pair[0];


			int thisCol = pair[1];


			int previousRow = thisRow - 1;

			int previousCol = thisCol - 1;


			for (; previousRow >= 0; previousRow--) {

				if (getField(index(previousRow, previousCol)) != moveMark && !(isEmptyField(index(previousRow, previousCol)))) {
					setField(index(previousRow, previousCol), moveMark);
					previousCol -= 1;
					continue;
				}

				if (getField(index(previousRow, previousCol)) == moveMark) {
					break;
				}
			}

		}


		if (checkLeftDiagonalDown(moveMark).contains(moveIndex)) {
			//convert the index into the form of pair
			int[] pair = pair(moveIndex);

			//get the thisRow number of this index
			int thisRow = pair[0];

			//get the column number of ths index
			int thisCol = pair[1];

			//the next thisRow can be expressed as below
			int nextRow = thisRow + 1;
			//the next column can be expressed as below
			int nextCol = thisCol + 1;

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; nextRow < DIM; nextRow++) {

				if (getField(index(nextRow, nextCol)) != moveMark && getField(index(nextRow, nextCol)) != Mark.EMPTY) {
					setField(index(nextRow, nextCol), moveMark);
					nextCol += 1;
					continue;
				}

				if (getField(index(nextRow, nextCol)) == moveMark) {
					break;
				}
			}
		}

		if (checkUp(moveMark).contains(moveIndex)) {
			//convert the index into the form of pair
			int[] pair = pair(moveIndex);

			//get the thisRow number of this index
			int thisRow = pair[0];

			//get the column number of ths index
			int thisCol = pair[1];

			//the next thisRow can be expressed as below
			int previousRow = thisRow - 1;

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; previousRow >= 0; previousRow--) {

				if (getField(index(previousRow, thisCol)) != moveMark) {
					setField(index(previousRow, thisCol), moveMark);
					continue;
				}

				if (getField(index(previousRow, thisCol)) == moveMark) {
					break;
				}
			}
		}


		if (checkDown(moveMark).contains(moveIndex)) {
			//convert the index into the form of pair
			int[] pair = pair(moveIndex);

			//get the thisRow number of this index
			int thisRow = pair[0];

			//get the column number of ths index
			int thisCol = pair[1];

			//the next thisRow can be expressed as below
			int nextRow = thisRow + 1;

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; nextRow < DIM; nextRow++) {

				if (getField(index(nextRow, thisCol)) != moveMark) {
					setField(index(nextRow, thisCol), moveMark);
					continue;
				}

				if (getField(index(nextRow, thisCol)) == moveMark) {
					break;
				}
			}
		}


		if (checkRowFromRight(moveMark).contains(moveIndex)) {
			//convert the index into the form of pair
			int[] pair = pair(moveIndex);

			//get the thisRow number of this index
			int thisRow = pair[0];

			//get the column number of ths index
			int thisCol = pair[1];

			//the next column can be expressed as below
			int previousCol = thisCol - 1;

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; previousCol >= 0; previousCol--) {

				if (getField(index(thisRow, previousCol)) != moveMark) {
					setField(index(thisRow, previousCol), moveMark);
					continue;
				}

				if (getField(index(thisRow, previousCol)) == moveMark) {
					break;
				}
			}

		}


		if (checkRowFromLeft(moveMark).contains(moveIndex)) {
			//convert the index into the form of pair
			int[] pair = pair(moveIndex);

			//get the thisRow number of this index
			int thisRow = pair[0];

			//get the column number of ths index
			int thisCol = pair[1];

			//the next column can be expressed as below
			int nextCol = thisCol + 1;

			//if the previous thisRow in this column is empty, skip and move on to the next thisRow
			for (; nextCol < DIM; nextCol++) {

				if (getField(index(thisRow, nextCol)) != moveMark) {
					setField(index(thisRow, nextCol), moveMark);
					continue;
				}

				if (getField(index(thisRow, nextCol)) == moveMark) {
					break;
				}
			}
		}


	}


}
