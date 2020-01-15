package controller;

import model.Help;

public class HelpOutput {

    public static void write () {
        String[] strings = Help.getHelp();
        for (String string : strings) {
            System.out.println(string);
        }
    }

}
