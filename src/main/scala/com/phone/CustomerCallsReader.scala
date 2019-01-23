package com.phone

trait CustomerCallsReader {
  def read(): Seq[CustomerCall]
}
