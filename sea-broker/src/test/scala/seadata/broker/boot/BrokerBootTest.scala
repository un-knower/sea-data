package seadata.broker.boot

import seadata.core.test.SeaSpec
import seadata.broker.Main

class BrokerBootTest extends SeaSpec {

  "BrokerBootTest" should {

    "main" in {
      Main.main(Array.empty)
    }

  }

}
