#include <signal.h>
#include <sys/types.h>
#include <stdio.h>

void (*sn_posix_signal_SIG_DFL)(int) = SIG_DFL;

void (*sn_posix_signal_SIG_ERR)(int) = SIG_ERR;

void (*sn_posix_signal_SIG_IGN)(int) = SIG_IGN;

size_t sn_posix_signal_sigset_t_size = sizeof(sigset_t);

void mod_sigaction_sa_mask(struct sigaction *sa, int (*func)(sigset_t *)) {
    printf("running\n");
    func(&(sa->sa_mask));
    return;
}

// sigset_t* alloc_sigset_t()

int sn_posix_signal_SIGEV_NONE = SIGEV_NONE;

int sn_posix_signal_SIGEV_SIGNAL = SIGEV_SIGNAL;

int sn_posix_signal_SIGEV_THREAD = SIGEV_THREAD;

int sn_posix_signal_SIGABRT = SIGABRT;

int sn_posix_signal_SIGALRM = SIGALRM;

int sn_posix_signal_SIGBUS = SIGBUS;

int sn_posix_signal_SIGCHLD = SIGCHLD;

int sn_posix_signal_SIGCONT = SIGCONT;

int sn_posix_signal_SIGFPE = SIGFPE;

int sn_posix_signal_SIGHUP = SIGHUP;

int sn_posix_signal_SIGILL = SIGILL;

int sn_posix_signal_SIGINT = SIGINT;

int sn_posix_signal_SIGKILL = SIGKILL;

int sn_posix_signal_SIGPIPE = SIGPIPE;

int sn_posix_signal_SIGQUIT = SIGQUIT;

int sn_posix_signal_SIGSEGV = SIGSEGV;

int sn_posix_signal_SIGSTOP = SIGSTOP;

int sn_posix_signal_SIGTERM = SIGTERM;

int sn_posix_signal_SIGTSTP = SIGTSTP;

int sn_posix_signal_SIGTTIN = SIGTTIN;

int sn_posix_signal_SIGUSR1 = SIGUSR1;

int sn_posix_signal_SIGUSR2 = SIGUSR2;

int sn_posix_signal_SIGPOLL = SIGPOLL;

int sn_posix_signal_SIGPROF = SIGPROF;

int sn_posix_signal_SIGSYS = SIGSYS;

int sn_posix_signal_SIGTRAP = SIGTRAP;

int sn_posix_signal_SIGURG = SIGURG;

int sn_posix_signal_SIGVTALRM = SIGVTALRM;

int sn_posix_signal_SIGXCPU = SIGXCPU;

int sn_posix_signal_SIGXFSZ = SIGXFSZ;
