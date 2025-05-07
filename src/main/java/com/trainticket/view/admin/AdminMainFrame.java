package com.trainticket.view.admin;

import com.trainticket.model.User;
import com.trainticket.view.LoginFrame;

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

public class AdminMainFrame extends JFrame {

    // Modern UI renkleri - kırmızı tema
    private static final Color PRIMARY_COLOR = new Color(139, 0, 0); // Koyu kırmızı
    private static final Color SECONDARY_COLOR = new Color(220, 53, 69); // Normal kırmızı
    private static final Color ACCENT_COLOR = new Color(255, 193, 7); // Sarı tonlu vurgu
    private static final Color DARK_COLOR = new Color(52, 58, 64); // Koyu gri
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Açık gri arka plan
    private static final Color CARD_COLOR = new Color(255, 255, 255); // Beyaz kart
    private static final Color TEXT_COLOR = new Color(52, 58, 64); // Koyu gri metin
    private static final Color LIGHT_TEXT_COLOR = new Color(108, 117, 125); // Açık gri metin
    private static final Color BORDER_COLOR = new Color(222, 226, 230); // Açık gri kenarlık

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private User adminUser;
    private JPanel mainPanel;
    private JPanel contentPanel;

    private JButton trainsButton;
    private JButton wagonsButton;
    private JButton usersButton;
    private JButton reportsButton;
    private JButton logoutButton;

    public AdminMainFrame(User user) {
        this.adminUser = user;
        initComponents();
    }

    private void initComponents() {
        // Frame ayarları
        setTitle("TCDD Yönetim Paneli");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Ana panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Üst panel (navbar)
        JPanel navbarPanel = createNavbarPanel();
        mainPanel.add(navbarPanel, BorderLayout.NORTH);

        // İçerik paneli (CardLayout ile farklı ekranlar arasında geçiş)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Hoş geldiniz paneli
        JPanel welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, "welcome");

        // Frame'e ana paneli ekle
        getContentPane().add(mainPanel);

