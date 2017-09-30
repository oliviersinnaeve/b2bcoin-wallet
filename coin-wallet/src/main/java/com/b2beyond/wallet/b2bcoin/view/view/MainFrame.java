package com.b2beyond.wallet.b2bcoin.view.view;


import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.BlockCount;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.TabContainer;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainFrame extends JFrame implements Observer {

    private JPanel content;

    private JLabel dataSynchronizingBlocks;
    private JProgressBar progressBar = new JProgressBar();

    private List<JButton> menus;


    /**
     * Create the frame.
     *
     * @param menuBar the menubar to show in the frame
     * @param containers the containers that we will create menu tabs for (left side, not menu on top !!)
     */
    public MainFrame(MenuBar menuBar, List<TabContainer> containers, PropertiesConfiguration applicationProperties) {
        this.setTitle("B2BCoin GUI");
        this.setBackground(B2BUtil.mainColor);
        Dimension minimumSize = new Dimension(applicationProperties.getInt("min-width"), applicationProperties.getInt("min-height"));
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(minimumSize);
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //This will center the JFrame in the middle of the screen
        this.setLocationRelativeTo(null);

        //setBounds(100, 100, 1024, 541);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBorder(null);
        desktopPane.setBackground(SystemColor.window);
        contentPane.add(desktopPane, BorderLayout.CENTER);
        desktopPane.setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
                new RowSpec[] {
                        FormSpecs.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"),}));

        JSplitPane splitPane = new JSplitPane();
        splitPane.setForeground(B2BUtil.mainColor);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.setOneTouchExpandable(false);
        splitPane.setBackground(SystemColor.window);
        desktopPane.add(splitPane, "2, 2, fill, fill");

        JPanel menu = new JPanel();
//        menu.setBackground(SystemColor.textHighlight);
        menu.setBorder(null);
        splitPane.setLeftComponent(menu);
        GridBagLayout gbl_Menu = new GridBagLayout();
        gbl_Menu.columnWidths = new int[] {150, 0};
        gbl_Menu.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gbl_Menu.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_Menu.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        menu.setLayout(gbl_Menu);


        content = new JPanel();
        content.setBackground(B2BUtil.mainColor);
        content.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        splitPane.setRightComponent(content);
        content.setLayout(new CardLayout(0, 0));

        menus = new ArrayList<>();
        for (final TabContainer container : containers) {
            JComponent card = container.getView();
            card.setName(container.getName());
            content.add(card, container.getName());

            final JButton button = createMenuButton(menu, container.getIndex(), container.getName(), container.getIcon(), container.isEnabled());
            menus.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Show card : " + button.getText());
                    CardLayout cl = (CardLayout) (content.getLayout());
                    cl.show(content, button.getText());

                    // TODO 3 - change text color to white and black vice versa !!!!!

                    for (JButton buttonLoop : menus) {
                        buttonLoop.setBackground(B2BUtil.mainColor);
                    }

                    for (JButton buttonLoop : menus) {
                        if (buttonLoop.getText().equals(button.getText())) {
                            buttonLoop.setBackground(B2BUtil.selectedColor);
                        }
                    }
                }
            });
        }

        JPanel footerPanel = new JPanel();
        contentPane.add(footerPanel, BorderLayout.SOUTH);

        JLabel labelSynchronizingBlocks = new JLabel("Synchronizing blocks :");
        footerPanel.add(labelSynchronizingBlocks);
        dataSynchronizingBlocks = new JLabel("Loading ...");
        footerPanel.add(dataSynchronizingBlocks);
        footerPanel.add(progressBar);

        this.pack();
    }

    public void update(Observable rpcPoller, Object data) {
        if (data instanceof Status) {
            Status viewData = (Status) data;
            dataSynchronizingBlocks.setText("" + viewData.getBlockCount() + " / " + viewData.getKnownBlockCount());

            setProgressMax(viewData.getKnownBlockCount());
        }
        if (data instanceof BlockCount) {
            BlockCount blockCount = (BlockCount) data;
            setProgress((int)blockCount.getCount());
        }
    }

    public void setProgressMax(long maxProgress) {
        final long theProgress = maxProgress;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setMaximum((int) theProgress);
                progressBar.updateUI();
            }
        });
        progressBar.setStringPainted(true);
    }

    public void setProgress(int progress) {
        final int theProgress = progress;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(theProgress);
            }
        });
    }

    private JButton createMenuButton(JPanel menu, int yPosition, String buttonCaption, ImageIcon icon, boolean enabled) {
        JPanel buttonShowOverviewPanel = new JPanel();
        buttonShowOverviewPanel.setBackground(B2BUtil.mainColor);
        buttonShowOverviewPanel.setOpaque(true);
        buttonShowOverviewPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        GridBagConstraints gbc_buttonShowOverviewPanel = new GridBagConstraints();
        gbc_buttonShowOverviewPanel.fill = GridBagConstraints.BOTH;
        gbc_buttonShowOverviewPanel.gridx = 0;
        gbc_buttonShowOverviewPanel.gridy = yPosition;
        menu.add(buttonShowOverviewPanel, gbc_buttonShowOverviewPanel);
        GridBagLayout gbl_buttonShowOverviewPanel = new GridBagLayout();
        gbl_buttonShowOverviewPanel.columnWidths = new int[]{0, 0};
        gbl_buttonShowOverviewPanel.rowHeights = new int[]{0, 0};
        gbl_buttonShowOverviewPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_buttonShowOverviewPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        buttonShowOverviewPanel.setLayout(gbl_buttonShowOverviewPanel);

        final JButton buttonShowOverview = new JButton(buttonCaption);
        buttonShowOverview.setIcon(icon);
        buttonShowOverview.setBackground(B2BUtil.mainColor);
        buttonShowOverview.setBorder(null);
        buttonShowOverview.setOpaque(true);
        buttonShowOverview.setEnabled(enabled);

        GridBagConstraints gbc_buttonShowOverview = new GridBagConstraints();
        gbc_buttonShowOverview.fill = GridBagConstraints.BOTH;
        gbc_buttonShowOverview.gridx = 0;
        gbc_buttonShowOverview.gridy = 0;
        buttonShowOverviewPanel.add(buttonShowOverview, gbc_buttonShowOverview);

        return buttonShowOverview;
    }
}

