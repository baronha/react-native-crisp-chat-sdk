import * as React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import CrispChat, {
  CrispSessionEventColors,
  pushSessionEvent,
  resetSession,
  setSessionSegment,
  setTokenId,
  setUserAvatar,
  setUserEmail,
  setUserNickname,
  setUserPhone,
} from 'react-native-crisp-chat-sdk';

export default function App() {
  const [showChat, setShowChat] = React.useState<boolean>(false);

  const onShowChat = () => {
    setShowChat(!showChat);
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={onShowChat}>
        <Text>{showChat ? 'Hide' : 'Show'} Chat</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setTokenId('123456789')}>
        <Text>Set Token Id</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setUserEmail('test@test.com')}>
        <Text>Set User Email</Text>
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
      {showChat && <CrispChat />}
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
