Steam Condenser
===============

The Steam Condenser is a multi-language library for querying the Steam
Community, Source and GoldSrc game servers as well as the Steam master servers.
Currently it is implemented in Java, PHP and Ruby.

## Requirements

* Linux, MacOS X or Windows
* Java 1.5 or newer

The following Java libraries are required:

* Apache Commons Compress (for Source servers sending compressed responses)
* Apache Commons Lang 3
* Jakarta Commons HTTPClient (for the Web API features)
* JSON (for the Web API features)
* JUnit (for testing)

Maven will install these for you.

## Installation

To install and use Steam Condenser in your Maven managed project use the
following dependency definition:

    <dependency>
        <groupId>com.github.koraktor</groupId>
        <artifactId>steamcondenser</artifactId>
    </dependency>

## License

This code is free software; you can redistribute it and/or modify it under the
terms of the new BSD License. A copy of this license can be found in the
included LICENSE file.

## Credits

* Sebastian Staudt -- koraktor(at)gmail.com
* David Wursteisen -- david.wursteisen(at)gmail.com
* Guto Maia -- guto(at)guto.net

## See Also

* [Steam Condenser home](https://koraktor.de/steam-condenser)
* [GitHub project page](https://github.com/koraktor/steam-condenser)
* [Wiki](https://github.com/koraktor/steam-condenser/wiki)
* [Google group](http://groups.google.com/group/steam-condenser)
* [Ohloh profile](http://www.ohloh.net/projects/steam-condenser)

Follow Steam Condenser on Twitter
[@steamcondenser](http://twitter.com/steamcondenser).
