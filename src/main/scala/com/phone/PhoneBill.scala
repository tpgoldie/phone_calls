package com.phone

case class PhoneBill(customerId: String, calls: Seq[PhoneCall], totalCost: BigDecimal) {
}
