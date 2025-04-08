// src/main/resources/static/js/script.js
let map, markers = [], infoWindows = [], baslangicMarker, hedefMarker, clickMarker, nearestMarker;
let currentRouteMarkers = [];
let Ascendants = [];
let currentRoutePolylines = [];
let animationInterval;

let route1Polylines = []; // Bus routes
let route2Polylines = []; // Bus+Tram routes
let route3Polylines = []; // Tram routes
let taxiPolylines = [];   // Taxi routes

// Global variable to store route data
window.routeDataResult = null;

// Variables for filtering
let selectedTransportType = null;
let selectedCriteria = null;

function initMap() {
    const baslangicEnlem = parseFloat(window.baslangicEnlem) || 40.7669;
    const baslangicBoylam = parseFloat(window.baslangicBoylam) || 29.9169;
    const hedefEnlem = parseFloat(window.hedefEnlem);
    const hedefBoylam = parseFloat(window.hedefBoylam);
    const durakVerisi = Array.isArray(window.durakVerisi) ? window.durakVerisi : [];
    const nearestStop = window.nearestStop || null;

    console.log("initMap called with:", { baslangicEnlem, baslangicBoylam, hedefEnlem, hedefBoylam, durakVerisi, nearestStop });

    map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: baslangicEnlem, lng: baslangicBoylam },
        zoom: 13,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.TOP_RIGHT
        },
        zoomControl: true,
        zoomControlOptions: { position: google.maps.ControlPosition.RIGHT_TOP },
        streetViewControl: true,
        streetViewControlOptions: { position: google.maps.ControlPosition.RIGHT_BOTTOM },
        fullscreenControl: true,
        fullscreenControlOptions: { position: google.maps.ControlPosition.BOTTOM_RIGHT },
        styles: document.body.classList.contains('dark-mode') ? [
            { elementType: "geometry", stylers: [{ color: "#242f3e" }] },
            { elementType: "labels.text.stroke", stylers: [{ color: "#242f3e" }] },
            { elementType: "labels.text.fill", stylers: [{ color: "#746855" }] },
            { featureType: "administrative.locality", elementType: "labels.text.fill", stylers: [{ color: "#d59563" }] },
            { featureType: "poi", elementType: "labels.text.fill", stylers: [{ color: "#d59563" }] },
            { featureType: "poi.park", elementType: "geometry", stylers: [{ color: "#263c3f" }] },
            { featureType: "poi.park", elementType: "labels.text.fill", stylers: [{ color: "#6b9a76" }] },
            { featureType: "road", elementType: "geometry", stylers: [{ color: "#38414e" }] },
            { featureType: "road", elementType: "geometry.stroke", stylers: [{ color: "#212a37" }] },
            { featureType: "road", elementType: "labels.text.fill", stylers: [{ color: "#9ca5b3" }] },
            { featureType: "road.highway", elementType: "geometry", stylers: [{ color: "#746855" }] },
            { featureType: "road.highway", elementType: "geometry.stroke", stylers: [{ color: "#1f2835" }] },
            { featureType: "road.highway", elementType: "labels.text.fill", stylers: [{ color: "#f3d19c" }] },
            { featureType: "transit", elementType: "geometry", stylers: [{ color: "#2f3948" }] },
            { featureType: "transit.station", elementType: "labels.text.fill", stylers: [{ color: "#d59563" }] },
            { featureType: "water", elementType: "geometry", stylers: [{ color: "#17263c" }] },
            { featureType: "water", elementType: "labels.text.fill", stylers: [{ color: "#515c6d" }] },
            { featureType: "water", elementType: "labels.text.stroke", stylers: [{ color: "#17263c" }] }
        ] : []
    });

    const bounds = new google.maps.LatLngBounds();

    durakVerisi.forEach(durak => {
        const iconUrl = durak.type === 'bus'
            ? 'https://img.icons8.com/color/48/000000/bus.png'
            : 'https://img.icons8.com/color/48/000000/tram.png';
        const marker = new google.maps.Marker({
            position: { lat: parseFloat(durak.lat), lng: parseFloat(durak.lot || durak.lon) },
            map: map,
            title: durak.name,
            icon: { url: iconUrl, scaledSize: new google.maps.Size(34, 34) },
            type: durak.type,
            animation: google.maps.Animation.DROP
        });
        const infoWindow = new google.maps.InfoWindow({
            content: `
                <div style="font-family: 'Poppins', sans-serif; padding: 10px;">
                    <h3 style="margin: 0; color: #4285F4;">${durak.name}</h3>
                    <p style="margin: 5px 0;">Tür: ${durak.type === 'bus' ? 'Otobüs' : 'Tramvay'}</p>
                    <p style="margin: 5px 0;">Konum: ${parseFloat(durak.lat).toFixed(6)}, ${(parseFloat(durak.lot || durak.lon)).toFixed(6)}</p>
                </div>`
        });
        marker.addListener('click', () => {
            infoWindows.forEach(window => window.close());
            infoWindow.open(map, marker);
            document.getElementById('status-bar').textContent = `Durum: ${durak.name} seçildi`;
            map.panTo(marker.getPosition());
        });
        marker.addListener('mouseover', () => infoWindow.open(map, marker));
        marker.addListener('mouseout', () => infoWindow.close());
        Ascendants.push(marker);
        infoWindows.push(infoWindow);
        bounds.extend(marker.getPosition());
    });

    baslangicMarker = new google.maps.Marker({
        position: { lat: baslangicEnlem, lng: baslangicBoylam },
        map: map,
        title: 'Başlangıç Noktası',
        icon: { url: 'https://img.icons8.com/color/48/000000/marker.png', scaledSize: new google.maps.Size(36, 36) },
        animation: google.maps.Animation.DROP
    });
    bounds.extend(baslangicMarker.getPosition());

    if (hedefEnlem && hedefBoylam && !isNaN(hedefEnlem) && !isNaN(hedefBoylam)) {
        hedefMarker = new google.maps.Marker({
            position: { lat: hedefEnlem, lng: hedefBoylam },
            map: map,
            title: 'Hedef Noktası',
            icon: { url: 'https://img.icons8.com/color/48/000000/finish-flag.png', scaledSize: new google.maps.Size(36, 36) },
            animation: google.maps.Animation.DROP
        });
        bounds.extend(hedefMarker.getPosition());
    }

    if (nearestStop && nearestStop.lat && (nearestStop.lot || nearestStop.lon)) {
        nearestMarker = new google.maps.Marker({
            position: { lat: parseFloat(nearestStop.lat), lng: parseFloat(nearestStop.lot || nearestStop.lon) },
            map: map,
            title: nearestStop.name,
            icon: {
                url: nearestStop.type === 'bus' ? 'https://img.icons8.com/color/48/000000/bus.png' : 'https://img.icons8.com/color/48/000000/tram.png',
                scaledSize: new google.maps.Size(42, 42)
            },
            animation: google.maps.Animation.BOUNCE
        });
        const nearestInfoWindow = new google.maps.InfoWindow({
            content: `
                <div style="font-family: 'Poppins', sans-serif; padding: 10px;">
                    <h3 style="margin: 0; color: #4285F4;">${nearestStop.name}</h3>
                    <p style="margin: 5px 0;">Tür: ${nearestStop.type === 'bus' ? 'Otobüs' : 'Tramvay'}</p>
                    <p style="margin: 5px 0;">Konum: ${parseFloat(nearestStop.lat).toFixed(6)}, ${(parseFloat(nearestStop.lot || nearestStop.lon)).toFixed(6)}</p>
                    <p style="margin: 5px 0;">Mesafe: ${nearestStop.distance_text || '0 km'}</p>
                </div>`
        });
        nearestMarker.addListener('click', () => {
            infoWindows.forEach(window => window.close());
            nearestInfoWindow.open(map, nearestMarker);
            document.getElementById('status-bar').textContent = `Durum: En yakın durak ${nearestStop.name} seçildi`;
        });
        infoWindows.push(nearestInfoWindow);
        bounds.extend(nearestMarker.getPosition());
    }

    map.fitBounds(bounds);

    map.addListener('click', (event) => {
        placeClickMarker(event.latLng);
        highlightNearestStop(event.latLng);
        document.getElementById('status-bar').textContent = 'Durum: Yeni konum seçildi';
    });

    setupEventListeners();

    if (window.routeData) {
        initializeRoute(window.routeData);
    }

    if (hedefEnlem && hedefBoylam && nearestStop) {
        drawInitialRoute();
    }

    createParticles();
}

