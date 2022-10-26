package webserver.library

import webserver.library.{WebRequest, WebResponse}

import java.net.{InetAddress, ServerSocket, Socket}
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}
import scala.util.control.Breaks.*
import java.time.Instant

case class EchoServer(server: ServerSocket) {

  def startRec(): Unit = {
    val localhost: InetAddress = InetAddress.getLocalHost
    val localIpAddress: String = localhost.getHostAddress
    val servPort: Int = server.getLocalPort

    println(s"\nWaiting for a new client to connect...")
    println(s"\r\nServer address:\n\t$localIpAddress:$servPort")
    println(s"\tLocalhost:$servPort\n")

    val connexion = Using(server.accept()) { client =>
      processMessage(client) // returns a boolean: true to stop the loop, else false
    }
    connexion match {
      case Success(res) =>
        if (res) ()
        else startRec()
      case Failure(res) =>
        println(">>> 500: Internal Server Error ")
        println(s">>> client connection failure: ${res.getMessage}")
    }
    ()
  }

  def processMessage(client: Socket): Boolean = {
    Using(new BufferedReader(new InputStreamReader(client.getInputStream))) { in =>
      val message = readAllBuffer("", in)
      val isRequest = messageIsRequest(message)
      val isNameRequest = messageIsNameRequest(message)
      println(s">>> I received the following message:\n\"$message\"")
      println(s">>> is this detected as a request? $isRequest")
      println(s">>> is this detected as a nameRequest? $isNameRequest")
      if(isNameRequest) {
        handleNameRequest(client, message)
      } else if(isRequest) {
        handleRequest(client, message)
      } else {
        handleMessage(client, message)
      }     // and if it is not a request, so a lambda message...
    }.fold(
      error => {
        println(">>> 500: Internal Server Error ")
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
        println(">>> 500: Internal Server Error ")
        println(s">>> we will try to send the message back")
        sendMessage(client, s"I am going to send your message back:\t\"$message\"")
        false     // no reason to stop the server
    }
  }

  def handleNameRequest(client: Socket, request: String): Boolean = {
    val split = request.split(" ")
    val path = split.apply(1)
    val method = split.apply(0)
    val name = split.last
    method match {
      case "GET" =>
        println(">>> GET name")
        val response = createRequestResponse(method, path, description = s"{'reponse':'bonjour à toi $name'}")
        sendMessage(client, response)
        false
      case "PUT" =>
        println(s">>> PUT")
        val response = createRequestResponse(method, path = "", description = "{'reponse':'bonjour du serveur'}")
        sendMessage(client, response)
        false
      case "DELETE" =>
        println(s">>> DELETE")
        val response = createRequestResponse(method, path = "", description = "{'reponse':'bonjour du serveur'}")
        sendMessage(client, response)
        false
      case "POST" =>
        println(s">>> POST")
        val response = createRequestResponse(method, path = "", description = "{'reponse':'bonjour du serveur'}")
        sendMessage(client, response)
        false
    }
  }

  def handleRequest(client: Socket, request: String): Boolean = {
    val split = request.split(" ")
    val path = split.apply(1)
    val method = split.apply(0)
    method match {
      case "GET" =>
        println(">>> GET")
        val response = createRequestResponse(method, path, description = "OK")
        sendMessage(client, response)
        false
      case "PUT" =>
        println(">>> PUT")
        val response = createRequestResponse(method, path, description = "OK")
        sendMessage(client, response)
        false
      case "DELETE" =>
        println(">>> DELETE")
        val response = createRequestResponse(method, path, description = "OK")
        sendMessage(client, response)
        false
      case "POST" =>
        println(">>> POST")
        val response = createRequestResponse(method, path, description = "OK")
        sendMessage(client, response)
        false
    }
  }

  def sendMessage(client: Socket, message: String): Unit = {
    Using(new PrintWriter(client.getOutputStream, true)) { out =>
      println(s">>> we are about to send the following message back:\n\t\"$message\"")
      out.println(message)
    }.fold(
      error => {
        println(">>> 500: Internal Server Error ")
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

  def messageIsRequest(message: String): Boolean = { // very basic test of the request, can be easily miss-route
    val list_method: List[String] = List("GET", "PUT", "DELETE", "POST")
    val split = message.split(" ")
    if(split.length == 1) return false
    val method = split.apply(0)
    if(list_method.contains(method)) true
    else false
  }

  def messageIsNameRequest(message: String): Boolean = { // very basic test of the request, can be easily miss-routed
    val split = message.split(" ")
    if (split.length == 1) return false
    val method = split.apply(0)
    val name = split.apply(1)
    if (method == "GET" && name == "name") true
    else false
  }

  def createRequestResponse(code: String, path: String, description: String): String = {
    val timeInMillis = System.currentTimeMillis()
    val currentDate = Instant.ofEpochMilli(timeInMillis)
    val requestResponse: String = s"HTTP/1.1 \"$code\" \"$path\" Date: \"$currentDate\" Content-Type: plain/text Content: \"$description\""
    requestResponse
  }

  def readAllBuffer(response: String, in: BufferedReader): String = {
    val to_add = in.readLine()
    println(s"in reader, to_add=$to_add")
    if(in.ready() && to_add != null) {
      if(response == "") {
        val resp = to_add
        readAllBuffer(resp, in)
      }
      else {
        val resp = response + " " + to_add
        readAllBuffer(resp, in)
      }
    } else if(to_add != null) {
      if (response == "") {
        to_add
      }
      else {
        response + " " + to_add
      }
    }
    else response
  }

  /* commented for now
  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")*/
}

object EchoServer {
  def apply(port:Int): EchoServer = EchoServer(new ServerSocket(port))
}
