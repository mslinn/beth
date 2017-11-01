package com.micronautics.terminal

import com.micronautics.cli.{GlobalConfig, Styles}
import org.jline.terminal.Terminal
import org.jline.utils.{AttributedStringBuilder, AttributedStyle}

object TerminalStyles {
  val styles: Styles = GlobalConfig.instance.styles
  val defaultStyle: AttributedStyle   = styles.default.style
  val debugStyle: AttributedStyle     = styles.debug.style
  val errorStyle: AttributedStyle     = styles.error.style
  val helpStyle: AttributedStyle      = styles.help.style
  val helpCNameStyle: AttributedStyle = helpStyle.bold
  val infoStyle: AttributedStyle      = styles.info.style
  val jsStyle: AttributedStyle        = styles.javaScript.style

  @inline def printRichDebug(message: String)
                              (implicit terminal: Terminal): Unit = {
      terminal.writer.println(richDebug(message))
      terminal.writer.flush()
    }

  @inline def printRichError(message: String)
                            (implicit terminal: Terminal): Unit = {
    terminal.writer.println(richError(message))
    terminal.writer.flush()
  }

  @inline def printRichInfo(message: String)
                           (implicit terminal: Terminal): Unit = {
    terminal.writer.println(info(message))
    terminal.writer.flush()
  }

  @inline def printRichHelp(message: String)
                           (implicit terminal: Terminal): Unit = {
    terminal.writer.println(help(message))
    terminal.writer.flush()
  }

  @inline def printJsResult(message: String)
                           (implicit terminal: Terminal): Unit = {
    terminal.writer.println(js(message))
    terminal.writer.flush()
  }

  def richDebug(message: String): String = {
    val asBuilder = new AttributedStringBuilder
    if (!TerminalCapabilities.supportsAnsi) asBuilder.append(message)
    else asBuilder
           .style(debugStyle)
           .append(message)
           .style(defaultStyle)
  }.toAnsi

  def richError(message: String): String = {
    val asBuilder = new AttributedStringBuilder
    if (!TerminalCapabilities.supportsAnsi) asBuilder.append(s"Error: $message\n")
    else asBuilder
           .style(errorStyle)
           .append(s"Error: $message\n")
           .style(defaultStyle)
  }.toAnsi

  def help(message: String): String = {
    val asBuilder = new AttributedStringBuilder
    if (!TerminalCapabilities.supportsAnsi) asBuilder.append(message)
    else asBuilder
           .style(helpStyle)
           .append(message)
           .style(defaultStyle)
  }.toAnsi

  def info(message: String): String = {
    val asBuilder = new AttributedStringBuilder
    if (!TerminalCapabilities.supportsAnsi) asBuilder.append(message)
        else asBuilder
              .style(infoStyle)
              .append(message)
              .style(defaultStyle)
  }.toAnsi

  def js(message: String): String = {
    val asBuilder = new AttributedStringBuilder
    if (!TerminalCapabilities.supportsAnsi) asBuilder.append(message)
        else asBuilder
              .style(jsStyle)
              .append(message)
              .style(defaultStyle)
  }.toAnsi
}
