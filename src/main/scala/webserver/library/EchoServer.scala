package webserver.library

import webserver.library.{WebRequest, WebResponse}
import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}
import scala.io.{Source}
import scala.util.{Using}

case class EchoServer(server: ServerSocket) {

  def start: Unit = {
    println("Waiting for a client...")
    while(true) {
      Using(server.accept()) { client =>
        Using(new BufferedReader(new InputStreamReader(client.getInputStream))) { in =>
          val message = in.readLine
          processMessage(client, message)
        }.fold(
          error => {
            println(s">>> cannot acquire the 'in', check the inputSteam: ${error.getMessage}")
            closeServer()
          },
          _ => ()
        )
      }.fold( // fold acts here like an if
        // if we have an error with the client...
        error => {
          println(s">>> client connection failure: ${error.getMessage}")
          closeServer()
        },
        // otherwise, we do nothing
        _ => ()
      )
    }
  }

  def processMessage(client: Socket, message: String): Unit = {
    println(s"I received the following message:\n\t\"$message\"")
    message match {
      case "hello server" => {
        sendMessage(client, s"Hello client, you are on port ${client.getPort()}")
        sendMessage(client, s"We are now listening to you")
        }
      case "stop server"  => {
        sendMessage(client, s"Copy client, we are closing the server")
        closeServer()
      }
      case _ => {
        sendMessage(client, s"I am going to send your message back")
        sendMessage(client, message)
      }
    }
    ()
  }

  def sendMessage(client: Socket, message: String): Unit = {
    Using(new PrintWriter(client.getOutputStream, true)) { out =>
      out.println(message)
    }.fold(
      error => {
        println(s">>> could not send the message, check 'out': ${error.getMessage}")
        closeServer()
      },
      _ => ()
    )
    ()
  }

  def closeServer(): Unit = {
    ()
  }

  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")
}

object EchoServer {
  def apply(port:Int): EchoServer = EchoServer(new ServerSocket(port))
}
