package com.phone

case class CallDuration(hours: Int, minutes: Int, seconds: Int) extends Ordered[CallDuration] {
  val length = hours * 3600 + minutes * 60 + seconds

  override def compare(that: CallDuration): Int = {
    if (this.length == that.length) { 0 }
    else if (this.length > that.length) { 1 }
    else { -1 }
  }
}

object CallDuration {
  def apply(record: String) : CallDuration = {

    val tokens = record.split(":")

    CallDuration(toInteger(tokens(0)), toInteger(tokens(1)), toInteger(tokens(2)))
  }

  def toInteger(token : String): Integer = 10 * token.charAt(0).toString.toInt + token.charAt(1).toString.toInt

}

case class PhoneCall(customerId: String, phoneNumberCalled: String, duration: CallDuration) {
}

object PhoneCall {
  def apply(record: String): PhoneCall = {
    val tokens = record.split(" ")

    new PhoneCall(tokens(0), tokens(1), CallDuration(tokens(2)))
  }
}