        // İlk olarak hoş geldiniz panelini göster
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "welcome");
    }

    private JPanel createNavbarPanel() {
        JPanel navbarPanel = new JPanel();
        navbarPanel.setLayout(new BorderLayout());
        navbarPanel.setBackground(PRIMARY_COLOR);
        navbarPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Logo ve başlık (sol taraf)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("TCDD");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Yönetim Paneli");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        titleLabel.setForeground(Color.WHITE);

        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);

        // Menü butonları (orta kısım)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        menuPanel.setOpaque(false);

        trainsButton = createNavbarButton("Tren Seferleri", "trains");
        wagonsButton = createNavbarButton("Vagonlar", "wagons");
        usersButton = createNavbarButton("Kullanıcılar", "users");
        reportsButton = createNavbarButton("Raporlar", "reports");

        menuPanel.add(trainsButton);
        menuPanel.add(wagonsButton);
        menuPanel.add(usersButton);
        menuPanel.add(reportsButton);

        // Kullanıcı bilgisi ve çıkış butonu (sağ taraf)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);

        // Kullanıcı adı
        JLabel userLabel = new JLabel("Admin: " + (adminUser != null ? adminUser.getFullName() : "Yönetici"));
        userLabel.setFont(LABEL_FONT);
        userLabel.setForeground(Color.WHITE);

        // Çıkış butonu
        logoutButton = new JButton("Çıkış");
        logoutButton.setFont(SMALL_FONT);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(SECONDARY_COLOR);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(200, 35, 51));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(SECONDARY_COLOR);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        userPanel.add(userLabel);
        userPanel.add(logoutButton);

        // Navbar'ı birleştir
        navbarPanel.add(logoPanel, BorderLayout.WEST);
        navbarPanel.add(menuPanel, BorderLayout.CENTER);
        navbarPanel.add(userPanel, BorderLayout.EAST);

        return navbarPanel;
    }

    private JButton createNavbarButton(String text, String command) {
        JButton button = new JButton(text);
        button.setFont(BOLD_LABEL_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setActionCommand(command);

        // Hover efekti
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(120, 0, 0)); // Daha koyu kırmızı
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        // Tıklama olayı
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                switch (cmd) {
                    case "trains":
                        showTrainManagementPanel();
                        break;
                    case "wagons":
                        showWagonManagementPanel();
                        break;
                    case "users":
                        showUserManagementPanel();
                        break;
                    case "reports":
                        showReportsPanel();
                        break;
                }
            }
        });

        return button;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(30, 40, 40, 40));

        // Hoş geldiniz mesajı
        JPanel welcomeMessagePanel = new JPanel();
        welcomeMessagePanel.setLayout(new BoxLayout(welcomeMessagePanel, BoxLayout.Y_AXIS));
        welcomeMessagePanel.setOpaque(false);
        welcomeMessagePanel.setBorder(new EmptyBorder(20, 0, 30, 0));

        JLabel welcomeLabel = new JLabel("Hoş Geldiniz, " + adminUser.getFullName());
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("TCDD Yönetim Paneline hoş geldiniz. Lütfen bir işlem seçin.");
        subLabel.setFont(LABEL_FONT);
        subLabel.setForeground(LIGHT_TEXT_COLOR);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomeMessagePanel.add(welcomeLabel);
        welcomeMessagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomeMessagePanel.add(subLabel);

        // Hızlı erişim kartları
        JPanel quickAccessPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        quickAccessPanel.setOpaque(false);

        quickAccessPanel.add(createActionCard(
                "Tren Seferleri Yönetimi",
                "Tren seferlerini ekleyin, düzenleyin ve yönetin",
                PRIMARY_COLOR,
                e -> showTrainManagementPanel()
        ));

        quickAccessPanel.add(createActionCard(
                "Vagon Yönetimi",
                "Vagonları ve koltukları düzenleyin",
                SECONDARY_COLOR,
                e -> showWagonManagementPanel()
        ));

        quickAccessPanel.add(createActionCard(
                "Kullanıcı Yönetimi",
                "Kullanıcı hesaplarını görüntüleyin ve yönetin",
                DARK_COLOR,
                e -> showUserManagementPanel()
        ));

        quickAccessPanel.add(createActionCard(
                "Raporlar",
                "Satış ve kullanım istatistiklerini görüntüleyin",
                ACCENT_COLOR,
                e -> showReportsPanel()
        ));

        // İstatistikler paneli
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        statsPanel.add(createStatsCard("Kullanıcılar", getNumberOfUsers(), "kişi"));
        statsPanel.add(createStatsCard("Aktif Seferler", getNumberOfTrains(), "sefer"));
        statsPanel.add(createStatsCard("Satılan Biletler", getNumberOfTickets(), "bilet"));

        // Ana panele bileşenleri ekle
        panel.add(welcomeMessagePanel, BorderLayout.NORTH);
        panel.add(quickAccessPanel, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createActionCard(String title, String description, Color color, ActionListener listener) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Renkli başlık bandı
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(color);
        headerPanel.setPreferredSize(new Dimension(0, 8));
        card.add(headerPanel, BorderLayout.NORTH);

        // İçerik paneli
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='width:200px'>" + description + "</div></html>");
        descLabel.setFont(SMALL_FONT);
        descLabel.setForeground(LIGHT_TEXT_COLOR);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton actionButton = new JButton("İncele");
        actionButton.setFont(BOLD_LABEL_FONT);
        actionButton.setForeground(Color.WHITE);
        actionButton.setBackground(color);
        actionButton.setBorderPainted(false);
        actionButton.setFocusPainted(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionButton.setMaximumSize(new Dimension(100, 35));

        // Hover efekti
        Color darkerColor = color.darker();
        actionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                actionButton.setBackground(darkerColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                actionButton.setBackground(color);
            }
        });

        if (listener != null) {
            actionButton.addActionListener(listener);
        }

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(actionButton);

        card.add(contentPanel, BorderLayout.CENTER);

        // Kart tıklanabilir (bütün kart tıklanabilir)
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, "cardClicked"));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Hafif hover efekti
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(color, 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    private JPanel createStatsCard(String title, int value, String unit) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(BOLD_LABEL_FONT);
        titleLabel.setForeground(LIGHT_TEXT_COLOR);

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(PRIMARY_COLOR);

        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(SMALL_FONT);
        unitLabel.setForeground(LIGHT_TEXT_COLOR);

        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        valuePanel.setOpaque(false);
        valuePanel.add(valueLabel);
        valuePanel.add(unitLabel);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);

        return card;
    }

    private void showTrainManagementPanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Tren yönetim panelini oluştur
        TrainManagementPanel trainPanel = new TrainManagementPanel();
        contentPanel.add(trainPanel, "trains");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "trains");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showWagonManagementPanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Vagon yönetim panelini oluştur
        WagonManagementPanel wagonPanel = new WagonManagementPanel();
        contentPanel.add(wagonPanel, "wagons");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "wagons");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUserManagementPanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Kullanıcı yönetim panelini oluştur
        UserManagementPanel userPanel = new UserManagementPanel();
        contentPanel.add(userPanel, "users");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "users");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showReportsPanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Rapor panelini oluştur
        ReportPanel reportPanel = new ReportPanel();
        contentPanel.add(reportPanel, "reports");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "reports");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Çıkış yapmak istediğinizden emin misiniz?",
                "Çıkış",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Admin login ekranına dön
            AdminLoginFrame loginFrame = new AdminLoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }

    // İstatistik metodları (gerçek uygulamada DAO'lardan çekilecek)
    private int getNumberOfUsers() {
        // Örnek değer
        return 15;
    }

    private int getNumberOfTrains() {
        // Örnek değer
        return 30;
    }

    private int getNumberOfTickets() {
        // Örnek değer
        return 120;
    }
}