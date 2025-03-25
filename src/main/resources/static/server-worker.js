const CACHE_NAME = "offline-cache-v1";
const urlsToCache = [
    "/",
    "/index.html",
    "/styles.css",
    "/manifest.json",
    "/icons/icon-192x192.png",
    "/icons/icon-512x512.png"
];

// Установка Service Worker
self.addEventListener("install", (event) => {
    console.log("Service Worker: Устанавливается...");
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            console.log("Service Worker: Кешируем файлы");
            return cache.addAll(urlsToCache);
        })
    );
});

// Перехват запросов
self.addEventListener("fetch", (event) => {
    console.log("Service Worker: Перехваченный запрос", event.request.url);
    event.respondWith(
        caches.match(event.request).then((response) => {
            return response || fetch(event.request);
        })
    );
});

// Обновление кеша
self.addEventListener("activate", (event) => {
    console.log("Service Worker: Активирован!");
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames.map((cacheName) => {
                    if (cacheName !== CACHE_NAME) {
                        console.log("Service Worker: Удаляем старый кеш", cacheName);
                        return caches.delete(cacheName);
                    }
                })
            );
        })
    );
});
