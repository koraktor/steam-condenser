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

<div class="command">
  <h3>Ruby</h3>
  Install the gem via RubyGems:
  <pre>$ gem install steam-condenser</pre>
  or specify it as a dependency in your Gemfile:
  <pre>gem 'steam-condenser', '~> {{page.version}}'</pre>
</div>

<div class="command">
  <h3>Java</h3>
  You have to define Steam Condenser as a dependency in your POM file:
  <pre>&lt;dependency>
    &lt;groupId>com.github.koraktor&lt;/groupId>
    &lt;artifactId>steam-condenser&lt;/artifactId>
    &lt;version>{{page.version}}&lt;/version>
&lt;/dependency></pre>
</div>

<div class="download">
  <h3>PHP</h3>
  <a href="https://github.com/koraktor/steam-condenser-php/archive/{{page.version}}.tar.gz">steam-condenser-php-{{page.version}}.tar.gz</a>
  <br>
  <pre>
  SHA1: 10358495a110752289211a9bcf101b1e84e8aa4d
  MD5:  11788a10deb57232d695543ded8f806e</pre>
</div>

## Git repositories
If you want to contribute, cloning the Git repository of a language specific
implementation is the best way to get started:

<div class="command">
  <h3>Java repository</h3>
  <pre>git clone git://github.com/koraktor/steam-condenser-java.git</pre>
</div>

<div class="command">
  <h3>PHP repository</h3>
  <pre>git clone git://github.com/koraktor/steam-condenser-php.git</pre>
</div>

<div class="command">
  <h3>Ruby repository</h3>
  <pre>git clone git://github.com/koraktor/steam-condenser-ruby.git</pre>
</div>

If you want to work across implementations or modify some of the main files,
like the <code>README</code>, you should clone the main repository.

<div class="command">
  <h3>Main repository</h3>
  <pre>git clone git://github.com/koraktor/steam-condenser.git</pre>
</div>

<br><br>

{% include google_adsense %}

  [1]: http://github.com/koraktor/steam-condenser/downloads
