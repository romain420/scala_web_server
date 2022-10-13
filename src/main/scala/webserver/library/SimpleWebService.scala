package webserver.library

import webserver.library.{WebRequest, WebResponse}
import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, PrintWriter, InputStreamReader}

case class SimpleWebService(serverSocket: ServerSocket,
                            clientSocket: Socket,
                            out: PrintWriter,
                            in: BufferedReader) {

  def stop() = {
    in.close()
    out.close()
    clientSocket.close()
    serverSocket.close()
    println("Server is closed - Goodbye")
  }

  def get(request: WebRequest): WebResponse = request.toWebResponse

  def post(request: WebRequest): WebResponse = WebResponse("0001")

  def put(request: WebRequest): WebResponse = WebResponse("0002")

  def delete(request: WebRequest): WebResponse = WebResponse("0003")
}

object SimpleWebService {
  def apply(port:Int): SimpleWebService = {

    println("Starting the service :)")

    val serverSocket = ServerSocket(port)
    val clientSocket = serverSocket.accept()
    val out = new PrintWriter(clientSocket.getOutputStream(), true)
    val in = new BufferedReader(InputStreamReader(clientSocket.getInputStream()))

    val simpleWebService = SimpleWebService(
      serverSocket = serverSocket,
      clientSocket = clientSocket,
      out = out,
      in = in
    )

    println("Startup done - Hello world!")
    simpleWebService
  }
}