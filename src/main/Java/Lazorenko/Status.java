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
     * Singleton pattern implementation with loading from file
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

    /**
     * Method for updating Status object
     * @param ctx - ChannelHandlerContext object
     * @param msg - HttpRequest object
     */

    public void update(ChannelHandlerContext ctx, HttpRequest msg){
        requestsCounter++;
        Requests req = new Requests(ctx.channel().remoteAddress().toString());
        toFile();
    }

    /**
     * Method for storing data present in Status object in a file
     */

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

    /**
     * Method for loading serialized data of Status object back to the status object
     * @returns Status object
     */

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

    /**
     * Response forming String
     * @returns String object ready for response
     */

    @Override
    public String toString() {
        return "Status{" +
                "requestsCounter=" + requestsCounter +
                ", uniqueRequestsCounter=" + uniqueRequestsCounter +
                ", log=" + log +
                ", requests=" + requests +
                ", redirects=" + redirects +
                ", connections=" + connections +
                '}';
    }
}
