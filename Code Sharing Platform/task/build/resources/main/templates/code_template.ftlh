<!DOCTYPE html>
<html>
<head>
    <title>Code</title>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<div>
<span id="load_date" style="color: green">${load_date_str}</span>
<br>
<#if (views>0)>
 <span id="views_restriction"><u>${views}</u> more views allowed</span>
 <br>
</#if>
<#if (time>0)>
 <span id="time_restriction">The code will be available for <u>${time}</u> seconds</span>
 <br>
</#if>
<pre id="code_snippet" style=" border-style: solid; border-width: 1px;width: 600px"><code>${code}</code></pre>
<!-- padding: 10px; background-color: lightgray; -->
</div>
</body>
</html>