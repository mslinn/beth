package com.micronautics.cli

import java.nio.file.{Path, Paths}
import com.typesafe.config.ConfigFactory
import io.circe.config.syntax._
import io.circe.generic.auto._
import org.jline.utils.AttributedStyle

object GlobalConfig {
  import io.circe.{ Decoder, Encoder, HCursor, Json }

  implicit val encodeFoo: Encoder[Path] = (path: Path) => Json.fromString(path.toFile.getCanonicalPath)

  implicit val decodeFoo: Decoder[Path] = (hCursor: HCursor) =>
    for {
      foo <- hCursor.value.as[String]
    } yield Paths.get(foo)

  lazy val instance: GlobalConfig = ConfigFactory.load.resolve().as[GlobalConfig]("cliLoop") match {
    case Left(error) =>
      System.err.println("Configuration error: " + error.getMessage)
      sys.exit(0)

    case Right(config) => config
  }
}

object Style {
  val defaultStyle: AttributedStyle = AttributedStyle.DEFAULT
}

case class Style(foregroundColor: String) {
  import Style._

  val style: AttributedStyle = colorToStyle(foregroundColor)

  private def colorToStyle(foregroundColor: String): AttributedStyle = {
    foregroundColor.toLowerCase match {
      case "black"   => defaultStyle.foreground(AttributedStyle.BLACK)
      case "red"     => defaultStyle.foreground(AttributedStyle.RED)
      case "green"   => defaultStyle.foreground(AttributedStyle.GREEN)
      case "yellow"  => defaultStyle.foreground(AttributedStyle.YELLOW)
      case "blue"    => defaultStyle.foreground(AttributedStyle.BLUE)
      case "magenta" => defaultStyle.foreground(AttributedStyle.MAGENTA)
      case "cyan"    => defaultStyle.foreground(AttributedStyle.CYAN)
      case "white"   => defaultStyle.foreground(AttributedStyle.WHITE)
      case _         => defaultStyle
    }
  }
}

case class Styles(
  default: Style,
  debug: Style,
  error: Style,
  help: Style,
  info: Style,
  javaScript: Style
)

case class GlobalConfig(
  productName: String,
  version: String,
  cliHome: Path,
  styles: Styles
)
