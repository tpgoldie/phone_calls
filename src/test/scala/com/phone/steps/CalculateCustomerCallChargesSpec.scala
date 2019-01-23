package com.phone.steps

import com.phone._
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class CalculateCustomerCallChargesSpec extends FeatureSpec with GivenWhenThen with Matchers {
  info("As a customer call charge calculator")
  info("I want to be able to process customer call records from the customer calls log file")
  info("So I can calculate customer call charges")

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

  feature("Calculate the call charges for a customer") {
    scenario("Calculate the call charge for a call duration of less than three minutes") {
      Given("A call duration of less than three minutes")
      val duration = CallDuration(0, 2, 59)

      val callChargeCalculator = new CallChargeCalculator()

      When("The call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", CallDuration(0, 2, 59))))

      Then("The customer charge is calculated at 0.05p per second")
      actual should be(CustomerBill("A", Seq(CallDuration(0, 2, 59)), BigDecimal((2 * 60 + 59) * 0.05)))
    }

    scenario("Calculate the call charge for a call duration of exactly three minutes") {
      Given("A call duration of less than three minutes")
      val duration = CallDuration(0, 3, 0)

      val callChargeCalculator = new CallChargeCalculator()

      When("The call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", CallDuration(0, 3, 0))))

      Then("The customer charge is calculated at 0.05p per second")
      actual should be(CustomerBill("A", Seq(CallDuration(0, 3, 0)), BigDecimal(3 * 60 * 0.05)))
    }
  }
}
