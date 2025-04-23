package com.trainticket.view;

import com.trainticket.dao.UserDAO;
import com.trainticket.model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.regex.Pattern;

public class RegisterFrame extends JFrame {

    // Modern UI renkleri
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219); // #3498db - Ana mavi renk
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113); // #2ecc71 - Yeşil
    private static final Color ERROR_COLOR = new Color(231, 76, 60); // #e74c3c - Kırmızı
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // #f8f9fa - Açık gri arka plan
    private static final Color TEXT_COLOR = new Color(52, 58, 64); // #343a40 - Koyu gri metin
    private static final Color PANEL_COLOR = new Color(255, 255, 255); // #ffffff - Beyaz panel
    private static final Color BORDER_COLOR = new Color(222, 226, 230); // #dee2e6 - Açık gri border

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font HINT_FONT = new Font("Segoe UI", Font.ITALIC, 12);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox;
    private JTextField tcNoField;
    private JTextField phoneField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;

    private JFrame parentFrame;

    public RegisterFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
    }

    private void initComponents() {
        // Frame ayarları
        setTitle("TCDD Bilet Sistemi - Kayıt Ol");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Ana panel: GridBagLayout ile esnek layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Bilgi Panel: sol tarafta bilgilendirme/marka paneli
        JPanel infoPanel = createInfoPanel();

        // Kayıt Panel: sağ tarafta form
        JPanel registerPanel = createRegisterPanel();

        // Ana layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(infoPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        mainPanel.add(registerPanel, gbc);

        // Ana paneli frame'e ekle
        getContentPane().add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(PRIMARY_COLOR);
        infoPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // TCDD Logosu
        JLabel logoLabel = new JLabel("TCDD");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Alt başlık
        JLabel subtitleLabel = new JLabel("Yeni Üyelik");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bilgi başlığı
        JLabel infoTitleLabel = new JLabel("Üyelik Avantajları");
        infoTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoTitleLabel.setForeground(Color.WHITE);
        infoTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avantaj listesi
        JPanel advantagePanel = new JPanel();
        advantagePanel.setLayout(new BoxLayout(advantagePanel, BoxLayout.Y_AXIS));
        advantagePanel.setOpaque(false);
        advantagePanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        String[] advantages = {
                "Hızlı bilet alımı",
                "Seyahat geçmişinizi görüntüleme",
                "Bilet iptal ve değişiklik kolaylığı",
                "Özel kampanya ve indirimler",
                "Favori rotalarınızı kaydetme",
                "E-posta ile seyahat hatırlatmaları"
        };

        for (String advantage : advantages) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            itemPanel.setOpaque(false);
            itemPanel.setMaximumSize(new Dimension(300, 35));

            // Check icon
            JLabel checkLabel = new JLabel("✓");
            checkLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            checkLabel.setForeground(Color.WHITE);

            // Advantage text
            JLabel textLabel = new JLabel(advantage);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(Color.WHITE);

            itemPanel.add(checkLabel);
            itemPanel.add(textLabel);
            advantagePanel.add(itemPanel);
        }

        // Panele elemanları ekle
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(logoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(subtitleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        infoPanel.add(infoTitleLabel);
        infoPanel.add(advantagePanel);
        infoPanel.add(Box.createVerticalGlue());

        return infoPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BorderLayout());
        registerPanel.setBackground(PANEL_COLOR);
        registerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Başlık paneli
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Yeni Hesap Oluştur");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Lütfen aşağıdaki bilgileri doldurunuz");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(new Color(108, 117, 125)); // #6c757d
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);

        // Form paneli - GridBagLayout ile form daha düzenli olacak
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Form elemanları
        // Kullanıcı adı
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Kullanıcı Adı *");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        // Şifre
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Şifre *");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // Şifre Tekrar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel confirmPasswordLabel = new JLabel("Şifre Tekrar *");
        confirmPasswordLabel.setFont(LABEL_FONT);
        confirmPasswordLabel.setForeground(TEXT_COLOR);
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        confirmPasswordField = createStyledPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Ad Soyad
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        JLabel fullNameLabel = new JLabel("Ad Soyad *");
        fullNameLabel.setFont(LABEL_FONT);
        fullNameLabel.setForeground(TEXT_COLOR);
        formPanel.add(fullNameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        fullNameField = createStyledTextField();
        formPanel.add(fullNameField, gbc);

        // İki sütunlu alanlar - Sol sütun
        // E-posta
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        JLabel emailLabel = new JLabel("E-posta *");
        emailLabel.setFont(LABEL_FONT);
        emailLabel.setForeground(TEXT_COLOR);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        emailField = createStyledTextField();
        formPanel.add(emailField, gbc);

        // Cinsiyet
        gbc.gridx = 0;
        gbc.gridy = 10;
        JLabel genderLabel = new JLabel("Cinsiyet *");
        genderLabel.setFont(LABEL_FONT);
        genderLabel.setForeground(TEXT_COLOR);
        formPanel.add(genderLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        String[] genders = {"Seçiniz...", "Erkek", "Kadın"};
        genderComboBox = createStyledComboBox(genders);
        formPanel.add(genderComboBox, gbc);

        // İki sütunlu alanlar - Sağ sütun
        // TC Kimlik No
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.insets = new Insets(8, 20, 8, 0);
        JLabel tcNoLabel = new JLabel("TC Kimlik No *");
        tcNoLabel.setFont(LABEL_FONT);
        tcNoLabel.setForeground(TEXT_COLOR);
        formPanel.add(tcNoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        tcNoField = createStyledTextField();
        formPanel.add(tcNoField, gbc);

        // Telefon
        gbc.gridx = 1;
        gbc.gridy = 10;
        JLabel phoneLabel = new JLabel("Telefon");
        phoneLabel.setFont(LABEL_FONT);
        phoneLabel.setForeground(TEXT_COLOR);
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        phoneField = createStyledTextField();
        formPanel.add(phoneField, gbc);

        // Durum mesajı
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 8, 0);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(HINT_FONT);
        statusLabel.setForeground(ERROR_COLOR);
        formPanel.add(statusLabel, gbc);

        // Scrollable form paneli oluştur (sadece form içeriği için)
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null); // Scrollpane kenarlığını kaldır
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Kaydırma çubuğunun sadece gerekliyse görünmesini sağla
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Daha modern kaydırma çubuğu görünümü
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Daha hızlı kaydırma

        // Buton paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        backButton = createStyledButton("Giriş Ekranına Dön", Color.WHITE, PRIMARY_COLOR);
        registerButton = createStyledButton("Kayıt Ol", PRIMARY_COLOR, Color.WHITE);

        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(registerButton);

        // Panel'i frame'e ekle - form yerine scrollPane kullanılıyor
        registerPanel.add(headerPanel, BorderLayout.NORTH);
        registerPanel.add(scrollPane, BorderLayout.CENTER); // Değişiklik burada
        registerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Olay dinleyicileri
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });

        return registerPanel;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(LABEL_FONT);
        textField.setPreferredSize(new Dimension(0, 40));
        textField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(LABEL_FONT);
        passwordField.setPreferredSize(new Dimension(0, 40));
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return passwordField;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(LABEL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(0, 40));
        comboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        return comboBox;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(fgColor.equals(PRIMARY_COLOR) ? PRIMARY_COLOR : bgColor, 1, true));
        button.setPreferredSize(new Dimension(180, 40));

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
                if (!bgColor.equals(PRIMARY_COLOR)) {
                    button.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
                }
            }
        });

        return button;
    }

    private void register() {
        // Form verilerini al
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String tcNo = tcNoField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validasyon
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                fullName.isEmpty() || email.isEmpty() || gender.equals("Seçiniz...") ||
                tcNo.isEmpty()) {

            statusLabel.setText("Lütfen tüm zorunlu alanları doldurun! (*)");
            statusLabel.setForeground(ERROR_COLOR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Şifreler eşleşmiyor!");
            statusLabel.setForeground(ERROR_COLOR);
            return;
        }

        // E-posta formatı kontrolü
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            statusLabel.setText("Geçerli bir e-posta adresi girin!");
            statusLabel.setForeground(ERROR_COLOR);
            return;
        }

        if (tcNo.length() != 11 || !tcNo.matches("\\d+")) {
            statusLabel.setText("TC Kimlik No 11 haneli sayısal bir değer olmalıdır!");
            statusLabel.setForeground(ERROR_COLOR);
            return;
        }

        // Kullanıcı oluştur
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setGender(gender);
        user.setTcNo(tcNo);
        user.setPhoneNumber(phone);

        // Veritabanına kaydet
        UserDAO userDAO = new UserDAO();

        // TC No zaten kayıtlı mı kontrol et
        if (userDAO.findUserByTcNo(tcNo) != null) {
            statusLabel.setText("Bu TC Kimlik Numarası zaten kayıtlı!");
            statusLabel.setForeground(ERROR_COLOR);
            return;
        }

        boolean success = userDAO.addUser(user);
        if (success) {
            // Başarılı mesajı göster
            statusLabel.setText("Kayıt başarıyla tamamlandı! Giriş yapabilirsiniz.");
            statusLabel.setForeground(SECONDARY_COLOR);

            // Kullanıcı arayüzünü geçici olarak devre dışı bırak
            setFormEnabled(false);

            // Kısa bir süre sonra giriş ekranına dön
            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    goBackToLogin();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            statusLabel.setText("Kayıt sırasında bir hata oluştu! Kullanıcı adı zaten kullanılıyor olabilir.");
            statusLabel.setForeground(ERROR_COLOR);
        }
    }

    private void setFormEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        confirmPasswordField.setEnabled(enabled);
        fullNameField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        genderComboBox.setEnabled(enabled);
        tcNoField.setEnabled(enabled);
        phoneField.setEnabled(enabled);
        registerButton.setEnabled(enabled);
        backButton.setEnabled(enabled);
    }

    private void goBackToLogin() {
        parentFrame.setVisible(true);
        this.dispose();
    }
}