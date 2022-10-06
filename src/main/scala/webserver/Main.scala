package webserver

import webserver.library.{SimpleWebService, WebResponse, WebRequest, Test}

@main def hello(): Unit =
  println("Hello world!")
  println(msg)
  
  val my_test = Test
  val my_web_server = SimpleWebService

  println(my_test.hello)
  println(my_web_server.get(WebRequest("0001")))
  
def msg = "I was compiled by Scala 3. :)"

