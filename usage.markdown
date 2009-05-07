---
layout:      default
title:       Usage
---
<script type="text/javascript">
  var languages = ['java', 'php', 'ruby'];

  function displayCode(codeBlock, codeLanguage) {
    languages.each(function f(language) {
      codeBlockId = codeBlock + '-' + language;
      codeBlockLinkId = codeBlock + '-' + language + '-link';
      if(language == codeLanguage) {
        $(codeBlockId).show();
        $(codeBlockLinkId).addClassName('language-selected');
      }
      else {
        $(codeBlockId).hide();
        $(codeBlockLinkId).removeClassName('language-selected');
      }
      new Effect.Highlight($(codeBlockId), { endcolor: '#eeeeff', restorecolor: '#eeeeff', startcolor: '#215373' });
    });
  }
</script>

## Querying game servers
<div id="simple-query"></div>

## Querying master servers
<div id="master-query"></div>

## Controlling game servers using RCON
<div id="rcon"></div>

<script type="text/javascript">
  new Ajax.Updater('simple-query', 'code/simple-query.html', { method: 'get' });
  new Ajax.Updater('master-query', 'code/master-query.html', { method: 'get' });
  new Ajax.Updater('rcon', 'code/rcon.html', { method: 'get' });
</script>