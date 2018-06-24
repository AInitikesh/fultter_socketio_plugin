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

/** FlutterSocketPlugin */
public class FlutterSocketPlugin implements MethodCallHandler , EventChannel.StreamHandler{


  private Socket mSocket;
  private Emitter.Listener onNewMessage;


  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_socket_plugin/method");
    final EventChannel socketEvents = new EventChannel(registrar.messenger(), "flutter_socket_plugin/event");
    socketEvents.setStreamHandler(new FlutterSocketPlugin());
    channel.setMethodCallHandler(new FlutterSocketPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("socket")) {
      {
        try {
          String url = call.argument("url");
          mSocket = IO.socket(url);
          Log.e("Nitikesh ","Socket initialised");
        } catch (URISyntaxException e) {
          Log.e("Nitikesh ",e.toString());
        }
      }
      result.success("created");


    } else if (call.method.equals("connect")){
      mSocket.connect();
      result.success("connected");
    } else if (call.method.equals("emit")){
      String messsge = call.argument("message");
      Log.e("Nitikesh  ","emiting message");
      mSocket.emit("message",messsge);
      result.success("sent");
    }
    else {
      result.notImplemented();
      Log.e("Nitikesh ","Not Implemented");
    }
  }

  @Override
  public void onListen(Object o, EventChannel.EventSink eventSink) {
    onNewMessage = createEmmiterListener(eventSink);
    mSocket.on("chat message", onNewMessage);
  }

  Emitter.Listener createEmmiterListener(EventChannel.EventSink event){
    final EventChannel.EventSink myEvent = event;
    return new Emitter.Listener() {
      @Override
      public void call(final Object... args) {
        Object data =  args[0];

        // add the message to view
        myEvent.success(data);

      }
    };

  }

  @Override
  public void onCancel(Object o) {

  }
}
