package hu.multiplayermnkgame.gameview;

import hu.multiplayermnkgame.game.algorithm.MaxN;
import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.algorithm.Paranoid;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.game.heuristic.RulesHeuristic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import static javafx.geometry.Pos.CENTER;

public class ConfigurationPane extends BorderPane {
    private Control parent;

    private EventHandler eventHandler;

    private GridPane gridPanePlayers;

    private TextField textFieldM;

    private TextField textFieldN;

    private TextField textFieldK;

    private CheckBox checkBoxAnalise;

    private int M;

    private int N;

    private int K;

    private int numberOfPlayers = 0;

    private MultiPlayerAlgorithm[] listOfPlayerAlgorithms;

    private Heuristic[] listOfPlayerHeuristics;

    private boolean logging = false;

    public ConfigurationPane(Control parent, EventHandler<ActionEvent> eventHandler) {
        this.parent = parent;
        this.eventHandler = eventHandler;
    }

    public void initialize() {
        Label title = new Label("Beállítások");
        title.setPrefWidth(500);
        title.setAlignment(CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setAlignment(CENTER);
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
        buttonPlus.setStyle("-fx-background-color:lightgreen");
        buttonPlus.setMaxWidth(30);
        buttonPlus.setOnAction(this::handlePlayerAdded);
        Button buttonMinus = new Button("-");
        buttonMinus.setStyle("-fx-background-color:tomato");
        buttonMinus.setMaxWidth(30);
        buttonMinus.setOnAction(this::handlePlayerRemoved);
        gridPane.addRow(2, label3, buttonPlus, buttonMinus);

        gridPanePlayers = new GridPane();
        gridPanePlayers.setPadding(new Insets(5,1,5,1));
        gridPanePlayers.setVgap(5);

        ScrollPane scrollPane = new ScrollPane(gridPanePlayers);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(500);

        VBox vBox = new VBox(5, gridPane, scrollPane);

        Button startButton = new Button("Start");
        startButton.setOnAction(this::handleStart);

        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(20);
        hBoxBottom.setPadding(new Insets(5,5,10,5));
        hBoxBottom.setAlignment(CENTER);
        checkBoxAnalise = new CheckBox("Lépések elemzése");
        hBoxBottom.getChildren().add(checkBoxAnalise);
        hBoxBottom.getChildren().add(startButton);

        setCenter(vBox);
        setTop(title);
        setBottom(hBoxBottom);

        setPadding(new Insets(10, 10, 5, 10));
        minWidthProperty().bind(parent.widthProperty().multiply(0.3));

        setStyle("-fx-background-color:lemonchiffon");
    }

    @FXML
    private void handlePlayerAdded(ActionEvent actionEvent) {
        numberOfPlayers++;
        gridPanePlayers.addRow(numberOfPlayers - 1, createHBoxForPlayer(numberOfPlayers));
    }

    private HBox createHBoxForPlayer(int player) {
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER_LEFT);

        hBox.getChildren().add(new Label(player + "."));

        ComboBox comboBoxAlgorithm = new ComboBox(FXCollections.observableArrayList(
                "Max N", "Paranoid"));
        comboBoxAlgorithm.setValue("Max N");
        hBox.getChildren().add(comboBoxAlgorithm);

        ComboBox comboBoxHeuristic = new ComboBox(FXCollections.observableArrayList(
                "Rules Heuristic"));
        comboBoxHeuristic.setValue("Rules Heuristic");
        hBox.getChildren().add(comboBoxHeuristic);
        ObservableList<String> colors = FXCollections.observableArrayList("blue", "red", "gold",
            "green", "darkorchid", "darkgoldenrod", "black", "rosybrown", "blueviolet", "brown");
        ComboBox comboBoxColor = new ComboBox(colors);
        Callback<ListView<String>, ListCell<String>> factory = list -> new ColorCell();

        comboBoxColor.setCellFactory(factory);
        comboBoxColor.setButtonCell(factory.call(null));
        hBox.getChildren().add(comboBoxColor);

        return hBox;
    }

    private void handlePlayerRemoved(ActionEvent actionEvent) {
        if (numberOfPlayers > 0) {
            numberOfPlayers--;
            gridPanePlayers.getChildren().remove(numberOfPlayers);
        }
    }

    private void handleStart(ActionEvent actionEvent) {
        if (isInputFromUserValid()) {

            getInputFromUser();

            eventHandler.handle(actionEvent);
        } else {
            //TODO Error message
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
                listOfPlayerAlgorithms[i] = new MaxN(numberOfPlayers);
            } else {
                listOfPlayerAlgorithms[i] = new Paranoid();
            }
        }

        logging = checkBoxAnalise.isSelected();
    }

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    public int getK() {
        return K;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public MultiPlayerAlgorithm[] getListOfPlayerAlgorithms() {
        return listOfPlayerAlgorithms;
    }

    public Heuristic[] getListOfPlayerHeuristics() {
        return listOfPlayerHeuristics;
    }

    public boolean isLogging() {
        return logging;
    }

    private static class ColorCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(30, 18);
            if (item != null) {
                rect.setFill(Color.web(item));
                setGraphic(rect);
            }
        }
    }
}
