import play.api.libs.json._
import scala.math.BigDecimal.RoundingMode

object SellComponent {
  def execute(currencyData: Seq[JsValue]): (String, Double) = {
    // Get user input: choose a currency to sell
    print("Enter the currency code to sell (e.g., USD): ")
    val selectedCurrency = scala.io.StdIn.readLine().toUpperCase()

    // Check if the selected currency exists
    currencyData.find(currency => (currency \ "cc").as[String] == selectedCurrency) match {
      case Some(currency) =>
        // Get user input: enter the amount to sell
        var amount: Double = 0.0
        do {
          print(s"Enter the amount to sell (in $selectedCurrency): ")
          amount = scala.io.StdIn.readDouble()
        } while (amount <= 0)

        // Perform the conversion for selling
        val sellingRate = (currency \ "rate").as[Double]
        val result = BigDecimal(amount * sellingRate).setScale(2, RoundingMode.HALF_UP).toDouble

        // Return the result and the chosen currency
        (selectedCurrency, result)

      case None =>
        // Display a message if the selected currency does not exist
        println(s"The currency with code $selectedCurrency does not exist.")
        // Return a default value in case of an error
        ("", 0.0)
    }
  }
}