CoFITS
======

JADE and Android applications for file exchange with MT4j-based devices.


* [Wiki](https://github.com/hkaj/CoFITS/wiki)
* [Website](http://hkaj.github.io/CoFITS/)

#### [ HOWTO ] Run the server

__Compiling:__

First, make sure that __lib/__ contains all the necessary libraries (jade, jackson and postgresql 9.3 jdbc). Then, just type in:

```
$ cd CoFITS/server
$ javac -classpath "lib/*" -d bin src/**/*.java
```

__Starting the platform:__ (for debugging purpose, mostly)
```
$ java -cp lib/jade.jar jade.Boot -local-host 127.0.0.1 -local-port 1098 -platform-id cofits
```

### OR

__Running the agents:__
```
$ java -cp "lib/*:bin/" jade.Boot -local-host 127.0.0.1 -local-port 1098 -platform-id cofits -agents documentAgent:DocumentAgent.DocumentAgent
```
