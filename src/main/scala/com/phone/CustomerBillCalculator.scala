package com.phone

case class CustomerBillCalculator(callChargeCalculator: CallChargeCalculator) {
  def calculateBills(calls: Seq[PhoneCall]): Seq[PhoneBill] = {
    val groupByCustomer = calls.groupBy(call => call.customerId)

    val bills = groupByCustomer.map(customer => Tuple2(customer._1, callChargeCalculator.calculateCallCharges(customer._2)))
    bills.values.toSeq
  }
}
