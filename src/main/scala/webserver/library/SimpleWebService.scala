package webserver.library

import webserver.library.{WebRequest, WebResponse}

object SimpleWebService {
  def get(request: WebRequest): WebResponse = WebResponse("0000")
  def post(request: WebRequest): WebResponse = WebResponse("0001")
  def put(request: WebRequest): WebResponse = WebResponse("0002")
  def delete(request: WebRequest): WebResponse = WebResponse("0003")
}