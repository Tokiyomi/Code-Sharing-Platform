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
<span id="views_restriction">${views} more allowed</span>
<br>
<span id="time_restriction">${time} remaining seconds</span>
<br>
<pre id="code_snippet" style=" border-style: solid; border-width: 1px;width: 600px"><code>${code}</code></pre>
<!-- padding: 10px; background-color: lightgray; -->
</div>
</body>
</html>