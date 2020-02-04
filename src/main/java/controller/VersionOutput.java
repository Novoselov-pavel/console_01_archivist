package controller;

import gui.Archivist;

public class VersionOutput implements ProcessInterface {

    @Override
    public boolean write () {
        System.out.println(Archivist.getVersionID());
        return true;
    }

}
