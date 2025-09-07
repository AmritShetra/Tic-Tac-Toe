package tictactoe;

import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Board implements EventHandler<ActionEvent> {

	private static Button[][] buttons = new Button[3][3];
	private static boolean yourTurn, youAreX;
	private enum playerState {
		X,
		O
	}
	private static playerState player;
	private enum gameState {
		WIN,
		LOSE,
		DRAW
	}
	private static gameState result;
	private static int yourScore, computerScore;

	void initialiseBoard(GridPane grid) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				buttons[x][y] = new Button("");
				buttons[x][y].setPrefSize(150, 150);
				buttons[x][y].setOnAction(this);
				grid.add(buttons[x][y], x, y);
				grid.setAlignment(Pos.CENTER);
			}
		}
		App.resetGame.setOnAction(this);
		determineTurn();
	}

	private int randomNumber() {
		return new Random().nextInt(2) + 1;
	}

	private void determineTurn() {
		int rand = randomNumber();
		youAreX = rand == 1;
		yourTurn = rand == 1;
		if (rand == 1) { //Setting the player to "X" or "O"
			player = playerState.X;
			App.setStatus("Your turn - You are " + player);
		}
		else
		{
			player = playerState.O;
			App.setStatus("Computer's turn - You are " + player);
			nextRound();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (event.getSource() == buttons[x][y]) {
					if (yourTurn) {
						buttons[x][y].setText(player.toString());
					}

					buttons[x][y].setDisable(true); //Disables the button so it cannot be clicked again

					winCheck();
					if (winCheck()) { //Checks if you have won the game
						result = gameState.WIN;
						endGame();
					}
					else {
						drawCheck(); //You have not won the game but there might be a draw
						if (drawCheck()) {
							result = gameState.DRAW;
							endGame();
						}
						else {
							yourTurn = false; //Indicate the next turn is the computer's
							nextRound();
						}
					}
				}
			}
		}

		if (event.getSource() == App.resetGame) {
			resetGame(App.getStatus());
		}
	}

	private boolean winCheck() {
		// Top horizontal
		if (!buttons[0][0].getText().equals("") &&
				buttons[0][0].getText().equals(buttons[0][1].getText()) &&
				buttons[0][1].getText().equals(buttons[0][2].getText())
		) {
			highlightButtons(buttons[0][0], buttons[0][1], buttons[0][2]);
			return true;
		}

		//Middle horizontal
		if (!buttons[1][0].getText().equals("")
				&& buttons[1][0].getText().equals(buttons[1][1].getText())
				&& buttons[1][1].getText().equals(buttons[1][2].getText())
		) {
			highlightButtons(buttons[1][0], buttons[1][1], buttons[1][2]);
			return true;
		}

		//Bottom horizontal
		if (!buttons[2][0].getText().equals("")
				&& buttons[2][0].getText().equals(buttons[2][1].getText())
				&& buttons[2][1].getText().equals(buttons[2][2].getText())
		) {
			highlightButtons(buttons[2][0], buttons[2][1], buttons[2][2]);
			return true;
		}

		//Left vertical
		if (!buttons[0][0].getText().equals("")
				&& buttons[0][0].getText().equals(buttons[1][0].getText())
				&& buttons[1][0].getText().equals(buttons[2][0].getText())
		) {
			highlightButtons(buttons[0][0], buttons[1][0], buttons[2][0]);
			return true;
		}

		//Middle vertical
		if (!buttons[0][1].getText().equals("")
				&& buttons[0][1].getText().equals(buttons[1][1].getText())
				&& buttons[1][1].getText().equals(buttons[2][1].getText())
		) {
			highlightButtons(buttons[0][1], buttons[1][1], buttons[2][1]);
			return true;
		}

		//Right vertical
		if (!buttons[0][2].getText().equals("")
				&& buttons[0][2].getText().equals(buttons[1][2].getText())
				&& buttons[1][2].getText().equals(buttons[2][2].getText())
		) {
			highlightButtons(buttons[0][2], buttons[1][2], buttons[2][2]);
			return true;
		}

		//Left-to-right diagonal
		if (!buttons[0][0].getText().equals("")
				&& buttons[0][0].getText().equals(buttons[1][1].getText())
				&& buttons[1][1].getText().equals(buttons[2][2].getText())
		) {
			highlightButtons(buttons[0][0], buttons[1][1], buttons[2][2]);
			return true;
		}

		//Right-to-left diagonal
		if (!buttons[0][2].getText().equals("")
				&& buttons[0][2].getText().equals(buttons[1][1].getText())
				&& buttons[1][1].getText().equals(buttons[2][0].getText())
		) {
			highlightButtons(buttons[0][2], buttons[1][1], buttons[2][0]);
			return true;
		}

		return false;
	}

	private void highlightButtons(Button button, Button button2, Button button3) {
		button.getStyleClass().add("highlight-button");
		button2.getStyleClass().add("highlight-button");
		button3.getStyleClass().add("highlight-button");
	}

	private void resetGame(Label status) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				buttons[x][y].setDisable(false);
				buttons[x][y].setText("");
				buttons[x][y].getStyleClass().clear();
				buttons[x][y].getStyleClass().add("button");
			}
		}

		if (yourTurn) {
			status.setText("Your turn - You are " + player);
		}
		else {
			nextRound(); //If it's not your turn, let the Computer go first
		}
	}

	private boolean drawCheck() {
		int disabledButtons = 0; //Used to count how many buttons have been used already
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (buttons[x][y].isDisabled()) {
					disabledButtons++;
				}
			}
		}
		//There are 9 available squares, so a draw has taken place if they have all been used
		return disabledButtons == 9;
	}

	private void nextRound() {
		App.setStatus("Computer's turn");
		boolean completedMove = false;

		//Computer will aim to make a move in the center square, if available
		if (buttons[1][1].getText().equals("")) {
			if (youAreX) {
				buttons[1][1].setText("O");
			}
			else {
				buttons[1][1].setText("X");
			}
			buttons[1][1].setDisable(true);
			completedMove = true;
		}

		//If not, go through each square and place an icon in the first available square
		for (int x = 0; x < 3 && !completedMove; x++) {
			for (int y = 0; y < 3; y++) {
				if (buttons[x][y].getText().equals("")) {
					if (youAreX) { //User is "X" so the Computer must be "O"
						buttons[x][y].setText("O");
					}
					else {
						buttons[x][y].setText("X");
					}
					buttons[x][y].setDisable(true); //Disables the button so it cannot be clicked again
					completedMove = true;
					break;
				}
			}
		}

		if (winCheck()) { //Checks if the Computer has won the game
			result = gameState.LOSE;
			endGame();
		}
		else {
			drawCheck(); //The Computer might not have won the game but there might be a draw
			if (drawCheck()) {
				result = gameState.DRAW;
				endGame();
			}
			else {
				yourTurn = true; //Indicates the next turn is yours
				App.setStatus("Your turn - You are " + player);
			}
		}
	}

	private void endGame() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				buttons[x][y].setDisable(true); //Disable all the remaining buttons as the game is over
			}
		}

		//Check to see who has won the game and set the text accordingly
		if (result == gameState.WIN) {
			App.setStatus("You win! Congratulations!");
			yourScore++;
		}
		else if (result == gameState.LOSE) {
			App.setStatus("You lose... Better luck next time!");
			computerScore++;
		}
		else if (result == gameState.DRAW) {
			App.setStatus("It's a draw!");
		}

		App.setScores("You " + yourScore + "-" + computerScore + " Computer");
	}
}