# CalculadoraDescendenteJava

Este repositorio contiene la implementación de una **calculadora simple** desarrollada en Java. El proyecto se centra en la aplicación de principios de **análisis léxico y sintáctico descendente recursivo** para procesar expresiones matemáticas, y ofrece una **interfaz gráfica de usuario (GUI)** para una interacción intuitiva.

Fue desarrollado como parte de la **Práctica #8: Reconocimiento Descendente** de un curso de compiladores o lenguajes de programación, con el objetivo de demostrar la construcción de un analizador sintáctico capaz de validar la sintaxis y evaluar expresiones, además de reportar errores de manera descriptiva.

## Contenido del Repositorio

El proyecto está organizado en las siguientes clases Java, cada una con una responsabilidad específica dentro del proceso de la calculadora:

* ### `Main.java`
    * Este archivo actúa como el punto de entrada principal para la **versión de consola** de la calculadora. Permite a los usuarios ingresar expresiones directamente en la terminal para su evaluación. Es útil para pruebas rápidas y para observar el proceso de tokenización y análisis.

* ### `CalculatorGUI.java`
    * Contiene la implementación de la **interfaz gráfica de usuario (GUI)** de la calculadora, construida con la biblioteca Swing de Java.
    * Ofrece un diseño visual mejorado con botones interactivos para números, operadores, paréntesis, un botón "C" (Clear) para borrar la entrada, y un botón "Copy" para copiar el resultado al portapapeles.
    * Gestiona los eventos de usuario y se encarga de mostrar las expresiones y los resultados, así como los mensajes de error.

* ### `Lexer.java`
    * Implementa el **analizador léxico** (scanner o tokenizador).
    * Su función es leer la cadena de entrada (la expresión matemática) carácter por carácter y agruparlos en unidades significativas llamadas **tokens** (números, operadores, paréntesis, etc.).
    * Es responsable de identificar y reportar **errores de caracteres inválidos** que no forman parte del lenguaje de la calculadora.

* ### `Parser.java`
    * Implementa el **analizador sintáctico descendente recursivo**.
    * Toma la secuencia de tokens generada por el `Lexer` y verifica si la expresión cumple con la gramática definida para la calculadora.
    * Evalúa la expresión matemática para producir el resultado final.
    * Es crucial para el **manejo de errores de sintaxis**, proporcionando mensajes específicos cuando se detectan problemas como:
        * Tokens inesperados (ej. se esperaba un número pero se encontró un operador).
        * Paréntesis no balanceados.
        * Operadores faltantes (ej. `2(3)` en lugar de `2*(3)`).
        * Caracteres adicionales al final de una expresión válida.
        * También maneja el **error de división por cero**.

* ### `Token.java`
    * Define la estructura de un **token**.
    * Contiene una enumeración (`TokenType`) para clasificar los diferentes tipos de tokens que la calculadora puede reconocer (ej. `NUMBER`, `PLUS`, `LPAREN`, `EOF`).
    * Almacena el valor (`value`) asociado a cada token (ej. el número "123" o el símbolo "+").

## Funcionalidades Clave

* **Análisis Completo:** Desde la entrada de texto hasta la evaluación del resultado, pasando por el análisis léxico y sintáctico.
* **Detección de Errores Detallada:** Mensajes de error específicos para ayudar al usuario a corregir la sintaxis.
* **Soporte para Operaciones Básicas:** Suma, resta, multiplicación, división.
* **Precedencia de Operadores:** Manejo correcto de la precedencia y asociatividad gracias al diseño del analizador.
* **Interfaz Amigable:** Facilita la interacción y las pruebas de la calculadora.

## ¿Cómo Ejecutar?

Para compilar y ejecutar este proyecto, necesitarás un **Java Development Kit (JDK) 8 o superior** y un IDE como IntelliJ IDEA.

1.  **Clona el repositorio:**
    ```bash
    git clone https://github.com/alejorendons/JavaSimpleCalculatorGUI.git
    cd JavaSimpleCalculatorGUI
    ```
2.  **Abre el proyecto en tu IDE:** Importa la carpeta `JavaSimpleCalculatorGUI` como un proyecto Java.
3.  **Ejecuta la GUI:** Abre `CalculatorGUI.java` y ejecuta su método `main()`.
4.  **Ejecuta la versión de consola (opcional):** Abre `Main.java` y ejecuta su método `main()`.

---
