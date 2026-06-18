package com.autodealer.ui;

import com.autodealer.database.DataStore;
import com.autodealer.models.Car;
import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class TestDrivePanel extends JPanel {
    private DataStore db;
    private JComboBox<String> carCombo;
    private JTextField tfName, tfPhone, tfDate, tfTime;

    public TestDrivePanel() {
        db = DataStore.getInstance();
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🔄 Запись на тест-драйв");
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
            carCombo.addItem(car.getFullName() + " (" + car.getYear() + ")");
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

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel dateLabel = new JLabel("📅 Дата:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        main.add(dateLabel, gbc);

        tfDate = new JTextField("дд.мм.гггг", 20);
        tfDate.setFont(new Font("Arial", Font.PLAIN, 14));
        tfDate.setForeground(Color.GRAY);
        gbc.gridx = 1;
        main.add(tfDate, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel timeLabel = new JLabel("⏰ Время:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        main.add(timeLabel, gbc);

        tfTime = new JTextField("чч:мм", 20);
        tfTime.setFont(new Font("Arial", Font.PLAIN, 14));
        tfTime.setForeground(Color.GRAY);
        gbc.gridx = 1;
        main.add(tfTime, gbc);

        JButton btn = new JButton("✅ Записаться на тест-драйв");
        btn.setBackground(ColorTheme.SUCCESS);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> submitTestDrive());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        main.add(btn, gbc);

        add(main, BorderLayout.CENTER);
    }

    private void submitTestDrive() {
        String car = (String) carCombo.getSelectedItem();
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        String date = tfDate.getText().trim();
        String time = tfTime.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || time.isEmpty() ||
                date.equals("дд.мм.гггг") || time.equals("чч:мм")) {
            JOptionPane.showMessageDialog(this, "❌ Заполните все поля!");
            return;
        }

        db.addTestDrive(car, name, phone, date, time);
        JOptionPane.showMessageDialog(this,
                "✅ Вы записаны на тест-драйв!\n\n" +
                        "🚗 " + car + "\n" +
                        "👤 " + name + "\n" +
                        "📞 " + phone + "\n" +
                        "📅 " + date + " " + time,
                "Успешно", JOptionPane.INFORMATION_MESSAGE);

        tfName.setText("");
        tfPhone.setText("");
        tfDate.setText("дд.мм.гггг");
        tfTime.setText("чч:мм");
    }
}
