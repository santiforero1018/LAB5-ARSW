### Escuela Colombiana de Ingeniería

### Arquitecturas de Software
### Santiago Forero Yate, Juan Sebastian Cepeda Saray	



#### API REST para la gestión de planos.

En este ejercicio se va a construír el componente BlueprintsRESTAPI, el cual permita gestionar los planos arquitectónicos de una prestigiosa compañia de diseño. La idea de este API es ofrecer un medio estandarizado e 'independiente de la plataforma' para que las herramientas que se desarrollen a futuro para la compañía puedan gestionar los planos de forma centralizada.
El siguiente, es el diagrama de componentes que corresponde a las decisiones arquitectónicas planteadas al inicio del proyecto:

![](img/CompDiag.png)

Donde se definió que:

* El componente BlueprintsRESTAPI debe resolver los servicios de su interfaz a través de un componente de servicios, el cual -a su vez- estará asociado con un componente que provea el esquema de persistencia. Es decir, se quiere un bajo acoplamiento entre el API, la implementación de los servicios, y el esquema de persistencia usado por los mismos.

Del anterior diagrama de componentes (de alto nivel), se desprendió el siguiente diseño detallado, cuando se decidió que el API estará implementado usando el esquema de inyección de dependencias de Spring (el cual requiere aplicar el principio de Inversión de Dependencias), la extensión SpringMVC para definir los servicios REST, y SpringBoot para la configurar la aplicación:


![](img/ClassDiagram.png)

### Parte I

1. Integre al proyecto base suministrado los Beans desarrollados en el ejercicio anterior. Sólo copie las clases, NO los archivos de configuración. Rectifique que se tenga correctamente configurado el esquema de inyección de dependencias con las anotaciones @Service y @Autowired.

2. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.
	- Implementación:
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/9ae12d64-b328-4ce3-9533-42069d10ed89)

3. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:

	* Modifique la clase BlueprintAPIController teniendo en cuenta el siguiente ejemplo de controlador REST hecho con SpringMVC/SpringBoot:

	```java
	@RestController
	@RequestMapping(value = "/url-raiz-recurso")
	public class XXController {
    
        
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoXX(){
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }        
	}

	```
	* Haga que en esta misma clase se inyecte el bean de tipo BlueprintServices (al cual, a su vez, se le inyectarán sus dependencias de persisntecia y de filtrado de puntos).

	- Implementación del Api Controller
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/3e822236-aed0-4df7-8bee-398ec6e1c64c)
		* Aqui se evidencia la inyección del BlueprintService

4. Verifique el funcionamiento de a aplicación lanzando la aplicación con maven:

	```bash
	$ mvn compile
	$ mvn spring-boot:run
	
	```
	Y luego enviando una petición GET a: http://localhost:8080/blueprints. Rectifique que, como respuesta, se obtenga un objeto jSON con una lista que contenga el detalle de los planos suministados por defecto, y que se haya aplicado el filtrado de puntos correspondiente.
	- Consulta de blueprints por medio de la petición GET
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/5e6d49e8-0c8c-47da-8f98-423813cf63ac)


6. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}, el cual retorne usando una representación jSON todos los planos realizados por el autor cuyo nombre sea {author}. Si no existe dicho autor, se debe responder con el código de error HTTP 404. Para esto, revise en [la documentación de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html), sección 22.3.2, el uso de @PathVariable. De nuevo, verifique que al hacer una petición GET -por ejemplo- a recurso http://localhost:8080/blueprints/juan, se obtenga en formato jSON el conjunto de planos asociados al autor 'juan' (ajuste esto a los nombres de autor usados en el punto 2).

	- Implementación del codigo requerido
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/4a7b5743-1130-4bda-9116-24d557cef986)
 	- prueba de la consulta desde la URL, consultado por autores
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/a130cce3-8b4a-4dc6-b2d7-4f8f191a2cee)

7. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}/{bpname}, el cual retorne usando una representación jSON sólo UN plano, en este caso el realizado por {author} y cuyo nombre sea {bpname}. De nuevo, si no existe dicho autor, se debe responder con el código de error HTTP 404. 

	- Implementación del codigo requerido
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/d6108e04-087d-4c51-8fd2-9fd85da8ccfd)
	- prueba de la consulta desde la URL, consultando autor y nombre del blueprint
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/231f40a3-d51d-4eb0-b1f9-c88355ce36cd)


