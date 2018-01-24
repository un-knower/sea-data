package seadata.data.boot

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object DataBoot extends App {
  println(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
}
