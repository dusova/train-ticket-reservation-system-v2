package com.trainticket.view;

import com.trainticket.dao.SeatDAO;
import com.trainticket.model.Seat;
import com.trainticket.model.Train;
import com.trainticket.model.User;
import com.trainticket.model.Wagon;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class SeatSelectionPanel extends JPanel {

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

    // Koltuk renkleri
    private static final Color SEAT_AVAILABLE = new Color(46, 204, 113, 230); // Boş koltuk - Yeşil
    private static final Color SEAT_RESERVED = new Color(231, 76, 60, 230); // Rezerve koltuk - Kırmızı
    private static final Color SEAT_SELECTED = new Color(52, 152, 219, 230); // Seçili koltuk - Mavi
    private static final Color SEAT_HOVER = new Color(241, 196, 15, 230); // Hover durumu - Sarı

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private User currentUser;
    private Train selectedTrain;
    private MainFrame parentFrame;

    private JComboBox<Wagon> wagonComboBox;
    private JPanel seatLayoutPanel;
    private JScrollPane seatScrollPane;
    private JButton continueButton;
    private JButton backButton;
    private JLabel seatInfoLabel;
    private JLabel selectedSeatLabel;

    private Wagon selectedWagon;
    private Seat selectedSeat;
    private List<JToggleButton> seatButtons = new ArrayList<>();

    public SeatSelectionPanel(User user, Train train, MainFrame parent) {
        this.currentUser = user;
        this.selectedTrain = train;
        this.parentFrame = parent;
        initComponents();
    }

    private void initComponents() {
        // Panel ayarları
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 40, 40));

        // Başlık
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Koltuk Seçimi");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Lütfen yolculuk yapacağınız vagonu ve koltuğu seçin");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Ana içerik paneli
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);

        // Üst panel: Tren bilgisi kartı ve vagon seçimi
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setOpaque(false);

        // Tren bilgisi kartı
        JPanel trainInfoCard = createTrainInfoCard();

        // Vagon seçim kartı
        JPanel wagonSelectionCard = createWagonSelectionCard();

        topPanel.add(trainInfoCard);
        topPanel.add(wagonSelectionCard);

        // Orta panel: Koltuk düzeni
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setOpaque(false);

        JPanel seatSelectionCard = createSeatSelectionCard();
        middlePanel.add(seatSelectionCard, BorderLayout.CENTER);

        // Alt panel: Butonlar
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        backButton = createStyledButton("Geri", Color.WHITE, PRIMARY_COLOR);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        continueButton = createStyledButton("Devam Et", PRIMARY_COLOR, Color.WHITE);
        continueButton.setEnabled(false);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueToPassengerInfo();
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(continueButton);

        // Seçili koltuk bilgisi
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setOpaque(false);

        selectedSeatLabel = new JLabel("Seçili Koltuk: Henüz seçilmedi");
        selectedSeatLabel.setFont(BOLD_LABEL_FONT);
        selectedSeatLabel.setForeground(TEXT_COLOR);
        infoPanel.add(selectedSeatLabel);

        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Ana panel düzeni
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(middlePanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Ana panele bileşenleri ekle
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Vagonları yükle
        loadWagons();
    }

    private JPanel createTrainInfoCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        // Renkli başlık bandı
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 5));
        card.add(headerPanel, BorderLayout.NORTH);

        // Tren bilgisi
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        infoPanel.setOpaque(false);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        addDetailRow(infoPanel, "Tren:", selectedTrain.getTrainNumber() + " - " + selectedTrain.getTrainName());
        addDetailRow(infoPanel, "Güzergah:", selectedTrain.getDepartureStation() + " → " + selectedTrain.getArrivalStation());
        addDetailRow(infoPanel, "Tarih:", selectedTrain.getDepartureTime().format(dateFormatter));
        addDetailRow(infoPanel, "Kalkış:", selectedTrain.getDepartureTime().format(timeFormatter));
        addDetailRow(infoPanel, "Varış:", selectedTrain.getArrivalTime().format(timeFormatter));

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createWagonSelectionCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        // Renkli başlık bandı
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SECONDARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 5));
        card.add(headerPanel, BorderLayout.NORTH);

        // Başlık
        JLabel titleLabel = new JLabel("Vagon Seçimi");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        // İçerik paneli
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel wagonLabel = new JLabel("Vagon Seçin:");
        wagonLabel.setFont(LABEL_FONT);
        wagonLabel.setForeground(TEXT_COLOR);
        wagonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        wagonComboBox = new JComboBox<>();
        wagonComboBox.setFont(LABEL_FONT);
        wagonComboBox.setBackground(Color.WHITE);
        wagonComboBox.setForeground(TEXT_COLOR);
        wagonComboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        wagonComboBox.setPreferredSize(new Dimension(0, 40));
        wagonComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        wagonComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Vagon bilgisi paneli
        JPanel wagonInfoPanel = new JPanel();
        wagonInfoPanel.setLayout(new BoxLayout(wagonInfoPanel, BoxLayout.Y_AXIS));
        wagonInfoPanel.setOpaque(false);
        wagonInfoPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        wagonInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel wagonTypeLabel = new JLabel("Vagon Tipi: ");
        wagonTypeLabel.setFont(BOLD_LABEL_FONT);
        wagonTypeLabel.setForeground(TEXT_COLOR);
        wagonTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel availableSeatsLabel = new JLabel("Boş Koltuk: ");
        availableSeatsLabel.setFont(BOLD_LABEL_FONT);
        availableSeatsLabel.setForeground(TEXT_COLOR);
        availableSeatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        wagonInfoPanel.add(wagonTypeLabel);
        wagonInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        wagonInfoPanel.add(availableSeatsLabel);

        // Koltuk lejantı
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Boş koltuk
        JPanel availableLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        availableLegend.setOpaque(false);

        JPanel availableColor = new JPanel();
        availableColor.setBackground(SEAT_AVAILABLE);
        availableColor.setPreferredSize(new Dimension(20, 20));
        availableColor.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JLabel availableLabel = new JLabel("Boş");
        availableLabel.setFont(SMALL_FONT);
        availableLabel.setForeground(TEXT_COLOR);

        availableLegend.add(availableColor);
        availableLegend.add(availableLabel);

        // Dolu koltuk
        JPanel reservedLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        reservedLegend.setOpaque(false);

        JPanel reservedColor = new JPanel();
        reservedColor.setBackground(SEAT_RESERVED);
        reservedColor.setPreferredSize(new Dimension(20, 20));
        reservedColor.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JLabel reservedLabel = new JLabel("Dolu");
        reservedLabel.setFont(SMALL_FONT);
        reservedLabel.setForeground(TEXT_COLOR);

        reservedLegend.add(reservedColor);
        reservedLegend.add(reservedLabel);

        // Seçili koltuk
        JPanel selectedLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        selectedLegend.setOpaque(false);

        JPanel selectedColor = new JPanel();
        selectedColor.setBackground(SEAT_SELECTED);
        selectedColor.setPreferredSize(new Dimension(20, 20));
        selectedColor.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JLabel selectedLabel = new JLabel("Seçili");
        selectedLabel.setFont(SMALL_FONT);
        selectedLabel.setForeground(TEXT_COLOR);

        selectedLegend.add(selectedColor);
        selectedLegend.add(selectedLabel);

        legendPanel.add(availableLegend);
        legendPanel.add(reservedLegend);
        legendPanel.add(selectedLegend);

        // Vagon Seçimi değiştiğinde
        wagonComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedWagon();

                // Vagon bilgisini güncelle
                if (selectedWagon != null) {
                    wagonTypeLabel.setText("Vagon Tipi: " + selectedWagon.getWagonType());

                    int availableCount = 0;
                    for (Seat seat : selectedWagon.getSeats()) {
                        if (!seat.isReserved()) {
                            availableCount++;
                        }
                    }
                    availableSeatsLabel.setText("Boş Koltuk: " + availableCount + " / " + selectedWagon.getTotalSeats());
                }
            }
        });

        // İçerik paneline bileşenleri ekle
        contentPanel.add(wagonLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(wagonComboBox);
        contentPanel.add(wagonInfoPanel);
        contentPanel.add(legendPanel);

        // Kartın ana düzeni
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSeatSelectionCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        // Başlık
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0)); // Alt boşluk eklendi

        JPanel titlesPanel = new JPanel(new BorderLayout(10, 0));
        titlesPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Koltuk Düzeni");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        // Yeni panel - koltuk bilgisi için
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);

        // Bilgi etiketi daha belirgin hale getirildi
        seatInfoLabel = new JLabel("Lütfen bir koltuk seçin");
        seatInfoLabel.setFont(BOLD_LABEL_FONT);
        seatInfoLabel.setForeground(SECONDARY_COLOR); // Yeşil renk
        seatInfoLabel.setBorder(new CompoundBorder(
                new LineBorder(SECONDARY_COLOR.brighter(), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        infoPanel.add(seatInfoLabel);

        titlesPanel.add(titleLabel, BorderLayout.WEST);
        titlesPanel.add(infoPanel, BorderLayout.EAST);

        headerPanel.add(titlesPanel, BorderLayout.CENTER);

        // Ön bilgi paneli (tren yönü)
        JPanel directionPanel = new JPanel(new BorderLayout());
        directionPanel.setOpaque(false);
        directionPanel.setBorder(new EmptyBorder(10, 0, 15, 0));

        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        arrowPanel.setOpaque(false);

        JLabel directionLabel = new JLabel("Yön: " + selectedTrain.getDepartureStation() + " → " + selectedTrain.getArrivalStation());
        directionLabel.setFont(BOLD_LABEL_FONT);
        directionLabel.setForeground(TEXT_COLOR);

        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        arrowLabel.setForeground(PRIMARY_COLOR);

        arrowPanel.add(directionLabel);
        arrowPanel.add(Box.createHorizontalStrut(10));
        arrowPanel.add(arrowLabel);

        directionPanel.add(arrowPanel, BorderLayout.CENTER);

        // Koltuk düzeni paneli - Tren şekli
        seatLayoutPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Tren şeklini çiz
                g2d.setColor(new Color(230, 244, 244));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Tren kenarlarını çiz
                g2d.setColor(new Color(200, 220, 220));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);

                // Alt bilgi yazısı kaldırıldı

                g2d.dispose();
            }
        };

        // Koltukların yatayda düzenlenebilmesi için tam boyutlandırma
        seatLayoutPanel.setLayout(null); // Absolute positioning
        seatLayoutPanel.setOpaque(false);

        // Yatay kaydırma için ScrollPane
        seatScrollPane = new JScrollPane(seatLayoutPanel);
        seatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        seatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        seatScrollPane.setBorder(null);
        seatScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        seatScrollPane.setOpaque(false);
        seatScrollPane.getViewport().setOpaque(false);

        // Trene uygun yükseklik
        seatScrollPane.setPreferredSize(new Dimension(700, 250)); // Yükseklik arttırıldı

        // Kartın ana düzeni
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(directionPanel, BorderLayout.CENTER);
        card.add(seatScrollPane, BorderLayout.SOUTH);

        return card;
    }

    private void loadWagons() {
        com.trainticket.dao.WagonDAO wagonDAO = new com.trainticket.dao.WagonDAO();
        List<Wagon> wagons = wagonDAO.getWagonsByTrainId(selectedTrain.getId());

        wagonComboBox.removeAllItems();

        if (wagons.isEmpty()) {
            // Vagon yoksa oluştur
            createDefaultWagons();
            wagons = wagonDAO.getWagonsByTrainId(selectedTrain.getId());
        }

        // ComboBox'a vagonları ekle
        for (Wagon wagon : wagons) {
            wagonComboBox.addItem(wagon);
        }

        // İlk vagonu seç
        if (!wagons.isEmpty()) {
            selectedWagon = wagons.get(0);
            updateSeatLayout();
        }
    }

    private void createDefaultWagons() {
        // Varsayılan vagonları oluştur
        com.trainticket.dao.WagonDAO wagonDAO = new com.trainticket.dao.WagonDAO();

        // Ekonomi vagon
        Wagon economyWagon = new Wagon();
        economyWagon.setTrainId(selectedTrain.getId());
        economyWagon.setWagonNumber(1);
        economyWagon.setWagonType("Ekonomi");
        economyWagon.setTotalSeats(40);
        wagonDAO.addWagon(economyWagon);

        // Business vagon
        Wagon businessWagon = new Wagon();
        businessWagon.setTrainId(selectedTrain.getId());
        businessWagon.setWagonNumber(2);
        businessWagon.setWagonType("Business");
        businessWagon.setTotalSeats(20);
        wagonDAO.addWagon(businessWagon);
    }

    private void updateSelectedWagon() {
        selectedWagon = (Wagon) wagonComboBox.getSelectedItem();
        if (selectedWagon != null) {
            updateSeatLayout();
            // Seçili koltuğu temizle
            selectedSeat = null;
            continueButton.setEnabled(false);
            selectedSeatLabel.setText("Seçili Koltuk: Henüz seçilmedi");
        }
    }

    private void updateSeatLayout() {
        // Koltuk panelini temizle
        seatLayoutPanel.removeAll();
        seatButtons.clear();

        if (selectedWagon == null) {
            seatLayoutPanel.revalidate();
            seatLayoutPanel.repaint();
            return;
        }

        // Koltukları yükle
        SeatDAO seatDAO = new SeatDAO();
        List<Seat> seats = seatDAO.getSeatsByWagonId(selectedWagon.getId());

        if (seats.isEmpty()) {
            seatInfoLabel.setText("Bu vagonda koltuk bulunamadı");
            seatLayoutPanel.revalidate();
            seatLayoutPanel.repaint();
            return;
        }

        // Koltuk düzeni parametreleri
        int totalSeats = seats.size();

        // Koltuk boyutları ve aralıkları
        int seatWidth = 50;  // Genişletilmiş koltuk boyutu
        int seatHeight = 50; // Genişletilmiş koltuk boyutu
        int seatSpacing = 10; // Arttırılmış aralık

        // Tren ve koltuk düzeni - Genişletilmiş yerleşim
        int trainWidth = (seatWidth + seatSpacing) * 12; // Daha genişletilmiş
        int trainHeight = 180;

        // Panel boyutunu ayarla
        seatLayoutPanel.setPreferredSize(new Dimension(trainWidth, trainHeight));

        // Koltukları düzenle - Daha dengeli yerleşim
        // Üst sıra: Tek numaralı (1, 3, 5...)
        // Alt sıra: Çift numaralı (2, 4, 6...)
        int topRowY = 30;    // Üst kenardan boşluk arttırıldı
        int bottomRowY = 110; // Alt sıra pozisyonu ayarlandı

        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            int seatNumber = seat.getSeatNumber();

            // X pozisyonunu hesapla (yatay düzen için)
            int xPos;
            int yPos;

            if (seatNumber % 2 == 1) {
                // Tek sayılı koltuklar üst sırada
                xPos = 30 + ((seatNumber - 1) / 2) * (seatWidth + seatSpacing + 10); // Ekstra boşluk eklendi
                yPos = topRowY;
            } else {
                // Çift sayılı koltuklar alt sırada
                xPos = 30 + ((seatNumber - 2) / 2) * (seatWidth + seatSpacing + 10); // Ekstra boşluk eklendi
                yPos = bottomRowY;
            }

            // Koltuk butonunu oluştur
            SeatButton seatButton = new SeatButton(seat);
            seatButton.setBounds(xPos, yPos, seatWidth, seatHeight);

            // Koltuk tıklama olayı
            seatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSeatSelection((SeatButton) e.getSource());
                }
            });

            seatLayoutPanel.add(seatButton);
            seatButtons.add(seatButton);
        }

        // Yenileme
        seatLayoutPanel.revalidate();
        seatLayoutPanel.repaint();

        // Bilgilendirme mesajını güncelle - Başlık panel değişikliği
        long availableCount = seats.stream().filter(s -> !s.isReserved()).count();

        // Bilgilendirme metni daha görünür hale getirildi
        seatInfoLabel.setText(availableCount + " / " + seats.size() + " koltuk boşta");
        seatInfoLabel.setFont(BOLD_LABEL_FONT);
        seatInfoLabel.setForeground(SECONDARY_COLOR);
    }

    private void handleSeatSelection(SeatButton clickedButton) {
        // Daha önce seçili olan koltuğu kontrol et
        for (JToggleButton button : seatButtons) {
            if (button != clickedButton && button.isSelected()) {
                button.setSelected(false);
            }
        }

        // Yeni seçimi güncelle
        if (clickedButton.isSelected()) {
            selectedSeat = clickedButton.getSeat();
            continueButton.setEnabled(true);
            selectedSeatLabel.setText("Seçili Koltuk: " + selectedSeat.getSeatNumber() +
                    " (" + selectedWagon.getWagonType() + " Vagon)");
        } else {
            selectedSeat = null;
            continueButton.setEnabled(false);
            selectedSeatLabel.setText("Seçili Koltuk: Henüz seçilmedi");
        }
    }

    private void goBack() {
        // Tren arama ekranına geri dön
        SearchTrainsPanel searchPanel = new SearchTrainsPanel(currentUser, parentFrame);
        parentFrame.showPanel(searchPanel, "search");
    }

    private void continueToPassengerInfo() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen bir koltuk seçin!",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Yolcu bilgileri ekranına geç
        PassengerInfoPanel passengerPanel = new PassengerInfoPanel(
                currentUser, selectedTrain, selectedWagon, selectedSeat, parentFrame);
        parentFrame.showPanel(passengerPanel, "passengerInfo");
    }

    // Özel JToggleButton sınıfı (koltuklar için)
    class SeatButton extends JToggleButton {
        private Seat seat;
        private boolean isHovered = false;

        public SeatButton(Seat seat) {
            this.seat = seat;
            setText(String.valueOf(seat.getSeatNumber()));
            setFont(BOLD_LABEL_FONT);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false); // Özel çizim için
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Koltuk durumuna göre görünümü ayarla
            updateAppearance();

            // Hover efekti
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!seat.isReserved() && !isSelected()) {
                        isHovered = true;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    updateAppearance();
                }
            });
        }

        public Seat getSeat() {
            return seat;
        }

        private void updateAppearance() {
            if (seat.isReserved()) {
                setBackground(SEAT_RESERVED);
                setEnabled(false);
                String genderInfo = seat.getReservedByGender() != null ?
                        " (" + seat.getReservedByGender() + ")" : "";
                setToolTipText("Rezerve" + genderInfo);
            } else if (isSelected()) {
                setBackground(SEAT_SELECTED);
                setToolTipText("Seçili koltuk");
            } else {
                setBackground(SEAT_AVAILABLE);
                setEnabled(true);
                setToolTipText("Boş koltuk - Koltuk No: " + seat.getSeatNumber());
            }
            repaint();
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            updateAppearance();
        }

        // Yuvarlak köşeli koltuk görünümü
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Koltuk arkaplanı
            if (isSelected()) {
                g2.setColor(SEAT_SELECTED);
            } else if (isHovered) {
                g2.setColor(SEAT_HOVER);
            } else if (seat.isReserved()) {
                g2.setColor(SEAT_RESERVED);
            } else {
                g2.setColor(SEAT_AVAILABLE);
            }

            // Yuvarlak köşeli koltuk çiz
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

            // Koltuk numarası
            String text = getText();
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            g2.setColor(Color.WHITE);
            g2.setFont(getFont());
            g2.drawString(text,
                    (getWidth() - textWidth) / 2,
                    (getHeight() + textHeight) / 2 - fm.getDescent());

            g2.dispose();
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BOLD_LABEL_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor.equals(Color.WHITE) ? PRIMARY_COLOR : bgColor, 1, true));
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    if (bgColor.equals(PRIMARY_COLOR)) {
                        button.setBackground(new Color(41, 128, 185)); // Daha koyu mavi
                    } else {
                        button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                    if (bgColor.equals(Color.WHITE)) {
                        button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                    }
                }
            }
        });

        return button;
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(BOLD_LABEL_FONT);
        labelComponent.setForeground(LIGHT_TEXT_COLOR);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(BOLD_LABEL_FONT);
        valueComponent.setForeground(TEXT_COLOR);

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    @Override
    public String toString() {
        return wagonComboBox != null ? wagonComboBox.getSelectedItem().toString() : "null";
    }
}