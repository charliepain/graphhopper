# Documentation tâche 2
## Auteur(s): Charlie Peng, Yan Shek
### 7 cas de tests
#### 1. [*testRemoveConsecutiveDuplicatesDupesOnlyAfterEnd()*](core/src/test/java/com/graphhopper/util/ArrayUtilExtraTest.java) 
##### Intention:
Tester la méthode [*ArrayUtil.removeConsecutiveDuplicates(int[] arr, int end)*](core/src/main/java/com/graphhopper/util/ArrayUtil.java)
sur un tableau qui contient des doublons consécutifs seulement à partir de l'index correspondant
au paramètre *end*.
##### Motivation des données choisies:
Données:  
arr = {0, 1, 2, 3, 4, 7, 7, 7}  
end = 5  
Le tableau en argument contient des doublons consécutifs (trois 7) seulement à partir de l'index 5,
qui est aussi la valeur de l'argument *end*.
#### Explication de l'oracle:
La méthode *ArrayUtil.removeConsecutiveDuplicates()* retire les doublons consécutifs entre 0 et *end* exclusivement.
Les doublons (les trois 7) se trouvent seulement à partir de l'index *end* (5), donc ils ne devraient pas être
retiré du tableau. Le tableau devrait donc demeurer inchangé après l'appel de *ArrayUtil.removeConsecutiveDuplicates()*
Comme il n'y a aucun élément qui a été retiré entre 0 et *end* (5), il est garanti qu'il n'y pas de doublon
dans cet intervalle. Donc, l'appel retourne la fin de cet intervalle (5).


#### 2. [*testTransformMapTooSmall()*](core/src/test/java/com/graphhopper/util/ArrayUtilExtraTest.java)
##### Intention:
Tester la méthode [*ArrayUtil.transform(IntIndexedContainer arr, IntIndexedContainer map)*](core/src/main/java/com/graphhopper/util/ArrayUtil.java)
avec un argument *map* dont la taille - 1 est inférieure à une des valeur de l'argument *arr*. 
##### Motivation des données choisies:
Données:  
arr = [1, 2, 3, 4, 9, 3]  
map = [10, 11, 12, 13, 14]  
Le tableau arr contient un nombre (9) supérieur à taille de *map* (5) - 1 (4).
#### Explication de l'oracle:
Le java doc de la méthode décrit que chaque element x de *arr* est remplacé par *map*[x].
Cependant, *map* est de taille 5, donc son index maximal est 4. Ainsi, *map*[9] ne fait pas de sens.
L'appel devrait donc renvoyer une exception d'argument illégal.

#### 3. [*testMergeSameElems()*](core/src/test/java/com/graphhopper/util/ArrayUtilExtraTest.java)
##### Intention:
Tester la méthode [*ArrayUtil.merge(int[] a, int[] b)*](core/src/main/java/com/graphhopper/util/ArrayUtil.java)
avec deux tableaux qui contiennent les mêmes éléments dans le même ordre.
##### Motivation des données choisies:
Données:  
a = {-2, -1, 0, 1, 2}  
b = {-2, -1, 0, 1, 2}  
Les deux tableaux sont triés de façon croissante et contiennent les mêmes éléments dans
le même ordre.
#### Explication de l'oracle:
Selon le javadoc, la méthode doit retourner un tableau trié contenant les éléments des deux tableaux
en argument et les doublons doivent avoir été enlevés. Comme les deux tableaux contiennent les mêmes
éléments et sont triés, le tableau retourné devrait contenir les mêmes éléments dans le même ordre.

#### 4. [*testCalcDist3DNormalXYZ()*](core/src/test/java/com/graphhopper/util/DistanceCalcEuclideanExtraTest.java)
##### Intention:
Tester la méthode [*DistanceCalcEuclidean.calcDist3D(double fromY, double fromX, double fromHeight, double toY, double toX, double toHeight)*](core/src/main/java/com/graphhopper/util/DistanceCalcEuclidean.java)
avec des valeurs normales de coordonnées x, y, z (height), c'est-à-dire que chaque delta de direction est non nul.
##### Motivation des données choisies:
Les arguments ont été choisies de sorte que pour chaque direction,
*to{direction}* - *from{direction}* != 0.
#### Explication de l'oracle:
La valeur retournée attendue est une approximation du résultat du calcul sqrt((5-1)^2 + (7-(-3))^2 + (8-2)^2):
https://www.wolframalpha.com/input?i=sqrt%28%285-1%29%5E2+%2B+%287-%28-3%29%29%5E2+%2B+%288-2%29%5E2%29.
La marge d'erreur 1e-6 a été choisie, car c'est cela qui a été choisie pour un autre cas de test de
la méthode. On suppose que cette valeur est donc l'erreur acceptable pour cette méthode en général.

