# WACeL-Java
# Automated Analysis of Natural Language Requirements: Scenarios &amp; Lexicons Tool

C&L is a web application (Java, MySql) for editing, visualization and analysis of scenarios. C&L architecture is based on layers style, divided into modules and developed using Domain-driven Design practices. Modules group functionalities to manage users (User), projects (Project), lexicon symbols (Language used in the application - LEL), scenarios (Scenario) and to perform Analysis. The input of the C&L is composed of projects containing scenarios in plain text format. The output is a set of formatted scenarios, where the relationships among scenarios are represented by hyperlinks (It facilitates the navigation between scenarios). Other outputs include: (1) A Petri-Net representing the scenarios and their relationships and (2) a Feedback with detailed information about scenarios analysis.

Scenarios are often described using templates based on Cockburn [1] or scenario language proposed by Leite et al. [2].

[1] A. Cockburn, Writing Effective Use Cases. Addison-Wesley, 2001.

[2] J. C. S. P. Leite, G. Hadad, J. Doorn and G. Kaplan, A scenario construction process, Requirements Engineering Journal, Springer-Verlag London Limited, vol. 5, num. 1, pp. 38-61, 2000.

## Technologies used :

-   Spring Security 5.1.3.RELEASE
-   Spring 5.1.5.RELEASE
-   mysql-connector-java:jar 8.0.16
-   Maven 3
-   Java 8
-   Stanford Core NLP 3.9.2
- 
## Build Instructions

Here are some helpful instructions to use the latest code:

### Build with Eclipse and Maven