### Parte II

1.  Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento jSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

	```	java
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<?> manejadorPostRecursoXX(@RequestBody TipoXX o){
        try {
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);            
        }        
 	
	}
	```
 	- Implementación de la función usando la petición POST
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/ae3721a8-a900-4c88-8452-b466a618f8b7)




2.  Para probar que el recurso ‘planos’ acepta e interpreta
    correctamente las peticiones POST, use el comando curl de Unix. Este
    comando tiene como parámetro el tipo de contenido manejado (en este
    caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo
    cual en este caso debe ser un documento jSON equivalente a la clase
    Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:

	```	
	$ curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://URL_del_recurso_ordenes -d '{ObjetoJSON}'
	```	

	Con lo anterior, registre un nuevo plano (para 'diseñar' un objeto jSON, puede usar [esta herramienta](http://www.jsoneditoronline.org/)):
	

	Nota: puede basarse en el formato jSON mostrado en el navegador al consultar una orden con el método GET.


3. Teniendo en cuenta el autor y numbre del plano registrado, verifique que el mismo se pueda obtener mediante una petición GET al recurso '/blueprints/{author}/{bpname}' correspondiente.

	- Como se uso el SO de Windows, se ejecutó el siguiente comando:
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/04b2afcd-6426-4ead-9fc5-d71adc942b68)

	- y se verifica que si se añadió correctamente el nuevo plano
		![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/f76925ec-6868-49e6-832f-b059faad4362)

4. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.
   	- Implementación del metodo para actualizar
   	  1. En InMemoryPersistance
   	 	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/1bff4611-f3d8-45f4-9aef-004a3dfccde6)


   	  2. En BluePrintService
   	   	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/33fac651-3341-4200-bf13-a5c2c2e1843d)
  
   	  3. En el API controller
   	     	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/97ac8d5d-f7f8-46d5-b7eb-01ee7354390d)

	- siguiendo lo mismo que se hizo para el metodo POST, se hace la misma ejecución para el PUT
	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/228e62bd-24fe-4586-9836-5e720dbd18c3)

	- Vemos que se actualiza el bluePrint que queriamos actualizar, el cual correspondia al autor john con el nombre "thepaint2"
	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/5e6d49e8-0c8c-47da-8f98-423813cf63ac)
	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/7a4e9356-c56f-4730-b4e1-8a7235a71b95)

 	- Si modificamos el json usado como prueba para actualizar, y dejamos uno de los atributos nulos, el error se vera de manera personalizada a nuestra manera

	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/3a2e2ab1-c731-4909-9719-a41aaef86d08)
	![image](https://github.com/santiforero1018/LAB5-ARSW/assets/88952698/d920685e-7273-4dd2-85ee-3715de5fa993)


### Parte III

El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

* Qué condiciones de carrera se podrían presentar?
* Cuales son las respectivas regiones críticas?

Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.

Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt

#### Criterios de evaluación

1. Diseño.
	* Al controlador REST implementado se le inyectan los servicios implementados en el laboratorio anterior.
	* Todos los recursos asociados a '/blueprint' están en un mismo Bean.
	* Los métodos que atienden las peticiones a recursos REST retornan un código HTTP 202 si se procesaron adecuadamente, y el respectivo código de error HTTP si el recurso solicitado NO existe, o si se generó una excepción en el proceso (dicha excepción NO DEBE SER de tipo 'Exception', sino una concreta)	
2. Funcionalidad.
	* El API REST ofrece los recursos, y soporta sus respectivos verbos, de acuerdo con lo indicado en el enunciado.
3. Análisis de concurrencia.
	* En el código, y en las respuestas del archivo de texto, se tuvo en cuenta:
		* La colección usada en InMemoryBlueprintPersistence no es Thread-safe (se debió cambiar a una con esta condición).
		* El método que agrega un nuevo plano está sujeta a una condición de carrera, pues la consulta y posterior agregación (condicionada a la anterior) no se realizan de forma atómica. Si como solución usa un bloque sincronizado, se evalúa como R. Si como solución se usaron los métodos de agregación condicional atómicos (por ejemplo putIfAbsent()) de la colección 'Thread-Safe' usada, se evalúa como B.
