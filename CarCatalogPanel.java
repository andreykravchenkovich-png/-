package com.autodealer.ui;

import com.autodealer.database.DataStore;
import com.autodealer.models.Car;
import com.autodealer.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CarCatalogPanel extends JPanel {
    private DataStore db;
    private JPanel cardsPanel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JComboBox<String> brandFilter;
    private List<Car> currentCars;

    public CarCatalogPanel() {
        db = DataStore.getInstance();
        currentCars = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Верхняя панель с заголовком и поиском
        JPanel topPanel = new JPanel(new BorderLayout(15, 10));
        topPanel.setOpaque(false);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        JLabel title = new JLabel("🚗 Каталог автомобилей");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(ColorTheme.PRIMARY_DARK);
        leftPanel.add(title);

        JLabel count = new JLabel("Всего: " + db.getAllCars().size() + " авто");
        count.setFont(new Font("Arial", Font.PLAIN, 14));
        count.setForeground(Color.GRAY);
        leftPanel.add(count);

        topPanel.add(leftPanel, BorderLayout.WEST);

        // Панель поиска
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        // Поиск по тексту
        searchPanel.add(new JLabel("🔍 Поиск:"));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
        searchPanel.add(searchField);

        // Фильтр по марке
        searchPanel.add(new JLabel("Марка:"));
        brandFilter = new JComboBox<>();
        brandFilter.addItem("Все марки");
        for (Car car : db.getAllCars()) {
            if (!brandFilterContains(car.getBrand())) {
                brandFilter.addItem(car.getBrand());
            }
        }
        brandFilter.setFont(new Font("Arial", Font.PLAIN, 12));
        brandFilter.addActionListener(e -> performSearch());
        searchPanel.add(brandFilter);

        // Фильтр по статусу
        searchPanel.add(new JLabel("Статус:"));
        filterCombo = new JComboBox<>(new String[]{"Все", "В наличии", "Забронирован", "Продан"});
        filterCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        filterCombo.addActionListener(e -> performSearch());
        searchPanel.add(filterCombo);

        // Кнопка поиска
        JButton btnSearch = new JButton("🔍 Найти");
        btnSearch.setBackground(ColorTheme.PRIMARY);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 12));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> performSearch());
        searchPanel.add(btnSearch);

        // Кнопка сброса
        JButton btnReset = new JButton("✖ Сброс");
        btnReset.setBackground(ColorTheme.GRAY);
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Arial", Font.BOLD, 12));
        btnReset.setFocusPainted(false);
        btnReset.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetSearch());
        searchPanel.add(btnReset);

        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Карточки
        cardsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        cardsPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadCars();
    }

    private boolean brandFilterContains(String brand) {
        for (int i = 0; i < brandFilter.getItemCount(); i++) {
            if (brandFilter.getItemAt(i).equals(brand)) {
                return true;
            }
        }
        return false;
    }

    private void loadCars() {
        currentCars = db.getAllCars();
        displayCars(currentCars);
    }

    private void resetSearch() {
        searchField.setText("");
        brandFilter.setSelectedIndex(0);
        filterCombo.setSelectedIndex(0);
        performSearch();
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        String filter = (String) filterCombo.getSelectedItem();
        String brand = (String) brandFilter.getSelectedItem();

        List<Car> filtered = new ArrayList<>();

        for (Car car : db.getAllCars()) {
            boolean matchesSearch = true;
            boolean matchesFilter = true;
            boolean matchesBrand = true;

            // Поиск по тексту (марка, модель, год, цена)
            if (!query.isEmpty()) {
                matchesSearch = car.getFullName().toLowerCase().contains(query) ||
                        car.getBrand().toLowerCase().contains(query) ||
                        car.getModel().toLowerCase().contains(query) ||
                        String.valueOf(car.getYear()).contains(query) ||
                        String.valueOf(car.getPrice()).contains(query);
            }

            // Фильтр по марке
            if (brand != null && !brand.equals("Все марки")) {
                matchesBrand = car.getBrand().equals(brand);
            }

            // Фильтр по статусу
            if (filter != null && !filter.equals("Все")) {
                matchesFilter = car.getStatus().equals(filter);
            }

            if (matchesSearch && matchesBrand && matchesFilter) {
                filtered.add(car);
            }
        }

        displayCars(filtered);
    }

    private void displayCars(List<Car> cars) {
        cardsPanel.removeAll();
        currentCars = cars;

        if (cars.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setOpaque(false);

            JLabel emptyLabel = new JLabel("🔍 Автомобилей не найдено");
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 24));
            emptyLabel.setForeground(Color.GRAY);
            emptyPanel.add(emptyLabel);

            JLabel hint = new JLabel("Попробуйте изменить параметры поиска");
            hint.setFont(new Font("Arial", Font.PLAIN, 14));
            hint.setForeground(Color.LIGHT_GRAY);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 1;
            emptyPanel.add(hint, gbc);

            cardsPanel.add(emptyPanel);
        } else {
            for (Car car : cars) {
                cardsPanel.add(createCarCard(car));
            }
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCarCard(Car car) {
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

        // Фото автомобиля
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 160));

        try {
            URL imgUrl = new URL(car.getImagePath());
            ImageIcon icon = new ImageIcon(imgUrl);
            Image img = icon.getImage().getScaledInstance(250, 160, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
            imageLabel.setForeground(ColorTheme.PRIMARY);
        }
        card.add(imageLabel, BorderLayout.NORTH);

        // Информация
        JPanel info = new JPanel(new GridLayout(5, 1, 5, 3));
        info.setOpaque(false);

        JLabel name = new JLabel(car.getFullName());
        name.setFont(new Font("Arial", Font.BOLD, 16));
        info.add(name);

        JLabel year = new JLabel("📅 " + car.getYear() + " • " + car.getColor());
        year.setFont(new Font("Arial", Font.PLAIN, 13));
        info.add(year);

        JLabel engine = new JLabel("🔧 " + car.getEngine() + " • " + car.getTransmission());
        engine.setFont(new Font("Arial", Font.PLAIN, 13));
        info.add(engine);

        JLabel price = new JLabel("💰 " + String.format("%,d", car.getPrice()) + " ₽");
        price.setFont(new Font("Arial", Font.BOLD, 16));
        price.setForeground(ColorTheme.SUCCESS);
        info.add(price);

        JLabel status = new JLabel("● " + car.getStatus());
        status.setFont(new Font("Arial", Font.BOLD, 13));
        if (car.getStatus().equals("В наличии")) {
            status.setForeground(ColorTheme.SUCCESS);
        } else if (car.getStatus().equals("Забронирован")) {
            status.setForeground(ColorTheme.WARNING);
        } else {
            status.setForeground(ColorTheme.DANGER);
        }
        info.add(status);

        card.add(info, BorderLayout.CENTER);

        // Кнопка
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        JButton btn = new JButton("📋 Подробнее");
        btn.setBackground(ColorTheme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showCarDetail(car));
        btnPanel.add(btn);
        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showCarDetail(Car car) {
        // Открываем диалог на весь экран
        new CarDetailDialog((Frame) SwingUtilities.getWindowAncestor(this), car);
        performSearch(); // Обновляем список после изменений
    }
}