function drawInitialRoute() {
    clearAllRoutes();
    const baslangicEnlem = parseFloat(window.baslangicEnlem) || 40.7669;
    const baslangicBoylam = parseFloat(window.baslangicBoylam) || 29.9169;
    const hedefEnlem = parseFloat(window.hedefEnlem);
    const hedefBoylam = parseFloat(window.hedefBoylam);
    const nearestStop = window.nearestStop || null;

    if (nearestStop && hedefEnlem && hedefBoylam && !isNaN(hedefEnlem) && !isNaN(hedefBoylam)) {
        const transitPath = [
            { lat: baslangicEnlem, lng: baslangicBoylam },
            { lat: parseFloat(nearestStop.lat), lng: parseFloat(nearestStop.lot || nearestStop.lon) },
            { lat: hedefEnlem, lng: hedefBoylam }
        ];
        drawRoute(transitPath, '#4285F4');
    }
}

function placeClickMarker(location) {
    if (clickMarker) clickMarker.setMap(null);
    clickMarker = new google.maps.Marker({
        position: location,
        map: map,
        icon: { url: 'https://img.icons8.com/color/48/000000/map-pin.png', scaledSize: new google.maps.Size(34, 34) },
        animation: google.maps.Animation.DROP
    });
    document.getElementById('selected-coordinates').textContent = `${location.lat().toFixed(6)}, ${location.lng().toFixed(6)}`;
    map.panTo(location);
    clickMarker.addListener('click', () => {
        document.getElementById('status-bar').textContent = 'Durum: Seçilen konum: ' + document.getElementById('selected-coordinates').textContent;
    });
}

function clearPreviousRoutes() {
    currentRouteMarkers.forEach(marker => {
        if (marker && marker.setMap) marker.setMap(null);
    });
    currentRouteMarkers = [];

    currentRoutePolylines.forEach(polyline => {
        if (polyline && polyline.setMap) polyline.setMap(null);
    });
    currentRoutePolylines = [];
}

function clearAllRoutes() {
    const allRoutes = [route1Polylines, route2Polylines, route3Polylines, taxiPolylines, currentRoutePolylines];
    allRoutes.forEach(routeArray => {
        routeArray.forEach(polyline => {
            if (polyline && polyline.setMap) {
                polyline.setMap(null);
            }
        });
        routeArray.length = 0; // Clear the array
    });
    console.log("All routes cleared: Bus:", route1Polylines.length, "Bus+Tram:", route2Polylines.length, "Tram:", route3Polylines.length, "Taxi:", taxiPolylines.length, "Current:", currentRoutePolylines.length);
}

