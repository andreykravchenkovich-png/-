package com.autodealer.ui;

import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("🚗 AutoDealer - Автосалон");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Контент с градиентным фоном
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, ColorTheme.BG_START,
                        0, getHeight(), ColorTheme.BG_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setOpaque(false);

        contentPanel.add(new CarCatalogPanel(), "cars");
        contentPanel.add(new PartsPanel(), "parts");
        contentPanel.add(new TestDrivePanel(), "testdrive");
        contentPanel.add(new BookingPanel(), "booking");
        contentPanel.add(new AboutPanel(), "about");
        contentPanel.add(new SupportPanel(), "support");

        add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        cardLayout.show(contentPanel, "cars");
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(33, 150, 243),
                        getWidth(), 0, new Color(25, 118, 210)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setPreferredSize(new Dimension(0, 80));

        JLabel logo = new JLabel("🚗 AutoDealer");
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        panel.add(logo, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        navPanel.setOpaque(false);

        String[] items = {"🚗 Автомобили", "🔧 Запчасти", "🔄 Тест-драйв", "📌 Бронирование", "ℹ️ О нас", "💬 Поддержка"};
        String[] names = {"cars", "parts", "testdrive", "booking", "about", "support"};

        for (int i = 0; i < items.length; i++) {
            JButton btn = new JButton(items[i]);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(255, 255, 255, 30));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setOpaque(true);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(255, 255, 255, 60));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(255, 255, 255, 30));
                }
            });

            final String name = names[i];
            btn.addActionListener(e -> cardLayout.show(contentPanel, name));
            navPanel.add(btn);
        }

        panel.add(navPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 33, 33));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel banner = new JLabel("🔥 Спецпредложение! Скидка 15% на все запчасти до конца месяца!");
        banner.setFont(new Font("Arial", Font.BOLD, 16));
        banner.setForeground(new Color(255, 193, 7));
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(banner, BorderLayout.CENTER);

        return panel;
    }
}
