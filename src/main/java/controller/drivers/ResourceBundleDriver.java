package controller.drivers;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleDriver {
    private final ResourceBundle resourceBundle;

    public ResourceBundleDriver(String name) {
        Locale currentLocale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(name, currentLocale);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**Set localization for components instance  of {@link AbstractButton}from {@link ResourceBundle}
     *
     * @param input component from each (down hierarchy) localization has set
     */
    public void setLocalization(Component input) {
        if (input instanceof Container) {
            for (Component component : ((Container) input).getComponents()) {
                if (component instanceof AbstractButton) {
                    try {
                        ((AbstractButton) component).setText(resourceBundle.getString(((AbstractButton) component).getText()));
                    } catch (Exception ex) { }
                }
                setLocalization(component);
            }
        }
    }

    ///TODO localisation for message
}
