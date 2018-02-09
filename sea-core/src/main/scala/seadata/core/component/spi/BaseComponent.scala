/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.core.component.spi

trait BaseComponent {
  def preStart(): Unit

  def postStop(): Unit
}
