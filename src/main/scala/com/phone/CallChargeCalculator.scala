package com.phone

class CallChargeCalculator {

  private val ThreeMinutesInSeconds = 180

  def calculateCallCharges(calls: Seq[CustomerCall]) : CustomerBill = {

    val pairs = calls.map(call => Tuple2(call, calculateCallCharge(call.duration)))

    CustomerBill(calls.head.customerId, calls.map(call => call.duration), pairs.flatMap(pair => pair._2).sum)
  }

  private def calculateCallCharge(duration: CallDuration) : Option[BigDecimal] = {
    if (duration.length < ThreeMinutesInSeconds) {
      return Some(BigDecimal(duration.length * 0.05))
    }

    None
  }
}
