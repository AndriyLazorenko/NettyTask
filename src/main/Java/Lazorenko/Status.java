package Lazorenko;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

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
    private Set<Requests> requestsData = new HashSet<>();
    private Map<String,Integer> redirects = new HashMap<>();
    private int connections = 0;
    private Queue<ConnectionData> log = new LinkedBlockingQueue<>();

    /**
     * Method for updating Status object
     * @param ctx - ChannelHandlerContext object
     * @param msg - HttpRequest object
     */

    public void update(ChannelHandlerContext ctx, HttpRequest msg, HttpResponse resp){
        requestsCounter++;
        Requests currentReq = requestsDataAdd(
                new Requests(extractIp(ctx.channel().remoteAddress().toString())));
        uniqueRequestsCounter = requestsData.size();
        redirectsCounting(msg);
        getNumberOfConnections();
        connectionDataAdd(currentReq, msg, resp);
        toFile();
    }

    /**
     * Processes a request according to uniqueness of IP
     * @param req - Requests object
     * @return Requests object with current request
     */

    private Requests requestsDataAdd(Requests req){
        Requests forRet = null;
        boolean hasReq = false;
        for (Requests r : requestsData) {
            if (r.equals(req)){
                hasReq = true;
                r.setLastRequestTime(new Date());
                r.setRequestsOnIp(r.getRequestsOnIp()+1);
                forRet = r;
                break;
            }
        }
        if (!hasReq){
            requestsData.add(req);
            forRet = req;
        }
        return forRet;
    }

    /**
     * Extracts IP form String value of remoteAddress method
     * @param remoteAddress - remoteAddress method of chanel
     * @returns IP in form of String
     */

    private String extractIp (String remoteAddress){
        StringBuilder sb = new StringBuilder();
        sb.append(remoteAddress.substring(1));
        String forRet = sb.substring(0,sb.indexOf(":"));
        System.out.println(forRet);
        return forRet;
    }

    /**
     * Processes a request according to uniqueness of redirected URL's
     * @param req - HttpRequest object
     */

    private void redirectsCounting (HttpRequest req){
        if (req.getUri().contains("/redirect?url=")){
            String modifiedUri = req.getUri().replaceAll("%3C","<").replaceAll("%3E", ">");
            String url = modifiedUri.substring(modifiedUri.indexOf("<")+1,modifiedUri.lastIndexOf(">"));
            if(redirects.keySet().contains(url)){
                int counter = redirects.get(url);
                redirects.replace(url,counter++);
            }
            else {
                redirects.put(url,1);
            }
        }
    }

    /**
     * Processes the data for representation in required format (through queue of last 16 connections)
     * @param currentReq - Requests object formed from incoming ChannelHandlerContext object
     * @param msg - HttpRequests object
     * @param resp - HttpResponse object
     */

    private void connectionDataAdd(Requests currentReq, HttpRequest msg, HttpResponse resp){
        String ip = currentReq.getIp();
        String URI = msg.getUri();
        long bytesSent = Long.parseLong(resp.headers().get(CONTENT_LENGTH));
        long bytesReceived;
        try {
            bytesReceived = Long.parseLong(msg.headers().get(CONTENT_LENGTH));
        } catch (NumberFormatException e){
            bytesReceived=0;
        }
        ConnectionData newData = new ConnectionData(ip,URI,bytesSent,bytesReceived);
        log.offer(newData);
        if (log.size()>16){
            log.poll();
        }
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
            System.err.print("Status object created");
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
                "\n"+"requestsCounter=" + requestsCounter +
                "\n"+", uniqueRequestsCounter=" + uniqueRequestsCounter +
                "\n"+", log=" + log.toString() +
                "\n"+", requestsData=" + requestsData.toString() +
                "\n"+", redirects=" + redirects.toString() +
                "\n"+", connections=" + connections +
                "\n"+'}';
    }

    /**
     * Updates variable <code>connections</code> with data on current number of connections
     */

    private void getNumberOfConnections (){
        connections=HttpServerInitializer.channels.size();
    }

}
