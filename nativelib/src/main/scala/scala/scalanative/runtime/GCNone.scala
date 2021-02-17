package scala.scalanative.runtime

import scala.scalanative.runtime.libc._
import scala.scalanative.unsafe.{CQuote, CSize, CLong}
import scala.scalanative.LowUnsigned._
import scala.scalanative.StackPointers._
import scala.scalanative.ll.Allocate._
import scala.scalanative.runtime.gc.immix.headers._

object GCNone extends GCImpl {
  libc.printf(c"starting\n")
  val heapInfo = HeapInfo.stackalloc
  val obj      = Obj.stackAlloc()
  heapInfo.current = UL(0)
  heapInfo.end = UL(0)
  val current = S_ULP(UL(0l))
  val end     = S_ULP(UL(0l))

  def init() = {
    libc.printf(c"init\n")
    val chunk = UL(4 * 1024 * 1024 * 1024L)
    val c     = UL.fromRawPtr(mallocl(chunk))
    val e     = c plus chunk

    //val a = implicitly[SizeOf[UL]]
    libc.printf(c"here\n")
    obj.length = 5
    obj.stride = 56

    obj.rtti = Rtti.stackAlloc

    libc.printf(c"init1 done\n")
    heapInfo.current = c

    heapInfo.end = e
    libc.printf(c"init2 done\n")
  }

  def alloc_small(rawPtr: RawPtr, size: Long): RawPtr = {
    libc.printf(c"intro\n")
    val sizeAdj = UL(size + (8 - size % 8))
    libc.printf(c"intro2\n")

    val needed = heapInfo.current plus sizeAdj
    libc.printf(c"intro3\n")

    if (needed.lt(heapInfo.end)) {
      val allocPtr = heapInfo.current.toRawPtr
      Intrinsics.storeRawPtr(allocPtr, rawPtr)
      heapInfo.current = heapInfo.current.plus(sizeAdj)
      libc.printf(c"finish\n")
      allocPtr
    } else {
      init()
      alloc_small(rawPtr, size)
    }
  }

  def alloc(rawPtr: RawPtr, size: CSize) = alloc_small(rawPtr, size.underlying)

  def alloc_large(rawPtr: RawPtr, size: CLong) = alloc_small(rawPtr, size)

  def alloc_atomic(rawPtr: RawPtr, size: CSize) =
    alloc_small(rawPtr, size.underlying)

  def collect() {}
}
