package tictactoe;

import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Board implements EventHandler<ActionEvent> {

	private static Button[] buttons2 = new Button[9];

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

		for (int x = 0; x < 9; x++) {
			buttons2[x] = new Button("");
			buttons2[x].setPrefSize(150, 150);
			buttons2[x].setOnAction(this);
		}

		HBox firstRow = new HBox();
		firstRow.getChildren().addAll(buttons2[0], buttons2[1], buttons2[2]);
		HBox secondRow = new HBox();
		secondRow.getChildren().addAll(buttons2[3], buttons2[4], buttons2[5]);
		HBox thirdRow = new HBox();
		thirdRow.getChildren().addAll(buttons2[6], buttons2[7], buttons2[8]);

	
		VBox vbox = new VBox();
		vbox.getChildren().addAll(firstRow, secondRow, thirdRow);

		grid.add(vbox, 0, 0);

		App.resetGame.setOnAction(this);
		determineTurn();
		

		/*

		int buttonNo = 0;
		for (int x = 0; x< 3; x++) {
            for (int y = 0; y < 3; y++) {
				buttons2[buttonNo] = new Button("");
				buttons2[buttonNo].setPrefSize(150, 150);
				buttons2[buttonNo].setOnAction(this);
                grid.add(buttons2[buttonNo], x, y);
				buttonNo++;
            }
        }
		App.resetGame.setOnAction(this);
		determineTurn();
		*/
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
		for (int x = 0; x < 9; x++){
			if (event.getSource() == buttons2[x] && yourTurn) {
				buttons2[x].setText(player.toString());
				buttons2[x].setDisable(true);

				winCheck();
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

		String[] buttonsToText = new String[buttons2.length];
		for (int i = 0; i < buttons2.length; i++) {
			buttonsToText[i] = buttons2[i].getText();
		}
		
		// Top horizontal
		if (!buttons2[0].getText().equals("") && 
			buttonsToText[0].equals(buttonsToText[1]) && buttonsToText[1].equals(buttonsToText[2])
		) {
			highlightButtons(buttons2[0], buttons2[1], buttons2[2]);
			return true;
		}

		// Middle horizontal
		if (!buttons2[3].getText().equals("") && buttons2[3].getText().equals(buttons2[4].getText()) && buttons2[4].getText().equals(buttons2[5].getText())) {
			highlightButtons(buttons2[3], buttons2[4], buttons2[5]);
			return true;
		}

		//Bottom horizontal
		if (!buttons2[6].getText().equals("") && buttons2[6].getText().equals(buttons2[7].getText()) && buttons2[7].getText().equals(buttons2[8].getText())) {
			highlightButtons(buttons2[6], buttons2[7], buttons2[8]);
			return true;
		}

		//Left vertical
		if (!buttons2[3].getText().equals("") && buttons2[0].getText().equals(buttons2[3].getText()) && buttons2[3].getText().equals(buttons2[6].getText())) {
			highlightButtons(buttons2[0], buttons2[3], buttons2[6]);
			return true;
		}

		//Middle vertical
		if (!buttons2[1].getText().equals("") && buttons2[1].getText().equals(buttons2[4].getText()) && buttons2[4].getText().equals(buttons2[7].getText())) {
			highlightButtons(buttons2[1], buttons2[4], buttons2[7]);
			return true;
		}

		//Right vertical
		if (!buttons2[2].getText().equals("") && buttons2[2].getText().equals(buttons2[5].getText()) && buttons2[5].getText().equals(buttons2[8].getText())) {
			highlightButtons(buttons2[2], buttons2[5], buttons2[8]);
			return true;
		}

		//Left-to-right diagonal
		if (!buttons2[0].getText().equals("") && buttons2[0].getText().equals(buttons2[4].getText()) && buttons2[4].getText().equals(buttons2[8].getText())) {
			highlightButtons(buttons2[0], buttons2[4], buttons2[8]);
			return true;
		}

		//Right-to-left diagonal
		if (!buttons2[2].getText().equals("") && buttons2[2].getText().equals(buttons2[4].getText()) && buttons2[4].getText().equals(buttons2[6].getText())) {
			highlightButtons(buttons2[2], buttons2[4], buttons2[6]);
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
				buttons2[x].setDisable(false);
				buttons2[x].setText("");
				buttons2[x].getStyleClass().clear();
				buttons2[x].getStyleClass().add("button");
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
			if (buttons2[x].isDisabled()) {
				disabledButtons++;
			}
		}

		return disabledButtons == 9;
	}

	private void nextRound() {
		App.setStatus("Computer's turn");
		boolean completedMove = false;

		
		if (buttons2[4].getText().equals("")) {
			if (youAreX) {
				buttons2[4].setText("O");
			}
			else {
				buttons2[4].setText("X");
			}
			buttons2[4].setDisable(true);
			completedMove = true;
		}

		for (int x = 0; x < 9; x++) {
			if (completedMove) {
				break;
			}

			if (buttons2[x].getText().equals("")) {
				if (youAreX) {
					buttons2[x].setText("O");
				}
				else {
					buttons2[x].setText("X");
				}
				buttons2[x].setDisable(true);
				completedMove = true;
				break;
			}
		}
		

		winCheck();
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
				buttons2[x].setDisable(true);
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