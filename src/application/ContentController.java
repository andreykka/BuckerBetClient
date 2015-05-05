package application;

import connection.PortListener;
import connection.ServerConnectionService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import listeners.DataListener;
import pojo.DateConverter;
import pojo.OutputData;
import pojo.TimeConverter;

import java.net.URL;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ContentController implements Initializable {

    private class DataListenerImpl implements DataListener{
        @Override
        public void dataObtained(List<OutputData> data) {
            tableView.getItems().clear();
            tableView.getItems().addAll(data);
        }
    }

    @FXML private MenuItem                        refreshMenuItem;
    @FXML private MenuItem                        exitMenuItem;
    @FXML private TableView<OutputData>           tableView;
    @FXML private TableColumn<Object, Object>     eventCol;
    @FXML private TableColumn<Object, LocalDate>  dateCol;
    @FXML private TableColumn<Object, LocalTime>  timeCol;
    @FXML private TableColumn<Object, Object>     resultCol;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        PortListener.addListener(new DataListenerImpl());

        this.eventCol.setCellValueFactory(new PropertyValueFactory<>("event"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.dateCol.setCellFactory(param -> new ComboBoxTableCell<>(new DateConverter()));
        this.timeCol.setCellFactory(param -> new ComboBoxTableCell<>(new TimeConverter()));

        this.tableView.getItems().clear();

        this.refreshMenuItem.setOnAction(action -> getDataFromServer());

        this.tableView.setRowFactory(new Callback<TableView<OutputData>, TableRow<OutputData>>() {
            @Override
            public TableRow<OutputData> call(TableView<OutputData> param) {
                final TableRow<OutputData> row = new TableRow<OutputData>(){
                    @Override
                    protected void updateItem(OutputData item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null)
                            return;

                        ObservableList<String> styleClass = getStyleClass();

                        if (item.getDate().isEqual(LocalDate.now())) {
                            if (!styleClass.contains("todayMatch")) {
                                styleClass.add("todayMatch");
                            }
                        } else if (item.getDate().isEqual(LocalDate.now().plusDays(1))){
                            if (!styleClass.contains("lastMath")) {
                                styleClass.add("lastMath");
                            }
                        } else {
                            getStyleClass().removeAll(Collections.singleton("lastMath"));
                            getStyleClass().removeAll(Collections.singleton("todayMatch"));
                        }
                    }
                };
                return row;
            }
        });

        this.exitMenuItem.setOnAction(action -> {
            ServerConnectionService.getInstance().stopConnection();
            stage.close();
        });
        getDataFromServer();
    }

    private void getDataFromServer(){
        List<OutputData> arr;
        try {
            arr = ServerConnectionService.getInstance().getContentData();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Внимание");
            alert.setHeaderText(null);
            alert.setContentText("Нет соединения с сервером !!!");
            alert.showAndWait();
            return;
        }

        System.out.println("array size: " + arr.size());
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(arr);
    }
}
