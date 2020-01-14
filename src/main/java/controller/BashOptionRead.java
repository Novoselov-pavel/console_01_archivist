package controller;

import exception.InvalidBashOption;
import model.BashOption;
import model.Settings;

import java.util.Arrays;
import java.util.LinkedList;

public class BashOptionRead {

    private String[] argument;

    public BashOptionRead(String[] argument) {

        this.argument = argument;
    }

    public Settings getSettings() throws InvalidBashOption {
        if (argument==null)
            throw  new InvalidBashOption();
        LinkedList<String> queue = new LinkedList<>(Arrays.asList(argument));
        LinkedList<BashOption> options = new LinkedList<>();
        String inputPath = null;
        String outputPath =null;


        while (BashOption.getOptionFromString(queue.peekFirst())!=null) {
            options.add(BashOption.getOptionFromString(queue.pollFirst()));
        }
        inputPath = queue.pollFirst();
        outputPath = queue.pollFirst();
        Settings settings = new Settings(options,inputPath,outputPath);
        return settings;
    }

}
