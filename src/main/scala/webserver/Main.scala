package webserver

import webserver.library.{Client, Server, WebResponse, WebRequest, EchoServer, Test}

@main def serverSide(): Unit =
  //println("Hello world!")
  //println(msg)

  //val myTest = Test
//  val myWebServer = Server(5000)
//  myWebServer.start
  val myEchoServer = EchoServer(6000)
  myEchoServer.start
  //println(myTest.hello)
  //println(myWebServer.get(WebRequest("test1")))
  //println(myWebServer.post(WebRequest("oui2")))
  //println(myWebServer.put(WebRequest("non3")))
  //println(myWebServer.delete(WebRequest("bon4")))
  //myWebServer.stop()
//def msg = "I was compiled by Scala 3. :)"


@main def clientSide(): Unit =
  val firstClient = Client("127.0.0.1", 5000, "hello server")
  firstClient.start

@main def clientSideStranger(): Unit =
  val secondClient = Client("127.0.0.1", 5000, "I send a wrong message")
  secondClient.start





