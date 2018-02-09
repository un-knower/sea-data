package seadata.core.server

import org.scalatest.BeforeAndAfterAll
import seadata.core.test.SeaSpec

trait SeaBootSpec extends SeaSpec with BeforeAndAfterAll {

  override protected def afterAll(): Unit = {
    SeaBoot.stop()
    super.afterAll()
  }

}
