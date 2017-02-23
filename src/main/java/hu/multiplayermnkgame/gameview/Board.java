package hu.multiplayermnkgame.gameview;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Board extends StackPane {
    private int m;
    private int n;
    private VBox vBox;
    private List<Rectangle> rectangleList;
    private List signsList;

    public Board(int m, int n) {
        this.m = m;
        this.n = n;
        setMinSize(30, 30);
        build();
        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            int rw = newValue.intValue() / (this.n + 1);
            int rh = (int) (vBox.getHeight() / (this.m + 1));
            int rSize = Math.min(rw, rh);
            updateSigns(rSize);
        });

        vBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            int rh = newValue.intValue() / (this.m + 1);
            int rw = (int) (vBox.getWidth() / (this.n + 1));
            int rSize = Math.min(rw, rh);
            updateSigns(rSize);
        });
    }

    private void updateSigns(int rSize) {
        for (Rectangle rec : rectangleList) {
            rec.setWidth(rSize);
            rec.setHeight(rSize);
        }
        for (Object object : signsList) {
            Node s = (Node) object;
            if (s instanceof Circle) {
                int r2 = rSize / 2;
                ((Circle) s).setCenterX(r2);
                ((Circle) s).setCenterY(r2);
                ((Circle) s).setRadius(r2 - 1);
            }
            if (s instanceof Label) {
                ((Label) s).setMinWidth(rSize - 2);
                ((Label) s).setMinHeight(rSize - 2);
                ((Label) s).setMaxWidth(rSize - 2);
                ((Label) s).setMaxHeight(rSize - 2);
            }
        }
    }

    private void build() {
        rectangleList = new ArrayList<>();
        signsList = new ArrayList();
        vBox = new VBox();
        vBox.setStyle("-fx-background-color: lemonchiffon");
        vBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < m; i++) {
            HBox hb = new HBox();
            hb.setAlignment(Pos.CENTER);
            for (int j = 0; j < n; j++) {
                Rectangle rec = new Rectangle(1, 1);
                rec.setStyle("-fx-fill: papayawhip; -fx-stroke:dimgray");
                Group g = new Group();
                g.getChildren().add(rec);
                hb.getChildren().add(g);
                rectangleList.add(rec);
            }
            vBox.getChildren().add(hb);
        }
        getChildren().add(vBox);
    }

    public void setSign(int row, int col, Color sign) {
        Group g = getGroup(row, col);
        if (g == null) return;
        Circle c = new Circle(1, sign);
        g.getChildren().add(c);
        signsList.add(c);
        Rectangle rec = rectangleList.get(0);
        int rw = (int) rec.getWidth();
        int r2 = rw / 2;
        c.setCenterX(r2);
        c.setCenterY(r2);
        c.setRadius(r2 - 1);
    }

    public void setSign(int row, int col, String sign) {
        Group g = getGroup(row, col);
        if (g == null) return;
        Label t = new Label(sign);
        t.setAlignment(Pos.CENTER);
        t.setStyle("-fx-text-fill: white");
        g.getChildren().add(t);
        signsList.add(t);
        Rectangle rec = rectangleList.get(0);
        int rw = (int) rec.getWidth();
        t.setMinWidth(rw - 2);
        t.setMinHeight(rw - 2);
        t.setMaxWidth(rw - 2);
        t.setMaxHeight(rw - 2);
    }

    private Group getGroup(int row, int col) {
        try {
            return (Group) ((HBox) vBox.getChildren().get(row)).getChildren().get(col);
        } catch (Exception e) {
            return null;
        }
    }

}
