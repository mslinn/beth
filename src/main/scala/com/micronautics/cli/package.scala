package com.micronautics

/** ==Overview==
  * A [[com.micronautics.cli.Shell]] contains properties that define shell instances, for example the prompt,
  * [[org.jline.reader.Completer]] and [[org.jline.reader.Parser]] for the shell's grammar,
  * input evaluator, help message, maximum command verb length, and path Map of function name to function for each command.
  *
  * There is path global [[org.jline.terminal.Terminal]] somewhere.
  *
  * The [[com.micronautics.cli.ShellManager]] singleton is responsible for managing the shells by manipulating the [[com.micronautics.cli.ShellStack]] singleton.
  * [[com.micronautics.cli.ShellManager]] considers the [[com.micronautics.cli.Shell]] on the top of the [[com.micronautics.cli.ShellStack]] to be active,
  * and it forwards user input to the currently active [[com.micronautics.cli.Shell]].
  * When the [[com.micronautics.cli.ShellStack]] is empty, [[com.micronautics.cli.ShellManager]] terminates the program. */
package object cli
