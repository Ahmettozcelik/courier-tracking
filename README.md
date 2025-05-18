
Courier Tracking API - README
=============================

Proje Tanımı
------------
Bu proje, Migros için geliştirilen bir Kurye Takip API'sidir.
Kurye konumları kaydedilir, mağaza yakınlığına göre ziyaretler oluşturulur ve toplam seyahat mesafesi hesaplanır.

Kullanılan Teknolojiler
-----------------------
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Swagger (OpenAPI)
- JUnit 5
- SLF4J
- Jackson

Özellikler
----------
1. Kurye Konum Takibi:
    - /api/couriers/location → kurye konumu gönderilir
    - Konum veritabanına kaydedilir
    - Tüm hareketler toplam mesafeye yansıtılır

2. Mağaza Ziyaretleri:
    - Kurye bir mağazaya 100 metre içinde yaklaşırsa StoreVisit kaydı yapılır
    - Aynı mağazaya 1 dakika içinde tekrar girerse kayıt oluşmaz, sadece loglanır
    - 100 metreden uzaksa ziyaret kaydı yapılmaz ama mesafe artar (yeni özellik!)
    - Ziyaretler `GET /api/couriers/{courierId}/visits` ile görüntülenebilir

3. Observer Sistemi:
    - Observer Pattern ile genişletilebilir yapı
    - BadgeObserver: Kurye 3 farklı mağaza ziyaret ettiğinde rozet loglanır

4. Toplam Mesafe Hesaplama:
    - /api/couriers/{courierId}/distance → km cinsinden toplam mesafe

5. Mağaza Verisi:
    - stores.json içinden okunarak veritabanına yüklenir (@PostConstruct ile)

6. Zaman ve Veri Güvenliği:
    - Kurye daha eski zamanlı bir kayıt gönderirse sistem işlemi engeller
    - Geriye dönük veri kaydı yapılmaz, loglanır

Swagger UI
----------
URL: http://localhost:8080/swagger-ui/index.html

H2 Console
----------
URL: http://localhost:8080/h2-console

JDBC: jdbc:h2:mem:testdb
Username: sa
Password:

Örnek JSON (Konum Gönderimi)
----------------------------
{
"courierId": "1",
"lat": 40.9923307,
"lng": 29.1244229,
"timestamp": "2025-05-15T10:00:00"
}

{
"courierId": "1",
"lat": 40.986106,
"lng": 29.1161293,
"timestamp": "2025-05-15T10:05:00"
}

{
"courierId": "1",
"lat": 41.0066851,
"lng": 28.6552262,
"timestamp": "2025-05-15T10:10:00"
}


Unit Testler
-------------

---------------------------------------------------------
1. Kurye İlk Ziyaretini Yaptığında StoreVisit Kaydedilir
- Test: `testCourierFirstVisitShouldBeSaved`
- Açıklama: Kurye mağazaya ilk kez 100 metre içinde gelirse, StoreVisit kaydı oluşturulur.
- Doğrulama: storeVisitRepository.save(...) 1 kez çağrılır.

---------------------------------------------------------
2. Aynı Mağazaya 1 Dakika İçinde Tekrar Girişte Kayıt Yapılmaz

- Test: `testCourierSecondVisitWithinOneMinuteShouldNotBeSaved`
- Açıklama: Aynı mağazaya 1 dakika içinde tekrar giriş yapılırsa yeni kayıt yapılmaz.
- Doğrulama: storeVisitRepository.save(...) sadece ilk girişte çağrılır.

---------------------------------------------------------
3. Servis Bean'i Yüklenebiliyor mu?
- Test: `contextLoads`
- Açıklama: Spring context açıldığında CourierTrackingService null değilse test geçer.
- Doğrulama: `assertDoesNotThrow()` ile kontrol edilir.

---------------------------------------------------------
4. Konum Gönderme Hatasız Çalışıyor mu?
- Test: `receiveLocation_doesNotFail`
- Açıklama: Basit bir CourierLocation gönderildiğinde receiveLocation metodu hata fırlatmamalı.
- Doğrulama: `assertDoesNotThrow()` ile metodun sorunsuz çalıştığı doğrulanır.

