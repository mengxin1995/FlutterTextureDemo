
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterTextureDemo {
  static const MethodChannel _channel =
      const MethodChannel('flutter_texture_demo');

  static Future<Map> get imageTexture async {
    final Map map = await _channel.invokeMethod('getImageTexture');
    return map;
  }
}
