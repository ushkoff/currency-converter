import play.api.libs.json._

object Main extends App {
  // Display initial information
  println("Currency converter. Developer Ivan Ushkov IPZ-41, ushkov2002452@gmail.com")

  // Read JSON data from the file
  // Improvement proposal #1: set up a websocket solution for every-day-retrieving data from API.
  // Improvement proposal #2: set up an analogue for nodejs cron jobs for automated data updating (once in 24 hours).
  val jsonString = scala.io.Source.fromFile("currencies.json").mkString

  // Parse the JSON data
  val currencyData: Seq[JsValue] = Json.parse(jsonString).as[Seq[JsValue]]

  println("Офіційні курси валют та цінних металів НБУ на дату " + (currencyData.head \ "exchangedate").as[String])
  println("======================")

  // Display available currencies
  val availableCurrencies: Seq[String] = currencyData.map { currency =>
    val title = (currency \ "txt").as[String]
    val cc = (currency \ "cc").as[String]
    val rate = (currency \ "rate").as[Double].toString
    s"[$cc] $title - $rate"
  }

  println("Доступні валюти: \n" + availableCurrencies.mkString("\n"))

  println("======================")

  while (true) {
    // Get user input: selling or buying
    print("Do you want to (S)ell or (B)uy a currency? ")
    val operation = scala.io.StdIn.readLine().toUpperCase()

    // Call the appropriate component based on the operation
    operation match {
      case "S" =>
        val (selectedCurrency, result) = SellComponent.execute(currencyData)
        if (selectedCurrency.nonEmpty) {
          println(s"Selling Result: $result UAH for $selectedCurrency")
          println("======================")
          println(s"Найближчі до вас обмінники, де ви можете виконати обрано операцію з $selectedCurrency:")
          println(s"https://www.google.com/maps?q=nearest+money+exchange+${selectedCurrency}+to+me&sca_esv=587494963&uact=5&gs_lp=Egxnd3Mtd2l6LXNlcnAiIG5lYXJlc3QgbW9uZXkgZXhjaGFuZ2UgVVNEIHRvIG1lMgUQIRigATIFECEYoAEyBBAhGBUyCBAhGBYYHhgdMggQIRgWGB4YHTIIECEYFhgeGB1ImAZQlgFYkQVwAHgCkAEAmAG9AaAB1AaqAQMwLja4AQPIAQD4AQHCAgQQABhHwgIHECEYoAEYCuIDBBgAIEGIBgGQBgg&um=1&ie=UTF-8&sa=X&ved=2ahUKEwjKvfKr-_OCAxXS7rsIHSVxBTYQ_AUoAXoECAEQAw")
        } else {
          println("You entered wrong value. Try again...")
        }
      case "B" =>
        val (selectedCurrency, result) = BuyComponent.execute(currencyData)
        if (selectedCurrency.nonEmpty) {
          println(s"Buying Result: $result $selectedCurrency")
          println("======================")
          println(s"Найближчі до вас обмінники, де ви можете виконати обрано операцію з $selectedCurrency:")
          println(s"https://www.google.com/maps?q=nearest+money+exchange+${selectedCurrency}+to+me&sca_esv=587494963&uact=5&gs_lp=Egxnd3Mtd2l6LXNlcnAiIG5lYXJlc3QgbW9uZXkgZXhjaGFuZ2UgVVNEIHRvIG1lMgUQIRigATIFECEYoAEyBBAhGBUyCBAhGBYYHhgdMggQIRgWGB4YHTIIECEYFhgeGB1ImAZQlgFYkQVwAHgCkAEAmAG9AaAB1AaqAQMwLja4AQPIAQD4AQHCAgQQABhHwgIHECEYoAEYCuIDBBgAIEGIBgGQBgg&um=1&ie=UTF-8&sa=X&ved=2ahUKEwjKvfKr-_OCAxXS7rsIHSVxBTYQ_AUoAXoECAEQAw")
        } else {
          println("You entered wrong value. Try again...")
        }
      case _ =>
        println("Invalid operation. Please choose (S)ell or (B)uy.")
    }

    // Ask if the user wants to continue
    println("======================")
    print("Do you want to perform another conversion? (Y/N): ")
    val continueOperation = scala.io.StdIn.readLine().toUpperCase()
    if (continueOperation != "Y") {
      println("Exiting the program. Goodbye!")
      System.exit(0)
    }
  }
}
