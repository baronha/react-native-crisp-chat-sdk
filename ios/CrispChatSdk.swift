import Crisp

@objc(CrispChatSdk)
class CrispChatSdk: NSObject {
    var sessionCallback: Crisp.CallbackToken?

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

    @objc
    func show(_ callback: RCTResponseSenderBlock?) {
        DispatchQueue.main.async {
            var viewController = RCTPresentedViewController()

            if viewController == nil {
                viewController = UIApplication.shared.windows.first?.rootViewController
            }

            let chatViewController = ChatViewController()

            viewController?.present(chatViewController, animated: true)
        }

        if let sessionCallback = sessionCallback {
            CrispSDK.removeCallback(token: sessionCallback)
        }

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

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
