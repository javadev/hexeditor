package com.github.hexeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.Timer;

class slaveT extends Thread implements ActionListener {

    binEdit hexV;
    Timer timer = new Timer(300, this);
    private BufferedReader bR;

    slaveT() {
        this.bR = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {
        this.timer.addActionListener(this);
        this.timer.start();
    }

    public void actionPerformed(ActionEvent var1) {
        boolean var2 = true;
        boolean var3 = false;
        int var4 = 0;
        int var5 = 0;
        String var9 = null;
        StringBuffer var11 = new StringBuffer();
        Vector var12 = new Vector();
        if (var1.getSource() == this.timer) {
            this.timer.stop();

            try {
                if (!this.bR.ready()) {
                    this.timer.setDelay(300);
                } else {
                    var9 = this.bR.readLine().replaceAll("  ", " ");
                    this.timer.setDelay(50);
                }
            } catch (IOException var14) {;
            }

            if (var9 != null && 0 < var9.length()) {
                for (; var5 < var9.length(); ++var5) {
                    var2 = true;
                    if (var9.charAt(var5) == 34) {
                        var2 = !var2;
                    } else if (var2 && var9.charAt(var5) == 32) {
                        var12.add(var9.substring(var4, var5));
                        var4 = var5 + 1;
                    }
                }

                var12.add(var9.substring(var4, var5));
                if (0 < var12.size()) {
                    var9 = (String) var12.firstElement();
                    if (var9.equals("-goto") && var12.size() == 2) {
                        this.hexV.goTo((String) var12.elementAt(1));
                    } else if ((var9.equals("-Mark")
                                    || var9.equals("-mark")
                                    || var9.equals("-delmark")
                                    || var9.equals("-delMark"))
                            && 1 < var12.size()) {
                        for (var4 = 1; var4 < var12.size(); ++var4) {
                            try {
                                Long var8 = Long.valueOf((String) var12.elementAt(var4));
                                if (var9.equals("-Mark") && !this.hexV.MarkV.contains(var8)) {
                                    this.hexV.MarkV.add(var8);
                                } else if (var9.equals("-mark")
                                        && !this.hexV.markV.contains(var8)) {
                                    this.hexV.markV.add(var8);
                                } else if (var9.equals("-delMark")) {
                                    this.hexV.MarkV.remove(var8);
                                } else if (var9.equals("-delmark")) {
                                    this.hexV.markV.remove(var8);
                                }
                            } catch (Exception var15) {;
                            }
                        }
                    } else if (var9.equals("-file") && var12.size() == 2) {
                        var9 = (String) var12.elementAt(1);
                        if (var2 = (var9.length() & 3) == 0) {
                            var4 = 0;

                            while (var4 < var9.length() - 3 && var2) {
                                long var6 = 0L;

                                for (var5 = 0; var5 < 4 && var2; ++var5) {
                                    char var16 = var9.charAt(var4);
                                    var2 = 48 <= var16 && var16 <= 57 || 65 <= var16 && var16 <= 90;
                                    var6 =
                                            (var6 << 4)
                                                    + (long) var16
                                                    - (long) (48 <= var16 && var16 <= 57 ? 48 : 55);
                                    ++var4;
                                }

                                var11.append((char) ((int) var6));
                            }
                        }

                        this.hexV.loadFile(new File(var11.toString()));
                    } else if (var9.equals("-close")) {
                        this.hexV.closeFile();
                    }
                }
            }

            this.timer.restart();
        }
    }
}
