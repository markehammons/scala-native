package scala.scalanative

import scala.scalanative.runtime.Intrinsics
import scala.scalanative.runtime.RawPtr
import scala.scalanative.LowUnsigned._

object StackPointers {
  type S_LP <: RawPtr
  object S_LP {
    @inline def apply(): S_LP = {
      Intrinsics.stackallocL(8).asInstanceOf[S_LP]
    }

  }
  implicit final class S_LPOps(p: S_LP) {
    @inline def toRawPtr: RawPtr = p
    @inline def deref: Long      = Intrinsics.loadLong(p)
    @inline def update(l: Long)  = Intrinsics.storeLong(p, l)
  }

  type S_ULP <: RawPtr
  object S_ULP {
    @inline def apply(u: UL): S_ULP = {
      val sulp = Intrinsics.stackallocL(8).asInstanceOf[S_ULP]
      Intrinsics.storeLong(sulp, u)
      sulp
    }
    @inline def plus(a: S_ULP, b: UL)   = UL(Intrinsics.loadLong(a) + b)
    @inline def update(p: S_ULP, l: UL) = Intrinsics.storeLong(p, l)
    @inline def gt(p: S_ULP, l: UL) =
      UL.gt(UL(Intrinsics.loadLong(p)), l)

    @inline def deref(s: S_ULP): UL = UL(Intrinsics.loadLong(s))
  }
  implicit final class S_ULPOPS(val p: S_ULP) extends AnyVal {
    @inline def toRawPtr: RawPtr = p
    @inline def deref: UL        = Intrinsics.loadLong(p).asInstanceOf[UL]
    @inline def update(l: UL)    = Intrinsics.storeLong(p, l)
    @inline def plus(l: UL): UL  = UL(Intrinsics.loadLong(p) + l)
    // @inline def lt(o: S_ULP)        = deref lt o.deref
    // @inline def gte(o: UL): Boolean = o lt deref
  }

}
