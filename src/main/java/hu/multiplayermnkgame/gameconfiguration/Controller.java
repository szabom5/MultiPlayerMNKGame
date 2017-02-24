package hu.multiplayermnkgame.gameconfiguration;

import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.gameplay.GameLoop;
import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.gameview.ConfigurationPane;
import hu.multiplayermnkgame.gameview.GamePane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    @FXML
    private SplitPane splitPane;

    private GamePane rightBorderPane;

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
        rightBorderPane = new GamePane();
        return rightBorderPane;
    }

    private void handleStart() {

        startEventHandler = event -> {

            GameAttributes gameAttributes = initializeGameAttributes();

            GameLoop gameLoop = new GameLoop(gameAttributes);

            rightBorderPane.initialize(gameAttributes, gameLoop);

            GameState firstGameState = gameLoop.firstMove();

            rightBorderPane.updateBoard(firstGameState);
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
                .setPlayerColors(leftBorderPane.getPlayerColors())
                .setLogging(leftBorderPane.isLogging())
                .build();
    }
}
