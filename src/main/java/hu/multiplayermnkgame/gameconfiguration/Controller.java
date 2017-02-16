package hu.multiplayermnkgame.gameconfiguration;

import hu.multiplayermnkgame.game.algorithm.MaxN;
import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.gameplay.GameLoop;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.game.heuristic.RulesHeuristic;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Controller {

    @FXML
    private TextField textFieldNumberOfPlayers;

    @FXML
    private TextField textFieldM;

    @FXML
    private TextField textFieldN;

    @FXML
    private TextField textFieldK;

    @FXML
    private VBox vBox;

    @FXML
    private CheckBox checkBoxStatistics;

    // M >= 2
    public static int M;

    // N >= 2
    public static int N;

    // 2 <= K <= min(M,N)
    public static int K;

    //numberOfPlayers >= 2
    public static int numberOfPlayers;

    private static MultiPlayerAlgorithm[] listOfPlayerAlgorithms;

    private static Heuristic[] listOfPlayerHeuristics;

    public static boolean logging = false;

    public void handleStartButtonAction(ActionEvent actionEvent) {

        getInputFromUser();

        Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies = new HashMap<>();

        for (int i = 1; i <= numberOfPlayers; ++i) {
            mapOfPlayerStrategies.put(i, new Pair(listOfPlayerAlgorithms[i], listOfPlayerHeuristics[i]));
        }

        GameLoop gameLoop = new GameLoop.GameLoopBuilder()
                .setNumberOfPlayers(numberOfPlayers)
                .setBoardParameters(M, N)
                .setWinningNumber(K)
                .setMapOfPlayerStrategies(mapOfPlayerStrategies)
                .setLogging(logging)
                .build();

        gameLoop.loop();
    }

    @FXML
    private void getInputFromUser() {
        //TODO checking for invalid initializations

        M = Integer.parseInt(textFieldM.getText());
        N = Integer.parseInt(textFieldN.getText());
        K = Integer.parseInt(textFieldK.getText());
        numberOfPlayers = Integer.parseInt(textFieldNumberOfPlayers.getText());

        for (int i = 1; i <= numberOfPlayers; i++) {
            vBox.getChildren().add(createHBoxForPlayer(i));
        }

        listOfPlayerHeuristics = new Heuristic[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            listOfPlayerHeuristics[i] = new RulesHeuristic();
        }

        listOfPlayerAlgorithms = new MultiPlayerAlgorithm[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            listOfPlayerAlgorithms[i] = new MaxN();
        }

        logging = checkBoxStatistics.isSelected();
    }

    private HBox createHBoxForPlayer(int player) {
        HBox hBox = new HBox(20);

        hBox.getChildren().add(new Label(player+". játékos: "));
        ChoiceBox choiceBoxAlgorithm = new ChoiceBox(FXCollections.observableArrayList(
                "Max N", "Paranoid"));
        hBox.getChildren().add(choiceBoxAlgorithm);
        ChoiceBox choiceBoxHeuristic = new ChoiceBox(FXCollections.observableArrayList(
                "Rules Heuristic"));
        hBox.getChildren().add(choiceBoxHeuristic);

        return hBox;
    }
}
