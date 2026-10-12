**Maestría en SOFTWARE**

**Asignatura:** Patrones de Diseño de Software  
**Actividad #:** 01 — Patrones creacionales  
**Docente:** Mauricio Ortiz Ochoa

---

**Actividad de aprendizaje #: 01**

# Patrones creacionales

## Fecha

- 2026/09/17

## Grupo base

- Grupo XX

## Integrantes

- Apellido 1 Apellido 2 Nombre 1 Nombre 2

- Apellido 1 Apellido 2 Nombre 1 Nombre 2

- Apellido 1 Apellido 2 Nombre 1 Nombre 2

- Apellido 1 Apellido 2 Nombre 1 Nombre 2

## Resultado de aprendizaje

- RA1. Selecciona y aplica patrones creacionales para resolver problemas de creación de objetos, revisando críticamente las alternativas elaboradas con apoyo de inteligencia artificial.

## Entregables:

- Archivo en PDF con el siguiente nombre: PDS_Actividad_01_GrupoXX.pdf

- Aplicación de Software almacenado en repositorio de código.

- La actividad se desarrolla en grupo; la carga al AVAC es individual.

## Tareas específicas:

- Participar en la clase de la Unidad 01, secciones 01, 02 y 03.

- Leer detenidamente el material de apoyo relacionado.

- Crear un repositorio de código para almacenar la aplicación solicitada.

- Seguir las instrucciones del ejercicio.

## Instrucciones:

### Escenario 01

> Una empresa desarrolla una aplicación para registrar vehículos y calcular el valor correspondiente a su matrícula. Actualmente el sistema debe trabajar con tres tipos de vehículos: Auto, Camioneta y Camión. Todos comparten información general, como placa, marca, modelo, año de fabricación y avalúo, y todos deben ofrecer una operación para obtener el costo de matrícula.
>
> Sin embargo, el cálculo no es igual para todos los tipos de vehículo. Por ejemplo, un Auto puede considerar principalmente su avalúo y antigüedad; una Camioneta puede incorporar además características relacionadas con su capacidad o uso; y un Camión puede requerir información adicional como tonelaje o capacidad de carga. Cada tipo de vehículo debe, por tanto, implementar su propia lógica de cálculo manteniendo un contrato común.
>
> La aplicación no conoce de antemano qué tipo concreto de vehículo deberá utilizar. La selección será realizada durante la ejecución del sistema a partir de la información proporcionada por el cliente. Una vez seleccionado el tipo, el resto del proceso debería poder trabajar con el vehículo sin depender directamente de Auto, Camioneta o Camión.
>
> Además, la empresa prevé incorporar posteriormente otros tipos de vehículos, por ejemplo motocicletas, buses o vehículos eléctricos. Se desea que la incorporación de estas nuevas variantes no obligue a modificar continuamente la lógica principal que utiliza los vehículos ni a distribuir instrucciones de creación de clases concretas por diferentes partes de la aplicación.
>
> Diseñar una solución orientada a objetos que permita delegar adecuadamente la creación del vehículo requerido, mantener un tratamiento común de los distintos tipos y facilitar la incorporación futura de nuevas variantes.

### Escenario 02

> Una institución necesita desarrollar un módulo para registrar sus activos fijos, entre los que pueden existir computadores, monitores, impresoras, muebles, vehículos y otros bienes institucionales.
>
> Todo activo fijo debe registrar obligatoriamente al menos:

- código institucional;

- nombre o descripción;

- precio de adquisición.

> Dependiendo del tipo de activo y de la información disponible, también pueden registrarse datos opcionales como:

- marca;

- modelo;

- número de serie;

- ubicación;

- custodio responsable;

- fecha de adquisición;

- período de garantía;

- proveedor;

- características técnicas;

- observaciones.

> No todos los activos poseen todos estos datos. Por ejemplo, un computador puede requerir marca, modelo, número de serie, procesador y garantía, mientras que una mesa puede necesitar únicamente material, dimensiones, ubicación y custodio.
>
> Si se intenta resolver el problema mediante constructores tradicionales, podrían aparecer múltiples constructores con diferentes cantidades y combinaciones de parámetros. Además, una llamada como: new ActivoFijo("AF001", "Computador", 1200, "Dell", "Latitude", null, "Cuenca", true, 36, ...)
>
> sería difícil de leer, mantener y utilizar correctamente, especialmente cuando aumenta el número de atributos opcionales.
>
> La institución necesita que los objetos puedan construirse progresivamente, estableciendo primero la información obligatoria y agregando posteriormente únicamente las características que correspondan a cada activo. La solución debe hacer explícito qué información se está incorporando y permitir obtener finalmente un objeto correctamente configurado.
>
> También debe ser posible crear diferentes configuraciones sin tener que mantener una gran cantidad de constructores sobrecargados ni obligar a proporcionar valores vacíos para características que no aplican.
>
> Diseñar una solución orientada a objetos que permita construir activos fijos paso a paso, manejar adecuadamente atributos obligatorios y opcionales y mantener legible el proceso de creación.

**Para ambos escenarios, cada grupo deberá:**

- Identificar el problema de creación de objetos.

- Seleccionar y justificar el patrón creacional correspondiente.

- Elaborar el diagrama de clases UML.

- Identificar los participantes del patrón.

- Implementar la solución en un lenguaje de programación.

- Almacenar el código en el repositorio del grupo.

**Para cada escenario, documentar el uso crítico de IA mediante:**

- Prompt principal.

- Una recomendación aceptada.

- Una recomendación modificada o rechazada.

- Justificación de ambas decisiones.

---

# Escenario 01

## Justificación

- _(completar)_

## Diagrama de clases

_(insertar diagrama)_

## Código fuente

- _(completar)_

## Uso crítico de inteligencia artificial

- Prompt principal:

- Recomendación aceptada (con justificación):

- Recomendación modificada o rechazada (con justificación):

# Escenario 02

## Justificación

- _(completar)_

## Diagrama de clases

_(insertar diagrama)_

## Código fuente

- _(completar)_

## Uso crítico de inteligencia artificial

- Prompt principal:

- Recomendación aceptada (con justificación):

- Recomendación modificada o rechazada (con justificación):

---

## Conclusiones de la actividad (200 palabras)

- Conclusiones.

## Rúbrica de evaluación:

| Criterios                                                               | No Presenta | Nivel Bajo | Nivel Medio | Nivel Alto |
|-------------------------------------------------------------------------|-------------|------------|-------------|------------|
| Selección, justificación y diagrama UML de Factory Method               | 0           | 1          | 2           | 3          |
| Implementación funcional y correspondencia UML–código de Factory Method | 0           | 1          | 2           | 3          |
| Selección, justificación y diagrama UML de Builder                      | 0           | 1          | 2           | 3          |
| Implementación funcional y correspondencia UML–código de Builder        | 0           | 1          | 2           | 3          |
| Uso crítico de IA, justificación y conclusiones                         | 0           | 1          | 2           | 3          |

## Documentos relacionados:

- Unidad_01_01_PDS.pdf

- Unidad_01_02_PDS.pdf

- Unidad_01_03_PDS.pdf
