// Create an instance of the SharedPreferenceManager
/**
val sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
*/

// Save user details
/**
sharedPreferenceManager.saveUserDetails(email, userID, password, username, latitude, longitude, address, city, state, country, postalCode, profileURL)
*/

// Retrieve user details
/**
val userDetails = sharedPreferenceManager.getUserDetails()
*/

// Access user details
/**
val email = userDetails.email
val userID = userDetails.userID
// ...*/

