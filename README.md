# [Chat de coco](https://github.com/coco-chat)-Servidor
Servidor para el procesamiento de las peticiones de los clientes del chat


## Base de datos
Archivo de base de datos [Aqui](https://drive.google.com/open?id=11XKnhv3RKS2rBQr3twS_R7frxiANsmR3)

## Codificación JSON
Se utilza la biblioteca Gson. [Ejemplo](https://www.adictosaltrabajo.com/tutoriales/gson-java-json/)

Las siguientes partes son necesarias:
- [gson javadoc](http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2-javadoc.jar)
- [gson sources](http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2-sources.jar)
- [gson](http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar)

## Tipos de mensajes
#### Mesnajes Recibidos en el Server
- [X] login
- [X] registro usuario
- [X] logout
- [X] recepción de mensaje
- [X] solicitud amigo
- [ ] olvidar amigo
- [ ] cambiar apodo amigo
- [X] agregar integrantes grupo
- [X] creargrupo + agregar integrantes grupo
- [ ] eliminar integrante grupo (admin, o personal)
- [X] pedir lista de usuarios conectados
- [X] pedir lista de usuarios desconectados
- [ ] pedir lista de amigos
- [ ] pedir lista de grupos
- [ ] pedir información de grupo

#### Envío de mensajes a clientes
- [ ] reconocimiento de mensajes (validación)
- [X] aceptar o rechazar login
- [X] envio de mensaje
- [ ] mensajes de grupos
- [ ] información grupo
- [X] usuarios activos


#### Tabla de codigos de respuesta del servidor
Codigo | Significado
------|------------
210 | login exitoso
220 | registro exitoso y login exitoso
230 | logout exitoso
240 | solicitud de amigo enviada
241 | solicitud aceptada (amigo 2)
250 | solicitud de grupo enviada
260 | mensaje enviado
270 | grupo creado
271 | miembro agregado correctamente(peticion de miembro)
404 | comando no encontrado
410 | error en el login
420 | registro exitoso pero login fallido
421 | se intentó registrar con nombre repetido
422 | registro fallido
440 | solicitud de amigo no enviada
441 | solicitud de amigo ya registrada
450 | solicitud de grupo no enviada
451 | solicitud de grupo ya registrada
460 | mensaje no enviado
470 | grupo no creado
471 | miembro no agregado (peticion de miembro)
