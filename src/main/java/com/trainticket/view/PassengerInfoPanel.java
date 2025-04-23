package com.trainticket.view;

import com.trainticket.dao.TicketDAO;
import com.trainticket.dao.UserDAO;
import com.trainticket.model.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class PassengerInfoPanel extends JPanel {

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
    private static final Color INPUT_FOCUS_COLOR = new Color(52, 152, 219, 50); // Odaklanılan input alanı rengi

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private User currentUser;
    private Train selectedTrain;
    private Wagon selectedWagon;
    private Seat selectedSeat;
    private MainFrame parentFrame;

    private JTextField nameField;
    private JTextField tcNoField;
    private JComboBox<String> genderComboBox;
    private JLabel priceLabel;
    private JLabel discountedPriceLabel;
    private JButton confirmButton;
    private JButton backButton;
    private JCheckBox useProfileInfoCheckBox;
    private JCheckBox discountCheckBox;
    private JLabel statusLabel;

    private double originalPrice;
    private double finalPrice;

    public PassengerInfoPanel(User user, Train train, Wagon wagon, Seat seat, MainFrame parent) {
        this.currentUser = user;
        this.selectedTrain = train;
        this.selectedWagon = wagon;
        this.selectedSeat = seat;
        this.parentFrame = parent;

        // Fiyat hesapla
        calculatePrice();

        initComponents();
    }

    private void calculatePrice() {
        originalPrice = selectedTrain.getPrice();

        // Eğer Business vagon ise %50 ek ücret
        if (selectedWagon.getWagonType().equals("Business")) {
            originalPrice *= 1.5;
        }

        // Başlangıçta indirim yok
        finalPrice = originalPrice;
    }

    private void initComponents() {
        // Panel ayarları
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 40, 40));

        // Main scroll panel - BÜTÜN İÇERİK KAYDIRILEBILIR OLACAK
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 20));
        mainContentPanel.setOpaque(false);

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.setOpaque(false);
        mainScrollPane.getViewport().setOpaque(false);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Başlık
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Yolcu Bilgileri");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Lütfen yolculuk yapacak kişinin bilgilerini girin");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Ana içerik paneli
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Üst kısım - Tren ve bilet özeti
        JPanel summaryCard = createSummaryCard();
        summaryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        summaryCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Boşluk
        Component verticalSpace = Box.createRigidArea(new Dimension(0, 20));

        // Alt kısım - Yolcu bilgi formu
        JPanel passengerFormCard = createPassengerFormCard();
        passengerFormCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // İçeriği panele ekle (dikey düzen)
        contentPanel.add(summaryCard);
        contentPanel.add(verticalSpace);
        contentPanel.add(passengerFormCard);

        // Buton ve durum paneli
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Durum mesajı
        statusLabel = new JLabel(" ");
        statusLabel.setFont(SMALL_FONT);
        statusLabel.setForeground(ACCENT_COLOR);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        backButton = createStyledButton("Geri", Color.WHITE, PRIMARY_COLOR);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        confirmButton = createStyledButton("Ödemeye Geç", PRIMARY_COLOR, Color.WHITE);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPassengerInfo();
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(confirmButton);

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Ana içerik paneline ekle
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);

        // Ana panele bileşenleri ekle
        add(headerPanel, BorderLayout.NORTH);
        add(mainScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSummaryCard() {
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

        // Başlık
        JLabel titleLabel = new JLabel("Bilet Özeti");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        // İçerik düzeni - 2 sütun: Tren bilgileri ve Bilet detayları
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        contentPanel.setOpaque(false);

        // Tren bilgileri
        JPanel trainInfoPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        trainInfoPanel.setOpaque(false);

        // Tren adı ve numarası
        JLabel trainLabel = new JLabel(selectedTrain.getTrainNumber() + " - " + selectedTrain.getTrainName());
        trainLabel.setFont(BOLD_LABEL_FONT);
        trainLabel.setForeground(TEXT_COLOR);

        // Güzergah
        JPanel routePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        routePanel.setOpaque(false);

        JLabel fromLabel = new JLabel(selectedTrain.getDepartureStation());
        fromLabel.setFont(LABEL_FONT);
        fromLabel.setForeground(TEXT_COLOR);

        JLabel arrowLabel = new JLabel(" → ");
        arrowLabel.setFont(LABEL_FONT);
        arrowLabel.setForeground(PRIMARY_COLOR);

        JLabel toLabel = new JLabel(selectedTrain.getArrivalStation());
        toLabel.setFont(LABEL_FONT);
        toLabel.setForeground(TEXT_COLOR);

        routePanel.add(fromLabel);
        routePanel.add(arrowLabel);
        routePanel.add(toLabel);

        // Tarih ve saat
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        JLabel dateLabel = new JLabel("Tarih: " + selectedTrain.getDepartureTime().format(dateFormatter));
        dateLabel.setFont(LABEL_FONT);
        dateLabel.setForeground(TEXT_COLOR);

        JLabel timeLabel = new JLabel("Saat: " + selectedTrain.getDepartureTime().format(timeFormatter));
        timeLabel.setFont(LABEL_FONT);
        timeLabel.setForeground(TEXT_COLOR);

        trainInfoPanel.add(trainLabel);
        trainInfoPanel.add(routePanel);
        trainInfoPanel.add(dateLabel);
        trainInfoPanel.add(timeLabel);

        // Bilet detayları
        JPanel ticketDetailsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        ticketDetailsPanel.setOpaque(false);

        // Vagon ve koltuk
        JLabel wagonLabel = new JLabel("Vagon: " + selectedWagon.getWagonNumber() + " (" + selectedWagon.getWagonType() + ")");
        wagonLabel.setFont(LABEL_FONT);
        wagonLabel.setForeground(TEXT_COLOR);

        JLabel seatLabel = new JLabel("Koltuk: " + selectedSeat.getSeatNumber());
        seatLabel.setFont(LABEL_FONT);
        seatLabel.setForeground(TEXT_COLOR);

        // Fiyat bilgileri
        JPanel pricePanel = new JPanel(new GridLayout(2, 1));
        pricePanel.setOpaque(false);

        JLabel basicPriceLabel = new JLabel("Bilet Ücreti:");
        basicPriceLabel.setFont(BOLD_LABEL_FONT);
        basicPriceLabel.setForeground(TEXT_COLOR);

        priceLabel = new JLabel(String.format("%.2f TL", originalPrice));
        priceLabel.setFont(BOLD_LABEL_FONT);
        priceLabel.setForeground(ACCENT_COLOR);

        pricePanel.add(basicPriceLabel);
        pricePanel.add(priceLabel);

        // İndirimli fiyat (indirimsiz başta görünmez)
        JPanel discountedPanel = new JPanel(new GridLayout(2, 1));
        discountedPanel.setOpaque(false);

        JLabel discountedTitleLabel = new JLabel("İndirimli Fiyat:");
        discountedTitleLabel.setFont(BOLD_LABEL_FONT);
        discountedTitleLabel.setForeground(TEXT_COLOR);

        discountedPriceLabel = new JLabel(String.format("%.2f TL", finalPrice));
        discountedPriceLabel.setFont(BOLD_LABEL_FONT);
        discountedPriceLabel.setForeground(SECONDARY_COLOR);

        discountedPanel.add(discountedTitleLabel);
        discountedPanel.add(discountedPriceLabel);
        discountedPanel.setVisible(false); // Başlangıçta görünmez

        ticketDetailsPanel.add(wagonLabel);
        ticketDetailsPanel.add(seatLabel);
        ticketDetailsPanel.add(pricePanel);
        ticketDetailsPanel.add(discountedPanel);

        // İçerik paneline ekle
        contentPanel.add(trainInfoPanel);
        contentPanel.add(ticketDetailsPanel);

        // Kartın ana düzeni
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)), BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.SOUTH);

        card.add(mainPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createPassengerFormCard() {
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

        // Başlık ve bilgi paneli
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Yolcu Bilgileri");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        // Profil bilgilerini kullan seçeneği
        useProfileInfoCheckBox = new JCheckBox("Profilimden Bilgilerimi Doldur");
        useProfileInfoCheckBox.setFont(LABEL_FONT);
        useProfileInfoCheckBox.setForeground(PRIMARY_COLOR);
        useProfileInfoCheckBox.setOpaque(false);
        useProfileInfoCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        useProfileInfoCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillFromUserProfile();
            }
        });

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(useProfileInfoCheckBox, BorderLayout.EAST);

        // Form alanları - Dikey düzene değiştirdik
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 15, 0));

        // Kişisel bilgiler - Her form alanı kendi satırında

        // Ad Soyad
        JPanel namePanel = new JPanel(new BorderLayout(0, 5));
        namePanel.setOpaque(false);
        namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        namePanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel nameLabel = new JLabel("Ad Soyad *");
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setForeground(TEXT_COLOR);

        nameField = createStyledTextField();

        namePanel.add(nameLabel, BorderLayout.NORTH);
        namePanel.add(nameField, BorderLayout.CENTER);

        // TC Kimlik No
        JPanel tcNoPanel = new JPanel(new BorderLayout(0, 5));
        tcNoPanel.setOpaque(false);
        tcNoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        tcNoPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel tcNoLabel = new JLabel("TC Kimlik No *");
        tcNoLabel.setFont(LABEL_FONT);
        tcNoLabel.setForeground(TEXT_COLOR);

        tcNoField = createStyledTextField();

        tcNoPanel.add(tcNoLabel, BorderLayout.NORTH);
        tcNoPanel.add(tcNoField, BorderLayout.CENTER);

        // Cinsiyet
        JPanel genderPanel = new JPanel(new BorderLayout(0, 5));
        genderPanel.setOpaque(false);
        genderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        genderPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel genderLabel = new JLabel("Cinsiyet *");
        genderLabel.setFont(LABEL_FONT);
        genderLabel.setForeground(TEXT_COLOR);

        String[] genders = {"Seçiniz...", "Erkek", "Kadın"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(LABEL_FONT);
        genderComboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        genderComboBox.setBackground(Color.WHITE);
        genderComboBox.setMaximumRowCount(3);

        genderPanel.add(genderLabel, BorderLayout.NORTH);
        genderPanel.add(genderComboBox, BorderLayout.CENTER);

        // İndirim seçeneği
        JPanel discountPanel = new JPanel(new BorderLayout(0, 5));
        discountPanel.setOpaque(false);
        discountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel discountLabel = new JLabel("İndirim");
        discountLabel.setFont(LABEL_FONT);
        discountLabel.setForeground(TEXT_COLOR);

        discountCheckBox = new JCheckBox("Yaş/Öğrenci İndirimi (%20)");
        discountCheckBox.setFont(LABEL_FONT);
        discountCheckBox.setForeground(TEXT_COLOR);
        discountCheckBox.setOpaque(false);
        discountCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        discountCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyDiscount();
            }
        });

        discountPanel.add(discountLabel, BorderLayout.NORTH);
        discountPanel.add(discountCheckBox, BorderLayout.CENTER);

        // Not bilgisi
        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.setOpaque(false);
        notePanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel noteLabel = new JLabel("* işaretli alanların doldurulması zorunludur.");
        noteLabel.setFont(SMALL_FONT);
        noteLabel.setForeground(LIGHT_TEXT_COLOR);

        notePanel.add(noteLabel, BorderLayout.WEST);

        // Formu ana panele ekle - Dikey düzende
        formPanel.add(namePanel);
        formPanel.add(tcNoPanel);
        formPanel.add(genderPanel);
        formPanel.add(discountPanel);
        formPanel.add(notePanel);

        // Kartın ana düzeni
        card.add(titlePanel, BorderLayout.NORTH);
        card.add(formPanel, BorderLayout.CENTER);

        return card;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(LABEL_FONT);
        textField.setPreferredSize(new Dimension(0, 40));
        textField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Odaklanma efekti
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                textField.setBackground(new Color(240, 248, 255)); // Çok açık mavi
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                textField.setBackground(Color.WHITE);
            }
        });

        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BOLD_LABEL_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor.equals(Color.WHITE) ? PRIMARY_COLOR : bgColor, 1, true));
        button.setPreferredSize(new Dimension(150, 40));
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

    private void fillFromUserProfile() {
        if (useProfileInfoCheckBox.isSelected()) {
            nameField.setText(currentUser.getFullName());
            tcNoField.setText(currentUser.getTcNo());

            if (currentUser.getGender() != null && !currentUser.getGender().isEmpty()) {
                genderComboBox.setSelectedItem(currentUser.getGender());
            }
        } else {
            nameField.setText("");
            tcNoField.setText("");
            genderComboBox.setSelectedIndex(0);
        }
    }

    private void applyDiscount() {
        // Fiyat hesapla
        calculatePrice();

        // İndirim uygula
        if (discountCheckBox.isSelected()) {
            finalPrice = originalPrice * 0.8; // %20 indirim

            // İndirimli fiyatı göster
            Component[] components = ((JPanel)priceLabel.getParent().getParent().getComponent(3)).getComponents();
            JPanel discountedPanel = (JPanel)components[3];
            discountedPanel.setVisible(true);
            discountedPriceLabel.setText(String.format("%.2f TL", finalPrice));

            // Orijinal fiyat görünümünü güncelle
            priceLabel.setText("<html><s>" + String.format("%.2f TL", originalPrice) + "</s></html>");
        } else {
            finalPrice = originalPrice;

            // İndirimli fiyatı gizle
            Component[] components = ((JPanel)priceLabel.getParent().getParent().getComponent(3)).getComponents();
            JPanel discountedPanel = (JPanel)components[3];
            discountedPanel.setVisible(false);

            // Orijinal fiyat görünümünü normal yap
            priceLabel.setText(String.format("%.2f TL", originalPrice));
        }
    }

    private void goBack() {
        // Koltuk seçim ekranına geri dön
        SeatSelectionPanel seatPanel = new SeatSelectionPanel(currentUser, selectedTrain, parentFrame);
        parentFrame.showPanel(seatPanel, "seatSelection");
    }

    private void processPassengerInfo() {
        // Form validasyonu
        String name = nameField.getText().trim();
        String tcNo = tcNoField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();

        if (name.isEmpty() || tcNo.isEmpty() || gender == null || gender.equals("Seçiniz...")) {
            statusLabel.setText("Lütfen zorunlu alanları doldurun!");
            statusLabel.setForeground(ACCENT_COLOR);
            return;
        }

        if (tcNo.length() != 11 || !tcNo.matches("\\d+")) {
            statusLabel.setText("TC Kimlik No 11 haneli sayısal bir değer olmalıdır!");
            statusLabel.setForeground(ACCENT_COLOR);
            return;
        }

        // Cinsiyet kısıtlamasını kontrol et
        if (!selectedSeat.canBeReservedBy(gender)) {
            statusLabel.setText("Seçtiğiniz koltuk, cinsiyet kısıtlaması nedeniyle rezerve edilemez!");
            statusLabel.setForeground(ACCENT_COLOR);
            return;
        }

        // Bilet oluştur
        Ticket ticket = new Ticket();
        ticket.setUserId(currentUser.getId());
        ticket.setTrainId(selectedTrain.getId());
        ticket.setWagonId(selectedWagon.getId());
        ticket.setSeatId(selectedSeat.getId());
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setPassengerName(name);
        ticket.setPassengerTcNo(tcNo);
        ticket.setPassengerGender(gender);
        ticket.setPrice(finalPrice);
        ticket.setPaid(false);

        // Referansları ayarla
        ticket.setTrain(selectedTrain);
        ticket.setWagon(selectedWagon);
        ticket.setSeat(selectedSeat);

        // Ödeme ekranını aç
        PaymentPanel paymentPanel = new PaymentPanel(currentUser, ticket, parentFrame);
        parentFrame.showPanel(paymentPanel, "payment");
    }
}