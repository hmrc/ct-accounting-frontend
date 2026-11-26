package pages.manageClient

import play.api.libs.json.JsPath
import queries.{Gettable, Settable}

case object UtrQuery extends Gettable[String] with Settable[String] {

  override def path: JsPath = JsPath \"utr"
}
