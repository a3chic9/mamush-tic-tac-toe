package ciccarello.mamush.tictactoe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * OnClick event listener for when a button is clicked
 * @author Anthony Ciccarello and Mamush Ciccarello
 *
 */
public class PlayOnClick implements OnClickListener {
	/** Variable to hold the x position of this button */
	private int x;
	/** Variable to hold the y position of this button */
	private int y;
	private GameActivity activity;
	
	/**
	 * gets the position of the button on construction
	 * @param gameActivity 
	 */
	public PlayOnClick(int x, int y, GameActivity gameActivity) {
		this.x = x;
		this.y = y;
		this.activity = gameActivity;
	}

	/**
	 * Play a button. 
	 * 
	 * @param v The button view to click 
	 */
	@Override
	public void onClick(View v) {
		if(v instanceof Button) {
			activity.clickButton(x, y, (Button) v);

		}
		
	}
	
}