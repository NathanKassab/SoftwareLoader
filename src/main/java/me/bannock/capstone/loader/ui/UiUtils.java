package me.bannock.capstone.loader.ui;

import javax.swing.JOptionPane;

public class UiUtils {

    /**
     * Displays an error dialog to the user. Locks thread until close
     * @param title The title of the dialog
     * @param message The message to show on the dialog
     */
    public static void displayErrorDialog(String title, String message){
        displayDialog(title, message, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information dialog to the user. Locks thread until close
     * @param title The title of the dialog
     * @param message The message to show on the dialog
     */
    public static void displayInfoDialog(String title, String message){
        displayDialog(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a warning dialog to the user. Locks thread until close
     * @param title The title of the dialog
     * @param message The message to show on the dialog
     */
    public static void displayWarningDialog(String title, String message){
        displayDialog(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a dialog to the user. Locks thread until close
     * @param title The title of the dialog
     * @param message The message to show on the dialog
     * @param dialogType The type of dialog
     */
    public static void displayDialog(String title, String message, int dialogType){
        JOptionPane.showMessageDialog(null, message, title, dialogType);
    }

}
