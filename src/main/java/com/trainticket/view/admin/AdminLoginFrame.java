package com.trainticket.view.admin;

import com.trainticket.dao.UserDAO;
import com.trainticket.model.User;
import com.trainticket.view.LoginFrame;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminLoginFrame extends JFrame {

    // Modern UI renkleri - kırmızı tema
    private static final Color PRIMARY_COLOR = new Color(139, 0, 0); // Koyu kırmızı
    private static final Color SECONDARY_COLOR = new Color(220, 53, 69); // Normal kırmızı
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Açık gri arka plan
    private static final Color TEXT_COLOR = new Color(52, 58, 64); // Koyu gri metin
    private static final Color PANEL_COLOR = new Color(255, 255, 255); // Beyaz panel
    private static final Color BORDER_COLOR = new Color(222, 226, 230); // Açık gri kenarlık

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel statusLabel;

    public AdminLoginFrame() {
        initComponents();
    }

    private void initComponents() {
        // Frame ayarları
        setTitle("TCDD Bilet Sistemi - Admin Girişi");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Ana panel: GridBagLayout ile esnek layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Logo panel: sol tarafta renkli panel
        JPanel logoPanel = createLogoPanel();

        // Login panel: sağ tarafta beyaz panel
        JPanel loginPanel = createLoginPanel();

        // Ana panele logo ve login panellerini ekle
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(logoPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        mainPanel.add(loginPanel, gbc);

        // Ana paneli frame'e ekle
        getContentPane().add(mainPanel);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(PRIMARY_COLOR);
        logoPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // TCDD Logosu
        JLabel logoLabel = new JLabel("TCDD");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Alt başlık
        JLabel taglineLabel = new JLabel("Yönetim Paneli");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slogan
        JLabel sloganLabel = new JLabel("Tren Bileti Sisteminin Yönetim Arayüzü");
        sloganLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        sloganLabel.setForeground(new Color(255, 255, 255, 200));
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Görsel bir dekoratif eleman - basit bir tren çizimi
        JPanel trainIconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Basit bir tren silueti çiz
                g2d.setColor(Color.WHITE);
                // Gövde
                g2d.fillRoundRect(50, 30, 150, 60, 10, 10);
                // Öndeki lokomotif
                g2d.fillRoundRect(20, 50, 50, 40, 10, 10);
                // Pencereler
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRect(70, 40, 20, 20);
                g2d.fillRect(110, 40, 20, 20);
                g2d.fillRect(150, 40, 20, 20);
                // Tekerlekler
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillOval(40, 85, 20, 20);
                g2d.fillOval(90, 85, 20, 20);
                g2d.fillOval(140, 85, 20, 20);
                g2d.fillOval(190, 85, 20, 20);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(250, 120);
            }
        };
        trainIconPanel.setOpaque(false);
        trainIconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Admin bilgisi
        JPanel adminInfoPanel = new JPanel();
        adminInfoPanel.setLayout(new BoxLayout(adminInfoPanel, BoxLayout.Y_AXIS));
        adminInfoPanel.setOpaque(false);
        adminInfoPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel adminInfoTitle = new JLabel("Yönetici Girişi");
        adminInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        adminInfoTitle.setForeground(Color.WHITE);
        adminInfoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel adminInfoText = new JLabel("Sadece yetkili kullanıcılar içindir");
        adminInfoText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminInfoText.setForeground(new Color(255, 255, 255, 200));
        adminInfoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        adminInfoPanel.add(adminInfoTitle);
        adminInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        adminInfoPanel.add(adminInfoText);

        // Panele elemanları ekle
        logoPanel.add(Box.createVerticalGlue());
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(taglineLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(sloganLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        logoPanel.add(trainIconPanel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        logoPanel.add(adminInfoPanel);
        logoPanel.add(Box.createVerticalGlue());

        return logoPanel;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(PANEL_COLOR);
        loginPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Başlık
        JLabel titleLabel = new JLabel("Admin Girişi");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        // Kullanıcı adı alanı
        JLabel usernameLabel = new JLabel("Kullanıcı Adı");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameField = createStyledTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 40));

        // Şifre alanı
        JLabel passwordLabel = new JLabel("Şifre");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordField = createStyledPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 40));

        // Durum mesajı için label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(SECONDARY_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Butonlar
        loginButton = createStyledButton("Admin Girişi", PRIMARY_COLOR, Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        backButton = createStyledButton("Normal Giriş Ekranına Dön", Color.WHITE, PRIMARY_COLOR);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });

        // Formları panele ekle
        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(statusLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(backButton);

        // Panele form panelini ekle
        loginPanel.add(titleLabel);
        loginPanel.add(formPanel);

        return loginPanel;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(LABEL_FONT);
        textField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(LABEL_FONT);
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return passwordField;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor.equals(Color.WHITE) ? PRIMARY_COLOR : bgColor, 1, true));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover efekti için MouseListener ekle
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (bgColor.equals(PRIMARY_COLOR)) {
                    button.setBackground(new Color(120, 0, 0)); // Daha koyu kırmızı
                } else {
                    button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                if (!bgColor.equals(PRIMARY_COLOR)) {
                    button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                }
            }
        });

        return button;
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Kullanıcı adı ve şifre gereklidir!");
            return;
        }

        // Admin kontrolü (sadece "admin" kullanıcısına izin ver)
        if (!"admin".equals(username)) {
            statusLabel.setText("Sadece admin kullanıcıları giriş yapabilir!");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateUser(username, password);

        if (user != null) {
            statusLabel.setText("Admin girişi başarılı!");
            statusLabel.setForeground(new Color(46, 204, 113)); // Yeşil renk

            // Admin ana ekranını aç
            AdminMainFrame adminMainFrame = new AdminMainFrame(user);
            adminMainFrame.setVisible(true);

            // Bu girişi kapat
            this.dispose();
        } else {
            statusLabel.setText("Geçersiz kullanıcı adı veya şifre!");
            statusLabel.setForeground(SECONDARY_COLOR);
        }
    }

    private void goBackToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
}