package com.github.hexeditor;

import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

final class UI$1 implements Runnable {

    private final String[] val$args;

    UI$1(String[] var1) {
        this.val$args = var1;
    }

    public void run() {
        JFrame var1 =
                new JFrame(
                        1 < this.val$args.length && this.val$args[0].equals("-slave")
                                ? "hexeditor.jar currently linked to  " + this.val$args[1]
                                : "hexeditor.jar     https://github.com/javadev/hexeditor     Updated: 2014-07-29",
                        GraphicsEnvironment.getLocalGraphicsEnvironment()
                                .getDefaultScreenDevice()
                                .getDefaultConfiguration());
        var1.setIconImage((new ImageIcon(UI.logo)).getImage());
        var1.setDefaultCloseOperation(3);
        Rectangle var2 = var1.getGraphicsConfiguration().getBounds();
        Insets var3 = Toolkit.getDefaultToolkit().getScreenInsets(var1.getGraphicsConfiguration());
        int var4 = var2.width - var3.left - var3.right;
        int var5 = var2.height - var3.top - var3.bottom;
        var4 = 700 < var4 ? 700 : var4;
        var5 = 999 < var5 ? var5 : var5;
        if (1 < this.val$args.length && this.val$args[0].equals("-slave")) {
            var1.setBounds(
                    var2.x + var2.width - var4,
                    var2.y + (var2.height + var3.top - var3.bottom - var5) >> 1,
                    var4,
                    var5);
        } else {
            var1.setBounds(
                    var2.x + (var2.width + var3.left - var3.right - var4) >> 1,
                    var2.y + (var2.height + var3.top - var3.bottom - var5) >> 1,
                    var4,
                    var5);
        }

        UI.jRP = var1.getRootPane();
        UI.all();
        JFrame.setDefaultLookAndFeelDecorated(true);
        var1.getContentPane().add(UI.access$000(), "Center");
        var1.setVisible(true);
    }
}
