package Lazorenko;

import java.util.Date;

/**
 * Class that serves as a container for various connection data
 */

public class ConnectionData {

    /**
     * Variables
     */

    private String src_ip;
    private String URI;
    private Date timestamp;
    private long sent_bytes;
    private long received_bytes;
    private double speed;

    /**
     * Constructor of class
     * @param src_ip - IP in form of String
     * @param URI - URI in form of String
     * @param sent_bytes - bytes sent (long)
     * @param received_bytes - bytes received (long)
     */

    public ConnectionData(String src_ip, String URI, long sent_bytes, long received_bytes) {
        this.src_ip = src_ip;
        this.URI = URI;
        timestamp = new Date();
        this.sent_bytes = sent_bytes;
        this.received_bytes = received_bytes;
    }


}
