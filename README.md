                                                Elearning-Java-RMI


Etape 1: Description Du Projet
        
        Ce projet est Une plateforme de E-Learning developpé en java Dans le cadre de l'apprentissage dans un module programmation concurrente.
        à l'aide des technologies Java comme RMI ( RMI qui est utilisé pour créer des applications distribuées, il fournit une communication à distance             entre les programmes Java )
        Ensuite pour l'interface graphique de l'application , JavaFX a été utilisé pour illistré les interactions .
        Dans ce projet principalement va permettre à trois acteurs, un etudiant , un apprenant et un admin d'interagir à fin de se partager au sein d'une           classe des ressources pédagiques comme, des fichiers de cours, ou vidéo, s'envoyer des messages et au prof de partager un tableau blanc entre ces           étudiants.
        
 Etape 2: Comment Lancer L'application
          Pour Lancer cet Application et avoir les fonctionnalités dans votre machine, il vous d'abord avoir:
                                        
                                        Avoir les Outils nécessaires, nous avons:
          1 : Un environnement de developpement comme vs code, eclipse, intelliJ dans le quel est conçu l'application en question
          2 : Avoir le JDK 8 pour pouvoir lancer les interfaces graphiques de javaFX.
          3 : Avoir la librairie Mysql-Connector pour la connectivité avec base de donnée.
          4 : Dans le dossier ressource/EnregistrementDB se trouve un fichier .sql contenant déjà quelques lignes que vous pouvez Utiliser.
          5 : Dans le package src/connectionDB , se trouve une classe pour permettre l'accès il faudra mettre les informations de votre base de donnée
                                           
                                        Lancement de L'application : 
           1: Dans le package src/server se trouve un fichier ServerRMI qui contient une méthode main, vous lancer d'abord ce fichier.
           2: Dans le package src/gui se trouve un fichier ClienUI que vous pouvez lancer pour avoir une fenetre graphique.
           
           
Etape 3: L'utilisation
          
          Pour utiliser l'application, connectez-vous à l'aide des emails et passwords dans le fichier .sql que j'ai cité ci-dessus.
          Ensuite vous pouvez voir si vous êtes etudiants, vous serez rédirigez vers l'accueil etudiant. si vous êtes enseignant, vous serez redirigez vers
          l'accueil enseigant et de même pour l'admin.

                            
        
