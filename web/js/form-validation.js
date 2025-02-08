/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
    "use strict";

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll(".needs-validation");

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener(
                "submit",
                function (event) {
                    event.preventDefault(); // Evita que el formulario se envíe inmediatamente
                    if (!form.checkValidity()) {
                        event.stopPropagation();
                    } else {
                        var dniInput = form.querySelector("#doc");
                        if (dniInput) {
                            var dniValue = dniInput.value.trim();

                            // Validar si el valor del DNI está vacío
                            if (dniValue === "") {
                                event.preventDefault();
                                event.stopPropagation();
                                Swal.fire({
                                    title: "Error",
                                    text: "El número de DNI no es válido.",
                                    icon: "error",
                                    confirmButtonText: "Aceptar",
                                });
                            } else {
                                // Si el DNI es válido, mostrar SweetAlert y luego proceder
                                Swal.fire({
                                    title: "Éxito",
                                    text: "Haz clic en OK para generar su constancia.",
                                    icon: "success",
                                    confirmButtonText: "OK",
                                    // No ponemos `timer` para que no se cierre automáticamente
                                    didClose: () => {
                                        // Después de que el usuario haga clic en "OK", generamos el PDF
                                        generarPDF(); // Llama a la función para generar el PDF
                                        window.location.href = "home.xhtml"; // O redirige si es necesario
                                    }
                                });
                            }
                        }
                    }

                    form.classList.add("was-validated");
                },
                false
                );
    });

    function generarPDF() {
        const {jsPDF} = window.jspdf; // Crea una instancia de jsPDF
        const doc = new jsPDF(); // Crea un nuevo documento PDF

        // Recolecta los valores del formulario
        const dni = document.getElementById("doc").value;
        const nombre = document.getElementById("nombre").value;
        const apellidoPaterno = document.getElementById("apellido_paterno").value;
        const apellidoMaterno = document.getElementById("apellido_materno").value;
        const email = document.getElementById("email").value;
        const pais = document.getElementById("country").value;
        const departamento = document.getElementById("state").value;
        const distrito = document.getElementById("district").value;
        const metodoPago = document.querySelector('input[name="paymentMethod"]:checked').nextElementSibling.textContent;
        const titularTarjeta = document.getElementById("cc-name").value;
        const numeroTarjeta = document.getElementById("cc-number").value;
        const expiracion = document.getElementById("cc-expiration").value;
        const cvv = document.getElementById("cc-cvv").value;

        // Agrega el título
        doc.setFontSize(22);
        doc.setFont("helvetica", "bold");
        doc.text("Formulario de Inscripción", 10, 10);

        // Establecer la fuente para el resto del contenido
        doc.setFontSize(12);
        doc.setFont("helvetica", "normal");

        // Agregar los datos al PDF
        doc.text("Número de DNI:", 10, 20);
        doc.text(dni, 60, 20);

        doc.text("Nombre:", 10, 30);
        doc.text(nombre, 60, 30);

        doc.text("Apellido Paterno:", 10, 40);
        doc.text(apellidoPaterno, 60, 40);

        doc.text("Apellido Materno:", 10, 50);
        doc.text(apellidoMaterno, 60, 50);

        doc.text("Email:", 10, 60);
        doc.text(email ? email : "No proporcionado", 60, 60);

        doc.text("País:", 10, 70);
        doc.text(pais, 60, 70);

        doc.text("Departamento:", 10, 80);
        doc.text(departamento, 60, 80);

        doc.text("Distrito:", 10, 90);
        doc.text(distrito, 60, 90);

        doc.text("Método de Pago:", 10, 100);
        doc.text(metodoPago, 60, 100);

        doc.text("Nombre del Titular:", 10, 110);
        doc.text(titularTarjeta, 60, 110);

        doc.text("Número de Tarjeta:", 10, 120);
        doc.text(numeroTarjeta, 60, 120);

        doc.text("Expiración:", 10, 130);
        doc.text(expiracion, 60, 130);

        doc.text("CVV:", 10, 140);
        doc.text(cvv, 60, 140);

        // Agregar líneas de separación y bordes
        doc.setLineWidth(0.5);
        doc.line(10, 15, 200, 15); // Línea horizontal debajo del título
        doc.line(10, 145, 200, 145); // Línea horizontal debajo de la última sección

        // Agregar información resaltada con color
        doc.setTextColor(0, 102, 204); // Color azul para resaltar
        doc.text("Detalles del Pago", 10, 120);

        // Agregar un pie de página con información
        doc.setTextColor(0, 0, 0); // Restaurar color de texto
        doc.setFontSize(10);
        doc.text("Gracias por completar el formulario. Todos los datos han sido registrados correctamente.", 10, 180);

        // Guarda el PDF
        doc.save("Constancia.pdf"); // Descarga el archivo PDF
    }

})();

//////// condiciones para la fecha y el dni
document.getElementById("boton").addEventListener("click", function () {
    // Obtener los valores de fecha y DNI ingresados
    var fechaNacimiento = document.getElementById("fechaNacimiento").value;
    var dni = document.getElementById("dni").value;

    // Validar si las cajas de fecha y DNI están llenadas
    if (!fechaNacimiento || !dni) {
        alert("Por favor, completa todos los campos.");
        return;
    }

    // Validar si el usuario es mayor de 18 años
    if (!esMayorDeEdad(fechaNacimiento)) {
        alert("Debes ser mayor de 18 años para continuar.");
        return;
    }

    // Mostrar SweetAlert mientras se realiza la búsqueda del DNI
    Swal.fire({
        title: "Buscando su DNI",
        icon: "info",
        showConfirmButton: false,
        allowOutsideClick: false,
        allowEscapeKey: false,
    });

    // Simular una llamada asincrónica para buscar el DNI
    setTimeout(function () {
        // Ocultar SweetAlert
        Swal.close();

        // Mostrar el formulario
        document.getElementById("mensaje").style.display = "block";

        // Ocultar el section
        document.querySelector("section").style.display = "none";
    }, 2000); // Ejemplo: 2000 milisegundos (2 segundos)

    // Evitar que el formulario se envíe automáticamente
    event.preventDefault();
});

function esMayorDeEdad(fechaNacimiento) {
    // Lógica para verificar si la fecha de nacimiento corresponde a una persona mayor de edad
    // Puedes implementar tu propia lógica aquí, comparando la fecha actual con la fecha de nacimiento
    // Por simplicidad, en este ejemplo se considera que una persona es mayor de edad si su año de nacimiento es anterior a 2004
    var anioNacimiento = new Date(fechaNacimiento).getFullYear();
    var anioActual = new Date().getFullYear();
    return anioNacimiento <= 2004;
}
