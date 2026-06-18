package com.autodealer.ui;

import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class SupportPanel extends JPanel {
    private JTextArea taMessages;
    private JTextField tfMessage;
    private JComboBox<String> cbTopic;

    public SupportPanel() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("💬 Поддержка онлайн");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(ColorTheme.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);

        cbTopic = new JComboBox<>(new String[]{"Вопрос по авто", "Вопрос по запчастям", "Тест-драйв", "Бронирование", "Другое"});
        cbTopic.setFont(new Font("Arial", Font.PLAIN, 13));
        top.add(cbTopic, BorderLayout.WEST);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        JLabel status = new JLabel("🟢 Оператор онлайн");
        status.setForeground(ColorTheme.SUCCESS);
        status.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(status);
        top.add(statusPanel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        JPanel chatPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(ColorTheme.CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        chatPanel.setOpaque(false);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        taMessages = new JTextArea(
                "💬 Добро пожаловать в чат поддержки!\n" +
                        "🤖 Напишите ваш вопрос, и мы ответим вам в ближайшее время.\n" +
                        "─────────────────────────────────────────────\n\n"
        );
        taMessages.setEditable(false);
        taMessages.setFont(new Font("Arial", Font.PLAIN, 14));
        taMessages.setBackground(new Color(255, 255, 255, 200));
        taMessages.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(taMessages);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        chatPanel.add(scroll, BorderLayout.CENTER);

        add(chatPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        tfMessage = new JTextField();
        tfMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        tfMessage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tfMessage.addActionListener(e -> sendMessage());
        bottom.add(tfMessage, BorderLayout.CENTER);

        JButton btnSend = new JButton("📤 Отправить");
        btnSend.setBackground(ColorTheme.PRIMARY);
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(new Font("Arial", Font.BOLD, 14));
        btnSend.setFocusPainted(false);
        btnSend.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSend.addActionListener(e -> sendMessage());
        bottom.add(btnSend, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String msg = tfMessage.getText().trim();
        if (msg.isEmpty()) return;

        String topic = (String) cbTopic.getSelectedItem();
        taMessages.append("👤 Вы [" + topic + "]: " + msg + "\n");
        taMessages.append("🤖 Оператор: Спасибо за ваш вопрос! Мы ответим в ближайшее время.\n\n");
        tfMessage.setText("");
        taMessages.setCaretPosition(taMessages.getDocument().getLength());
    }
}
