# Documentation tâche 3

## Contexte

Pour garantir que les contributions ne fassent pas régresser le score de mutation PIT, nous avons enrichi le workflow GitHub Actions `Run New Tests`. L’objectif est de comparer la couverture mutationnelle du commit courant avec celle du commit précédent et d’échouer la build si le score diminue.

## Choix d’implémentation

- **Scripts Python très simples** : les nouveaux scripts `.github/scripts/extract_mutation_score.py` et `.github/scripts/compare_mutation_scores.py` se limitent à la bibliothèque standard (`os`, `re`, `sys`). Ils contiennent des messages d’erreur explicites, un format d’appel minimal et aucune annotation avancée. Ce style volontairement simple rend le code plus accessible à un membre d’équipe peu expérimenté.
- **Comparaison séquentielle des commits** : le job exécute PIT sur le commit courant, puis fait un `git checkout` du commit précédent, relance PIT et revient au commit initial. Cette approche évite l’usage des worktrees ou d’astuces Bash moins familières et reste facile à suivre.
- **Tolérance aux premiers commits** : si l’historique ne contient pas de commit précédent, le script affiche un message et saute la comparaison pour ne pas bloquer une première contribution.
- **Seuil de comparaison** : la comparaison autorise une marge flottante minimale (`1e-6`) afin d’éviter les échecs causés par des arrondis.
- **Validation** : Pour valider que ces changements au workflow ont fonctionné, on a mis en commentaire des tests de la tâche 2 documentés à faire augmenter le score de mutation. On a confirmé qu'un tel changement cause un échec de workflow lors d'un push, et ce, pour chaque module modifié lors de la tâche 2 (`core` et `web-api`). Après avoir réinclut ces tests en les décommentant, le workflow du prochain push ne lance pas d'erreur comme attendu. De plus, les scripts imprime des messages indiquant le score de mutation pour le commit précédent et courant pour chacun des modules. Ces scores ont été comparés avec les vrais scores indiqués dans les rapports générés par pitest à chaque changement pour valider la cohérence.
### Exemple d'échec du workflow:
Lien au commit associé: https://github.com/charliepain/graphhopper/commit/90fb55280b0e791461d04b2dcf6dc62ae78f7870  
Lien au workflow : https://github.com/charliepain/graphhopper/actions/runs/19485028991/job/55765315230
<img title="a title" alt="Alt text" src="tache3_images/workflow_fail.png">

## Documentation des tests avec mocks (ArrayUtilMockTest)

### Choix des classes testées

**Classe testée : `ArrayUtil`**

La classe `ArrayUtil` a été choisie pour les tests avec mocks pour les raisons suivantes :

1. **Dépendance à une source de non-déterminisme** : `ArrayUtil` contient des méthodes (`shuffle` et `permutation`) qui dépendent de la classe `Random` de Java. Cette dépendance introduit un comportement non-déterministe qui complique les tests unitaires traditionnels.

2. **Méthodes à tester** : Les méthodes `shuffle` et `permutation` sont des opérations fondamentales pour la manipulation de listes. Elles sont utilisées dans plusieurs contextes du projet GraphHopper et méritent une validation rigoureuse.

3. **Besoin de tests déterministes** : Contrairement aux tests existants dans `ArrayUtilTest` qui utilisent un `Random` réel avec une graine fixe (lignes 97-98), les tests avec mocks permettent de contrôler précisément les valeurs retournées et de vérifier le comportement exact de l'algorithme.

4. **Couverture de mutation** : Les méthodes utilisant `Random` sont difficiles à tester exhaustivement sans mocks, car elles produisent des résultats différents à chaque exécution. Les mocks permettent de créer des scénarios de test reproductibles qui améliorent la couverture de mutation.

### Choix des classes simulées

**Classe simulée : `java.util.Random`**

La classe `Random` a été choisie pour être simulée (mockée) pour les raisons suivantes :

1. **Source de non-déterminisme** : `Random` produit des valeurs différentes à chaque appel, rendant les tests non-reproductibles. En mockant cette classe, nous pouvons contrôler exactement quelles valeurs sont retournées, rendant les tests déterministes.

2. **Dépendance externe** : `Random` est une dépendance externe au code métier de `ArrayUtil`. Selon les principes de test unitaire, les dépendances externes doivent être isolées pour tester uniquement la logique de la classe sous test.

3. **Facilité de vérification** : Mockito permet de vérifier que `Random.nextInt()` est appelé le bon nombre de fois avec les bons paramètres, ce qui valide que l'algorithme de `shuffle` utilise correctement le générateur aléatoire.

4. **Analyse de l'algorithme** : En examinant le code de `ArrayUtil.shuffle()` (lignes 116-125), on observe que la méthode appelle `random.nextInt(maxHalf)` où `maxHalf = list.size() / 2`. Pour une liste de taille 10, cela signifie 5 appels à `nextInt(5)`. Le mock permet de valider ce comportement exact.

