package com.phone

case class CustomerBillCalculator(callChargeCalculator: PhoneCallChargeCalculator, promotion: Option[PhoneCallPromotion] = None) {
  def calculateBills(calls: Seq[PhoneCall]): Seq[PhoneBill] = {
    val groupByCustomer = calls.groupBy(call => call.customerId)

    val result = promotion match {
      case Some(promo) => groupByCustomer.map(entry => (entry._1, promo.apply(entry._2)))
      case _ => groupByCustomer
    }

    val bills = result.map(customer => Tuple2(customer._1, callChargeCalculator.calculateCallCharges(customer._2)))
    bills.values.toSeq
  }
}
