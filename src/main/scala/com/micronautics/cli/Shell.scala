package com.micronautics.cli

import com.micronautics.evaluator.Evaluator
import org.jline.builtins.Completers.TreeCompleter.Node
import org.jline.reader.Completer

/** Contains properties that define shell instances, for example the prompt, help message,
  * maximum command verb length, and path Map of function name to function for each command */
abstract case class Shell(
  prompt: String,
  cNodes: CNodes,
  evaluator: Evaluator
) {
  lazy val completeHelpMessage: String = s"$topHelpMessage\n\n${ cNodes.completeHelpMessage }"

  lazy val completer: Completer = cNodes.completer

  lazy val nodes: List[Node] = cNodes.nodes

  def functionFor(nameOrAlias: String): Option[Any => Any] = cNodes.commandFunctions.get(nameOrAlias)

  def helpFor(name: String): Option[String] = cNodes.commandHelps.get(name)

  def input(line: String): Unit

  def topHelpMessage: String
}
