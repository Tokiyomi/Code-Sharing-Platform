<!DOCTYPE html>
<html>
<head>
    <title>Create</title>
</head>
<body>
<script>
    function send() {


    let object = {
        "code": document.getElementById("code_snippet").value,
        "original_time": document.getElementById("time_restriction").value, // == "0" ? "-9999" : document.getElementById("time_restriction").value,
        "views": document.getElementById("views_restriction").value, // == "0" ? "-9999" : document.getElementById("views_restriction").value
        //"time_restricted": document.getElementById("time_restriction").value == "0" ? false : true,
        //"views_restricted": document.getElementById("views_restriction").value == "0" ? false : true
    };


    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);

    console.log(json);

    if (xhr.status == 200) {
      alert("Success!");
    }
}
</script>
<div>

<textarea id="code_snippet" style="padding: 5px;width: 600px; height: 100px">// write your code here</textarea>

<br>
<p>Time restriction: </p><input id="time_restriction" type="text" value="0" >
<br>
<p>Views restriction: </p><input id="views_restriction" type="text" value="0" >
<br>
<br>
<button id="send_snippet" type="submit" onclick="send()">Submit</button>
</div>
</body>
</html>