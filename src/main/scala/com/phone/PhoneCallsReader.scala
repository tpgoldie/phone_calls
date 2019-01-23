package com.phone

trait PhoneCallsReader {
  def read(): Seq[PhoneCall]
}
