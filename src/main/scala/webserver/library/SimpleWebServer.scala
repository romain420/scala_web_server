package webserver.library

// Interface to implement by the user of your lib
  trait SimpleWebService:
    def get(request: WebRequest): WebResponse
    def post(request: WebRequest): WebResponse
    def put(request: WebRequest): WebResponse
    def delete(request: WebRequest): WebResponse

  // EXAMPLE OF USE

  // This implementation should be given by the user of your lib
  val myWebservice: SimpleWebService =
    abstract class Get(request: WebRequest)
    {
      // abstract method
      def get(request: WebRequest): WebResponse
    }

    abstract class Post(request: WebRequest)
    {
      // abstract method
      def post(webRequest: WebRequest): WebResponse
    }


    abstract class Delete(request: WebRequest)
    {
      // abstract method
      def delete(webRequest: WebRequest): WebResponse
    }


    abstract class Put()
    {
      // abstract method
      def put(webRequest: WebRequest): WebResponse
    }



