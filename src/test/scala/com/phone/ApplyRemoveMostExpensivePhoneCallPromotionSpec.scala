package com.phone

class ApplyRemoveMostExpensivePhoneCallPromotionSpec extends PhoneCallsProcessingSpec  {
  info("As a customer bill calculator")
  info("I want to be able to process customer calls and apply a single phone call promotion")
  info("So I can calculate customer phone bills")

  feature("Applying a promotion to customer bills") {
    scenario("Apply the following promotion of removing the phone call with the greatest charge") {
      Given("A customer with calls of various durations")

      val calls = Seq(
        PhoneCall("B", "555-433-212", CallDuration(0, 3, 13)),
        PhoneCall("B", "555-333-212", CallDuration(0, 4, 29)),
        PhoneCall("B", "555-333-212", CallDuration(0, 1, 47))
      )

      val billsCalculator = CustomerBillCalculator(new PhoneCallChargeCalculator(), Some(new RemoveMostExpensivePhoneCall()))

      When("the customer bills are calculated")
      val actual = billsCalculator.calculateBills(calls)

      Then("the call charges bill for the customer is calculated and the phone call with the " +
        "greatest charge is removed")

      actual should contain (
        PhoneBill("B", Seq(calls(0), calls(2)), BigDecimal((3 * 60 * 0.05) + (13 * 0.03)) + BigDecimal((1 * 60 + 47) * 0.05))
      )
    }
  }
}
