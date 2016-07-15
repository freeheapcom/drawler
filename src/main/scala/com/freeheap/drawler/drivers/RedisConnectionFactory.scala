package com.freeheap.drawler.drivers

import java.util.Properties

/**
  * Created by william on 7/11/16.
  */
object RedisConnectionFactory {
  def apply(redisHost: String, cluster: Boolean = false): RedisConnection = {
    val conf = if (cluster) new CJConf(redisHost) else new SJConf(redisHost)
    val conn = new RedisConnection(conf)
    conn.start()
    conn
  }
}
