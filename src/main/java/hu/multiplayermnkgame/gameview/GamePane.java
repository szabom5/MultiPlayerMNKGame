package hu.multiplayermnkgame.gameview;

import hu.multiplayermnkgame.game.gameplay.GameLoop;
import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GamePane extends BorderPane {

    private GameAttributes attributes;

    private GameLoop gameLoop;

    private TextArea textAreaLog;

    private BoardPane boardPane;

    private Button nextMoveButton;

    public GamePane() {
        Label title = new Label("Többszemélyes m,n,k-játék");
        title.setPrefWidth(500);
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(10, 5, 5, 5));

        textAreaLog = new TextArea("****Log****\n");
        textAreaLog.setMinHeight(Double.MAX_VALUE);
        textAreaLog.setMaxWidth(140);

        ScrollPane scrollPaneLog = new ScrollPane(textAreaLog);
        scrollPaneLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLog.setMaxWidth(150);

        nextMoveButton = new Button("Következő lépés");
        nextMoveButton.setOnAction(this::handleNextMove);
        nextMoveButton.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(10, nextMoveButton);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setAlignment(Pos.CENTER);

        setTop(title);
        setRight(scrollPaneLog);
        setPadding(new Insets(5, 10, 5, 5));
        setBottom(hBox);
        setStyle("-fx-background-color:lemonchiffon");
    }

    public void initialize(GameAttributes attributes, GameLoop gameLoop) {
        this.attributes = attributes;
        this.gameLoop = gameLoop;

        nextMoveButton.setDisable(false);

        boardPane = new BoardPane(attributes.getM(), attributes.getN());

        setCenter(boardPane);
    }

    private void handleNextMove(ActionEvent actionEvent) {

        GameState gameState = gameLoop.nextMove();

        updateBoard(gameState);


        Text stepLog = new Text(gameState.lastStep.player + " játékos\n"
                + gameState.lastStep.toString() + "\n");
        stepLog.setFill(attributes.getPlayerColors().get(gameState.lastStep.player - 1));

        textAreaLog.appendText(stepLog.getText());

        if (gameLoop.getStatus() != 0) {
            handleGameEnding(gameLoop.getStatus());
        }

    }

    private void handleGameEnding(int status) {
        nextMoveButton.setDisable(true);

        textAreaLog.appendText("---VÉGE---\n");
        if (status == -1) {
            textAreaLog.appendText("DÖNTETLEN\n");
        } else {
            textAreaLog.appendText("Nyert:\n");
            textAreaLog.appendText(status + ". játékos\n");
        }

    }

    public void updateBoard(GameState gs) {
        int player = gs.lastStep.player;

        if (attributes.isLogging() && gs.state.countAllMarks() > 1) {
            boardPane.clearNumbers();

            textAreaLog.appendText("\n" + player + " " + attributes.getMapOfPlayerStrategies().get(player).getValue().name() + "\n");

            Color color = attributes.getPlayerColors().get(player - 1);
            boardPane.setSign(gs.lastStep.getX() - 1, gs.lastStep.getY() - 1, color);

            for (int i = 0; i < attributes.getM(); ++i) {
                for (int j = 0; j < attributes.getN(); ++j) {
                    double value = gs.details[i + 1][j + 1];
                    if (value != 0.1) {
                        boardPane.setSign(i, j, String.valueOf(value));
                    }
                }
            }
        } else {
            Color color = attributes.getPlayerColors().get(player - 1);
            boardPane.setSign(gs.lastStep.getX() - 1, gs.lastStep.getY() - 1, color);
        }
    }
}
