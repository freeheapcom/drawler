package com.freeheap.drawler.drivers;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by william on 7/11/16.
 * @name RedisConnection
 */
public class RedisConnection extends Thread implements Closeable {

    public static enum RedisClientMode {

        Single, Cluster
    }

    private Jedis jedis;
    private SJConf sconf;
    private CJConf cconf;
    private JedisCluster cluster;
    private RedisClientMode mode;
    private int maxRetries;

    public RedisConnection(DbConf conf) throws InvalidConf {
        super("Redis-Connection");
        this.counter = new AtomicInteger();
        this.errorCounter = new AtomicInteger();
        if (conf != null && conf instanceof CJConf) {
            this.cconf = (CJConf) conf;
            this.mode = RedisClientMode.Cluster;
            if (!validateConf(cconf)) {
                throw new InvalidConf("Empty host field");
            }
            this.sconf = null;
            maxRetries = cconf.getMaxRetries();
        } else if (conf != null && conf instanceof SJConf) {
            this.sconf = (SJConf) conf;
            this.mode = RedisClientMode.Single;
            if (!validateConf(sconf)) {
                throw new InvalidConf("Empty host field");
            }
            maxRetries = sconf.getMaxRetries();
            this.cconf = null;
        } else {
            String name;
            if (conf != null) {
                name = conf.getClass().getCanonicalName();
            } else {
                name = null;
            }
            throw new InvalidConf("Invalid configuration for redis. Passed configuration is : " + name);
        }
    }

    private boolean isConnected;

    private boolean isConnected() {
        return isConnected;
    }

    private synchronized void connectIfNeeded() {
        if (!isConnected()) {
            switch (this.mode) {
                case Cluster: {
                    try {
                        cluster = newCluster();
                        isConnected = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case Single: {
                    try {
                        jedis = newJedis();
                        isConnected = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    // check hosts
    private boolean validateConf(SJConf conf) {
        return conf != null && !conf.getHosts().isEmpty();
    }

    /**
     * Warning: using this method carefully, because it should be called by
     * internal, using the thread model for connection management only
     *
     * @return
     */
    public Jedis newJedis() {
        if (sconf != null) {
            for (HostAndPort hap : sconf.getHosts()) {
                return new Jedis(hap.getHost(), hap.getPort(), sconf.getConnTimeout(), sconf.getSoTimeout());
            }
        }
        return null;

    }

    /**
     * Warning: using this method carefully, because it should be called by
     * internal, using the thread model for connection management only
     *
     * @return
     */
    public JedisCluster newCluster() {
        JedisCluster c;
        if (cconf != null) {
            if (cconf.getPoolConfig() != null) {
                c = new JedisCluster(cconf.getHosts(), cconf.getConnTimeout(),
                        cconf.getSoTimeout(), cconf.getMaxRedirection(), cconf.getPoolConfig());
            } else {
                c = new JedisCluster(cconf.getHosts(), cconf.getConnTimeout(), cconf.getMaxRedirection());
            }
            return c;
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
            if (!isConnected()) {
                connectIfNeeded();
                counter.set(0);
                if (!isConnected()) {
                    errorCounter.incrementAndGet();
                    if (errorCounter.get() == maxRetries) {
                        System.out.println("Cannot connect to redis in " + errorCounter.get() + " retries.");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                        }
                    }
                } else {
                    System.out.println("Connect to Redis successfully.");
                }
            } else {
                counter.incrementAndGet();
                errorCounter.set(0);
                // ping
//            if (counter.get() % 1000 == 0) {
//                switch (mode) {
//                    case Cluster: {
//                        if ((cluster == null)) {
//                            failed();
//                        }
//                    }
//                    case Single: {
//                        if ((jedis == null) || (!jedis.ping().equals("PONG"))) {
//                            failed();
//                        }
//                        break;
//                    }
//                }
//            }
            }

                ssleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final AtomicInteger counter;
    private final AtomicInteger errorCounter;

    @Override
    public void close() throws IOException {
        try {
            if (jedis != null) {
                jedis.close();
            }
            if (cluster != null) {
                cluster.close();
            }
        } finally {
            jedis = null;
            cluster = null;
            isConnected = false;
        }
    }

    public synchronized void failed() {
        this.isConnected = false;
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final int DEFAULT_GET_TIMEOUT = 500;
    public static final int SLICETIMEOUT = 20;

    private void ssleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e){}
    }
    public Jedis getJedis(int timeout) {
        while (!isConnected()) {
            ssleep(SLICETIMEOUT);
            timeout -= SLICETIMEOUT;
            if (timeout <= 0) {
                return null;
            }
        }

        return jedis;
    }

    public Jedis getJedis() {
        return getJedis(DEFAULT_GET_TIMEOUT);
    }

    public JedisCluster getCluster() {
        return getCluster(DEFAULT_GET_TIMEOUT);
    }

    public JedisCluster getCluster(int timeout) {
        while (!isConnected()) {
            ssleep(SLICETIMEOUT);
            timeout -= SLICETIMEOUT;
            if (timeout <= 0) {
                return null;
            }
        }
        return cluster;
    }

}
