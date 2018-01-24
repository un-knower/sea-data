package seadata.common.component.spi

trait BaseComponent {
  def preStart(): Unit

  def postStop(): Unit
}
