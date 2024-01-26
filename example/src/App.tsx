import * as React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import {
  CrispSessionEventColors,
  pushSessionEvent,
  resetSession,
  setSessionSegment,
  setSessionString,
  setSessionBool,
  setSessionInt,
  setTokenId,
  setUserAvatar,
  setUserEmail,
  setUserNickname,
  setUserPhone,
  getSessionId,
  show,
  addListener,
  CrispChatEvent,
} from 'react-native-crisp-chat-sdk';

export default function App() {
  const onShowChat = () => {
    show(console.log, () => {
      console.log('closed');
    });
  };

  const onGetSessionId = () => {
    getSessionId(console.log);
  };

  React.useEffect(() => {
    const eventClosed = addListener(CrispChatEvent.CrispChatClosed, () => {
      console.log('closed');
    });

    return () => {
      eventClosed.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={onGetSessionId}>
        <Text>Get Session Id</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onShowChat}>
        <Text>Show Chat</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setTokenId('222')}>
        <Text>Set Token Id</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setUserEmail('test@test.com')}>
        <Text>Set User Email</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => setSessionString('stringkey', 'string value')}
      >
        <Text>Set Session String</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setSessionBool('boolkey', true)}>
        <Text>Set Session Boolean</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setSessionInt('intkey', 10)}>
        <Text>Set Session Int</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setUserNickname('John Smith')}>
        <Text>Set User Nickname</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setUserPhone('+4412345678890')}>
        <Text>Set User Phone</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() =>
          setUserAvatar(
            'https://s.gravatar.com/avatar/5bc4980ee481a05395a6d9cb3d61379c?s=80'
          )
        }
      >
        <Text>Set User Avatar</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setSessionSegment('app')}>
        <Text>Set Session Segment</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() =>
          pushSessionEvent('Sign Up', CrispSessionEventColors.BLUE)
        }
      >
        <Text>Push Session Segment</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => resetSession()}>
        <Text>Reset Session</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
