---
layout:      default
title:       Usage
---
<script type="text/javascript">
  var languages = ['java', 'php', 'ruby'];

  function displayCode(codeBlock, codeLanguage) {
    $.each(languages, function f(i, language) {
      var codeBlockId = '#' + codeBlock + '-' + language;
      var codeBlockLinkId = '#'  + codeBlock + '-' + language + '-link';
      if(language == codeLanguage) {
        $(codeBlockId).show();
        $(codeBlockLinkId).addClass('language-selected');
      } else {
        $(codeBlockId).hide();
        $(codeBlockLinkId).removeClass('language-selected');
      }
      $(codeBlockId + ' > div').effect('highlight', { color: '#99c9ff' }, 2000);
    });
  }

  $(function() {
    $('#simple-query').load('{{site.baseurl}}/code/simple-query');
    $('#master-query').load('{{site.baseurl}}/code/master-query');
    $('#rcon').load('{{site.baseurl}}/code/rcon');
    $('#community').load('{{site.baseurl}}/code/community');
  });
</script>

## Querying game servers
<div id="simple-query"></div>

## Querying master servers
<div id="master-query"></div>

## Controlling game servers using RCON
<div id="rcon"></div>

## Getting information from the Steam Community
<div id="community"></div>
