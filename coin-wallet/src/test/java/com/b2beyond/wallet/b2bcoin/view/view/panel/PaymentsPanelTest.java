package com.b2beyond.wallet.b2bcoin.view.view.panel;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;


public class PaymentsPanelTest {

    private FrameFixture window;
    private PaymentsPanel paymentsPanel;

    @Before
    public void setUp() {
        paymentsPanel = GuiActionRunner.execute(new Callable<PaymentsPanel>() {
            @Override
            public PaymentsPanel call() throws Exception {
                return new PaymentsPanel();
            }
        });

        JFrame frame = new JFrame("testing payments panel");
        frame.add(paymentsPanel);

        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testGetNumberOfPayments() throws Exception {
        this.paymentsPanel.getNumberOfPayments().setText("20");

        Assert.assertTrue(window.label("numberOfPayments").text().equals("20"));

        this.paymentsPanel.getNumberOfPayments().setText("0.200000000000");

        Assert.assertTrue(window.label("numberOfPayments").text().equals("0.200000000000"));
    }

    @Test
    public void testGetTotalPaymentsAmount() throws Exception {
        this.paymentsPanel.getTotalPaymentsAmount().setText("40");

        Assert.assertTrue(window.label("totalPaymentsAmount").text().equals("40"));

        this.paymentsPanel.getTotalPaymentsAmount().setText("0.586511515453");

        Assert.assertTrue(window.label("totalPaymentsAmount").text().equals("0.586511515453"));
    }

    @Test
    public void testGetTotalPaymentsLockedAmount() throws Exception {
        this.paymentsPanel.getTotalPaymentsLockedAmount().setText("456789");

        Assert.assertTrue(window.label("totalPaymentsLockedAmount").text().equals("456789"));

        this.paymentsPanel.getTotalPaymentsLockedAmount().setText("6656.586511515453");

        Assert.assertTrue(window.label("totalPaymentsLockedAmount").text().equals("6656.586511515453"));

    }
}