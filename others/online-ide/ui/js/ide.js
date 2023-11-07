let editor;

window.onload = function() {
    editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.session.setMode("ace/mode/c_cpp");
}

function changeLanguage() {

    let language = $("#languages").val();

    if(language == 'c' || language == 'cpp')editor.session.setMode("ace/mode/c_cpp");
    else if(language == 'php')editor.session.setMode("ace/mode/php");
    else if(language == 'python')editor.session.setMode("ace/mode/python");
    else if(language == 'node')editor.session.setMode("ace/mode/javascript");
    else if(language == 'Java')editor.session.setMode("ace/mode/java");
}

function executeCode() {
    const url = "http://localhost:3000/compile"; // Sunucu endpoint'ini düzeltin
    const language = document.getElementById("languages").value;
    const code = editor.getSession().getValue();

    const xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = xhr.responseText;
            document.querySelector(".output").textContent = response;
        } else {
            console.error("Hata: " + xhr.responseText);
        }
    };

    xhr.onerror = function () {
        console.error("İstek sırasında bir hata oluştu.");
    };

    const data = JSON.stringify({ language: language, code: code });
    xhr.send(data);
}
