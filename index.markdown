---
layout: default
title:  About
---
<div id="logo">
  <img alt="Steam Condenser logo"
       src="images/steam-condenser-64x64-opaque.png" />
</div>

Steam Condenser
===============

The Steam Condenser is a multi-language library for querying the Steam
Community, Source and GoldSrc game servers as well as the Steam master servers.
Currently it is implemented in Java, PHP and Ruby - a C# implementation is in
progress.

If you want to keep up with the latest development, [follow Steam Condenser on
Twitter][5].

## Features
- Server queries
  - Query Steam's master servers to receive a list of available game servers
  - Query game servers based on Valve's GoldSrc and Source engines for
    information
- Use RCON to control game servers
- Acquire information from the Steam Community about players and their game
  statistics
  - This features achievement statistics for *every* game supporting
    achievements
  - Additional, more detailed statistics for *Day of Defeat: Source*,
    *Left4Dead*, *Left4Dead 2* and *Team Fortress 2* as well as *Defense Grid:
    The Awakening*

## Requirements
- Java 1.4 or newer
- PHP 5 or newer
- Ruby 1.8.6 or newer
  - The Ruby [BZ2 module][1] is needed if you want to query Source servers
    sending compressed packets.

## Problems?

- If you think you found an error in Steam Condenser, please check the [issue
  list][3] for a report. If the error hasn't been reported yet, please submit an
  issue report.
- If you have a problem with using Steam Condenser or a question, please have
  a look at the [Steam Condenser group][4] for a related topic. If you can't
  find one, feel free to create your own.
- Additionally you're welcome to ask for support by messaging to [me
  (koraktor)][6] at GitHub or [@steamcondenser][5] at Twitter.

{% include google_adsense %}

## License
Steam Condenser is free software; you can redistribute it and/or modify it
under the terms of the new BSD License. A copy of this license can be found
[here][2].

  [1]: http://github.com/trans/bz2
  [2]: license.html
  [3]: http://github.com/koraktor/steam-condenser/issues
  [4]: http://groups.google.com/group/steam-condenser
  [5]: http://twitter.com/steamcondenser
  [6]: http://github.com/koraktor
