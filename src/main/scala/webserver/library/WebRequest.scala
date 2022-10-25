package webserver.library

case class WebRequest(method: String, path: String, version: String, host: String, message: String) {

  override def toString: String = {
    val str: String = method + " " + path + " " + version + " " + "Host: " + host + " " + " " + message
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
}


