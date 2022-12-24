package Project.Proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ProxyServer {
    private static HashMap<String,ProxyCacheItem> map= new HashMap<String,ProxyCacheItem>();
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);

        while(true) {
            Socket client = socket.accept();
            new ServerHandler(client).start();
        }
    }
    public static synchronized HashMap<String, ProxyCacheItem> getCache(){
        return map;
    }
}