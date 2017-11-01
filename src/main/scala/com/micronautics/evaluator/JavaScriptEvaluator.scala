package com.micronautics.evaluator

import javax.script.{ScriptEngineFactory, ScriptException}
import com.micronautics.cli.MainLoop

/** @param useClassloader set false for unit tests; see [[https://github.com/sbt/sbt/issues/1214]] */
class JavaScriptEvaluator(useClassloader: Boolean = true) extends Evaluator {
  import javax.script.{Bindings, ScriptContext, ScriptEngine, ScriptEngineManager}
  import com.micronautics.terminal.TerminalStyles._
  import scala.collection.JavaConverters._
  import MainLoop._

  protected lazy val scriptEngineManager: ScriptEngineManager =
    if (useClassloader) new ScriptEngineManager(getClass.getClassLoader)
    else new ScriptEngineManager()

  lazy val scriptEngine: ScriptEngine = getScriptEngine("JavaScript")
  lazy val engineContext: ScriptContext = scriptEngine.getContext
  lazy val bindings: javax.script.Bindings = engineContext.getBindings(ScriptContext.ENGINE_SCOPE)

  lazy val factory: ScriptEngineFactory = scriptEngine.getFactory


  def init(): EvaluatorInfo = {
    _linesInput = 0
    _lastErrorInputLine = None
    _lastErrorMessage = None

    // todo reload previous session context somehow

    info
  }

  /** User input is passed to the `JavaScriptEvaluator` `Evaluator` subclass */
  def eval(string: String): Option[AnyRef] =
    try {
      _linesInput = _linesInput + 1
      if (null==scriptEngine) {
        scriptEngineOk
        None
      } else {
        val value = scriptEngine.eval(string, bindingsEngine)
        val result: Any = value match {
          case x: java.lang.Boolean => Some(Boolean.unbox(x))
          case x: java.lang.Double  => Some(Double.unbox(x))
          case x: java.lang.Float   => Some(Float.unbox(x))
          case x: java.lang.Integer => Some(Int.unbox(x))
          case x: AnyRef => Some(x)
          case x if x == null => None
          case x => Some(x.toString) // this is the only way to return something like this
        }
        result.asInstanceOf[Option[AnyRef]]
      }
    } catch {
      case e: NullPointerException =>
        printRichError(s"Error: ${ e.getMessage }")
        None

      case e: ScriptException =>
        printRichError(s"Error on line ${ e.getLineNumber }, column ${ e.getColumnNumber}: ${ e.getMessage }")
        None

      case e: Exception =>
        val cause: String = {
          val x = e.getCause.getMessage
          if (x.nonEmpty) s"cause: $x, " else ""
        }
        val message: String = {
          val x = e.getMessage
          if (x.nonEmpty) s"$x, " else ""
        }
        val stackTrace = e.getStackTrace.mkString("\n")
        printRichError(s"JavaScript evaluation error: $cause $message $stackTrace")
        None
    }

  def get(name: String): AnyRef = bindingsEngine.get(name)

  def info = EvaluatorInfo(
    engineName       = factory.getEngineName,
    engineVersion    = factory.getEngineVersion,
    evaluatorName    = factory.getLanguageName,
    evaluatorVersion = s"${ factory.getLanguageVersion }",
    names            = factory.getNames.asScala.toList
  )

  def isDefined(name: String): Boolean = bindingsEngine.containsKey(name)

  /** All numbers in JavaScriptEvaluator are doubles: that is, they are stored as 64-bit IEEE-754 doubles.
    * JavaScriptEvaluator does not have integers, so before an `Int` can be provided to the `value` parameter
    * it is first implicitly converted to `Double`. */
  def put(name: String, value: AnyVal): AnyRef = {
    bindingsEngine.put(name, value)
    val retrieved: AnyRef = bindingsEngine.get(name)
    retrieved
  }

  /** This JavaScriptEvaluator interpreter maintains state throughout the life of the program.
    * Multiple `eval()` invocations accumulate state. */
  def getScriptEngine(engineName: String): ScriptEngine =
    Option(scriptEngineManager.getEngineByName(engineName)).getOrElse {
      throw new Exception(s"Error: $engineName engine not available.")
    }

  def scriptEngineFactories: List[ScriptEngineFactory] = {
    scriptEngineOk
    val factories = scriptEngineManager.getEngineFactories
    println(s"${ factories.size } scripting engines are available.")
    factories.asScala.toList
  }

  def scriptEngineOk: Boolean = {
    if (scriptEngineManager==null) {
      Console.err.println("\nError: scriptEngineManager is null!")
      System.exit(0)
    }
    scriptEngineManager.getEngineFactories.asScala.exists(_.getNames.contains("JavaScript"))
  }

  def scopeKeysEngine: Set[String] = bindingsEngine.keySet.asScala.toSet

  /** Initialize JavaScriptEvaluator instance */
  def setup(): Evaluator = {
    try {
      // todo reload context from path previous session
    } catch {
      case e: Exception =>
        richError(e.getMessage)
    }
    this
  }

  /* Sample output for Windows (Linux does not show the Scala REPL):
      2 scripting engines are available.
      Engine Name      = Scala REPL
      Engine Version   = 2.0
      Language Name    = Scala
      Language Version = version 2.12.4
      Names = scala

      Engine Name      = Oracle Nashorn
      Engine Version   = 1.8.0_151
      Language Name    = ECMAScript
      Language Version = ECMA - 262 Edition 5.1
      Names = nashorn, Nashorn, js, JS, JavaScriptEvaluator, javascript, ECMAScript, ecmascript */
  def showEngineFactories(engineFactories: List[ScriptEngineFactory]): Unit =
    engineFactories.foreach { engine =>
      println(
        s"""Engine name      = ${ engine.getEngineName }
           |Engine version   = ${ engine.getEngineVersion }
           |Language name    = ${ engine.getLanguageName }
           |Language version = ${ engine.getLanguageVersion }
           |Names that can be used to retrieve this engine = ${ engine.getNames.asScala.mkString(", ") }
           |""".stripMargin)
    }

  override def shutdown(): EvaluatorStatus = {
    // todo save session context somehow

    super.shutdown()
  }

  protected[evaluator] def bindingsEngine: Bindings = engineContext.getBindings(ScriptContext.ENGINE_SCOPE)
}
