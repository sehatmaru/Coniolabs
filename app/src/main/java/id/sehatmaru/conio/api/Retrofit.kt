package id.sehatmaru.conio.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {

    companion object{
        fun build() : Retrofit {
            val builder = Retrofit.Builder()
            builder.baseUrl("https://api.snaptig.com/")
            builder.addConverterFactory(GsonConverterFactory.create())

            return builder.build()
        }
    }
}