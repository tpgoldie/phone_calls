package com.phone

case class PhoneCallsLogReader(callsLog: PhoneCallsLog) extends PhoneCallsReader {
  override def read(): Seq[PhoneCall] = {
    val records = callsLog.loadCustomerCalls
    records.filter(line => !(line == null || line.isEmpty)).map(record => PhoneCall(record))
  }
}
