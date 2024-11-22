let pw1 = document.getElementById("paswoord");
let pw2 = document.getElementById("paswoord2");
let btnPassword = document.getElementById("btnPaswoord");


pw2.addEventListener("keyup",checkPaswords);
pw1.addEventListener("keyup",checkPaswords);



pw2.addEventListener("focusout",setAlert);

function setAlert() {
    if (pw1.value !== pw2.value || pw1.value ===''){
        alert("De paswoorden komen niet overeen.");
    }
}


function checkPaswords() {
    if (pw1.value === pw2.value && pw1.value !==''){
        btnPassword.removeAttribute('disabled');
    } else{
        btnPassword.setAttribute('disabled','');
    }
}


tooltips.forEach(t=>{new bootstrap.Tooltip(t)})

function makeEmail() {
    result.value = vn.value.trim().replace(' ','').toLowerCase()+"."+fn.value.trim().replace(' ','').toLowerCase()+(dag.value<10?("0"+dag.value):dag.value)+"@llnrvl.be" ;
}
