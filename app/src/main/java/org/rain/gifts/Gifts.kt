package org.rain.gifts

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.onesignal.OneSignal
import java.io.IOException
import java.lang.Error

var sub1:String? = null
var sub2:String? = null
var sub3:String? = null
var sub4:String? = null
var sub5:String? = null
var adset:String? = null
var af_status:String? = null
var appsflyer_id:String? = null
var idfa:String? = null
var appsflyer_key= "5MSfQ44h7QRoGMixZ54zL9"
var deepLink :String? = null


class Gifts:Application() {
    override fun onCreate() {
        super.onCreate()
        init()

        MobileAds.initialize(this) {}
        Thread{
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(this)
                idfa = info.id
                Log.e("idfa", "$idfa")
            } catch (exception: IOException) {
            } catch (exception: GooglePlayServicesRepairableException) {
            } catch (exception: GooglePlayServicesNotAvailableException) {
            }
        }.start()

        appsflyer_id = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        Log.e("appsflyer_id", "$appsflyer_id")

        val conversionListener  = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                p0?.let { cvData ->
                    cvData.map {
                        Log.e("APS", "conversion_attribute:  ${it.key} = ${it.value}")
                    }

                    af_status = cvData.getValue("af_status").toString()
                    if (af_status == "Non-organic"){
                        val campaign = cvData.getValue("campaign").toString().split("_")
                        Log.e("campaign", "$campaign")
                        try { sub1 = campaign[0]} catch (e: Error){ sub1 = ""}
                        try { sub2 = campaign[1]} catch (e: Error){ sub2 = ""}
                        try { sub3 = campaign[2]} catch (e: Error){ sub3 = ""}
                        try { sub4 = campaign[3]} catch (e: Error){ sub4 = ""}
                        try { sub5 = campaign[4]} catch (e: Error){ sub5 = ""}

                        adset = cvData.getValue("adset").toString()
                    }
                }
            }
            override fun onConversionDataFail(p0: String?) {}
            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
            override fun onAttributionFailure(p0: String?) {}
        }

        AppsFlyerLib.getInstance().run {
            init(appsflyer_key, conversionListener, this@Gifts)
            start(this@Gifts)
        }

    }
    private fun init(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("bc35836c-495f-427e-8456-95cd4f7b117e")

        FacebookSdk.fullyInitialize()
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        FacebookSdk.setAutoLogAppEventsEnabled(true)
        AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP)

        AppLinkData.fetchDeferredAppLinkData(this) {
            if (it != null && it.targetUri != null) {
                deepLink = "&" + it.targetUri.toString().split("\\?".toRegex()).toTypedArray()[1]
            }else{
                Log.e("AppLinkData", "$deepLink")
            }
        }

    }
}