package controller.interfacesImplementation;

import controller.interfaces.ProcessInterface;
import gui.GetInfoInterface;

public class InfoOutput implements ProcessInterface {
    GetInfoInterface infoInterface;

    public InfoOutput(GetInfoInterface infoInterface) {
        this.infoInterface = infoInterface;
    }


    @Override
    public boolean write() {
        for (String s : infoInterface.getInfo()) {
            System.out.println(s);
        }
        return true;
    }
}
