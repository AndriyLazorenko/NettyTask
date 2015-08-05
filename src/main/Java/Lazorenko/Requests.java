package Lazorenko;

import java.util.Date;

/**
 * Class that serves as a request counter
 * @author andriylazorenko
 */
public class Requests {

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
}
