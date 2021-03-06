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

package sbt.inc.text

import org.scalacheck.Prop._
import org.scalacheck._
import sbt.internal.inc.text._

object Base64Specification extends Properties("Base64") {

  val java89Encoder = new Java89Encoder

  property("java89") = forAll { (bs: Array[Byte]) =>
    java89Encoder.decode(java89Encoder.encode(bs)).deep == bs.deep
  }
}
