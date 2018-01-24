package helloscala.test

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import helloscala.common.jackson.Jackson
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{EitherValues, MustMatchers, OptionValues, Suite}

import scala.concurrent.Future

trait TSpec
  extends MustMatchers
  with OptionValues
  with EitherValues
  with ScalaFutures {
  this: Suite =>

  val defaultObjectMapper: ObjectMapper = Jackson.defaultObjectMapper

  implicit val defaultPatience: PatienceConfig = PatienceConfig(Span(90, Seconds), Span(100, Millis))

  def jsonPrettyString[T](f: Future[T]): String = {
    val results = f.futureValue
    val writer = defaultObjectMapper.writer(new DefaultPrettyPrinter())
    writer.writeValueAsString(results)
  }

  def jsonPrettyString(obj: AnyRef): String = {
    val writer = defaultObjectMapper.writer(new DefaultPrettyPrinter())
    writer.writeValueAsString(obj)
  }

  def jsonString[T](f: Future[T]): String = {
    val results = f.futureValue
    defaultObjectMapper.writeValueAsString(results)
  }

  def jsonString(obj: AnyRef): String = {
    defaultObjectMapper.writeValueAsString(obj)
  }

}
