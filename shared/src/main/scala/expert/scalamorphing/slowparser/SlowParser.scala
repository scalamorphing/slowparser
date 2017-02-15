package expert.scalamorphing.slowparser

import fastparse.core.Implicits._
import fastparse.core._
import fastparse.parsers.Combinators._

class SlowParserImpl[+T, Elem, Repr](self: Parser[T, Elem, Repr])
                                   (implicit repr: fastparse.utils.ReprOps[Elem, Repr], delimiters: Parser[Unit, Elem, Repr])
  extends ParserApiImpl[T, Elem, Repr](self) with SlowParserApi[T, Elem, Repr] {

  override def rep[R](implicit ev: Repeater[T, R]): Parser[R, Elem, Repr] =
    Repeat(self, 0, Int.MaxValue, delimiters)

  override def rep[R](min: Int = 0, sep: Parser[_, Elem, Repr],
             max: Int = Int.MaxValue, exactly: Int = -1)
            (implicit ev: Repeater[T, R]): Parser[R, Elem, Repr] = {
    if (exactly < 0)
      Repeat(Sequence(self, Optional(delimiters), cut = false), min, max, Sequence(sep, Optional(delimiters), cut=false))
    else
      Repeat(Sequence(self, Optional(delimiters), cut = false), exactly, exactly, Sequence(sep, Optional(delimiters), cut=false))
  }

  override def |[V >: T](p: Parser[V, Elem, Repr]): Parser[V, Elem, Repr] =
    Either[V, Elem, Repr](
      Either.flatten(
        Vector(
          self,
          Sequence(p, Optional(delimiters), cut=false)
        )
      ):_*
    )

  override def ~[V, R](p: Parser[V, Elem, Repr])(implicit ev: Sequencer[T, V, R]): Parser[R, Elem, Repr] =
    Sequence.flatten(
      Sequence(
        self,
        Sequence(Optional(delimiters),
          p,
          cut=false),
        cut=false
      ).asInstanceOf[Sequence[R, R, R, Elem, Repr]])

  override def ~/[V, R](p: Parser[V, Elem, Repr])(implicit ev: Sequencer[T, V, R]): Parser[R, Elem, Repr] =
    Sequence.flatten(Sequence(self, p, cut=true).asInstanceOf[Sequence[R, R, R, Elem, Repr]])

  def plural[R](pluralSuffix: Parser[Unit, Elem, Repr]): Parser[R, Elem, Repr] =
    Sequence(self, pluralSuffix, cut = false).asInstanceOf[Sequence[R, R, R, Elem, Repr]]
}

trait SlowParserApi[+T, Elem, Repr]
  extends ParserApi[T, Elem, Repr] {

  def plural[R](pluralSuffix: Parser[Unit, Elem, Repr]): Parser[R, Elem, Repr]
}

object api extends fastparse.StringApi {
  implicit def parserApi[T, V](p: T)(implicit c: T => fastparse.core.Parser[V, Char, String],
                                     reprOps: fastparse.utils.ReprOps[Char, String],
                                     delimiters: Set[Char]): SlowParserApi[V, Char, String] =
    new SlowParserImpl[V, Char, String](p)(reprOps, P(CharsWhile(delimiters.contains)))
}

