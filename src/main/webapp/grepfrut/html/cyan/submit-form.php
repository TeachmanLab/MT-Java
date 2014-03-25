<?php

// if the url field is empty
if(isset($_POST['url']) && $_POST['url'] == ''){

	// put your email address here
	$youremail = 'yourname@mail.com';  

	// prepare message 
	$body = "You have got new message from your website :
	
	Name:  $_POST[name]
	Company:  $_POST[company]
	Phone:  $_POST[phone]
	Email:  $_POST[email]
	Message:  $_POST[message]";

	if( $_POST['email'] && !preg_match( "/[\r\n]/", $_POST['email']) ) {
	  $headers = "From: $_POST[email]";
	} else {
	  $headers = "From: $youremail";
	}

	mail($youremail, 'Message from Grepfrut', $body, $headers );

}

?>

<!DOCTYPE HTML>
<html>
<head>
<title>Thanks!</title>
</head>
<body>
<p> <img src="img/correct.png" alt="icon" style=" margin-right: 10px;">Thank you! We will get back to you soon.</p>
</body>
</html>