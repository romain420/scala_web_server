package webserver.library

import java.net.{Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}

case class Client(ip: String, port: Int) {

  println("Creation of a Client :)")
  private val clientSocket = Socket(ip, port)
  private val out = PrintWriter(clientSocket.getOutputStream(), true)
  private val in = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

  def sendMessage(message: String): String = {
    out.println(message)
    val resp: String = in.readLine()
    resp
  }

  def stopConnection = {
    in.close()
    out.close()
    clientSocket.close()
  }
}

/*
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
}*/
