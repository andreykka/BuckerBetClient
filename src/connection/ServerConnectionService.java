package connection;

import application.Main;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import org.apache.log4j.Logger;
import pojo.OutputData;
import pojo.RegistrationData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by gandy on 07.04.15.
 *
 */
public class ServerConnectionService {

    public static boolean successConnection = false;

    private static ServerConnectionService instance = new ServerConnectionService();
    public  static ServerConnectionService getInstance(){ return  instance;}
    private ServerConnectionService(){}

    private Logger logger = Logger.getLogger(getClass());

    private ExecutorService     executorService = Executors.newCachedThreadPool();
    private ServerConnection    connection;
    private PortListener        portListener;

    public void startConnection() {
        try {
            connection = new ServerConnection(SocketOpener.openNewSocket());
            portListener = new PortListener(SocketOpener.openNewSocket());

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошыбка соединения с сервером");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(Main.stage);
            alert.setHeaderText(null);

            alert.setContentText("Не удалось соединиться с сервером.\r\nПопробуйте позже.");
            alert.showAndWait();
            logger.error(e);
            successConnection = false;
            return;
        }

        executorService.submit(portListener);

        executorService.shutdown();
        logger.info("Connection success");
        successConnection = true;
    }

    public void stopConnection() {
        if (portListener != null)
            portListener.stopListen();
        if (connection != null)
            connection.closeConnection();
    }

    public Boolean login (String login, String password) throws NullPointerException {
        return connection.login(login,password);
    }

    public Boolean registerUser(RegistrationData regData){
        return connection.registerUser(regData);
    }

    public List<OutputData> getContentData(){
        return connection.getContentData();
    }


    public static boolean isSuccessConnection() {
        return successConnection;
    }
}
