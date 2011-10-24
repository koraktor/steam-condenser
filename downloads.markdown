---
layout:  default
title:   Downloads
version: 1.0.2
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
  <a href="http://cloud.github.com/downloads/koraktor/steam-condenser/steam-condenser-{{page.version}}-php.tar.bz2">steam-condenser-{{page.version}}-php.tar.bz2</a>
  <br />
  <pre>
  SHA1: 2e55f11898e0d0ab230f33fc142b274e5fd1b1bf
  MD5:  8f06f26586f19555fcf431396cc972b0</pre>
</div>

## Git repository
If you want to contribute, cloning the Git repository is the best way to get
started:

<div class="command">
  <h3>Git repository</h3>
  <pre>git clone git://github.com/koraktor/steam-condenser.git</pre>
</div>

## Source archives
These files include the complete source code of Steam Condenser as found in the
*master* branch of the Git repository, i.e. the code for Java, PHP and Ruby. If
you want to work with a special part of the source code - or code not included
in the *master* branch, please clone the Git repository.

<div class="download">
  <h3>Gzip compressed Tar</h3>
  <a href="https://github.com/koraktor/steam-condenser/tarball/{{page.version}}">steam-condenser-{{page.version}}.tar.gz</a>
</div>

<div class="download">
  <h3>Zip archive</h3>
  <a href="https://github.com/koraktor/steam-condenser/zipball/{{page.version}}">steam-condenser-{{page.version}}.zip</a>
</div>

<br /><br />

{% include google_adsense %}

  [1]: http://github.com/koraktor/steam-condenser/downloads
