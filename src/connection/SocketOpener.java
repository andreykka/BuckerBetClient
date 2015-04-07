package connection;

import config.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by gandy on 07.04.15.
 *
 */

public class SocketOpener  {

//    private static SocketOpener opener = new SocketOpener();
    private  SocketOpener(){}

    private static final int                PORT    = Config.PORT();        // port server
    private static final String             ADDRESS = Config.IP_ADDRESS();  // ip address server
//
//    public static SocketOpener getInstance() {
//        return opener;
//    }

    public static Socket openNewSocket() throws IOException{

        InetAddress inetAddress = InetAddress.getByName(ADDRESS);
        Socket socket = new Socket(inetAddress, PORT);
        return socket;

    }

}
