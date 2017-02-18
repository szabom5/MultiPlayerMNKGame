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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
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

    public void handleStart(ActionEvent actionEvent) throws IOException {

        if(isInputFromUserValid()){

            getInputFromUser();

            GameLoop gameLoop = initializeGameLoop();

            createNewScene(actionEvent, gameLoop);
        }

    }

    private boolean isInputFromUserValid() {
        //TODO checking for invalid initializations
        return true;
    }

    @FXML
    private void getInputFromUser() {

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

    private void createNewScene(ActionEvent actionEvent, GameLoop gameLoop) throws IOException {
        Button startButton = (Button) actionEvent.getSource();

        Stage stage = (Stage) startButton.getScene().getWindow();

        GameBoardController gameBoardController = new GameBoardController(gameLoop);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameboard.fxml"));
        loader.setController(gameBoardController);

        Parent root = loader.load();

        Scene scene = new Scene(root,600,600);
        stage.setScene(scene);
        stage.show();
    }

    private GameLoop initializeGameLoop() {
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

        return gameLoop;
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
