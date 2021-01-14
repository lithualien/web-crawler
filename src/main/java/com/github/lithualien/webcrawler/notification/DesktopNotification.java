package com.github.lithualien.webcrawler.notification;

import java.awt.*;

public class DesktopNotification {

    private static final Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
    private static final TrayIcon trayIcon = new TrayIcon(image, "Info image");

    public void displayNotification(Integer amount) {
        SystemTray tray = SystemTray.getSystemTray();

        tray.remove(trayIcon);

        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException empty) {

        }

        trayIcon.displayMessage("Rasti nauji skelbimai",
                "Rasti nauji " + amount + " skelbimai, peržiūrėkite paštą", TrayIcon.MessageType.INFO);
    }

}
