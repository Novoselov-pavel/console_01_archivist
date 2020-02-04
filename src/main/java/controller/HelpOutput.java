package controller;

import model.Help;

public class HelpOutput implements ProcessInterface {

    @Override
    public boolean write () {
        String[] strings = Help.getHelp();
        for (String string : strings) {
            System.out.println(string);
        }
        return true;
    }

}
