package com.micronautics.cli

import com.micronautics.terminal.TerminalStyles.help

object CNode {
  protected val nullFunction: Any => Any = (_: Any) => null.asInstanceOf[Any]
}

case class CNode(
  name: String,
  function: Any => Any = CNode.nullFunction,
  helpMessage: String = "",
  children: CNodes = CNodes(),
  alias: String = ""
) {
  lazy val nameAlias: String = name + (if (alias.isEmpty) "" else s"/$alias")

  lazy val paddedChildNames: List[String] = children.paddedCommandNames // todo delete because it is not used?

  lazy val richHelp: String = help(s"\n$helpMessage")

  lazy val width: Int = math.max(name.length, alias.length)

  def paddedName(width: Int): String = this match {
    case _ if alias.isEmpty => name + " "*(width - name.length)
    case _                  => s"$name / $alias" + " "*(width - name.length - "/".length - alias.length)
  }
}
