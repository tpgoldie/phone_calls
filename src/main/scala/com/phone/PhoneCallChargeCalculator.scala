package com.phone

import com.phone.PhoneCallChargeCalculator.ThreeMinutesInSeconds
import com.phone.CallChargePricingPerSecond.{AdditionalMinutesPrice, ThreeMinutesUnitPrice}

class PhoneCallChargeCalculator {

  def calculateCallCharges(calls: Seq[PhoneCall]) : PhoneBill = {

    val pairs = calls.map(call => Tuple2(call, calculateCallCharge(call.duration)))

    PhoneBill(calls.head.customerId, calls, pairs.flatMap(pair => pair._2).sum)
  }

  private def calculateCallCharge(duration: CallDuration) : Option[BigDecimal] = {
    if (duration.length < 0) {
      return None
    }

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

object PhoneCallChargeCalculator {
  val ThreeMinutesInSeconds = 180
}

object CallChargePricingPerSecond {
  val ThreeMinutesUnitPrice = 0.05
  val AdditionalMinutesPrice = 0.03
}
