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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class Controller {
    @FXML
    private SplitPane splitPane;

    private BorderPane rightBorderPane;

    private GridPane gridPanePlayers;

    private TextField textFieldM;

    private TextField textFieldN;

    private TextField textFieldK;

    private CheckBox checkBoxAnalize;

    private TextArea textAreaLog;

    private Group[][] arrayOfRectangles;

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

    private static boolean logging = false;

    @FXML
    public void initialize() throws IOException {
        splitPane.setDividerPositions(0.30);

        splitPane.setPrefSize(1000, 700);

        splitPane.getItems().addAll(createLeftBorderPane(), createRightBorderPane());
    }

    private BorderPane createLeftBorderPane() {
        Label title = new Label("Beállítások");
        title.setPrefWidth(500);
        title.setAlignment(Pos.BOTTOM_CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        Label label1 = new Label("A tábla méretei:");
        label1.setPrefWidth(200);
        gridPane.addRow(0, label1);

        textFieldM = new TextField("6");
        textFieldM.setMaxWidth(40);
        textFieldN = new TextField("6");
        textFieldN.setMaxWidth(40);
        gridPane.addRow(0, textFieldM, textFieldN);

        Label label2 = new Label("Nyerő jelsorozat hossza:");
        label2.setPrefWidth(200);
        gridPane.addRow(1, label2);

        textFieldK = new TextField("3");
        textFieldK.setMaxWidth(40);
        gridPane.addRow(1, textFieldK);

        Label label3 = new Label("Játékosok:");
        label3.setPrefWidth(200);
        Button buttonPlus = new Button("+");
        buttonPlus.setMaxWidth(25);
        buttonPlus.setOnAction(this::handlePlayerAdded);
        Button buttonMinus = new Button("-");
        buttonMinus.setMaxWidth(25);
        buttonMinus.setOnAction(this::handlePlayerRemoved);
        gridPane.addRow(2, label3, buttonPlus, buttonMinus);

        gridPanePlayers = new GridPane();
        gridPanePlayers.setHgap(10);
        gridPanePlayers.setVgap(5);

        ScrollPane scrollPane = new ScrollPane(gridPanePlayers);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(500);

        BorderPane borderPane = new BorderPane(new VBox(gridPane, scrollPane));
        borderPane.setPadding(new Insets(5, 20, 5, 20));
        borderPane.setTop(title);

        borderPane.minWidthProperty().bind(splitPane.widthProperty().multiply(0.3));

        Button startButton = new Button("Start");
        startButton.setOnAction(this::handleStart);


        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(10);
        checkBoxAnalize = new CheckBox("Lépések elemzése");
        hBoxBottom.getChildren().add(checkBoxAnalize);
        hBoxBottom.getChildren().add(startButton);

        borderPane.setBottom(hBoxBottom);

        return borderPane;
    }

    private BorderPane createRightBorderPane() {
        Label title = new Label("Többszemélyes m,n,k-játék");
        title.setPrefWidth(500);
        title.setAlignment(Pos.BOTTOM_CENTER);

        textAreaLog = new TextArea("****Log****");
        textAreaLog.setMinHeight(500);

        ScrollPane scrollPaneLog = new ScrollPane(textAreaLog);
        scrollPaneLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLog.setMaxWidth(100);
        scrollPaneLog.setMinHeight(500);
        scrollPaneLog.setStyle("-fx-background-color:lightgreen");

        rightBorderPane = new BorderPane();
        rightBorderPane.setTop(title);
        rightBorderPane.setRight(scrollPaneLog);

        return rightBorderPane;
    }

    private Collection createBoardTiles(int m, int n) {

        List<Group> list = new ArrayList<>();
        arrayOfRectangles = new Group[m][n];

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                Rectangle rectangle = new Rectangle(j * 20, i * 20, 20, 20);
                rectangle.setStyle("-fx-fill:blue;-fx-stroke:red");
                Group group = new Group(rectangle);
                group.getChildren().add(new Circle(j * 20 + 10, i * 20 + 10, 8, Color.YELLOW));
                list.add(group);
                arrayOfRectangles[i][j] = group;
            }
        }

        return list;
    }

    private void handleStart(ActionEvent actionEvent) {

        if (isInputFromUserValid()) {

            getInputFromUser();

            Group group = new Group(createBoardTiles(M, N));

            rightBorderPane.setCenter(group);

            GameLoop gameLoop = initializeGameLoop();
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
            if (((ComboBox) ((HBox) gridPanePlayers.getChildren().get(i - 1)).getChildren().get(2)).getValue().toString().equals("Rules Heuristic")) {
                listOfPlayerHeuristics[i] = new RulesHeuristic();
            } else {
                //no other option
                listOfPlayerHeuristics[i] = new RulesHeuristic();
            }
        }

        listOfPlayerAlgorithms = new MultiPlayerAlgorithm[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            if (((ComboBox) ((HBox) gridPanePlayers.getChildren().get(i - 1)).getChildren().get(1)).getValue().toString().equals("Max N")) {
                listOfPlayerAlgorithms[i] = new MaxN();
            } else {
                listOfPlayerAlgorithms[i] = new Paranoid();
            }
        }

        logging = checkBoxAnalize.isSelected();
    }

    private GameLoop initializeGameLoop() {
        Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies = new HashMap<>();

        for (int i = 1; i <= numberOfPlayers; ++i) {
            mapOfPlayerStrategies.put(i, new Pair(listOfPlayerAlgorithms[i], listOfPlayerHeuristics[i]));
        }

        return new GameLoop.GameLoopBuilder()
                .setNumberOfPlayers(numberOfPlayers)
                .setBoardParameters(M, N)
                .setWinningNumber(K)
                .setMapOfPlayerStrategies(mapOfPlayerStrategies)
                .setLogging(logging)
                .build();
    }

    @FXML
    private void handlePlayerAdded(ActionEvent actionEvent) {
        numberOfPlayers++;
        gridPanePlayers.addRow(numberOfPlayers - 1, createHBoxForPlayer(numberOfPlayers));
    }

    private HBox createHBoxForPlayer(int player) {
        HBox hBox = new HBox(20);

        hBox.getChildren().add(new Label(player + "."));
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
        if (numberOfPlayers > 0) {
            numberOfPlayers--;
            gridPanePlayers.getChildren().remove(numberOfPlayers);
        }
    }
}
