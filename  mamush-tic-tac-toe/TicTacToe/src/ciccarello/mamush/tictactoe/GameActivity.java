package ciccarello.mamush.tictactoe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Main activity of the TicTacToe game.
 * @author Anthony Ciccarello and Mamush Ciccarello
 *
 */
public class GameActivity extends Activity {

	/** true if it is x's turn to play, if o's false */ 
	private boolean xTurn = false;
	/** 2D array to hold board values */
	private char board[][] = new char[3][3];
	
	/** Count of team X wins */
	private int xScore = 0;
	/** Count of team O wins */
	private int oScore = 0;
	
	/** Array that can hold 3 positions (for knowing where win is) */
	int positions[][] = new int[3][2];
	final static int X_INDEX = 0;
	final static int Y_INDEX = 1;
	
	// Keys for preferences
	private final static String X_KEY = "X_SCORE_KEY";
	private final static String O_KEY = "O_SCORE_KEY";
	private final static int	DEFAULT_SCORE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		// Set up the on click listener
		setupOnClickListeners();
		Button button = (Button) findViewById(R.id.newGameButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newGame();
			}
		});
		
		// Restore score from preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		xScore = prefs.getInt(X_KEY, DEFAULT_SCORE);
		oScore = prefs.getInt(O_KEY, DEFAULT_SCORE);
		updateScore();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Adds "Reset Score Count to menu
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.reset_score:
	            resetScore();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Helper method to get buttons from the buttonLayout
	 * <br /><br />
	 * Takes the following steps:
	 * <ol>
	 *   <li>Get TableLayout object</li>
	 *   <li>Get child at y</li>
	 *   <li>If child is a TableRow</li>
	 *   <li>Get child at x</li>
	 *   <li>If child is a button return view</li>
	 * <ol><br />
	 * 
	 * @param x the horizontal coordinates of the button
	 * @param y the vertical coordinates of the button
	 * @return The element of buttonLayout, null if not a button
	 */
	private Button getButton(int x, int y) {
		TableLayout T = (TableLayout) findViewById(R.id.buttonLayout);
		if(T.getChildAt(y) instanceof TableRow) {
			TableRow R = (TableRow) T.getChildAt(y);
			if(R.getChildAt(x) instanceof Button) {
				return (Button) R.getChildAt(x);
			} else {	// If child is not a Button
				return null;
			}
		} else {		// if child is not a TableRow
			return null;
		}
	}
	
	/**
	 * Used to add OnClickListener to each button inside the TableLayout
	 * 
	 * Goes through each row of the table layout and creates a new
	 * {@link PlayOnClick} for each button
	 */
	private void setupOnClickListeners() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				Button B = getButton(x, y);
				if (B != null) {
					B.setOnClickListener(new PlayOnClick(x, y, this));
				}	// If not null
			}		// For each button in row
		}			// For each row of buttons
	}
	
	/**
	 * Takes the following steps:
	 * <br>
	 * <ol>
	 * <li>Enter selection into board array </li>
	 * <li>Change button text</li>
	 * <li>Disable clicking on the button</li>
	 * <li>Change who's turn it is</li>
	 * <li>Check for win</li>
	 * </ol>
	 * @param x Horizontal position in grid starting at 0
	 * @param y Vertical position in grid starting at 0
	 * @param B The button that was pressed
	 */
	public void clickButton(int x, int y, Button B) {
		String team;
		if (xTurn) {
			team = "X";
		} else {
			team = "O";
		}
		// Enter selection into board array
		board[x][y] = team.charAt(0);
		// Change button text
		B.setText(team);
		
		// Disable clicking on the button
		B.setEnabled(false);
		
		// Change who's turn it is
		xTurn = !xTurn;
		
		checkWin();
	}
	
	/**
	 * Method to check for a win of a certain team.<br />
	 * <br />
	 * Stores the x/y position of three winning buttons in the positions[][] array (or junk data if no win). 
	 * If a team wins, the win count will be updated and displayed
	 * 
	 * @param player Char specifying what team to check for
	 * @return true if team char has three values in a row, column, or diagonal; else false
	 */
	private boolean checkTeamWin(char player) {
		int total;
		// check each column
		for(int x = 0; x < 3; x++ ) {
			total = 0;
			for (int y = 0; y < 3; y++){
				if (board[x][y] == player) {
					positions[total][X_INDEX] = x;
					positions[total][Y_INDEX] = y;
					total++;
				}
			}
			if (total >= 3) {
				return true; // they win
			}
		}
		// check each row
		for (int y = 0; y < 3; y++) {
			total = 0;
			for (int x = 0; x < 3; x++) {
				if (board[x][y] == player) {
					positions[total][X_INDEX] = x;
					positions[total][Y_INDEX] = y;
					total++;
				}
			}
			if (total >= 3) {
				return true; // they win
			
			}
		}
		// forward diagonal
		total = 0;
		for (int i = 0; i < 3; i++) {
			if (board[i][i]== player) {
				positions[total][X_INDEX] = i;
				positions[total][Y_INDEX] = i;
				total++;
			
			}
			
			if (total >= 3) {
				return true; // they win
			}
		}
				
		// backwards diagonal
		total = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x + y == 2 && board[x][y] == player) {
					positions[total][X_INDEX] = x;
					positions[total][Y_INDEX] = y;
					total++;
				}
			}
		}
		if (total >= 3) {
			return true; // they win
		}
		
		
		
		return false;	// nobody won
	}
	
	/**
	 * Method to check if there has been a winning move. <br />
	 * <br />
	 * If there is a win
	 * <ul>
	 * <li>Highlights three winning buttons</li>
	 * <li>Disables all buttons</li>
	 * <li>Updates score</li>
	 * </ul>
	 * 
	 * @return true if a team has won
	 */
	private boolean checkWin() {
		char winner = '\0';
		if (checkTeamWin('X')) {
			winner = 'X';
			xScore++;
		}else if (checkTeamWin('O')) {
			winner = 'O';
			oScore++;
		}
		
		if (winner == '\0') {
			return false;
		} else { // If there is a win
			// Highlight 3 winning buttons
			for (int i = 0; i < 3; i++) {
				getButton(positions[i][X_INDEX], positions[i][Y_INDEX]).setBackgroundResource(R.drawable.button_win);
				
			}
			// Disable all buttons
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 3; y++) {
					getButton(x,y).setEnabled(false);
				}
			}
			updateScore();
			return true;
		}
	}
	
	/**
	 * Called when new game button is clicked. <br>
	 * Performs following actions
	 * <ul>
	 *   <li>Clears (creates new) board</li>
	 *   <li>Resets buttons</li>
	 *   <ul>
	 *     <li>Clears the text</li>
	 *     <li>Enables the button</li>
	 *     <li>Resets the background</li>
	 *   </ul>
	 * </ul>
	 */
	private void newGame() {
		board = new char[3][3];
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				Button b = getButton(x, y);
				b.setEnabled(true);
				b.setText("");
				b.setBackgroundResource(R.drawable.button_default);
			}
		}
	}
	
	/**
	 * Method to update the screen with the scores. Stores score in preferences.
	 */
	void updateScore() {
		Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
		
		// Set xScore
		TextView scoreLabel = (TextView) findViewById(R.id.xScore);
		scoreLabel.setText(Integer.toString(xScore));
		prefEditor.putInt(X_KEY, xScore);
		
		// Set oScore
		scoreLabel = (TextView) findViewById(R.id.oScore);
		scoreLabel.setText(Integer.toString(oScore));
		prefEditor.putInt(O_KEY, oScore);
		
		prefEditor.commit();
	}
	
	/**
	 * Clears the scores of the two teams and updates score on screen.
	 */
	private void resetScore() {
		xScore = 0;
		oScore = 0;
		updateScore();
	}
}

