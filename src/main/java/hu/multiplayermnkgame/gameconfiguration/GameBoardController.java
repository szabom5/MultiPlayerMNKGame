package hu.multiplayermnkgame.gameconfiguration;

import hu.multiplayermnkgame.game.gameplay.GameLoop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static hu.multiplayermnkgame.gameconfiguration.Controller.M;
import static hu.multiplayermnkgame.gameconfiguration.Controller.N;

public class GameBoardController {

    @FXML
    private Pane boardPane;

    @FXML
    private VBox rowsVBox;

    private GameLoop gameLoop;

    public GameBoardController(GameLoop loop){
        gameLoop = loop;
    }

    @FXML
    public void initialize() {
        for (int i = 0; i < M; ++i) {
            rowsVBox.getChildren().add(createHBoxForColumns());
        }

        gameLoop.loop();
    }

    private Node createHBoxForColumns() {
        HBox hBox = new HBox(0);

        double height = boardPane.getHeight();
        double width = boardPane.getWidth();

        for (int i = 0; i < N; ++i) {
            Rectangle rectangle = new Rectangle(width / N, height / M);
            rectangle.setFill(Color.BLUE);
            hBox.getChildren().add(rectangle);
        }

        return hBox;
    }
}
