package controller;

import exception.InvalidBashOption;
import model.BashOption;
import model.Settings;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BashOptionReadTest {

    @Test
    void getSettings() {
        String[] input1 = new String[1];
        input1[0] = "-h";

        String[] input2 = new String[1];
        input2[0] = "--version";


        String[] input3= new String[3];
        input3[0] = "-d";
        input3[1] = "/a/0/1";
        input3[2] = "/a/0/3";

        String[] input4 = new String[1];
        input4[0] = "-a";

        String[] input5 = null;

        String[] input6= new String[3];
        input6[0] = "-d";
        input6[1] = "/a/0/1";
        input6[2] = null;


        BashOptionRead bashOptionRead1 = new BashOptionRead(input1);
        BashOptionRead bashOptionRead2 = new BashOptionRead(input2);
        BashOptionRead bashOptionRead3 = new BashOptionRead(input3);
        BashOptionRead bashOptionRead4 = new BashOptionRead(input4);
        BashOptionRead bashOptionRead5 = new BashOptionRead(input5);
        BashOptionRead bashOptionRead6 = new BashOptionRead(input6);

        try {
            Settings settings1 = bashOptionRead1.getSettings();
            Settings settings2 = bashOptionRead2.getSettings();
            Settings settings3 = bashOptionRead3.getSettings();

            LinkedList<BashOption> set1 = new LinkedList<>();
            set1.add(BashOption.HELP);

            LinkedList<BashOption> set2 = new LinkedList<>();
            set2.add(BashOption.VERSION);

            LinkedList<BashOption> set3 = new LinkedList<>();
            set3.add(BashOption.DEARCHIVE);

            Settings checkSettings1 = new Settings(set1,null,null);
            Settings checkSettings2 = new Settings(set2,null,null);
            Settings checkSettings3 = new Settings(set3,input3[1],input3[2]);

            boolean check1, check2, check3;
            check1 = settings1.equals(checkSettings1);
            check2 = settings2.equals(checkSettings2);
            check3 = settings3.equals(checkSettings3);

            assertTrue(check1&check2&check3);

        } catch (InvalidBashOption ex) {
            fail();
        } catch (UnsupportedEncodingException e) {
            fail();
        }


        try {
            Settings settings4 = bashOptionRead4.getSettings();
            Settings settings5 = bashOptionRead5.getSettings();
            Settings settings6 = bashOptionRead6.getSettings();

        } catch (InvalidBashOption ex) {
            assertTrue(true);
        }




    }
}