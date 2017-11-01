package com.micronautics.cli

import javax.script.ScriptEngineFactory
import com.micronautics.evaluator.JavaScriptEvaluator
import org.junit.runner.RunWith
import org.scalatest.Matchers._
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestyMcTestFace extends WordSpec with MustMatchers {
  val js = new JavaScriptEvaluator(useClassloader = false)

  "JavaScriptEvaluator" should {
    "work" in {
      js.scriptEngineOk shouldBe true
      val engineFactories: List[ScriptEngineFactory] = js.scriptEngineFactories
      engineFactories.size should be > 0

      js.showEngineFactories(engineFactories)
      js.scriptEngine should not be null
      js.scriptEngine.getFactory.getLanguageName shouldBe "ECMAScript"

      js.isDefined("ten")   shouldBe false
      js.put("ten", 10)     shouldBe 10.0
      js.get("ten")         shouldBe 10.0
      js.isDefined("ten")   shouldBe true

      js.put("twenty", 20)  shouldBe 20.0
      js.get("twenty")      shouldBe 20.0

      js.eval("var twelve = ten + 2")
      js.get("twelve")      shouldBe 12.0

      js.eval("twelve")     shouldBe Some(12.0)
      js.eval("twelve * 2") shouldBe Some(24)
      js.get("twelve")      shouldBe 12

      js.put("y", 99)       shouldBe 99
      js.get("y")           shouldBe 99
    }
  }
}
