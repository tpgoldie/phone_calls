package com.phone

import java.io.File

import com.phone.Control.using

object Control {
  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}

case class PhoneCallsLogFile(filename: String) extends PhoneCallsLog {
  override def loadCustomerCalls: Array[String] = {
    using(io.Source.fromFile(new File(filename))) {
      source => {
        val lines = source.getLines.toSeq
        Array() ++ lines
      }
    }
  }
}
