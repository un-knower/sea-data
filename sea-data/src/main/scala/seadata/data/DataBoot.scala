/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.data

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object DataBoot extends App {
  println(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
}
