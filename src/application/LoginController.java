package application;

import connection.ServerConnectionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by gandy on 20.04.15.
 *
 */
public class LoginController implements Initializable{

    // login pane
    @FXML private TextField     loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button        loginBtn;
    @FXML private Button        showRegisterBtn;

    private Stage   dialogStage;
    private boolean okClicked = false;

    private Pattern loginPat = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_\\.]{0,20}$");

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        okClicked = false;
    }

    private boolean isFilledLoginData(){
        return !(loginField.getText().isEmpty() || passwordField.getText().isEmpty());
    }

    private void showContent(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(Main.MAIN_FORM));

            AnchorPane pane = loader.load();
            Stage mainStage = new Stage();
            mainStage.setTitle("Buker Bet");

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Main.STYLE_PATH);
            mainStage.setScene(scene);

            ContentController controller = loader.getController();
            controller.setStage(mainStage);

            mainStage.setOnCloseRequest(action -> {
                ServerConnectionService.getInstance().stopConnection();
                mainStage.close();
            });
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean showRegistration(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(Main.REG_FORM));

            AnchorPane pane = loader.load();
            Stage regStage = new Stage();
            regStage.setTitle("Регистрация");
            regStage.setResizable(false);
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Main.STYLE_PATH);
            regStage.setScene(scene);

            RegistrationController controller = loader.getController();
            controller.setStage(regStage);
            regStage.setOnCloseRequest(action -> {
                regStage.close();
                dialogStage.show();
            });
            regStage.show();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.loginField.setOnAction(event -> {
            TextField field  = null;
            if (event.getSource() instanceof  TextField ){
                field = (TextField) event.getSource();
            } else {
                System.out.println("Not a TextField event (");
                throw new Error("Event not a 'TextField type' FIX IT please!!");
            }
            while (field.getText().length() > 0) {
                if (loginPat.matcher(field.getText()).matches())
                    break;
                field.setText(field.getText().substring(0, field.getText().length()-1));
                field.positionCaret(field.getLength());
            }
        });

        loginBtn.setOnAction(event -> {
            if (!isFilledLoginData()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информационное сообщение");
                alert.setHeaderText(null);
                alert.setContentText("Необходимо заполнить все поля даными !!!");
                alert.show();
                return;
            }
            Boolean isLogin = false;

            try {
                isLogin = ServerConnectionService.getInstance().login(loginField.getText(), passwordField.getText());
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Внимание");
                alert.setHeaderText(null);
                alert.setContentText("Нет соединения с сервером !!!");
                alert.showAndWait();
                return;
            }

            if (isLogin!= null && isLogin) {
                okClicked = true;
                dialogStage.close();
                showContent();
            }
        });

        this.showRegisterBtn.setOnAction(action -> {
            showRegistration();
            dialogStage.close();
        });

    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
