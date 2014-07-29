package com.github.hexeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JProgressBar;

class findT extends Thread {

   File f1;
   Vector v1;
   boolean isApplet;
   boolean ignoreCase;
   long pos;
   byte[] inBytes = null;
   byte[][][] inChars = (byte[][][])null;
   binEdit hexV;
   JProgressBar jPBar;
   private boolean isFound = false;
   private int realLength = 0;
   private int inCharsLength = 0;
   protected int wordSize;
   private long virtualSize;
   private Vector pile = new Vector();


   public void run() {
      boolean var1 = false;
      boolean var3 = false;
      FileInputStream var5 = null;
      byte[] var6 = new byte[2097152];
      edObj var7 = null;
      if(this.v1 != null && this.v1.size() != 0) {
         long var12 = 0L;
         this.virtualSize = ((edObj)this.v1.lastElement()).p2;
         this.jPBar.setMaximum(1073741824);
         int var2;
         int var4;
         int var18;
         if(!this.ignoreCase) {
            this.inCharsLength = this.inBytes.length;
         } else {
            for(var2 = 0; var2 < this.inChars.length; ++var2) {
               var4 = 0;

               for(var18 = 0; var18 < this.inChars[var2].length; ++var18) {
                  var4 = var4 < this.inChars[var2][var18].length?var4:this.inChars[var2][var18].length;
               }

               this.inCharsLength += var4;
            }
         }

         for(var18 = 0; var18 < this.v1.size(); ++var18) {
            var7 = (edObj)this.v1.get(var18);
            if(this.pos < var7.p2) {
               break;
            }
         }

         try {
            if(this.f1 != null) {
               var5 = new FileInputStream(this.f1);
            }

            while(var18 < this.v1.size() && this.next()) {
               var7 = (edObj)this.v1.get(var18);
               long var8 = var7.p1 - var7.offset;
               if(var7.o.a1 != 4 && var7.o.a1 != 2 && (var7.o.a1 != 6 || 1 >= var7.o.B.size())) {
                  if(var7.o.a1 == 6) {
                     byte var17 = ((Byte)var7.o.B.get(0)).byteValue();

                     while(this.pos < var7.p2 && this.next()) {
                        this.findB(var17);
                        if(this.pile.size() == 0 && this.pos < var7.p2 - (long)this.inCharsLength) {
                           this.pos = var7.p2 - (long)this.inCharsLength;
                        }
                     }
                  } else {
                     long var10;
                     for(var10 = this.pos - var8; var12 < var10; var12 += var5.skip(var10 - var12)) {
                        ;
                     }

                     while(this.pos < var7.p2 && this.next()) {
                        var10 = var7.p2 - this.pos;
                        var2 = var5.read(var6, 0, var10 < 2097152L?(int)var10:2097152);
                        if(var2 <= 0) {
                           throw new IOException(var2 == 0?"Unable to access file":"EOF");
                        }

                        var12 += (long)var2;

                        for(var4 = 0; var4 < var2 && this.next(); ++var4) {
                           this.findB(var6[var4]);
                        }

                        this.setJPBar();
                     }
                  }
               } else {
                  while(this.pos < var7.p2 && this.next()) {
                     this.findB(((Byte)var7.o.B.get((int)(this.pos - var8))).byteValue());
                  }
               }

               this.setJPBar();
               ++var18;
            }

            if(this.f1 != null) {
               var5.close();
            }
         } catch (Exception var16) {
            System.err.println("findT " + var16 + "\n\t" + var7 + "\n\t" + var18 + "\t" + this.pos);
         }

         try {
            var5.close();
         } catch (Exception var15) {
            ;
         }

         this.hexV.find2(this.pos, this.pos - (long)(this.isFound?(this.ignoreCase?this.realLength:this.inBytes.length):0));
      }
   }

   protected void setJPBar() {
      this.jPBar.setValue((int)(1.07374182E9F * ((float)this.pos / (float)this.virtualSize)));
      this.jPBar.setString((float)((int)((float)this.pos / ((float)this.virtualSize / 1000.0F))) / 10.0F + "%");
   }

   private boolean next() {
      return !this.isFound && !Thread.currentThread().isInterrupted();
   }

   private void findB(byte var1) {
      int[] var6 = new int[4];
      int var3;
      int var4;
      if(!this.ignoreCase) {
         for(var3 = this.pile.size() - 1; -1 < var3; --var3) {
            var4 = ((Integer)this.pile.get(var3)).intValue();
            if(this.inBytes[var4] != var1) {
               this.pile.remove(var3);
            } else {
               if(var4 + 1 >= this.inBytes.length) {
                  this.isFound = true;
                  break;
               }

               this.pile.set(var3, new Integer(var4 + 1));
            }
         }

         if(var1 == this.inBytes[0] && this.pos % (long)this.wordSize == 0L) {
            this.pile.add(new Integer(1));
            if(this.inBytes.length == 1) {
               this.isFound = true;
            }
         }
      } else {
         for(var3 = this.pile.size() - 1; -1 < var3; --var3) {
            var6 = (int[])((int[])this.pile.get(var3));
            ++var6[2];
            ++var6[3];
            if(this.inChars[var6[0]][var6[1]].length <= var6[2]) {
               ++var6[0];
               var6[1] = var6[2] = 0;
            }

            if(this.inChars.length <= var6[0]) {
               this.pile.remove(var3);
            } else if(var1 == this.inChars[var6[0]][var6[1]][var6[2]]) {
               this.pile.set(var3, var6.clone());
               if(var6[2] + 1 == this.inChars[var6[0]][var6[1]].length && var6[0] + 1 == this.inChars.length) {
                  this.isFound = true;
               }
            } else {
               for(var4 = var6[1] + 1; var4 < this.inChars[var6[0]].length; ++var4) {
                  int var5;
                  for(var5 = 0; var5 < var6[2] && var5 < this.inChars[var6[0]][var4].length && this.inChars[var6[0]][var4][var5] == this.inChars[var6[0]][var6[1]][var5]; ++var5) {
                     ;
                  }

                  if(var5 < var6[2]) {
                     this.pile.remove(var3);
                     break;
                  }

                  if(var1 == this.inChars[var6[0]][var4][var5]) {
                     var6[1] = var4;
                     this.pile.set(var3, var6.clone());
                     if(var6[2] + 1 == this.inChars[var6[0]][var6[1]].length && var6[0] + 1 == this.inChars.length) {
                        this.isFound = true;
                     }
                     break;
                  }

                  if(var4 + 1 == this.inChars[var6[0]].length) {
                     this.pile.remove(var3);
                     break;
                  }
               }

               if(var6[0] + 1 == this.inChars.length && var6[1] + 1 == this.inChars[0].length && var6[2] + 1 == this.inChars[0][1].length) {
                  this.pile.remove(var3);
               }
            }

            if(this.isFound) {
               this.realLength = var6[3];
               break;
            }
         }

         if(this.pos % (long)this.wordSize == 0L) {
            for(var3 = 0; var3 < this.inChars[0].length; ++var3) {
               if(var1 == this.inChars[0][var3][0]) {
                  var6[0] = var6[2] = 0;
                  var6[1] = var3;
                  var6[3] = 1;
                  this.pile.add(var6.clone());
                  if(this.inChars.length + this.inChars[0][var3].length == 2) {
                     this.isFound = true;
                     this.realLength = var6[3];
                  }
                  break;
               }
            }
         }
      }

      ++this.pos;
   }
}
