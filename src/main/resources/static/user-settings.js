function enableInput(inputId) {
    document.getElementById(inputId).disabled=false;
}

function enablePass(inputId) {
    document.getElementById(inputId).disabled=false;
    document.getElementById("confirmPass").style.display = "block";
}

function confirmSave() {
    document.querySelectorAll('input:disabled').forEach(input => {
        input.disabled = false;
    });
}