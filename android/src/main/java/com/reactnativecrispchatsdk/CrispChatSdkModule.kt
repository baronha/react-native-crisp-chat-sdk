package com.reactnativecrispchatsdk

import android.app.Activity
import android.content.Intent
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import im.crisp.client.ChatActivity
import im.crisp.client.Crisp
import im.crisp.client.data.SessionEvent
import im.crisp.client.data.SessionEvent.Color
import android.os.Handler
import android.os.Looper
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter


class CrispChatSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "CrispChatSdk"
  }

  private var sessionId: String = ""
  private lateinit var sessionHandler: Handler
  private val CRISP_CHAT_CLOSED = 1


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
    callback(this.sessionId)
  }

  @ReactMethod
  fun show(callback: Callback) {
    val activity = currentActivity ?: return
    val context = reactApplicationContext
    val crispIntent = Intent(context, ChatActivity::class.java)
    context.addActivityEventListener(mActivityEventListener)

    if (callback != null) {
      sessionHandler = startSessionInterval(context) { session ->
        this.sessionId = sessionId
        callback(session)
      }
    }

    activity.startActivityForResult(crispIntent, CRISP_CHAT_CLOSED)
  }


  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(
      activity: Activity,
      requestCode: Int,
      resultCode: Int,
      intent: Intent?
    ) {
      if (requestCode == CRISP_CHAT_CLOSED) {
        reactApplicationContext
          .getJSModule(RCTDeviceEventEmitter::class.java)
          .emit(CrispChatEvent.CrispChatClosed.toString(), null)

      }
    }
  }

//  context.addActivityEventListener(mActivityEventListener)

}


fun startSessionInterval(context: ReactApplicationContext, callback: (String) -> Unit): Handler {
  val interval = 1000L // 1s
  val timeout = 10000L // 10s
  val handler = Handler(Looper.getMainLooper())
  var elapsedTime = 0L
  var isCallbackInvoked = false

  val runnable = object : Runnable {
    override fun run() {
      val session = Crisp.getSessionIdentifier(context)

      if (session != null && !isCallbackInvoked) {
        // Có sessionId và chưa gọi callback trước đó
        callback(session.toString())
        isCallbackInvoked = true
        cancelHandler(handler)
      }

      if (elapsedTime < timeout && !isCallbackInvoked) {
        elapsedTime += interval
        handler.postDelayed(this, interval)
      } else {
        cancelHandler(handler)
      }
    }
  }

  // Bắt đầu interval
  handler.postDelayed(runnable, interval)

  return handler
}

fun cancelHandler(handler: Handler) {
  handler.removeCallbacksAndMessages(null)
}

enum class CrispChatEvent {
  CrispChatClosed
}
