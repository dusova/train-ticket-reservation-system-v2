package com.trainticket.view;

import com.trainticket.model.User;
import com.trainticket.model.Ticket;
import com.trainticket.dao.TicketDAO;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {

    // Modern UI renkleri
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219); // #3498db - Ana mavi renk
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113); // #2ecc71 - Yeşil
    private static final Color ACCENT_COLOR = new Color(231, 76, 60); // #e74c3c - Kırmızı/Turuncu
    private static final Color DARK_COLOR = new Color(52, 73, 94); // #34495e - Koyu mavi/gri
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // #f8f9fa - Açık gri arka plan
    private static final Color CARD_COLOR = new Color(255, 255, 255); // #ffffff - Beyaz panel
    private static final Color TEXT_COLOR = new Color(52, 58, 64); // #343a40 - Koyu gri metin
    private static final Color LIGHT_TEXT_COLOR = new Color(108, 117, 125); // #6c757d - Açık gri metin

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private User currentUser;
    private JPanel contentPanel;

    private JButton searchTrainsButton;
    private JButton myTicketsButton;
    private JButton userProfileButton;
    private JButton logoutButton;

    private List<Ticket> recentTickets;

    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
        showWelcomePanel();
    }

    private void initComponents() {
        // Frame ayarları
        setTitle("TCDD Bilet Sistemi");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Ana panel: BorderLayout kullanıyoruz
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Navbar paneli (Üst panel)
        JPanel navbarPanel = createNavbarPanel();
        mainPanel.add(navbarPanel, BorderLayout.NORTH);

        // İçerik paneli (CardLayout ile farklı ekranlar arasında geçiş)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Frame'e ana paneli ekle
        getContentPane().add(mainPanel);
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

        JLabel titleLabel = new JLabel("Bilet Sistemi");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        titleLabel.setForeground(Color.WHITE);

        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);

        // Menü butonları (orta kısım)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        menuPanel.setOpaque(false);

        searchTrainsButton = createNavbarButton("Bilet Ara", "search");
        myTicketsButton = createNavbarButton("Biletlerim", "tickets");
        userProfileButton = createNavbarButton("Profilim", "profile");

        menuPanel.add(searchTrainsButton);
        menuPanel.add(myTicketsButton);
        menuPanel.add(userProfileButton);

        // Kullanıcı bilgisi ve çıkış butonu (sağ taraf)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);

        // Kullanıcı adı
        JLabel userLabel = new JLabel("Hoş geldin, " + (currentUser != null ? currentUser.getFullName() : "Misafir"));
        userLabel.setFont(LABEL_FONT);
        userLabel.setForeground(Color.WHITE);

        // Çıkış butonu
        logoutButton = new JButton("Çıkış");
        logoutButton.setFont(SMALL_FONT);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(231, 76, 60)); // Kırmızı
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(192, 57, 43)); // Daha koyu kırmızı
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(231, 76, 60));
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
                button.setBackground(new Color(41, 128, 185)); // Daha koyu mavi
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
                    case "search":
                        showSearchTrainsPanel();
                        break;
                    case "tickets":
                        showMyTicketsPanel();
                        break;
                    case "profile":
                        showUserProfilePanel();
                        break;
                }
            }
        });

        return button;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout(20, 20));
        welcomePanel.setBackground(BACKGROUND_COLOR);
        welcomePanel.setBorder(new EmptyBorder(30, 40, 40, 40));

        // Üst bilgi - hoş geldin mesajı ve kısa açıklama
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Hoş Geldiniz, " + currentUser.getFullName());
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("TCDD Bilet Sistemi ile seyahatlerinizi kolayca planlayın");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subtitleLabel);

        // Ana içerik paneli
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;

        // Hızlı erişim kartları
        JPanel quickAccessPanel = createQuickAccessPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(quickAccessPanel, gbc);

        // Recent tickets ve popüler rotalar için grid
        gbc.gridy = 1;
        gbc.weighty = 0.4;
        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        gridPanel.setOpaque(false);

        // Son biletler paneli
        JPanel recentTicketsPanel = createRecentTicketsPanel();

        // Popüler rotalar paneli
        JPanel popularRoutesPanel = createPopularRoutesPanel();

        gridPanel.add(recentTicketsPanel);
        gridPanel.add(popularRoutesPanel);

        contentPanel.add(gridPanel, gbc);

        // Ana paneli birleştir
        welcomePanel.add(headerPanel, BorderLayout.NORTH);
        welcomePanel.add(contentPanel, BorderLayout.CENTER);

        return welcomePanel;
    }

    private JPanel createQuickAccessPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);

        // Bilet Ara Kartı
        JPanel searchCard = createActionCard(
                "Bilet Ara",
                "Şehirler ve tarih seçerek hızlıca tren bileti arayın",
                PRIMARY_COLOR,
                e -> showSearchTrainsPanel()
        );

        // Biletlerim Kartı
        JPanel ticketsCard = createActionCard(
                "Biletlerim",
                "Mevcut ve geçmiş biletlerinizi görüntüleyin",
                SECONDARY_COLOR,
                e -> showMyTicketsPanel()
        );

        // Profilim Kartı
        JPanel profileCard = createActionCard(
                "Profilim",
                "Kişisel bilgilerinizi görüntüleyin ve güncelleyin",
                DARK_COLOR,
                e -> showUserProfilePanel()
        );

        panel.add(searchCard);
        panel.add(ticketsCard);
        panel.add(profileCard);

        return panel;
    }

    private JPanel createActionCard(String title, String description, Color color, ActionListener listener) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1, true),
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
                        new LineBorder(new Color(222, 226, 230), 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    private JPanel createRecentTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Başlık
        JLabel titleLabel = new JLabel("Son Biletleriniz");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Son biletler listesi
        JPanel ticketsListPanel = new JPanel();
        ticketsListPanel.setLayout(new BoxLayout(ticketsListPanel, BoxLayout.Y_AXIS));
        ticketsListPanel.setOpaque(false);

        // Veritabanından biletleri getir
        TicketDAO ticketDAO = new TicketDAO();
        recentTickets = ticketDAO.getTicketsByUserId(currentUser.getId());

        if (recentTickets.isEmpty()) {
            JLabel noTicketsLabel = new JLabel("Henüz bilet satın almadınız.");
            noTicketsLabel.setFont(LABEL_FONT);
            noTicketsLabel.setForeground(LIGHT_TEXT_COLOR);
            noTicketsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel noTicketsPanel = new JPanel();
            noTicketsPanel.setLayout(new BoxLayout(noTicketsPanel, BoxLayout.Y_AXIS));
            noTicketsPanel.setOpaque(false);
            noTicketsPanel.add(Box.createVerticalGlue());
            noTicketsPanel.add(noTicketsLabel);
            noTicketsPanel.add(Box.createVerticalGlue());

            panel.add(noTicketsPanel, BorderLayout.CENTER);
        } else {
            // Son 3 bileti göster
            int count = Math.min(recentTickets.size(), 3);

            for (int i = 0; i < count; i++) {
                Ticket ticket = recentTickets.get(i);
                JPanel ticketItemPanel = createTicketItemPanel(ticket);
                ticketsListPanel.add(ticketItemPanel);

                // Son öğe değilse ayraç ekle
                if (i < count - 1) {
                    ticketsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
                    separator.setForeground(new Color(222, 226, 230));
                    separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    ticketsListPanel.add(separator);
                    ticketsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            panel.add(ticketsListPanel, BorderLayout.CENTER);
        }

        // Tümünü Görüntüle butonu
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton viewAllButton = new JButton("Tümünü Görüntüle");
        viewAllButton.setFont(SMALL_FONT);
        viewAllButton.setForeground(PRIMARY_COLOR);
        viewAllButton.setBackground(CARD_COLOR);
        viewAllButton.setBorderPainted(false);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllButton.addActionListener(e -> showMyTicketsPanel());

        buttonPanel.add(viewAllButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTicketItemPanel(Ticket ticket) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // İkon (sol) - basit bir simge
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Bilet ikonunun arka planı
                g2d.setColor(new Color(52, 152, 219, 40)); // Açık mavi
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Bilet ikonunun kendisi
                g2d.setColor(PRIMARY_COLOR);
                int padding = getWidth() / 4;
                g2d.fillRoundRect(padding, getHeight()/3, getWidth() - 2*padding, getHeight()/3, 5, 5);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
        };
        iconPanel.setOpaque(false);

        // Bilet bilgileri (orta)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel routeLabel = new JLabel();
        if (ticket.getTrain() != null) {
            routeLabel.setText(ticket.getTrain().getDepartureStation() + " → " + ticket.getTrain().getArrivalStation());
        } else {
            routeLabel.setText("Bilet #" + ticket.getId());
        }
        routeLabel.setFont(BOLD_LABEL_FONT);
        routeLabel.setForeground(TEXT_COLOR);
        routeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel();
        if (ticket.getTrain() != null) {
            dateLabel.setText(ticket.getTrain().getFormattedDepartureTime());
        } else {
            dateLabel.setText("Tarih bilgisi yok");
        }
        dateLabel.setFont(SMALL_FONT);
        dateLabel.setForeground(LIGHT_TEXT_COLOR);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(routeLabel);
        infoPanel.add(dateLabel);

        // Görüntüle butonu (sağ)
        JButton viewButton = new JButton("Detay");
        viewButton.setFont(SMALL_FONT);
        viewButton.setForeground(Color.WHITE);
        viewButton.setBackground(SECONDARY_COLOR);
        viewButton.setBorderPainted(false);
        viewButton.setFocusPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> showTicketDetails(ticket));

        // Hover efekti
        viewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                viewButton.setBackground(SECONDARY_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                viewButton.setBackground(SECONDARY_COLOR);
            }
        });

        // Panel tıklanabilir
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTicketDetails(ticket);
            }
        });

        panel.add(iconPanel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(viewButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createPopularRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Başlık
        JLabel titleLabel = new JLabel("Popüler Rotalar");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Popüler rotalar listesi
        JPanel routesListPanel = new JPanel();
        routesListPanel.setLayout(new BoxLayout(routesListPanel, BoxLayout.Y_AXIS));
        routesListPanel.setOpaque(false);

        // Örnek popüler rotalar (gerçekte veritabanından alınabilir)
        String[][] popularRoutes = {
                {"İstanbul", "Ankara", "₺180"},
                {"Ankara", "Konya", "₺120"},
                {"İzmir", "İstanbul", "₺220"},
                {"Eskişehir", "İstanbul", "₺150"}
        };

        for (int i = 0; i < popularRoutes.length; i++) {
            String[] route = popularRoutes[i];
            JPanel routeItemPanel = createRouteItemPanel(route[0], route[1], route[2]);
            routesListPanel.add(routeItemPanel);

            // Son öğe değilse ayraç ekle
            if (i < popularRoutes.length - 1) {
                routesListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
                separator.setForeground(new Color(222, 226, 230));
                separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                routesListPanel.add(separator);
                routesListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        panel.add(routesListPanel, BorderLayout.CENTER);

        // Tümünü Görüntüle butonu
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton searchButton = new JButton("Bilet Ara");
        searchButton.setFont(SMALL_FONT);
        searchButton.setForeground(PRIMARY_COLOR);
        searchButton.setBackground(CARD_COLOR);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> showSearchTrainsPanel());

        buttonPanel.add(searchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRouteItemPanel(String from, String to, String price) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Rota (sol)
        JPanel routePanel = new JPanel();
        routePanel.setLayout(new BoxLayout(routePanel, BoxLayout.Y_AXIS));
        routePanel.setOpaque(false);

        JLabel routeLabel = new JLabel(from + " → " + to);
        routeLabel.setFont(BOLD_LABEL_FONT);
        routeLabel.setForeground(TEXT_COLOR);
        routeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel estimateLabel = new JLabel("~4s 30d");  // Örnek süre
        estimateLabel.setFont(SMALL_FONT);
        estimateLabel.setForeground(LIGHT_TEXT_COLOR);
        estimateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        routePanel.add(routeLabel);
        routePanel.add(estimateLabel);

        // Fiyat (sağ)
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setOpaque(false);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(BOLD_LABEL_FONT);
        priceLabel.setForeground(SECONDARY_COLOR);
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel infoLabel = new JLabel("Başlangıç fiyatı");
        infoLabel.setFont(SMALL_FONT);
        infoLabel.setForeground(LIGHT_TEXT_COLOR);
        infoLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pricePanel.add(priceLabel);
        pricePanel.add(infoLabel);

        // Panel tıklanabilir
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Bilet arama ekranını aç, ilgili şehirleri seç
                showSearchTrainsPanel();
            }
        });

        panel.add(routePanel, BorderLayout.WEST);
        panel.add(pricePanel, BorderLayout.EAST);

        return panel;
    }

    private void showSearchTrainsPanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Tren arama panelini oluştur
        SearchTrainsPanel searchPanel = new SearchTrainsPanel(currentUser, this);
        contentPanel.add(searchPanel, "search");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "search");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMyTicketsPanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Biletlerim panelini oluştur
        MyTicketsPanel ticketsPanel = new MyTicketsPanel(currentUser);
        contentPanel.add(ticketsPanel, "tickets");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "tickets");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUserProfilePanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Profil panelini oluştur
        UserProfilePanel profilePanel = new UserProfilePanel(currentUser);
        contentPanel.add(profilePanel, "profile");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "profile");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showWelcomePanel() {
        // Mevcut içeriği temizle
        contentPanel.removeAll();

        // Hoş geldiniz panelini yeniden oluştur (son biletleri güncellemek için)
        JPanel welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, "welcome");

        // Görünümü güncelle
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "welcome");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showTicketDetails(Ticket ticket) {
        // Modern bilet detay diyaloğu
        JDialog ticketDialog = new JDialog(this, "Bilet Detayları", true);
        ticketDialog.setSize(450, 500);
        ticketDialog.setLocationRelativeTo(this);
        ticketDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Başlık
        JLabel titleLabel = new JLabel("Bilet Detayları");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Bilet kartı
        JPanel ticketCard = new JPanel();
        ticketCard.setLayout(new BoxLayout(ticketCard, BoxLayout.Y_AXIS));
        ticketCard.setBackground(CARD_COLOR);
        ticketCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Tren rotası ve zaman bilgisi
        JPanel routePanel = new JPanel(new BorderLayout());
        routePanel.setOpaque(false);

        JPanel stationsPanel = new JPanel();
        stationsPanel.setLayout(new BoxLayout(stationsPanel, BoxLayout.Y_AXIS));
        stationsPanel.setOpaque(false);

        JLabel fromLabel = new JLabel();
        if (ticket.getTrain() != null) {
            fromLabel.setText(ticket.getTrain().getDepartureStation());
        } else {
            fromLabel.setText("Kalkış");
        }
        fromLabel.setFont(TITLE_FONT);
        fromLabel.setForeground(TEXT_COLOR);
        fromLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrowLabel = new JLabel("↓");
        arrowLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        arrowLabel.setForeground(LIGHT_TEXT_COLOR);
        arrowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel toLabel = new JLabel();
        if (ticket.getTrain() != null) {
            toLabel.setText(ticket.getTrain().getArrivalStation());
        } else {
            toLabel.setText("Varış");
        }
        toLabel.setFont(TITLE_FONT);
        toLabel.setForeground(TEXT_COLOR);
        toLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        stationsPanel.add(fromLabel);
        stationsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        stationsPanel.add(arrowLabel);
        stationsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        stationsPanel.add(toLabel);

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setOpaque(false);

        JLabel dateLabel = new JLabel();
        if (ticket.getTrain() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            dateLabel.setText(ticket.getTrain().getDepartureTime().format(formatter));
        } else {
            dateLabel.setText("Tarih");
        }
        dateLabel.setFont(BOLD_LABEL_FONT);
        dateLabel.setForeground(TEXT_COLOR);
        dateLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel timeFromLabel = new JLabel();
        if (ticket.getTrain() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            timeFromLabel.setText(ticket.getTrain().getDepartureTime().format(formatter));
        } else {
            timeFromLabel.setText("--:--");
        }
        timeFromLabel.setFont(BOLD_LABEL_FONT);
        timeFromLabel.setForeground(PRIMARY_COLOR);
        timeFromLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel timeToLabel = new JLabel();
        if (ticket.getTrain() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            timeToLabel.setText(ticket.getTrain().getArrivalTime().format(formatter));
        } else {
            timeToLabel.setText("--:--");
        }
        timeToLabel.setFont(BOLD_LABEL_FONT);
        timeToLabel.setForeground(PRIMARY_COLOR);
        timeToLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        timePanel.add(dateLabel);
        timePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        timePanel.add(timeFromLabel);
        timePanel.add(Box.createRigidArea(new Dimension(0, 25)));
        timePanel.add(timeToLabel);

        routePanel.add(stationsPanel, BorderLayout.WEST);
        routePanel.add(timePanel, BorderLayout.EAST);

        // Ayırıcı çizgi
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(new Color(222, 226, 230));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Diğer bilet detayları
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        detailsPanel.setOpaque(false);

        addDetailRow(detailsPanel, "Yolcu:", ticket.getPassengerName());
        addDetailRow(detailsPanel, "TC No:", ticket.getPassengerTcNo());

        if (ticket.getWagon() != null) {
            addDetailRow(detailsPanel, "Vagon:", ticket.getWagon().getWagonNumber() + " (" + ticket.getWagon().getWagonType() + ")");
        } else {
            addDetailRow(detailsPanel, "Vagon:", "Bilgi yok");
        }

        if (ticket.getSeat() != null) {
            addDetailRow(detailsPanel, "Koltuk:", String.valueOf(ticket.getSeat().getSeatNumber()));
        } else {
            addDetailRow(detailsPanel, "Koltuk:", "Bilgi yok");
        }

        addDetailRow(detailsPanel, "Ücret:", String.format("%.2f TL", ticket.getPrice()));

        // Ödeme durumu
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        paymentPanel.setOpaque(false);

        JLabel paymentLabel = new JLabel("Ödeme Durumu:");
        paymentLabel.setFont(BOLD_LABEL_FONT);
        paymentLabel.setForeground(TEXT_COLOR);

        JLabel statusLabel = new JLabel(ticket.isPaid() ? "Ödenmiş" : "Ödenmemiş");
        statusLabel.setFont(BOLD_LABEL_FONT);
        statusLabel.setForeground(ticket.isPaid() ? SECONDARY_COLOR : ACCENT_COLOR);

        paymentPanel.add(paymentLabel);
        paymentPanel.add(statusLabel);

        // Ek açıklamalar
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        notesPanel.setOpaque(false);
        notesPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel pnrLabel = new JLabel("PNR: " + ticket.getId());
        pnrLabel.setFont(SMALL_FONT);
        pnrLabel.setForeground(LIGHT_TEXT_COLOR);
        pnrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel purchaseDateLabel = new JLabel("Satın Alma: " + ticket.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        purchaseDateLabel.setFont(SMALL_FONT);
        purchaseDateLabel.setForeground(LIGHT_TEXT_COLOR);
        purchaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        notesPanel.add(pnrLabel);
        notesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        notesPanel.add(purchaseDateLabel);

        // Tüm bileşenleri karta ekle
        ticketCard.add(routePanel);
        ticketCard.add(Box.createRigidArea(new Dimension(0, 20)));
        ticketCard.add(separator);
        ticketCard.add(Box.createRigidArea(new Dimension(0, 20)));
        ticketCard.add(detailsPanel);
        ticketCard.add(Box.createRigidArea(new Dimension(0, 10)));
        ticketCard.add(paymentPanel);
        ticketCard.add(Box.createRigidArea(new Dimension(0, 10)));
        ticketCard.add(notesPanel);

        // Kartı panele ekle
        JScrollPane scrollPane = new JScrollPane(ticketCard);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton printButton = new JButton("Yazdır");
        printButton.setFont(BOLD_LABEL_FONT);
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(DARK_COLOR);
        printButton.setFocusPainted(false);
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Burada yazdırma işlemi yapılabilir
                    JOptionPane.showMessageDialog(ticketDialog,
                            "Bilet yazdırma işlemi başlatıldı.",
                            "Yazdırma",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ticketDialog,
                            "Yazdırma sırasında bir hata oluştu: " + ex.getMessage(),
                            "Yazdırma Hatası",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(printButton);

        JButton closeButton = new JButton("Kapat");
        closeButton.setFont(BOLD_LABEL_FONT);
        closeButton.setForeground(PRIMARY_COLOR);
        closeButton.setBackground(CARD_COLOR);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> ticketDialog.dispose());
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        ticketDialog.add(panel);
        ticketDialog.setVisible(true);
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

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Çıkış yapmak istediğinizden emin misiniz?",
                "Çıkış",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Login ekranına dön
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }

    // Diğer panel'leri MainFrame'e eklemek için yardımcı metot
    public void showPanel(JPanel panel, String name) {
        contentPanel.removeAll();
        contentPanel.add(panel, name);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, name);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Ana sayfaya dönmek için
    public void showHome() {
        showWelcomePanel();
    }
}