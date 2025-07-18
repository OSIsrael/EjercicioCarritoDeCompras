# Sistema de Gestión de Carrito de Compras

## Nombre:Israel Orellana
![Sistema de Gestión de Carrito de Compras Logo](https://raw.githubusercontent.com/your-username/your-repo-name/main/docs/logo.png) Un robusto sistema de gestión de carrito de compras desarrollado en Java Swing, diseñado con una arquitectura MVC y el patrón DAO para una gestión eficiente de productos, usuarios y transacciones de compra. El sistema soporta múltiples roles de usuario (administrador y usuario regular) y cuenta con funcionalidad de internacionalización.

## 🚀 Características Principales

* **Gestión de Usuarios:**
    * Registro de nuevos usuarios con preguntas de seguridad.
    * Inicio de sesión seguro.
    * Recuperación de contraseña (olvido de contraseña).
    * Modificación de datos personales del usuario.
    * **Administradores:** Gestión completa de usuarios (crear, buscar, modificar roles, eliminar).
* **Gestión de Productos:**
    * Creación de nuevos productos (solo administradores).
    * Búsqueda de productos por nombre.
    * Listado de todos los productos disponibles.
    * Edición de productos existentes (solo administradores).
    * Eliminación de productos (solo administradores).
* **Gestión de Carritos de Compras:**
    * Añadir productos al carrito (simulación de compra).
    * Visualización detallada de los ítems en el carrito.
    * Cálculo automático de subtotal, IVA y total.
    * Listado y visualización de carritos de compra existentes.
    * Modificación de carritos existentes.
* **Roles y Control de Acceso:**
    * Diferenciación entre roles `ADMINISTRADOR` y `USUARIO`.
    * Acceso a funcionalidades específicas según el rol del usuario logueado.
* **Internacionalización (i18n):**
    * Soporte para múltiples idiomas: Español, Inglés y Francés.
    * Cambio de idioma dinámico desde la interfaz de usuario.
    * Formateo de fechas y valores monetarios según la configuración regional.
* **Interfaz de Usuario Avanzada:**
    * Entorno Multi-Documento (MDI) utilizando `JDesktopPane` y `JInternalFrame`.
    * Tablas dinámicas (`JTable`) para la visualización y gestión de datos.
    * Componentes Swing interactivos: `JTextField`, `JPasswordField`, `JButton`, `JComboBox`, `JMenu`, `JMenuItem`.
    * Iconografía clara en los botones (`ImageIcon`).
    * Personalización visual del `JDesktopPane` con un fondo.

## 🧱 Arquitectura y Diseño

El proyecto sigue rigurosamente los principios de diseño de software para garantizar la mantenibilidad, escalabilidad y robustez.

### Arquitectura MVC (Modelo-Vista-Controlador)

La aplicación está estructurada en tres capas principales:

* **Modelo (`ec.edu.ups.poo.modelo`):** Contiene las clases que representan la lógica de negocio y los datos de la aplicación (e.g., `Usuario`, `Producto`, `Carrito`, `ItemCarrito`, `Rol`, `Genero`, `Pregunta`).
* **Vista (`ec.edu.ups.poo.view`):** Comprende todas las clases de interfaz de usuario, construidas con Java Swing (`JFrame`, `JInternalFrame`). Son responsables de la presentación de los datos al usuario y de capturar sus interacciones.
* **Controlador (`ec.edu.ups.poo.controller`):** (Asumido en la descripción del proyecto) Actúa como intermediario entre la Vista y el Modelo, gestionando las entradas del usuario, actualizando el Modelo y reflejando los cambios en la Vista.

### Patrón DAO (Data Access Object)

Se ha implementado una capa DAO (`ec.edu.ups.poo.dao`) para abstraer el acceso a la fuente de datos. Esto permite que la lógica de negocio sea independiente de la tecnología de persistencia subyacente, facilitando la integración futura con bases de datos reales o sistemas de archivos.

### Principios SOLID

El código base se adhiere a los principios SOLID para promover un diseño de software limpio y robusto:

* **Single Responsibility Principle (SRP):** Cada clase tiene una única responsabilidad. Por ejemplo, las clases `*View` solo manejan la UI, mientras que las clases del modelo (`Producto`, `Usuario`) solo encapsulan datos y lógica de negocio.
* **Open/Closed Principle (OCP):** El sistema está abierto a la extensión, pero cerrado a la modificación. La implementación de internacionalización con la clase `Idioma` es un claro ejemplo, permitiendo añadir nuevos idiomas sin cambiar el código de las vistas existentes.
* **Liskov Substitution Principle (LSP):** Los subtipos pueden sustituir a sus tipos base sin alterar la corrección del programa. Esto se ve en el uso polimórfico de `JInternalFrame` con sus diferentes implementaciones de vistas específicas.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje de Programación:** Java
* **Framework de GUI:** Java Swing
* **Gestión de Proyectos:** Maven (Recomendado, aunque no se mostró directamente el `pom.xml`, es una práctica estándar para proyectos Java).

## 🚀 Cómo Ejecutar el Proyecto

1.  **Clonar el Repositorio:**
    ```bash
    git clone [https://github.com/your-username/your-repo-name.git](https://github.com/your-username/your-repo-name.git)
    cd your-repo-name
    ```
    *(Asegúrate de reemplazar `your-username` y `your-repo-name` con los valores reales de tu repositorio).*

2.  **Abrir con un IDE:**
    * Importa el proyecto en tu IDE favorito (IntelliJ IDEA, Eclipse, NetBeans).
    * Asegúrate de que las dependencias de Maven (si aplicables) se resuelvan automáticamente.

3.  **Ejecutar la Aplicación:**
    * Localiza la clase principal (ej. `ec.edu.ups.poo.main.MainApp` o similar, si tienes una).
    * Ejecuta el método `main`.


