package me.bannock.capstone.loader;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Stage;
import me.bannock.capstone.loader.account.AccountManager;
import me.bannock.capstone.loader.account.AccountManagerException;
import me.bannock.capstone.loader.guice.LoaderModule;
import me.bannock.capstone.loader.guice.LoaderUiModule;
import me.bannock.capstone.loader.hwid.HwidService;
import me.bannock.capstone.loader.ui.frames.MainLoaderFrame;
import me.bannock.capstone.loader.ui.UiUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Insets;

public class Loader {

    @Inject
    public Loader(AccountManager accountManager, HwidService hwidService, MainLoaderFrame mainLoaderFrame){
        this.accountManager = accountManager;
        this.hwidService = hwidService;
        this.mainLoaderFrame = mainLoaderFrame;
    }

    private final Logger logger = LogManager.getLogger();
    private final AccountManager accountManager;
    private final HwidService hwidService;
    private final MainLoaderFrame mainLoaderFrame;

    /**
     * Starts the loader
     */
    public void start(){
        String hwid = hwidService.getHwid();
        try{
            accountManager.login(hwid);
        }catch (AccountManagerException e){
            logger.fatal("Failed to log user in", e);
            UiUtils.displayErrorDialog("Failed to login", String.format("Failed to log into your account:\n%s", e.getErrorMessage()));
            return;
        }

        SwingUtilities.invokeLater(() -> {
            mainLoaderFrame.populateFrame();
            mainLoaderFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {

        // This is to ensure all created swing components get the correct look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TabbedPane.contentBorderInsets",
                    new Insets(0, 0, 0, 0));
            UIManager.put("ScrollBar.width", 3);
        } catch (Exception ignored) {}

        Injector injector = Guice.createInjector(Stage.PRODUCTION, new LoaderModule(), new LoaderUiModule());
        injector.getInstance(Loader.class).start();

    }

}
