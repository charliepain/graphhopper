# Documentation tâche 3

## Contexte

Pour garantir que les contributions ne fassent pas régresser le score de mutation PIT, nous avons enrichi le workflow GitHub Actions `Run New Tests`. L’objectif est de comparer la couverture mutationnelle du commit courant avec celle du commit précédent et d’échouer la build si le score diminue.

## Choix d’implémentation

- **Scripts Python très simples** : les nouveaux scripts `.github/scripts/extract_mutation_score.py` et `.github/scripts/compare_mutation_scores.py` se limitent à la bibliothèque standard (`os`, `re`, `sys`). Ils contiennent des messages d’erreur explicites, un format d’appel minimal et aucune annotation avancée. Ce style volontairement simple rend le code plus accessible à un membre d’équipe peu expérimenté.
- **Comparaison séquentielle des commits** : le job exécute PIT sur le commit courant, puis fait un `git checkout` du commit précédent, relance PIT et revient au commit initial. Cette approche évite l’usage des worktrees ou d’astuces Bash moins familières et reste facile à suivre.
- **Tolérance aux premiers commits** : si l’historique ne contient pas de commit précédent, le script affiche un message et saute la comparaison pour ne pas bloquer une première contribution.
- **Seuil de comparaison** : la comparaison autorise une marge flottante minimale (`1e-6`) afin d’éviter les échecs causés par des arrondis.
- **Validation** : Pour valider que ces changements au workflow ont fonctionné, on a mis en commentaire des tests de la tâche 2 documentés à faire augmenter le score de mutation. On a confirmé qu'un tel changement cause un échec de workflow lors d'un push, et ce, pour chaque module modifié lors de la tâche 2 (`core` et `web-api`). Après avoir réinclut ces tests en les décommentant, le workflow du prochain push ne lance pas d'erreur comme attendu. De plus, les scripts imprime des messages indiquant le score de mutation pour le commit précédent et courant pour chacun des modules. Ces scores ont été comparés avec les vrais scores indiqués dans les rapports générés par pitest à chaque changement pour valider la cohérence.



