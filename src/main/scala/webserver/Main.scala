package webserver

import webserver.library.{Client, Server, WebResponse, WebRequest, Test}

@main def serverSide(): Unit =
  //println("Hello world!")
  //println(msg)

  //val myTest = Test
  val myWebServer = Server(5000)
  myWebServer.start
  //println(myTest.hello)
  //println(myWebServer.get(WebRequest("test1")))
  //println(myWebServer.post(WebRequest("oui2")))
  //println(myWebServer.put(WebRequest("non3")))
  //println(myWebServer.delete(WebRequest("bon4")))
  //myWebServer.stop()
//def msg = "I was compiled by Scala 3. :)"


@main def clientSide(): Unit =

  val firstClient = Client("127.0.0.1", 5000)
  val response: String = firstClient.sendMessage("hello server")
  assert(response == "hello client")

@main def clientSideStranger(): Unit =

  val secondClient = Client("127.0.0.2", 5000)
  val response: String = secondClient.sendMessage("First message from client")
  assert(response == "hello stranger")




