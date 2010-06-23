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
    $('#simple-query').load('code/simple-query.html');
    $('#master-query').load('code/master-query.html');
    $('#rcon').load('code/rcon.html');
  });
</script>

## Querying game servers
<div id="simple-query"></div>

## Querying master servers
<div id="master-query"></div>

## Controlling game servers using RCON
<div id="rcon"></div>
