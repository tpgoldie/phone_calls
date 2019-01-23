package com.phone

import com.phone.PhoneBill.PoundsScaleFactor

case class PhoneBill(customerId: String, calls: Seq[PhoneCall], totalCost: BigDecimal) {
  override def toString(): String = {
    val amount = totalCost.toInt
    val pounds = amount / PoundsScaleFactor
    val pence = amount % PoundsScaleFactor

    s"${customerId}: £${pounds}.${pence}"
  }
}

object PhoneBill {
  val PoundsScaleFactor = 100
}
