package hu.multiplayermnkgame.gameconfiguration;

import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.gameplay.GameLoop;
import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.gameview.BoardPane;
import hu.multiplayermnkgame.gameview.ConfigurationPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    @FXML
    private SplitPane splitPane;

    private BorderPane rightBorderPane;

    private TextArea textAreaLog;

    private BoardPane boardPane;

    private ConfigurationPane leftBorderPane;

    private EventHandler<ActionEvent> startEventHandler;

    @FXML
    public void initialize() throws IOException {
        splitPane.setDividerPositions(0.30);

        splitPane.setPrefSize(1000, 700);

        handleStart();

        splitPane.getItems().addAll(createLeftBorderPane(), createRightBorderPane());
    }

    private BorderPane createLeftBorderPane() {
        leftBorderPane = new ConfigurationPane(splitPane, startEventHandler);
        leftBorderPane.initialize();
        return leftBorderPane;
    }

    private BorderPane createRightBorderPane() {
        Label title = new Label("Többszemélyes m,n,k-játék");
        title.setPrefWidth(500);
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(10, 5, 5, 5));

        textAreaLog = new TextArea("****Log****");
        textAreaLog.setMinHeight(Double.MAX_VALUE);
        textAreaLog.setMaxWidth(140);

        ScrollPane scrollPaneLog = new ScrollPane(textAreaLog);
        scrollPaneLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLog.setMaxWidth(150);

        rightBorderPane = new BorderPane();
        rightBorderPane.setTop(title);
        rightBorderPane.setRight(scrollPaneLog);
        rightBorderPane.setStyle("-fx-background-color:lemonchiffon");

        return rightBorderPane;
    }

    private void handleStart() {

        startEventHandler = event -> {

            boardPane = new BoardPane(leftBorderPane.getM(), leftBorderPane.getN());

            rightBorderPane.setCenter(boardPane);

            GameAttributes gameAttributes = initializeGameAttributes();

            GameLoop gameLoop = new GameLoop(gameAttributes);

            gameLoop.loop();

            boardPane.setSign(1, 1, Color.AQUA);

        };
    }

    private GameAttributes initializeGameAttributes() {
        Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies = new HashMap<>();

        for (int i = 1; i <= leftBorderPane.getNumberOfPlayers(); ++i) {
            mapOfPlayerStrategies.put(i, new Pair(leftBorderPane.getListOfPlayerAlgorithms()[i], leftBorderPane.getListOfPlayerHeuristics()[i]));
        }

        return new GameAttributes.GameAttributesBuilder()
                .setNumberOfPlayers(leftBorderPane.getNumberOfPlayers())
                .setBoardParameters(leftBorderPane.getM(), leftBorderPane.getN())
                .setWinningNumber(leftBorderPane.getK())
                .setMapOfPlayerStrategies(mapOfPlayerStrategies)
                .setLogging(leftBorderPane.isLogging())
                .build();
    }
}
