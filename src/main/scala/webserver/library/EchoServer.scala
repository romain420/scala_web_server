package webserver.library

import webserver.library.{WebRequest, WebResponse}
import java.net.{ServerSocket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}
import scala.io.{Source}
import scala.util.{Using}

case class EchoServer(server: ServerSocket) {

  def start: Unit = {
    println("Waiting for a client...")
    while(true) {
      Using(server.accept()) { client =>
        val out = new PrintWriter(client.getOutputStream, true)
        val in = new BufferedReader(new InputStreamReader(client.getInputStream))
        val greeting = in.readLine
        if ("hello server" == greeting) {
          println("Connection with a recognized client")
          out.println("hello client")
        }
        else {
          println("Connection with an unrecognized client")
          out.println("unrecognised greeting")
        }
      }.fold( // fold acts here like an if
        // if we have an error with the client...
        error => println(s">>> client connection failure: ${error.getMessage}"),
        // otherwise, we do nothing
        _ => ()
      )
    }
  }

  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")
}

object EchoServer {
  def apply(port:Int): EchoServer = EchoServer(new ServerSocket(port))
}
