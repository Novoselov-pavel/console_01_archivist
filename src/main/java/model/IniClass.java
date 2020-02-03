package model;

import controller.GetCrc32;
import gui.Archivist;

import java.io.*;
import java.util.List;
import java.util.TreeMap;

public class IniClass implements Serializable {
    private static final long serialVersionUID = 202001251002L;
    private transient final String PREFIX = "INI file for Archivist";
    private transient FileItem iniFileItem;
    private List<FileItem> itemList;



    public IniClass(List<FileItem> list, FileItem iniFileItem) {
        this.itemList = list;
        this.iniFileItem = iniFileItem;
    }

    public void storeToFile() {
        try(FileOutputStream stream = new FileOutputStream(iniFileItem.getFullFileName())) {
            storeToFile(stream);

        } catch (IOException e) {
            Archivist.exitProgramm(2, e.getMessage());
        }
    }

    public void loadFromFile() {
        try(FileInputStream stream = new FileInputStream(iniFileItem.getFullFileName())) {
            loadFromFile(stream);
        } catch (IOException | ClassNotFoundException e) {
            Archivist.exitProgramm(2, e.getMessage());
        }
    }

    public List<FileItem> getItemList() {
        return itemList;
    }

    private void storeToFile(OutputStream out) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(out);
        stream.writeObject(this);
        stream.flush();
    }

    private void loadFromFile(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(in);
        IniClass newIniClass = (IniClass)stream.readObject();
        this.itemList = newIniClass.getItemList();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(PREFIX);
        out.writeObject(itemList);
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.readObject();
        this.itemList=(List<FileItem>) in.readObject();
    }



}