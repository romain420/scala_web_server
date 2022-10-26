package webserver.library

import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.{Using, Random}
import scala.util.Using.Releasable

case class Client(ip: String, port: Int, message: String) {

  def start(): Unit = {
    Using(new Socket(ip, port)) { serverSocket =>
      println(s">>> 200: OK")
      processCommunication(serverSocket, message)
    }.fold(
      error => {
        println(s">>> Error 404: Page not found")
        println(s"Could not connect to the server, please check the Socket(ip and port) on the client side or that the server is on :). Error: ${error.getMessage}")
      },
      _ => ()
    )
  }

  def processCommunication(serverSocket: Socket, message: String): Unit = {

    Using(new PrintWriter(serverSocket.getOutputStream, true)) { out =>
      out.println(message)
      println(s">>> message sent to server")
      Using(new BufferedReader(InputStreamReader(serverSocket.getInputStream))) { in =>
        val answer: String = readAllBuffer("", in)
        println(s">>> we got an answer from server:\n\"$answer\"")
      }.fold(
        error => {
          println(s">>> 404: Page not found")
          println(s">>> cannot acquire the 'in', check the inputSteam of client: ${error.getMessage}")
        },
        _ => ()
      )
    }.fold(
      error => {
        println(s">>> 405: Method Not Allowed")
        println(s">>> could not send the message, check the 'out' of client: ${error.getMessage}")
      },
      _ => ()
    )
    ()
  }

  def createSendRequest(): Unit = {
    val request: WebRequest = createRandomRequest()
    request.debugMembers()
    val requestString: String = request.toString

    Using(new Socket(ip, port)) { serverSocket =>
      println(s">>> 200: OK")
      processCommunication(serverSocket, requestString)
    }.fold(
      error => {
        println(s">>> 404: Page not found")
        println(s"Could not connect to the server, please check the Socket(ip and port) on the client side or that the server is on :). Error: ${error.getMessage}")
      },
      _ => ()
    )
  }

  def createSendNameRequest(name: String): Unit = {
    val request: WebRequest = createNameRequest(name)
    request.debugMembers()
    val requestString: String = request.toString

    Using(new Socket(ip, port)) { serverSocket =>
      println(s">>> 200: OK")
      processCommunication(serverSocket, requestString)
    }.fold(
      error => {
        println(s">>> 404: Page not found")
        println(s"Could not connect to the server, please check the Socket(ip and port) on the client side or that the server is on :). Error: ${error.getMessage}")
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
    val message = "random_message"
    val request = WebRequest(method, path, version, host, message)
    request
  }

  def createNameRequest(name: String): WebRequest = {
    val method = "GET"
    val path = "name"
    val version = "HTTP/1.1"
    val host = "localhost"
    val request = WebRequest(method, path, version, host, name)
    request
  }

  def readAllBuffer(response: String, in: BufferedReader): String = {
    val to_add = in.readLine()
    if (in.ready() && to_add != null) {
      if (response == "") {
        val resp = to_add + "\r\n"
        readAllBuffer(resp, in)
      }
      else {
        val resp = response + " " + to_add + "\r\n"
        readAllBuffer(resp, in)
      }
    } else if (to_add != null) {
      if (response == "") {
        to_add + "\r\n"
      }
      else {
        response + " " + to_add + "\r\n"
      }
    }
    else response
  }
}