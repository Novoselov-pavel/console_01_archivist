package controller;

import gui.Archivist;

public class VersionOutput {

    public static void write () {
        System.out.println(Archivist.getVersionID());
    }

}
