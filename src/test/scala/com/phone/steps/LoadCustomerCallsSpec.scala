package com.phone.steps

import com.phone._
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class LoadCustomerCallsSpec extends FeatureSpec with GivenWhenThen with Matchers {
  info("As a customer call charge calculating process")
  info("I want to be able to successfully load customer call records from the customer calls log file")
  info("So the customer call charges can be calculated")

  feature("Load customer calls") {
    scenario("Customer call log exists and contains valid phone call records") {

      Given("A customer calls log containing valid customer call records")
      val callsLog = new CallsLog {
        val callRecords = Array("A 555-333-212 00:02:03", "B 555-333-202 00:01:20")

        override def loadCustomerCalls: Array[String] = { callRecords }
      }

      When("Load customer calls")
      val customerCallsReader: CustomerCallsReader = CustomerCallsLogReader(callsLog)

      val actual = customerCallsReader.read()

      Then("Customer call records are available for processing")
      actual should contain inOrderOnly (
        CustomerCall("A", "555-333-212", CallDuration(0, 2, 3)),
        CustomerCall("B", "555-333-202", CallDuration(0, 1, 20))
      )
    }
  }
}
