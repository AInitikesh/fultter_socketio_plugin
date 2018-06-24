package com.falio.fluttersocketplugin;

import android.util.Log;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/** FlutterSocketPlugin */
public class FlutterSocketPlugin implements MethodCallHandler {


  private Socket mSocket;
  private MethodChannel channel;

  public FlutterSocketPlugin(MethodChannel channel) {
    this.channel = channel;
  }


  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_socket_plugin/method");
    channel.setMethodCallHandler(new FlutterSocketPlugin(channel));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("socket")) {
      {
        try {
          String url = call.argument("url");
          mSocket = IO.socket(url);
          Log.d("SocketIO ","Socket initialised");
        } catch (URISyntaxException e) {
          Log.e("SocketIO ",e.toString());
        }
      }
      result.success("created");


    } else if (call.method.equals("connect")){
      mSocket.connect();
      Log.d("SocketIO  ","Connected");
      result.success("connected");
    } else if (call.method.equals("emit")){
      String message = call.argument("message");
      String topic = call.argument("topic");
      Log.d("SocketIO  ","Pushing " +  message + " on topic " + topic);
      mSocket.emit(topic,message);
      result.success("sent");
    } else if (call.method.equals("on")){
      String topic = call.argument("topic");
      Log.d("SocketIO  ","registering to "+ topic + " topic");
      mSocket.on(topic, onNewMessage);
      result.success("sent");
    }
    else {
      result.notImplemented();
      Log.d("SocketIO ","Not Implemented");
    }
  }

  private Emitter.Listener onNewMessage = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      String data = (String)args[0];
      Log.d("SocketIO ", "Received " + data);
      Map<String, String> myMap= new HashMap<String, String>();
      myMap.put("message", data);
      channel.invokeMethod("received", myMap);
    }
  };

}
