package com.phone

case class PhoneBill(customerId: String, calls: Seq[CallDuration], totalCost: BigDecimal) {
}
