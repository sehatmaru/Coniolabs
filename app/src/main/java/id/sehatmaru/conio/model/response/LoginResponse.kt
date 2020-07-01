package id.sehatmaru.conio.model.response

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("token_type")
    var tokenType = ""
    @SerializedName("access_token")
    var accessToken = ""
}
