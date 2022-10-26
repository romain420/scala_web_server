package webserver.library

import webserver.library.{SimpleWebService, WebRequest, WebResponse}

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import java.net.{InetAddress, ServerSocket, Socket}
import java.time.Instant
import scala.io.Source
import scala.util.control.Breaks.*
import scala.util.{Failure, Success, Try, Using}

case class FinalServer(server: ServerSocket, service: SimpleWebService) {

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
    val method = split.apply(0)
    val web_request: WebRequest = stringToRequest(request)
    val web_response = method match {
      case "GET" =>
        service.get(web_request)
      case "PUT" =>
        service.put(web_request)
      case "DELETE" =>
        service.delete(web_request)
      case "POST" =>
        service.post(web_request)
    }
    val response: String = web_response.toString
    sendMessage(client, response)
    false
  }

  def sendMessage(client: Socket, message: String): Unit = {
    Using(new PrintWriter(client.getOutputStream, true)) { out =>
      println(s">>> we are about to send the following message back:\n\"$message\"")
      out.println(message)
      println(">>> message sent")
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

  def stringToRequest(request: String): WebRequest = {
    val split = request.split(" ")
    val method = split.apply(0)
    val path = split.apply(1)
    val version = split.apply(2)
    val host = split.apply(4)
    val message = split.apply(6)
    val res: WebRequest = WebRequest(method, path, version, host, message)
    res
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
    if(in.ready() && to_add != null) {
      if(response == "") {
        val resp = to_add + "\r\n"
        readAllBuffer(resp, in)
      }
      else {
        val resp = response + " " + to_add + "\r\n"
        readAllBuffer(resp, in)
      }
    } else if(to_add != null) {
      if (response == "") {
        to_add + "\r\n"
      }
      else {
        response + " " + to_add + "\r\n"
      }
    }
    else response
  }
}

object FinalServer {
  def apply(port: Int, service: SimpleWebService): FinalServer = FinalServer(new ServerSocket(port), service)
}
