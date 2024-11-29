package com.reactnativecrispchatsdk

import android.content.Intent
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import im.crisp.client.external.ChatActivity
import im.crisp.client.external.Crisp
import im.crisp.client.external.data.SessionEvent
import im.crisp.client.external.data.SessionEvent.Color
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import im.crisp.client.external.EventsCallback
import im.crisp.client.external.data.message.Message


class CrispChatSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "CrispChatSdk"
  }

  private var sessionId: String = ""
  private var crispCallback: EventsCallback? = null


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
    val context = reactApplicationContext
    val crispIntent = Intent(context, ChatActivity::class.java)
    crispIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    crispCallback = object : EventsCallback {
      override fun onSessionLoaded(sessionId: String) {
        callback(sessionId)
      }

      override fun onChatOpened() {

      }

      override fun onChatClosed() {
        reactApplicationContext
          .getJSModule(RCTDeviceEventEmitter::class.java)
          .emit(CrispChatEvent.CrispChatClosed.toString(), null)
        crispCallback?.let {
          Crisp.removeCallback(it)
          crispCallback = null
        }
      }

      override fun onMessageSent(p0: Message) {
        //
      }

      override fun onMessageReceived(p0: Message) {
        //
      }
    }

    crispCallback?.let { Crisp.addCallback(it) }

    // start activity
    context.startActivity(crispIntent)
  }
}

enum class CrispChatEvent {
  CrispChatClosed
}
