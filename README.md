# Google Chat Appender for Log4j [![Build Status](https://travis-ci.org/modeverv/log4j-googlechat.svg?branch=master)](https://travis-ci.org/modeverv/log4j-googlechat)

This is a simple webhook-based Google Chat appender for Log4j 2.x.
To use this appender in your code, first get a webhook URL from Google Chat.
Then, add this to your project's dependencies:

```
allprojects {
   repositories {
        ...
        maven { url 'https://jitpack.io' }
   }
}
dependencies {
        implementation 'com.github.modeverv:log4j-googlechat:0.0.1'
}
```

To use this plugin, add the following to your `log4j2.xml` configuration (or translate to your preferred format):

```
<Configuration>
    <Appenders>
        <GoogleChat name="googlechat" webhook="https://chat.googleapis.com/v1/spa...">
            <MarkerFilter onMatch="ACCEPT" onMismatch="DENY" marker="GOOGLECHAT"/>
            <PatternLayout pattern="%m"/>
        </GoogleChat>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="googlechat"/>
        </Root>
    </Loggers>
</Configuration>
```

Using the MarkerFilter configuration like this allows you to send a log message to GoogleChat from any class.
By using the `GOOGLECHAT` marker, this allows the configuration to filter all messages with that marker and make sure to send them to the GoogleChatAppender.
As long as you don't override the additivity property of the relevant loggers, then these log messages will also be logged to the console or file or whatever you have configured normally.
Read more about markers [here](https://logging.apache.org/log4j/2.x/manual/markers.html).

## Configuration

The following are all the configuration attributes or elements supported:

* name: name of the appender, used when referencing via `<AppenderRef ref="name"/>`.
* filter: an optional filter to use; a MarkerFilter is recommended here as described above.
* ignoreExceptions: whether or not to let exceptions from the appender to be swallowed or propagated.
  This is true by default and should usually only be set to false when using a [FailoverAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#FailoverAppender) or similar.
* layout: a string layout to use to format log events into a Google Chat message.
  Only `StringLayout` layouts are supported since sending arbitrary binary data to Google Chat doesn't make sense.
  If no layout is specified, the default pattern layout is used.
* webhook: URL to the Google Chat webhook to send messages to.

## Test
set environment variable and run test.
```
GOOGLECHAT_WEBHOOK="https://...." ./gradlew test
```

## Contributing

Submit pull requests.

## License
Apache License, Version 2.0

## Thanks
[log4j-slack](https://github.com/jvz/log4j-slack)  