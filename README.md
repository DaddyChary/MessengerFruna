# Messenger Fruna

Messenger Fruna es una aplicación de chat desarrollada en Android que utiliza Firebase para la autenticación y mensajería en tiempo real. Proporciona una interfaz intuitiva y responsiva para que los usuarios puedan comunicarse fácilmente.

---

## 📋 Características

- **Autenticación de Usuarios:** Registro e inicio de sesión mediante Firebase Authentication.
- **Mensajería en Tiempo Real:** Comunicación instantánea a través de Firebase Realtime Database.
- **Interfaz Adaptativa:** Los mensajes se posicionan automáticamente según el remitente.
- **Compatibilidad con Varios Usuarios:** Ideal para chats individuales o grupales.

---

## 🚀 Comenzando

Sigue estas instrucciones para obtener una copia del proyecto y ejecutarla en tu entorno local.

### 🛠️ Prerrequisitos

- [Android Studio](https://developer.android.com/studio) (Arctic Fox o superior).
- Una cuenta configurada en [Firebase Console](https://console.firebase.google.com/).

### 🔧 Instalación

1. **Clonar el repositorio:**

   [```bash
   git clone https://github.com/tu-usuario/messenger-fruna.git
   cd messenger-fruna](https://github.com/DaddyChary/MessengerFruna.git)

2. **Configurar Firebase:**

   - Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
   - Activa **Authentication** con el método de correo/contraseña.
   - Configura las reglas de seguridad para Realtime Database en la consola de Firebase, usando las siguientes reglas:

     ```json
     {
       "rules": {
         ".read": true,
         ".write": true
       }
     }
     ```

   - Descarga el archivo `google-services.json` desde la consola de Firebase. Este archivo contiene las configuraciones necesarias para conectar tu aplicación con Firebase.
   - Coloca el archivo `google-services.json` en la carpeta `app/` de tu proyecto en Android Studio.

3. **Abrir el proyecto en Android Studio:**

   - Abre Android Studio y selecciona **Open an Existing Project**.
   - Dirígete al directorio donde clonaste el repositorio y selecciona el archivo `build.gradle` del proyecto.

4. **Sincronizar dependencias:**

   - Una vez abierto el proyecto en Android Studio, espera a que se sincronicen automáticamente las dependencias necesarias.
   - Si Android Studio muestra un mensaje para actualizar las herramientas o complementos de Gradle, acepta y espera a que finalice.

5. **Ejecutar la aplicación:**

   - Conecta un dispositivo Android o usa un emulador.
   - Haz clic en el botón de ejecución (`▶`) en Android Studio para compilar y lanzar la aplicación.
   - Inicia sesión o registra un nuevo usuario para empezar a utilizar la app.

---

### 🚀 Funcionalidades principales

1. **Registro e inicio de sesión:**
   - Los usuarios pueden registrarse utilizando su correo electrónico y contraseña.
   - Autenticación implementada con Firebase Authentication.

2. **Lista de usuarios:**
   - Muestra todos los usuarios registrados (excepto al usuario autenticado).
   - Permite iniciar una conversación con cualquier usuario.

3. **Chat en tiempo real:**
   - Enviar y recibir mensajes en tiempo real utilizando Firebase Realtime Database.
   - Cada conversación se identifica con un `chatId` único basado en los IDs de los usuarios.

4. **Eliminar conversaciones:**
   - Opción para eliminar una conversación existente con confirmación.

5. **Interfaz personalizada:**
   - Los mensajes se alinean a la izquierda o derecha dependiendo del remitente.
   - Cada chat muestra el nombre del usuario con el que se conversa.

---

### 📂 Estructura del proyecto

```plaintext
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/messengerfruna/
│   │   │   │   ├── MainActivity.java        # Pantalla principal para el chat
│   │   │   │   ├── UserAdapter.java         # Adaptador para la lista de usuarios
│   │   │   │   ├── MessageAdapter.java      # Adaptador para los mensajes
│   │   │   │   ├── LoginActivity.java       # Pantalla de inicio de sesión
│   │   │   │   ├── RegisterActivity.java    # Pantalla de registro
│   │   │   │   ├── Etc...    # El resto de clases
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml    # Diseño de la pantalla principal
│   │   │   │   │   ├── item_user.xml        # Diseño para cada usuario en la lista
│   │   │   │   │   ├── item_message.xml     # Diseño para cada mensaje
│   │   │   │   │   ├── Etc...               # El resto de layouts
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
│   │   │   ├── AndroidManifest.xml          # Configuración del manifiesto
├── build.gradle
├── google-services.json                      # Archivo de configuración de Firebase
