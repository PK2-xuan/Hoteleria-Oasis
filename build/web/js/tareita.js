/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/// token 
var boton = document.getElementById("boton");

function traer() {
    var dni = document.getElementById("dni").value;
    fetch(
            "https://dniruc.apisperu.com/api/v1/dni/" +
            dni +
            "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN1YW5fYzFAb3V0bG9vay5jb20ifQ.IyJPBPJmR7OGTKyPQfSD2lmnFZN7_13Zfgbzqs0kuBY"
            )
            .then((res) => res.json())
            .then((data) => {
                if (data.success) {
                    document.getElementById("doc").value = data.dni || '';
                    document.getElementById("nombre").value = data.nombres || '';
                    document.getElementById("apellido_paterno").value = data.apellidoPaterno || '';
                    document.getElementById("apellido_materno").value = data.apellidoMaterno || '';
                    document.getElementById("cui").value = data.codVerifica || '';
                } else {
                    document.getElementById("doc").value = '';
                    document.getElementById("nombre").value = '';
                    document.getElementById("apellido_paterno").value = '';
                    document.getElementById("apellido_materno").value = '';
                    document.getElementById("cui").value = '';

                }
            })
            .catch((error) => {
                console.log(error);
            });

}

boton.addEventListener("click", traer);
