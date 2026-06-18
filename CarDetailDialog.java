package com.autodealer.ui;

import com.autodealer.database.DataStore;
import com.autodealer.models.Car;
import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CarDetailDialog extends JDialog {
    private Car car;
    private DataStore db;
    private JLabel lblStatus;
    private JLabel imageLabel;

    public CarDetailDialog(Frame parent, Car car) {
        super(parent, "🚗 " + car.getFullName(), true);
        this.car = car;
        this.db = DataStore.getInstance();

        // На весь экран
        parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        initUI();
        pack();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Фоновый градиент
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(240, 248, 255),
                        0, getHeight(), new Color(200, 225, 245)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Декоративные круги на фоне
                g2d.setColor(new Color(33, 150, 243, 30));
                g2d.fillOval(-100, -100, 400, 400);
                g2d.fillOval(getWidth() - 300, getHeight() - 300, 500, 500);
                g2d.setColor(new Color(255, 193, 7, 20));
                g2d.fillOval(getWidth() - 200, 50, 300, 300);
                g2d.fillOval(50, getHeight() - 250, 350, 350);
            }
        };
        backgroundPanel.setOpaque(false);
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Основной контент
        JPanel mainPanel = new JPanel(new BorderLayout(30, 30));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Верхняя панель с заголовком и кнопкой закрыть
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("🚗 " + car.getFullName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(ColorTheme.PRIMARY_DARK);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnClose = new JButton("✖ Закрыть");
        btnClose.setBackground(ColorTheme.DANGER);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 16));
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        headerPanel.add(btnClose, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Центральная панель с фото и информацией
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Фото (слева)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        JPanel imagePanel = createImagePanel();
        centerPanel.add(imagePanel, gbc);

        // Информация (справа)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        JPanel infoPanel = createInfoPanel();
        centerPanel.add(infoPanel, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Нижняя панель с кнопками действий
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnBook = createActionButton("📌 Забронировать", ColorTheme.WARNING, Color.BLACK);
        btnBook.setEnabled(car.getStatus().equals("В наличии"));
        btnBook.addActionListener(e -> bookCar());

        JButton btnOrder = createActionButton("🛒 Оформить заказ", ColorTheme.SUCCESS, Color.WHITE);
        btnOrder.setEnabled(car.getStatus().equals("В наличии") || car.getStatus().equals("Забронирован"));
        btnOrder.addActionListener(e -> {
            new OrderDialog(this, car).setVisible(true);
            dispose();
        });

        JButton btnCloseBottom = createActionButton("❌ Закрыть", ColorTheme.GRAY, Color.WHITE);
        btnCloseBottom.addActionListener(e -> dispose());

        bottomPanel.add(btnBook);
        bottomPanel.add(btnOrder);
        bottomPanel.add(btnCloseBottom);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        add(backgroundPanel);
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        try {
            URL imgUrl = new URL(car.getImagePath());
            ImageIcon icon = new ImageIcon(imgUrl);
            // Масштабируем под размер экрана
            int maxWidth = 500;
            int maxHeight = 400;
            Image img = icon.getImage();
            int width = img.getWidth(null);
            int height = img.getHeight(null);

            double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

            Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
            imageLabel.setForeground(ColorTheme.PRIMARY);
        }

        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Заголовок информации
        JLabel infoTitle = new JLabel("📋 Информация об автомобиле");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 20));
        infoTitle.setForeground(ColorTheme.PRIMARY_DARK);
        panel.add(infoTitle, gbc);

        gbc.gridwidth = 1;

        // Данные
        String[][] data = {
                {"🚗 Марка:", car.getBrand()},
                {"📋 Модель:", car.getModel()},
                {"📅 Год:", String.valueOf(car.getYear())},
                {"💰 Цена:", String.format("%,d ₽", car.getPrice())},
                {"🔧 Двигатель:", car.getEngine()},
                {"⚙️ Коробка передач:", car.getTransmission()},
                {"🎨 Цвет:", car.getColor()},
                {"📌 Статус:", car.getStatus()}
        };

        int y = 1;
        for (String[] row : data) {
            gbc.gridy = y;
            gbc.gridx = 0;
            gbc.weightx = 0.3;

            JLabel label = new JLabel(row[0]);
            label.setFont(new Font("Arial", Font.BOLD, 15));
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;

            JLabel value = new JLabel(row[1]);
            value.setFont(new Font("Arial", Font.PLAIN, 15));
            if (row[0].equals("📌 Статус:")) {
                if (row[1].equals("В наличии")) {
                    value.setForeground(ColorTheme.SUCCESS);
                    value.setFont(new Font("Arial", Font.BOLD, 16));
                } else if (row[1].equals("Забронирован")) {
                    value.setForeground(ColorTheme.WARNING);
                    value.setFont(new Font("Arial", Font.BOLD, 16));
                } else {
                    value.setForeground(ColorTheme.DANGER);
                    value.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            panel.add(value, gbc);
            y++;
        }

        return panel;
    }

    private JButton createActionButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    private void bookCar() {
        if (car.getStatus().equals("В наличии")) {
            db.updateCarStatus(car.getId(), "Забронирован");
            car.setStatus("Забронирован");
            JOptionPane.showMessageDialog(this, "✅ Автомобиль забронирован!");
            dispose();
        }
    }
}
