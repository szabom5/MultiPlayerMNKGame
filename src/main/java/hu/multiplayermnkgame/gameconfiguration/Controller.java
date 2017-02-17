package hu.multiplayermnkgame.gameconfiguration;

import hu.multiplayermnkgame.game.algorithm.MaxN;
import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.algorithm.Paranoid;
import hu.multiplayermnkgame.game.gameplay.GameLoop;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.game.heuristic.RulesHeuristic;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    @FXML
    private TextField textFieldM;

    @FXML
    private TextField textFieldN;

    @FXML
    private TextField textFieldK;

    @FXML
    private GridPane gridPanePlayers;

    @FXML
    private CheckBox checkBoxStatistics;

    // M >= 2
    public static int M;

    // N >= 2
    public static int N;

    // 2 <= K <= min(M,N)
    public static int K;

    //numberOfPlayers >= 2
    public static int numberOfPlayers = 0;

    private static MultiPlayerAlgorithm[] listOfPlayerAlgorithms;

    private static Heuristic[] listOfPlayerHeuristics;

    public static boolean logging = false;

    public void handleStart(ActionEvent actionEvent) {

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

        listOfPlayerHeuristics = new Heuristic[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            if(((ComboBox)((HBox)gridPanePlayers.getChildren().get(i-1)).getChildren().get(2)).getValue().toString().equals("Rules Heuristic")){
                listOfPlayerHeuristics[i] = new RulesHeuristic();
            }else {
                //no other option
                listOfPlayerHeuristics[i] = new RulesHeuristic();
            }
        }

        listOfPlayerAlgorithms = new MultiPlayerAlgorithm[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            if(((ComboBox)((HBox)gridPanePlayers.getChildren().get(i-1)).getChildren().get(1)).getValue().toString().equals("Max N")){
                listOfPlayerAlgorithms[i] = new MaxN();
            }else{
                listOfPlayerAlgorithms[i] = new Paranoid();
            }
        }

        logging = checkBoxStatistics.isSelected();
    }
    @FXML
    public void handlePlayerAdded(ActionEvent actionEvent) {
        numberOfPlayers++;
        gridPanePlayers.addRow(numberOfPlayers-1,createHBoxForPlayer(numberOfPlayers));
    }

    private HBox createHBoxForPlayer(int player) {
        HBox hBox = new HBox(20);

        hBox.getChildren().add(new Label(player+". játékos: "));
        ComboBox comboBoxAlgorithm = new ComboBox(FXCollections.observableArrayList(
                "Max N", "Paranoid"));
        comboBoxAlgorithm.setValue("Max N");
        hBox.getChildren().add(comboBoxAlgorithm);
        ComboBox comboBoxHeuristic = new ComboBox(FXCollections.observableArrayList(
                "Rules Heuristic"));
        comboBoxHeuristic.setValue("Rules Heuristic");
        hBox.getChildren().add(comboBoxHeuristic);

        return hBox;
    }

    @FXML
    public void handlePlayerRemoved(ActionEvent actionEvent) {
        if(numberOfPlayers > 0){
            numberOfPlayers--;
            gridPanePlayers.getChildren().remove(numberOfPlayers);
        }
    }
}
