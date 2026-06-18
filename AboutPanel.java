package com.autodealer.ui;

import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel {
    public AboutPanel() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("ℹ️ О нас");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(ColorTheme.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(ColorTheme.CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JTextArea text = new JTextArea(
                "🚗 AutoDealer - ваш надежный партнер в мире автомобилей!\n\n" +
                        "⭐ Мы предлагаем:\n" +
                        "  • Широкий выбор новых автомобилей\n" +
                        "  • Оригинальные запчасти\n" +
                        "  • Профессиональный сервис\n" +
                        "  • Удобные условия покупки\n" +
                        "  • Кредит и лизинг\n\n" +
                        "📍 Адрес: г. Москва, ул. Автомобильная, 1\n" +
                        "📞 Телефон: +7 (495) 123-45-67\n" +
                        "✉️ Email: info@autodealer.ru\n" +
                        "🌐 Сайт: www.autodealer.ru\n\n" +
                        "⏰ Режим работы:\n" +
                        "  Пн-Пт: 9:00 - 21:00\n" +
                        "  Сб: 10:00 - 19:00\n" +
                        "  Вс: Выходной"
        );
        text.setFont(new Font("Arial", Font.PLAIN, 15));
        text.setEditable(false);
        text.setBackground(new Color(255, 255, 255, 0));
        text.setForeground(Color.BLACK);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        mainPanel.add(text, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
}
