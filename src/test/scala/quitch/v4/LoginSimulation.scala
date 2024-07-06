import io.gatling.core.Predef._
import io.gatling.http.Predef._

class LoginSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://app.quitch.com")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val headers_1 = Map(
    "Origin" -> "https://app.quitch.com",
    "Content-Type" -> "application/x-www-form-urlencoded"
  )

  val scn = scenario("LoginSimulation")
    .exec(http("Load Login Page")
      .get("/login")
      .headers(headers_0)
      .check(status.is(200)) // Check if the login page loads successfully
    )
    .pause(5)
    .exec(http("Submit Login Form")
      .post("/login")
      .headers(headers_1)
      .formParam("email", "chiloane.rk@gmail.com")
      .formParam("password", "password")
      .check(status.is(200)) // Check if the login request was successful
    )
    .exec(http("Access Reporting Page")
      .get("/reporting/classes")
      .check(status.is(200)) // Check if accessing the reporting page is successful
      .check(bodyString.saveAs("reportingPageBody")) // Save the response body to a session variable
    )
    .exec { session =>
      // Print the contents of the reporting page body
      println(session("reportingPageBody").as[String])
      session
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
