package model;

import java.io.UnsupportedEncodingException;

/**Option for command line, short and long version
 *
 */
public enum BashOption {

    HELP("-h","--help"), VERSION("-v","--version"), ARCHIVE("-a","--archive"), DEARCHIVE("-d", "--dearchive");


    private String[] stringOption;

    BashOption(String... stringOption) {
        if (stringOption==null)
            throw new IllegalArgumentException("enum BashOption does not correct initialise");
        this.stringOption = stringOption;
    }


    /**Return BashOption correspond of {@param value} or null     *
     * @param value
     * @return
     */
    public static BashOption getOptionFromString(String value) {
        if (value==null) return null;
        BashOption bash = null;

        String conEnd = System.getProperty("consoleEncoding");
        if (conEnd==null)
            conEnd = System.getProperty("sun.jnu.encoding");
        if (conEnd == null)
            conEnd = "UTF-8";

        try {
            value = new  String(value.getBytes(conEnd),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        for (BashOption bashOption : BashOption.values()) {
            for (String s : bashOption.getStringOption()) {
                if (s.equals(value)) {
                    return bashOption;
                }
            }
        }
        return null;
    }

    /**Return all String[] correspond of this BashOption
     *
     * @return
     */
    public String[] getStringOption() {
        return stringOption;
    }
}
