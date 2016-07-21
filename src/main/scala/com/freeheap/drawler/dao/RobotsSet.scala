package com.freeheap.drawler.dao

import com.freeheap.drawler.drivers.{RedisConnection, RedisConnectionFactory}

/**
  * Created by william on 7/21/16.
  */
object RobotsSet {

  import com.freeheap.drawler.drivers.RedisConnection

  final val DEF_TIMEOUT = 2000

  def getDataFromSingle(conn: RedisConnection, hashName: String, key: String): Option[String] = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        Option(j.hget(hashName, key))
      case None => None
    }
  }

  def getDataFromCluster(conn: RedisConnection, hashName: String, key: String): Option[String] = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        Option(c.hget(hashName, key))
      case None => None
    }
  }


  def addDataToSingle(conn: RedisConnection, hashName: String, key: String, value: String): Unit = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        j.hset(hashName, key, value)
      case None =>
    }
  }

  def addDataToCluster(conn: RedisConnection, hashName: String, key: String, value: String): Unit = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        c.hset(hashName, key, value)
      case None =>
    }

  }

  def apply(connStr: String, hashName: String) = {
    new RobotsSet(RedisConnectionFactory(connStr), hashName)
  }
}

class RobotsSet(conn: RedisConnection, setName: String) {
  def getData(f: (RedisConnection, String, String) => Option[String])(data: String): Option[String] = {
    f(conn, setName, data)
  }

  def addSet(f: (RedisConnection, String, String) => Unit)(data: String): Unit = {
    f(conn, setName, data)
  }
}
