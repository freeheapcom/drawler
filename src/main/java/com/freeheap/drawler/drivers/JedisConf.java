package com.freeheap.drawler.drivers;

/**
 * Created by william on 7/11/16.
 * @name JedisConf
 */
public class JedisConf extends DbConf {

    public static final int MAX_RETRIES = 6;

    private int connTimeout;
    private int soTimeout;
    private int maxRetries;

    public JedisConf(int connTimeout, int soTimeout, int maxRetries) {
        this.connTimeout = connTimeout;
        this.soTimeout = soTimeout;
        this.maxRetries = maxRetries;
    }

    public JedisConf(int connTimeout, int soTimeout) {
        this(connTimeout, soTimeout, MAX_RETRIES);
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

}
