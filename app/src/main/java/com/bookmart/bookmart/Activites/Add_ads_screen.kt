package com.bookmart.bookmart.Activites

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.bookmart.bookmart.Adapters.Poast_ads_Adapter.Post_ads_adapter
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface.ApiService
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.ApiClient_OperationResponse
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.Book_upload_response

import com.bookmart.bookmart.Model.Post_Ads_model.ApiResponse
import com.bookmart.bookmart.R
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferenceManager
import com.bookmart.bookmart.Required_App_operations.InternetConnectionChecker
import com.bookmart.bookmart.databinding.ActivityAddAdsScreenBinding
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.Required_FILES.VolleySingleton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates


class Add_ads_screen : AppCompatActivity(), Post_ads_adapter.OnImageClickListener,
    Post_ads_adapter.OnItemClickListener, Post_ads_adapter.ImageLoadCallback {
    //    val binding by lazy {
//        ActivityAddAdsScreenBinding.inflate(layoutInflater)
//    }
    lateinit var binding: ActivityAddAdsScreenBinding
    private val handler = Handler(Looper.getMainLooper())
    private var progressDialog: ProgressDialog? = null  //progress Dialog
    var ApicallCount by Delegates.notNull<Int>()
    var baseurl: String? = "https://www.onlinevideodownloader.co/"
    var final_co_numner: String? = null
    var condition: String = ""
    var contact_premmision: String = ""
    lateinit var Compressed_IMAGE_1: ByteArray
    lateinit var Compressed_IMAGE_2: ByteArray
    lateinit var Compressed_IMAGE_3: ByteArray
    var back_STATUS: Int = 0
    private lateinit var adapter: Post_ads_adapter
    private var progress = 0

    //uploaded book id
    var uploadedBookId: String = "null"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  setFullScreen()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color
            window.statusBarColor = ContextCompat.getColor(this, R.color.MainAppColour)
        }
        //setting adapter for dummy
        cate_Adapter()
        // Initialize your data and RecyclerView here


        binding.frontImage.setOnClickListener {
            imagePicker(1)
        }
        binding.backImage.setOnClickListener {
            imagePicker(2)
        }
        binding.anysideimage.setOnClickListener {
            imagePicker(3)
        }

        binding.clicktomovebutton.setOnClickListener {
            if (editTextnull_or_fill_checker()) {
                //  showToastMAssage(this@Add_ads_screen, "all done preform further sction")

                // Start the shimmer effect
                switchViewsWithAnimation(binding.scroolviewbasic, binding.scrollView6)
                binding.backbutton.setImageResource(R.drawable.baseline_arrow_back_ios_24)
                binding.seclayout.visibility = View.GONE
                binding.firstLayoutBootonLayout.visibility = View.VISIBLE
                back_STATUS = 1
            } else if (!editTextnull_or_fill_checker()) {
                showToastMAssage(this@Add_ads_screen, "Must be fill required fields")
            }


        }
