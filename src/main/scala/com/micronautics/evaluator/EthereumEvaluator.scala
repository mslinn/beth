package com.micronautics.evaluator

class EthereumEvaluator extends Evaluator {
  override def init(): EvaluatorInfo = {
    // todo perform session initialization

    info
  }

  def eval(text: String): Option[AnyRef] = Some("TODO implement EthereumEvaluator.eval()")

  override def setup(): Evaluator = {
    // todo perform any configuration

    this
  }

  override def shutdown(): EvaluatorStatus = {
    // todo save session context somehow
    super.shutdown()
  }


  protected def info = EvaluatorInfo(
    engineName = "Ethereum library name goes here",
    engineVersion = "Ethereum library version goes here",
    evaluatorName = "Ethereum library name goes here",
    evaluatorVersion = s"Ethereum library version goes here | Micronautics v0.1.0",
    names = List("shell", "eth")
  )
}
