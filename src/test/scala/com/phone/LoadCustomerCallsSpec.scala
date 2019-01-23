package com.phone

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class LoadCustomerCallsSpec extends FeatureSpec with GivenWhenThen with Matchers {
  info("As a customer call charge calculator")
  info("I want to be able to process customer call records from the customer calls log file")
  info("So I can calculate customer call charges")

  feature("Load customer calls") {
    scenario("Customer call log exists and contains valid phone call records") {

      Given("A customer calls log containing valid customer call records")
      val callsLog: CallsLog = new CallsLog {
        val callRecords = Array("A 555-333-212 00:02:03", "B 555-333-202 00:01:20")

        override def loadCustomerCalls: Array[String] = { callRecords }
      }

      When("loading the customer calls")
      val customerCallsReader: CustomerCallsReader = CustomerCallsLogReader(callsLog)

      val actual = customerCallsReader.read()

      Then("the customer call records are available for processing")
      actual should contain inOrderOnly (
        CustomerCall("A", "555-333-212", CallDuration(0, 2, 3)),
        CustomerCall("B", "555-333-202", CallDuration(0, 1, 20))
      )
    }
  }
}
