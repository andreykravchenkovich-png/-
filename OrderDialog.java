package com.autodealer.ui;

import com.autodealer.database.DataStore;
import com.autodealer.models.Car;
import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class OrderDialog extends JDialog {
    private Car car;
    private DataStore db;
    private JTextField tfName, tfPhone, tfEmail;
    private JComboBox<String> cbPayment;

    public OrderDialog(CarDetailDialog parent, Car car) {
        super(parent, "🛒 Оформление заказа", true);
        this.car = car;
        this.db = DataStore.getInstance();

        setSize(500, 500);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel bgPanel = new JPanel(new BorderLayout()) {
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
            }
        };
        bgPanel.setOpaque(false);
        bgPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Заголовок
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("🛒 Оформление заказа");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(ColorTheme.PRIMARY_DARK);
        mainPanel.add(title, gbc);

        // Автомобиль
        gbc.gridy = 1;
        JLabel carLabel = new JLabel("🚗 " + car.getFullName());
        carLabel.setFont(new Font("Arial", Font.BOLD, 18));
        carLabel.setForeground(ColorTheme.PRIMARY);
        mainPanel.add(carLabel, gbc);

        // Цена
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("💰 Цена: " + String.format("%,d", car.getPrice()) + " ₽");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(ColorTheme.SUCCESS);
        mainPanel.add(priceLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3;

        // ФИО
        gbc.gridx = 0;
        JLabel nameLabel = new JLabel("👤 ФИО клиента:*");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(nameLabel, gbc);

        tfName = new JTextField(20);
        tfName.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(tfName, gbc);

        // Телефон
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel phoneLabel = new JLabel("📞 Телефон:*");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(phoneLabel, gbc);

        tfPhone = new JTextField(20);
        tfPhone.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(tfPhone, gbc);

        // Email
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel emailLabel = new JLabel("✉️ Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(emailLabel, gbc);

        tfEmail = new JTextField(20);
        tfEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(tfEmail, gbc);

        // Способ оплаты
        gbc.gridy = 6;
        gbc.gridx = 0;
        JLabel paymentLabel = new JLabel("💳 Способ оплаты:");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(paymentLabel, gbc);

        cbPayment = new JComboBox<>(new String[]{"Наличные", "Кредит", "Лизинг", "Рассрочка"});
        cbPayment.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(cbPayment, gbc);

        // Кнопки
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        JButton btnConfirm = new JButton("✅ Оформить");
        btnConfirm.setBackground(ColorTheme.SUCCESS);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(e -> confirmOrder());

        JButton btnCancel = new JButton("❌ Отмена");
        btnCancel.setBackground(ColorTheme.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 16));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnConfirm);
        btnPanel.add(btnCancel);
        mainPanel.add(btnPanel, gbc);

        bgPanel.add(mainPanel, BorderLayout.CENTER);
        add(bgPanel);
    }

    private void confirmOrder() {
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        String email = tfEmail.getText().trim();
        String payment = (String) cbPayment.getSelectedItem();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Заполните обязательные поля!\n(ФИО и Телефон)");
            return;
        }

        db.addOrder(car.getId(), car.getFullName(), name, phone, email, payment);
        db.updateCarStatus(car.getId(), "Продан");

        JOptionPane.showMessageDialog(this,
                "✅ Заказ успешно оформлен!\n\n" +
                        "🚗 " + car.getFullName() + "\n" +
                        "👤 " + name + "\n" +
                        "📞 " + phone + "\n" +
                        "💳 " + payment,
                "Успешно", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
