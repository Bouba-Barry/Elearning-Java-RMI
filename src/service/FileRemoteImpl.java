package service;

import dao.RessourceDao;
import metier.Ressource;

import java.io.*;
import java.nio.file.Files;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class FileRemoteImpl extends UnicastRemoteObject implements FileRemote {

    public FileRemoteImpl() throws RemoteException {}

    @Override
    public void sendFile(String fileName) throws RemoteException {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            if(fileName.contains(".txt")){
                // uploader du binaire
            }
            else{
                // uploader du text
            }
           // fos.write(data, 0, len);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void uploadBinaryFile(String fileName, String destination) throws IOException {
        BufferedInputStream fichier = new BufferedInputStream(new FileInputStream(fileName));

        BufferedOutputStream sortie = new BufferedOutputStream(new FileOutputStream(destination));
        byte buffer[] = new byte[1024];
        int taille = 0;
        while((taille = fichier.read(buffer)) != -1 ){
            sortie.write(buffer, 0, taille);
        }
        // libérer les rsrce une fois traitement terminé ....
        sortie.close();
        fichier.close();
    }

    @Override
    public boolean uploadTxtFile(String fileName, String desc,int module_id ,String addBy,File file) throws RemoteException {
        try{
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileContent = content.toString();
            Ressource ressource = new Ressource(desc,addBy,module_id,fileName,fileContent);
            return RessourceDao.addText(ressource);
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    @Override
    public boolean uploadBinaryFile(String fileName, String desc,int module_id ,String addBy,File file) throws RemoteException {
       try{
           FileInputStream fileInputStream = new FileInputStream(file);
           byte [] data = new byte[fileInputStream.available()];
           fileInputStream.read(data);

           Ressource ressource = new Ressource(fileName,desc,addBy,module_id,data);
           return RessourceDao.addBinary(ressource);
       }catch (Exception e){e.printStackTrace();}
       return false;
    }

    @Override
    public void receiveFileBinary(String fileName, File dest) throws RemoteException {
        Ressource ressource = RessourceDao.getRessourceByName(fileName);
        if(ressource.getData() != null){
            try{
                FileOutputStream fos = new FileOutputStream(dest);
                fos.write(ressource.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public List<Ressource> getAllFileFromModule(int module_id) throws RemoteException {
        List<Ressource> ressources = RessourceDao.getRessourcesByModuleId(module_id);
        return ressources;
    }

    @Override
    public void receiveTxtFile(String fileName, File file) throws RemoteException{
        Ressource ressource = RessourceDao.getRessourceByName(fileName);
        if (ressource.getText_data() != null) {
            try {
                Files.write(file.toPath(), ressource.getText_data().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
