package webserver.library

import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.{Using, Random}
import scala.util.Using.Releasable

case class Client(ip: String, port: Int, message: String) {

  def start(): Unit = {
    Using(new Socket(ip, port)) { serverSocket =>
      processCommunication(serverSocket, message)
    }.fold(
      error => {
        println("Could not connect to the server, please check the Socket(ip and port) on the client side or that the server is on :).")
      },
      _ => ()
    )
  }

  def processCommunication(serverSocket: Socket, message: String): Unit = {

    Using(new PrintWriter(serverSocket.getOutputStream, true)) { out =>
      out.println(message)
      println(s"Message sent to server")
      Using(new BufferedReader(InputStreamReader(serverSocket.getInputStream))) { in =>
        //println(s"We check of the Socket is still open, is Socket open in processCommunication? ${serverSocket.isConnected()}")
        val answer: String = in.readLine
        println(s"We got an answer from server:\n\t\"$answer\"")
      }.fold(
        error => {
          println(s">>> cannot acquire the 'in', check the inputSteam of client: ${error.getMessage}")
        },
        _ => ()
      )
    }.fold(
      error => {
        println(s">>> could not send the message, check the 'out' of client: ${error.getMessage}")
      },
      _ => ()
    )
    ()
  }

  // DEPRECATED
  def processRequest(): Unit = {    // do not use that anymore, use createSendRequest instead
    Using(new Socket(ip, port)) { serverSocket =>
      val request = createRandomRequest()
      println(s"the new random request is:\n\n$request")
    }.fold(
      error => {
        println("Could not connect to the server, please check the Socket(ip and port) on the client side or that the server is on :).")
      },
      _ => ()
    )
  }

  def createSendRequest(): Unit = {
    val request: WebRequest = createRandomRequest()
    request.debugMembers
    val requestString: String = request.toString

    Using(new Socket(ip, port)) { serverSocket =>
      processCommunication(serverSocket, requestString)
    }.fold(
      error => {
        println("Could not connect to the server, please check the Socket(ip and port) on the client side or that the server is on :).")
      },
      _ => ()
    )

  }

  def createRandomRequest(): WebRequest = {

    /*
    request template:
    GET /index.html HTTP/1.1 <CRLF>
    Host: localhost <CRLF>
    <CRLF>
    */

    val random = new Random
    val list_method: List[String] = List("GET", "PUT", "DELETE", "POST")

    val method = list_method(random.nextInt(list_method.length))
    val path = "/users"
    val version = "HTTP/1.1"
    val host = "localhost"
    val message = "random message"
    val request = WebRequest(method, path, version, host, message)

    request
  }
}