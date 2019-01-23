package com.phone

import com.phone.CallChargeCalculator.ThreeMinutesInSeconds
import com.phone.CallChargePricingPerSecond.{AdditionalMinutesPrice, ThreeMinutesUnitPrice}

class CallChargeCalculator {

  def calculateCallCharges(calls: Seq[CustomerCall]) : CustomerBill = {

    val pairs = calls.map(call => Tuple2(call, calculateCallCharge(call.duration)))

    CustomerBill(calls.head.customerId, calls.map(call => call.duration), pairs.flatMap(pair => pair._2).sum)
  }

  private def calculateCallCharge(duration: CallDuration) : Option[BigDecimal] = {
    if (duration.length <= ThreeMinutesInSeconds) {
      return Some(BigDecimal(duration.length * ThreeMinutesUnitPrice))
    }

    if (duration.length > ThreeMinutesInSeconds) {
      return Some(BigDecimal(ThreeMinutesInSeconds * ThreeMinutesUnitPrice) +
        BigDecimal((duration.length - ThreeMinutesInSeconds) * AdditionalMinutesPrice))
    }

    None
  }
}

object CallChargeCalculator {
  val ThreeMinutesInSeconds = 180
}

object CallChargePricingPerSecond {
  val ThreeMinutesUnitPrice = 0.05
  val AdditionalMinutesPrice = 0.03
}
