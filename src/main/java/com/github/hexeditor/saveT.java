package com.github.hexeditor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

class saveT extends Thread {

    File f1;
    File f2;
    Vector v1;
    binEdit hexV;
    JProgressBar jPBar;
    private long time;
    private long virtualSize;
    private long pos;

    public void run() {
        boolean var1 = false;
        int var3 = 0;
        FileInputStream var5 = null;
        byte[] var8 = new byte[2097152];
        if (this.v1 != null && this.v1.size() != 0) {
            long var14 = 0L;
            this.pos = 0L;
            this.time = System.currentTimeMillis();
            this.virtualSize = ((edObj) this.v1.lastElement()).p2;
            this.jPBar.setMaximum(1073741824);

            edObj var9;
            while (var3 < this.v1.size()) {
                var9 = (edObj) this.v1.get(var3);
                if (this.pos < var9.p2) {
                    break;
                }

                ++var3;
            }

            BufferedOutputStream var16 = null;

            try {
                if (this.f1 != null) {
                    var5 = new FileInputStream(this.f1);
                }

                File var6 = new File(this.f2.getPath() + ".TMP");

                for (var16 = new BufferedOutputStream(new FileOutputStream(var6), 2097152);
                        var3 < this.v1.size() && this.next();
                        ++var3) {
                    var9 = (edObj) this.v1.get(var3);
                    long var10 = var9.p1 - var9.offset;
                    this.pos = var9.p1;
                    if (var9.o.a1 != 4
                            && var9.o.a1 != 2
                            && (var9.o.a1 != 6 || 1 >= var9.o.B.size())) {
                        int var2;
                        long var12;
                        if (var9.o.a1 == 6) {
                            Arrays.fill(var8, ((Byte) var9.o.B.get(0)).byteValue());

                            while (this.pos < var9.p2 && this.next()) {
                                var12 = var9.p2 - this.pos;
                                var2 = var12 < 2097152L ? (int) var12 : 2097152;
                                var16.write(var8, 0, var2);
                                this.pos += (long) var2;
                                this.setJPBar();
                            }
                        } else {
                            for (var12 = this.pos - var10;
                                    var14 < var12;
                                    var14 += var5.skip(var12 - var14)) {;
                            }

                            while (this.pos < var9.p2 && this.next()) {
                                var12 = var9.p2 - this.pos;
                                var2 = var5.read(var8, 0, var12 < 2097152L ? (int) var12 : 2097152);
                                if (var2 <= 0) {
                                    throw new IOException(
                                            var2 == 0 ? "Unable to access file" : "EOF");
                                }

                                var14 += (long) var2;
                                var16.write(var8, 0, var2);
                                this.pos += (long) var2;
                                this.setJPBar();
                            }
                        }
                    } else {
                        while (this.pos < var9.p2 && this.next()) {
                            var16.write(
                                    ((Byte) var9.o.B.get((int) (this.pos - var10))).byteValue());
                            ++this.pos;
                        }

                        this.setJPBar();
                    }
                }

                var16.close();
                if (var5 != null) {
                    var5.close();
                }

                if (this.hexV.rAF != null) {
                    this.hexV.rAF.close();
                }

                if (this.f1 != null && this.f1.equals(this.f2)) {
                    File var7 = new File(this.f1.getParent(), this.f1.getName() + ".bak");
                    if (var7.exists()) {
                        var7.delete();
                    }

                    this.f1.renameTo(var7);
                }

                var6.renameTo(this.f2);
                this.hexV.save2(this.f2);
            } catch (Exception var20) {
                JOptionPane.showMessageDialog(this.hexV, var20);
                this.hexV.save2((File) null);
            }

            try {
                var16.close();
            } catch (Exception var19) {;
            }

            try {
                var5.close();
            } catch (Exception var18) {;
            }
        }
    }

    protected void setJPBar() {
        this.jPBar.setValue((int) (1.07374182E9F * ((float) this.pos / (float) this.virtualSize)));
        this.jPBar.setString(
                this.toTime(this.pos, this.virtualSize, System.currentTimeMillis() - this.time));
    }

    private boolean next() {
        return !Thread.currentThread().isInterrupted();
    }

    private String toTime(long var1, long var3, long var5) {
        StringBuffer var7 =
                new StringBuffer(
                        Float.toString(
                                (float) ((int) ((float) var1 / ((float) var3 / 1000.0F))) / 10.0F));
        var7.append("% saved");
        if (var1 != 0L) {
            var5 = var5 / 1000L * (var3 / var1);
        }

        if (var5 == Long.MAX_VALUE) {
            return "";
        } else {
            long[] var8 = new long[] {86400L, 3600L, 60L, 1L};
            String[] var9 = new String[] {"D ", "H ", "mn ", "s "};
            int var11 = 0;
            var7.append(", time remaining ");

            for (int var10 = 0; var10 < var8.length && var11 < 2; ++var10) {
                long var12;
                if ((var12 = var5 / var8[var10]) != 0L || var11 == 1) {
                    if (var12 < 10L && 0 < var11) {
                        var7.append("0");
                    }

                    var7.append(var12).append(var9[var10]);
                    var5 %= var8[var10];
                    ++var11;
                }
            }

            return var7.toString();
        }
    }
}
