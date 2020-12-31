# WACeL-Java

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
| 2 |  TestupdatePosTagWithVerb | Cambiar cualquier tag segun su id por un verbo en una sentencia | Texto : The 25-year-old lives in Rockford, Illinois, sharing a home with his parents about a half mile from the Don Carter Lanes, where he was a regular at the upstairs tavern, Shooter’s Bar and Grill. He was there as usual this past Saturday, but left earlier than he normally does.| run 25-year-old lives in rockford , illinois , sharing a home with his parents about a half mile from the don carter lanes , where he was a regular at the upstairs tavern , shooter bar and grill . he was there as usual this past saturday , but left earlier than he normally does | run 25-year-old lives in rockford , illinois , sharing a home with his parents about a half mile from the don carter lanes , where he was a regular at the upstairs tavern , shooter bar and grill . he was there as usual this past saturday , but left earlier than he normally does| pass | 
| 3 | TestupdatePosTagPrepositionWithVerb | Cambiar cualquier tag segun sea una preposicion por un verbo en una sentencia | Texto : Carrera said there was some confusion on Monday, as some manamko' who weren't registered to come to the clinic showed up. Public Health is working with the island's mayors to register their manamko' to schedule when they should come to Okkodo.|cut said there was some confusion on monday , as some manamko ' who were n't registered to come to the clinic showed up . public health is working with the island 's mayors to register their manamko ' to schedule when they should come to okkodo | cut said there was some confusion on monday , as some manamko ' who were n't registered to come to the clinic showed up . public health is working with the island 's mayors to register their manamko ' to schedule when they should come to okkodo | pass | 
| 4 | TestupdatePosTagVerbWithAdjective | Cambiar cualquier tag segun sea un verbo por un adjetivo en una sentencia | Texto : They had complained that Maxwell was being mistreated by guards who wake her every 15 minutes at night and who subject her to repeated unnecessary searches while failing to adequately protect her from an outbreak of the coronavirus at the jail.| pretty had complained that maxwell was being mistreated by guards who wake her every 15 minutes at night and who subject her to repeated unnecessary searches while failing to adequately protect her from an outbreak of the coronavirus at the jail | pretty had complained that maxwell was being mistreated by guards who wake her every 15 minutes at night and who subject her to repeated unnecessary searches while failing to adequately protect her from an outbreak of the coronavirus at the jail| pretty had complained that maxwell was being mistreated by guards who wake her every 15 minutes at night and who subject her to repeated unnecessary searches while failing to adequately protect her from an outbreak of the coronavirus at the jail | pass | 
| 5 | TestadjustNounPosTags |  Ajustar etiquetas a partir de sustantivos| Texto : For substantially the same reasons as the Court determined that detention was warranted in the initial bail hearing, the Court again concludes that no conditions of release can reasonably assure the Defendant’s appearance at future proceedings.|-o-|-o-| pass | 
| 6 | TestadjustVerbPosTags |  Ajustar etiquetas a partir de verbos| Texto : For substantially the same reasons as the Court determined that detention was warranted in the initial bail hearing, the Court again concludes that no conditions of release can reasonably assure the Defendant’s appearance at future proceedings.|-o-|-o-| pass | 
| 7 |  TestupdatePosTagWithNoun | Cambiar cualquier tag segun su id por un sustantivo en una sentencia | Texto : Ever since our five children got their cell phones and started texting, Susan and I have used this technology to lift them up and encourage them| object since our five children got their cell phones and started texting , susan and i have used this technology to lift them up and encourage them | object since our five children got their cell phones and started texting , susan and i have used this technology to lift them up and encourage them  | pass |
| 8 |  TestadjustPrePositionPosTags | Ajustar etiquetas a partir de preposiciones | Texto : We created a collaborative account for Maddy, Owen, and Cora under TextPlus Free account.  It’s free, and there are ads, so be forewarned.| -o- | -o- | pass |
| 9 |  TestadjustAdjectivePosTags | Ajustar etiquetas a partir de adjectivos | Texto : to give the kids a bit of controlled freedom as they communicate with family members and friends that we agree upon| -o- | -o- | pass |
| 10 |  TestadjustUseCaseKeywords |  Ajustar el caso de uso de las palabras claves| Texto : have a rule that the phone stays in the house and on our main living area, not downstairs or in bedrooms| -o- | -o-| pass |
| 11 |  getVerbBaseFromThirdPerson |  Verbo base de retorno de un tiempo presente | Texto : Are our kids young for this? Probably. But it’s a different day and age, folks, and we’re doing the best we can|  are our kids young for this? probably. but it?s a different day and age, folks, and we?re doing the best we can | are our kids young for this? probably. but it?s a different day and age, folks, and we?re doing the best we can  | pass |

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
