package com.github.hexeditor;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class UI extends JApplet {

   private static final String link = "https://github.com/javadev/hexeditor";
   private static final String appName = "hexeditor.jar";
   private static final String version = "2014-07-29";
   private static boolean applet = false;
   private static JPanel jP = new JPanel(new BorderLayout());
   private static Runtime rT = Runtime.getRuntime();
   private static String[] arg = null;
   static final byte[] logo = new byte[]{(byte)71, (byte)73, (byte)70, (byte)56, (byte)57, (byte)97, (byte)16, (byte)0, (byte)16, (byte)0, (byte)-128, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)-1, (byte)-1, (byte)-1, (byte)33, (byte)-7, (byte)4, (byte)1, (byte)0, (byte)0, (byte)1, (byte)0, (byte)44, (byte)0, (byte)0, (byte)0, (byte)0, (byte)16, (byte)0, (byte)16, (byte)0, (byte)0, (byte)2, (byte)40, (byte)-116, (byte)-113, (byte)-87, (byte)-101, (byte)0, (byte)-26, (byte)24, (byte)124, (byte)33, (byte)74, (byte)107, (byte)25, (byte)-90, (byte)-102, (byte)70, (byte)14, (byte)77, (byte)-50, (byte)-57, (byte)109, (byte)-93, (byte)-9, (byte)100, (byte)-37, (byte)49, (byte)34, (byte)-85, (byte)-24, (byte)-94, (byte)83, (byte)85, (byte)-54, (byte)-86, (byte)76, (byte)87, (byte)-115, (byte)-60, (byte)-9, (byte)71, (byte)1, (byte)0, (byte)59};
   public static JRootPane jRP = null;
   public static String browse = null;
   public static final String htmlBase = null;
   public static final String htmlReport = null;
   public static final String htmlEnd = null;


   static void all() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var1) {
         ;
      }

      jP.add(new binPanel(applet, arg));
   }

   public void init() {
      applet = true;
      jRP = this.getRootPane();
      all();
      String var1 = this.getParameter("JAVAMINVERSION");
      if(System.getProperty("java.specification.version").compareToIgnoreCase(var1) < 0) {
         this.getContentPane().add(new JLabel("<html><h1>Java version found: " + System.getProperty("java.version") + ", needed: " + var1), "Center");
      } else {
         this.getContentPane().add(jP, "Center");
      }

   }

   public static void main(String[] var0) {
      arg = (String[])((String[])var0.clone());
      if(!applet && var0 != null && 0 < var0.length) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if(var0[var1].toLowerCase().equals("-bug")) {
               bugReport();
            }
         }
      }

      SwingUtilities.invokeLater(new UI$1(var0));
   }

   private static void bugReport() {
      try {
         System.setErr(new PrintStream(new FileOutputStream(new File(System.getProperty("user.dir"), "Hexeditor.jar_BugReport.txt"))));
         StringBuffer var1 = new StringBuffer("if you find errors, feel free to send me a mail with a short explaination and this file at: @T \r\n\r\nHexeditor.jar 2014-07-29\r\n");
         Properties var2 = System.getProperties();
         Enumeration var3 = var2.propertyNames();

         while(var3.hasMoreElements()) {
            String var0 = (String)var3.nextElement();
            if(" user.name user.home ".indexOf(var0) == -1) {
               try {
                  var1.append(var0 + " " + var2.getProperty(var0) + "\n");
               } catch (Exception var5) {
                  var1.append(var0 + " SECURITY EXCEPTION!\n");
               }
            }
         }

         var1.append("\r\nµP\t" + rT.availableProcessors());
         var1.append("\r\nMem(MiB), free/total/max: " + (rT.freeMemory() >> 20) + "/" + (rT.totalMemory() >> 20) + "/" + (rT.maxMemory() >> 20));
         var1.append("\r\n\r\nError messages:");
         System.err.println(var1);
      } catch (Exception var6) {
         ;
      }

   }

   static JPanel access$000() {
      return jP;
   }

}
