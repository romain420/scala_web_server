package webserver.library

import java.time.Instant
import webserver.library.WebResponse

case class WebRequest(method: String, path: String, version: String, host: String, message: String) {

  override def toString: String = {
    val str: String = method + " " + path + " " + version + "\r\n" + "Host: " + host + "\r\n" + "\r\n" + message
    str
  }

  def debugMembers(): Unit = {
    println("---------------\nDescription of the WebRequest:")
    println(s"method: $method")
    println(s"path: $path")
    println(s"version: $version")
    println(s"host: $host")
    println(s"message: $message")
    println("---------------")
  }

  def toWebResponse(code: String, status: String, message: String): WebResponse = {   //basic transformation
    val timeInMillis = System.currentTimeMillis()
    val currentDate = Instant.ofEpochMilli(timeInMillis)
    val response: WebResponse = WebResponse(version, code, status, currentDate, message)
    response
  }
}


