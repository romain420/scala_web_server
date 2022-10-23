package webserver

import webserver.library.{Client, Server, WebResponse, WebRequest, EchoServer, Test}


val server_port: Int = 8000

@main def recServer(): Unit =
  val myRecServer = EchoServer(server_port)
  myRecServer.start_rec

/* DEPRECATED
@main def serverSide(): Unit =          // same as server but infinite loop for service
  val myEchoServer = EchoServer(server_port)
  myEchoServer.start
*/

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

@main def requestClient(): Unit =
  val fourthClient = Client("localhost", server_port, "useless message")
  fourthClient.createSendRequest()







