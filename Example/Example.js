import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

import DeltaDNA from 'react-native-deltadna';

import credientials from './credentials.json';

const Example = () => {
  const onStartSDK = () => DeltaDNA.start(credientials);
  const onStopSDK = () => DeltaDNA.stop();
  const onNewSession = () => DeltaDNA.newSession();
  const onUpload = () => DeltaDNA.upload();
  const onClearPersistantData = () => DeltaDNA.onClearPersistantData();
  const onSimpleEvent = () => {
    DeltaDNA.recordEvent({ name: 'missionCompleted' });
  };
  const onComplexEvent = () => {
    DeltaDNA.recordEvent({
      name: 'missionCompleted',
      params: {
        mission: 10,
        bonus: 200,
      },
    });
  };
  const onRecordPushNotification = () => {
    DeltaDNA.recordPushNotification({
      name: 'Hello push',
    }, false);
  };
  const onEngage = () => {
    DeltaDNA.engage({
      name: 'engage',
    }, result => {
      alert(result);
    });
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={onStartSDK}>
        <Text style={styles.text}>Start SDK</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onStopSDK}>
        <Text style={styles.text}>Stop SDK</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onNewSession}>
        <Text style={styles.text}>New session</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onClearPersistantData}>
        <Text style={styles.text}>Clear persistant data</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onUpload}>
        <Text style={styles.text}>Upload</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onSimpleEvent}>
        <Text style={styles.text}>Simple Event</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onComplexEvent}>
        <Text style={styles.text}>Complex Event</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onRecordPushNotification}>
        <Text style={styles.text}>Record push notification</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={onEngage}>
        <Text style={styles.text}>Engage</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  text: {
    textAlign: 'center',
    color: '#333333',
    marginTop: 8,
    marginBottom: 8,
    fontSize: 14,
  },
});

export default Example;
