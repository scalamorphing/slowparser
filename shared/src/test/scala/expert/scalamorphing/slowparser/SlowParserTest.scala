package expert.scalamorphing.slowparser

import org.scalatest.{FlatSpec, Matchers}
import scala.collection.mutable.ArrayBuffer

class SlowParserTest extends FlatSpec with Matchers {
  implicit val delimiters = Set[Char](" \n\r\t" :_*)
  import expert.scalamorphing.slowparser.api._

  "plurals" should "be parsed" in {
    val pluralQueen: P[String] = P("Queen".!.plural("s".?))
    val queensParse = pluralQueen.parse("Queens")

    queensParse should be (Parsed.Success("Queen", "Queens".length))
  }

  "whitespace" should "be skipped with ~ parser combinator" in {
    val parser: P[String] = P("Queen".! ~ "is" ~ "a" ~ "great" ~ "person")
    val input = "Queen is a great                            person"
    val parsed = parser.parse(input)
    parsed should be (Parsed.Success("Queen", input.length))
  }

  "whitespace" should "be skipped with | parser combinator" in {
    val parser: P[String] = P(("Queen" | "King" | "Knight").!)
    val input = "Queen                       "
    val parsed = parser.parse(input)
    parsed should be (Parsed.Success("Queen", "Queen".length))
  }

  "whitespace" should "be skipped with rep parser combinator" in {
    val parser: P[Seq[String]] = P("Queen".!.rep(sep = ","))
    val input = 1 to 10 map (_ => "Queen            ,       ")
    val parsed = parser.parse(input.mkString + "Queen")
    parsed should be (
      Parsed.Success(
        ArrayBuffer((input ++ Seq("Queen")).map(_ => "Queen") :_*),
        (input.mkString + "Queen").length
      )
    )
  }
}

