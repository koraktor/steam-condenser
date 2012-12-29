---
layout:  default
title:   Downloads
version: 1.2.1
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

{% highlight ruby %}gem 'steam-condenser', '~> {{page.version}}'{% endhighlight %}

or install the gem directly via RubyGems:

{% highlight sh %}$ gem install steam-condenser{% endhighlight %}

### Java

If you're using Maven you have to define Steam Condenser as a dependency in
your POM file:

{% highlight xml %}
<dependency>
    <groupId>com.github.koraktor</groupId>
    <artifactId>steam-condenser</artifactId>
    <version>{{page.version}}</version>
</dependency>
{% endhighlight %}

If you're using another build tool, please use the appropriate syntax to use
Maven artifacts as a dependency.

### PHP

The recommended way to use Steam Condenser is to define a dependency using
Composer:

{% highlight json %}
{
    "require": {
        "koraktor/steam-condenser": "{{page.version}}"
    }
}
{% endhighlight %}

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

{% highlight sh %}
$ git clone git://github.com/koraktor/steam-condenser-java.git
{% endhighlight %}

### PHP repository

{% highlight sh %}
$ git clone git://github.com/koraktor/steam-condenser-php.git
{% endhighlight %}

### Ruby repository

{% highlight sh %}
$ git clone git://github.com/koraktor/steam-condenser-ruby.git
{% endhighlight %}

If you want to work across implementations or modify some of the main files,
like the `README`, you should clone the main repository.

### Main repository

{% highlight sh %}
$ git clone git://github.com/koraktor/steam-condenser.git
{% endhighlight %}

<br><br>

{% include google_adsense %}
