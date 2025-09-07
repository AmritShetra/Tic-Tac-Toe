package tictactoe;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private static Label status = new Label();
	private static Label scores = new Label();
	static Button resetGame = new Button();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tic-Tac-Toe");
		
		BorderPane layout = new BorderPane(); //Main layout used to determine position of GridPane and other components
		GridPane grid = new GridPane(); //Used to determine positions of buttons etc.

		Board board = new Board();
		board.initialiseBoard(grid);
		
		Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
		ImageView logoImage = new ImageView(logo);
		
		scores.setText("You 0-0 Computer");
		VBox logoScoreBox = new VBox(logoImage, scores);
		logoScoreBox.setAlignment(Pos.CENTER);
		logoScoreBox.setSpacing(10);
		
		HBox logoImageBox = new HBox(logoScoreBox); //Used to align the logo at the top of the screen
		logoImageBox.setAlignment(Pos.CENTER);

		Image reset = new Image(getClass().getResourceAsStream("/Reset-Game.png"));
		ImageView resetImage = new ImageView(reset);
		resetImage.setImage(reset);
		resetGame.setGraphic(resetImage);

		VBox buttonStatusBox = new VBox(status, resetGame); //Aligns the reset button and status label
		buttonStatusBox.setAlignment(Pos.CENTER);
		buttonStatusBox.setSpacing(10);
		
		HBox bottomBox = new HBox(buttonStatusBox); //Aligns the above box at the bottom of the screen
		bottomBox.setAlignment(Pos.CENTER);

		layout.setTop(logoImageBox);
		layout.setCenter(grid); //Sets the grid containing the buttons to the center of the layout
		layout.setBottom(bottomBox);
		
		Scene mainScene = new Scene(layout, 800, 800); //Add the BorderPane layout to the window
		mainScene.getStylesheets().add(getClass().getResource("/theme.css").toExternalForm()); //Adds the CSS stylesheet to the scene
		primaryStage.setScene(mainScene); //Set the window scene to mainScene which contains the layout
		primaryStage.show();
    }

    public static Label getStatus() {
		return status;
	}

    /**
	 * Sets the text for the "status" label, e.g. whose turn it is.
	 * @param newText	New label string.
	 */
	public static void setStatus(String newText) {
		status.setText(newText);
	}

	/**
	 * Sets the text for the "scores" label, e.g. "You 0-0 Computer".
	 * @param newScore	New score string.
	 */
	public static void setScores(String newScore) {
		scores.setText(newScore);
	}

}
