package com.github.hexeditor;

import java.net.URL;
import java.text.MessageFormat;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

class jEP extends JScrollPane implements HyperlinkListener {

   JEditorPane eP = new JEditorPane();
   String s1;
   public String s2 = null;


   public jEP(String var1, boolean var2) {
      super(22, 30);
      this.eP.addHyperlinkListener(this);
      this.eP.setText(var1);
      this.s1 = var1;
      this.eP.setEditable(var2);
      this.eP.setCaretPosition(0);
      this.getViewport().setView(this.eP);
   }

   public void setContentType(String var1) {
      this.eP.setContentType(var1);
   }

   public void setUrl(URL var1) {
      try {
         this.eP.setPage(var1);
         this.eP.setCaretPosition(0);
      } catch (Exception var3) {
         this.eP.setText("Help file not found");
      }

   }

   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if(var1.getEventType() == EventType.ACTIVATED) {
         UI.browse = var1.getURL().toString();
         this.Browser2(var1.getURL());
      }

   }

   public void Browser2(URL var1) {
      String var2 = System.getProperty("os.name");
      String var5 = new String(var1.toExternalForm());

      try {
         String var3;
         MessageFormat var4;
         Object[] var6;
         if(var2.startsWith("Win")) {
            var6 = new Object[]{var1.toString()};
            String var7 = -1 >= var2.indexOf("9") && -1 >= var2.indexOf("Me")?"cmd.exe /c start \"\" \"{0}\"":"command.com /c start \"{0}\"";
            var4 = new MessageFormat(var7);
            var3 = var4.format(var6);
            if(var1.getProtocol().equals("file")) {
               throw new Exception("This class doesn\'t allow the opening a file, avoiding evil code.");
            }

            if(var1.toString().startsWith("mailto:")) {
               throw new Exception("This class doesn\'t allow the opening of mailto: .");
            }
         } else if(var2.startsWith("Mac OS")) {
            var6 = new Object[]{var5, var1.toString()};
            var4 = new MessageFormat("open -a /Applications/Safari.app {0}");
            var3 = var4.format(var6);
         } else {
            var6 = new Object[]{var5, var1.toString()};
            var4 = new MessageFormat(System.getProperty("mozilla {0}"));
            var3 = var4.format(var6);
         }

         Runtime.getRuntime().exec(var3);
      } catch (Exception var8) {
         System.err.println("Could not invoke browser: " + var8);
      }

   }
}
