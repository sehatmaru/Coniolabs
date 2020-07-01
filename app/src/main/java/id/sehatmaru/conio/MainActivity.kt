package id.sehatmaru.conio

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.sehatmaru.conio.api.Retrofit
import id.sehatmaru.conio.api.Service
import id.sehatmaru.conio.model.request.LoginRequest
import id.sehatmaru.conio.model.response.LoginResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog
    private lateinit var service : Service
    private var request = LoginRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
        initListener()
    }

    private fun setup(){
        Picasso
            .get()
            .load("https://avatars0.githubusercontent.com/u/37797241?s=460&v=4")
            .into(profile_image)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        progressDialog = Dialog(this)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setContentView(R.layout.dialog_loading)

        service = Retrofit.build().create(Service::class.java)
    }

    private fun initListener(){
        emailInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                request.email = emailInput.text.toString()
                isValidBtn()
            }
        })

        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                request.password = passwordInput.text.toString()
                isValidBtn()
            }
        })

        emailInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) emailInput.setBackgroundResource(R.drawable.bg_form_active)
            else emailInput.setBackgroundResource(R.drawable.bg_form_inactive)
        }

        passwordInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) passwordInput.setBackgroundResource(R.drawable.bg_form_active)
            else passwordInput.setBackgroundResource(R.drawable.bg_form_inactive)
        }

        signInBtn.setOnClickListener { login() }
    }

    private fun login(){
        val callAsync: Call<LoginResponse> = service.login(request)
        progressDialog.show()

        callAsync.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        progressDialog.dismiss()
                        toHome()
                    }

                } else {
                    progressDialog.dismiss()
                    showMessage(response.message())
                }
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                progressDialog.dismiss()
            }
        })
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toHome(){
        finish()
        startActivity(HomeActivity.newIntent(this))
    }


    private fun isValidBtn(){
        if (request.isValid()){
            if (isValidEmail(emailInput.text.toString())) {
                signInBtn.isEnabled = true
                signInBtn.setBackgroundResource(R.drawable.bg_btn_active)
            }
        } else {
            signInBtn.isEnabled = false
            signInBtn.setBackgroundResource(R.drawable.bg_btn_inactive)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}
