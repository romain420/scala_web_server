package webserver.library

import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.Using

/*
case class EchoServer(serverSocket: ServerSocket,
                      clientSocket: Socket,
                      out: PrintWriter,
                      in: BufferedReader) {
}

object EchoServer {
  def apply(port: Int): EchoServer = {

    val serverSocket = ServerSocket(port)
    val clientSocket = serverSocket.accept()
    val out = PrintWriter(clientSocket.getOutputStream(), true)
    val in = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

    val echoServer = EchoServer(
      serverSocket = serverSocket,
      clientSocket = clientSocket,
      out = out,
      in = in
    )

    echoServer
  }

  def applyForever(port: Int) = {
    apply(port)
    applyForever(port)
  }
}*/
