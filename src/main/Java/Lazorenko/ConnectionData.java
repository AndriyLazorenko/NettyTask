package Lazorenko;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that serves as a container for various connection data
 */

public class ConnectionData implements Serializable{

    /**
     * Variables
     */

    private String src_ip;
    private String URI;
    private Date timestamp;
    private long sent_bytes;
    private long received_bytes;
    private double speed=0;

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
        this.timestamp = new Date();
        this.sent_bytes = sent_bytes;
        this.received_bytes = received_bytes;
    }

    /**
     * Overrides toString() for better data representation
     * @return String object with data
     */

    @Override
    public String toString() {
        return "ConnectionData{" +
              "\t" + "src_ip='" + src_ip + '\'' +
              "\t" + ", URI='" + URI + '\'' +
              "\t" + ", timestamp=" + dateFormatter(timestamp) +
              "\t" + ", sent_bytes=" + sent_bytes +
              "\t" + ", received_bytes=" + received_bytes +
              "\t" + ", speed=" + speed +
                '}' +"\n";
    }

    /**
     * Formats date into readable String
     * @param date - Date object
     * @return String representation of Date object
     */

    private String dateFormatter(Date date){
        String forRet;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y HH:mm:ss");
        forRet = sdf.format(date);
        return forRet;
    }

}
