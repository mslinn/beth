package com.micronautics.cli

import scala.collection.mutable

object ShellStack {
  def empty: ShellStack = new ShellStack
}

class ShellStack {
  protected[cli] lazy val stack: mutable.Buffer[Shell] = mutable.Buffer.empty

  def isEmpty: Boolean = stack.isEmpty

  def nonEmpty: Boolean = stack.nonEmpty

  def pop(): (Shell, ShellStack) = {
    val result = stack.head
    stack.remove(0)
    (result, this)
  }

  def push(shell: Shell): ShellStack = {
    stack.insert(0, shell)
    this
  }

  def top: Shell = stack.head
}
