package webserver.library

import webserver.library.{WebRequest, WebResponse}
import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}
import scala.io.Source
import scala.util.{Using, Try, Success, Failure}
import scala.util.control.Breaks._
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

case class EchoServer(server: ServerSocket) {

  def start_rec(): Unit = {
    println(s"\nWaiting for a new client to connect...")
    val connexion = Using(server.accept()) { client =>
      processMessage(client) // returns a boolean: true to stop the loop, else false
    }
    connexion match {
      case Success(res) =>
        if (res) ()
        else start_rec()
      case Failure(res) =>
        println(s">>> client connection failure: ${res.getMessage}")
    }
    ()
  }

  // DEPRECATED
  /*
  def start(): Unit = {   // Don't use that anymore, use start_rec instead
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
  }*/

  def processMessage(client: Socket): Boolean = {
    Using(new BufferedReader(new InputStreamReader(client.getInputStream))) { in =>
      val message = in.readLine
      val isRequest = messageIsRequest(message)
      println(s">>> I received the following message:\t\"$message\"")
      println(s">>> is this detected as a request? $isRequest")
      //println(s"We check of the Socket is still open, is Socket open in processMessage? ${client.isConnected()}")
      if (isRequest) {
        handleRequest(client, message)
      } else {
        handleMessage(client, message)
      }     // and if it is not a request, so a lambda message...
    }.fold(
      error => {
        println(s">>> cannot acquire the 'in', check the inputSteam of server: ${error.getMessage}")
        closeServer()
      },
      resp => {
        if(resp) true
        else false
      }
    )
  }

  def handleMessage(client: Socket, message: String): Boolean = {
    message match {
      case "hello server" =>
        println(s">>> we will try to send a hello back...")
        sendMessage(client, s"Hello client, you are on port ${client.getPort} we hear you now")
        false     // no reason to stop the server
      case "stop server" =>
        println(s">>> we will try to send a closing announce")
        sendMessage(client, s"Copy, we are closing the server")
        closeServer()  // returns true
      case _ =>
        println(s">>> we will try to send the message back")
        sendMessage(client, s"I am going to send your message back:\t\"$message\"")
        false     // no reason to stop the server
    }
  }

  def handleRequest(client: Socket, request: String): Boolean = { // TODO should send back a String that in the same format as WebResponse
    val list_method: List[String] = List("GET", "PUT", "DELETE", "POST")
    val split = request.split(" ")
    val method = split.apply(0)
    method match {
      case "GET" =>
        println(">>> GET")
        val response = createRequestResponse(method, description = "jaaj")
        sendMessage(client, response)
        false
      case "PUT" =>
        println(s">>> PUT")
        val response = createRequestResponse(method, description = "jaaj")
        sendMessage(client, response)
        false
      case "DELETE" =>
        println(s">>> DELETE")
        val response = createRequestResponse(method, description = "jaaj")
        sendMessage(client, response)
        false
      case "POST" =>
        println(s">>> POST")
        val response = createRequestResponse(method, description = "jaaj")
        sendMessage(client, response)
        false
    }
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


  def messageIsRequest(message: String): Boolean = { // very basic test of the request, can be easily miss-routed

    val list_method: List[String] = List("GET", "PUT", "DELETE", "POST")
    val split = message.split(" ")
    val method = split.apply(0)
    if(list_method.contains(method)) true
    else false
  }

  def createRequestResponse(code: String, description: String): String = {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME
    val requestResponse: String = s"HTTP/1.1 \"$code\" \"$description\" \r\n Date:  \"$dateTimeFormatter.format(ZonedDateTime.now()\" \r\n Content-Type: plain/text \r\n \r\n \"$description\""
    requestResponse
  }
  /*HTTP/1.1 200 OK <CRLF>
    Date: Tue, 6 Sep 2022 14:01:07 +0200 <CRLF>
      Content-Type: plain/text <CRLF>
        <CRLF>
          OK
  */
  /* commented for now
  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")*/
}

object EchoServer {
  def apply(port:Int): EchoServer = EchoServer(new ServerSocket(port))
}
