package com.reactnativecrispchatsdk

import android.content.Intent
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import im.crisp.client.ChatActivity
import im.crisp.client.Crisp
import im.crisp.client.data.SessionEvent
import im.crisp.client.data.SessionEvent.Color
import android.os.Handler
import android.os.Looper


class CrispChatSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "CrispChatSdk"
  }

  private var sessionId: String = ""

  @ReactMethod
  fun setTokenId(id: String) {
    val context = reactApplicationContext
    Crisp.setTokenID(context, id)
  }

  @ReactMethod
  fun setUserEmail(email: String) {
    Crisp.setUserEmail(email)
  }

  @ReactMethod
  fun setUserNickname(name: String) {
    Crisp.setUserNickname(name)
  }

  @ReactMethod
  fun setUserPhone(phone: String) {
    Crisp.setUserPhone(phone)
  }

  @ReactMethod
  fun setUserAvatar(url: String) {
    Crisp.setUserAvatar(url)
  }

  @ReactMethod
  fun setSessionSegment(segment: String) {
    Crisp.setSessionSegment(segment)
  }

  @ReactMethod
  fun setSessionString(key: String, value: String) {
    Crisp.setSessionString(key, value)
  }

  @ReactMethod
  fun setSessionBool(key: String, value: Boolean) {
    Crisp.setSessionBool(key, value)
  }

  @ReactMethod
  fun setSessionInt(key: String, value: Int) {
    Crisp.setSessionInt(key, value)
  }

  @ReactMethod
  fun pushSessionEvent(name: String, color: Int) {
    var sessionEventColor: Color = Color.BLACK

    when (color) {
      0 -> sessionEventColor = Color.RED
      1 -> sessionEventColor = Color.ORANGE
      2 -> sessionEventColor = Color.YELLOW
      3 -> sessionEventColor = Color.GREEN
      4 -> sessionEventColor = Color.BLUE
      5 -> sessionEventColor = Color.PURPLE
      6 -> sessionEventColor = Color.PINK
      7 -> sessionEventColor = Color.BROWN
      8 -> sessionEventColor = Color.GREY
      9 -> sessionEventColor = Color.BLACK
    }

    Crisp.pushSessionEvent(
      SessionEvent(
        name,
        sessionEventColor
      )
    )
  }

  @ReactMethod
  fun resetSession() {
    val context = reactApplicationContext
    this.sessionId = ""
    Crisp.resetChatSession(context)
  }

  @ReactMethod
  fun getSessionId(callback: Callback) {
    val context = reactApplicationContext
//    callback(Crisp.getSessionIdentifier(context))
    callback(this.sessionId)


//    println("Crisp.getSessionIdentifier(context) ${Crisp.getSessionIdentifier(context)}")
  }

  @ReactMethod
  fun show(callback: Callback) {
    val context = reactApplicationContext



    Handler(Looper.getMainLooper()).postDelayed({
//      println("Crisp.getSessionIdentifier(context) ${}")
      this.sessionId = Crisp.getSessionIdentifier(context).toString()

    }, 3000)

//    callback(Crisp.getSessionIdentifier(context))
    val crispIntent = Intent(context, ChatActivity::class.java)
    crispIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


    context.startActivity(crispIntent)
  }

//  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
//    override fun onActivityResult(
//      activity: Activity,
//      requestCode: Int,
//      resultCode: Int,
//      intent: Intent?
//    ) {
//
//
//      println("requestCode:  ${requestCode}")
//    }
//  }
}
