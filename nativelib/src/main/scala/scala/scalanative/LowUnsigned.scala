package scala.scalanative

import scala.scalanative.runtime.Intrinsics
import scala.scalanative.runtime.RawPtr
import scala.scalanative.annotation.alwaysinline

private[scalanative] object LowUnsigned {
  type UL <: Long
  object UL {
    @inline def cmpUnsigned(a: Long, b: Long): Int = {
      val unsignedA = a ^ Long.MinValue
      val unsignedB = b ^ Long.MinValue
      if (unsignedA == unsignedB) {
        return 0
      } else if (unsignedA < unsignedB) {
        return -1
      } else {
        return 1
      }
    }

    @alwaysinline def apply(l: Long): UL     = l.asInstanceOf[UL]
    @inline def unbox(ul: UL): Long          = ul.asInstanceOf[Long]
    @inline def lt(ul: UL, ul2: UL): Boolean = cmpUnsigned(ul, ul2) == -1
    @inline def gt(ul: UL, ul2: UL): Boolean = cmpUnsigned(ul, ul2) == 1
    @inline def plus(a: UL, b: UL)           = UL(a + b)
    @inline def toRawPtr(u: UL)              = Intrinsics.castLongToRawPtr(u)
    @inline def fromRawPtr(ptr: RawPtr)      = UL(Intrinsics.castRawPtrToLong(ptr))
  }
  implicit final class ULOps(val l: UL) extends AnyVal {
    @alwaysinline def toLong: Long = l.asInstanceOf[Long]

    @alwaysinline def lt(o: UL) = UL.cmpUnsigned(l.toLong, o.toLong) == -1

    @alwaysinline def gt(o: UL) = UL.cmpUnsigned(l.toLong, o.toLong) == 1

    @alwaysinline def plus(o: UL): UL = UL(l.toLong + o.toLong)

    @alwaysinline def toRawPtr: RawPtr = Intrinsics.castLongToRawPtr(l)
  }
}
