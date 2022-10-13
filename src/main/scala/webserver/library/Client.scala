package webserver.library

import java.net.{Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}

case class Client(clientSocket: Socket,
                  out: PrintWriter,
                  in: BufferedReader) {

  def sendMessage(message: String): String = {
    out.println(message)
    val resp: String = in.readLine()
    resp
  }

  def stopConnetion = {
    in.close()
    out.close()
    clientSocket.close()
  }
}

object Client {

  def apply(ip: String, port: Int): Client = {

    println("Creation of a Client :)")

    val clientSocket = Socket(ip, port)
    val out = PrintWriter(clientSocket.getOutputStream(), true)
    val in = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

    val client = Client(
      clientSocket = clientSocket,
      out = out,
      in = in
    )

    println("Client is created - Hello world :)")
    client
  }
}