### Définition des mocks

**Configuration du mock :**

```java
@ExtendWith(MockitoExtension.class)
public class ArrayUtilMockTest {
    @Mock
    private Random mockRandom;
}
```

**Justification de la configuration :**

1. **Annotation `@ExtendWith(MockitoExtension.class)`** : Cette annotation active l'intégration de Mockito avec JUnit 5, permettant l'injection automatique des mocks annotés avec `@Mock`.

2. **Annotation `@Mock`** : Cette annotation crée automatiquement un mock de la classe `Random`. Le mock est créé avant chaque test et réinitialisé, garantissant l'isolation entre les tests.

3. **Avantages de cette approche** :
   - **Simplicité** : Pas besoin de créer manuellement le mock avec `Mockito.mock(Random.class)`
   - **Lisibilité** : L'intention est claire grâce à l'annotation
   - **Maintenance** : Si la signature de `Random` change, Mockito détectera les problèmes au moment de la compilation

**Configuration des comportements du mock :**

Dans les tests, le comportement du mock est défini avec `when().thenReturn()` :

```java
when(mockRandom.nextInt(5)).thenReturn(2, 1, 3, 0, 2);
```

Cette configuration spécifie que lorsque `nextInt(5)` est appelé, le mock retournera successivement les valeurs 2, 1, 3, 0, et 2.

### Choix des valeurs simulées

#### Test `testShuffleWithMockedRandom()`

**Valeurs choisies :** `2, 1, 3, 0, 2`

**Justification :**

1. **Analyse de l'algorithme `shuffle`** :
   - Pour une liste de taille 10, `maxHalf = 10 / 2 = 5`
   - La boucle itère 5 fois (pour `x1` de 0 à 4)
   - À chaque itération, `x2 = random.nextInt(5) + 5`, donc `x2` est dans l'intervalle [5, 9]
   - Les valeurs retournées (2, 1, 3, 0, 2) produisent donc les indices : 7, 6, 8, 5, 7

2. **Couverture des cas** :
   - **Valeurs variées** : Les valeurs 0, 1, 2, 3 couvrent différentes positions dans la seconde moitié de la liste
   - **Valeur répétée** : La valeur 2 apparaît deux fois, testant le cas où le même indice est sélectionné plusieurs fois
   - **Valeurs aux extrémités** : 0 (première position de la seconde moitié) et 3 (proche de la fin) testent les cas limites

3. **Nombre d'appels** : Exactement 5 valeurs sont fournies, correspondant au nombre d'itérations de la boucle (`maxHalf = 5`)

4. **Vérification** : Le test vérifie que `nextInt(5)` est appelé exactement 5 fois avec `verify(mockRandom, times(5)).nextInt(5)`, validant que l'algorithme utilise correctement le générateur aléatoire.

#### Test `testPermutationWithMockedRandom()`

**Valeurs choisies :** `1, 0, 2, 1, 0`

**Justification :**

1. **Relation avec `shuffle`** : La méthode `permutation` appelle `shuffle` en interne (ligne 82 de `ArrayUtil.java`). Les mêmes valeurs de mock sont nécessaires car `permutation(10, mockRandom)` crée une liste de taille 10 et la mélange.

2. **Valeurs différentes du premier test** : Des valeurs différentes (1, 0, 2, 1, 0) sont utilisées pour :
   - Démontrer que les tests sont indépendants
   - Tester un scénario différent de mélange
   - Valider que le mock fonctionne correctement avec différentes séquences

3. **Vérification de la permutation** : Le test vérifie que le résultat est une permutation valide avec `ArrayUtil.isPermutation(result)`, ce qui garantit que :
   - Tous les nombres de 0 à 9 sont présents
   - Aucun nombre n'est dupliqué
   - Aucun nombre n'est hors de la plage [0, 9]

4. **Vérification de l'utilisation** : Le test utilise `verify(mockRandom, atLeastOnce()).nextInt(anyInt())` pour s'assurer que le générateur aléatoire est bien utilisé, sans spécifier le nombre exact d'appels (car cela dépend de l'implémentation interne de `shuffle`).

### Résumé des justifications

| Aspect | Choix | Justification principale |
|--------|-------|-------------------------|
| **Classe testée** | `ArrayUtil` | Dépendance à `Random` nécessitant des tests déterministes |
| **Classe mockée** | `Random` | Source de non-déterminisme à contrôler |
| **Configuration mock** | `@Mock` avec `MockitoExtension` | Simplicité et intégration JUnit 5 |
| **Valeurs test 1** | `2, 1, 3, 0, 2` | Couverture variée avec cas limites et répétitions |
| **Valeurs test 2** | `1, 0, 2, 1, 0` | Scénario alternatif pour validation indépendante |

Ces choix permettent de créer des tests unitaires déterministes, reproductibles et maintenables qui améliorent significativement la couverture de mutation des méthodes dépendantes de sources aléatoires.


