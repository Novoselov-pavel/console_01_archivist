package controller.interfaces;

import controller.GetCrc32;
import gui.ExitProgramInterface;
import gui.GetInfoInterface;
import gui.LoggerInterface;
import model.SettingInterface;

public interface FabricControllerInterface {

    /** Create instance (if needed) and return {@link ExitProgramInterface}
     *
     * @param
     * @return
     */
    ExitProgramInterface getExitProgramInterface();

    /** Create instance (if needed) and return {@link LoggerInterface}
     *
     * @param
     * @return
     */
    LoggerInterface getLoggerInterface();

    /**Create instance and return needed implementation {@link FileInterface}
     *
     * @return
     */
    FileInterface getFileInterface();

    /**Return String value of CRC for Directory
     *
     * @return
     */
    String getCRCValueForDir();

    /**Create instance and return needed implementation {@link Crc32Interface}
     *
     * @return
     */
    Crc32Interface getCRC32Class();

    /**Create instance and return needed implementation {@link SettingInterface}
     *
     * @return
     */
    SettingInterface getSettings();

    /**Main work function with all logic
     *
     * @param
     */
    void workFormSetting();

}