function drawRoute(path, color) {
    const validPath = path.filter(p => p && typeof p.lat === 'number' && !isNaN(p.lat) && typeof p.lng === 'number' && !isNaN(p.lng));
    if (validPath.length < 2) {
        document.getElementById('status-bar').textContent = 'Durum: Geçerli rota çizilemedi';
        return;
    }

    const routeLine = new google.maps.Polyline({
        path: validPath,
        geodesic: true,
        strokeColor: color,
        strokeOpacity: 1.0,
        strokeWeight: 6,
        map: map,
        icons: [{
            icon: { path: google.maps.SymbolPath.CIRCLE, scale: 3, fillColor: '#ffffff', fillOpacity: 1, strokeColor: color },
            offset: '0%',
            repeat: '20px'
        }]
    });

    currentRoutePolylines.push(routeLine);

    const bounds = new google.maps.LatLngBounds();
    validPath.forEach(point => bounds.extend(point));
    map.fitBounds(bounds);

    let totalDistance = 0;
    for (let i = 0; i < validPath.length - 1; i++) {
        totalDistance += google.maps.geometry.spherical.computeDistanceBetween(
            new google.maps.LatLng(validPath[i].lat, validPath[i].lng),
            new google.maps.LatLng(validPath[i + 1].lat, validPath[i + 1].lng)
        );
    }
    const distanceInKm = (totalDistance / 1000).toFixed(2);
    document.getElementById('status-bar').innerHTML = `Durum: Rota çizildi<br>Mesafe: ${distanceInKm} km`;
}

function highlightNearestStop(startPos) {
    if (nearestMarker) {
        nearestMarker.setAnimation(null);
        if (nearestMarker.originalIcon) nearestMarker.setIcon(nearestMarker.originalIcon);
        clearInterval(animationInterval);
    }

    let minDistance = Infinity;
    let nearestStop = null;

    markers.forEach(marker => {
        const markerPos = marker.getPosition();
        const distance = google.maps.geometry.spherical.computeDistanceBetween(
            new google.maps.LatLng(startPos.lat(), startPos.lng()),
            markerPos
        );
        if (distance < minDistance) {
            minDistance = distance;
            nearestStop = marker;
        }
    });

    if (nearestStop) {
        nearestMarker = nearestStop;
        nearestMarker.originalIcon = nearestMarker.getIcon() || {
            url: nearestMarker.type === 'bus' ? 'https://img.icons8.com/color/48/000000/bus.png' : 'https://img.icons8.com/color/48/000000/tram.png',
            scaledSize: new google.maps.Size(34, 34)
        };
        nearestMarker.setIcon({
            url: nearestMarker.originalIcon.url,
            scaledSize: new google.maps.Size(42, 42)
        });
        nearestMarker.setAnimation(google.maps.Animation.BOUNCE);

        animationInterval = setInterval(() => {
            const currentSize = nearestMarker.getIcon().scaledSize.width;
            nearestMarker.setIcon({
                url: nearestMarker.originalIcon.url,
                scaledSize: new google.maps.Size(currentSize === 42 ? 34 : 42, currentSize === 42 ? 34 : 42)
            });
        }, 800);

        document.getElementById('status-bar').textContent = `Durum: En yakın durak: ${nearestStop.title} (${(minDistance / 1000).toFixed(2)} km)`;
    }
}

function filterMarkers(type) {
    markers.forEach(marker => {
        if (type === 'all' || marker.type === type) {
            marker.setVisible(true);
        } else {
            marker.setVisible(false);
        }
    });
    document.getElementById('status-bar').textContent = `Durum: ${type === 'all' ? 'Tüm duraklar gösteriliyor' : 'Sadece ' + (type === 'bus' ? 'otobüs' : 'tramvay') + ' durakları gösteriliyor'}`;
}

function saveRoute() {
    const route = {
        baslangic: baslangicMarker ? { lat: baslangicMarker.getPosition().lat(), lng: baslangicMarker.getPosition().lng() } : null,
        hedef: hedefMarker ? { lat: hedefMarker.getPosition().lat(), lng: hedefMarker.getPosition().lng() } : null,
        waypoints: currentRoutePolylines.length > 0 ? currentRoutePolylines[0].getPath().getArray().map(point => ({ lat: point.lat(), lng: point.lng() })) : []
    };
    const routeJSON = JSON.stringify(route);
    const blob = new Blob([routeJSON], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'route_' + new Date().toISOString().split('T')[0] + '.json';
    a.click();
    URL.revokeObjectURL(url);
    document.getElementById('status-bar').textContent = 'Durum: Rota kaydedildi';
}

document.querySelectorAll('.tab-btn').forEach(button => {
    button.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
        document.querySelectorAll('.tab-pane').forEach(pane => pane.classList.remove('active'));
        button.classList.add('active');
        const tabId = button.getAttribute('data-tab');
        document.getElementById(tabId).classList.add('active');
        document.getElementById('status-bar').textContent = `Durum: ${button.textContent} sekmesi açık`;
    });
});

document.getElementById('homeForm')?.addEventListener('submit', (e) => {
    e.preventDefault();
    const button = e.target.querySelector('button');
    button.textContent = 'Yükleniyor...';
    button.disabled = true;
    document.getElementById('status-bar').textContent = 'Durum: Rota hesaplanıyor...';
});

