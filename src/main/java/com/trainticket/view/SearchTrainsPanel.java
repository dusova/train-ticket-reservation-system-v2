package com.trainticket.view;

import com.trainticket.dao.TrainDAO;
import com.trainticket.model.Train;
import com.trainticket.model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class SearchTrainsPanel extends JPanel {

    // Modern UI renkleri
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219); // #3498db - Ana mavi renk
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113); // #2ecc71 - Yeşil
    private static final Color ACCENT_COLOR = new Color(231, 76, 60); // #e74c3c - Kırmızı/Turuncu
    private static final Color DARK_COLOR = new Color(52, 73, 94); // #34495e - Koyu mavi/gri
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // #f8f9fa - Açık gri arka plan
    private static final Color CARD_COLOR = new Color(255, 255, 255); // #ffffff - Beyaz panel
    private static final Color TEXT_COLOR = new Color(52, 58, 64); // #343a40 - Koyu gri metin
    private static final Color LIGHT_TEXT_COLOR = new Color(108, 117, 125); // #6c757d - Açık gri metin
    private static final Color BORDER_COLOR = new Color(222, 226, 230); // #dee2e6 - Açık gri border

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private User currentUser;
    private MainFrame parentFrame;

    private JComboBox<String> fromStationComboBox;
    private JComboBox<String> toStationComboBox;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JButton searchButton;
    private JButton clearButton;
    private JTable trainsTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JPanel resultsPanel;
    private JPanel emptyStatePanel;

    private List<Train> searchResults;

    public SearchTrainsPanel(User user, MainFrame parent) {
        this.currentUser = user;
        this.parentFrame = parent;
        initComponents();

        // Sayfa yüklendiğinde paneli hazırla
        setupInitialState();
    }

    private void initComponents() {
        // Panel ayarları
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 40, 40));

        // Başlık
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Tren Bileti Ara");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("İstasyon ve tarih seçerek uygun seferleri bulun");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Arama formu kartı
        JPanel searchFormCard = createSearchFormCard();

        // Arama sonuçları bölümü - başlangıçta görünmez
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);
        resultsPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        resultsPanel.setVisible(false);

        // Sonuç başlığı
        JPanel resultsHeaderPanel = new JPanel(new BorderLayout());
        resultsHeaderPanel.setOpaque(false);
        resultsHeaderPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel resultsLabel = new JLabel("Arama Sonuçları");
        resultsLabel.setFont(SUBTITLE_FONT);
        resultsLabel.setForeground(TEXT_COLOR);
        resultsHeaderPanel.add(resultsLabel, BorderLayout.WEST);

        statusLabel = new JLabel("");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(LIGHT_TEXT_COLOR);
        resultsHeaderPanel.add(statusLabel, BorderLayout.EAST);

        resultsPanel.add(resultsHeaderPanel, BorderLayout.NORTH);

        // Sonuç tablosu
        createResultsTable();

        JScrollPane scrollPane = new JScrollPane(trainsTable);
        scrollPane.setBackground(CARD_COLOR);
        scrollPane.getViewport().setBackground(CARD_COLOR);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Boş durumlar için panel
        emptyStatePanel = createEmptyStatePanel();
        emptyStatePanel.setVisible(false);

        // Ana panel düzeni
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(searchFormCard);
        contentPanel.add(emptyStatePanel);
        contentPanel.add(resultsPanel);

        // Ana panele bileşenleri ekle
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 30, 25, 30)
        ));

        // Form paneli - şehir seçimi ve tarih seçimi
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        formPanel.setOpaque(false);

        // Güzergah seçim paneli
        JPanel routePanel = new JPanel();
        routePanel.setLayout(new BoxLayout(routePanel, BoxLayout.Y_AXIS));
        routePanel.setOpaque(false);

        JLabel routeLabel = new JLabel("Güzergah");
        routeLabel.setFont(BOLD_LABEL_FONT);
        routeLabel.setForeground(TEXT_COLOR);
        routeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Nereden
        JPanel fromPanel = new JPanel(new BorderLayout(10, 0));
        fromPanel.setOpaque(false);
        fromPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel fromLabel = new JLabel("Nereden:");
        fromLabel.setFont(LABEL_FONT);
        fromLabel.setForeground(TEXT_COLOR);

        fromStationComboBox = createStyledComboBox();
        populateStations(fromStationComboBox);

        fromPanel.add(fromLabel, BorderLayout.NORTH);
        fromPanel.add(fromStationComboBox, BorderLayout.CENTER);

        // Nereye
        JPanel toPanel = new JPanel(new BorderLayout(10, 0));
        toPanel.setOpaque(false);
        toPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel toLabel = new JLabel("Nereye:");
        toLabel.setFont(LABEL_FONT);
        toLabel.setForeground(TEXT_COLOR);

        toStationComboBox = createStyledComboBox();
        populateStations(toStationComboBox);

        toPanel.add(toLabel, BorderLayout.NORTH);
        toPanel.add(toStationComboBox, BorderLayout.CENTER);

        routePanel.add(routeLabel);
        routePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        routePanel.add(fromPanel);
        routePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        routePanel.add(toPanel);

        // Tarih seçim paneli
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setOpaque(false);

        JLabel dateLabel = new JLabel("Seyahat Tarihi");
        dateLabel.setFont(BOLD_LABEL_FONT);
        dateLabel.setForeground(TEXT_COLOR);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Tarih seçimi
        JPanel calendarPanel = new JPanel(new BorderLayout(10, 0));
        calendarPanel.setOpaque(false);
        calendarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel selectDateLabel = new JLabel("Tarih Seçin:");
        selectDateLabel.setFont(LABEL_FONT);
        selectDateLabel.setForeground(TEXT_COLOR);

        JPanel dateSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dateSelectionPanel.setOpaque(false);

        // Güncel tarih bilgisi
        LocalDate today = LocalDate.now();

        // Gün için ComboBox
        dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(i);
        }
        dayComboBox.setSelectedItem(today.getDayOfMonth());
        customizeComboBox(dayComboBox, 60);

        dateSelectionPanel.add(dayComboBox);

        // Ay için ComboBox
        String[] months = {"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
                "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(today.getMonthValue() - 1);
        customizeComboBox(monthComboBox, 100);

        dateSelectionPanel.add(monthComboBox);

        // Yıl için ComboBox
        yearComboBox = new JComboBox<>();
        int currentYear = today.getYear();
        for (int i = currentYear; i <= currentYear + 1; i++) {
            yearComboBox.addItem(i);
        }
        yearComboBox.setSelectedItem(currentYear);
        customizeComboBox(yearComboBox, 80);

        dateSelectionPanel.add(yearComboBox);

        calendarPanel.add(selectDateLabel, BorderLayout.NORTH);
        calendarPanel.add(dateSelectionPanel, BorderLayout.CENTER);

        datePanel.add(dateLabel);
        datePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        datePanel.add(calendarPanel);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        clearButton = createStyledButton("Temizle", Color.WHITE, PRIMARY_COLOR);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });

        searchButton = createStyledButton("Tren Ara", PRIMARY_COLOR, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTrains();
            }
        });

        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(searchButton);

        // Form panelini oluştur
        formPanel.add(routePanel);
        formPanel.add(datePanel);

        // Karta ekle
        card.add(formPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void createResultsTable() {
        // Tablo sütunları
        String[] columnNames = {"Tren No", "Tren Adı", "Kalkış", "Varış", "Kalkış Saati", "Varış Saati", "Süre", "Fiyat", ""};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Sadece son sütun (işlem butonu) düzenlenebilir
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8) {
                    return JButton.class; // Son sütun buton içerecek
                }
                return String.class;
            }
        };

        trainsTable = new JTable(tableModel);
        trainsTable.setRowHeight(50);
        trainsTable.setShowGrid(false);
        trainsTable.setIntercellSpacing(new Dimension(0, 10));
        trainsTable.setFillsViewportHeight(true);
        trainsTable.setBackground(CARD_COLOR);
        trainsTable.setSelectionBackground(new Color(236, 242, 248));
        trainsTable.getTableHeader().setBackground(new Color(241, 245, 249));
        trainsTable.getTableHeader().setFont(BOLD_LABEL_FONT);
        trainsTable.getTableHeader().setForeground(TEXT_COLOR);
        trainsTable.setFont(LABEL_FONT);
        trainsTable.setForeground(TEXT_COLOR);

        // Sıralama özelliği ekle
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        trainsTable.setRowSorter(sorter);

        // Hücre içeriklerini ortalamak için (buton sütunu hariç)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < trainsTable.getColumnCount() - 1; i++) {
            trainsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Sütun genişliklerini ayarla
        trainsTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Tren No
        trainsTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Tren Adı
        trainsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Kalkış
        trainsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Varış
        trainsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Kalkış Saati
        trainsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Varış Saati
        trainsTable.getColumnModel().getColumn(6).setPreferredWidth(80); // Süre
        trainsTable.getColumnModel().getColumn(7).setPreferredWidth(80); // Fiyat
        trainsTable.getColumnModel().getColumn(8).setPreferredWidth(100); // İşlem butonu

        // Özel buton render'ı
        trainsTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        trainsTable.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Tablo header ayarları
        JTableHeader header = trainsTable.getTableHeader();
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
    }

    private JPanel createEmptyStatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(50, 30, 50, 30)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // İkon (basit bir görsel)
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Daire arka plan
                g2d.setColor(new Color(52, 152, 219, 40));
                g2d.fillOval(getWidth()/2 - 30, 0, 60, 60);

                // Tren ikonunun basit çizimi
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRoundRect(getWidth()/2 - 25, 15, 50, 20, 5, 5);
                g2d.fillRoundRect(getWidth()/2 - 15, 10, 30, 10, 5, 5);

                // Tekerlekler
                g2d.setColor(DARK_COLOR);
                g2d.fillOval(getWidth()/2 - 15, 30, 8, 8);
                g2d.fillOval(getWidth()/2 + 7, 30, 8, 8);

                g2d.dispose();
            }

            public Dimension getPreferredSize() {
                return new Dimension(100, 60);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emptyLabel = new JLabel("Arama sonucu bulunamadı");
        emptyLabel.setFont(SUBTITLE_FONT);
        emptyLabel.setForeground(TEXT_COLOR);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel suggestionLabel = new JLabel("Farklı tarih veya istasyon seçimi yapabilirsiniz");
        suggestionLabel.setFont(LABEL_FONT);
        suggestionLabel.setForeground(LIGHT_TEXT_COLOR);
        suggestionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(iconPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(emptyLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(suggestionLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void setupInitialState() {
        // İlk yüklendiğinde tüm trenleri yükle
        loadAllTrains();
    }

    private void populateStations(JComboBox<String> comboBox) {
        TrainDAO trainDAO = new TrainDAO();
        List<String> stations = trainDAO.getAllStations();

        comboBox.addItem("Seçiniz...");
        for (String station : stations) {
            comboBox.addItem(station);
        }
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(LABEL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        comboBox.setPreferredSize(new Dimension(0, 40));

        return comboBox;
    }

    private void customizeComboBox(JComboBox<?> comboBox, int width) {
        comboBox.setFont(LABEL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        comboBox.setPreferredSize(new Dimension(width, 38));
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BOLD_LABEL_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor.equals(Color.WHITE) ? PRIMARY_COLOR : bgColor, 1, true));
        button.setPreferredSize(new Dimension(100, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (bgColor.equals(PRIMARY_COLOR)) {
                    button.setBackground(new Color(41, 128, 185)); // Daha koyu mavi
                } else {
                    button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                if (bgColor.equals(Color.WHITE)) {
                    button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                }
            }
        });

        return button;
    }

    private void loadAllTrains() {
        try {
            TrainDAO trainDAO = new TrainDAO();
            searchResults = trainDAO.getAllTrains();

            // Tabloyu temizle
            tableModel.setRowCount(0);

            if (searchResults.isEmpty()) {
                showEmptyState();
                return;
            }

            // Sonuçları tabloya ekle
            addTrainsToTable(searchResults);

            // Sonuç panelini göster
            showResults(searchResults.size() + " sefer bulundu");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Tren seferleri yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    private void searchTrains() {
        // Form değerlerini al ve doğrula
        String fromStation = (String) fromStationComboBox.getSelectedItem();
        String toStation = (String) toStationComboBox.getSelectedItem();

        // En az kalkış ve varış istasyonları seçili olmalı
        if (fromStation == null || fromStation.equals("Seçiniz...") ||
                toStation == null || toStation.equals("Seçiniz...")) {
            showError("Lütfen kalkış ve varış istasyonlarını seçin!");
            return;
        }

        // Aynı şehir kontrolü
        if (fromStation.equals(toStation)) {
            showError("Kalkış ve varış istasyonları aynı olamaz!");
            return;
        }

        // Tarih bilgilerini al
        int day = (Integer) dayComboBox.getSelectedItem();
        int month = monthComboBox.getSelectedIndex() + 1; // 0-tabanlı indeks
        int year = (Integer) yearComboBox.getSelectedItem();

        // Tarih doğrulaması
        LocalDate localDate;
        try {
            localDate = LocalDate.of(year, month, day);

            // Geçmiş tarih kontrolü
            if (localDate.isBefore(LocalDate.now())) {
                showError("Geçmiş bir tarih seçemezsiniz!");
                return;
            }
        } catch (Exception ex) {
            showError("Geçersiz tarih! Lütfen doğru bir tarih seçin.");
            return;
        }

        // Seçilen günün başlangıcı
        LocalDateTime searchDate = LocalDateTime.of(localDate, LocalTime.of(0, 0));

        try {
            // Trenleri ara
            TrainDAO trainDAO = new TrainDAO();
            List<Train> foundTrains = trainDAO.getTrainsByRoute(fromStation, toStation);

            // Tarihine göre filtrele
            List<Train> filteredTrains = new ArrayList<>();
            for (Train train : foundTrains) {
                if (train.getDepartureTime().toLocalDate().equals(localDate)) {
                    filteredTrains.add(train);
                }
            }

            // Sonuçları güncelle
            this.searchResults = filteredTrains;

            // Tabloyu temizle
            tableModel.setRowCount(0);

            if (filteredTrains.isEmpty()) {
                showEmptyState();
                return;
            }

            // Sonuçları tabloya ekle
            addTrainsToTable(filteredTrains);

            // Sonuç panelini göster
            showResults(filteredTrains.size() + " sefer bulundu");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void addTrainsToTable(List<Train> trains) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Train train : trains) {
            String departureDate = train.getDepartureTime().format(dateFormatter);
            String departureTime = train.getDepartureTime().format(timeFormatter);
            String arrivalTime = train.getArrivalTime().format(timeFormatter);

            // Seyahat süresini hesapla
            double durationHours = train.calculateTravelDuration();
            String duration = String.format("%.1fs", durationHours);

            Object[] row = {
                    train.getTrainNumber(),
                    train.getTrainName(),
                    train.getDepartureStation(),
                    train.getArrivalStation(),
                    departureTime,
                    arrivalTime,
                    duration,
                    String.format("%.2f TL", train.getPrice()),
                    "Seç" // Bu buton olarak render edilecek
            };

            tableModel.addRow(row);
        }
    }

    private void showResults(String statusText) {
        emptyStatePanel.setVisible(false);
        resultsPanel.setVisible(true);
        statusLabel.setText(statusText);
        statusLabel.setForeground(LIGHT_TEXT_COLOR);
    }

    private void showEmptyState() {
        resultsPanel.setVisible(false);
        emptyStatePanel.setVisible(true);
    }

    private void showError(String message) {
        resultsPanel.setVisible(true);
        emptyStatePanel.setVisible(false);
        statusLabel.setText(message);
        statusLabel.setForeground(ACCENT_COLOR);
    }

    private void resetForm() {
        fromStationComboBox.setSelectedIndex(0);
        toStationComboBox.setSelectedIndex(0);

        // Güncel tarihe sıfırla
        LocalDate today = LocalDate.now();
        dayComboBox.setSelectedItem(today.getDayOfMonth());
        monthComboBox.setSelectedIndex(today.getMonthValue() - 1);
        yearComboBox.setSelectedItem(today.getYear());

        // Sonuçları ve durum mesajını temizle
        tableModel.setRowCount(0);
        statusLabel.setText("");

        // Tüm seferleri yükle
        loadAllTrains();
    }

    private void selectTrain(int row) {
        if (row >= 0 && row < searchResults.size()) {
            Train selectedTrain = searchResults.get(row);

            // Koltuk seçim ekranını aç
            SeatSelectionPanel seatPanel = new SeatSelectionPanel(currentUser, selectedTrain, parentFrame);
            parentFrame.showPanel(seatPanel, "seatSelection");
        }
    }

    // Buton render sınıfı
    class ButtonRenderer implements TableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton();
            button.setOpaque(true);
            button.setFont(BOLD_LABEL_FONT);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            button.setText((value == null) ? "Seç" : value.toString());
            button.setBackground(SECONDARY_COLOR);
            button.setForeground(Color.WHITE);

            return button;
        }
    }

    // Buton editor sınıfı
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(BOLD_LABEL_FONT);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setBackground(SECONDARY_COLOR);
            button.setForeground(Color.WHITE);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {

            label = (value == null) ? "Seç" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Tıklanan satırı al (sıralanmış tablo için doğru indeks)
                int modelRow = trainsTable.convertRowIndexToModel(trainsTable.getSelectedRow());
                selectTrain(modelRow);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}