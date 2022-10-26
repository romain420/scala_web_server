package webserver.library

import java.time.Instant

case class WebResponse(version: String, code: String, status:String, date: Instant, message: String) {

  override def toString: String = {
    val str: String = version + " " + code + " " + status + "\r\n" + "Date: " + date + "\r\n" + "Content-Type: plain/text" + "\r\n" + "\r\n" + status + "\r\n" + message
    str
  }

  def debugMembers(): Unit = {
    println("---------------\nDescription of the WebResponse:")
    println(s"version: $version")
    println(s"code: $code")
    println(s"status: $status")
    println(s"date: $date")
    println("---------------")
  }
}