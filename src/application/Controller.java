package application;

import connection.PortListener;
import connection.ServerConnection;
import connection.ServerConnectionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import listeners.DataListener;
import pojo.DateConverter;
import pojo.OutputData;
import pojo.RegistrationData;
import pojo.TimeConverter;

import javax.swing.*;
import java.net.URL;
import java.time.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    private class DataListenerImpl implements DataListener{

        @Override
        public void dataObtained(List<OutputData> data) {
            tableView.getItems().clear();
            tableView.getItems().addAll(data);
        }
    }

    // окно главной программы
    public static Stage stage;
    // content pane
    @FXML
    private AnchorPane                      contentPane;
    @FXML
    private MenuItem                        refreshMenuItem;
    @FXML
    private MenuItem                        exitMenuItem;
    @FXML
    private Menu                            refreshMenu;
    @FXML
    private TableView<OutputData>           tableView;
    @FXML
    private TableColumn<Object, Object>     eventCol;
    @FXML
    private TableColumn<Object, LocalDate>  dateCol;
    @FXML
    private TableColumn<Object, LocalTime>  timeCol;
    @FXML
    private TableColumn<Object, Object>     resultCol;

    // login panne
    @FXML
    private AnchorPane      loginPane;
    @FXML
    private TextField       loginField;
    @FXML
    private PasswordField   passwordField;
    @FXML
    private Button          loginBtn;
    @FXML
    private Label           showRegisterPaneLabel;

    // registration pane
    @FXML
    private AnchorPane      registerPane;
    @FXML
    private TextField       regUserSurnameField;
    @FXML
    private TextField       regUserNameField;
    @FXML
    private TextField       regUserTelField;
    @FXML
    private TextField       regUserEmailField;
    @FXML
    private TextField       regUserLoginField;
    @FXML
    private PasswordField   regUserPasswordField;
    @FXML
    private Button          registerBtn;
    @FXML
    private Label           showLoginPaneLabel;

    private Pattern numbPat = Pattern.compile("[0-9]");
    private Pattern namePat = Pattern.compile("^[а-яА-ЯёЁіІїЇa-zA-Z]{0,25}$");
    private Pattern mailPat = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
    private Pattern loginPat = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_\\.]{0,20}$");
    //private Pattern passPat = Pattern.compile("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");

    private Matcher matcher;

    private ServerConnectionService connectionService = ServerConnectionService.getInstance();

    private boolean isFilledLoginData(){
        return !(loginField.getText().isEmpty() || passwordField.getText().isEmpty());
    }

    private boolean isFilledRegisterData(){
        return !(
                    regUserNameField.getText().isEmpty()
                ||  regUserSurnameField.getText().isEmpty()
                ||  regUserEmailField.getText().isEmpty()
                ||  regUserTelField.getText().isEmpty()
                ||  regUserLoginField.getText().isEmpty()
                ||  regUserPasswordField.getText().isEmpty()
        ) ;
    }

    private void showRegistration(){
        this.loginPane.setVisible(false);
        this.contentPane.setVisible(false);
        this.registerPane.setVisible(true);
        stage.hide();
        stage.setWidth(550);
        stage.setHeight(460);
        stage.setX((Screen.getPrimary().getBounds().getWidth() - stage.getWidth()) / 2);
        stage.setY((Screen.getPrimary().getBounds().getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    private void showLogin(){
        this.contentPane.setVisible(false);
        this.registerPane.setVisible(false);
        this.loginPane.setVisible(true);
        stage.hide();
        stage.setWidth(500);
        stage.setHeight(450);
        stage.setX((Screen.getPrimary().getBounds().getWidth() - stage.getWidth()) / 2);
        stage.setY((Screen.getPrimary().getBounds().getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    private void showContent(){
        this.loginPane.setVisible(false);
        this.registerPane.setVisible(false);
        this.contentPane.setVisible(true);
        stage.hide();
        stage.setWidth(900);
        stage.setHeight(600);
        stage.setX((Screen.getPrimary().getBounds().getWidth() - stage.getWidth()) / 2);
        stage.setY((Screen.getPrimary().getBounds().getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        PortListener.addListener(new DataListenerImpl());

        this.eventCol.setCellValueFactory(new PropertyValueFactory<>("event"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.dateCol.setCellFactory(param -> new ComboBoxTableCell<Object, LocalDate>(new DateConverter()));
        this.timeCol.setCellFactory(param -> new ComboBoxTableCell<Object, LocalTime>(new TimeConverter()));

        this.tableView.getItems().clear();

        showRegisterPaneLabel.setOnMouseClicked(action -> {
            this.loginField.clear();
            this.passwordField.clear();
            this.showRegistration();
        });

        showLoginPaneLabel.setOnMouseClicked(action -> {
            this.regUserNameField.clear();
            this.regUserSurnameField.clear();
            this.regUserEmailField.clear();
            this.regUserLoginField.clear();
            this.regUserPasswordField.clear();
            this.regUserTelField.clear();
            this.showLogin();
        });

//        this.refreshMenu.setOnMenuValidation(event -> getDataFromServer() );
        this.refreshMenu.setOnAction(action -> getDataFromServer());

        this.refreshMenuItem.setOnAction(action -> getDataFromServer());
        this.exitMenuItem.setOnAction(action -> {
            ServerConnectionService.getInstance().stopConnection();
            stage.close();

        });

        this.regUserTelField.setOnKeyReleased(event ->{
            if (this.regUserTelField.getText().length() > 14 ){
                this.regUserTelField.setText(this.regUserTelField.getText().substring(0, 15));
            }
            for (int i=0; i< this.regUserTelField.getLength(); i++){
                matcher = numbPat.matcher(this.regUserTelField.getText().substring(i,i+1));
                if (!matcher.matches()){
                    if (i < 1) {
                        this.regUserTelField.setText(this.regUserTelField.getText().substring(i));
                    }
                    this.regUserTelField.setText(this.regUserTelField.getText().substring(0,i) +
                                                this.regUserTelField.getText().substring(i+1));
                }
            }
        });

        this.regUserLoginField.setOnKeyReleased(event ->{
            TextField field  = null;
            if (event.getSource() instanceof  TextField ){
                field = (TextField) event.getSource();
            } else {
                System.out.println("Not a TextField event (");
                throw new Error("Event not a 'TextField type' FIX IT please!!");
            }
            while (field.getText().length() > 0) {
                matcher = loginPat.matcher(field.getText());
                if (matcher.matches())
                    break;
                field.setText(field.getText().substring(0, field.getText().length()-1));
                field.positionCaret(field.getLength());
            }
        });
        this.loginField.setOnKeyReleased(this.regUserLoginField.getOnKeyReleased());

        this.regUserNameField.setOnKeyReleased(event ->{
            TextField field  = null;
            if (event.getSource() instanceof  TextField ){
                field = (TextField) event.getSource();
            } else {
                System.out.println("Not a TextField event (");
                throw new Error("Event not a 'TextField type' FIX IT please!!");
            }
            while (field.getText().length() > 0) {
                matcher = namePat.matcher(field.getText());
                if (matcher.matches())
                    break;
                field.setText(field.getText().substring(0, field.getText().length()-1));
                field.positionCaret(field.getLength());
            }
        });

        this.regUserSurnameField.setOnKeyReleased(this.regUserNameField.getOnKeyReleased());

        loginBtn.setOnAction(event -> {
            if (!isFilledLoginData()){
                JOptionPane.showMessageDialog(null, "Необходимо заполнить все поля");
                return;
            }

            Boolean isRegister = connectionService.login(this.loginField.getText(), this.passwordField.getText());
            if (isRegister == null || (isRegister != null  && !isRegister)){
               /* JOptionPane.showMessageDialog(null, "Введены не верные даные\r\nПовторите попытку!!!");
                System.out.println("user is not registred");*/
                return;
            }
            showContent();
            getDataFromServer();
        });

        registerBtn.setOnAction(action -> {
            if (!isFilledRegisterData()) {
                JOptionPane.showMessageDialog(null, "Необходимо заполнить все поля");
                return;
            }
            RegistrationData user = new RegistrationData();

            user.setSurname(regUserSurnameField.getText());
            user.setName(regUserNameField.getText());
            user.setEMail(regUserEmailField.getText());
            user.setTel(regUserTelField.getText());
            user.setLogin(regUserLoginField.getText());
            user.setPassword(regUserPasswordField.getText());

            Boolean result = this.connectionService.registerUser(user);
            if (result != null && result ) {
                this.showLogin();
            }
        });

    }

    private void getDataFromServer(){
        this.tableView.getItems().clear();
        List<OutputData> arr = connectionService.getContentData();
        System.out.println("array size: " + arr.size());
        this.tableView.getItems().addAll(arr);
    }
}