function setupEventListeners() {
    console.log("Setting up event listeners...");
    document.getElementById('modeToggle').addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        const isDark = document.body.classList.contains('dark-mode');
        document.getElementById('modeToggle').textContent = isDark ? '🌙' : '☀';
        document.getElementById('theme-info').textContent = `Tema: ${isDark ? 'Karanlık Mod' : 'Açık Mod'}`;
        document.getElementById('status-bar').textContent = `Durum: ${isDark ? 'Karanlık' : 'Açık'} mod aktif`;
        map.setOptions({
            styles: isDark ? [
                { elementType: "geometry", stylers: [{ color: "#242f3e" }] },
                { elementType: "labels.text.stroke", stylers: [{ color: "#242f3e" }] },
                { elementType: "labels.text.fill", stylers: [{ color: "#746855" }] },
                { featureType: "administrative.locality", elementType: "labels.text.fill", stylers: [{ color: "#d59563" }] },
                { featureType: "poi", elementType: "labels.text.fill", stylers: [{ color: "#d59563" }] },
                { featureType: "poi.park", elementType: "geometry", stylers: [{ color: "#263c3f" }] },
                { featureType: "poi.park", elementType: "labels.text.fill", stylers: [{ color: "#6b9a76" }] },
                { featureType: "road", elementType: "geometry", stylers: [{ color: "#38414e" }] },
                { featureType: "road", elementType: "geometry.stroke", stylers: [{ color: "#212a37" }] },
                { featureType: "road", elementType: "labels.text.fill", stylers: [{ color: "#9ca5b3" }] },
                { featureType: "road.highway", elementType: "geometry", stylers: [{ color: "#746855" }] },
                { featureType: "road.highway", elementType: "geometry.stroke", stylers: [{ color: "#1f2835" }] },
                { featureType: "road.highway", elementType: "labels.text.fill", stylers: [{ color: "#f3d19c" }] },
                { featureType: "transit", elementType: "geometry", stylers: [{ color: "#2f3948" }] },
                { featureType: "transit.station", elementType: "labels.text.fill", stylers: [{ color: "#d59563" }] },
                { featureType: "water", elementType: "geometry", stylers: [{ color: "#17263c" }] },
                { featureType: "water", elementType: "labels.text.fill", stylers: [{ color: "#515c6d" }] },
                { featureType: "water", elementType: "labels.text.stroke", stylers: [{ color: "#17263c" }] }
            ] : []
        });
    });

    document.getElementById('resetMap').addEventListener('click', () => {
        map.setCenter({ lat: parseFloat(window.baslangicEnlem) || 40.7669, lng: parseFloat(window.baslangicBoylam) || 29.9169 });
        map.setZoom(13);
        document.getElementById('status-bar').textContent = 'Durum: Harita sıfırlandı';
    });

    document.getElementById('getLocation').addEventListener('click', () => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const pos = { lat: position.coords.latitude, lng: position.coords.longitude };
                    placeClickMarker(pos);
                    map.setCenter(pos);
                    map.setZoom(15);
                    highlightNearestStop(pos);
                    document.getElementById('status-bar').textContent = 'Durum: Mevcut konum bulundu';
                },
                (error) => {
                    alert('Konum alınamadı: ' + error.message);
                    document.getElementById('status-bar').textContent = 'Durum: Konum alınamadı';
                }
            );
        } else {
            alert('Tarayıcınız konumu desteklemiyor.');
            document.getElementById('status-bar').textContent = 'Durum: Konum desteklenmiyor';
        }
    });

    document.getElementById('zoomIn').addEventListener('click', () => {
        map.setZoom(map.getZoom() + 1);
        document.getElementById('status-bar').textContent = `Durum: Yakınlaştırma (${map.getZoom()})`;
    });

    document.getElementById('zoomOut').addEventListener('click', () => {
        map.setZoom(map.getZoom() - 1);
        document.getElementById('status-bar').textContent = `Durum: Uzaklaştırma (${map.getZoom()})`;
    });

    document.getElementById('clearMarkers').addEventListener('click', () => {
        if (clickMarker) {
            clickMarker.setMap(null);
            clickMarker = null;
            document.getElementById('selected-coordinates').textContent = 'Henüz seçilmedi';
            if (nearestMarker) {
                nearestMarker.setAnimation(null);
                nearestMarker.setIcon(nearestMarker.originalIcon);
                clearInterval(animationInterval);
            }
            document.getElementById('status-bar').textContent = 'Durum: İşaretler temizlendi';
        }
        clearAllRoutes();
    });

    document.getElementById('set-start').addEventListener('click', () => {
        if (clickMarker) {
            const pos = clickMarker.getPosition();
            document.getElementById('baslangic_enlem').value = pos.lat().toFixed(6);
            document.getElementById('baslangic_boylam').value = pos.lng().toFixed(6);
            baslangicMarker.setPosition(pos);
            animateMarker(baslangicMarker);
            highlightNearestStop(pos);
            document.getElementById('status-bar').textContent = 'Durum: Başlangıç noktası ayarlandı';
        }
    });

    document.getElementById('set-destination').addEventListener('click', () => {
        if (clickMarker) {
            const pos = clickMarker.getPosition();
            document.getElementById('hedef_enlem').value = pos.lat().toFixed(6);
            document.getElementById('hedef_boylam').value = pos.lng().toFixed(6);
            if (!hedefMarker) {
                hedefMarker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    title: 'Hedef Noktası',
                    icon: { url: 'https://img.icons8.com/color/48/000000/finish-flag.png', scaledSize: new google.maps.Size(36, 36) },
                    animation: google.maps.Animation.DROP
                });
            } else {
                hedefMarker.setPosition(pos);
            }
            animateMarker(hedefMarker);
            document.getElementById('status-bar').textContent = 'Durum: Hedef noktası ayarlandı';
        }
    });

    document.getElementById('filter-bus').addEventListener('click', () => filterMarkers('bus'));
    document.getElementById('filter-tram').addEventListener('click', () => filterMarkers('tram'));
    document.getElementById('filter-all').addEventListener('click', () => filterMarkers('all'));
    document.getElementById('saveRoute').addEventListener('click', saveRoute);

    document.getElementById('locationForm').addEventListener('submit', (e) => {
        e.preventDefault();
        const button = e.target.querySelector('button');
        button.textContent = 'Yükleniyor...';
        button.disabled = true;
        document.getElementById('status-bar').textContent = 'Durum: Rota hesaplanıyor...';

        const formData = new FormData(e.target);
        fetch('/', {
            method: 'POST',
            body: formData,
            headers: { 'Accept': 'application/json' }
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Server error: ${response.status} - ${text}`);
                    });
                }
                return response.json();
            })
            .then(sonuc => {
                console.log("Fetch result:", JSON.stringify(sonuc, null, 2));
                button.textContent = 'Konumları Göster';
                button.disabled = false;
                initializeRoute(sonuc);
            })
            .catch(error => {
                console.error('Fetch error:', error);
                button.textContent = 'Konumları Göster';
                button.disabled = false;
                document.getElementById('status-bar').textContent = 'Durum: hata oluştu: ' + error.message;
            });
    });

    document.getElementById('searchButton').addEventListener('click', () => {
        const query = document.getElementById('searchInput').value.trim();
        if (query) {
            searchLocation(query);
        }
    });

    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            const query = document.getElementById('searchInput').value.trim();
            if (query) {
                searchLocation(query);
            }
        }
    });

    document.getElementById('closeInfoPanel').addEventListener('click', () => {
        document.getElementById('infoPanel').style.display = 'none';
        document.getElementById('status-bar').textContent = 'Durum: Bilgi paneli kapatıldı';
    });

    // Transport Type Buttons
    document.querySelectorAll('.transport-btn').forEach(button => {
        button.addEventListener('click', () => {
            console.log(`Transport button clicked with data-type: ${button.getAttribute('data-type')}`);
            selectedTransportType = button.getAttribute('data-type');
            document.querySelectorAll('.transport-btn').forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            selectedCriteria = null;
            document.querySelectorAll('.criteria-btn').forEach(btn => btn.classList.remove('active'));
            clearAllRoutes();
            console.log(`Calling showFilteredRoutes with transportType: ${selectedTransportType}`);
            showFilteredRoutes(selectedTransportType);
        });
    });

    // Criteria Buttons
    document.querySelectorAll('.criteria-btn').forEach(button => {
        button.addEventListener('click', () => {
            if (!selectedTransportType) {
                document.getElementById('status-bar').textContent = 'Durum: Lütfen önce bir ulaşım türü seçin';
                return;
            }
            selectedCriteria = button.getAttribute('data-criteria');
            document.querySelectorAll('.criteria-btn').forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            clearAllRoutes();
            console.log(`Criteria selected: ${selectedCriteria} for ${selectedTransportType}`);
            showFilteredRoutes(selectedTransportType, selectedCriteria);
        });
    });

    // Show All Routes Button
    document.getElementById('show-all-routes').addEventListener('click', () => {
        selectedTransportType = null;
        selectedCriteria = null;
        document.querySelectorAll('.transport-btn').forEach(btn => btn.classList.remove('active'));
        document.querySelectorAll('.criteria-btn').forEach(btn => btn.classList.remove('active'));
        clearAllRoutes();
        toggleRoute("all");
    });

    console.log("Event listeners setup complete.");
}

function initializeRoute(sonuc) {
    console.log("initializeRoute called with:", JSON.stringify(sonuc, null, 2));

    if (!sonuc || typeof sonuc !== 'object') {
        document.getElementById('status-bar').textContent = 'Durum: Geçersiz rota verisi';
        return;
    }

    window.routeDataResult = sonuc;
    clearAllRoutes();

    const routeColors = {
        "bus": "#4285F4",
        "bus_tram": "#000080",
        "tram": "#34C759",
        "taxi": "#fb9403"
    };

    let bounds = new google.maps.LatLngBounds();
    let routeCount = 0;
    let routeInfoHtml = '';

    for (const routeId in sonuc) {
        console.log(`Processing routeId: ${routeId}`);
        if (sonuc[routeId] && Array.isArray(sonuc[routeId].coordinates)) {
            const coordinates = sonuc[routeId].coordinates;
            const routePath = coordinates.map(coord => ({
                lat: parseFloat(coord.lat),
                lng: parseFloat(coord.lon || coord.lot || coord.lng)
            }));

            if (routePath.length >= 2) {
                routeCount++;
                const isBalanceSufficient = sonuc[routeId].bakiye_yeterli;
                const transportType = routeId.split('-')[0];
                const defaultColor = isBalanceSufficient ? (routeColors[transportType] || '#4285F4') : '#808080';
                const startType = sonuc[routeId].start_type || 'unknown';
                const endType = sonuc[routeId].end_type || 'unknown';

                console.log(`Route ${routeId}: startType=${startType}, endType=${endType}, bakiye_yeterli=${isBalanceSufficient}`);

                for (let i = 0; i < routePath.length - 1; i++) {
                    let segmentColor = defaultColor;
                    if (isBalanceSufficient) {
                        if (i === 0) {
                            segmentColor = startType === 'walking' ? '#000000' : (startType === 'taxi' ? '#FFA500' : defaultColor);
                        } else if (i === routePath.length - 2) {
                            segmentColor = endType === 'walking' ? '#000000' : (endType === 'taxi' ? '#FFA500' : defaultColor);
                        }
                    }
                    const segmentPath = [routePath[i], routePath[i + 1]];
                    console.log(`Drawing segment ${i} for route ${routeId} with color ${segmentColor}`);
                    const polyline = drawRouteWithId(segmentPath, segmentColor, transportType);
                    if (polyline) {
                        if (transportType === "bus") route1Polylines.push(polyline);
                        else if (transportType === "bus_tram") route2Polylines.push(polyline);
                        else if (transportType === "tram") route3Polylines.push(polyline);
                        else if (transportType === "taxi") taxiPolylines.push(polyline);
                    }
                }

                if (routeId === "bus-ucret" && routePath.length > 0) {
                    baslangicMarker.setPosition(routePath[0]);
                    if (routePath.length > 1) {
                        if (!hedefMarker) {
                            hedefMarker = new google.maps.Marker({
                                position: routePath[routePath.length - 1],
                                map: map,
                                title: 'Hedef Noktası',
                                icon: { url: 'https://img.icons8.com/color/48/000000/finish-flag.png', scaledSize: new google.maps.Size(36, 36) }
                            });
                        } else {
                            hedefMarker.setPosition(routePath[routePath.length - 1]);
                        }
                    }
                }

                routePath.forEach(point => bounds.extend(point));

                const routeData = sonuc[routeId];
                const distance = routeData.toplam_mesafe_km + " km";
                const duration = routeData.toplam_sure_dk + " dk";
                const balanceStatus = isBalanceSufficient ? `Kalan Bakiye: ${(routeData.kalan_bakiye || 0).toFixed(2)} TL` : "Bakiye Yetersiz";
                const routeName = routeId === "taxi" ? "Taksi Rotası" : `Rota ${routeId}`;
                const cost = typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A";
                const discountedCost = typeof routeData.indirimli_ucret === 'number' ? `${routeData.indirimli_ucret} TL` : (typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A");

                // Daha fazla hata ayıklama
                console.log(`Route ${routeId} - Full Data:`, JSON.stringify(routeData, null, 2));
                console.log(`Route ${routeId} - Cost Values:`, {
                    toplam_ucret: routeData.toplam_ucret,
                    indirimli_ucret: routeData.indirimli_ucret,
                    computed_cost: cost,
                    computed_discountedCost: discountedCost
                });


                const routeHtml = `
                    <h4>${routeName}</h4>
                    <p><span>Mesafe:</span> ${distance}</p>
                    <p><span>Ücret:</span> ${cost}</p>
                    <p><span>Süre:</span> ${duration}</p>
                    <p><span>Durum:</span> ${balanceStatus}</p>
                    <p><span>İndirimli Ücret:</span> ${discountedCost}</p>
                `;

                console.log(`Route ${routeId} - Generated HTML:`, routeHtml);
                routeInfoHtml += routeHtml;
            } else {
                console.warn(`Route ${routeId} has insufficient coordinates: ${routePath.length}`);
            }
        }
    }

    console.log(`Route counts - Bus: ${route1Polylines.length}, Bus+Tram: ${route2Polylines.length}, Tram: ${route3Polylines.length}, Taxi: ${taxiPolylines.length}`);
    if (routeCount > 0) {
        map.fitBounds(bounds);
        document.getElementById('status-bar').textContent = `Durum: ${routeCount} rota çizildi`;

        const infoPanel = document.getElementById('infoPanel');
        const routeInfoContent = document.getElementById('routeInfoContent');
        console.log("Final routeInfoHtml before setting:", routeInfoHtml);
        routeInfoContent.innerHTML = routeInfoHtml;
        console.log("DOM after setting innerHTML:", routeInfoContent.innerHTML);
        infoPanel.style.display = 'block';
    } else {
        document.getElementById('status-bar').textContent = 'Durum: Geçerli rota bulunamadı';
    }
}

function drawRouteWithId(path, color, routeId) {
    const validPath = path.filter(p => p && typeof p.lat === 'number' && !isNaN(p.lat) && typeof p.lng === 'number' && !isNaN(p.lng));
    if (validPath.length < 2) {
        console.error(`Invalid path for route ${routeId}:`, validPath);
        return null;
    }

    const routeLine = new google.maps.Polyline({
        path: validPath,
        geodesic: true,
        strokeColor: color,
        strokeOpacity: 1.0,
        strokeWeight: 6,
        map: map,
        routeId: routeId,
        icons: [{
            icon: { path: google.maps.SymbolPath.CIRCLE, scale: 3, fillColor: '#ffffff', fillOpacity: 1, strokeColor: color },
            offset: '0%',
            repeat: '20px'
        }]
    });

    if (routeId === "bus") route1Polylines.push(routeLine);
    else if (routeId === "bus_tram") route2Polylines.push(routeLine);
    else if (routeId === "tram") route3Polylines.push(routeLine);
    else if (routeId === "taxi") taxiPolylines.push(routeLine);
    else currentRoutePolylines.push(routeLine);

    return routeLine;
}

function toggleRoute(routeId) {
    const routeColors = {
        "bus": "#4285F4",
        "bus_tram": "#000080",
        "tram": "#34C759",
        "taxi": "#fb9403"
    };

    console.log(`Toggling route: ${routeId}`);

    clearAllRoutes();

    let statusMessage = "";
    let bounds = new google.maps.LatLngBounds();

    if (routeId === "all") {
        for (const id in window.routeDataResult) {
            if (window.routeDataResult[id] && Array.isArray(window.routeDataResult[id].coordinates)) {
                const coordinates = window.routeDataResult[id].coordinates.map(coord => ({
                    lat: parseFloat(coord.lat),
                    lng: parseFloat(coord.lon || coord.lot || coord.lng)
                }));
                const isBalanceSufficient = window.routeDataResult[id].bakiye_yeterli;
                const transportType = id.split('-')[0];
                const defaultColor = isBalanceSufficient ? routeColors[transportType] : '#808080';
                const startType = window.routeDataResult[id].start_type || 'unknown';
                const endType = window.routeDataResult[id].end_type || 'unknown';

                for (let i = 0; i < coordinates.length - 1; i++) {
                    let segmentColor = defaultColor;
                    if (isBalanceSufficient) {
                        if (i === 0) {
                            segmentColor = startType === 'walking' ? '#000000' : (startType === 'taxi' ? '#FFA500' : defaultColor);
                        } else if (i === coordinates.length - 2) {
                            segmentColor = endType === 'walking' ? '#000000' : (endType === 'taxi' ? '#FFA500' : defaultColor);
                        }
                    }
                    const segmentPath = [coordinates[i], coordinates[i + 1]];
                    const polyline = drawRouteWithId(segmentPath, segmentColor, transportType);
                    if (polyline) {
                        if (transportType === "bus") route1Polylines.push(polyline);
                        else if (transportType === "bus_tram") route2Polylines.push(polyline);
                        else if (transportType === "tram") route3Polylines.push(polyline);
                        else if (transportType === "taxi") taxiPolylines.push(polyline);
                    }
                }

                coordinates.forEach(point => bounds.extend(point));

                const routeData = window.routeDataResult[id];
                const distance = routeData.toplam_mesafe_km + " km";
                const duration = routeData.toplam_sure_dk + " dk";
                const balanceStatus = isBalanceSufficient ? `Kalan Bakiye: ${(routeData.kalan_bakiye || 0).toFixed(2)} TL` : "Bakiye Yetersiz";
                const cost = typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A";
                const discountedCost = typeof routeData.indirimli_ucret === 'number' ? `${routeData.indirimli_ucret} TL` : (typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A");
                statusMessage += `${id}: Mesafe: ${distance}, Ücret: ${cost}, İndirimli Ücret: ${discountedCost}, Süre: ${duration}, ${balanceStatus}\n`;
            }
        }
        statusMessage = `Durum: Tüm rotalar gösteriliyor\n${statusMessage}`;
    } else {
        const routeData = window.routeDataResult[routeId];
        if (routeData && Array.isArray(routeData.coordinates)) {
            const coordinates = routeData.coordinates.map(coord => ({
                lat: parseFloat(coord.lat),
                lng: parseFloat(coord.lon || coord.lot || coord.lng)
            }));
            const isBalanceSufficient = routeData.bakiye_yeterli;
            const transportType = routeId.split('-')[0];
            const defaultColor = isBalanceSufficient ? routeColors[transportType] : '#808080';
            const startType = routeData.start_type || 'unknown';
            const endType = routeData.end_type || 'unknown';

            for (let i = 0; i < coordinates.length - 1; i++) {
                let segmentColor = defaultColor;
                if (isBalanceSufficient) {
                    if (i === 0) {
                        segmentColor = startType === 'walking' ? '#000000' : (startType === 'taxi' ? '#FFA500' : defaultColor);
                    } else if (i === coordinates.length - 2) {
                        segmentColor = endType === 'walking' ? '#000000' : (endType === 'taxi' ? '#FFA500' : defaultColor);
                    }
                }
                const segmentPath = [coordinates[i], coordinates[i + 1]];
                const polyline = drawRouteWithId(segmentPath, segmentColor, transportType);
                if (polyline) {
                    if (transportType === "bus") route1Polylines.push(polyline);
                    else if (transportType === "bus_tram") route2Polylines.push(polyline);
                    else if (transportType === "tram") route3Polylines.push(polyline);
                    else if (transportType === "taxi") taxiPolylines.push(polyline);
                }
            }

            coordinates.forEach(point => bounds.extend(point));

            const distance = routeData.toplam_mesafe_km + " km";
            const duration = routeData.toplam_sure_dk + " dk";
            const balanceStatus = isBalanceSufficient ? `Kalan Bakiye: ${(routeData.kalan_bakiye || 0).toFixed(2)} TL` : "Bakiye Yetersiz";
            const cost = typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A";
            const discountedCost = typeof routeData.indirimli_ucret === 'number' ? `${routeData.indirimli_ucret} TL` : (typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A");
            statusMessage = `Durum: ${routeId === "taxi" ? "Taksi" : transportType + " (" + (routeId.split('-')[1] || 'Genel') + ")"}: Mesafe: ${distance}, Ücret: ${cost}, İndirimli Ücret: ${discountedCost}, Süre: ${duration}, ${balanceStatus}`;
        } else {
            statusMessage = `Durum: ${routeId === "taxi" ? "Taksi" : routeId} gösteriliyor (Detaylar mevcut değil)`;
        }
    }

    if (routeId === "all" || window.routeDataResult[routeId]) {
        map.fitBounds(bounds);
    }

    document.getElementById('status-bar').textContent = statusMessage;
}

function showFilteredRoutes(transportType, criteria = null) {
    console.log(`showFilteredRoutes called with transportType: ${transportType}, criteria: ${criteria}`);

    const routeColors = {
        "bus": "#4285F4",
        "bus_tram": "#000080",
        "tram": "#34C759",
        "taxi": "#fb9403"
    };

    clearAllRoutes();

    if (!window.routeDataResult) {
        console.error("routeDataResult is not defined or empty");
        document.getElementById('status-bar').textContent = 'Durum: Rota verisi bulunamadı';
        return;
    }

    console.log("Current routeDataResult:", JSON.stringify(window.routeDataResult, null, 2));

    let statusMessage = "";
    let bounds = new google.maps.LatLngBounds();
    let routeCount = 0;

    const routeKeys = {
        "bus": {
            "all": ["bus-ucret", "bus-mesafe", "bus-sure"],
            "ucret": ["bus-ucret"],
            "mesafe": ["bus-mesafe"],
            "zaman": ["bus-sure"]
        },
        "tram": {
            "all": ["tram-ucret", "tram-mesafe", "tram-sure"],
            "ucret": ["tram-ucret"],
            "mesafe": ["tram-mesafe"],
            "zaman": ["tram-sure"]
        },
        "bus_tram": {
            "all": ["bus_tram-ucret", "bus_tram-mesafe", "bus_tram-sure"],
            "ucret": ["bus_tram-ucret"],
            "mesafe": ["bus_tram-mesafe"],
            "zaman": ["bus_tram-sure"]
        },
        "taxi": {
            "all": ["taxi"],
            "ucret": ["taxi"],
            "mesafe": ["taxi"],
            "zaman": ["taxi"]
        }
    };

    const routesToShow = routeKeys[transportType][criteria || "all"];
    console.log(`Routes to show for ${transportType}:`, routesToShow);

    routesToShow.forEach(routeKey => {
        const routeData = window.routeDataResult[routeKey];
        if (routeData && routeData.coordinates) {
            routeCount++;
            const isBalanceSufficient = routeData.bakiye_yeterli !== false;
            const defaultColor = isBalanceSufficient ? routeColors[transportType] : '#808080';
            const startType = routeData.start_type || 'unknown';
            const endType = routeData.end_type || 'unknown';

            console.log(`Drawing route: ${routeKey}, Balance: ${isBalanceSufficient}, Start: ${startType}, End: ${endType}`);

            const coordinates = routeData.coordinates.map(coord => ({
                lat: parseFloat(coord.lat),
                lng: parseFloat(coord.lon || coord.lot || coord.lng)
            }));

            if (!coordinates || coordinates.length < 2) {
                console.error(`Invalid coordinates for ${routeKey}:`, coordinates);
                return;
            }

            if (isBalanceSufficient && (startType === 'walking' || endType === 'walking' || startType === 'taxi' || endType === 'taxi')) {
                for (let i = 0; i < coordinates.length - 1; i++) {
                    let segmentColor = defaultColor;
                    if (i === 0 && startType === 'walking') segmentColor = '#000000';
                    else if (i === 0 && startType === 'taxi') segmentColor = '#FFA500';
                    else if (i === coordinates.length - 2 && endType === 'walking') segmentColor = '#000000';
                    else if (i === coordinates.length - 2 && endType === 'taxi') segmentColor = '#FFA500';
                    else segmentColor = defaultColor;

                    const segmentPath = [coordinates[i], coordinates[i + 1]];
                    const polyline = drawRouteWithId(segmentPath, segmentColor, transportType);
                    if (!polyline) {
                        console.error(`Failed to draw polyline for segment ${i} of ${routeKey}`);
                    }
                }
            } else {
                const polyline = drawRouteWithId(coordinates, defaultColor, transportType);
                if (!polyline) {
                    console.error(`Failed to draw polyline for ${routeKey}`);
                }
            }

            coordinates.forEach(point => bounds.extend(point));

            const distance = routeData.toplam_mesafe_km + " km";
            const duration = routeData.toplam_sure_dk + " dk";
            const balanceStatus = isBalanceSufficient ? `Kalan Bakiye: ${(routeData.kalan_bakiye || 0).toFixed(2)} TL` : "Bakiye Yetersiz";
            const cost = typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A";
            const discountedCost = typeof routeData.indirimli_ucret === 'number' ? `${routeData.indirimli_ucret} TL` : (typeof routeData.toplam_ucret === 'number' ? `${routeData.toplam_ucret} TL` : "N/A");
            statusMessage += `${routeKey}: Mesafe: ${distance}, Ücret: ${cost}, İndirimli Ücret: ${discountedCost}, Süre: ${duration}, ${balanceStatus}\n`;
        } else {
            console.log(`No data found for route: ${routeKey}`);
        }
    });

    console.log("After drawing - Bus:", route1Polylines.length, "Bus+Tram:", route2Polylines.length, "Tram:", route3Polylines.length, "Taxi:", taxiPolylines.length);

    if (routeCount > 0) {
        map.fitBounds(bounds);
        document.getElementById('status-bar').textContent = `Durum: ${criteria ? `${transportType} (${criteria})` : `${transportType}`} rotaları gösteriliyor\n${statusMessage}`;
    } else {
        document.getElementById('status-bar').textContent = `Durum: ${criteria ? `${transportType} (${criteria})` : `${transportType}`} için rota bulunamadı`;
    }
}

function searchLocation(query) {
    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ address: query }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK && results[0]) {
            const location = results[0].geometry.location;
            map.setCenter(location);
            map.setZoom(14);
            placeClickMarker(location);
            document.getElementById('status-bar').textContent = `Durum: "${results[0].formatted_address}" bulundu`;
        } else {
            document.getElementById('status-bar').textContent = `Durum: "${query}" bulunamadı`;
            alert(`Lokasyon bulunamadı: ${query}`);
        }
    });
}

function animateMarker(marker) {
    marker.setAnimation(google.maps.Animation.BOUNCE);
    setTimeout(() => marker.setAnimation(null), 2000);
}

function createParticles() {
    const particleCount = 20;
    for (let i = 0; i < particleCount; i++) {
        const particle = new google.maps.Marker({
            position: map.getCenter(),
            map: map,
            icon: {
                url: 'https://img.icons8.com/color/48/000000/star.png',
                scaledSize: new google.maps.Size(10, 10)
            },
            animation: google.maps.Animation.DROP
        });
        setTimeout(() => {
            particle.setMap(null);
        }, 2000);
    }
}