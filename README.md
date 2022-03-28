# Payment-Gateway-Assignment


<b>Introduction:</b><br/>
Implement a payment gateway front end. This assignment highlights some of the typical everyday challenges in the card processing industry.

<br/>
 <b>Project using technology:<br/></b>
•	Java 11 <br/>
•	Maven 3.6.0 <br/>
•	Spring Boot, Spring web, Spring Data Jpa <br/>
•	H2database.<br/>
•	Lombok.<br/>
•	Mockito<br/>
•	Junit <br/>
•	Maven<br/>

<br/>
<b>To run the application: :</b><br/>
  Run PaymentGatewayApplication <br/>
  Run PaymentServiceTest and ValidationServiceTest <br/>
 <br/>
<b>Payment rest controller implements api :</b>

1.	SubmitPayment 
	<br/><b> Post :</b> http://localhost:8080/payments/services/submit
	<br/><b> Body :</b> { "invoice": 12354, "amount": 32423.5, "currency": "EUR",
	 "cardHolder": { "name": "Racheli Amrusi", "email": "s0548438280@gmail.com" }, 
   "card":{ "pan": "5196081888500645", "expiry": "0127", "cvv": "789" }}
<br/><b>  Response :</b>	{  "approved": false,
                         "errors": {
                         "CardHolder.email": "Email should be a valid Email ddress",
                         "Invoice": "Payment already existing, invoice is unique filed",
                         "Card.expiry": "Card expiration date has passed",
                          "Currency": " Currency is not supported value",
                          "card.expiry": "Expiry date should be provide with 4 digits value ( ex : 01/22" }}

2.	GetPayment 
	<br/> <b> Get:</b> http://localhost:8080/payments/services/getPayment/12354
  <br/> <b> Response: (masked data) </b>{
    "paymentDao": {
        "invoice": 12354,
        "amount": 32423.5,
        "currency": "EUR",
        "name": "**************",
        "email": "s0548438280@gmail.com",
        "pan": "************0645",
        "expiry": "****"
    }
}
3.	GetAllPayments </br>
  <b> Get:</b> http://localhost:8080/payments/services/getAllPayments <br/>
  Stream the transaction to an external source for audit.<br/>
	you can configurable file loaction in application.properties file <br/>
  audit.file.location = {{config path}}
	
<b>** attach collection file to test api using Post man **</b>


