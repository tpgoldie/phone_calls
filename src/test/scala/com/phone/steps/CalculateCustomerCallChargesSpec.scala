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

  feature("Calculate the call charges for a customer") {
    scenario("Calculate the call charge for a call duration of less than three minutes") {
      Given("A call duration of less than three minutes")
      val duration = CallDuration(0, 2, 59)

      val callChargeCalculator = new CallChargeCalculator()

      When("The call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", duration)))

      Then("The customer charge is calculated at 0.05p per second")
      val cost = BigDecimal((2 * 60 + 59) * 0.05)
      actual should be(CustomerBill("A", Seq(CallDuration(0, 2, 59)), BigDecimal((2 * 60 + 59) * 0.05)))
    }

    scenario("Calculate the call charge for a call duration of exactly three minutes") {
      Given("A call duration of exactly three minutes")
      val duration = CallDuration(0, 3, 0)

      val callChargeCalculator = new CallChargeCalculator()

      When("the call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", duration)))

      Then("the customer charge is calculated at 0.05p per second")
      val cost = BigDecimal(3 * 60 * 0.05)
      actual should be(CustomerBill("A", Seq(CallDuration(0, 3, 0)), cost))
    }

    scenario("Calculate the call charge for a call duration of over three minutes") {
      Given("A call duration of over three minutes")
      val duration = CallDuration(0, 4, 23)

      val callChargeCalculator = new CallChargeCalculator()

      When("the call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", duration)))

      Then("the customer charge is calculated at 0.05p per second for the first three minutes and " +
        "the additional time over three minutes is charged at 0.03p per second")
      val cost = BigDecimal(3 * 60 * 0.05) + BigDecimal((1 * 60 + 23) * 0.03)
      actual should be(CustomerBill("A", Seq(CallDuration(0, 4, 23)), cost))
    }

    scenario("Handle a zero call duration") {
      Given("A call duration of zero length")
      val duration = CallDuration(0, 0, 0)

      val callChargeCalculator = new CallChargeCalculator()

      When("the call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", duration)))

      Then("the call charge is 0p")
      val cost = BigDecimal(0)
      actual should be(CustomerBill("A", Seq(CallDuration(0, 0, 0)), cost))
    }

    scenario("handle a negative call duration") {
      Given("A negative call duration")
      val duration = CallDuration(0, -4, 23)

      val callChargeCalculator = new CallChargeCalculator()

      When("the call charge is calculated")
      val actual = callChargeCalculator.calculateCallCharges(Seq(CustomerCall("A", "555-333-212", duration)))

      Then("the customer is not charged")
      val cost = BigDecimal(0)
      actual should be(CustomerBill("A", Seq(CallDuration(0, -4, 23)), cost))
    }
  }

  scenario("Calculate the call charges for more than one call for the same customer") {
    Given("A sequence of calls of mixed duration")
    val durations = Seq(CallDuration(0, 2, 21), CallDuration(0, 3, 0), CallDuration(0, 4, 23))

    val callChargeCalculator = new CallChargeCalculator()

    When("the call charge is calculated")
    val calls = durations.map(duration => CustomerCall("A", "555-333-212", duration))

    val actual = callChargeCalculator.calculateCallCharges(calls)

    Then("the customer charge is the sum of all the call charges for each call")
    val cost = BigDecimal((2 * 60 + 21) * 0.05) + BigDecimal(3 * 60 * 0.05) + BigDecimal(3 * 60 * 0.05) + BigDecimal((1 * 60 + 23) * 0.03)

    actual should be(CustomerBill("A", durations, cost))
  }
}
