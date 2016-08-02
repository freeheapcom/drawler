package com.freeheap.drawler.dao

import com.freeheap.drawler.drivers.{RedisConnection, RedisConnectionFactory}

/**
  * Created by william on 7/11/16.
  */
object RedisSet {
  final val DEF_TIMEOUT = 2000

  def existsFromSingle(conn: RedisConnection, setName: String, data: String): Option[Boolean] = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        Option(j.sismember(setName, data))
      case None => None
    }
  }

  def existsFromCluster(conn: RedisConnection, setName: String, data: String): Option[Boolean] = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        Option(c.sismember(setName, data))
      case None => None
    }
  }


  def addDataToSingle(conn: RedisConnection, setName: String, data: String): Unit = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        j.sadd(setName, data)
      case None =>
    }
  }

  def addDataToCluster(conn: RedisConnection, setName: String, data: String): Unit = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        c.sadd(setName, data)
      case None =>
    }

  }

  def apply(connStr: String, setName: String) = {
    new RedisSet(RedisConnectionFactory(connStr), setName)
  }
}

class RedisSet(conn: RedisConnection, setName: String) {
  def exists(f: (RedisConnection, String, String) => Option[Boolean])(data: String): Boolean = {
    f(conn, setName, data).getOrElse(false)
  }

  def addSet(f: (RedisConnection, String, String) => Unit)(data: String): Unit = {
    f(conn, setName, data)
  }
}
