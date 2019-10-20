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

public class UserInterface extends Application {
	static Label status = new Label();
	static Label scores = new Label();
	final static Button resetGame = new Button("Reset Game");

	public static void main (String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Tic-Tac-Toe");
		
		BorderPane layout = new BorderPane(); //Main layout used to determine position of GridPane and other components
		GridPane grid = new GridPane(); //Used to determine positions of buttons etc.
		
		Board board = new Board();
		board.initialiseBoard(grid);
		
		Image logo = new Image(getClass().getResourceAsStream("res/Logo.png"));
		ImageView logoImage = new ImageView(logo);
		
		scores.setText("You 0-0 Computer");
		VBox logoScoreBox = new VBox(logoImage, scores);
		logoScoreBox.setAlignment(Pos.CENTER);
		logoScoreBox.setSpacing(10);
		
		HBox logoImageBox = new HBox(logoScoreBox); //Used to align the logo at the top of the screen
		logoImageBox.setAlignment(Pos.CENTER);
		
		VBox buttonStatusBox = new VBox(status, resetGame); //Aligns the reset button and status label
		buttonStatusBox.setAlignment(Pos.CENTER);
		buttonStatusBox.setSpacing(10);
		
		HBox bottomBox = new HBox(buttonStatusBox); //Aligns the above box at the bottom of the screen
		bottomBox.setAlignment(Pos.CENTER);

		layout.setTop(logoImageBox);
		layout.setCenter(grid); //Sets the grid containing the buttons to the center of the layout
		layout.setBottom(bottomBox);
		
		Scene mainScene = new Scene(layout, 800, 800); //Add the BorderPane layout to the window
		mainScene.getStylesheets().add(getClass().getResource("res/Theme.css").toExternalForm()); //Adds the CSS stylesheet to the scene
		primaryStage.setScene(mainScene); //Set the window scene to mainScene which contains the layout
		primaryStage.show();
	}
}