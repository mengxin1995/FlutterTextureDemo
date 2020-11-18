package com.plugin.flutter_texture_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.TextureRegistry;

/** FlutterTextureDemoPlugin */
public class FlutterTextureDemoPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private TextureRegistry textureRegistry;
  private Context mContext;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_texture_demo");
    channel.setMethodCallHandler(this);
    textureRegistry = flutterPluginBinding.getTextureRegistry();
    mContext = flutterPluginBinding.getApplicationContext();
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_texture_demo");
    channel.setMethodCallHandler(new FlutterTextureDemoPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getImageTexture")) {
      TextureRegistry.SurfaceTextureEntry surfaceTextureEntry = textureRegistry.createSurfaceTexture();
      long textureId = surfaceTextureEntry.id();
      Map<String, Object> reply = new HashMap<>();
      reply.put("textureId", textureId);
      surfaceTextureEntry.surfaceTexture();


      Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.meinv);
      int imageWidth = bitmap.getWidth();
      int imageHeight = bitmap.getHeight();
      Rect rect = new Rect(0, 0, 200, 200);
      surfaceTextureEntry.surfaceTexture().setDefaultBufferSize(imageWidth, imageHeight);
      Surface surface = new Surface(surfaceTextureEntry.surfaceTexture());
      Canvas canvas = surface.lockCanvas(rect);
      canvas.drawBitmap(bitmap, null, rect, null);
      bitmap.recycle();
      surface.unlockCanvasAndPost(canvas);
      surface.release();

      result.success(reply);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    textureRegistry =  null;
    mContext = null;
  }
}
