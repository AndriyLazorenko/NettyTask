package Lazorenko;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that serves as a request counter
 * @author andriylazorenko
 */
public class Requests implements Serializable {

    /**
     * Variables
     */

    private String ip;
    private int requestsOnIp=0;
    private Date lastRequestTime;

    /**
     * Class constructor
     * @param ip - String with IP info
     */

    public Requests (String ip){
        this.ip = ip;
        requestsOnIp++;
        lastRequestTime = new Date();
    }

    /**
     * Getters
     * @return
     */

    public String getIp() {
        return ip;
    }

    public int getRequestsOnIp() {
        return requestsOnIp;
    }

    public Date getLastRequestTime() {
        return lastRequestTime;
    }

    /**
     * Special class for comparison between two requests.
     * @param req - Requests object to compare to current
     * @returns TRUE or FALSE
     */

    public boolean equals(Requests req) {
        if (req.getIp().equals(this.getIp())){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Setters
     * @param requestsOnIp
     */

    public void setRequestsOnIp(int requestsOnIp) {
        this.requestsOnIp = requestsOnIp;
    }

    public void setLastRequestTime(Date lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    /**
     * Overrides toString() for better data representation
     * @return String object with all data off this Request object fields
     */

    @Override
    public String toString() {
        return "Requests{" +
              "\t" + "ip='" + ip + '\'' +
              "\t" + ", requestsOnIp=" + requestsOnIp +
              "\t" + ", lastRequestTime=" + dateFormatter(lastRequestTime) +
                '}' + "\n";
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
