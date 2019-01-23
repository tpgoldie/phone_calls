package com.phone

class CalculatePhoneBillsSpec extends PhoneCallsProcessingSpec  {
  info("As a customer bill calculator")
  info("I want to be able to process customer calls")
  info("So I can calculate customer phone bills")

  feature("Calculating customer bills for different customers") {
    scenario("Calculate customer bills for different customers") {
      Given("Two different customers with calls of various durations")
      val calls = Seq(
                    PhoneCall("A", "555-333-212", CallDuration(0, 2, 51)),
                    PhoneCall("B", "555-433-212", CallDuration(0, 3, 13)),
                    PhoneCall("B", "555-333-212", CallDuration(0, 1, 47)),
                    PhoneCall("A", "555-433-212", CallDuration(0, 0, 19)),
                    PhoneCall("B", "555-333-212", CallDuration(0, 4, 29))
                  )

      val billsCalculator = CustomerBillCalculator(new CallChargeCalculator())

      When("the customer bills are calculated")
      val actual = billsCalculator.calculateBills(calls)

      Then("the call charges bill for each customer is calculated")

      val aCalls = calls.filter(call => call.customerId.equals("A"))
      val bCalls = calls.filter(call => call.customerId.equals("B"))

      actual should contain inOrderOnly (
        PhoneBill("A", aCalls.map(aCall => aCall.duration), BigDecimal((2 * 60 + 51) * 0.05) + BigDecimal(19 * 0.05)),
        PhoneBill("B", bCalls.map(bCall => bCall.duration), BigDecimal((3 * 60 * 0.05) + (13 * 0.03)) +
          BigDecimal((1 * 60 + 47) * 0.05) + BigDecimal((3 * 60 * 0.05) + (1 * 60 + 29) * 0.03))
      )
    }
  }
}
