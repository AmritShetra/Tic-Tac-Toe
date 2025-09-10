package tictactoe;

import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


public class Board implements EventHandler<ActionEvent> {

	private static Button[] buttons = new Button[9];
	private static boolean yourTurn;
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
		int buttonNo = 0;
		for (int x = 0; x< 3; x++) {
            for (int y = 0; y < 3; y++) {
				buttons[buttonNo] = new Button("");
				buttons[buttonNo].setPrefSize(150, 150);
				buttons[buttonNo].setOnAction(this);
                grid.add(buttons[buttonNo], x, y);
				buttonNo++;
            }
        }
		grid.setAlignment(Pos.CENTER);
		App.resetGame.setOnAction(this);
		determineTurn();
	}

	private void determineTurn() {
		int rand = randomNumber();
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

	// Generate number between 0 and 1
	private int randomNumber() {
		return new Random().nextInt(2) + 1;
	}

	@Override
	public void handle(ActionEvent event) {
		for (int x = 0; x < 9; x++){
			if (event.getSource() == buttons[x] && yourTurn) {
				buttons[x].setText(player.toString());
				buttons[x].setDisable(true);

				if (winCheck()) {
					result = gameState.WIN;
					endGame();
				} 
				else {
					if (drawCheck()) {
						result = gameState.DRAW;
						endGame();
					}
					else {
						yourTurn = false;
						nextRound();
					}
				}
			}
		}

		if (event.getSource() == App.resetGame) {
			resetGame(App.getStatus());
		}
	}

	private boolean winCheck() {
		// 0 1 2
		// 3 4 5
		// 6 7 8

		String[] buttonsText = new String[buttons.length];
		for (int i = 0; i < buttons.length; i++) {
			buttonsText[i] = buttons[i].getText();
		}
		
		// Top horizontal
		if (checkButtons(buttonsText[0], buttonsText[1], buttonsText[2])) {
			highlightButtons(buttons[0], buttons[1], buttons[2]);
			return true;
		}

		// Middle horizontal
		if (checkButtons(buttonsText[3], buttonsText[4], buttonsText[5])) {
			highlightButtons(buttons[3], buttons[4], buttons[5]);
			return true;
		}

		//Bottom horizontal
		if (checkButtons(buttonsText[6], buttonsText[7], buttonsText[8])) {
			highlightButtons(buttons[6], buttons[7], buttons[8]);
			return true;
		}

		//Left vertical
		if (checkButtons(buttonsText[0], buttonsText[3], buttonsText[6])) {
			highlightButtons(buttons[0], buttons[3], buttons[6]);
			return true;
		}

		//Middle vertical
		if (checkButtons(buttonsText[1], buttonsText[4], buttonsText[7])) {
			highlightButtons(buttons[1], buttons[4], buttons[7]);
			return true;
		}

		//Right vertical
		if (checkButtons(buttonsText[2], buttonsText[5], buttonsText[8])) {
			highlightButtons(buttons[2], buttons[5], buttons[8]);
			return true;
		}

		//Left-to-right diagonal
		if (checkButtons(buttonsText[0], buttonsText[4], buttonsText[8])) {
			highlightButtons(buttons[0], buttons[4], buttons[8]);
			return true;
		}

		//Right-to-left diagonal
		if (checkButtons(buttonsText[2], buttonsText[4], buttonsText[6])) {
			highlightButtons(buttons[2], buttons[4], buttons[6]);
			return true;
		}

		return false;
	}

	private Boolean checkButtons(String buttonOne, String buttonTwo, String buttonThree) {
		if (buttonOne.equals("")) {
			return false;
		}

		if (buttonOne.equals(buttonTwo) && buttonTwo.equals(buttonThree)) {
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
		for (int x = 0; x < 9; x++) {
				buttons[x].setDisable(false);
				buttons[x].setText("");
				buttons[x].getStyleClass().clear();
				buttons[x].getStyleClass().add("button");
		}

		if (yourTurn) {
			status.setText("Your turn - You are " + player);
		}
		else {
			nextRound(); //If it's not your turn, let the Computer go first
		}
	}

	private boolean drawCheck() {
		int disabledButtons = 0;
		for (int x = 0; x < 9; x++) {
			if (buttons[x].isDisabled()) {
				disabledButtons++;
			}
		}

		return disabledButtons == 9;
	}

	private void nextRound() {
		App.setStatus("Computer's turn");
		boolean completedMove = false;

		if (buttons[4].getText().equals("")) {
			if (player == playerState.X) {
				buttons[4].setText("O");
			}
			else {
				buttons[4].setText("X");
			}
			buttons[4].setDisable(true);
			completedMove = true;
		}

		for (int x = 0; x < 9; x++) {
			if (completedMove) {
				break;
			}

			if (buttons[x].getText().equals("")) {
				if (player == playerState.X) {
					buttons[x].setText("O");
				}
				else {
					buttons[x].setText("X");
				}
				buttons[x].setDisable(true);
				completedMove = true;
				break;
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
		for (int x = 0; x < 9; x++) {
				buttons[x].setDisable(true);
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