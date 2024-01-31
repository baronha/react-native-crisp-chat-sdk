import Crisp
import Foundation

@objc(CrispChatSdk)
class CrispChatSdk: RCTEventEmitter {
    var sessionCallback: Crisp.CallbackToken?
    var closeCallback: Crisp.CallbackToken?

    override func supportedEvents() -> [String]! {
        return [CrispChatEvent.CrispChatClosed.rawValue]
    }

    @objc
    func setTokenId(_ id: String) {
        CrispSDK.setTokenID(tokenID: id)
    }

    @objc
    func setUserEmail(_ email: String) {
        CrispSDK.user.email = email
    }

    @objc
    func setUserNickname(_ nickname: String) {
        CrispSDK.user.nickname = nickname
    }

    @objc
    func setUserPhone(_ phone: String) {
        CrispSDK.user.phone = phone
    }

    @objc
    func setUserAvatar(_ url: String) {
        CrispSDK.user.avatar = URL(string: url)
    }

    @objc
    func setSessionSegment(_ segment: String) {
        CrispSDK.session.segment = segment
    }

    @objc
    func setSessionString(_ key: String, value: String) {
        CrispSDK.session.setString(value, forKey: key)
    }

    @objc
    func setSessionBool(_ key: String, value: Bool) {
        CrispSDK.session.setString(String(value), forKey: key)
    }

    @objc
    func setSessionInt(_ key: String, value: Int) {
        CrispSDK.session.setInt(value, forKey: key)
    }

    @objc
    func pushSessionEvent(_ eventName: String, color: Crisp.SessionEventColor) {
        CrispSDK.session.pushEvent(Crisp.SessionEvent(name: eventName, color: color))
    }

    @objc
    func resetSession() {
        CrispSDK.session.reset()
    }

//    func dispatchEvent(_ eventName: CrispChatEvent) {
    ////        bridge.eventDispatcher.sendAppEventWithName(eventName, body: "Woot!")
//    }

    @objc
    func show(_ callback: RCTResponseSenderBlock?) {
        DispatchQueue.main.async {
            var viewController = RCTPresentedViewController()

            if viewController == nil {
                viewController = UIApplication.shared.windows.first?.rootViewController
            }

            if let viewController {
                let chatViewController = ChatViewController()

                chatViewController.modalTransitionStyle = .coverVertical
                chatViewController.modalPresentationStyle = .fullScreen
                viewController.present(chatViewController, animated: true)
            }
        }

        if let sessionCallback = sessionCallback {
            CrispSDK.removeCallback(token: sessionCallback)
        }

        if let closeCallback = closeCallback {
            CrispSDK.removeCallback(token: closeCallback)
        }

        closeCallback = CrispSDK.addCallback(.chatClosed {
            self.sendEvent(withName: CrispChatEvent.CrispChatClosed.rawValue, body: nil)
        })

        if let callback = callback {
            sessionCallback = CrispSDK.addCallback(Callback.sessionLoaded { sessionId in
                callback([sessionId])
            })
        }
    }

    @objc
    func getSessionId(_ callback: RCTResponseSenderBlock) {
        let id = CrispSDK.session.identifier ?? ""
        callback([id])
    }

//    @objc
//    static func requiresMainQueueSetup() -> Bool {
//        return true
//    }

    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
}

enum CrispChatEvent: String {
    case CrispChatClosed
}
