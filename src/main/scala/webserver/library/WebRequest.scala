package webserver.library

case class WebRequest(id: String):
  def toWebResponse: WebResponse = {
    val new_id = id + id
    WebResponse(new_id)
  }