#### 5. [*testIntermediatePointNormalInputs()*](core/src/test/java/com/graphhopper/util/DistanceCalcEuclideanExtraTest.java)
##### Intention:
Tester la méthode [*DistanceCalcEuclidean.intermediatePoint(double f, double lat1, double lon1, double lat2, double lon2)*](core/src/main/java/com/graphhopper/util/DistanceCalcEuclidean.java)
avec des valeurs normales de latitudes et longitudes,
c'est-à-dire que chaque delta de latitude et longitude est non nul.
##### Motivation des données choisies:
Les arguments ont été choisies de sorte que pour chaque coordonnée,
*{coordonnée}2* - *{coordonnée}1* != 0.
#### Explication de l'oracle:
Les valeurs [*lat*, *long*] du GHPoint retourné sont les résultats du calcul [1+(11-1)*0.7, 3+(8-3)*0.7]:
https://www.wolframalpha.com/input?i2d=true&i=%7B%7B1%2B%5C%2840%2911-1%5C%2841%29*0.7%7D%2C%7B3%2B%5C%2840%298-3%5C%2841%29*0.7%7D%7D.

#### 6. [*testPruneFileEndTwoDots()*](web-api/src/test/java/com/graphhopper/util/HelperExtraTest.java)
##### Intention:
Tester la méthode [*Helper.pruneFileEnd(String file)*](web-api/src/main/java/com/graphhopper/util/Helper.java)
sur un nom de fichier contenant deux ".".
##### Motivation des données choisies:
Données:
file = "c_program.c.txt"
*file* contient deux ".".
#### Explication de l'oracle:
La méthode doit retourner une chaîne qui est identique à la sous-chaîne avant le deuxième "." dans *file*,
donc l'appel doit retourner "c_program.c".

#### 7. [*testKeepInExceedsMax()*](web-api/src/test/java/com/graphhopper/util/HelperExtraTest.java)
##### Intention:
Tester la méthode [*Helper.keepIn(double value, double min, double max)*](web-api/src/main/java/com/graphhopper/util/Helper.java)
avec *value* supérieur à *max*.
##### Motivation des données choisies:
Données:
value = 9  
min = 3  
max = 5  
*value* (9) > *max* (5)
#### Explication de l'oracle:
La méthode retourne la valeur *max* si *value* est supérieur à *max*. Ici, c'est le cas, donc
l'appel retourne *max* (5).

### Mutation
Les fichier HTML permettant de voir les rapports de mutations se trouvent dans le
répertoire [mutations](mutations).
#### Score de mutation avant les nouveaux tests
* ArrayUtil, DistanceCalcEuclidean: 73%
* Helper: 25%
#### Score de mutation après les nouveaux tests
* ArrayUtil, DistanceCalcEuclidean: 82%
* Helper: 26%
#### Analyse des mutants
Le score de mutation a augmenté, donc les nouveaux tests ont découvert des mutants.
Beaucoup trop de mutants ont été détectés pour qu'on puisse tous les analyser.
On va analyser 2 de ces mutants, car l'énoncé demande d'ajouter des tests jusqu'à
obtenir 2 nouveaux mutants si on en avait pas trouvé.
1. ArrayUtil.java ligne 244: Replaced integer addition with subtraction  
La mutation se produit sur une condition dans la méthode ArrayUtilMerge().
La conditionnelle non mutée vérifie que l'addition de la taille de a avec celle de b égale 0 et 
retourne un tableau vide si c'est le cas. La conditionnelle mutée vérifie que la soustraction
de la taille de a avec celle de b égale 0 et retourne un tableau vide si c'est le cas. 
La condition non mutée est satisfaite seulement lorsqu'il y a deux tableaux vides.
La condition mutée est satisfaite seulement lorsqu'il a deux tableaux de même taille.
Parmi les tests (assert) originaux seulement un d'eux (le test avec deux tableaux vides)
dépend sur la nature de la condition. C'est donc seulement ce test qui a le potentiel de
causer un échec si la condition est muté.
Cependant, avec mutation ou non, l'appel avec deux tableaux vides (et donc deux tableaux de même taille)
retourne un tableau vide dans les deux cas, ce qui est le résultat attendu.
Donc, le test passe dans les deux cas. Aucun des tests échoue avec l'introduction du mutant.
Le test [*testMergeSameElems()*](core/src/test/java/com/graphhopper/util/ArrayUtilExtraTest.java)
qu'on a ajouté teste deux tableaux de même taille, mais elles ne sont pas vides.
L'appel ne retourne donc pas un tableau vide avec la condition non mutée, mais il reoturne un tableau
vide avec la condition mutée. Le mutant cause donc un échec du test. Ainsi, on parvient à détecter
un nouveau mutant.
2. Deuxième mutant détecté...
