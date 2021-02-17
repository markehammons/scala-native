package scala.scalanative.ll
import scala.language.higherKinds
import scala.scalanative.LowUnsigned._
import scala.scalanative.runtime.RawPtr
import scala.scalanative.runtime.Intrinsics
import scala.scalanative.runtime.libc
import scala.scalanative.unsafe.CQuote
import scala.scalanative.annotation.alwaysinline

object Allocate {
  type SizeOf[T] <: UL
  type Struct[TUP] <: RawPtr

  type Ptr[A] <: RawPtr

  object Ptr {
    def zero[A] = Intrinsics.castLongToRawPtr(0).asInstanceOf[A]
  }

  implicit class PtrIntOps(val p: Ptr[Int]) extends AnyVal {
    def deref: Int = Intrinsics.loadInt(p)

    def set(i: Int) = Intrinsics.storeInt(p, i)
  }

  implicit class PtrULOps(val p: Ptr[UL]) extends AnyVal {
    def deref: UL = {
      libc.printf(c"summoned PtrULOps\n")
      UL(Intrinsics.loadLong(p))
    }
    def update(ul: UL): Unit = Intrinsics.storeLong(p, ul)

    def sizeOf: UL = UL(8)
  }

  implicit class PtrPtr[T <: RawPtr](val p: Ptr[T]) extends AnyVal {
    def deref: T             = Intrinsics.loadRawPtr(p).asInstanceOf[T]
    def update(ptr: T): Unit = Intrinsics.storeRawPtr(p, ptr)
  }

  implicit class Struct2Ops[A, B](val struct: Struct[(A, B)]) extends AnyVal {
    def _1: Ptr[A] = Intrinsics.elemRawPtr(struct, 0).asInstanceOf[Ptr[A]]
    def _2: Ptr[B] =
      Intrinsics.elemRawPtr(struct, 8).asInstanceOf[Ptr[B]]
  }

  type HeapInfo <: RawPtr

  object HeapInfo {
    def stackalloc: HeapInfo = Intrinsics.stackallocL(16).asInstanceOf[HeapInfo]
    def heapalloc: HeapInfo  = libc.mallocl(16).asInstanceOf[HeapInfo]
  }

  implicit final class heapInfoOps(val heapInfo: HeapInfo) extends AnyVal {

    @inline def current =
      UL(Intrinsics.loadLong(Intrinsics.elemRawPtr(heapInfo, 0)))
    @inline def current_=(ul: UL) = {

      Intrinsics.storeLong(Intrinsics.elemRawPtr(heapInfo, 0), ul)
    }

    @alwaysinline def end =
      UL(Intrinsics.loadLong(Intrinsics.elemRawPtr(heapInfo, 8)))
    @alwaysinline def end_=(ul: UL) = {
      Intrinsics.storeLong(Intrinsics.elemRawPtr(heapInfo, 8), ul)
    }
  }

  //implicitly[SizeOf[UL]]
}
