# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files\Android\sdk1/tools/proguard/proguard-android.txt
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

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

#四大组件不能混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application {*;}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
#自定义控件不要混淆
-keep public class * extends android.view.View {*;}
#adapter也不能混淆
-keep public class * extends android.widget.**Adapter {*;}
-keep public class * extends android.support.v4.widget.**Adapter {*;}
-keep public class * extends android.support.v7.widget.**Adapter {*;}
#数据模型不要混淆
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {*;}

#项目数据模型
-keep class **.entity.* {*;}
#lib
-keep class com.google.zxing.** {*;}
-dontwarn   com.google.zxing.**

-keep class com.android.volley.** {*;}
-dontwarn   com.android.volley.**

-keep class com.nostra13.universalimageloader.** {*;}
-dontwarn   com.nostra13.universalimageloader.**

-keep class com.pgyersdk.** {*;}
-dontwarn   com.pgyersdk.**

-keep class com.nostra13.universalimageloader.** {*;}
-dontwarn   com.nostra13.universalimageloader.**


-keep class cn.jpush.** {*;}
-dontwarn   cn.jpush.**


-keep class android.support.** {*;}
-keep class com.baidu.** {*;}
-dontwarn  com.baidu.**

-keepattributes Signature
-keep class com.google.gson.examples.android.model.** { *; }