package hu.multiplayermnkgame.gameview;

import hu.multiplayermnkgame.game.algorithm.MaxN;
import hu.multiplayermnkgame.game.algorithm.MaxN0;
import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.algorithm.Paranoid;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.game.heuristic.RandomHeuristic;
import hu.multiplayermnkgame.game.heuristic.RulesHeuristic;
import hu.multiplayermnkgame.game.heuristic.TerminalNodeHeuristic;
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

import java.util.ArrayList;
import java.util.List;

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

    private List<Color> playerColors;

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

        textFieldM = new TextField("9");
        textFieldM.setMaxWidth(40);
        textFieldN = new TextField("6");
        textFieldN.setMaxWidth(40);
        gridPane.addRow(0, textFieldM, textFieldN);

        Label label2 = new Label("Nyerő jelsorozat hossza:");
        label2.setPrefWidth(200);
        gridPane.addRow(1, label2);

        textFieldK = new TextField("4");
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
        gridPanePlayers.setPadding(new Insets(5, 1, 5, 1));
        gridPanePlayers.setVgap(5);

        ScrollPane scrollPane = new ScrollPane(gridPanePlayers);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(500);

        VBox vBox = new VBox(5, gridPane, scrollPane);

        handlePlayerAdded(null);
        handlePlayerAdded(null);

        Button startButton = new Button("Start");
        startButton.setOnAction(this::handleStart);

        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(20);
        hBoxBottom.setPadding(new Insets(5, 5, 10, 5));
        hBoxBottom.setAlignment(CENTER);
        checkBoxAnalise = new CheckBox("Lépések elemzése");
        checkBoxAnalise.setSelected(true);
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
        if(numberOfPlayers < 9){
            numberOfPlayers++;
            gridPanePlayers.addRow(numberOfPlayers - 1, createHBoxForPlayer(numberOfPlayers));
        }
    }

    private HBox createHBoxForPlayer(int player) {
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER_LEFT);

        hBox.getChildren().add(new Label(player + "."));

        ComboBox comboBoxAlgorithm = new ComboBox(FXCollections.observableArrayList(
                "Max N 0", "Max N", "Paranoid"));
        comboBoxAlgorithm.setValue("Max N 0");
        hBox.getChildren().add(comboBoxAlgorithm);

        ComboBox comboBoxHeuristic = new ComboBox(FXCollections.observableArrayList(
                "Rules Heuristic", "TerminalNode", "Random"));
        comboBoxHeuristic.setValue("Rules Heuristic");
        hBox.getChildren().add(comboBoxHeuristic);
        ObservableList<String> colors = FXCollections.observableArrayList("blue", "red", "gold",
                "green", "darkorchid", "darkgoldenrod", "black", "rosybrown", "blueviolet", "brown");
        ComboBox comboBoxColor = new ComboBox(colors);
        Callback<ListView<String>, ListCell<String>> factory = list -> new ColorCell();

        comboBoxColor.setCellFactory(factory);
        comboBoxColor.setButtonCell(factory.call(null));
        comboBoxColor.getSelectionModel().select(player - 1);
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
            Alert alert = new Alert(Alert.AlertType.WARNING, "Hibás adatok lettek megadva!\n" +
                    "Így nem lehet a játékot konfigurálni,\nkérem változtasson a tábla méretein \nvagy a nyerő jelsorozat hosszán!", ButtonType.OK);
            alert.setHeaderText("Figyelmeztetés");
            alert.setTitle("Figyelmeztetés");
            alert.showAndWait();
        }
    }

    private boolean isInputFromUserValid() {
        return isTextFieldValid(textFieldM) && isTextFieldValid(textFieldN) && isTextFieldKValid();
    }

    private boolean isTextFieldValid(TextField textField) {
        boolean valid = false;
        if (!(textField.getText() == null || textField.getText().length() == 0)) {
            try {
                Integer data = Integer.parseInt(textField.getText());
                if (1.0 <= data && data <= 100.0) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
            }
        }
        return valid;
    }

    private boolean isTextFieldKValid() {
        boolean valid = false;
        if (!(textFieldK.getText() == null || textFieldK.getText().length() == 0)) {
            try {
                Integer data = Integer.parseInt(textFieldK.getText());
                Integer m = Integer.parseInt(textFieldM.getText());
                Integer n = Integer.parseInt(textFieldN.getText());
                if (2.0 <= data && data <= Math.max(m, n)) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
            }
        }
        return valid;
    }

    @FXML
    private void getInputFromUser() {

        M = Integer.parseInt(textFieldM.getText());
        N = Integer.parseInt(textFieldN.getText());
        K = Integer.parseInt(textFieldK.getText());

        listOfPlayerHeuristics = new Heuristic[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            String heuristic = ((ComboBox) ((HBox) gridPanePlayers.getChildren().get(i - 1)).getChildren().get(2)).getValue().toString();
            if (heuristic.equals("Rules Heuristic")) {
                listOfPlayerHeuristics[i] = new RulesHeuristic();
            } else if (heuristic.equals("TerminalNode")) {
                listOfPlayerHeuristics[i] = new TerminalNodeHeuristic();
            } else if (heuristic.equals("Random")) {
                listOfPlayerHeuristics[i] = new RandomHeuristic();
            }
        }

        listOfPlayerAlgorithms = new MultiPlayerAlgorithm[numberOfPlayers + 1];
        for (int i = 1; i <= numberOfPlayers; i++) {
            String algorithm = ((ComboBox) ((HBox) gridPanePlayers.getChildren().get(i - 1)).getChildren().get(1)).getValue().toString();
            if (algorithm.equals("Max N 0")) {
                listOfPlayerAlgorithms[i] = new MaxN0();
            } else if (algorithm.equals("Max N")) {
                listOfPlayerAlgorithms[i] = new MaxN();
            } else if (algorithm.equals("Paranoid")) {
                listOfPlayerAlgorithms[i] = new Paranoid();
            }
        }

        playerColors = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            String colorString = ((ComboBox) ((HBox) gridPanePlayers.getChildren().get(i - 1)).getChildren().get(3)).getValue().toString();
            playerColors.add(Color.web(colorString));
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

    public List<Color> getPlayerColors() {
        return playerColors;
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
