# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\guugu\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#代码混淆的压缩比例，值在0-7之间 Android通常为5
-optimizationpasses 5

#混淆后类名都为小写
-dontusemixedcaseclassnames

#指定不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers

#不做预校验的操作
-dontpreverify

#生成原类名和混淆后的类名的映射文件
-verbose
-printmapping priguardMapping.txt

#指定混淆是采用的算法
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

################common###############
-keep public class * implements com.hxw.frame.integration.ConfigModule
#实体类不参与混淆
#自定义控件不参与混淆
-keep class com.hxw.frame.** { *; }
#泛型，解决出现类型转换错误的问题
-keepattributes Signature
#注解
-keepattributes *Annotation*
#保持 Parcelable 不被混淆（aidl文件不能去混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keepnames class * implements java.io.Serializable
#保护实现接口Serializable的类中，指定规则的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保留R下面的资源
-keep class **.R$* {*;}
-keepclassmembers class **.R$* {
    public static <fields>;
}
#下面这行代码是 忽略警告，避免打包时某些警告出现
-ignorewarnings
#使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#jni方法不可混淆，因为这个方法需要和native方法保持一致
#保持native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#AndroidMainfest中的类不混淆，所以四大组件和Application的子类和Framework层下所有的类默认不会进行混淆。
#自定义的View默认也不会被混淆；所以像网上贴的很多排除自定义View，
#或四大组件被混淆的规则在Android Studio中是无需加入的。
################support###############
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

################butterknife###############
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
   @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
 @butterknife.* <methods>;
}

################retrofit###############
#-dontnote retrofit2.Platform
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor
#-dontwarn retrofit2.Platform$Java8
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions
#-keepclasseswithmembers class * {
#    @retrofit2.http.* <methods>;
#}
-dontwarn okio.**
-dontwarn javax.annotation.**
################gson###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
# 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
# 将下面替换成自己的实体类
-keep class com.app.modou.framework.bean.** { *; }

################okhttp(含OKhttp3)###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
# Okio
-dontwarn okio.**

################Timber#################
-dontwarn org.jetbrains.annotations.**

################glide###############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
-keep class GlideConfiguration

################Rxjava and RxAndroid(仿写2版本)###############
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keep class rx.** { *; }
-keep interface rx.** { *; }

-keep class io.reactivex.** { *; }
-keep interface io.reactivex.** { *; }

#okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn rx.**

-dontwarn io.reactivex.**

-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.**

-dontwarn java.lang.invoke.*
-dontwarn rx.internal.util.unsafe.**


-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn rx.internal.util.unsafe.**

-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}

-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    io.reactivex.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    io.reactivex.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn io.reactivex.internal.util.unsafe.**

################RxLifeCycle#################
-keep class com.trello.rxlifecycle.** { *; }
-keep interface com.trello.rxlifecycle.** { *; }
-keep class com.trello.rxlifecycle2.** { *; }
-keep interface com.trello.rxlifecycle2.** { *; }

################RxCache#################
-dontwarn io.rx_cache.internal.**
-keep class io.rx_cache.internal.Record { *; }
-keep class io.rx_cache.Source { *; }

-dontwarn io.rx_cache2.internal.**
-keep class io.rx_cache2.internal.Record { *; }
-keep class io.rx_cache2.Source { *; }

-keep class io.victoralbertos.jolyglot.** { *; }
-keep interface io.victoralbertos.jolyglot.** { *; }

################AndroidEventBus################
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}
-keepattributes *Annotation*

################LeakCanary#################
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

-dontwarn android.app.Notification

################epresso###############
-keep class android.support.test.espresso.** { *; }
-keep interface android.support.test.espresso.** { *; }

################alipay支付宝钱包###############
#-dontwarn com.alipay.**
#-dontwarn HttpUtils.HttpFetcher
#-dontwarn com.ta.utdid2.**
#-dontwarn com.ut.device.**
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
#-keep class com.alipay.mobilesecuritysdk.*
#-keep class com.ut.*

################zxing###############
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**
################RxErrorHandler################
 -keep class me.jessyan.rxerrorhandler.** { *; }
 -keep interface me.jessyan.rxerrorhandler.** { *; }