package com.phone

case class CallDuration(hours: Int, minutes: Int, seconds: Int) {}

object CallDuration {
  def apply(record: String) : CallDuration = {

    val tokens = record.split(":")

    CallDuration(toInteger(tokens(0)), toInteger(tokens(1)), toInteger(tokens(2)))
  }

  def toInteger(token : String): Integer = 10 * token.charAt(0).toString.toInt + token.charAt(1).toString.toInt

}

case class CustomerCall(customerId: String, phoneNumberCalled: String, callDuration: CallDuration) {
}

object CustomerCall {
  def apply(record: String): CustomerCall = {
    val tokens = record.split(" ")

    new CustomerCall(tokens(0), tokens(1), CallDuration(tokens(2)))
  }
}
