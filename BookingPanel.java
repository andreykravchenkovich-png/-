package com.autodealer.ui;

import com.autodealer.database.DataStore;
import com.autodealer.models.Car;
import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class BookingPanel extends JPanel {
    private DataStore db;
    private JComboBox<String> carCombo;
    private JTextField tfName, tfPhone;

    public BookingPanel() {
        db = DataStore.getInstance();
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📌 Бронирование автомобиля");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(ColorTheme.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel main = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(ColorTheme.CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        main.setOpaque(false);
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel carLabel = new JLabel("🚗 Автомобиль:");
        carLabel.setFont(new Font("Arial", Font.BOLD, 14));
        main.add(carLabel, gbc);

        carCombo = new JComboBox<>();
        carCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        for (Car car : db.getAvailableCars()) {
            carCombo.addItem(car.getFullName() + " (" + car.getYear() + ") - " +
                    String.format("%,d", car.getPrice()) + " ₽");
        }
        gbc.gridx = 1;
        main.add(carCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel nameLabel = new JLabel("👤 ФИО:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        main.add(nameLabel, gbc);

        tfName = new JTextField(20);
        tfName.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        main.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("📞 Телефон:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        main.add(phoneLabel, gbc);

        tfPhone = new JTextField(20);
        tfPhone.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        main.add(tfPhone, gbc);

        JButton btn = new JButton("📌 Забронировать");
        btn.setBackground(ColorTheme.WARNING);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> bookCar());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        main.add(btn, gbc);

        add(main, BorderLayout.CENTER);
    }

    private void bookCar() {
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Заполните все поля!");
            return;
        }

        String selectedCar = (String) carCombo.getSelectedItem();
        int confirm = JOptionPane.showConfirmDialog(this,
                "📌 Подтверждение бронирования:\n\n" +
                        "🚗 " + selectedCar + "\n" +
                        "👤 " + name + "\n" +
                        "📞 " + phone,
                "Подтверждение", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "✅ Автомобиль забронирован!\n\n" +
                            "🚗 " + selectedCar + "\n" +
                            "👤 " + name,
                    "Успешно", JOptionPane.INFORMATION_MESSAGE);
            tfName.setText("");
            tfPhone.setText("");
        }
    }
}
