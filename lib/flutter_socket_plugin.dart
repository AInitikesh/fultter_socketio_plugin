import 'dart:async';

import 'package:flutter/services.dart';

class IO {
  static const MethodChannel _channel = const MethodChannel('flutter_socket_plugin/method');
  static const  EventChannel _eventChannel = const EventChannel('flutter_socket_plugin/event');
  static Future<String> socket(url) async {
    final String socket = await _channel.invokeMethod('socket',<String, dynamic>{'url': url});
    return socket;
  }

  static Future<String> emit(message) async {
    final String success = await _channel.invokeMethod('emit',<String, dynamic>{'message': message});
    return success;
  }


  static Future<Null> connect() async {
    final String socket = await _channel.invokeMethod('connect');
  }

  static Stream<String> get on {

    return _eventChannel.receiveBroadcastStream().map((dynamic event) => _parseString(event));
  }

  static String _parseString(String state) {
    return "yuhuu";
  }
}
