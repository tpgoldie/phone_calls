package com.phone

case class CustomerCallsLogReader(callsLog: CallsLog) extends CustomerCallsReader {
  override def read(): Seq[CustomerCall] = {
    val records = callsLog.loadCustomerCalls
    records.map(record => CustomerCall(record))
  }
}
