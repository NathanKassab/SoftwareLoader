package me.bannock.capstone.loader.ui;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

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

    /**
     * Creates a scaled image using the provided source
     * @param source The image source
     * @param width The width to scale to
     * @param height The height to scale to
     * @param hints flags to indicate the type of algorithm to use
     * @return The image
     * @throws IOException If something goes wrong while loading the image
     * @see java.awt.Image#SCALE_DEFAULT
     * @see java.awt.Image#SCALE_FAST
     * @see java.awt.Image#SCALE_SMOOTH
     * @see java.awt.Image#SCALE_REPLICATE
     * @see java.awt.Image#SCALE_AREA_AVERAGING
     */
    public static Image createScaledImage(URL source, int width, int height, int hints) throws IOException {
        Image rawImage = ImageIO.read(source);
        return rawImage.getScaledInstance(width, height, hints);
    }

    /**
     * Creates a scaled image using the provided source
     * @param source The image source
     * @param width The width to scale to
     * @param height The height to scale to
     * @return The image
     * @throws IOException If something goes wrong while loading the image
     */
    public static Image createScaledImage(URL source, int width, int height) throws IOException {
        return createScaledImage(source, width, height, Image.SCALE_SMOOTH);
    }

    /**
     * Creates a scaled image using the provided source
     * @param source The image source
     * @param size The width and height of the image
     * @return The image
     * @throws IOException If something goes wrong while loading the image
     */
    public static Image createScaledImage(URL source, int size) throws IOException {
        return createScaledImage(source, size, size, Image.SCALE_SMOOTH);
    }

}
