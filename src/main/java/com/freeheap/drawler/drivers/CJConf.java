package com.freeheap.drawler.drivers;

import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

/**
 * Created by william on 7/11/16.
 * @name Cluster Jedis Configuration
 */
public class CJConf extends SJConf {

    public static final int MAX_REDIRECTION = 10;
    public static final int MAX_CONN_TIMEOUT = 10000;
    public static final int MAX_SOCKET_TIMEOUT = 10000;

    private GenericObjectPoolConfig poolConfig;
    private int maxRedirection;

    public CJConf(GenericObjectPoolConfig poolConfig, int maxRedirection,
            Set<HostAndPort> hosts, int connTimeout, int soTimeout) {
        super(hosts, connTimeout, soTimeout);
        this.poolConfig = poolConfig;
        this.maxRedirection = maxRedirection;
    }

    public CJConf(int connTimeout, int soTimeout, String connStr) {
        this(null, MAX_REDIRECTION, null, connTimeout, soTimeout);
        addHost(connStr);
    }

    public CJConf(String connStr) {
        this(DEFAULT_CONN_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, connStr);
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getMaxRedirection() {
        return maxRedirection;
    }

    public void setMaxRedirection(int maxRedirection) {
        this.maxRedirection = maxRedirection;
    }
}
