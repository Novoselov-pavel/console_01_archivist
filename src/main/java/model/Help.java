package model;

/**Strings displayed for option -h or --help in command line
 *
 */
public class  Help {
    private static String[] help = {"Usage: Archivist.class [OPTION] [DIR_source] [DIR_destination]",
                            "Archive/dearchive  all files and all dir from [DIR_source] to zip file + manifest with MD5.",
                            "OPTION can contain next option separated by space.",
                            "all option is case sensitive.",
                            "",
                            "-h,   --help                 display help, all other options are ignore",
                            "-a,   --archive              archive [DIR_source] to [DIR_destination]",
                            "-d,   --dearchive            dearchive [DIR_source] to [DIR_destination]",
                            "-v,   --version              display version information and exit"
                            };

    /**Return help information in String[]
     *
     * @return
     */
    public static String[] getHelp() {
        return help;
    }
}
