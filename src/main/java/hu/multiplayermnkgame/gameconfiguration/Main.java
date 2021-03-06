package hu.multiplayermnkgame.gameconfiguration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gameconfig.fxml"));
        primaryStage.setTitle("Multi Player M,N,K - Game");
        primaryStage.setScene(new Scene(root, 1000,700));
        primaryStage.show();

        primaryStage.setMaximized(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
