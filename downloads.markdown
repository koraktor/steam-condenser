---
layout:  default
title:   Downloads
version: 1.3.0
---
Downloads
=========

The latest stable release of Steam Condenser is version **{{page.version}}**.

## Packages
These files contain complete, ready-for-use packages for their specific
programming language. Right now there are releases for Java, PHP and Ruby.
The Ruby gem can be installed using RubyGems, the Java JAR can be installed
using Maven and the PHP package has to be downloaded manually.

### Ruby

Specify the gem as a dependency in your Gemfile:

```ruby
gem 'steam-condenser', '~> {{page.version}}'
```

or install the gem directly via RubyGems:

```sh
$ gem install steam-condenser
```

### Java

If you're using Maven you have to define Steam Condenser as a dependency in
your POM file:

```xml
<dependency>
    <groupId>com.github.koraktor</groupId>
    <artifactId>steam-condenser</artifactId>
    <version>{{page.version}}</version>
</dependency>
```

If you're using another build tool, please use the appropriate syntax to use
Maven artifacts as a dependency.

### PHP

The recommended way to use Steam Condenser is to define a dependency using
Composer:

```json
{
    "require": {
        "koraktor/steam-condenser": "{{page.version}}"
    }
}
```

If you're not using Composer you can also get a plain source archive:

<div class="download">
  <a href="https://github.com/koraktor/steam-condenser-php/archive/{{page.version}}.tar.gz">steam-condenser-php-{{page.version}}.tar.gz</a>
  <br>
  <pre>
  SHA1: 10358495a110752289211a9bcf101b1e84e8aa4d
  MD5:  11788a10deb57232d695543ded8f806e</pre>
</div>

## Git repositories
If you want to contribute, cloning the Git repository of a language specific
implementation is the best way to get started:

### Java repository

```sh
$ git clone git://github.com/koraktor/steam-condenser-java.git
```

### PHP repository

```sh
$ git clone git://github.com/koraktor/steam-condenser-php.git
```

### Ruby repository

```sh
$ git clone git://github.com/koraktor/steam-condenser-ruby.git
```

If you want to work across implementations or modify some of the main files,
like the `README`, you should clone the main repository.

### Main repository

```sh
$ git clone git://github.com/koraktor/steam-condenser.git
```

<br><br>

{% include google_adsense %}
