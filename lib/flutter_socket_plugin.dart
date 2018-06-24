import 'dart:async';

import 'package:flutter/services.dart';

class IO {

  MethodChannel _channel = const MethodChannel('flutter_socket_plugin/method');

  Future<String> socket(url) async {
    final String socket = await _channel.invokeMethod('socket',<String, dynamic>{'url': url});
    return socket;
  }

  Future<String> emit(topic, message) async {
    final String success = await _channel.invokeMethod('emit',<String, dynamic>{'message': message, 'topic': topic});
    return success;
  }

  Future<Null> connect() async {
    final String socket = await _channel.invokeMethod('connect');
  }


  Future<String> on(String topic, Function _handle) async {
    final String socket = await _channel.invokeMethod('on', <String, dynamic>{'topic': topic});
    _channel.setMethodCallHandler((call) {
      if (call.method == 'received') {
        final String received = call.arguments['message'];
        Function.apply(_handle, [received]);
      }
    });
  }


}
