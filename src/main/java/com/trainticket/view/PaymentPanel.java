package com.trainticket.view;

import com.trainticket.dao.TicketDAO;
import com.trainticket.model.Ticket;
import com.trainticket.model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class PaymentPanel extends JPanel {

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
    private Ticket ticket;
    private MainFrame parentFrame;

    private JFormattedTextField cardNumberField;
    private JTextField cardHolderField;
    private JComboBox<String> expiryMonthComboBox;
    private JComboBox<String> expiryYearComboBox;
    private JFormattedTextField cvvField;
    private JButton payButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    // Ödeme işleminin tamamlanıp tamamlanmadığını izlemek için flag
    private boolean paymentCompleted = false;

    public PaymentPanel(User user, Ticket ticket, MainFrame parent) {
        this.currentUser = user;
        this.ticket = ticket;
        this.parentFrame = parent;
        initComponents();
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

        JLabel titleLabel = new JLabel("Ödeme");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Lütfen ödeme bilgilerinizi girin");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Ana içerik paneli - dikey yerleşim için BoxLayout kullanıyoruz
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Bilet özeti kartı
        JPanel summaryCard = createTicketSummaryCard();
        summaryCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Boşluk
        Component verticalSpace = Box.createRigidArea(new Dimension(0, 20));

        // Ödeme formu kartı
        JPanel paymentFormCard = createPaymentFormCard();
        paymentFormCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // İçeriği dikey olarak ekle
        contentPanel.add(summaryCard);
        contentPanel.add(verticalSpace);
        contentPanel.add(paymentFormCard);

        // Alt panel - Butonlar ve durum
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setOpaque(false);

        // Durum mesajı
        statusLabel = new JLabel(" ");
        statusLabel.setFont(SMALL_FONT);
        statusLabel.setForeground(ACCENT_COLOR);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        cancelButton = createStyledButton("İptal", Color.WHITE, PRIMARY_COLOR);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelPayment();
            }
        });

        payButton = createStyledButton("Ödemeyi Tamamla", PRIMARY_COLOR, Color.WHITE);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });

        // İlerleme çubuğu (başlangıçta gizli)
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(150, 25));
        progressBar.setVisible(false);

        buttonPanel.add(progressBar);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(payButton);

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Ana içerik paneline ekle
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);

        // Ana panele bileşenleri ekle
        add(headerPanel, BorderLayout.NORTH);
        add(mainScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTicketSummaryCard() {
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

        // Özet içeriği
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setOpaque(false);
        summaryPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Bilet detayları - iki sütunlu gösterim
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        detailsPanel.setOpaque(false);

        // Sol taraf - Tren ve güzergah bilgileri
        JPanel leftPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        leftPanel.setOpaque(false);

        // Tren adı
        JLabel trainLabel = new JLabel();
        if (ticket.getTrain() != null) {
            trainLabel.setText(ticket.getTrain().getTrainNumber() + " - " + ticket.getTrain().getTrainName());
        } else {
            trainLabel.setText("Bilet #" + ticket.getId());
        }
        trainLabel.setFont(BOLD_LABEL_FONT);
        trainLabel.setForeground(TEXT_COLOR);

        // Güzergah
        JPanel routePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        routePanel.setOpaque(false);

        JLabel fromLabel = new JLabel();
        JLabel arrowLabel = new JLabel(" → ");
        JLabel toLabel = new JLabel();

        if (ticket.getTrain() != null) {
            fromLabel.setText(ticket.getTrain().getDepartureStation());
            toLabel.setText(ticket.getTrain().getArrivalStation());
        } else {
            fromLabel.setText("Başlangıç");
            toLabel.setText("Bitiş");
        }

        fromLabel.setFont(LABEL_FONT);
        fromLabel.setForeground(TEXT_COLOR);
        arrowLabel.setFont(LABEL_FONT);
        arrowLabel.setForeground(PRIMARY_COLOR);
        toLabel.setFont(LABEL_FONT);
        toLabel.setForeground(TEXT_COLOR);

        routePanel.add(fromLabel);
        routePanel.add(arrowLabel);
        routePanel.add(toLabel);

        // Tarih ve saat
        JLabel dateLabel = new JLabel();
        JLabel timeLabel = new JLabel();

        if (ticket.getTrain() != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            dateLabel.setText("Tarih: " + ticket.getTrain().getDepartureTime().format(dateFormatter));
            timeLabel.setText("Saat: " + ticket.getTrain().getDepartureTime().format(timeFormatter));
        } else {
            dateLabel.setText("Tarih: --");
            timeLabel.setText("Saat: --");
        }

        dateLabel.setFont(LABEL_FONT);
        dateLabel.setForeground(TEXT_COLOR);
        timeLabel.setFont(LABEL_FONT);
        timeLabel.setForeground(TEXT_COLOR);

        // Yolcu
        JLabel passengerLabel = new JLabel("Yolcu: " + ticket.getPassengerName());
        passengerLabel.setFont(LABEL_FONT);
        passengerLabel.setForeground(TEXT_COLOR);

        leftPanel.add(trainLabel);
        leftPanel.add(routePanel);
        leftPanel.add(dateLabel);
        leftPanel.add(timeLabel);
        leftPanel.add(passengerLabel);

        // Sağ taraf - Vagon, koltuk ve fiyat bilgileri
        JPanel rightPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        rightPanel.setOpaque(false);

        // Vagon
        JLabel wagonLabel = new JLabel();
        if (ticket.getWagon() != null) {
            wagonLabel.setText("Vagon: " + ticket.getWagon().getWagonNumber() + " (" + ticket.getWagon().getWagonType() + ")");
        } else {
            wagonLabel.setText("Vagon: --");
        }
        wagonLabel.setFont(LABEL_FONT);
        wagonLabel.setForeground(TEXT_COLOR);

        // Koltuk
        JLabel seatLabel = new JLabel();
        if (ticket.getSeat() != null) {
            seatLabel.setText("Koltuk: " + ticket.getSeat().getSeatNumber());
        } else {
            seatLabel.setText("Koltuk: --");
        }
        seatLabel.setFont(LABEL_FONT);
        seatLabel.setForeground(TEXT_COLOR);

        // Boş satır
        JLabel emptyLabel = new JLabel("");

        // Fiyat - vurgulanmış
        JLabel priceLabel = new JLabel("Ödenecek Tutar:");
        priceLabel.setFont(BOLD_LABEL_FONT);
        priceLabel.setForeground(TEXT_COLOR);

        JLabel amountLabel = new JLabel(String.format("%.2f TL", ticket.getPrice()));
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountLabel.setForeground(ACCENT_COLOR);

        rightPanel.add(wagonLabel);
        rightPanel.add(seatLabel);
        rightPanel.add(emptyLabel);
        rightPanel.add(priceLabel);
        rightPanel.add(amountLabel);

        // Panelleri birleştir
        detailsPanel.add(leftPanel);
        detailsPanel.add(rightPanel);

        summaryPanel.add(detailsPanel, BorderLayout.CENTER);

        // Kartın ana düzeni
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(summaryPanel, BorderLayout.CENTER);

        card.add(mainPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createPaymentFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        // Renkli başlık bandı
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 5));
        card.add(headerPanel, BorderLayout.NORTH);

        // Başlık
        JLabel titleLabel = new JLabel("Ödeme Bilgileri");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        // Form içeriği - dikey yerleşim için BoxLayout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Kart numarası
        JPanel cardNumberPanel = new JPanel(new BorderLayout(0, 5));
        cardNumberPanel.setOpaque(false);
        cardNumberPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        cardNumberPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel cardNumberLabel = new JLabel("Kart Numarası *");
        cardNumberLabel.setFont(LABEL_FONT);
        cardNumberLabel.setForeground(TEXT_COLOR);

        try {
            MaskFormatter formatter = new MaskFormatter("#### #### #### ####");
            formatter.setPlaceholderCharacter('_');
            cardNumberField = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            cardNumberField = new JFormattedTextField();
        }

        cardNumberField.setFont(LABEL_FONT);
        cardNumberField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        cardNumberField.setPreferredSize(new Dimension(0, 40));

        // Odaklanma efekti
        addFocusEffect(cardNumberField);

        cardNumberPanel.add(cardNumberLabel, BorderLayout.NORTH);
        cardNumberPanel.add(cardNumberField, BorderLayout.CENTER);

        // Kart sahibi
        JPanel cardHolderPanel = new JPanel(new BorderLayout(0, 5));
        cardHolderPanel.setOpaque(false);
        cardHolderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        cardHolderPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel cardHolderLabel = new JLabel("Kart Sahibi *");
        cardHolderLabel.setFont(LABEL_FONT);
        cardHolderLabel.setForeground(TEXT_COLOR);

        cardHolderField = createStyledTextField();
        cardHolderField.setText(currentUser.getFullName());

        cardHolderPanel.add(cardHolderLabel, BorderLayout.NORTH);
        cardHolderPanel.add(cardHolderField, BorderLayout.CENTER);

        // Son kullanma tarihi ve CVV satırı
        JPanel expiryAndCvvPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        expiryAndCvvPanel.setOpaque(false);
        expiryAndCvvPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        expiryAndCvvPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Son kullanma tarihi
        JPanel expiryPanel = new JPanel(new BorderLayout(0, 5));
        expiryPanel.setOpaque(false);

        JLabel expiryLabel = new JLabel("Son Kullanma Tarihi *");
        expiryLabel.setFont(LABEL_FONT);
        expiryLabel.setForeground(TEXT_COLOR);

        JPanel expiryFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        expiryFieldPanel.setOpaque(false);

        // Ay için ComboBox
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        expiryMonthComboBox = new JComboBox<>(months);
        expiryMonthComboBox.setFont(LABEL_FONT);
        expiryMonthComboBox.setPreferredSize(new Dimension(80, 40));
        expiryMonthComboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        expiryMonthComboBox.setBackground(Color.WHITE);

        JLabel separatorLabel = new JLabel("/");
        separatorLabel.setFont(LABEL_FONT);
        separatorLabel.setForeground(TEXT_COLOR);

        // Yıl için ComboBox
        String[] years = new String[10];
        int currentYear = LocalDate.now().getYear() % 100; // Son 2 hane
        for (int i = 0; i < 10; i++) {
            years[i] = String.format("%02d", currentYear + i);
        }
        expiryYearComboBox = new JComboBox<>(years);
        expiryYearComboBox.setFont(LABEL_FONT);
        expiryYearComboBox.setPreferredSize(new Dimension(80, 40));
        expiryYearComboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        expiryYearComboBox.setBackground(Color.WHITE);

        expiryFieldPanel.add(expiryMonthComboBox);
        expiryFieldPanel.add(separatorLabel);
        expiryFieldPanel.add(expiryYearComboBox);

        expiryPanel.add(expiryLabel, BorderLayout.NORTH);
        expiryPanel.add(expiryFieldPanel, BorderLayout.CENTER);

        // CVV
        JPanel cvvPanel = new JPanel(new BorderLayout(0, 5));
        cvvPanel.setOpaque(false);

        JLabel cvvLabel = new JLabel("CVV *");
        cvvLabel.setFont(LABEL_FONT);
        cvvLabel.setForeground(TEXT_COLOR);

        try {
            MaskFormatter formatter = new MaskFormatter("###");
            formatter.setPlaceholderCharacter('_');
            cvvField = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            cvvField = new JFormattedTextField();
        }

        cvvField.setFont(LABEL_FONT);
        cvvField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        cvvField.setPreferredSize(new Dimension(0, 40));

        // Odaklanma efekti
        addFocusEffect(cvvField);

        cvvPanel.add(cvvLabel, BorderLayout.NORTH);
        cvvPanel.add(cvvField, BorderLayout.CENTER);

        expiryAndCvvPanel.add(expiryPanel);
        expiryAndCvvPanel.add(cvvPanel);

        // Güvenli ödeme notu
        JPanel secureNotePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        secureNotePanel.setOpaque(false);
        secureNotePanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Güvenlik ikonu
        JLabel secureIcon = new JLabel("\uD83D\uDD12"); // Kilit emoji
        secureIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        secureIcon.setForeground(SECONDARY_COLOR);

        JLabel secureLabel = new JLabel("Güvenli ödeme. Kart bilgileriniz şifrelenmiş ve korumalıdır.");
        secureLabel.setFont(SMALL_FONT);
        secureLabel.setForeground(LIGHT_TEXT_COLOR);

        secureNotePanel.add(secureIcon);
        secureNotePanel.add(secureLabel);

        // Form paneline ekle
        formPanel.add(cardNumberPanel);
        formPanel.add(cardHolderPanel);
        formPanel.add(expiryAndCvvPanel);
        formPanel.add(secureNotePanel);

        // Kartın ana düzeni
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        card.add(mainPanel, BorderLayout.CENTER);

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
        addFocusEffect(textField);

        return textField;
    }

    private void addFocusEffect(JComponent component) {
        component.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                component.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                if (component instanceof JTextField) {
                    ((JTextField) component).setBackground(new Color(240, 248, 255)); // Çok açık mavi
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                component.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                if (component instanceof JTextField) {
                    ((JTextField) component).setBackground(Color.WHITE);
                }
            }
        });
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

    private void cancelPayment() {
        // Ödeme zaten tamamlandıysa işlemi engelle
        if (paymentCompleted) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Ödemeyi iptal etmek istiyor musunuz? Bilet satın alınmayacaktır.",
                "Ödeme İptali",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Ana sayfaya dön
            parentFrame.showHome();
        }
    }

    private void processPayment() {
        // Ödeme zaten tamamlandıysa işlemi engelle
        if (paymentCompleted) {
            return;
        }

        // Form validasyonu
        String cardNumber = cardNumberField.getText().trim().replaceAll("\\s+", "");
        String cardHolder = cardHolderField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (cardNumber.isEmpty() || cardNumber.contains("_") || cardHolder.isEmpty() || cvv.isEmpty() || cvv.contains("_")) {
            statusLabel.setText("Lütfen tüm kart bilgilerini doldurun!");
            return;
        }

        if (cardNumber.length() != 16) {
            statusLabel.setText("Kart numarası 16 haneli olmalıdır!");
            return;
        }

        if (cvv.length() != 3) {
            statusLabel.setText("CVV 3 haneli olmalıdır!");
            return;
        }

        // Butonları devre dışı bırak ve ilerleme çubuğunu göster
        setFormEnabled(false);
        progressBar.setVisible(true);
        statusLabel.setText("Ödeme işlemi gerçekleştiriliyor...");
        statusLabel.setForeground(LIGHT_TEXT_COLOR);

        // Payment timer
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> completePayment());
            }
        }, 2000);
    }

    private void completePayment() {
        // Ödeme bayrağını ayarla
        paymentCompleted = true;

        // Bileti veritabanına kaydet
        TicketDAO ticketDAO = new TicketDAO();

        // Ödendi olarak işaretle
        ticket.setPaid(true);

        boolean success = ticketDAO.addTicket(ticket);

        if (success) {
            // İşlem başarılı olursa, bilet tamamlandı ekranını göster
            statusLabel.setText("Ödeme başarıyla tamamlandı!");
            statusLabel.setForeground(SECONDARY_COLOR);
            progressBar.setVisible(false);

            // Bilet tamamlandı ekranını göster
            TicketCompletedPanel completedPanel = new TicketCompletedPanel(currentUser, ticket, parentFrame);
            parentFrame.showPanel(completedPanel, "completed");
        } else {
            // İşlem başarısız olursa, hata mesajı göster
            statusLabel.setText("Ödeme işlemi başarısız oldu. Lütfen daha sonra tekrar deneyin.");
            statusLabel.setForeground(ACCENT_COLOR);
            progressBar.setVisible(false);
            setFormEnabled(true);
            paymentCompleted = false;
        }
    }

    private void setFormEnabled(boolean enabled) {
        cardNumberField.setEnabled(enabled);
        cardHolderField.setEnabled(enabled);
        expiryMonthComboBox.setEnabled(enabled);
        expiryYearComboBox.setEnabled(enabled);
        cvvField.setEnabled(enabled);
        payButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }
}