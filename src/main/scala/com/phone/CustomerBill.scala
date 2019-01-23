package com.phone

case class CustomerBill(customerId: String, calls: Seq[CallDuration], totalCost: BigDecimal) {
}
