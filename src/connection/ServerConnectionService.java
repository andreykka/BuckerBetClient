package connection;

import org.apache.log4j.Logger;
import pojo.OutputData;
import pojo.RegistrationData;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by gandy on 07.04.15.
 *
 */
public class ServerConnectionService {

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
            JOptionPane.showMessageDialog(null, "Не удалось соединиться с сервером", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.error(e);
        }

        executorService.submit(portListener);

        executorService.shutdown();
        logger.info("Connection success");
    }

    public void stopConnection() {
        portListener.stopListen();
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

}
