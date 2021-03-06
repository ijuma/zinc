/*
 * Zinc - The incremental compiler for Scala.
 * Copyright Lightbend, Inc. and Mark Harrah
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package sbt.internal.inc

import java.nio.file.Path

import sbt.internal.scripted._
import sbt.internal.util.ManagedLogger

class SleepingHandler(val handler: StatementHandler, delay: Long) extends StatementHandler {
  type State = handler.State
  override def initialState: State = handler.initialState
  override def apply(command: String, arguments: List[String], state: State): State = {
    val result = handler.apply(command, arguments, state)
    Thread.sleep(delay)
    result
  }
  override def finish(state: State) = handler.finish(state)
}

class IncScriptedHandlers(globalCacheDir: Path, compileToJar: Boolean) extends HandlersProvider {
  def getHandlers(config: ScriptConfig): Map[Char, StatementHandler] = Map(
    '$' -> new SleepingHandler(new ZincFileCommands(config.testDirectory()), 500),
    '#' -> CommentHandler,
    '>' -> {
      val logger = config.logger().asInstanceOf[ManagedLogger]
      new IncHandler(config.testDirectory().toPath, globalCacheDir, logger, compileToJar)
    }
  )
}
