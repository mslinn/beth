package com.micronautics.evaluator

import org.ethereum.facade.{Ethereum, EthereumFactory}
import org.ethereum.samples.CreateContractSample

class EthereumEvaluator extends Evaluator {
 val ethereum: Ethereum = EthereumFactory.createEthereum()

  def getBestBlock: String = ethereum.getBlockchain.getBestBlock.getNumber.toString

  def init(): EvaluatorInfo = {
    // See https://github.com/ethereum/ethereumj/blob/develop/ethereumj-core/src/main/java/org/ethereum/samples/CreateContractSample.java#L55-L59
    new CreateContractSample().onSyncDone()

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
    engineName = "Ethereum library: EthereumJ",
    engineVersion = "Ethereum library 1.6.3-RELEASE",
    evaluatorName = "EthereumEvaluator",
    evaluatorVersion = s"Ethereum v0.2.0",
    names = List("shell", "eth")
  )
}
