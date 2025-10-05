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
avec un argument *map* de taille inférieure à (valeur maximale dans *arr*) - 1.
##### Motivation des données choisies:
Données:  


#### Explication de l'oracle:

