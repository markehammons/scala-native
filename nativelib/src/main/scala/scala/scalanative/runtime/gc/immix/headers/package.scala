package scala.scalanative.runtime.gc.immix

import scala.scalanative.runtime.RawPtr
import scala.scalanative.LowUnsigned.UL
import scala.scalanative.runtime.Intrinsics
import scala.scalanative.ll.Allocate._
import scala.scalanative.runtime.libc
import scala.scalanative.unsafe.extern

package object headers {
  type rt <: RawPtr

  object Rt {
    def stackAlloc: rt = Intrinsics.stackallocL(size).asInstanceOf[rt]
    def size: UL =
      UL(
        4 + /*id*/
        4 + /*tid*/
        8 + /*namePtr*/
        8 /*clsPtr*/ )
  }

  implicit final class rtOps(val rt: rt) extends AnyVal {
    def id            = Intrinsics.loadInt(Intrinsics.elemRawPtr(rt, 0)).asInstanceOf[Int]
    def id_=(id: Int) = Intrinsics.storeInt(Intrinsics.elemRawPtr(rt, 0), id)

    def tid            = Intrinsics.loadInt(Intrinsics.elemRawPtr(rt, 4)).asInstanceOf[Int]
    def tid_(tid: Int) = Intrinsics.storeInt(Intrinsics.elemRawPtr(rt, 4), tid)

    def name: Ptr[Int] =
      Intrinsics.loadRawPtr(Intrinsics.elemRawPtr(rt, 8)).asInstanceOf[Ptr[Int]]
    def name_=(ptr: Ptr[Int]) =
      Intrinsics.storeRawPtr(Intrinsics.elemRawPtr(rt, 8), ptr)

    def cls: Ptr[Int] =
      Intrinsics
        .loadRawPtr(Intrinsics.elemRawPtr(rt, 16))
        .asInstanceOf[Ptr[Int]]
    def cls_=(ptr: Ptr[Int]) =
      Intrinsics.storeRawPtr(Intrinsics.elemRawPtr(rt, 16), ptr)
  }

  type Rtti <: RawPtr

  object Rtti {
    def stackAlloc: Rtti = Intrinsics.stackallocL(size).asInstanceOf[Rtti]
    def heapAlloc: Rtti  = libc.mallocl(size).asInstanceOf[Rtti]
    def size = UL(
      Rt.size +
        4 +
        4 +
        8
    )
  }

  implicit final class RttiOps(val rtti: Rtti) extends AnyVal {
    def rt: rt    = Intrinsics.elemRawPtr(rtti, 0).asInstanceOf[rt]
    def size: Int = Intrinsics.loadInt(Intrinsics.elemRawPtr(rtti, Rt.size))
    def size_=(i: Int) =
      Intrinsics.storeInt(Intrinsics.elemRawPtr(rtti, Rt.size), i)
    def idRangeUntil: Int =
      Intrinsics.loadInt(Intrinsics.elemRawPtr(rtti, Rt.size + 4))
    def idRangeUntil_=(i: Int) =
      Intrinsics.storeInt(Intrinsics.elemRawPtr(rtti, Rt.size + 4), i)
    def refMapStruct: Ptr[Long] =
      Intrinsics
        .loadRawPtr(Intrinsics.elemRawPtr(rtti, Rt.size + 4 + 4))
        .asInstanceOf[Ptr[Long]]
    def refMapStruct_=(ptr: Ptr[Long]) =
      Intrinsics.storeRawPtr(Intrinsics.elemRawPtr(rtti, Rt.size + 4 + 4), ptr)
  }

  type Obj <: RawPtr

  object Obj {
    def stackAlloc(): Obj = Intrinsics.stackallocL(size).asInstanceOf[Obj]
    def heapAlloc(): Obj  = libc.mallocl(size).asInstanceOf[Obj]
    def size              = UL(8 + 4 + 4)
  }

  implicit final class ObjOps(val obj: Obj) extends AnyVal {
    def rtti: Ptr[Rtti] =
      Intrinsics
        .loadRawPtr(Intrinsics.elemRawPtr(obj, 0))
        .asInstanceOf[Ptr[Rtti]]

    def rtti_=(ptr: Rtti) =
      Intrinsics.storeRawPtr(Intrinsics.elemRawPtr(obj, 0), ptr)

    def length           = Intrinsics.loadInt(Intrinsics.elemRawPtr(obj, 8))
    def length_=(l: Int) = Intrinsics.storeInt(Intrinsics.elemRawPtr(obj, 8), l)

    def stride = Intrinsics.loadInt(Intrinsics.elemRawPtr(obj, 12))
    def stride_=(l: Int) =
      Intrinsics.storeInt(Intrinsics.elemRawPtr(obj, 12), l)

    def isArray = {
      val id = rtti.deref.rt.id
      Headers.__array_ids_min <= id && id <= Headers.__array_ids_max
    }
  }

}

@extern
object Headers {
  val __object_array_id: Int = extern
  val __array_ids_min: Int   = extern
  val __array_ids_max: Int   = extern
}
