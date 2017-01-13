# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\waaa\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-optimizationpasses 2
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-ignorewarnings
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
##不混淆需要根据manifest来识别的类
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
#
## 保持 native 方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
## 保持自定义控件类不被混淆
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers class * extends android.app.Activity {
#    public void *(android.view.View);
#}
#
## 保持枚举 enum 类不被混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#
## universal-image-loader 混淆
#
#-dontwarn com.nostra13.universalimageloader.**
#
#-keep class com.nostra13.universalimageloader.** { *; }
#
#
##eventbus
#-dontwarn de.greenrobot.event.**
#-keep class de.greenrobot.event.** { *; }
#-keepclassmembers class ** {
#    public void onEvent*(**);
#    void onEvent*(**);
#}
## 保留model包中的类
#-keep class com.kunzhuo.xuechelang.model.** { *; }
#
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#-keep class com.idea.fifaalarmclock.entity.***
#-keep class com.google.gson.stream.** { *; }
#-keepclassmembers class * {
#   public <init>(org.json.JSONObject);
#}
#
#-keep class com.umeng.**
#
#-keep public class com.idea.fifaalarmclock.app.R$*{
#    public static final int *;
#}
#
#-keep public class com.umeng.fb.ui.ThreadView {
#}
#
#-dontwarn com.umeng.**
#
#-dontwarn org.apache.commons.**
#
#-keep public class * extends com.umeng.**
#
#-keep class com.umeng.** {*; }
#
#-keep class com.baidu.** {*;}
#-keep class vi.com.** {*;}
#-dontwarn com.baidu.**
#
#-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*; }
#-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*; }
#
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#-libraryjars libs/alipaySdk-20160825.jar
#
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}