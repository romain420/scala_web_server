package webserver.library

import webserver.library.{WebRequest, WebResponse}
import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}
import scala.io.Source
import scala.util.Using
import scala.util.control.Breaks._

case class EchoServer(server: ServerSocket) {

  def start: Unit = {
    while(true) {
      println(s"\nWaiting for a new client to connect...")
      Using(server.accept()) { client =>
        //println(s"We check of the Socket is still open, is Socket open in start? ${client.isConnected()}")
        processMessage(client) // returns a boolean: true to stop the loop, else false
      }.fold(
        error => {
          println(s">>> client connection failure: ${error.getMessage}")
        },
        resp => {
          if(resp) break
          else ()
        }
      )
    }
  }

  def processMessage(client: Socket): Boolean = {
    Using(new BufferedReader(new InputStreamReader(client.getInputStream))) { in =>
      val message = in.readLine
      println(s">>> I received the following message:\t\"$message\"")
      //println(s"We check of the Socket is still open, is Socket open in processMessage? ${client.isConnected()}")
      message match {
        case "hello server" => {
          println(s">>> we will try to send a hello back...")
          sendMessage(client, s"Hello client, you are on port ${client.getPort} we hear you now")
        }
        case "stop server" => {
          println(s">>> we will try to send a closing announce")
          sendMessage(client, s"Copy, we are closing the server")
          closeServer()
        }
        case _ => {
          println(s">>> we will try to send the message back")
          sendMessage(client, s"I am going to send your message back:\t\"$message\"")
        }
      }
    }.fold(
      error => {
        println(s">>> cannot acquire the 'in', check the inputSteam of server: ${error.getMessage}")
        closeServer()
      },
      resp => {
        if(resp == true) true
        else false
      }
    )
  }

  def sendMessage(client: Socket, message: String): Unit = {
    Using(new PrintWriter(client.getOutputStream, true)) { out =>
      //println(s"We check of the Socket is still open, is Socket open in sendMessage? ${client.isConnected()}")
      println(s">>> we are about to send the following message back:\n\t\"$message\"")
      out.println(message)
    }.fold(
      error => {
        println(s">>> could not send the message, check the OutputStream of server: ${error.getMessage}")
        closeServer()
      },
      _ => ()
    )
    ()
  }

  def closeServer(): Boolean = {
    println(s">>> we close the server!")
    true
  }

  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")
}

object EchoServer {
  def apply(port:Int): EchoServer = EchoServer(new ServerSocket(port))
}