1. Make sure you have MySql installed, details here:
    [https://www.mysql.com/downloads/](https://www.mysql.com/downloads/)
2. Create MySql schema "celdb" with user "root" and password "admin"     (change!)

3. Make sure you have Apache Tomcat installed, details here:
    [https://tomcat.apache.org/download-80.cgi](https://tomcat.apache.org/download-80.cgi)  --> Stop tomcat service
     
4. Make sure you have Eclipse IDE for Enterprise Java Developers, details here:     [https://www.eclipse.org/downloads/packages/](https://www.eclipse.org/downloads/packages/)
 
5. Eclipse IDE: Import as maven project (clone!)

6.  Eclipse IDE: Run as -> Maven build -> clean install

7. Eclipse IDE: Run as -> Run on server

8. Initialize (test user and profiles) "celdb": [initialize.sql](https://github.com/edgarsc22/WACeL-Java/blob/master/sqlscript/initialize.sql)

9. Initialize projects from Case Studies (projects and scenarios) in "celdb":  [case_study_projects.sql](https://github.com/edgarsc22/WACeL-Java/blob/master/sqlscript/case_study_projects.sql)

 10. go to http://localhost:8080/WACeL-Java and Test with user "test" and password "123456" (change!)
    
##  Case Studies
  There are some real **projects** in the literature, and as a result, we selected 4 projects to evaluate the _accuracy_ of the heuristics implemented in the C&L tool:      [Case Studies](https://github.com/edgarsc22/WACeL-Java/blob/master/docs/CeL%20and%20Case%20Study.pdf)

- E. Sarmiento, [Towards the improvement of natural language requirements descriptions: the C&L tool](https://dl.acm.org/doi/abs/10.1145/3341105.3374028), In The 35th ACM/SIGAPP Symposium On Applied Computing - SAC, 2020.

- E. Sarmiento, J. C. S. P. Leite, E. Almentero, [Analysis of Scenarios with Petri-Net Models](https://ieeexplore.ieee.org/abstract/document/7328013), In 29th Brazilian Symposium on Software Engineering (SBES), 2015.

- E. Sarmiento, J. C. S. P. Leite, and E. Almentero. [Using correctness, consistency, and completeness patterns for automated scenarios verification](https://ieeexplore.ieee.org/abstract/document/7407737/). 2015 IEEE Fifth International Workshop on Requirements Patterns (RePa). IEEE, 2015.

- E. Sarmiento, J. C. S. P. Leite, and E. Almentero. [C&L: Generating model based test cases from natural language requirements descriptions](https://ieeexplore.ieee.org/abstract/document/6908677/). IEEE 1st International Workshop on Requirements Engineering and Testing, 2014.

- E Sarmiento. [Analysis of Natural Language Scenarios](https://www.maxwell.vrac.puc-rio.br/28193/28193.PDF), Ph.D Thesis, Department  of Informatics, PUC-Rio, Brazil, 2016.

## Estudio de Refactorizacion hecho por:
Jhoel Tapara Quispe

Angelo Perez Rodriguez

Kelvin Pucho Zevallos 

Luis Vilcapaza Flores

## Actividades
Se aplico Pruebas Unitarias y la refactorizacion al archivo PosTagImprove.java

1. Pruebas Unitarias
```sh
 WACeL-Java\src\test\java\pe\edu\unsa\daisi\lis\cel\util\nlp\TestPosTagImprover.java
```

# Clase PosTagImprover
## Casos de Prueba calculadora
| TestID | Method |Test Scenario | Test Data | Expected Results | Returned Results | Successful/Failed|
| ------ | -------- |------------- | --------- | ---------------- | ---------------- |-------------|
| 1 |  TestgetPosTagsAsString| Calcular los tokens de los sujetos,objetos,verbos,etc de una sentencia | Texto : It is well-known that the existing theoretical models for outlier detection make assumptions that may not reflect the true nature of outliers in every real application. With that in mind, this paper describes an empirical study performed on unsupervised outlier detection using 8 algorithms from the state-of-the-art and 8 datasets that refer to a variety of real-world tasks of high impact, like spotting cyberat- tacks, clinical pathologies and abnormalities in nature. We present the| PRP VBZ JJ IN DT VBG JJ NNS IN NN NN VBP NNS WDT MD RB VB DT JJ NN IN NNS IN DT JJ NN . IN DT IN NN , DT NN VBZ DT JJ NN VBN IN JJ NN NN VBG CD NNS IN DT JJ CC CD NNS WDT VBP TO DT NN IN JJ NNS IN JJ NN , IN VBG NN : NNS , JJ NNS CC NNS IN NN . PRP VBP DT | PRP VBZ JJ IN DT VBG JJ NNS IN NN NN VBP NNS WDT MD RB VB DT JJ NN IN NNS IN DT JJ NN . IN DT IN NN , DT NN VBZ DT JJ NN VBN IN JJ NN NN VBG CD NNS IN DT JJ CC CD NNS WDT VBP TO DT NN IN JJ NNS IN JJ NN , IN VBG NN : NNS , JJ NNS CC NNS IN NN . PRP VBP DT | pass | 





2. Refactoring

# PosTagImprove.java
```sh
 WACeL-Java\src\main\java\pe\edu\unsa\daisi\lis\cel\util\nlp\PosTagImprove
```
### Funcion getPosTagsAsString: 
Cambio de **StringBuffer tags = new StringBuffer("")** cambio por  **StringBuilder tags = new StringBuilder("")**, ya que es mas rapida la operacion entre hilos que va a tener el programa
### Funcion adjustPosTags
Borrar todos los comentarios ya que no son necesiarios ademas que va a saturar el programa y la legibilidad de este 
Comentarios eliminados: **System.out.println(getPosTagsAsString(tokens, 0, tokens.size()))**
### Funcion adjustNounPosTags
- En primer lugar nos piden cambiar el nombre de la funcion de **adjustNounPosTags** se nos pide borrar por lo que es una funcion larga pero no se borrara porque tambien hay una regla de colocar el nombre de las funciones respecto a lo que va hacer ademas de que con esta funcion de puede guiar con las demas funciones que se crearon
- Eliminar **List<CustomToken> nouns = new ArrayList<>()** ya que nunca se usa esta variable
- Eliminacion de comentario **i: position in the analysis tokens array** 
- Cambio de variable **REGEX_PREV_POS_TAGS** por **rtags** para que no exista demora en lectura del condigo 
- Eliminar comentarios innecesarios como **System.out.println("PTR1 Token: " + noun.getStem())** ya que dificultan la lectura del codigo
- Cambio de **REGEX_PREV_POS_TAGS** y **REGEX_NEXT_POS_TAGS** por  **regexprevpostags** y **regexnextpostags** respectivamente en todo el codigo para que tenga una mejor lectura de codigo
- Eliminacion de comentarios inncesarios como **System.out.println("PTR2 Token: " + noun.getStem());**
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- Eliminar variables **REGEX_PREV_POS_TAGS** en linea 238 y **String prevPOSs =   getPosTagsAsString(tokens, 0, noun.getIndex())** por que nunca se usan y son variables basura
-  Eliminar variables **REGEX_NEXT_POS_TAGS** en linea 261 y **String nextvPOSs =   getPosTagsAsString(tokens, noun.getIndex()+1, tokens.size())** linea 262 por que nunca se usan y son variables basura 
### Funcion adjustVerbPosTags
- En primer lugar nos piden cambiar el nombre de la funcion de **adjustVerbPosTags** no se puede borrar por la misma explicacion de la antigua funcion
- **List<CustomToken> verbs = new ArrayList<>()** eliminar esta funcion porque no es usada nunca en el codigo
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- Cambio de **REGEX_PREV_POS_TAGS** y **REGEX_NEXT_POS_TAGS** por  **regexprevpostags** y **regexnextpostags** respectivamente en todo el codigo para que tenga una mejor lectura de codigo
- Eliminacion de **REGEX_NEXT_POS_TAGS** y **String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size())** ya que nunca se usan
- Eliminacion de **REGEX_PREV_POS_TAGS** y **prevPOSs = getPosTagsAsString(tokens, 0, verb.getIndex())** ya que nunca se usan
- Reemplazar **System.out.println("PTR13 Token: " + verb.getStem())** y **System.out.println("PTR13 Token is Noun!")** por **logger.log** para poder señalar errores y es mas eficaz al momento de señalar errores tanto para usuarios como desarrolladores
### Funcion updatePosTagWithNoun
- En primer lugar nos piden cambiar el nombre de la funcion de **updatePosTagWithNouns** no se puede borrar por la misma explicacion de la antigua funcion
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- Cambio de **REGEX_PREV_POS_TAGS** y **REGEX_NEXT_POS_TAGS** por  **regexprevpostags** y **regexnextpostags** respectivamente en todo el codigo para que tenga una mejor lectura de codigo
### Funcion adjustAdjectivePosTags
- En primer lugar nos piden cambiar el nombre de la funcion de **adjustAdjectivePosTags** no se puede borrar por la misma explicacion de la antigua funcion
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- Cambio de **REGEX_PREV_POS_TAGS** y **REGEX_NEXT_POS_TAGS** por  **regexprevpostags** y **regexnextpostags** respectivamente en todo el codigo para que tenga una mejor lectura de codigo
- Eliminacion de **REGEX_NEXT_POS_TAGS** y **String nextPOSs =  getPosTagsAsString(tokens, verb.getIndex()+1, tokens.size())** por que son variables sin uso del codigo y son basura para la lectura de este
### Funcion adjustUseCaseKeywords
- En primer lugar nos piden cambiar el nombre de la funcion de **adjustUseCaseKeywords** no se puede borrar por la misma explicacion de la antigua funcion
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- Cambio de **REGEX_USECASE_RETURN_KEYWORD** por **regexusecasereturnkeyword** espectivamente para que tenga una mejor lectura de codigo
### Funcion containsWord
- En primer lugar nos piden cambiar el nombre de la funcion de **containsWord** no se puede borrar por la misma explicacion de la antigua funcion
### Funcion containsSimilarWord
- En primer lugar nos piden cambiar el nombre de la funcion de **containsSimilarWord** no se puede borrar por la misma explicacion de la antigua funcion
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- cambiar todo **list.get(i).toUpperCase().equals(word)** por una variable **listing**
### Main
- Eliminacion de comentarios innecesarios en todo el codigo que no permiten legibilidad al codigo
- Reemplazar **System.out.println("before: " +getPosTagsAsString(tokens, 0, tokens.size()))** y **System.out.println("after: " + getPosTagsAsString(tokens, 0, tokens.size()))** por **logger.log** para poder señalar errores y es mas eficaz al momento de señalar errores tanto para usuarios como desarrolladores
