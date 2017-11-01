package com.micronautics.terminal

import java.util.{HashMap => JHashMap, HashSet => JHashSet, Map => JMap, Set => JSet}
import org.jline.utils.InfoCmp
import org.jline.utils.InfoCmp.Capability

// Could call `AttributedString.stripAnsi(string)` throughout if ansi is not supported
object TerminalCapabilities {
  val infoCmp: String = InfoCmp.getLoadedInfoCmp("ansi")
  val stringCapabilities: JHashMap[Capability, String] = new JHashMap
  val booleanCapabilities: JSet[Capability] = new JHashSet
  val intCapabilities: JMap[Capability, Integer] = new JHashMap

  InfoCmp.parseInfoCmp(infoCmp, booleanCapabilities, intCapabilities, stringCapabilities)
  val supportsAnsi: Boolean = stringCapabilities.containsKey(Capability.byName("acsc"))
}
