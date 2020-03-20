package gui.myswinggui;

import controller.interfaces.ProcessInterface;
import gui.interfaces.GetInfoInterface;

import java.awt.*;

public class HelpForm extends InfoForm implements ProcessInterface {
    private final GetInfoInterface info;

    public HelpForm(GetInfoInterface info) {
        this.info = info;

    }

    @Override
    public boolean write() {
        ///TODO adding a picture
        if (info == null) return false;

        StringBuilder builder = new StringBuilder();
        for (String s : info.getInfo()) {
            builder.append(s).append("\n");
        }
        textArea1.setEditable(true);
        textArea1.setText(builder.toString());
        textArea1.setEditable(false);
        this.pack();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (getWidth() / 2);
        int y = (screenSize.height / 2) - (getHeight() / 2);
        setLocation(x, y);
        this.setVisible(true);
//        this.update(this.getGraphics());
        return true;
    }
}
