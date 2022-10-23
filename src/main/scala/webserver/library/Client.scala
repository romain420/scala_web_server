package webserver.library

import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.{Using, Random}
import scala.util.Using.Releasable

case class Client(ip: String, port: Int, message: String) {

  def processRequest: Unit = {
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

  def start: Unit = {
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
      println(s"Message sent to server")
      out.println(message)
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

  def createRandomRequest(): String = {

    /*
    template for a request:
    GET /index.html HTTP/1.1 <CRLF>
    Host: localhost <CRLF>
    <CRLF>
    */
    val list_commands: List[String] = List("GET /users", "GET /users/42", "GET /users?name=Jon", "POST /users/23", "PUT /users/23", "DELETE /users/23")
    val random = new Random
    val result: String = list_commands(random.nextInt(list_commands.length)) + " HTTP/1.1\r\nHost: localhost\r\n\r\n"
    result
  }
}