package application;

import connection.ServerConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("client.fxml"));
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("BukerBet");
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add("application/clientStyle.css");
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent) ->{
            System.out.println("try to stop connection with server");
            ServerConnection.getInstance().interrupt();
        });

        primaryStage.setX((Screen.getPrimary().getBounds().getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((Screen.getPrimary().getBounds().getHeight() - primaryStage.getHeight()) / 2);
        Controller.stage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
