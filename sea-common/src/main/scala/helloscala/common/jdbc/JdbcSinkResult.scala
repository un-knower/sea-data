package helloscala.common.jdbc

import scala.collection.immutable

case class JdbcSinkResult(count: Long, results: immutable.Seq[immutable.Seq[Int]])
