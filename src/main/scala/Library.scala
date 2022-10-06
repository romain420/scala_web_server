// Interface to implement by the user of your lib
trait SimpleWebService:
  def get(request: WebRequest): WebResponse
  def post(request: WebRequest): WebResponse
  def put(request: WebRequest): WebResponse
  def delete(request: WebRequest): WebResponse

// EXAMPLE OF USE

// This implementation should be given by the user of your lib
val myWebservice: SimpleWebService = ???

SimpleWebServer
  .create()
  .listenPort(8080)
  .withService(myWebService)
  .runForever()