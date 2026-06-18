package com.autodealer.ui;

import com.autodealer.database.DataStore;
import com.autodealer.models.Part;
import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class PartsPanel extends JPanel {
    private DataStore db;
    private JPanel cardsPanel;
    private JComboBox<String> categoryFilter;

    public PartsPanel() {
        db = DataStore.getInstance();
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout(15, 10));
        top.setOpaque(false);

        JLabel title = new JLabel("🔧 Каталог запчастей");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(ColorTheme.PRIMARY_DARK);
        top.add(title, BorderLayout.WEST);

        String[] cats = {"Все", "Масла", "Электрика", "Аксессуары", "Стекла", "Тормозная система", "Фильтры", "Колеса"};
        categoryFilter = new JComboBox<>(cats);
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 13));
        categoryFilter.addActionListener(e -> loadParts());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("📁 Категория:"));
        filterPanel.add(categoryFilter);
        top.add(filterPanel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        cardsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        loadParts();
    }

    private void loadParts() {
        cardsPanel.removeAll();
        String selected = (String) categoryFilter.getSelectedItem();
        List<Part> parts = (selected == null || selected.equals("Все"))
                ? db.getAllParts() : db.getPartsByCategory(selected);

        for (Part p : parts) {
            cardsPanel.add(createPartCard(p));
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createPartCard(Part part) {
        JPanel card = new JPanel(new BorderLayout(5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(ColorTheme.CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 130));
        try {
            URL imgUrl = new URL(part.getImagePath());
            ImageIcon icon = new ImageIcon(imgUrl);
            Image img = icon.getImage().getScaledInstance(200, 130, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("🔧");
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        }
        card.add(imageLabel, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(4, 1, 5, 3));
        info.setOpaque(false);

        JLabel name = new JLabel(part.getName());
        name.setFont(new Font("Arial", Font.BOLD, 14));
        info.add(name);

        JLabel category = new JLabel("📁 " + part.getCategory());
        category.setFont(new Font("Arial", Font.PLAIN, 12));
        category.setForeground(Color.GRAY);
        info.add(category);

        JLabel price = new JLabel("💰 " + String.format("%,d", part.getPrice()) + " ₽");
        price.setFont(new Font("Arial", Font.BOLD, 16));
        price.setForeground(ColorTheme.SUCCESS);
        info.add(price);

        JLabel desc = new JLabel(part.getDescription());
        desc.setFont(new Font("Arial", Font.PLAIN, 11));
        desc.setForeground(Color.GRAY);
        info.add(desc);

        card.add(info, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        JButton btn = new JButton("🛒 В корзину");
        btn.setBackground(ColorTheme.SUCCESS);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "✅ " + part.getName() + " добавлен в корзину!\n" +
                            "💰 Цена: " + String.format("%,d", part.getPrice()) + " ₽",
                    "Корзина", JOptionPane.INFORMATION_MESSAGE);
        });
        btnPanel.add(btn);
        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }
}
