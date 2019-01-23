package com.phone

class RemoveMostExpensivePhoneCall extends PhoneCallPromotion {
  override def apply(calls: Seq[PhoneCall]): Seq[PhoneCall] = calls.sortWith(_.duration > _.duration).tail
}
