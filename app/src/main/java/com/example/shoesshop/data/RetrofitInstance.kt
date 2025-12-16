import com.example.shoesshop.data.service.UserManagementService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy

object RetrofitInstance {
    const val SUBABASE_URL = "https://ktdjnsutcinzokphqgjv.supabase.co"

    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.207.106.71", 3128))
    val client = OkHttpClient.Builder().proxy(proxy).build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SUBABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userManagementService = retrofit.create(UserManagementService::class.java)
}