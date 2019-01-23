package com.phone

trait PhoneCallPromotion {
  def apply(calls: Seq[PhoneCall]): Seq[PhoneCall]
}
