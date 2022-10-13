package webserver

import webserver.library.{SimpleWebService, WebResponse, WebRequest, Test}

@main def hello(): Unit =
  println("Hello world!")
  println(msg)
  
  val my_test = Test
  val my_web_server = SimpleWebService(8080)

  println(my_test.hello)
  println(my_web_server.get(WebRequest("test1")))
  println(my_web_server.post(WebRequest("oui2")))
  println(my_web_server.put(WebRequest("non3")))
  println(my_web_server.delete(WebRequest("bon4")))

  
def msg = "I was compiled by Scala 3. :)"

