# ProjectGraphMCS

Projet de Minimum Cut Set appliqué sur la ville de LAVAL

Ce projet analyse et extrait les informations contenues dans un fichier JSON pour créer un graphe du réseau routier de la ville de Laval.
On applique ensuite l'algorithme de Ford Fulkerson sur le graphe afin de récupérer le flux maximal.
De cette information, on calcul une coupe minimale et on affiche les différents postes de surveillance à établir pour contrôler tous les
accès au centre de la ville.

Le résultat est enregistré dans le fichier visualisation.html, qui permet l'affichage concrès des résultats.

## Lancement

Pour lancer le projet, il faut ajouter la dépendance org.json
