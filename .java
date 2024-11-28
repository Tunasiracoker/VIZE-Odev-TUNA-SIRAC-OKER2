import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Temel Sınıf
abstract class BaseEntity {
    private int id;
    private String name;

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void bilgiGoster();
}

// Müşteri Sınıfı
class Musteri extends BaseEntity {
    public Musteri(int id, String name) {
        super(id, name);
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Müşteri ID: " + getId() + ", Adı: " + getName());
    }
}

// Film Sınıfı
class Film {
    private String ad;
    private int sure;
    private String tur;

    public Film(String ad, int sure, String tur) {
        this.ad = ad;
        this.sure = sure;
        this.tur = tur;
    }

    public String getAd() {
        return ad;
    }

    public int getSure() {
        return sure;
    }

    public String getTur() {
        return tur;
    }

    public void bilgiGoster() {
        System.out.println("Film Adı: " + ad + ", Süresi: " + sure + " dakika, Türü: " + tur);
    }
}

// Salon Sınıfı
class Salon extends BaseEntity {
    private Film film;
    private List<Musteri> musteriler;

    public Salon(int id, String name, Film film) {
        super(id, name);
        this.film = film;
        this.musteriler = new ArrayList<>();
    }

    public Film getFilm() {
        return film;
    }

    public List<Musteri> getMusteriler() {
        return musteriler;
    }

    public void musteriEkle(Musteri musteri) {
        musteriler.add(musteri);
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Salon ID: " + getId() + ", Adı: " + getName());
        if (film != null) {
            System.out.println("Gösterimdeki Film:");
            film.bilgiGoster();
        } else {
            System.out.println("Bu salonda henüz film yok.");
        }
        System.out.println("Kayıtlı Müşteriler:");
        for (Musteri musteri : musteriler) {
            musteri.bilgiGoster();
        }
    }
}

// Sinema Sistemi Sınıfı
class SinemaSistemi {
    private List<Salon> salonlar;

    public SinemaSistemi() {
        this.salonlar = new ArrayList<>();
    }

    public void yeniSalonEkle(Salon salon) {
        salonlar.add(salon);
    }

    public void salonlariListele() {
        System.out.println("=== Salonlar ===");
        for (Salon salon : salonlar) {
            System.out.println("Salon ID: " + salon.getId() + ", Adı: " + salon.getName());
            if (salon.getFilm() != null) {
                System.out.println("   Gösterimdeki Film: " + salon.getFilm().getAd());
            } else {
                System.out.println("   Gösterimdeki Film: Yok");
            }
        }
    }

    public void salonMusterileriniListele(int salonId) {
        for (Salon salon : salonlar) {
            if (salon.getId() == salonId) {
                salon.bilgiGoster();
                return;
            }
        }
        System.out.println("Salon bulunamadı.");
    }

    public List<Salon> getSalonlar() {
        return salonlar;
    }
}

// Main Sınıfı
public class Main {
    public static void main(String[] args) {
        SinemaSistemi sistem = new SinemaSistemi();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenü:");
            System.out.println("1. Salonları Listele");
            System.out.println("2. Yeni Salon Ekle");
            System.out.println("3. Salon Müşterilerini Listele");
            System.out.println("4. Yeni Müşteri Ekle");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {
                case 1: // Salonları Listele
                    sistem.salonlariListele();
                    break;

                case 2: // Yeni Salon Ekle
                    System.out.print("Salon Adı: ");
                    String salonAdi = scanner.nextLine();
                    System.out.print("Film Adı (boş bırakabilirsiniz): ");
                    String filmAdi = scanner.nextLine();

                    Film film = null;
                    if (!filmAdi.isEmpty()) {
                        System.out.print("Film Süresi (dakika): ");
                        int sure = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Film Türü: ");
                        String tur = scanner.nextLine();
                        film = new Film(filmAdi, sure, tur);
                    }

                    Salon yeniSalon = new Salon(sistem.getSalonlar().size() + 1, salonAdi, film);
                    sistem.yeniSalonEkle(yeniSalon);
                    System.out.println("Yeni salon eklendi: " + salonAdi);
                    break;

                case 3: // Salon Müşterilerini Listele
                    System.out.print("Salon ID girin: ");
                    int id = scanner.nextInt();
                    sistem.salonMusterileriniListele(id);
                    break;

                case 4: // Yeni Müşteri Ekle
                    System.out.print("Müşteri Adı: ");
                    String musteriAdi = scanner.nextLine();

                    sistem.salonlariListele();
                    System.out.print("Salon Seçiniz (ID): ");
                    int salonId = scanner.nextInt();
                    scanner.nextLine();

                    Salon seciliSalon = sistem.getSalonlar().stream()
                            .filter(salon -> salon.getId() == salonId)
                            .findFirst()
                            .orElse(null);

                    if (seciliSalon != null) {
                        seciliSalon.musteriEkle(new Musteri(seciliSalon.getMusteriler().size() + 1, musteriAdi));
                        System.out.println("Müşteri başarıyla eklendi.");
                    } else {
                        System.out.println("Geçersiz Salon ID.");
                    }
                    break;

                case 5: // Çıkış
                    System.out.println("Çıkış yapılıyor...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }
}
