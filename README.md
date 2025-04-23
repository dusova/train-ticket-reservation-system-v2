# Train Ticket Reservation System

## 🚆 Proje Açıklaması

Bu proje, modern bir tren bileti rezervasyon sisteminin temel işlevselliğini gösteren kapsamlı bir yazılım uygulamasıdır. Gerçek dünya tren bileti satış sistemlerinin temel özelliklerini ve iş akışlarını simüle etmek için geliştirilmiştir.

## ✨ Proje Amacı

Projenin temel amaçları şunlardır:
- Nesne Yönelimli Programlama (OOP) prensiplerinin pratik uygulaması
- Modern Java Swing kullanarak masaüstü uygulaması geliştirme
- Veritabanı entegrasyonu ve veri yönetimi
- Kullanıcı dostu arayüz tasarımı
- Gerçek dünya senaryolarını yansıtan bir yazılım mimarisi oluşturma

## 🌟 Temel Özellikler

### Kullanıcı Tarafı Özellikleri
- 🔐 Güvenli kullanıcı kayıt ve giriş sistemi
- 🚉 Tren seferi arama
- 💺 Koltuk seçimi ve rezervasyonu
- 📋 Bilet satın alma
- 👤 Kullanıcı profil yönetimi
- 🎫 Bilet geçmişi görüntüleme

### Yönetici Tarafı Özellikleri
- 🚂 Tren seferlerini yönetme
- 👥 Kullanıcı hesaplarını yönetme
- 🚉 Vagon ve koltuk yönetimi
- 📊 Temel raporlama

## 🛠 Teknolojik Altyapı

### Kullanılan Teknolojiler
- **Programlama Dili**: Java 8+
- **Arayüz Kütüphanesi**: Java Swing
- **UI Tasarımı**: FlatLaf Modern UI Kütüphanesi
- **Veritabanı**: SQLite
- **Proje Yönetimi**: Maven

### Mimari Yaklaşım
Projede Model-View-Controller (MVC) mimari deseni kullanılmıştır:
- **Model**: Veri yapıları ve iş mantığı
- **View**: Kullanıcı arayüzü bileşenleri
- **Controller**: Model ve View arasındaki iletişimi sağlayan sınıflar

## 🏗 Proje Yapısı

```
src/main/java/com/trainticket/
│
├── controller/           # İş mantığı kontrolleri
│   ├── ReservationController.java
│   ├── TicketController.java
│   ├── TrainController.java
│   └── UserController.java
│
├── dao/                  # Veritabanı erişim katmanı
│   ├── SeatDAO.java
│   ├── TicketDAO.java
│   ├── TrainDAO.java
│   └── UserDAO.java
│
├── model/                # Veri modelleri
│   ├── Seat.java
│   ├── Ticket.java
│   ├── Train.java
│   ├── User.java
│   └── Wagon.java
│
├── util/                 # Yardımcı sınıflar
│   └── DatabaseUtil.java
│
└── view/                 # Kullanıcı arayüzü bileşenleri
    ├── LoginFrame.java
    ├── MainFrame.java
    └── ...
```

## 🚀 Kurulum ve Çalıştırma

### Ön Gereksinimler
- Java Development Kit (JDK) 8 veya üzeri
- Maven

### Kurulum Adımları
1. Depoyu klonlayın
   ```bash
   git clone https://github.com/kullanici/train-ticket-reservation-system.git
   ```

2. Proje dizinine gidin
   ```bash
   cd train-ticket-reservation-system
   ```

3. Bağımlılıkları yükleyin
   ```bash
   mvn clean install
   ```

4. Uygulamayı çalıştırın
   ```bash
   mvn exec:java
   ```

## 🔐 Giriş Bilgileri

### Varsayılan Kullanıcılar
- **Normal Kullanıcı**
  - Kullanıcı Adı: `kullanici`
  - Şifre: `sifre123`

- **Admin Kullanıcısı**
  - Kullanıcı Adı: `admin`
  - Şifre: `admin123`

## 🌈 Öne Çıkan Özellikler

- Modern ve kullanıcı dostu arayüz
- Cinsiyet bazlı koltuk yerleşim sistemi
- Detaylı bilet yönetimi
- Dinamik tren ve vagon yönetimi
- Esnek raporlama altyapısı

## 🔍 Geliştirme Notları

- Proje eğitim ve örnek amaçlı geliştirilmiştir
- Gerçek dünya uygulamaları için ek güvenlik katmanları ve performans iyileştirmeleri gereklidir
- Açık kaynak kodlu ve genişletilebilir bir mimari sunulmaktadır

## 📝 Lisans

MIT Lisansı altında yayınlanmıştır. Detaylar için [LICENSE](LICENSE) dosyasını inceleyebilirsiniz.

## 🤝 Katkıda Bulunma

1. Projeyi fork edin
2. Yeni bir özellik branch'i oluşturun
3. Değişikliklerinizi commit edin
4. Branch'inizi push edin
5. Bir Pull Request açın

## 📞 İletişim

Proje Sahibi: Mustafa Arda Düşova
E-Posta: [info@mdusova.com](mailto:info@mdusova.com)

---

**Disclaimer**: Bu proje bir eğitim ve gösterim amaçlı yazılım örneğidir. Gerçek tren bileti sistemlerinin tüm karmaşıklığını ve güvenlik gereksinimlerini tam olarak yansıtmaz.
