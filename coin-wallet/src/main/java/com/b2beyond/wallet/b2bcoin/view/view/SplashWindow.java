package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.view.panel.JpanelWithBackground;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class SplashWindow extends JWindow {
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel southPanel = new JPanel();
    FlowLayout southPanelFlowLayout = new FlowLayout();
    JProgressBar progressBar = new JProgressBar();
    ImageIcon imageIcon;
    String version;

    public SplashWindow(ImageIcon imageIcon, String version) {
        this.imageIcon = imageIcon;
        this.version = version;
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    // note - this class created with JBuilder
    void jbInit() throws Exception {
        setPreferredSize(new Dimension(480, 350));
        this.getContentPane().setLayout(borderLayout1);
        southPanel.setLayout(southPanelFlowLayout);
        southPanel.setBackground(Color.BLACK);

        // Adding to JPanel
        JLabel authorLabel = new JLabel("Author : Olivier Sinnaeve");
        JLabel logoDesignerLabel = new JLabel("https://b2bcoin.xyz");
        JLabel versionLabel = new JLabel("Wallet version " + version);
        JLabel copyrightLabel = new JLabel("Copyright @ B2B Coin");
        JPanel centerPanel = new JpanelWithBackground(imageIcon.getImage());
        centerPanel.setLayout(null);
        centerPanel.add(authorLabel);
        centerPanel.add(logoDesignerLabel);
        centerPanel.add(copyrightLabel);
        centerPanel.add(versionLabel);

        // Setting colors
        authorLabel.setForeground(B2BUtil.selectedColor);
        logoDesignerLabel.setForeground(B2BUtil.selectedColor);
        versionLabel.setForeground(B2BUtil.selectedColor);
        copyrightLabel.setForeground(B2BUtil.selectedColor);

        // Setting bounds
        authorLabel.setBounds(270, 20, 210, 25);
        logoDesignerLabel.setBounds(270, 45, 210, 25);
        copyrightLabel.setBounds(320, 290, 160, 25);
        versionLabel.setBounds(270, 70, 210, 25);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        southPanel.add(progressBar, null);
        this.pack();
    }

    public void setProgressMax(int maxProgress) {
        progressBar.setMaximum(maxProgress);
    }

    public void setProgress(int progress) {
        final int theProgress = progress;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(theProgress);
            }
        });
    }

    public void setProgress(String message, int progress) {
        final int theProgress = progress;
        final String theMessage = message;
        setProgress(progress);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(theProgress);
                setMessage(theMessage);
            }
        });
    }

    public void setScreenVisible(boolean b) {
        final boolean boo = b;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(boo);
            }
        });
    }

    private void setMessage(String message) {
        if (message==null)
        {
            message = "";
            progressBar.setStringPainted(false);
        }
        else
        {
            progressBar.setStringPainted(true);
        }
        progressBar.setString(message);
    }

}
