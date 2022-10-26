package webserver

import webserver.library.{Client, EchoServer, FinalServer, Server, SimpleWebService, Test, WebRequest, WebResponse}

val server_port: Int = 8000



@main def finalServer(): Unit =
  val myWebService: SimpleWebService = new SimpleWebService {   // very basic implementation of a service, that just gives back a WebResponse
    def get(request: WebRequest): WebResponse = request.toWebResponse("200", "OK", request.message)
    def post(request: WebRequest): WebResponse = request.toWebResponse("200", "OK", request.message)
    def put(request: WebRequest): WebResponse = request.toWebResponse("200", "OK", request.message)
    def delete(request: WebRequest): WebResponse = request.toWebResponse("200", "OK", request.message)
  }
  val myFinalServer = FinalServer(server_port, myWebService)
  myFinalServer.startRec()

@main def recServer(): Unit =
  val myRecServer = EchoServer(server_port)
  myRecServer.startRec()

@main def requestClient(): Unit =
  val fourthClient = Client("localhost", server_port, "useless message")
  fourthClient.createSendRequest()




// TESTS
@main def serverTestSide(): Unit =      // one time use
  val myServer = Server(server_port)
  myServer.start

@main def helloClient(): Unit =          // a client that will be recognized by the server
  val firstClient = Client("localhost", server_port, "hello server")
  firstClient.start()

@main def stopClient(): Unit =          // a client that will not be recognized by the server
  val secondClient = Client("localhost", server_port, "stop server")
  secondClient.start()

@main def randomClient(): Unit =        // a client that will not be recognized by the server
  val thirdClient = Client("localhost", server_port, "I send a random message")
  thirdClient.start()

@main def multipleClient(): Unit =      // multiple clients at the same time
  val client1 =  Client("localhost", server_port, "client1")
  val client2 =  Client("localhost", server_port, "client2")
  client1.start()
  client2.start()

@main def nameRequestClient(): Unit =
  val fifthClient = Client("localhost", server_port, "Bienvenue sur le serv")
  fifthClient.createSendNameRequest("JM-Tech")






