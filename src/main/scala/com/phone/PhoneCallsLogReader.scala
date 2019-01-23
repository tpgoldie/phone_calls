package com.phone

case class PhoneCallsLogReader(callsLog: CallsLog) extends PhoneCallsReader {
  override def read(): Seq[PhoneCall] = {
    val records = callsLog.loadCustomerCalls
    records.map(record => PhoneCall(record))
  }
}
