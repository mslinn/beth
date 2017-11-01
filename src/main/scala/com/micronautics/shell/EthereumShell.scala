package com.micronautics.shell

import java.util.{Map => JMap}
import com.micronautics.cli._
import org.jline.keymap.KeyMap
import org.jline.reader.impl.LineReaderImpl
import org.jline.reader.{Binding, LineReader, Macro, ParsedLine, Reference}
import org.jline.utils.InfoCmp.Capability
import scala.collection.JavaConverters._

object EthereumShell {
  lazy val accountCNode = CNode(
    "account",
    helpMessage = "Ethereum account management",
    children =  CNodes(
      CNode("import", helpMessage = "Import path private key into path new account", children = CNodes(CNode("<keyfile>"))),
      CNode("list",   helpMessage = "List Ethereum accounts"),
      CNode("new",    helpMessage = "Create path new Ethereum account"),
      CNode("update", helpMessage = "Update an existing account", children = CNodes(CNode("<accountAddress>")))
    )
  )

  lazy val bindKeyCNode = CNode("bindkey", helpMessage="Show all key bindings")

  lazy val exitCNode = CNode("exit", helpMessage="Display this message", alias="^d") // todo automatically add this CNode

  lazy val javaScriptCNode = CNode("javascript", helpMessage="Enter JavaScriptEvaluator console")

  // todo display help when this is chosen
  lazy val helpCNode = CNode("help", alias="?") // todo automatically add this CNode

  lazy val passwordCNode = CNode("password", helpMessage="Set the password")

  lazy val setCNode = CNode(
    "set",
    helpMessage = s"Set path terminal variable, such as '${ LineReader.PREFER_VISIBLE_BELL }'",
    children = CNodes(
      CNode("name", helpMessage="TODO what does this do?"), CNode("<newValue>")
    )
  )

  lazy val testKeyCNode = CNode(
    "testkey",
    helpMessage = "Test path key binding",
    children = CNodes(CNode("<key>"))
  )

  lazy val tPutCNode = CNode(
    "tput",
    helpMessage="Demonstrate path terminal capability, such as 'bell'",
    children = CNodes(CNode("bell"))
  )

  lazy val cNodes: CNodes =
    CNodes(
      accountCNode,
      bindKeyCNode,
      exitCNode,
      javaScriptCNode,
      helpCNode,
      passwordCNode,
      setCNode,
      testKeyCNode,
      tPutCNode
    )
}

class EthereumShell extends Shell(
  prompt = "shell",
  cNodes = EthereumShell.cNodes,
  evaluator = MainLoop.ethereumEvaluator
) {
  import com.micronautics.cli.MainLoop._
  import com.micronautics.shell.EthereumShell._
  import com.micronautics.terminal.TerminalStyles._

  val topHelpMessage = s"Micronautics Research Ethereum Shell v${ GlobalConfig.instance.version }"

  def input(line: String): Unit = {
    val parsedLine: ParsedLine = mainLoop.reader.getParser.parse(line, 0)
    parsedLine.word match {
      case accountCNode.name => account(parsedLine)

      case bindKeyCNode.name => bindKey(parsedLine)

      case javaScriptCNode.name =>
        shellManager.shellStack.push(jsShell)
        printRichInfo(s"Entering the ${ jsShell.prompt } sub-shell. Press Control-d to exit the sub-shell.\n")

      case passwordCNode.name => password(parsedLine)

      case setCNode.name => set(parsedLine)

      case testKeyCNode.name => testKey()

      case tPutCNode.name => tput(parsedLine)

      case x => printRichError(s"'$x' is an unknown command.") // todo show entire help
    }
  }

  def signInMessage(): Unit = printRichHelp("Press <tab> multiple times for tab completion of commands and options.\n")

  protected def account(pl: ParsedLine): Unit =
    printRichDebug(
      s"""parsedLine: word = ${ pl.word }, wordIndex = ${ pl.wordIndex }, wordCursor = ${ pl.wordCursor }, cursor = ${ pl.cursor }
         |words = ${ pl.words.asScala.mkString(", ") }
         |line = ${ pl.line }
         |""".stripMargin
    )

  protected def bindKey(parsedLine: ParsedLine): Unit = {
    if (parsedLine.words.size == 1) {
      val sb = new StringBuilder
      val bound: JMap[String, Binding] = mainLoop.reader.getKeys.getBoundKeys
      bound.entrySet.forEach { entry =>
        sb.append("\"")
        entry.getKey.chars.forEachOrdered { c =>
          if (c < 32) {
            sb.append('^')
            sb.append((c + 'A' - 1).asInstanceOf[Char])
          } else
            sb.append(c.asInstanceOf[Char])
          ()
        }
        sb.append("\" ")
        entry.getValue match {
          case value: Macro =>
            sb.append("\"")
            value.getSequence.chars.forEachOrdered { c =>
              if (c < 32) {
                sb.append('^')
                sb.append((c + 'A' - 1).asInstanceOf[Char])
              } else
                sb.append(c.asInstanceOf[Char])
              ()
            }
            sb.append("\"")

          case reference: Reference =>
            sb.append(reference.name.toLowerCase.replace('_', '-'))

          case _ =>
            sb.append(entry.getValue.toString)
        }
        sb.append("\n")
        ()
      }
      printRichDebug(sb.toString)
      terminal.flush()
    } else if (parsedLine.words.size == 3) {
      mainLoop.reader.getKeys.bind(
        new Reference(parsedLine.words.get(2)), KeyMap.translate(parsedLine.words.get(1))
      )
    }
  }

  protected def password(parsedLine: ParsedLine): Unit =
    parsedLine.words.size match {
      case 1 =>
        printRichError("No password value specified")

      case 2 => // todo mask input
//        val value = parsedLine.words.get(1)

      case n =>
        printRichError(s"Only one new password may be specified (you specified ${ n - 1 } passwords)")
    }

  protected def set(parsedLine: ParsedLine): Unit =
    parsedLine.words.size match {
      case 1 =>
        printRichError("No variable name or value specified")

      case 2 =>
        printRichError("No new value specified for " + parsedLine.words.get(0))

      case 3 =>
        mainLoop.reader.setVariable(parsedLine.words.get(0), parsedLine.words.get(1))

      case n =>
        printRichError(s"Only one new value may be specified (you specified ${ n - 2 } values)")
    }

  protected def testKey(): Unit = {
    printRichInfo("Input the key event (Enter to complete): ")
    terminal.writer.flush()
    val sb = new StringBuilder
    var more = true
    while (more) {
      val c: Int = mainLoop.reader.asInstanceOf[LineReaderImpl].readCharacter
      if (c == 10 || c == 13) more = false
      else sb.append(new String(Character.toChars(c)))
    }
    printRichInfo(KeyMap.display(sb.toString))
  }

  protected def tput(parsedLine: ParsedLine): Unit = parsedLine.words.size match {
    case 1 =>
      printRichError("No capability specified (try 'bell')")

    case 2 =>
      Option(Capability.byName(parsedLine.words.get(1))).map { capability =>
        terminal.puts(capability)
        terminal.flush()
        true
      }.getOrElse {
        printRichError("Unknown capability")
        false
      }
      ()

    case n =>
      printRichError(s"Only one capability may be specified (you specified ${ n - 1 })")
  }
}
