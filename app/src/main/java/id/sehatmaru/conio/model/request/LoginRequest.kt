package id.sehatmaru.conio.model.request

import com.bluelinelabs.logansquare.annotation.JsonField

class LoginRequest() {

    @JsonField(name = ["email"])
    var email = ""
    @JsonField(name = ["password"])
    var password = ""

    fun isValid(): Boolean{
        return (email.isNotEmpty()
                && password.isNotEmpty())
    }
}