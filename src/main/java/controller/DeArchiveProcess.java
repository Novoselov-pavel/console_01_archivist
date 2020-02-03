package controller;

import gui.Archivist;
import model.FileItem;
import model.IniClass;
import model.Settings;

import java.io.File;
import java.util.List;

public class DeArchiveProcess {
    private Settings settings = Archivist.getSettings();
    private IniClass iniClass;

    public DeArchiveProcess() {
        load();
    }

    public DeArchiveProcess(Settings settings) {
        this.settings = settings;
        load();
    }

    private void load() {
//        IniFileItem fileItem = new IniFileItem(settings.getInputPath());  // TODO need check
//        iniClass = new IniClass(null,fileItem);
//        iniClass.loadFromFile();
    }

    public boolean write() {
//        String
//
//        List<FileItem> itemList = iniClass.getItemList();
//        FileItem zip = searchZIP(itemList);
//        zip.getEndFile();
//
//        //TODO
        return false;
    }

    private FileItem searchZIP(List<FileItem> list) {
        for (FileItem item : list) {
                if (item.isExitFile()) return item;
        }
        return null;
    }

}

