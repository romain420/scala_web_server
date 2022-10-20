package webserver.library

import webserver.library.{WebRequest, WebResponse}
import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}
import scala.io.{Source}
import scala.util.{Using}
import java.io.BufferedReader
import java.io.PrintWriter

case class Server(port: Int, server: ServerSocket) {

  def start: Unit = {
    Using(server.accept()) { client =>
      val out = new PrintWriter(client.getOutputStream, true)
      val in = new BufferedReader(new InputStreamReader(client.getInputStream))
      val greeting = in.readLine
      if ("hello server" == greeting) println("hello client")
      else println("unrecognised greeting")
    }.fold( // fold acts here like an if
      // if we have an error with the client...
      error => println(s">>> client connection failure: ${error.getMessage}"),
      // otherwise, we do nothing
      _ => ()
    )
  }

  /*
  val out = new PrintWriter(clientSocket.getOutputStream(), true)
  val in = new BufferedReader(InputStreamReader(clientSocket.getInputStream()))
  */

  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")
}


object Server {
  def apply(port:Int): Server = Server(port, new ServerSocket(port))
}
  /*
  def apply(port:Int): Server = {

    println("Starting the service :)")

    val serverSocket = ServerSocket(port)
    val clientSocket = serverSocket.accept()
    val out = new PrintWriter(clientSocket.getOutputStream(), true)
    val in = new BufferedReader(InputStreamReader(clientSocket.getInputStream()))

    val server = Server(
      serverSocket = serverSocket,
      clientSocket = clientSocket,
      out = out,
      in = in
    )

    val greeting: String = in.readLine()
    if("hello server" == greeting){
      out.println("hello client")
    }
    else {
      out.println("hello stranger")
    }
    println("Startup done - Hello world!")
    server
  }
}*/