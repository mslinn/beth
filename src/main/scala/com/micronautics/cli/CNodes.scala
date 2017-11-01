package com.micronautics.cli

import com.micronautics.terminal.TerminalCapabilities
import com.micronautics.terminal.TerminalStyles.{defaultStyle, helpCNameStyle, helpStyle}
import org.jline.builtins.Completers.TreeCompleter
import org.jline.builtins.Completers.TreeCompleter.{Node, node}
import org.jline.reader.impl.completer.{AggregateCompleter, ArgumentCompleter}
import org.jline.utils.AttributedStringBuilder
import scala.language.implicitConversions

object CNodes {
  def empty: CNodes = CNodes()

  implicit def cNodesToNode(cNodes: CNodes): Node = node(cNodes.cNodes.map { cNode =>
    node(cNode.name, cNode.children.cNodes.toList)
  }: _*)

  implicit def cNodeToNode(cNode: CNode): Node = node(cNode.name :: cNode.children.cNodes.map(cNodeToNode).toList: _*)
}

/** Wraps a collection of [[CNode]] */
case class CNodes(cNodes: CNode*) {
  import CNodes._

  lazy val aliases: List[String] = sortedNodes.map(_.alias)

  lazy val commandNames: List[String] = sortedNodes.map(_.name)

  /** Maps names and aliases to functions */
  lazy val commandFunctions: Map[String, Any => Any] = {
    val nameToFunction = cNodes.map {
      case CNode(name, function, _, _, _) => (name, function)
    }

    val aliasToFunction = cNodes.map {
      case CNode(_, function, _, _, alias) if alias.trim.nonEmpty => (alias, function)
    }

    (nameToFunction ++ aliasToFunction).toMap
  }


  lazy val completeHelpMessage: String = {
    val widest = if (commandHelps.isEmpty) 0 else commandHelps.map(_._1.length).max
    commandHelps.map { help =>
      val paddedName = help._1 + " "*math.max(1, widest - help._1.length)
      s"$paddedName - ${ help._2 }"
    }.mkString("\n")
  }

  lazy val commandHelps: Map[String, String] =
    cNodes
      .sortBy(_.name)
      .map { case CNode(name, _, helpMessage, _, alias) =>
        val key = if (alias.nonEmpty) s"$name/$alias" else name
        (key, helpMessage)
      }
      .toMap

  lazy val isEmpty: Boolean = cNodes.isEmpty

  lazy val maxWidth: Int = cNodes.map(_.width).max

  lazy val nodes: List[Node] = cNodes.toList.map(cNodeToNode)

  lazy val nonEmpty: Boolean = cNodes.nonEmpty

  lazy val sortedNodes: List[CNode] = cNodes.toList.sortBy(_.name)

  /** Useful for help messages? Delete? */
  lazy val paddedCommandNames: List[String] =
    sortedNodes.map(node => node.paddedName(maxWidth))

  lazy val helpMessages: String =
    sortedNodes.map { cNode => helpMessage(cNode.name) }.mkString("\n")

  protected lazy val treeCompleter: TreeCompleter = new TreeCompleter(nodes: _*)

  // TODO unsure how to define
  protected lazy val argumentCompleter: ArgumentCompleter = new ArgumentCompleter()

  lazy val completer: AggregateCompleter = new AggregateCompleter(treeCompleter/*, argumentCompleter*/)


  /** @return List("name1", ("name2", "alias"), "name3") */
  def commandAliasNames: List[Any] = sortedNodes.map {
    case node if node.alias.isEmpty => node.name
    case node                       => (node.name, node.alias)
  }

  def helpMessage(name: String): Option[String] =
    for {
      paddedName <- paddedName(name)
      node       <- cNodeFor(name)
    } yield {
      if (TerminalCapabilities.supportsAnsi) {
        new AttributedStringBuilder().append("\n")
          .style(helpCNameStyle)
          .append(paddedName)
          .style(defaultStyle)
          .append(" - ")
          .style(helpStyle)
          .append(node.helpMessage)
          .style(defaultStyle)
          .toAnsi
      } else s"$paddedName - ${ node.helpMessage }"
    }

  /** Useful for help messages */
  def paddedName(name: String): Option[String] =
    cNodes
      .find(_.name==name)
      .map(node => node.paddedName(maxWidth))

  protected def cNodeFor(name: String): Option[CNode] = cNodes.find(_.name==name)

  protected def convertToNodes(cNodes: List[CNode]): List[Node] =
    cNodes.map { cNode =>
      if (cNode.children.isEmpty) {
        node(cNode.name)
      } else {
        val children: List[Node] = convertToNodes(cNode.children.cNodes.toList)
        val nodes: List[Object] = cNode.name :: children
        node(nodes: _*)
      }
    }
}
