import client.ClientRMI;
import dao.MessagerieDao;
import dao.ModuleDao;
import dao.RessourceDao;
import dao.UserDao;
import metier.Classe;
import metier.Messagerie;
import metier.Module;
import metier.Ressource;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /* List<Module> modules = ModuleDao.getAllModulesByClasse(1);

        for(Module m: modules)
            System.out.println(m.getLibelle());

        int idUser = ModuleDao.getModuleByName("Concept UML").getTeacher_id();
        System.out.println(UserDao.getUserById(idUser).getNom());

        List<Ressource> ressources = RessourceDao.getRessourcesByModuleId(1);
        for(Ressource r: ressources)
            System.out.println(r.getRessource_name());

        List<Messagerie> messagerieList = MessagerieDao.getMessagesByUserId(3,4);
        for(Messagerie m : messagerieList){
            System.out.println("content : "+m.getContent());
            System.out.println("etat :  "+m.getEtat());
        }

*/

        for(String s : ModuleDao.getAllStudentByTeacher(2)){
           // String part[] =  s.split(":");
            System.out.println(Integer.parseInt(s));
        }
    }

}