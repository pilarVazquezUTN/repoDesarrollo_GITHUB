'use client'; // Mantén esto si usas el carrusel o el menú con hooks

import MenuOptionsGrid from './components/menuOptionsGrid'; // Importa tus opciones de menú
import ImageCarousel from './components/imageCarousel';   // Importa el nuevo carrusel

export default function MenuPage() {
  // Define las rutas de tus imágenes (asegúrate que los nombres coincidan exactamente)
  const carouselImages = [
    '/habitacionHotelPremierNosotras.png',
    '/fachadaHotelPremier.png', 
    '/piletaHotelPremierNosotras.png',
  ];

  return (
    <main className="min-h-screen bg-gray-100 p-10 flex flex-col items-center">
      
      {/* Carrusel de Imágenes */}
      <ImageCarousel images={carouselImages} />

      <MenuOptionsGrid /> {/* Aquí se renderizan tus botones de menú */}

    </main>
  );
}