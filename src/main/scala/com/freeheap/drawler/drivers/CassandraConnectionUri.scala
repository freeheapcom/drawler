package com.freeheap.drawler.drivers

import java.net.URI

/**
  * Created by william on 7/11/16.
  */

case class CassandraConnectionUri(connectionString: String) {

  private val uri = new URI(connectionString)

  private val additionalHosts = Option(uri.getQuery) match {
    case Some(query) => query.split('&').map(_.split('=')).filter(param => param(0) == "host").map(param => param(1)).toSeq
    case None => Seq.empty
  }

  val host = uri.getHost
  val hosts = Seq(uri.getHost) ++ additionalHosts
  val port = uri.getPort
  val keyspace = uri.getPath.substring(1)

}
