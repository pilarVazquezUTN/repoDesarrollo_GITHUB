'use client'; // Mantén esto si usas el carrusel o el menú con hooks

import MenuOptionsGrid from './components/menuOptionsGrid';
import ImageCarousel from './components/imageCarousel';

export default function MenuPage() {
  const carouselImages = [
    '/fachadaHotelPremier.png',
    '/habitacionHotelPremierNosotras.png',
    '/piletaHotelPremierNosotras.png',
  ];

  return (
    <main className="bg-gray-100">
      
      {/* Sección 1: Carrusel - Pantalla completa */}
      <section className="h-screen">
        <ImageCarousel images={carouselImages} />
      </section>

      {/* Sección 2: Menú de opciones - Pantalla completa centrada */}
      <section className="min-h-screen flex items-center justify-center py-16">
        <MenuOptionsGrid />
      </section>
    </main>
  );
}