//handling snackbar clicks
        binding.bookuplodedagain.setOnClickListener {
            dismisssnackbar()
            handler.postDelayed({
                // This code will run after a delay of 2000 milliseconds (2 seconds)
                // Perform your actions here
                //here are papering environment for user that can user user again upload new book
                repraperingLayout()

            }, 1000)

        }
        //handling snack close button
        binding.snackclosebutton.setOnClickListener {
            dismisssnackbar()
        }

        // Set a listener to handle radio button selections
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            val selectedOptionText: String = selectedRadioButton.text.toString()
            binding.horizontalProgressBar.progress = 85

            // Handle the selected option
            // You can perform actions based on the selected option here
        }
        // Set a listener to handle radio button selections
        binding.contactOption.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            val selectedOptionText: String = selectedRadioButton.text.toString()
            binding.horizontalProgressBar.progress = 90

            // Handle the selected option
            // You can perform actions based on the selected option here
        }

        //preforming main action to add book on server
        binding.backbutton.setOnClickListener {
            if (back_STATUS == 0) {
                try {
                    // Create an AlertDialog
                    Handler(Looper.getMainLooper()).postDelayed({
                    }, 1000)
                    AlertDialog.Builder(this@Add_ads_screen)
                        .setTitle("Exit without post? ")
                        .setMessage("You may lost your data!").setIcon(R.drawable.baseline_error_24)
                        .setPositiveButton("Exit") { dialog, _ ->
                            // Handle OK button click
                            super.onBackPressed()
                            // Set the exit animation
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
                            finish();
                            dialog.dismiss()
                        }
                        .setNegativeButton("Close") { dialog, _ ->
                            // Handle Cancel button click
                            dialog.dismiss()
                        }
                        .show()


                } catch (e: Exception) {
                    Toast.makeText(this@Add_ads_screen, e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (back_STATUS == 1) {
                //  showToastMAssage(this@Add_ads_screen, "all done preform further sction")
                switchViewsWithAnimation(binding.scrollView6, binding.scroolviewbasic)

                binding.backbutton.setImageResource(R.drawable.baseline_clear_24)
                binding.firstLayoutBootonLayout.visibility = View.GONE
                binding.seclayout.visibility = View.VISIBLE

                back_STATUS = 0
            }

        }
        //preforming action for show user uploaded book on main veiwer
        binding.viewuploadedbook.setOnClickListener {
            try {
                val intent = Intent(this@Add_ads_screen, Book_Ads_viewer_detailed::class.java)
                if(uploadedBookId.toString().equals("")||uploadedBookId.toString().equals("null")){
                    //handle impossible error It will throw when book id will be null
                }else {
                    intent.putExtra("bookId", uploadedBookId)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

            } catch (e: Exception) {
                //handle error unable to start ad viewer activity

            }
        }
        //final operation button
        binding.climovebuttonfianl2.setOnClickListener {

            //check categories selected or not
            binding.shimmerViewContainer.stopShimmer()
            if (binding.cateText.text.toString().trim() != "") {
                //function to check all image uploaded or not
                if (Check_IMAGE_UPLOADED_OR_NOT()) {
                    //now checking condition selected or not
                    //  Check_CONDITION_SELECTED_OR_NOT()
                    if (Check_CONDITION_SELECTED_OR_NOT()) {

                        if (Checking_contact_ready_OR_NOT()) {

                            if (check_contactMethod_select_or_not()) {

                                //preforming action to  book  data to server
                                /**        how we are going to hndle this operation
                                step 1: collet whole book data and make a arraylst(data bundle)
                                step 2: request to ApiService to add book to server
                                 */
                                // Call this method to hide the keyboard
                                hideKeyboard(binding.coNumber)
                                hideKeyboard(binding.readdressOptional)
                                //with i am setting up Progress
                                showProgressDialog()
                                process_DaaCOLECION() //function for data collection


                            } else if (!check_contactMethod_select_or_not()) {
                                Toast.makeText(
                                    this@Add_ads_screen,
                                    "contact permissions!",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        } else if (!Checking_contact_ready_OR_NOT()) {
                            Toast.makeText(
                                this@Add_ads_screen,
                                "Contact number not valid!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                        }

                        //now checking condition selected or not

                    } else if (!Check_CONDITION_SELECTED_OR_NOT()) {
                        Toast.makeText(
                            this@Add_ads_screen,
                            "condition not selected",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        //this erro impossible
                    }


                } else if (!Check_IMAGE_UPLOADED_OR_NOT()) {
                    Toast.makeText(
                        this@Add_ads_screen,
                        "image not uploaded",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //this erro impossible
                }
            } else if (binding.cateText.text.toString().trim() == "") {
                Toast.makeText(
                    this@Add_ads_screen,
                    "Select categories",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //unknown error still it's impossible

            }


        }










        binding.readdressOptional.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                progressbar_updateder()


            }
        })
        binding.coNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                progressbar_updateder()


            }
        })
        binding.bookmaintitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                progressbar_updateder()


            }
        })
        binding.bookfullname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here
                progressbar_updateder()


            }
        })
        binding.booksubtitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.authername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.publisher.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.publishingyear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.basedon.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.standard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.anyboardpreffrence.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.booklanguage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.anskingprise.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here

                progressbar_updateder()

            }
        })
        binding.descriptionforusers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
                binding.textcounter.setText(count.toString() + "/" + 500)


            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed.
                val newText = s.toString() // Get the updated text
                // Your code to handle the updated text here
                binding.textcounter.setText(newText.length.toString() + "/" + 500)
                progressbar_updateder()

            }
        })
    }

    override fun onBackPressed() {

        if (back_STATUS == 1) {
            //  showToastMAssage(this@Add_ads_screen, "all done preform further sction")
            switchViewsWithAnimation(binding.scrollView6, binding.scroolviewbasic)

            binding.backbutton.setImageResource(R.drawable.baseline_clear_24)
            binding.firstLayoutBootonLayout.visibility = View.GONE
            binding.seclayout.visibility = View.VISIBLE

            back_STATUS = 0


        } else {
            try {
                // Create an AlertDialog
                Handler(Looper.getMainLooper()).postDelayed({
                }, 1000)
                AlertDialog.Builder(this@Add_ads_screen)
                    .setTitle("Exit without post? ")
                    .setMessage("You may lost your data!").setIcon(R.drawable.baseline_error_24)
                    .setPositiveButton("Exit") { dialog, _ ->
                        // Handle OK button click
                        super.onBackPressed()
                        // Set the exit animation
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
                        finish();
                        // Set the exit animation
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
                        finish();
                        dialog.dismiss()
                    }
                    .setNegativeButton("Close") { dialog, _ ->
                        // Handle Cancel button click
                        dialog.dismiss()
                    }
                    .show()


            } catch (e: Exception) {
                Toast.makeText(this@Add_ads_screen, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }


        }

    }

    private fun dataCollection(): ArrayList<String> {
        val data = ArrayList<String>()

        // Book details
        val bookMainTitle: String = binding.bookmaintitle.text.toString().trim()
        val bookFullName: String = binding.bookfullname.text.toString().trim()
        val bookSubTitle: String = binding.booksubtitle.text.toString().trim()

        data.add(bookMainTitle)
        data.add(bookFullName)
        data.add(bookSubTitle)

        // Author and publisher details
        val authorName: String = binding.authername.text.toString().trim()
        val publisherName: String = binding.publisher.text.toString().trim()
        val publishingYear: String = binding.publishingyear.text.toString().trim()

        data.add(authorName)
        data.add(publisherName)
        data.add(publishingYear)

        // About the book
        val basedOn: String = binding.basedon.text.toString().trim()
        val standard: String = binding.standard.text.toString().trim()
        val boardPreference: String = binding.anyboardpreffrence.text.toString().trim()
        val bookLanguage: String = binding.booklanguage.text.toString().trim()

        data.add(basedOn)
        data.add(standard)
        data.add(boardPreference)
        data.add(bookLanguage)

        // Asking price and description
        val askingPrice: String = binding.anskingprise.text.toString().trim()
        val description: String = binding.descriptionforusers.text.toString().trim()

        data.add(askingPrice)
        data.add(description)

        return data
    }

    fun cate_Adapter() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmer()
        // Initialize RecyclerView
        binding.recycForCate.layoutManager =
            LinearLayoutManager(this@Add_ads_screen, LinearLayoutManager.HORIZONTAL, false)

        val categoriesList = mutableListOf<ApiResponse>()
        val url = "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_all_categories.php"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Handle the successful response
                try {
                    // Parse the JSON response
                    val jsonResponse = JSONObject(response)
                    val categoriesArray = jsonResponse.getJSONArray("categories")

                    // Process the list of categories
                    for (i in 0 until categoriesArray.length()) {
                        val categoryObject = categoriesArray.getJSONObject(i)
                        val categoryId = categoryObject.getString("category_id")
                        val categoryName = categoryObject.getString("category_name")
                        val categoryImage = categoryObject.getString("category_image")

                        // Do something with the category data
                        // Create a Category object and add it to the list
                        val category =
                            ApiResponse(categoryId.toString(), categoryImage, categoryName)
                        categoriesList.add(category)
                    }

                    // Create a Post_ads_adapter and set it to the RecyclerView
                    // Assuming this code is inside YourActivity

// Create a Post_ads_adapter and set it to the RecyclerView
                    val adapter = Post_ads_adapter(
                        object : Post_ads_adapter.ImageLoadCallback {
                            override fun onImageLoadSuccess() {
                                // Handle image load success if needed
                                // For example, you can perform additional UI updates here
                                binding.shimmerViewContainer.hideShimmer()
                            }

                            override fun onImageLoadError() {
                                // Handle image load error if needed
                                // For example, you can perform additional error handling actions
                                binding.shimmerViewContainer.hideShimmer()
                            }
                        },
                        categoriesList,
                        object : Post_ads_adapter.OnItemClickListener {
                            override fun onItemClicked(position: Int) {
                                // Handle item click if needed
                                // Toast.makeText(this@YourActivity, position.toString(), Toast.LENGTH_SHORT).show()
                            }
                        },
                        object : Post_ads_adapter.OnImageClickListener {
                            override fun onImageClicked(position: Int, title: String, id: String) {
                                runOnUiThread {
                                    // Updating UI and setting category on TextView
                                    binding.cateChooseLay.visibility = View.VISIBLE
                                    binding.cateText.visibility = View.VISIBLE
                                    binding.cateText.text = title
                                    binding.cateText.setHint(id.toString().trim())
                                    binding.horizontalProgressBar.progress = 65
                                    // Toast.makeText(this@YourActivity, "Your book category $title", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )

// Set the adapter to the RecyclerView

                    binding.recycForCate.adapter = adapter


                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.shimmerViewContainer.stopShimmer()
                }
            },
            { error ->
                // Handle errors
                error.printStackTrace()
            })

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(this).requestQueue.add(request)
    }


    fun showToastMAssage(context: Context, massage: String) {
        Toast.makeText(context, massage.toString(), Toast.LENGTH_SHORT).show()
    }

    fun checkEditTextsNotNull(vararg editTexts: EditText): List<Boolean> {
        val editTextStatusList = mutableListOf<Boolean>()

        for (editText in editTexts) {
            val text = editText.text.toString().trim()
            val isNotNull = text.isNotEmpty()
            editTextStatusList.add(isNotNull)
        }

        return editTextStatusList
    }

    fun progressbar_updateder() {
        // Initialize the rest of your EditText views (editText3, editText4, ..., editText12)

        val editTexts = arrayOf(
            binding.bookmaintitle,
            binding.bookfullname,
            binding.booksubtitle,
            binding.authername,
            binding.publisher,
            binding.publishingyear,
            binding.basedon,
            binding.standard,
            binding.anyboardpreffrence,
            binding.booklanguage,
            binding.anskingprise,
            binding.descriptionforusers,
            binding.readdressOptional,
            binding.coNumber
        )

        val editTextStatusList = checkEditTextsNotNull(*editTexts)
        var false_STA: Int = 0
        var true_STA: Int = 0
        for (i in 0..13) {

            editTextStatusList[i]

            if (!editTextStatusList[i]) {
                val b = false_STA++
                //    showToastMAssage(this@Add_ads_screen,b.toString()+"false")
            } else if (editTextStatusList[i]) {
                val c = true_STA++
                //   showToastMAssage(this@Add_ads_screen,c.toString()+"true")
            }


        }
//        showToastMAssage(this@Add_ads_screen,false_STA.toString())
//        showToastMAssage(this@Add_ads_screen,true_STA.toString())
        // Your code to handle the updated text here
        if (true_STA == 0) {
            binding.horizontalProgressBar.progress = 0
        } else if (true_STA == 1) {
            binding.horizontalProgressBar.progress = 5
        } else if (true_STA == 2) {
            binding.horizontalProgressBar.progress = 10
        } else if (true_STA == 3) {
            binding.horizontalProgressBar.progress = 15
        } else if (true_STA == 4) {
            binding.horizontalProgressBar.progress = 20
        } else if (true_STA == 5) {
            binding.horizontalProgressBar.progress = 25
        } else if (true_STA == 6) {
            binding.horizontalProgressBar.progress = 30
        } else if (true_STA == 7) {
            binding.horizontalProgressBar.progress = 35
        } else if (true_STA == 8) {
            binding.horizontalProgressBar.progress = 40
        } else if (true_STA == 9) {
            binding.horizontalProgressBar.progress = 45
        } else if (true_STA == 10) {
            binding.horizontalProgressBar.progress = 50
        } else if (true_STA == 11) {
            binding.horizontalProgressBar.progress = 55
        } else if (true_STA == 12) {
            binding.horizontalProgressBar.progress = 60
        } else if (true_STA == 13) {
            binding.horizontalProgressBar.progress = 90
        } else if (true_STA == 14) {
            binding.horizontalProgressBar.progress = 95
        }
    }

    fun editTextnull_or_fill_checker(): Boolean {
        // Initialize the rest of your EditText views (editText3, editText4, ..., editText12)
        var status: Boolean = false
        val editTexts = arrayOf(
            binding.bookmaintitle,
            binding.bookfullname,
            binding.booksubtitle,
            binding.authername,
            binding.publisher,
            binding.publishingyear,
            binding.basedon,
            binding.standard,
            binding.anyboardpreffrence,
            binding.booklanguage,
            binding.anskingprise,
            binding.descriptionforusers
        )

        val editTextStatusList = checkEditTextsNotNull(*editTexts)

        if (editTextStatusList[0]) {

        } else {
            binding.maintitle.error = "Book heading title"
            binding.bookmaintitle.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[1]) {


        } else {

            binding.sectitle.error = "Book full name"
            binding.bookfullname.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[2]) {

        } else {

            binding.subtitle.error = "Book full name"
            binding.booksubtitle.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[3]) {

        } else {

            binding.authererror.error = "Author name"
            binding.authername.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[4]) {

        } else {

            binding.publishererror.error = "Publisher name"
            binding.publisher.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[5]) {

        } else {

            binding.publishingyearerror.error = "Publishing year"
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[6]) {

        } else {

            binding.basederror.error = "Based on"
            binding.basedon.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[7]) {

        } else {

            binding.standarderror.error = "Standard of book"
            binding.standard.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[8]) {

        } else {

            binding.boardpreffrenceerror.error = "Any Board preference"
            binding.anyboardpreffrence.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[9]) {

        } else {

            binding.languageerror.error = "Book language"
            binding.booklanguage.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[10]) {

        } else {

            binding.prisingerror.error = "Demanding prise"
            binding.anskingprise.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }
        if (editTextStatusList[11]) {


        } else {

            binding.decribeerror.error = "Description for visiter"
            binding.descriptionforusers.requestFocus()
            scrollToShowEditText(binding.scroolviewbasic, binding.bookmaintitle)
        }

        var false_STA: Int = 0
        var true_STA: Int = 0
        for (i in 0..11) {

            editTextStatusList[i]

            if (!editTextStatusList[i]) {
                val b = false_STA++
                //    showToastMAssage(this@Add_ads_screen,b.toString()+"false")
            } else if (editTextStatusList[i]) {
                val c = true_STA++
                //   showToastMAssage(this@Add_ads_screen,c.toString()+"true")
            }


        }

        if (true_STA == 12) {

            status = true
        }

        return status
    }

    private fun scrollToShowEditText(scrollView: ScrollView, editText: EditText) {
        // Get the Y-coordinate of the EditText relative to its parent
        val editTextY = editText.y.toInt()

        // Scroll to the Y-coordinate of the EditText
        scrollView.post {
            scrollView.scrollTo(0, editTextY)
        }
    }

    fun switchViewsWithAnimation(viewToHide: View, viewToShow: View) {
        // Hide the first view with animation
        viewToHide.animate()
            .alpha(0f)
            .translationYBy(100f) // You can adjust the translation or other properties
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(100)
            .withEndAction {
                // Once the animation is complete, set visibility to GONE
                viewToHide.visibility = View.GONE

                // Show the second view with animation
                viewToShow.visibility = View.VISIBLE
                viewToShow.alpha = 0f
                viewToShow.translationY = -100f // Adjust as needed
                viewToShow.animate()
                    .alpha(1f)
                    .translationYBy(100f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(100)
                    .start()
            }
    }

    fun switchViewsWithAnimation_FOR_BACKING(viewToHide: View, viewToShow: View) {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        // Hide the first view with animation
        viewToHide.animate()
            .translationXBy(-screenWidth.toFloat())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .withEndAction {
                // Once the animation is complete, set visibility to GONE
                viewToHide.visibility = View.GONE

                // Show the second view with animation
                viewToShow.visibility = View.VISIBLE
                viewToShow.translationX = screenWidth.toFloat()

                viewToShow.animate()
                    .translationXBy(-screenWidth.toFloat())
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(500)
                    .start()
            }
    }


    fun imagePicker(imageIdentifier: Int) {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imageIdentifier)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1 || requestCode == 2 || requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Handle the selected image URI
                val selectedImageUri: Uri = data.data!!

                // Now, you can use the selectedImageUri for further processing
//                imagePath = getRealPathFromUri(this, selectedImageUri)
                // imagePath = selectedImageUri.toString()
                // ... (upload the image using Retrofit or perform other actions)
                if (requestCode == 1) {
                    binding.frontImage.setImageURI(selectedImageUri)
                    watermarkAdd(binding.frontImage, 1)
                    binding.horizontalProgressBar.progress = 70
                } else if (requestCode == 2) {
                    binding.backImage.setImageURI(selectedImageUri)
                    watermarkAdd(binding.backImage, 2)
                    binding.horizontalProgressBar.progress = 75
                } else if (requestCode == 3) {
                    binding.anysideimage.setImageURI(selectedImageUri)
                    watermarkAdd(binding.anysideimage, 3)
                    binding.horizontalProgressBar.progress = 80
                }


            }

        }
    }


    fun watermarkAdd(imageview: ImageView, ImageView_INDENTIFIER: Int) {
        // Ensure that imageView and originalBitmap are not null
        if (imageview.drawable != null) {
            val originalBitmap: Bitmap = (imageview.drawable as BitmapDrawable).bitmap

            // Create a mutable copy of the original bitmap without recycling it
            val watermarkedBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

            // Create a Canvas to draw on the bitmap
            val canvas = Canvas(watermarkedBitmap)

            val watermarkResourceId1 = R.drawable.img_1
            val watermarkResourceId2 = R.drawable.img_1

            val trWeight = watermarkedBitmap.width.toFloat() - 100.0f
            val trHeight = watermarkedBitmap.height.toFloat() - 100.0f

            if (trWeight > 1000.0f && trHeight > 2000.0f) {
                val watermarkBitmap =
                    BitmapFactory.decodeResource(resources, watermarkResourceId1)
                // Calculate the position to center the watermark
                val x = (watermarkedBitmap.width - watermarkBitmap.width) / 2f
                val y = (watermarkedBitmap.height - watermarkBitmap.height) / 2f

                canvas.drawBitmap(
                    watermarkBitmap,
                    x,
                    y,
                    null
                )
//                canvas.drawBitmap(
//                    watermarkBitmap,
//                    x,
//                    y,
//                    null
//                )
            } else {
                // Draw the watermark image on the bitmap
                val watermarkBitmap =
                    BitmapFactory.decodeResource(resources, watermarkResourceId2)
                // Calculate the position to center the watermark
                val x = (watermarkedBitmap.width - watermarkBitmap.width) / 2f
                val y = (watermarkedBitmap.height - watermarkBitmap.height) / 2f
//                val x = (watermarkedBitmap.width - watermarkBitmap.width) / 2.0f
//                val y = (watermarkedBitmap.height - watermarkBitmap.height) / 2.0f
                canvas.drawBitmap(
                    watermarkBitmap,
                    x,
                    y,
                    null
                )
                // Draw the original image


//                canvas.drawBitmap(
//                    watermarkBitmap,
//                    x,
//                    y,
//                    null
//                )
            }

            // Display the watermarked image
            runOnUiThread {
                imageview.setImageBitmap(watermarkedBitmap)
            }

            try {
                // Convert the watermarkedBitmap to bytes without recycling it
                val baos = ByteArrayOutputStream()
                watermarkedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                val compressedImageBytes = baos.toByteArray()
                // Check if compression was successful
                if (compressedImageBytes.isNotEmpty()) {
                    // Do something with the compressed image bytes
                    // For example, upload to a server or save to storage
                    // ...
                    // Assume compressedImageBytes contains the compressed image data
// Decode the byte array into a Bitmap
                    if (ImageView_INDENTIFIER == 1) {
                        Compressed_IMAGE_1 = compressedImageBytes
                    } else if (ImageView_INDENTIFIER == 2) {
                        Compressed_IMAGE_2 = compressedImageBytes
                    } else if (ImageView_INDENTIFIER == 3) {
                        Compressed_IMAGE_3 = compressedImageBytes
                    }


                    val bitmap = BitmapFactory.decodeByteArray(
                        compressedImageBytes,
                        0,
                        compressedImageBytes.size
                    )
// Assuming you have an ImageView with the id "imageView" in your layout


// Set the Bitmap on the ImageView
                    imageview.setImageBitmap(bitmap)

                    //this code for testing image on image view demo
//                    binding.imageViewk.visibility=View.VISIBLE
//                    binding.imageViewk.setImageBitmap(bitmap)


                    val sizeInBytes: Double = compressedImageBytes.size.toDouble()
                    val sizeInKb: Double = sizeInBytes.toDouble() / 1024.0


                } else {
                    // Handle compression failure
                    // ...
                    Toast.makeText(
                        this@Add_ads_screen,
                        "@ERROR failed to compress image!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Add_ads_screen, e.localizedMessage, Toast.LENGTH_SHORT).show()
            } finally {
                // Recycle the originalBitmap after using it
                originalBitmap.recycle()
            }
        } else {
            // Handle the case when the imageView or originalBitmap is null
            // Log an error or take appropriate action
            Toast.makeText(this@Add_ads_screen, "Original bitmap is null", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun compressBitmapToTargetSize(
        originalBitmap: Bitmap,
        targetSizeBytes: Int
    ): ByteArray {
        var quality = 100 // Initial quality setting
        val stream = ByteArrayOutputStream()
        originalBitmap.compress(Bitmap.CompressFormat.WEBP, quality, stream)

        // Adjust quality iteratively to achieve the target file size
        while (stream.size() > targetSizeBytes) {
            stream.reset()
            quality -= 5
            originalBitmap.compress(Bitmap.CompressFormat.WEBP, quality, stream)
        }

        Log.d("Compressed Image", "Quality: $quality, File Size: ${stream.size()} bytes")
        return stream.toByteArray()
    }


    override fun onImageClicked(position: Int, title: String, id: String) {
        Toast.makeText(this@Add_ads_screen, title.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun Check_IMAGE_UPLOADED_OR_NOT(): Boolean {     //function for checking user all images uploaded or not
        var imageUpload_STATUS: Boolean = false

        if (Compressed_IMAGE_1 == null || Compressed_IMAGE_2 == null || Compressed_IMAGE_3 == null) {
            imageUpload_STATUS = false
        } else {
            imageUpload_STATUS = true

        }

        return imageUpload_STATUS


    }

    private fun Check_CONDITION_SELECTED_OR_NOT(): Boolean {
        var radioUpload_STATUS: Boolean = false
        val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId

        if (selectedRadioButtonId != -1) {
            val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
            val selectedOption = selectedRadioButton.text.toString()
            // Do something with the selected option
            condition = selectedOption
            radioUpload_STATUS = true
        } else {
            radioUpload_STATUS = false
        }

        return radioUpload_STATUS


    }

    private fun Checking_contact_ready_OR_NOT(): Boolean {
        var radioUpload_STATUS: Boolean = false

        try {
            var contact_no: String = binding.coNumber.text.toString().trim()
            val selectedCountryCode: String =
                binding.countryCodePicker.selectedCountryCode.toString()

            // Check if the string is a valid integer


            if (contact_no.length < 9 || contact_no.length > 15) {
                radioUpload_STATUS = false
                Toast.makeText(
                    this@Add_ads_screen,
                    "@ERROR Please check your number Not correct!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (contact_no.length > 9 && contact_no.length < 16) {
                final_co_numner = selectedCountryCode + "" + contact_no
                radioUpload_STATUS = true
            } else {
                // Handle other cases if needed
            }

        } catch (e: Exception) {
            // handle exception
            radioUpload_STATUS = false
            Toast.makeText(
                this@Add_ads_screen,
                "Error: ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        return radioUpload_STATUS
    }

    private fun check_contactMethod_select_or_not(): Boolean {
        var radioUpload_STATUS: Boolean = false
        val selectedRadioButtonId = binding.contactOption.checkedRadioButtonId

        if (selectedRadioButtonId != -1) {
            val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
            val selectedOption = selectedRadioButton.text.toString()
            // Do something with the selected option
            contact_premmision = selectedOption
            radioUpload_STATUS = true
        } else {
            radioUpload_STATUS = false
        }

        return radioUpload_STATUS


    }

    //private fun user_full_data_checker(passcode:String):ArrayList<String>  {
//
//
//}
    fun process_DaaCOLECION() {
        try {
            val user_book_data = dataCollection()

            // Assuming binding is properly initialized and represents the UI elements
            //now we are going to apply condition for check that any value not null of their following

//            if (binding.cateText.text.toString().trim() != "") {
//                Array_Main_store_listing.add(binding.cateText.text.toString().trim())
//            } else {
//                Toast.makeText(
//                    this@Add_ads_screen,
//                    "Select categories",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            if (condition.toString().trim() != "") {
//                Array_Main_store_listing.add(condition.toString().trim())
//            } else {
//                Toast.makeText(
//                    this@Add_ads_screen,
//                    "condition not selected",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            if (binding.readdressOptional.text.toString().trim() != "") {
//                Array_Main_store_listing.add(binding.readdressOptional.text.toString().trim())
//            } else if (binding.readdressOptional.text.toString().trim() == "") {
//                try {
//                    //creating instance of sharedPreferenceManager for use saved user data fot Address
//                    val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
//                    //getting user data here
//                    val userDetails = sharedPreferenceManager.getUserDetails()
//                    //now you have access of your user data
//                    Array_Main_store_listing.add(userDetails.address.toString().trim())
//
//                } catch (e: Exception) {
////handle impossible error here again it's impossible
//
//
//                }
//            }
//            if (final_co_numner.toString().trim() != "") {
//                Array_Main_store_listing.add(final_co_numner.toString().trim())
//            } else {
//                Checking_contact_ready_OR_NOT()
//            }
//            if (contact_premmision.toString().trim() != "") {
//                Array_Main_store_listing.add(contact_premmision.toString().trim())
//            } else {
//                check_contactMethod_select_or_not()
//            }
//
            //here required users id
            //we are getting id from sha_preffrence
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
            val userDetails = sharedPreferenceManager.getUserDetails()
            val userid = userDetails.userID.toString().trim()
            //require title and book name I am arranging this to combine title and book name together
            val booktitle: String = user_book_data[0] + "" + user_book_data[2].toString()
            //book name
            val bookname: String = user_book_data[1].toString()
            //author name
            val authorname: String = user_book_data[3].toString()
            //description
            val description: String = user_book_data[11].toString()
            //for prise
            val prise: String = user_book_data[10].toString()
            //for publishing year
            val pYear: String = user_book_data[5].toString()
            //publisher of book
            val publisher: String = user_book_data[4].toString()
            //based on
            val basedOn: String = user_book_data[6].toString()
            //standard
            val standard: String = user_book_data[7].toString()
            //board preffrence
            val boardPreffrence: String = user_book_data[8].toString()
            //language medium
            val language: String = user_book_data[9].toString()
            //local address if present
            val localAdd = getUserAddress()
            //final phone number
            val phoneNumber: String = final_co_numner.toString()
            //contactMethods
            val contactMethod: String = contact_premmision.toString()
            //images
            val photo1: ByteArray = Compressed_IMAGE_1
            val photo2: ByteArray = Compressed_IMAGE_2
            val photo3: ByteArray = Compressed_IMAGE_3
            //condition of book
            val condition: String = condition.toString()
            //book category
            val bookCategory: String = binding.cateText.hint.toString()
            uploadBook(
                userid,
                booktitle,
                bookname,
                authorname,
                description,
                prise,
                pYear,
                publisher,
                basedOn,
                standard,
                boardPreffrence,
                language,
                localAdd,
                phoneNumber,
                contactMethod,
                photo1,
                photo2,
                photo3,
                condition,
                bookCategory
            )


        } catch (e: Exception) {
            dismissProgressDialog()
            Log.d("DATA_EXCEPTION_MAX", e.localizedMessage)
        }
    }

    override fun onImageLoadSuccess() {

    }

    override fun onImageLoadError() {

    }

    private fun uploadBook(
        userID: String,
        title: String,
        bookNAME: String,
        author: String,
        description: String,
        price: String,
        publicationYear: String,
        publisher: String,
        basedOn: String,
        standard: String,
        boardPreffrence: String,
        medium: String,
        localAddress: String,
        phoneNumber: String,
        contactPermission: String,
        photo1: ByteArray,
        photo2: ByteArray,
        photo3: ByteArray,
        bookCondition: String,
        category: String
    ) {
        try {
            //declaring var for pass whatsapp number, phone number enabled status
            var whatsapp_number: String = phoneNumber.toString()
            var phone_number: String = phoneNumber.toString()
            var whatsapp_enable_status: Int = 0
            var phone_enable_status: Int = 0
            //handling final request for uploading book
            if (contactPermission != "") {
                if (contactPermission.equals("whatsapp only")) {
                    whatsapp_enable_status = 1
                    var phone_enable_status: Int = 0
                } else if (contactPermission.equals("call only")) {
                    phone_enable_status = 1
                    whatsapp_enable_status = 0
                } else if (contactPermission.equals("both")) {
                    phone_enable_status = 1
                    whatsapp_enable_status = 1
                } else {
                    //handle impossible error here!
                }


            } else {
                //handle impossible error here!
            }


//        Toast.makeText(this@Add_ads_screen,userID+""+title+""+bookNAME+""+author+""+description+""+price+""+publicationYear+""+publisher+""+basedOn+""+
//                standard+""+boardPreffrence+""+medium+""+localAddress+""+phoneNumber+""+contactPermission+""+photo1+""+photo2+""+photo3+""+bookCondition+""+category,Toast.LENGTH_SHORT).show()

// Create a Retrofit instance
            val retrofit = Retrofit.Builder()
                .baseUrl(baseurl) // Replace with the actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // Create a RequestBody for the image
            val userService = retrofit.create(ApiService::class.java)

            // prefroming action for saving image by byteArray
            val requestBody1: RequestBody = photo1.toRequestBody("image/*".toMediaTypeOrNull())
            val p1: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo1", "image.jpeg", requestBody1)
            val requestBody2: RequestBody = photo2.toRequestBody("image/*".toMediaTypeOrNull())
            val p2: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo2", "image.jpeg", requestBody2)
            val requestBody3: RequestBody = photo3.toRequestBody("image/*".toMediaTypeOrNull())
            val p3: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo3", "image.jpeg", requestBody3)


            val titleBook = RequestBody.create("text/plain".toMediaTypeOrNull(), title.toString())
            val authorBook = RequestBody.create("text/plain".toMediaTypeOrNull(), author.toString())
            val descriptionBook =
                RequestBody.create("text/plain".toMediaTypeOrNull(), description.toString())
            val priceBook = RequestBody.create("text/plain".toMediaTypeOrNull(), price.toString())
            val bookcondition =
                RequestBody.create("text/plain".toMediaTypeOrNull(), bookCondition.toString())
            val userid =
                RequestBody.create("text/plain".toMediaTypeOrNull(), userID.toString().trim())
            val bookname = RequestBody.create("text/plain".toMediaTypeOrNull(), bookNAME.toString())
            val pubyear =
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    publicationYear.toString().trim()
                )
            val pub = RequestBody.create("text/plain".toMediaTypeOrNull(), publisher.toString())
            val basedon = RequestBody.create("text/plain".toMediaTypeOrNull(), basedOn.toString())
            val standardBook =
                RequestBody.create("text/plain".toMediaTypeOrNull(), standard.toString())
            val boardpreff =
                RequestBody.create("text/plain".toMediaTypeOrNull(), boardPreffrence.toString())
            val mediumBook = RequestBody.create("text/plain".toMediaTypeOrNull(), medium.toString())
            val localAdd =
                RequestBody.create("text/plain".toMediaTypeOrNull(), localAddress.toString())
            val whatsaap =
                RequestBody.create("text/plain".toMediaTypeOrNull(), whatsapp_number.toString())
            val enablew =
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    whatsapp_enable_status.toString()
                )
            val phonenum =
                RequestBody.create("text/plain".toMediaTypeOrNull(), phone_number.toString())
            val phoneenabled =
                RequestBody.create("text/plain".toMediaTypeOrNull(), phone_enable_status.toString())
            val categoryyyy = RequestBody.create("text/plain".toMediaTypeOrNull(), category)


            //before calling api I am checking internet connected or not
            // Create an instance of InternetConnectionChecker
            val internetConnectionChecker = InternetConnectionChecker(this)

// Check if the internet is available
            if (internetConnectionChecker.isInternetAvailable()) {
                //now here internet connected
                // Make the API request
                val call: Call<Book_upload_response> = userService.uploadBook_ApiClient(
                    titleBook,
                    authorBook,
                    descriptionBook,
                    priceBook,
                    bookcondition,
                    userid,
                    bookname,
                    pubyear,
                    pub,
                    basedon,
                    standardBook,
                    boardpreff,
                    mediumBook,
                    localAdd,
                    whatsaap,
                    enablew,
                    phonenum,
                    phoneenabled,
                    categoryyyy,
                    p1,
                    p2,
                    p3
                )

                call.enqueue(object : retrofit2.Callback<Book_upload_response> {
                    override fun onResponse(
                        call: Call<Book_upload_response>,
                        response: Response<Book_upload_response>
                    ) {
                        if (response.isSuccessful) {
                            val userResponse: Book_upload_response? = response.body()

                            if (userResponse != null) {

                                if (userResponse.error) {
                                    Toast.makeText(
                                        this@Add_ads_screen,
                                        "book uploaded" + userResponse.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dismissProgressDialog()
                                } else if (!userResponse.error) {
                                    //if book uploaded successfully
                                    showsnackbar(photo1, title, price, localAddress, "1")
                                    uploadedBookId = userResponse.book_id.toString()
                                    dismissProgressDialog()
                                }

                            }

                        } else {
                            Toast.makeText(
                                this@Add_ads_screen,
                                "Api response error",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            dismissProgressDialog()
                        }
                    }

                    override fun onFailure(call: Call<Book_upload_response>, t: Throwable) {

                        // It's logic to implement if api failed to perform action
                        /**
                         *  I am giving three time permit It will call ownSelf for three times automatically
                         *  without permission if task failed with connecting issue or any server issue
                         *  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
                        Toast.makeText(
                            this@Add_ads_screen,
                            t.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        dismissProgressDialog()
                    }


                })
            } else {
                Toast.makeText(this, "Make sure connected to network?", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        } catch (e: Exception) {
            dismissProgressDialog()
            Toast.makeText(this@Add_ads_screen, e.localizedMessage.toString(), Toast.LENGTH_SHORT)
                .show()
            dismissProgressDialog()
            //handel if error during processing upload book method
        }


    }

    fun getUserAddress(): String {
        var finalAddress: String = ""
        if (binding.readdressOptional.text.toString().trim().equals("")) {
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
            val userDetails = sharedPreferenceManager.getUserDetails()
            finalAddress = userDetails.address.toString().trim()


        } else if (binding.readdressOptional.text.toString().trim() != "") {
            finalAddress = binding.readdressOptional.text.toString().trim()
        } else {
            //handle impossible error here
        }


        return finalAddress

    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(this@Add_ads_screen)
        //progressDialog?.setTitle("changing password...")
        progressDialog?.setMessage("Uploading your book...") // Set a message
        progressDialog?.setCancelable(false) // Make it non-cancelable
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    fun showsnackbar(
        imagebyes: ByteArray,
        title: String,
        prise: String,
        localAddress: String,
        bookId: String
    ) {
        try {
            runOnUiThread {
                // Load the animation from XML
                val animation = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_to_top)
//set data on snack bar
                // Assume you have a ByteArray representing an image
                val imageByteArray: ByteArray = imagebyes
                // Convert the ByteArray to a Bitmap
                val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                // Set the Bitmap as the image source for the ImageView
                binding.bookimage.setImageBitmap(bitmap)
//setting other data on views
                binding.snackbooktitle.setText(title + "\n" + "...")
                binding.snackprise.setText(prise)
                binding.snackaddres.setText(localAddress)
//handling other UI
                //  showToastMAssage(this@Add_ads_screen, "all done preform further sction")
                switchViewsWithAnimation(binding.scrollView6, binding.scroolviewbasic)

                binding.backbutton.setImageResource(R.drawable.baseline_clear_24)
                binding.firstLayoutBootonLayout.visibility = View.GONE
                binding.seclayout.visibility = View.GONE
                //setting edittext null
                binding.bookmaintitle.setText("")
                binding.bookfullname.setText("")
                binding.booksubtitle.setText("")
                binding.authername.setText("")
                binding.publisher.setText("")
                binding.publishingyear.setText("")
                binding.basedon.setText("")
                binding.standard.setText("")
                binding.anyboardpreffrence.setText("")
                binding.booklanguage.setText("")
                binding.anskingprise.setText("")
                binding.descriptionforusers.setText("")
                back_STATUS = 0
                // Start the animation
                binding.mainsanckbar.visibility = View.VISIBLE
                binding.sbackbarklayout.visibility = View.VISIBLE
                binding.mainsanckbar.startAnimation(animation)


            }
        } catch (e: Exception) {
            //handle exception
        }

    }

    fun dismisssnackbar() {
        runOnUiThread {
            // Load the animation from XML
            val animation = AnimationUtils.loadAnimation(this, R.anim.slidedown)

            // Start the animation

            binding.mainsanckbar.startAnimation(animation)
            handler.postDelayed({
                // This code will run after a delay of 2000 milliseconds (2 seconds)
                // Perform your actions here
                //here are papering environment for user that can user user again upload new book
                binding.sbackbarklayout.visibility = View.GONE
                binding.seclayout.visibility = View.VISIBLE
                binding.bookmaintitle.requestFocus()
            }, 200)


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun repraperingLayout() {
        //first we clear main book detail layout, ?????HERE IS WORK I ALREADY DONE>
        //now i should prapare second main layout
        // first i am refresing ui
        //here i refreshing category selection
        binding.cateText.setText("")
        //refreshing book imageview
        binding.frontImage.setImageDrawable(null)
        binding.backImage.setImageDrawable(null)
        binding.anysideimage.setImageDrawable(null)
//refreshing redio group selection
        contact_premmision = ""
        back_STATUS = 0


    }

    override fun onItemClicked(position: Int) {
      //...............
    }
}