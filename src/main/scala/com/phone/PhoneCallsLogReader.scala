package com.phone

case class PhoneCallsLogReader(callsLog: PhoneCallsLog) extends PhoneCallsReader {
  override def read(): Seq[PhoneCall] = {
    val records = callsLog.loadCustomerCalls
    records.map(record => PhoneCall(record))
  }
}
