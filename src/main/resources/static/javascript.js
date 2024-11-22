

let result = document.getElementById("email");
let vn = document.getElementById("voornaam");
let fn = document.getElementById("familienaam");
let dag = document.getElementById("dag");

const tooltips = document.querySelector(".tt");


fn.addEventListener("keyup",makeEmail);
vn.addEventListener("keyup",makeEmail);
dag.addEventListener("keyup",makeEmail);


tooltips.forEach(t=>{new bootstrap.Tooltip(t)})

function makeEmail() {
    result.value = vn.value.trim().replace(' ','').toLowerCase()+"."+fn.value.trim().replace(' ','').toLowerCase()+(dag.value<10?("0"+dag.value):dag.value)+"@llnrvl.be" ;
}
