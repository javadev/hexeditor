package com.github.hexeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class binPanel extends JPanel
        implements ActionListener, ItemListener, CaretListener, MouseListener {

    JTextField JTFile = new JTextField();
    JProgressBar savePBar = new JProgressBar(0, 0, 0);
    JProgressBar findPBar = new JProgressBar(0, 0, 0);
    binEdit hexV;
    JComponent help = this.help();
    boolean helpFlag = false;
    boolean cp437Available = false;
    boolean useFindChar = false;
    JComboBox[] viewCBox = new JComboBox[2];
    JTextField JTView = new JTextField();
    JLabel JTsizes = new JLabel("");
    byte[] selection;
    JLabel fJL = new JLabel(" ");
    JButton[] fJB = new JButton[2];
    JComboBox[] fJCB = new JComboBox[4];
    JTextField[] fJTF = new JTextField[2];
    JCheckBox fJRB = new JCheckBox("Ignore case:");
    JPanel jPBBP = new JPanel(new BorderLayout());
    JPanel fP0 = new JPanel(new BorderLayout());
    JPanel fP1 = this.findPanel();
    JPanel stat = this.status();
    JPanel frameFile;
    byte[] finByte;
    byte[] finByteU;
    byte[][][] findChar;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu;
    JMenuItem menuItem;
    boolean isApplet = false;
    boolean hexOffset = true;
    slaveT slave;
    DecimalFormatSymbols dFS = new DecimalFormatSymbols();
    DecimalFormat fForm = new DecimalFormat("#.##########E0");
    DecimalFormat dForm = new DecimalFormat("#.###################E0");

    public binPanel(boolean var1, String[] var2) {
        this.isApplet = var1;
        this.setLayout(new BorderLayout());
        String[][] var5 =
                new String[][] {
                    {"File", "Open", "Save as ", "Close file (Q)", "Screen to Png"},
                    {
                        "Edit",
                        "Select All",
                        "Undo (Z)",
                        "Redo (Y)",
                        "Cut (X)",
                        "Copy",
                        "Paste (V)",
                        "Find",
                        "Insert (before)",
                        "Delete"
                    },
                    {
                        "View",
                        "Goto",
                        "Toggle position Mark",
                        "Down to next mark",
                        "Up to previous mark ",
                        "Toggle caret ",
                        "Higher fontSize",
                        "Lower fontSize",
                        "Black/White background"
                    },
                    {"hidden", "Font +", "Font -"},
                    {"Help", "Toggle help"}
                };
        int[][] var6 =
                new int[][] {
                    {70, 79, 83, 81, 80},
                    {69, 65, 90, 89, 88, 67, 86, 70, 155, 127},
                    {86, 71, 77, 68, 85, 84, 107, 109, 87},
                    {178, 521, 45},
                    {72, 72}
                };
        int[][] var7 =
                new int[][] {
                    {70, 79, 83, 81, 80},
                    {69, 65, 90, 89, 88, 67, 86, 70, 73, 68},
                    {86, 71, 77, 68, 85, 84, 72, 76, 87},
                    {72, 43, 45},
                    {72, 72}
                };
        int[][] var8 =
                new int[][] {
                    {0, 2, 2, 2, 2},
                    {0, 2, 2, 2, 2, 2, 2, 2, 0, 0},
                    {0, 2, 2, 2, 2, 2, 2, 2, 2},
                    {0, 2, 2},
                    {0, 2}
                };

        for (int var3 = 0; var3 < var5.length; ++var3) {
            (this.menu = new JMenu(var5[var3][0])).setMnemonic(var7[var3][0]);
            if (var3 != 0 || !var1) {
                for (int var4 = 1; var4 < var5[var3].length; ++var4) {
                    this.menuItem = new JMenuItem(var5[var3][var4], var7[var3][var4]);
                    if (var3 != 4) {
                        this.menuItem.setAccelerator(
                                KeyStroke.getKeyStroke(var6[var3][var4], var8[var3][var4]));
                    }

                    this.menuItem.addActionListener(this);
                    this.menu.add(this.menuItem);
                    if (var3 == 1 && (var4 == 1 || var4 == 3 || var4 == 6 || var4 == 7)
                            || var3 == 2 && (var4 == 1 || var4 == 4 || var4 == 5 || var4 == 7)) {
                        this.menu.addSeparator();
                    }
                }
            }

            if (var3 != 3) {
                this.menuBar.add(this.menu);
            }
        }

        UI.jRP.setJMenuBar(this.menuBar);
        this.add(this.frameFile = this.frame(var2));
        this.dFS.setDecimalSeparator('.');
        this.fForm.setDecimalFormatSymbols(this.dFS);
        this.dForm.setDecimalFormatSymbols(this.dFS);
    }

    private JPanel frame(String[] var1) {
        this.savePBar.setStringPainted(true);
        this.savePBar.setString("");
        this.JTView.setEditable(false);
        JPanel var2 = new JPanel(new GridBagLayout());
        GridBagConstraints var3 = new GridBagConstraints();
        var3.fill = 2;
        var3.weightx = 1.0D;
        var3.gridx = var3.gridy = 0;
        this.JTFile.setEditable(false);
        var2.add(this.JTFile, var3);
        ++var3.gridy;
        var2.add(this.jPBBP, var3);
        ++var3.gridy;
        var2.add(this.fP0, var3);
        ++var3.gridy;
        var2.add(Box.createVerticalStrut(3), var3);
        var3.fill = 1;
        var3.weighty = 1.0D;
        ++var3.gridy;
        var2.add(this.hexV = new binEdit(this, this.isApplet), var3);
        var3.fill = 2;
        var3.weighty = 0.0D;
        ++var3.gridy;
        var2.add(this.stat, var3);
        this.fJCB[2].setSelectedIndex(6);
        if (var1 != null && 0 < var1.length) {
            if (var1[0].equals("-slave")) {
                (this.slave = new slaveT()).setDaemon(true);
                this.slave.hexV = this.hexV;
                this.slave.start();
            } else {
                this.hexV.loadFile(new java.io.File(var1[0]));
            }
        }

        return var2;
    }

    public JComponent help() {
        jEP var1 = new jEP((String) null, false);
        var1.setContentType("text/html");
        String var2 = Locale.getDefault().getLanguage();
        var2 = "ReadMe" + Character.toUpperCase(var2.charAt(0)) + var2.charAt(1) + ".htm";

        try {
            var1.eP
                    .getEditorKit()
                    .read(
                            UI.class
                                    .getResource(
                                            UI.class.getResource(var2) == null
                                                    ? "ReadMeEn.htm"
                                                    : var2)
                                    .openStream(),
                            var1.eP.getDocument(),
                            0);
            var1.eP
                    .getEditorKit()
                    .read(
                            UI.class.getResource("shortKey.htm").openStream(),
                            var1.eP.getDocument(),
                            var1.eP.getDocument().getLength());
        } catch (Exception var4) {;
        }

        return var1;
    }

    private JPanel findPanel() {
        String[][] var3 =
                new String[][] {
                    {"BE", "LE"},
                    {"Signed", "Unsigned"},
                    {
                        "Short (16)",
                        "Int (32)",
                        "Long (64)",
                        "Float (32)",
                        "Double (64)",
                        "Hexa",
                        "ISO/CEI 8859-1",
                        "UTF-8",
                        "UTF-16"
                    },
                    {"8 bits", "16 bits", "32 bits", "64 bits", "128 bits"},
                    {
                        "<html>Big-indian (natural order) or<br>Little-indian (Intel order).",
                        "Only for integer",
                        "Data type",
                        "<html>Select \'64\' if you search a machine instruction for a 64 bits processor.<br>If you don\'t know, left it at \'8\'."
                    },
                    {"BE", "Unsigned", "ISO/CEI 8859-1", "128 bits"},
                    {"Next", "Hide"}
                };
        JPanel var4 = new JPanel(new GridBagLayout());
        JPanel var5 = new JPanel(new GridBagLayout());
        JPanel var6 = new JPanel(new GridBagLayout());
        var4.setBorder(BorderFactory.createTitledBorder("Find:"));
        ((TitledBorder) var4.getBorder()).setTitleColor(Color.blue);
        GridBagConstraints var7 = new GridBagConstraints();
        var7.anchor = 21;
        var4.add(var5, var7);
        var7.fill = 2;
        ++var7.gridx;
        var4.add(var6, var7);
        this.findPBar.setStringPainted(true);
        var4.add(this.findPBar, var7);
        var5.add(this.fJTF[0] = new JTextField());
        this.fJTF[0].addCaretListener(this);
        var5.add(new JLabel("  "));
        var5.add(this.fJL);

        int var1;
        for (var1 = 0; var1 < this.fJCB.length; ++var1) {
            this.fJCB[var1] = new JComboBox();
            this.fJCB[var1].setPrototypeDisplayValue(var3[5][var1]);
            this.fJCB[var1].setToolTipText(var3[4][var1]);

            for (int var2 = 0; var2 < var3[var1].length; ++var2) {
                this.fJCB[var1].addItem(var3[var1][var2]);
            }

            this.fJCB[var1].addItemListener(this);
            var6.add(this.fJCB[var1]);
        }

        this.fJRB.setHorizontalTextPosition(2);
        this.fJRB.setMargin(new Insets(0, 1, 0, 1));
        this.fJRB.addActionListener(this);
        var6.add(this.fJRB);
        this.fJTF[0].setPreferredSize(
                new Dimension(
                        this.fJCB[0].getPreferredSize().width
                                + this.fJCB[1].getPreferredSize().width
                                + this.fJCB[2].getPreferredSize().width
                                + this.fJCB[3].getPreferredSize().width,
                        this.fJTF[0].getPreferredSize().height));
        var6.add(Box.createHorizontalGlue());
        var6.add(new JLabel("   From:"));
        var6.add(this.fJTF[1] = new JTextField(15));

        for (var1 = 0; var1 < this.fJB.length; ++var1) {
            this.fJB[var1] = new JButton(var3[6][var1]);
            this.fJB[var1].setMargin(new Insets(3, 2, 3, 2));
            this.fJB[var1].addActionListener(this);
            var6.add(this.fJB[var1]);
        }

        return var4;
    }

    private JPanel status() {
        String[][] var1 =
                new String[][] {
                    {"BE", "LE "},
                    {
                        "Binary",
                        "Byte, signed/unsigned    ",
                        "Short (16), signed",
                        "Short (16), unsigned",
                        "Int (32), signed",
                        "Int (32), unsigned",
                        "Long (64), signed",
                        "Long (64), unsigned",
                        "Float (32)",
                        "Double (64)",
                        "DOS-US/OEM-US/cp437",
                        "UTF-8",
                        "UTF-16"
                    },
                    {
                        "<html>Big-Endian (natural order) or little-Endian (Intel order).",
                        "<html>Conversion rule for the data following the caret (shown here after)."
                    }
                };

        try {
            if (!(this.cp437Available = Charset.isSupported("cp437"))) {
                var1[1][10] = "ISO/CEI 8859-1";
            }
        } catch (Exception var6) {;
        }

        JPanel var4 = new JPanel(new GridBagLayout());
        var4.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        GridBagConstraints var5 = new GridBagConstraints();
        var5.fill = 1;
        var5.insets = new Insets(0, 0, 0, 0);

        for (int var2 = 0; var2 < this.viewCBox.length; ++var2) {
            this.viewCBox[var2] = new JComboBox();
            this.viewCBox[var2].setPrototypeDisplayValue(var1[var2][1]);
            this.viewCBox[var2].setToolTipText(var1[2][var2]);

            for (int var3 = 0; var3 < var1[var2].length; ++var3) {
                this.viewCBox[var2].addItem(var1[var2][var3]);
            }

            ++var5.gridx;
            var4.add(this.viewCBox[var2], var5);
        }

        this.viewCBox[1].setSelectedIndex(1);
        var5.weightx = 1.0D;
        ++var5.gridx;
        this.JTView.setPreferredSize(
                new Dimension(
                        this.JTView.getPreferredSize().width,
                        this.viewCBox[0].getMinimumSize().height));
        var4.add(this.JTView, var5);
        var5.weightx = 0.0D;
        ++var5.gridx;
        var4.add(Box.createHorizontalStrut(3), var5);
        ++var5.gridx;
        var4.add(this.JTsizes, var5);
        ++var5.gridx;
        var4.add(Box.createHorizontalStrut(3), var5);
        this.viewCBox[0].addItemListener(this);
        this.viewCBox[1].addItemListener(this);
        this.JTsizes.addMouseListener(this);
        return var4;
    }

    public void actionPerformed(ActionEvent var1) {
        if (var1.getSource() == this.fJRB) {
            this.checkFindEntry();
        } else if (var1.getSource() == this.fJB[0] && this.fJB[0].getText() == "Next") {
            this.hexV.find1();
        } else if (var1.getSource() == this.fJB[0] && this.fJB[0].getText() == "Stop") {
            this.hexV.find.interrupt();
        } else if (var1.getSource() == this.fJB[1]) {
            this.fP0.removeAll();
            this.validate();
            this.repaint();
            this.hexV.slideScr(-1L, false);
        } else if (var1.getSource().getClass().isInstance(new JMenuItem())) {
            boolean var2 =
                    ((JMenuItem) ((JMenuItem) var1.getSource())).getText().equals("Toggle help");
            if (var2 || this.helpFlag) {
                this.removeAll();
                if (this.helpFlag) {
                    this.add(this.frameFile);
                } else {
                    this.add(this.help);
                }

                this.validate();
                this.repaint();
            }

            this.helpFlag = !this.helpFlag && var2;
            if (!var2) {
                this.hexV.KeyFromMenu(((JMenuItem) var1.getSource()).getAccelerator().getKeyCode());
            }
        }
    }

    public void itemStateChanged(ItemEvent var1) {
        if (var1.getSource() != this.viewCBox[0] && var1.getSource() != this.viewCBox[1]) {
            if (var1.getSource() == this.fJCB[2]) {
                int var2 = this.fJCB[2].getSelectedIndex();
                this.fJCB[0].setEnabled(var2 < 5 || var2 == 8);
                this.fJCB[1].setEnabled(var2 < 3);
                this.fJRB.setEnabled(5 < var2);
            }

            this.checkFindEntry();
        } else {
            this.hexV.rePaint();
        }
    }

    public void caretUpdate(CaretEvent var1) {
        this.checkFindEntry();
    }

    protected void saveRunning(boolean var1) {
        if (var1) {
            this.jPBBP.add(this.savePBar);
        } else {
            this.jPBBP.removeAll();
        }

        this.savePBar.setValue(0);
        this.validate();
        this.repaint();
    }

    protected void find() {
        this.findPBar.setString("");
        this.fP0.add(this.fP1, "West");
        this.validate();
        this.repaint();
    }

    protected void findRunning(boolean var1) {
        boolean var2 = false;
        this.fJB[0].setText(var1 ? "Stop" : "Next");
        this.fJB[1].setEnabled(!var1);
        this.findPBar.setValue(0);
        if (!var1) {
            this.findPBar.setString("");
        }
    }

    private void checkFindEntry() {
        boolean var1 = false;
        BigDecimal var3 = null;
        double var11 = 0.0D;
        float var14 = 0.0F;
        long var15 = System.currentTimeMillis();
        long[] var17 = new long[2];
        StringBuffer var18 = new StringBuffer(220);
        var18.append("<html>");

        while (System.currentTimeMillis() < var15 + 50L) {;
        }

        String var19 = this.fJTF[0].getText();
        String var20 = null;
        int var21 = var19.length();
        int var22 = this.fJCB[2].getSelectedIndex();
        boolean var24 = false;
        boolean var25 = false;
        this.useFindChar = this.fJRB.isSelected() && 5 < var22;
        if (var21 == 0) {
            this.fJL.setText(" ");
        } else {
            int var32;
            int var23;
            if (var22 < 5
                    && !var19.startsWith("0x")
                    && !var19.startsWith("Ox")
                    && !var19.startsWith("ox")) {
                if (var19.charAt(0) == 45 && this.fJCB[1].getSelectedIndex() == 1 && var22 < 3) {
                    this.fJL.setText(
                            "<html><FONT color=red>Input must be positive for unsigned integer.</FONT>");
                    return;
                }

                if (var19.equals("-") || var19.equals("+")) {
                    this.fJL.setText(" ");
                    return;
                }

                BigDecimal var4 =
                        var22 == 0
                                ? BigDecimal.valueOf(-32768L)
                                : (var22 == 1
                                        ? BigDecimal.valueOf(-2147483648L)
                                        : (var22 == 2
                                                ? BigDecimal.valueOf(Long.MIN_VALUE)
                                                : (var22 == 3
                                                        ? BigDecimal.valueOf(
                                                                -3.4028234663852886E38D)
                                                        : BigDecimal.valueOf(
                                                                -1.7976931348623157E308D))));
                BigDecimal var5;
                if (2 < var22) {
                    var5 =
                            var22 == 3
                                    ? BigDecimal.valueOf(3.4028234663852886E38D)
                                    : BigDecimal.valueOf(Double.MAX_VALUE);
                } else if (this.fJCB[1].getSelectedIndex() == 0) {
                    var5 =
                            var22 == 0
                                    ? BigDecimal.valueOf(32767L)
                                    : (var22 == 1
                                            ? BigDecimal.valueOf(2147483647L)
                                            : BigDecimal.valueOf(Long.MAX_VALUE));
                } else {
                    var5 =
                            var22 == 0
                                    ? BigDecimal.valueOf(65535L)
                                    : (var22 == 1
                                            ? BigDecimal.valueOf(4294967295L)
                                            : new BigDecimal("18446744073709551615"));
                }

                this.finByte = new byte[var22 < 3 ? 2 << var22 : (var22 == 3 ? 4 : 8)];

                while (0 < var21) {
                    try {
                        var3 = new BigDecimal(var19.substring(0, var21));
                        if (var3.compareTo(var4) >= 0 && var5.compareTo(var3) >= 0) {
                            break;
                        }

                        throw new Exception("");
                    } catch (Exception var31) {
                        --var21;
                    }
                }

                if (var21 == 0) {
                    this.fJL.setText("<html><FONT color=red>Input must be a number.</FONT>");
                } else if (var22 < 3) {
                    BigInteger var7;
                    try {
                        var7 = var3.setScale(0, 7).unscaledValue();
                    } catch (Exception var27) {
                        var7 = var3.setScale(0, 5).unscaledValue();
                        var1 = true;
                    }

                    if (var7.signum() < 0) {
                        var15 = var7.longValue();
                        if (this.fJCB[0].getSelectedIndex() == 0) {
                            for (var23 = 0; var23 < this.finByte.length; ++var23) {
                                this.finByte[this.finByte.length - var23 - 1] =
                                        (byte) ((int) (var15 & 255L));
                                var15 >>>= 8;
                            }
                        } else {
                            for (var23 = 0; var23 < this.finByte.length; ++var23) {
                                this.finByte[var23] = (byte) ((int) (var15 & 255L));
                                var15 >>>= 8;
                            }
                        }
                    } else {
                        byte[] var2 = var7.toByteArray();
                        var32 =
                                this.finByte.length < var2.length
                                        ? this.finByte.length
                                        : var2.length;
                        Arrays.fill(this.finByte, (byte) 0);
                        if (this.fJCB[0].getSelectedIndex() == 0) {
                            for (var23 = 1; var23 <= var32; ++var23) {
                                this.finByte[this.finByte.length - var23] =
                                        var2[var2.length - var23];
                            }
                        } else {
                            for (var23 = 0; var23 < var32; ++var23) {
                                this.finByte[var23] = var2[var2.length - 1 - var23];
                            }
                        }
                    }
                } else {
                    float var13 = var3.floatValue();
                    double var9 = var3.doubleValue();
                    this.useFindChar =
                            var1 =
                                    0
                                            != (var32 =
                                                    var3.compareTo(
                                                            new BigDecimal(
                                                                    var22 == 3
                                                                            ? (double) var13
                                                                            : var9)));
                    var15 =
                            var17[0] =
                                    var22 == 3
                                            ? (long) Float.floatToRawIntBits(var13)
                                            : Double.doubleToRawLongBits(var9);
                    if (this.fJCB[0].getSelectedIndex() == 0) {
                        for (var23 = 0; var23 < this.finByte.length; ++var23) {
                            this.finByte[this.finByte.length - var23 - 1] =
                                    (byte) ((int) (var15 & 255L));
                            var15 >>>= 8;
                        }
                    } else {
                        for (var23 = 0; var23 < this.finByte.length; ++var23) {
                            this.finByte[var23] = (byte) ((int) (var15 & 255L));
                            var15 >>>= 8;
                        }
                    }

                    if (this.useFindChar) {
                        var17[1] =
                                var22 == 3
                                        ? (long)
                                                Float.floatToRawIntBits(
                                                        var14 =
                                                                0 < var32
                                                                        ? math.nextUp(var13)
                                                                        : math.nextDown(var13))
                                        : Double.doubleToRawLongBits(
                                                var11 =
                                                        0 < var32
                                                                ? math.nextUp(var9)
                                                                : math.nextDown(var9));
                        if (var22 == 3) {
                            var18.append(var32 < 0 ? "&lt; " : "&gt; ")
                                    .append(this.fForm.format(new BigDecimal((double) var13)))
                                    .append("<br>" + (var32 < 0 ? "&gt; " : "&lt; "))
                                    .append(this.fForm.format(new BigDecimal((double) var14)));
                        } else {
                            var18.append(var32 < 0 ? "&lt; " : "&gt; ")
                                    .append(this.dForm.format(new BigDecimal(var9)))
                                    .append("<br>" + (var32 < 0 ? "&gt; " : "&lt; "))
                                    .append(this.dForm.format(new BigDecimal(var11)));
                        }

                        this.findChar = new byte[1][2][var22 == 3 ? 4 : 8];

                        for (var32 = 0; var32 < 2; ++var32) {
                            if (this.fJCB[0].getSelectedIndex() == 0) {
                                for (var23 = 0; var23 < this.findChar[0][var32].length; ++var23) {
                                    this.findChar[0][var32][this.finByte.length - var23 - 1] =
                                            (byte) ((int) (var17[var32] & 255L));
                                    var17[var32] >>>= 8;
                                }
                            } else {
                                for (var23 = 0; var23 < this.findChar[0][var32].length; ++var23) {
                                    this.findChar[0][var32][var23] =
                                            (byte) ((int) (var17[var32] & 255L));
                                    var17[var32] >>>= 8;
                                }
                            }
                        }
                    }
                }
            } else if (var22 == 5) {
                var19 = var19.trim().replaceAll(" ", "");
                if (var19.startsWith("0x") || var19.startsWith("Ox") || var19.startsWith("ox")) {
                    var19 = var19.substring(2);
                }

                for (var21 = 0;
                        var21 < var19.length()
                                && -1 < "0123456789abcdefABCDEF".indexOf(var19.charAt(var21));
                        ++var21) {;
                }

                if (var21 < 2) {
                    this.fJL.setText(
                            var21 == var19.length()
                                    ? " "
                                    : "<html><FONT color=red>Input must be a hexa string.</FONT>");
                    return;
                }

                this.finByte = new byte[var21 >> 1];

                try {
                    for (var23 = 0; var23 < this.finByte.length; ++var23) {
                        this.finByte[var23] =
                                (byte)
                                        Integer.parseInt(
                                                var19.substring(var23 << 1, var23 * 2 + 2), 16);
                    }
                } catch (Exception var30) {;
                }
            } else if (var22 == 6) {
                while (0 < var21) {
                    try {
                        var20 = var19.substring(0, var21);
                        this.finByte = var20.getBytes("ISO-8859-1");
                        if (!var20.equals(new String(this.finByte, "ISO-8859-1"))) {
                            throw new Exception("");
                        }
                        break;
                    } catch (Exception var28) {
                        --var21;
                    }
                }

                if (var21 < 1) {
                    this.fJL.setText(
                            "<html><FONT color=red>Input must be an ISO-8859-1 string.</FONT>");
                    return;
                }
            } else {
                while (0 < var21) {
                    try {
                        var20 = var19.substring(0, var21);
                        this.finByte =
                                var20.getBytes(
                                        var22 == 7
                                                ? "UTF-8"
                                                : (this.fJCB[0].getSelectedIndex() == 0
                                                        ? "UTF-16BE"
                                                        : "UTF-16LE"));
                        break;
                    } catch (Exception var29) {
                        --var21;
                    }
                }

                if (var21 < 1) {
                    this.fJL.setText("<html><FONT color=red>Input must be an UTF string.</FONT>");
                    return;
                }
            }

            if (var22 < 3 || 4 < var22 || !var1) {
                for (var23 = 0; var23 < this.finByte.length; ++var23) {
                    var32 = this.finByte[var23] & 255;
                    var18.append(
                            (var32 < 16 ? "0" : "") + Integer.toHexString(var32).toUpperCase());
                    if ((var23 + 1) % 16 == 0) {
                        var18.append("<br>");
                    } else if ((var23 + 1) % (1 << this.fJCB[3].getSelectedIndex()) == 0) {
                        var18.append(" ");
                    }
                }
            }

            var19 = var19.toUpperCase();
            if (var21 == var19.length() - 1
                    && (var22 >= 5 || var19.charAt(var19.length() - 1) != 69)) {
                var18.append("<br><FONT color=red>The last char is invalid.</FONT>");
            } else if (var21 < var19.length() - 1
                    && (var21 != var19.length() - 2
                            || var22 >= 5
                            || var19.charAt(var19.length() - 2) != 69
                            || var19.charAt(var19.length() - 1) != 43
                                    && var19.charAt(var19.length() - 1) != 45)) {
                var18.append("<br><FONT color=red>The last ")
                        .append(var19.length() - var21)
                        .append(" caracters are invalid.</FONT>");
            }

            if (var1 && !this.useFindChar) {
                var18.append(
                        "<br><FONT color=red>The binary doesn\'t represent exactly the significand.</FONT>");
            }

            this.fJL.setText(var18.toString());
            if (var20 != null
                    && 0 < var20.length()
                    && this.fJRB.isSelected()
                    && !var20.toUpperCase().equals(var20.toLowerCase())) {
                var20 = var20.toUpperCase();
                this.findChar = new byte[var20.length()][][];

                for (var21 = 0; var21 < var20.length(); ++var21) {
                    char var8 = var20.charAt(var21);
                    var23 = var8 == Character.toLowerCase(var8) ? 1 : 2;
                    if (1 < var23) {
                        for (var22 = 0; var22 < accent.s.length; ++var22) {
                            if (-1 < accent.s[var22].indexOf(var8)) {
                                var23 = accent.s[var22].length();
                                break;
                            }
                        }
                    }

                    this.findChar[var21] = new byte[var23][];

                    for (var32 = 0; var32 < var23; ++var32) {
                        if (var23 < 3) {
                            var8 = var32 == 0 ? var8 : Character.toUpperCase(var8);
                        } else if (var22 < accent.s.length) {
                            var8 = accent.s[var22].charAt(var32);
                        }

                        if (var22 == 6) {
                            this.findChar[var21][var32] = new byte[1];
                            this.findChar[var21][var32][0] =
                                    var8 < 256 ? (byte) var8 : this.findChar[var21][0][0];
                        } else if (var22 == 8) {
                            this.findChar[var21][var32] = new byte[2];
                            this.findChar[var21][var32][1 - this.fJCB[0].getSelectedIndex()] =
                                    (byte) (var8 & 255);
                            this.findChar[var21][var32][this.fJCB[0].getSelectedIndex()] =
                                    (byte) (var8 >> 8);
                        } else {
                            this.findChar[var21][var32] =
                                    new byte
                                            [var8 < 128
                                                    ? 1
                                                    : (var8 < 2048 ? 2 : (var8 < 65536 ? 3 : 4))];
                            if (var8 < 128) {
                                this.findChar[var21][var32][0] = (byte) var8;
                            } else {
                                int var33;
                                for (var33 = this.findChar[var21][var32].length - 1;
                                        0 < var33;
                                        --var33) {
                                    this.findChar[var21][var32][var33] = (byte) (var8 & 63 | 128);
                                    var8 = (char) (var8 >> 6);
                                }

                                var33 = this.findChar[var21][var32].length;
                                this.findChar[var21][var32][0] =
                                        (byte)
                                                (var8
                                                        | (var33 == 2
                                                                ? 192
                                                                : (var33 == 3 ? 224 : 240)));
                            }
                        }
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent var1) {}

    public void mouseEntered(MouseEvent var1) {}

    public void mouseExited(MouseEvent var1) {}

    public void mousePressed(MouseEvent var1) {}

    public void mouseClicked(MouseEvent var1) {
        this.hexOffset = !this.hexOffset;
        this.hexV.setStatus();
    }
}
