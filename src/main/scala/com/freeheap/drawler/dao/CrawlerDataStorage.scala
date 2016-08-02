package com.freeheap.drawler.dao

import com.datastax.driver.core.querybuilder.QueryBuilder
import com.freeheap.drawler.common.{CrawledData, CrawledDataFullInfo}
import com.freeheap.drawler.drivers.CassandraDriver

/**
  * Created by william on 7/11/16.
  */

object CrawlerDataStorage {
  def apply(connStr: String, ks: String, tbl: String) = new CrawlerDataStorage(connStr, ks, tbl)
}

class CrawlerDataStorage(connStr: String, ks: String, tbl: String) {

  val driver = CassandraDriver(connStr)

  def initKS(): Unit = {

  }

  def saveData(cdfi: CrawledDataFullInfo): Unit = {
    val statement = QueryBuilder.insertInto(ks, tbl)
      .value("domain", cdfi.domain)
      .value("url", cdfi.url)
      .value("content", cdfi.content)
      .value("crawled_time", cdfi.crawledTime)
      .value("outlink", scala.collection.JavaConversions.setAsJavaSet(cdfi.outlink))

    driver.session.execute(statement)
  }

  def saveData(cd: CrawledData): Unit = {
    val fi = CrawledData.toFullInfo(cd)
    saveData(fi)
  }
}
