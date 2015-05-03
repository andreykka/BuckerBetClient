package application;

import connection.ServerConnectionService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;
    public static final String LOG_FORM = "view/login.fxml";
    public static final String REG_FORM = "view/registration.fxml";
    public static final String MAIN_FORM = "view/content.fxml";

    public static final String STYLE_PATH = "application/view/clientStyle.css";

    @Override
    public void start(Stage primaryStage) throws Exception{
        ServerConnectionService.getInstance().startConnection();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(LOG_FORM));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("BukerBet");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getScene().getStylesheets().add(STYLE_PATH);
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        stage = primaryStage;
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {
            System.out.println("Close request login");
            ServerConnectionService.getInstance().stopConnection();
        });

//        primaryStage.setX((Screen.getPrimary().getBounds().getWidth() - primaryStage.getWidth()) / 2);
//        primaryStage.setY((Screen.getPrimary().getBounds().getHeight() - primaryStage.getHeight()) / 2);

//        ServerConnectionService.getInstance().startConnection();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
