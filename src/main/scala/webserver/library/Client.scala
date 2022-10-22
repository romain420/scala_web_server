package webserver.library

import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.Using
import scala.util.Using.Releasable

case class Client(ip: String, port: Int, message: String) {

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
}