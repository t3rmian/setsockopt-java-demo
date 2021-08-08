# setsockopt-java-demo

A sample that shows how to apply some native TCP/IP options (i.e. IP DF flag) specific to a chosen operating system through the Java Socket and JNA. [Read more…](https://blog.termian.dev/posts/java-socket-native-options/)

The `core` module (JNA based) was prepared for JDK 8. On the other hand, the `foreign` module (Foreign Linker API/Foreign Memory Access API) is a preview of JDK 16 incubator features,
meant to be run with `--illegal-access=permit --add-modules jdk.incubator.foreign -Dforeign.restricted=warn` JVM parameters.