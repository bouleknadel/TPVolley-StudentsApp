# Application Android de Gestion des Étudiants

Cette application Android est conçue pour gérer les informations des étudiants. Elle permet aux utilisateurs d'ajouter de nouveaux étudiants, de visualiser une liste d'étudiants et d'effectuer diverses opérations telles que la mise à jour, la suppression des d'étudiants, ainsi que le filtrage par tous les champs des étudiants

## Fonctionnalités

1. *Ajouter de nouveaux étudiants* : Les utilisateurs peuvent ajouter de nouveaux étudiants avec des détails tels que le nom, le prénom, la ville, le sexe et la photo de profil.
2. *Voir la liste des étudiants* : L'application affiche une liste de tous les étudiants avec leurs informations de base.
3. *Fonctionnalité de recherche* : Les utilisateurs peuvent rechercher des étudiants par nom , prénom, le sexe ou la ville.
4. *Modifier les informations des étudiants* : L'application permet de mettre à jour les détails des étudiants.
5. *Supprimer des étudiants* : Les utilisateurs peuvent supprimer des étudiants du système.
6. *Tiroir de navigation* : ermet une navigation facile entre les différentes sections de l'application en utilisant le composant ViewPager2 . Ce composant permet aux utilisateurs de naviguer entre les pages de l'application de manière fluide.

## Détails techniques

- *Communication backend* : L'application utilise la bibliothèque Volley pour communiquer avec un backend Spring (Java).
- *Gestion des images* : ImageView est utilisé pour afficher les photos de profil, et l'application peut sélectionner des images depuis la galerie de l'appareil.
- *Persistance des données* : Les données des étudiants sont stockées et récupérées depuis un serveur via des appels API REST.
- *Composants UI* : L'application utilise divers composants UI Android tels que ListView, SearchView, AlertDialog et ViewPager2.

## Activités

1. *SplashActivity* : ´Ecran de d´emarrage avec animation.
2. *MainActivity2* : Interface principale de l’application.
3. *AddEtudiantFragment* : Fragment pour ajouter un nouvel ´etudian.
4. *ListeEtudiantFragment* : Fragment affichant la liste des ´etudiants, permettant d’interagir avec les ´el´ements de la liste .

## Configuration

1. Assurez-vous d'avoir Android Studio installé.
2. Clonez le dépôt ou téléchargez le code source.
3. Ouvrez le projet dans Android Studio.
4. Exécutez l'application sur un émulateur ou un appareil physique.
   https://github.com/bouleknadel/TPVolley-StudentsApp.git
   
## Prerequisites
- Android Studio version 2023.3.1
- JDK version 22.0.1


## Dépendances

- Volley : Pour les requêtes réseau
- Gson : Pour l'analyse JSON
- glide: Pour les images .

## Note
Cette application suppose qu'un serveur backend est configuré pour gérer les opérations CRUD pour les données des étudiants. Assurez-vous que votre serveur est correctement configuré et accessible avant d'exécuter l'application.

## Screen Video
[![Vidéo de démonstration]](https://github.com/user-attachments/assets/59d83e56-230c-4f3e-8587-bed4c05c50e6)
## Contributors
- BOULEKNADEL Abderrahmane https://github.com/bouleknadel
