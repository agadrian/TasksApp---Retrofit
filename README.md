# Gestor de Tareas - App con Jetpack Compose y MongoDB

## Descripción del proyecto
Esta aplicación permite a los usuarios gestionar sus tareas de manera eficiente. Con una interfaz intuitiva y funcional, los usuarios pueden agregar, marcar como completadas y eliminar tareas. Además, la aplicación maneja errores mediante cuadros de diálogo, con mensajes claros e intuitivos.
Por otro lado, tiene una segunda pantalla o zona de Administrador, donde un usuario que tenga el rol ADMIN, puede gestionar lass tareas de cualquier usuario registrado en el sistema que aloja la App. Es decir, puede tanto crearle tareas, como eliminarlas, editarlas, ver todas las tareas del sistema, ver todas las tareas de algun usuario concreto, etc...

## Características principales
- Registro e inicio de sesión: Permite a los usuarios autenticarse. Lógica realizada mediante la API REST creada en SpringBoot usando seguridad con JWT y otros métodos, como encriptación a la hora de guardar la contraseña, etc...
- Base de datos usada para ello, MongoDB.
- Interfaz basada en JetpackCompose, haciendo la unión con la API mediante RetroFit.

## Usabilidad

La aplicación está diseñada para ser fácil de usar, con una interfaz clara y botones accesibles. Algunas características clave son:

- Diseño intuitivo y claro.
- Mensajes de error visibles: Los errores se muestran en cuadros de diálogo para no afectar la navegación.
- Flujo de usuario optimizado: Se minimiza la cantidad de pasos necesarios para realizar acciones comunes.
- Modo oscuro y modo claro alternable, a elección del usuario. Haciendo uso de **MaterialTheme.colorScheme**

## Ejemplo de funcionalidad
Disponible una prueba visual de la App en el archivo de video adjunto al proyedcto.

## Futuras implementaciones
La idea es conitnuar este desasrollo en el tiempo, implementandon nuevas características y funcionalidades que mejoren la experiencia del usuario tales como:

- Añadir notificaciones y recordatorios
- Sistema de arrastrar y soltar tareas, apra reorganizarlas de una manera sencilla
- Creación de un widget en la pantalla de inicio para permitir ves tus tareas de un vistazo rápido
- Añadir calendario, y posible sincronización con Google Calendar o similares.
