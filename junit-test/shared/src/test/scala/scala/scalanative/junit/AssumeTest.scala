package scala.scalanative.junit

// Ported from Scala.js

import org.junit.Assume._
import org.junit._

import scala.scalanative.junit.utils.JUnitTest

class AssumeTest {
  @Test def assumeFail(): Unit = {
    assumeTrue("This assume should not pass", false)
  }
}

class AssumeTestAssertions extends JUnitTest
