
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
   - 100 metre yakınlık kontrolü yapılır

2. Mağaza Ziyaretleri:
   - Ziyaretler StoreVisit olarak veritabanına kaydedilir
   - 1 dakikadan kısa sürede tekrar giriş yapılırsa log basılır

3. Toplam Mesafe Hesaplama:
   - /api/couriers/{courierId}/distance → km cinsinden toplam mesafe

4. Ziyaret Listesi:
   - /api/couriers/{courierId}/visits → tüm mağaza ziyaret geçmişi

5. Mağaza Verisi:
   - stores.json içinden okunarak veritabanına yüklenir (@PostConstruct ile)

Swagger UI
----------
http://localhost:8080/swagger-ui/index.html

H2 Console
----------
URL: http://localhost:8080/h2-console
JDBC: jdbc:h2:mem:testdb
Username: sa
Password: (boş)

Örnek JSON (Konum Gönderimi)
-----------------------------
{
  "courierId": "1",
  "lat": 40.9923307,
  "lng": 29.1244229,
  "timestamp": "2025-05-15T10:00:00"
}

Unit Testler
-------------
- receiveLocation_shouldSaveLocation
- getTotalTravelDistance_shouldReturnCorrectValue
- firstVisit_shouldCreateVisit
- reentryWithin1Min_shouldNotCreateVisit
- contextLoads

