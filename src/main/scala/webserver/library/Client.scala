package webserver.library

import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.Using
import scala.util.Using.Releasable

case class Client(ip: String, port: Int, message:String) {

  def start: Unit = {

    Using(new Socket(ip, port)) {
      socket =>
        Using(new PrintWriter(socket.getOutputStream(), true)) { out =>
          Using(new BufferedReader(InputStreamReader(socket.getInputStream()))) { in =>
            out.println(message)
            val answer = in.readLine()
            println(answer)
          }
        }
      //stopConnection(client: Socket, in: BufferedReader, out: PrintWriter)
    }
  }

  def sendMessage(message: String, in: BufferedReader, out: PrintWriter): String = {
    out.println(message)
    val resp: String = in.readLine()
    resp
  }

  def stopConnection(client: Socket, in: BufferedReader, out: PrintWriter):Unit = {
    in.close()
    out.close()
    client.close()
    ()
  }
}