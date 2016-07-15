package com.freeheap.drawler.dao

import com.freeheap.drawler.drivers.{RedisConnection, RedisConnectionFactory}

/**
  * Created by william on 7/11/16.
  */

object LinkQueue {
  final val DEF_TIMEOUT = 2000

  def getDataFromSingle(conn: RedisConnection, queueName: String): Option[String] = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        Option(j.lpop(queueName))
      case None => None
    }
  }

  def getDataFromCluster(conn: RedisConnection, queueName: String): Option[String] = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        Option(c.lpop(queueName))
      case None => None
    }
  }


  def pushDataToSingle(conn: RedisConnection, queueName: String, data: String): Unit = {
    Option(conn.getJedis(DEF_TIMEOUT)) match {
      case Some(j) =>
        j.lpush(queueName, data)
      case None =>
    }

  }

  def pushDataToCluster(conn: RedisConnection, queueName: String, data: String): Unit = {
    Option(conn.getCluster(DEF_TIMEOUT)) match {
      case Some(c) =>
        c.lpush(queueName, data)
      case None =>
    }
  }

  def apply(connStr: String, queueName: String) = {
    new LinkQueue(RedisConnectionFactory(connStr), queueName)
  }
}


class LinkQueue(conn: RedisConnection, queueName: String) {

  def popQueue(f: (RedisConnection, String) => Option[String]): Option[String] = {
    f(conn, queueName)
  }

  def pushQueue(f: (RedisConnection, String, String) => Unit)(data: String): Unit = {
    f(conn, queueName, data)
  }

}
