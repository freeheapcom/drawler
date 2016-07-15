package com.freeheap.drawler.drivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.HostAndPort;

/**
 * Created by william on 7/11/16.
 * @name Single Jedis Configuration
 */
public class SJConf extends JedisConf {

    private Set<HostAndPort> hosts;

    public SJConf(Set<HostAndPort> hosts, int connTimeout, int soTimeout) {
        super(connTimeout, soTimeout);
        this.hosts = hosts;
    }

    public SJConf(int connTimeout, int soTimeout, String connStr) {
        this(null, connTimeout, soTimeout);
        addHost(connStr);
    }

    public SJConf(String connStr) {
        this(DEFAULT_CONN_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, connStr);
    }

    public static final int DEFAULT_CONN_TIMEOUT = 10000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    public final synchronized void addHost(String connStr) {
        if (hosts == null) {
            hosts = new HashSet<>();
        }
        Map<String, Integer> m = DbConf.parseHost(connStr);

        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            hosts.add(new HostAndPort(entry.getKey(), entry.getValue()));
        }
    }

    public Set<HostAndPort> getHosts() {
        return hosts;
    }

    public void setHosts(Set<HostAndPort> hosts) {
        this.hosts = hosts;
    }

}
