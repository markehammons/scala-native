package scala.scalanative.posix

import signal._
import signal.{signal => ssignal}

import unistd.{fork, getppid}
import sys.wait._
import scala.scalanative.unsafe._

import org.junit.Test
import org.junit.Assert._

class SignalTest {

  def react(signal: CInt) = { kill(getppid(), SIGALRM); () }
  @Test def signalChild(): Unit = {

    Zone { implicit z =>
      val action = alloc[sigaction](1)
      mod_sigaction_sa_mask(action, sigemptyset(_))
      action._1 = (signal: CInt) => println("hello!")

      val pid = fork()
      if (pid == 0) {
        action._1 = react _
        println("in child")
        //renamed from signal to avoid problems with signal object
        ssignal(SIGINT, react _)
        //sigaction(SIGINT, action, null)
        val sig = alloc[CInt](1)
        //special allocator for sigset_t
        val sigset = sigset_t()
        sigaddset(sigset, SIGINT)
        sigwait(sigset, sig)
        println("dying")
        kill(getppid(), SIGALRM)
        System.exit(0)
      } else {
        Thread.sleep(5000)
        sigaction(SIGALRM, action, null)
        kill(pid, SIGINT)
        val sig    = alloc[CInt](1)
        val sigset = sigset_t()
        sigaddset(sigset, SIGALRM)
        sigwait(sigset, sig)
        println(s"got ${!sig == SIGALRM}")
      }
    }
    assert(true)
  }

  /** Bindings exercise below */
  def blackhole(any: Any): Unit = ()
  blackhole(SIGABRT)
  blackhole()
}
