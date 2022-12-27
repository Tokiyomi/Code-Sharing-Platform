<!DOCTYPE html>
<html>
<head>
    <title>Latest</title>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>

<table>
    <#list code_list as code>
        <tr>
            <td>
                <span id="load_date" style="color: green">${code.load_date_str}</span>
                <br>
                <pre id="code_snippet" style="border-style: solid; border-width: 1px;width: 600px"><code>${code.code}</code></pre>
                <!-- padding: 10px; background-color: lightgray; -->
                </td>
        </tr>
    </#list>
</table>

</body>
</html>