package com.phone

class CalculatePhoneCallChargesSpec extends PhoneCallsProcessingSpec {
  info("As a customer call charge calculator")
  info("I want to be able to process customer call records from the customer calls log file")
  info("So I can calculate customer call charges")

  feature("Calculate the call charges for a customer") {
    scenario("Calculate the call charge for a call duration of less than three minutes") {
      Given("A call duration of less than three minutes")
      val duration = CallDuration(0, 2, 59)

      val callChargeCalculator = new PhoneCallChargeCalculator()

      When("The call charge is calculated")
      val calls = Seq(PhoneCall("A", "555-333-212", duration))
      val actual = callChargeCalculator.calculateCallCharges(calls)

      Then("The customer charge is calculated at 0.05p per second")
      val cost = BigDecimal((2 * 60 + 59) * 0.05)
      actual should be(PhoneBill("A", calls, BigDecimal((2 * 60 + 59) * 0.05)))
    }

    scenario("Calculate the call charge for a call duration of exactly three minutes") {
      Given("A call duration of exactly three minutes")
      val duration = CallDuration(0, 3, 0)

      val callChargeCalculator = new PhoneCallChargeCalculator()

      When("the call charge is calculated")
      val calls = Seq(PhoneCall("A", "555-333-212", duration))
      val actual = callChargeCalculator.calculateCallCharges(calls)

      Then("the customer charge is calculated at 0.05p per second")
      val cost = BigDecimal(3 * 60 * 0.05)
      actual should be(PhoneBill("A", calls, cost))
    }

    scenario("Calculate the call charge for a call duration of over three minutes") {
      Given("A call duration of over three minutes")
      val duration = CallDuration(0, 4, 23)

      val callChargeCalculator = new PhoneCallChargeCalculator()

      When("the call charge is calculated")
      val calls = Seq(PhoneCall("A", "555-333-212", duration))
      val actual = callChargeCalculator.calculateCallCharges(calls)

      Then("the customer charge is calculated at 0.05p per second for the first three minutes and " +
        "the additional time over three minutes is charged at 0.03p per second")
      val cost = BigDecimal(3 * 60 * 0.05) + BigDecimal((1 * 60 + 23) * 0.03)
      actual should be(PhoneBill("A", calls, cost))
    }

    scenario("Handle a zero call duration") {
      Given("A call duration of zero length")
      val duration = CallDuration(0, 0, 0)

      val callChargeCalculator = new PhoneCallChargeCalculator()

      When("the call charge is calculated")
      val calls = Seq(PhoneCall("A", "555-333-212", duration))
      val actual = callChargeCalculator.calculateCallCharges(calls)

      Then("the call charge is 0p")
      val cost = BigDecimal(0)
      actual should be(PhoneBill("A", calls, cost))
    }

    scenario("handle a negative call duration") {
      Given("A negative call duration")
      val duration = CallDuration(0, -4, 23)

      val callChargeCalculator = new PhoneCallChargeCalculator()

      When("the call charge is calculated")
      val calls = Seq(PhoneCall("A", "555-333-212", duration))
      val actual = callChargeCalculator.calculateCallCharges(calls)

      Then("the customer is not charged")
      val cost = BigDecimal(0)
      actual should be(PhoneBill("A", calls, cost))
    }
  }

  scenario("Calculate the call charges for more than one call for the same customer") {
    Given("A sequence of calls of mixed duration")
    val durations = Seq(CallDuration(0, 2, 21), CallDuration(0, 3, 0), CallDuration(0, 4, 23))

    val callChargeCalculator = new PhoneCallChargeCalculator()

    When("the call charge is calculated")
    val calls = durations.map(duration => PhoneCall("A", "555-333-212", duration))

    val actual = callChargeCalculator.calculateCallCharges(calls)

    Then("the customer charge is the sum of all the call charges for each call")
    val cost = BigDecimal((2 * 60 + 21) * 0.05) + BigDecimal(3 * 60 * 0.05) + BigDecimal(3 * 60 * 0.05) + BigDecimal((1 * 60 + 23) * 0.03)

    actual should be(PhoneBill("A", calls, cost))
  }
}
