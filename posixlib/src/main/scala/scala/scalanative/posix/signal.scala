package scala.scalanative.posix

import scala.scalanative.unsafe.CFuncPtr2

import scala.scalanative.unsafe._
import scala.scalanative.posix.sys.types.{pid_t, uid_t, pthread_t, size_t}

import scala.scalanative.posix.time.timespec

@extern
object signal {
  type sigcatcher = CFuncPtr1[CInt, Unit]

  def signal(sig: CInt, func: sigcatcher): sigcatcher = extern

  //todo: CUnion2[CInt, Ptr[Unit]] type as final value
  type sigval = Ptr[Unit]

  type siginfo_t =
    CStruct9[CInt, CInt, CInt, pid_t, uid_t, Ptr[Unit], CInt, CLong, sigval]

  type sa_sigaction = CFuncPtr3[CInt, Ptr[siginfo_t], Ptr[sigset_t], Unit]

  def kill(pid: pid_t, sig: CInt): CInt = extern

  def killpg(pid: pid_t, sig: CInt): CInt = extern

  def psiginfo(siginfo: Ptr[siginfo_t], message: CString): Unit = extern

  def psignal(signal: CInt, message: CString): Unit = extern

  def pthread_kill(thread: pthread_t, signal: CInt): CInt               = extern
  def pthread_sigmask(how: CInt, set: Ptr[Unit], oset: Ptr[Unit]): CInt = extern

  def raise(sig: CInt): CInt = extern

  type sigaction = CStruct4[sigcatcher, Ptr[Unit], CInt, sa_sigaction]

  def sigaction(sig: CInt, act: Ptr[sigaction], oact: Ptr[sigaction]): CInt =
    extern

  //we can't know what sigset_t is thanks to posix saying it can be a struct or an int
  // void pointer is the best we can do
  //todo: make sigset_t an opaque type
  type sigset_t <: Unit

  object sigset_t {
    def apply()(implicit z: Zone): Ptr[sigset_t] = {
      val set =
        z.alloc(sn_posix_signal_sigset_t_size).asInstanceOf[Ptr[sigset_t]]

      val res = sigemptyset(set)
      if (res == 0)
        set
      else
        throw new Error("Could not allocate sigset_t")
    }
  }

  def mod_sigaction_sa_mask(sigaction: Ptr[sigaction],
                            func: CFuncPtr1[Ptr[sigset_t], CInt]): Unit = extern

  val sn_posix_signal_sigset_t_size: size_t = extern

  def sigemptyset(set: Ptr[sigset_t]): CInt               = extern
  def sigfillset(set: Ptr[sigset_t]): CInt                = extern
  def sigaddset(set: Ptr[sigset_t], signum: CInt): CInt   = extern
  def sigdelset(set: Ptr[sigset_t], signum: CInt): CInt   = extern
  def sigismember(set: Ptr[sigset_t], signum: CInt): CInt = extern

  type stack_t = CStruct3[Ptr[sigset_t], size_t, CInt]

  def sigaltstack(ss: Ptr[stack_t], old_ss: Ptr[stack_t]): CInt = extern

  def sighold(sig: CInt): CInt = extern

  def sigignore(sig: CInt): CInt = extern

  def siginterrupt(sig: CInt): CInt = extern

  def sigpause(sig: CInt): CInt = extern

  def sigpending(set: Ptr[sigset_t]): CInt = extern

  def sigprocmask(how: CInt, set: Ptr[sigset_t]): CInt = extern

  def sigqueue(pid: pid_t, signo: CInt, sigval: sigval): CInt = extern

  def sigrelse(sig: CInt): CInt = extern

  type sigsetfn = CFuncPtr1[CInt, Unit]
  def sigset(sig: Int, disp: sigsetfn): sigsetfn = extern

  def sigsuspend(sigmask: Ptr[sigset_t]): CInt = extern

  def sigtimedwait(set: Ptr[sigset_t],
                   info: Ptr[siginfo_t],
                   timeout: Ptr[timespec]): CInt = extern

  def sigwait(set: Ptr[sigset_t], sig: Ptr[CInt]): CInt = extern

  def sigwaitinfo(set: Ptr[sigset_t], info: Ptr[siginfo_t]): CInt = extern

  @name("sn_posix_signal_SIGABRT")
  val SIGABRT: CInt = extern

  @name("sn_posix_signal_SIGALRM")
  val SIGALRM: CInt = extern

  @name("sn_posix_signal_SIGBUS")
  val SIGBUS: CInt = extern

  @name("sn_posix_signal_SIGCHLD")
  val SIGCHLD: CInt = extern

  @name("sn_posix_signal_SIGCONT")
  val SIGCONT: CInt = extern

  @name("sn_posix_signal_SIGFPE")
  val SIGFPE: CInt = extern

  @name("sn_posix_signal_SIGHUP")
  val SIGHUP: CInt = extern

  @name("sn_posix_signal_SIGILL")
  val SIGILL: CInt = extern

  @name("sn_posix_signal_SIGINT")
  val SIGINT: CInt = extern

  @name("sn_posix_signal_SIGKILL")
  val SIGKILL: CInt = extern

  @name("sn_posix_signal_SIGPIPE")
  val SIGPIPE: CInt = extern

  @name("sn_posix_signal_SIGQUIT")
  val SIGQUIT: CInt = extern

  @name("sn_posix_signal_SIGSEGV")
  val SIGSEGV: CInt = extern

  @name("sn_posix_signal_SIGSTOP")
  val SIGSTOP: CInt = extern

  @name("sn_posix_signal_SIGTERM")
  val SIGTERM: CInt = extern

  @name("sn_posix_signal_SIGTSTP")
  val SIGTSTP: CInt = extern

  @name("sn_posix_signal_SIGTTIN")
  val SIGTTIN: CInt = extern

  @name("sn_posix_signal_SIGTTOU")
  val SIGTTOU: CInt = extern

  @name("sn_posix_signal_SIGUSR1")
  val SIGUSR1: CInt = extern

  @name("sn_posix_signal_SIGUSR2")
  val SIGUSR2: CInt = extern

  @name("sn_posix_signal_SIGPOLL")
  val SIGPOLL: CInt = extern

  @name("sn_posix_signal_SIGPROF")
  val SIGPROF: CInt = extern

  @name("sn_posix_signal_SIGSYS")
  val SIGSYS: CInt = extern

  @name("sn_posix_signal_SIGTRAP")
  val SIGTRAP: CInt = extern

  @name("sn_posix_signal_SIGURG")
  val SIGURG: CInt = extern

  @name("sn_posix_signal_SIGVTALRM")
  val SIGVTALRM: CInt = extern

  @name("sn_posix_signal_SIGXCPU")
  val SIGXCPU: CInt = extern

  @name("sn_posix_signal_SIGXFSZ")
  val SIGXFSZ: CInt = extern
}
