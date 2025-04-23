package com.trainticket.view;

import com.trainticket.model.Ticket;
import com.trainticket.model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.time.format.DateTimeFormatter;

public class TicketCompletedPanel extends JPanel {

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
    private Ticket ticket;
    private MainFrame parentFrame;

    private JTextArea ticketDetails;
    private JButton printButton;
    private JButton homeButton;

    public TicketCompletedPanel(User user, Ticket ticket, MainFrame parent) {
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

        // Başlık paneli
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Başarı ikonunu içeren panel
        JPanel successIconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        successIconPanel.setOpaque(false);

        // Yeşil daire içinde beyaz tik ikonu
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Daire çiz
                g2d.setColor(SECONDARY_COLOR);
                g2d.fillOval(0, 0, 80, 80);

                // Tik işareti çiz
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawLine(20, 40, 35, 55);
                g2d.drawLine(35, 55, 60, 25);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80, 80);
            }
        };
        iconPanel.setOpaque(false);
        successIconPanel.add(iconPanel);

        // Başlık metinleri
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel titleLabel = new JLabel("İşlem Tamamlandı!");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Biletiniz başarıyla oluşturuldu.");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(subtitleLabel);

        headerPanel.add(successIconPanel, BorderLayout.NORTH);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Ana içerik paneli
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        // Bilet detay kartı
        JPanel ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBackground(CARD_COLOR);
        ticketPanel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 30, 25, 30)
        ));

        // Kartın üstünde renkli bant
        JPanel colorBandPanel = new JPanel();
        colorBandPanel.setBackground(PRIMARY_COLOR);
        colorBandPanel.setPreferredSize(new Dimension(0, 5));
        ticketPanel.add(colorBandPanel, BorderLayout.NORTH);

        // Kart başlığı
        JLabel cardTitle = new JLabel("Bilet Bilgileri");
        cardTitle.setFont(SUBTITLE_FONT);
        cardTitle.setForeground(TEXT_COLOR);

        // Bilet içeriği
        ticketDetails = new JTextArea(15, 40);
        ticketDetails.setEditable(false);
        ticketDetails.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ticketDetails.setBorder(new EmptyBorder(15, 15, 15, 15));
        ticketDetails.setBackground(new Color(250, 250, 250));
        ticketDetails.setLineWrap(true);
        ticketDetails.setWrapStyleWord(true);

        // Bilet bilgilerini formatla
        StringBuilder sb = new StringBuilder();
        sb.append("----------------- TCDD BİLET -----------------\n\n");
        sb.append("Bilet No: ").append(ticket.getId()).append("\n");
        sb.append("Yolcu: ").append(ticket.getPassengerName()).append("\n");
        sb.append("TC No: ").append(ticket.getPassengerTcNo()).append("\n\n");

        if (ticket.getTrain() != null) {
            sb.append("Tren: ").append(ticket.getTrain().getTrainNumber())
                    .append(" - ").append(ticket.getTrain().getTrainName()).append("\n");
            sb.append("Güzergah: ").append(ticket.getTrain().getDepartureStation())
                    .append(" → ").append(ticket.getTrain().getArrivalStation()).append("\n");
            sb.append("Kalkış: ").append(ticket.getTrain().getFormattedDepartureTime()).append("\n");
            sb.append("Varış: ").append(ticket.getTrain().getFormattedArrivalTime()).append("\n\n");
        }

        if (ticket.getWagon() != null) {
            sb.append("Vagon: ").append(ticket.getWagon().getWagonNumber())
                    .append(" (").append(ticket.getWagon().getWagonType()).append(")\n");
        }

        if (ticket.getSeat() != null) {
            sb.append("Koltuk: ").append(ticket.getSeat().getSeatNumber()).append("\n\n");
        }

        sb.append("Ücret: ").append(String.format("%.2f TL", ticket.getPrice())).append("\n");
        sb.append("Ödeme Durumu: ").append(ticket.isPaid() ? "Ödenmiş" : "Ödenmemiş").append("\n\n");
        sb.append("Tarih: ").append(ticket.getPurchaseDate().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append("\n");
        sb.append("\n-----------------------------------------------\n");
        sb.append("TCDD yolculuğunuzda iyi eğlenceler diler.");

        ticketDetails.setText(sb.toString());

        // İçeriği oluştur ve karta ekle
        JPanel cardContentPanel = new JPanel(new BorderLayout(0, 15));
        cardContentPanel.setOpaque(false);
        cardContentPanel.add(cardTitle, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(ticketDetails);
        scrollPane.setBorder(null);
        cardContentPanel.add(scrollPane, BorderLayout.CENTER);

        ticketPanel.add(cardContentPanel, BorderLayout.CENTER);

        contentPanel.add(ticketPanel, BorderLayout.CENTER);

        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        printButton = createStyledButton("Bileti Yazdır", PRIMARY_COLOR, Color.WHITE);
        // İkon olmadığı için sadece metin kullan
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printTicket();
            }
        });

        homeButton = createStyledButton("Ana Sayfaya Dön", DARK_COLOR, Color.WHITE);
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goHome();
            }
        });

        buttonPanel.add(printButton);
        buttonPanel.add(homeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ana panele bileşenleri ekle
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BOLD_LABEL_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor, 1, true));
        button.setPreferredSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void printTicket() {
        try {
            ticketDetails.print();
            showSuccessMessage("Bilet yazdırma işlemi başlatıldı.");
        } catch (PrinterException e) {
            showErrorMessage("Yazdırma sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void goHome() {
        // Ana sayfaya dön
        parentFrame.showHome();
    }

    private void showSuccessMessage(String message) {
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.INFORMATION_MESSAGE
        );
        JDialog dialog = optionPane.createDialog(this, "Başarılı");
        dialog.setVisible(true);
    }

    private void showErrorMessage(String message) {
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.ERROR_MESSAGE
        );
        JDialog dialog = optionPane.createDialog(this, "Hata");
        dialog.setVisible(true);
    }
}