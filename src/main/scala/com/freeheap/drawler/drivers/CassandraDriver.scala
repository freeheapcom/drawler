package com.freeheap.drawler.drivers

import com.datastax.driver.core.{Cluster, ConsistencyLevel, QueryOptions}

/**
  * Created by william on 7/11/16.
  */
object CassandraDriver {
  def apply(connStr: String): CassandraDriver = new CassandraDriver(connStr)
}

class CassandraDriver(connStr: String) {
  val hosts = DbConf.parseHost(connStr)

  import scala.collection.JavaConversions._

  val cps = hosts.map(_._1).toArray[String]
  val p = hosts(cps(0))
  val defaultConsistencyLevel: ConsistencyLevel = QueryOptions.DEFAULT_CONSISTENCY_LEVEL
  val cluster = new Cluster.Builder().
    addContactPoints(cps: _*).
    withPort(p).
    withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build

  val session = cluster.connect
}
