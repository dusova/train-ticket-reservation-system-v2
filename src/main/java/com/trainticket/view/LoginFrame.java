package com.trainticket.view;

import com.trainticket.dao.UserDAO;
import com.trainticket.model.User;
import com.trainticket.view.admin.AdminLoginFrame;

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
import java.util.Objects;

public class LoginFrame extends JFrame {

    // Modern UI renkleri
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219); // #3498db - Ana mavi renk
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113); // #2ecc71 - Yeşil
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // #f8f9fa - Açık gri arka plan
    private static final Color TEXT_COLOR = new Color(52, 58, 64); // #343a40 - Koyu gri metin
    private static final Color PANEL_COLOR = new Color(255, 255, 255); // #ffffff - Beyaz panel
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        // Frame ayarları
        setTitle("TCDD Bilet Sistemi");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Ana panel: GridBagLayout ile esnek layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Login panel: sağ tarafta beyaz panel
        JPanel loginPanel = createLoginPanel();

        // Logo panel: sol tarafta renkli panel
        JPanel logoPanel = createLogoPanel();

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

        // TCDD Logosu (burada bir ImageIcon kullanabilirsiniz)
        JLabel logoLabel = new JLabel("TCDD");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Alt başlık
        JLabel taglineLabel = new JLabel("Tren Bileti Sistemi");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slogan
        JLabel sloganLabel = new JLabel("Konforlu Yolculuğun Adresi");
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
                g2d.setColor(new Color(52, 152, 219));
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

        // Panele elemanları ekle
        logoPanel.add(Box.createVerticalGlue());
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(taglineLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(sloganLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        logoPanel.add(trainIconPanel);
        logoPanel.add(Box.createVerticalGlue());

        return logoPanel;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(PANEL_COLOR);
        loginPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Başlık
        JLabel titleLabel = new JLabel("Giriş Yap");
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
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Butonlar
        loginButton = createStyledButton("Giriş Yap", PRIMARY_COLOR, Color.WHITE);
        registerButton = createStyledButton("Kayıt Ol", Color.WHITE, PRIMARY_COLOR);

        // Admin giriş butonu
        JLabel adminLoginLabel = new JLabel("Admin Girişi");
        adminLoginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminLoginLabel.setForeground(TEXT_COLOR);
        adminLoginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        adminLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                adminLoginLabel.setForeground(PRIMARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                adminLoginLabel.setForeground(TEXT_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new AdminLoginFrame().setVisible(true);
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
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(adminLoginLabel);

        // Panele form panelini ekle
        loginPanel.add(titleLabel);
        loginPanel.add(formPanel);

        // Olay dinleyicileri
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterFrame();
            }
        });

        return loginPanel;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(LABEL_FONT);
        textField.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(LABEL_FONT);
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
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
        button.setBorder(new LineBorder(bgColor, 1, true));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover efekti için MouseListener ekle
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
                if (!bgColor.equals(PRIMARY_COLOR)) {
                    button.setBorder(new LineBorder(bgColor, 1, true));
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

        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateUser(username, password);

        if (user != null) {
            statusLabel.setText("Giriş başarılı!");
            statusLabel.setForeground(SECONDARY_COLOR);

            // Ana ekranı aç
            MainFrame mainFrame = new MainFrame(user);
            mainFrame.setVisible(true);

            // Giriş ekranını kapat
            this.dispose();
        } else {
            statusLabel.setText("Geçersiz kullanıcı adı veya şifre!");
            statusLabel.setForeground(Color.RED);
            passwordField.setText("");
        }
    }

    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame(this);
        registerFrame.setVisible(true);
        this.setVisible(false);
    }

    // Özel bir panel sınıfı - yuvarlak köşeli paneller için
    private class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 15;

        public RoundedPanel(Color bgColor) {
            super();
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(backgroundColor);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2d.dispose();
        }
    }
}