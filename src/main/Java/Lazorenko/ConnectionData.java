package Lazorenko;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
     * @param timeDifference - time difference in between block of code responsible for sending data to server
     *                       (long)
     */

    public ConnectionData(String src_ip, String URI, long sent_bytes, long received_bytes,
                          long timeDifference) {
        this.src_ip = src_ip;
        this.URI = URI;
        this.timestamp = new Date();
        this.sent_bytes = sent_bytes;
        this.received_bytes = received_bytes;
        this.speed = ((double) sent_bytes) / ((double) timeDifference / 1000000000);
    }

    /**
     * Overrides <code>toString()</code> for better data representation
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
              "\t" + ", speed (bytes/sec)=" + doubleFormatter(speed) +
                '}' +"\n";
    }

    /**
     * Formats <code>Date</code> object into readable String
     * @param date - Date object
     * @return String representation of Date object
     */

    private String dateFormatter(Date date){
        String forRet;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y HH:mm:ss");
        forRet = sdf.format(date);
        return forRet;
    }

    /**
     * Formats double for better view
     * @param d - double input variable
     * @return String object - formatted double
     */

    public String doubleFormatter(double d) {
        NumberFormat formatter = new DecimalFormat("#0");
        String forRet = formatter.format(d);
        return forRet;
    }

}
