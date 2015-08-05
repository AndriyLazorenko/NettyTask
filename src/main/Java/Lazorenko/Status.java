package Lazorenko;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Container class for handling data related to status request from server as mentioned in task
 * @author andriylazorenko
 */

public class Status implements Serializable {

    /**
     * Singleton pattern implementation
     */
    
    private volatile static Status singleton;

    private Status(){
    }

    public static Status getInstance(){
        if (singleton==null){
            synchronized (Status.class){
                if (singleton==null){
                    singleton = fromFile();
                }
            }
        }
        return singleton;
    }

    /**
     * Counters as variables and data structures
     */

    private int requestsCounter = 0;
    private int uniqueRequestsCounter = 0;
    private Set<Requests> requests = new HashSet<>();
    private Map<String,Integer> redirects = new HashMap<>();
    private int connections = 0;
    private Queue<ConnectionData> log = new LinkedBlockingQueue<>();

    public void update(ChannelHandlerContext ctx, HttpRequest msg){
        requestsCounter++;
        Requests req = new Requests(ctx.channel().remoteAddress().toString());
        toFile();
    }

    private void toFile(){
        File storage = new File("src/main/resources/Output.data");
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(storage));
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Status fromFile(){
        File storage = new File("src/main/resources/Output.data");
        ObjectInputStream objectInputStream = null;
        Status forRet = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(storage));
            forRet = (Status) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (forRet==null){
            forRet = new Status();
        }
        return forRet;
    }
}