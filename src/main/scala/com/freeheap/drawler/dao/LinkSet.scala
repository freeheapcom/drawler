package com.freeheap.drawler.dao

import com.freeheap.drawler.drivers.{RedisConnection, RedisConnectionFactory}

/**
  * Created by william on 7/11/16.
  */
object LinkSet {
  final val DEF_TIMEOUT = 2000

  def chckExistsFromSingle(conn: RedisConnection, queueName: String, data: String): Option[Boolean] = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        Option(j.sismember(queueName, data))
      case None => None
    }
  }

  def chckExistsFromCluster(conn: RedisConnection, queueName: String, data: String): Option[Boolean] = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        Option(c.sismember(queueName, data))
      case None => None
    }
  }


  def addDataToSingle(conn: RedisConnection, queueName: String, data: String): Unit = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        j.sadd(queueName, data)
      case None =>
    }
  }

  def addDataToCluster(conn: RedisConnection, queueName: String, data: String): Unit = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        c.sadd(queueName, data)
      case None =>
    }

  }

  def apply(connStr: String, queueName: String) = {
    new LinkSet(RedisConnectionFactory(connStr), queueName)
  }
}

class LinkSet(conn: RedisConnection, setName: String) {
  def exists(f: (RedisConnection, String, String) => Option[Boolean])(data: String): Boolean = {
    f(conn, setName, data).getOrElse(false)
  }

  def addSet(f: (RedisConnection, String, String) => Unit)(data: String): Unit = {
    f(conn, setName, data)
  }
}
