package com.freeheap.drawler.common

import java.net.{URI, URL}

/**
  * Created by william on 7/15/16.
  */
case class CrawledData(url: String, content: String, crawledTime: Long, outlink: Set[String] = Set())

case class CrawledDataFullInfo(domain: String, url: String, content: String, crawledTime: Long, outlink: Set[String] = Set())

object CrawledData {
  def toFullInfo(data: CrawledData): CrawledDataFullInfo = {
    val url = new URL(data.url)
    CrawledDataFullInfo(url.getHost, data.url, data.content, data.crawledTime, data.outlink)
  }
}
