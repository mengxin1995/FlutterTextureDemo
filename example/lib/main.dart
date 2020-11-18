import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_texture_demo/flutter_texture_demo.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

const int INVALIDITY = -1;

class _MyAppState extends State<MyApp> {
  int _textureId = INVALIDITY;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      var map = await FlutterTextureDemo.imageTexture;
      _textureId = map["textureId"];
    } on PlatformException {
      _textureId = INVALIDITY;
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Texture example app'),
        ),
        body: Center(
          child: Container(
            width: 200,
           height: 200,
           child: Texture(textureId: _textureId),
          ),
        ),
      ),
    );
  }
}
