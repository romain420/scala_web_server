package webserver

import webserver.library.{Client, SimpleWebService, WebResponse, WebRequest, Test}

@main def hello(): Unit =
  println("Hello world!")
  println(msg)
  
  val myTest = Test
  val myWebServer = SimpleWebService(8080)
  val firstClient = Client("127.0.0.1", 8080)

  println(myTest.hello)
  println(myWebServer.get(WebRequest("test1")))
  println(myWebServer.post(WebRequest("oui2")))
  println(myWebServer.put(WebRequest("non3")))
  println(myWebServer.delete(WebRequest("bon4")))

  myWebServer.stop()

def msg = "I was compiled by Scala 3. :)"

