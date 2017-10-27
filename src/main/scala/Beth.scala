import picocli.CommandLine._

object Beth {
  def main(args: Array[String]): Unit = {
    picocli.CommandLine.run(new Beth, System.err, args: _*)
  }
}

@Command(name = "Beth", version = Array("Beth demo v0.1.0"), description = Array("@|bold Beth|@ demo"))
class Beth extends Runnable {
  @Option(names = Array("-c", "--count"), paramLabel = "COUNT", description = Array("the count"))
  private val count: Int = 0

  @Option(names = Array("-h", "--help"), usageHelp = true, description = Array("print this help and exit"))
  private val helpRequested: Boolean = false

  @Option(names = Array("-V", "--version"), versionHelp = true, description = Array("print version info and exit"))
  private val versionRequested: Boolean = false

  def run() : Unit =
    0 until count foreach { i =>
      println(s"Hello, world $i...")
    }
}
