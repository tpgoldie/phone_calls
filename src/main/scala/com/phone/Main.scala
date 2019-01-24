package com.phone

object Main extends App {

  override def main(args: Array[String]): Unit = {

    if (args.length < 1) { throw new RuntimeException(s"Missing filename!") }

    val home = System.getProperty("user.home")

    System.out.println(args(0))

    val path = s"$home/${args(0)}"

    val callsLog: PhoneCallsLog = PhoneCallsLogFile(path)

    val phoneCallsReader: PhoneCallsReader = PhoneCallsLogReader(callsLog)

    val billCalculator = CustomerBillCalculator(Some(new RemoveMostExpensivePhoneCall))

    val bills = billCalculator.calculateBills(phoneCallsReader.read())

    bills.foreach(bill => System.out.println(bill.toString))
  }
}
