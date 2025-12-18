'use client';

import { useState } from 'react';
import Image from 'next/image';
import { Cormorant_Garamond } from "next/font/google";

const cormorant = Cormorant_Garamond({
  subsets: ["latin"],
  weight: ["700"],
});

interface ImageCarouselProps {
  images: string[]; // Un array de rutas de imágenes (ej: ['/image1.jpg', '/image2.jpg'])
}

export default function ImageCarousel({ images }: ImageCarouselProps) {
  const [currentIndex, setCurrentIndex] = useState(0);

  const goToPrevious = () => {
    const isFirstSlide = currentIndex === 0;
    const newIndex = isFirstSlide ? images.length - 1 : currentIndex - 1;
    setCurrentIndex(newIndex);
  };

  const goToNext = () => {
    const isLastSlide = currentIndex === images.length - 1;
    const newIndex = isLastSlide ? 0 : currentIndex + 1;
    setCurrentIndex(newIndex);
  };

  const scrollToMenu = () => {
    const menuSection = document.getElementById('menu-opciones');
    if (menuSection) {
      menuSection.scrollIntoView({ behavior: 'smooth' });
    }
  };

  if (images.length === 0) {
    return null; // No muestra nada si no hay imágenes
  }

  return (
    <div className="relative w-full h-full overflow-hidden"> {/* Ocupa toda la sección */}
      
      {/* Imagen actual */}
      <Image
        src={images[currentIndex]}
        alt={`Slide ${currentIndex + 1}`}
        fill // Esto hace que la imagen ocupe todo el espacio del div padre
        style={{ objectFit: 'cover' }} // Para que la imagen se adapte sin distorsionarse
        priority // Carga esta imagen con alta prioridad
      />

      {/* Overlay oscuro para mejor contraste */}
      <div className="absolute inset-0 bg-black/40"></div>

      {/* Header con logo y título - Parte superior */}
      <div className="absolute top-0 left-0 right-0 z-20 p-6">
        <div className="flex items-center gap-4">
          <Image 
            src="/logoHotelPremier.png"
            alt="Logo Hotel Premier"
            width={80}
            height={80}
            className="object-contain"
          />
          <h1 className={`${cormorant.className} text-5xl font-bold text-white drop-shadow-lg`}>
            Hotel Premier
          </h1>
        </div>
      </div>

      {/* Contenido central */}
      <div className="absolute inset-0 flex flex-col items-center justify-center z-10">
        <h2 className="text-white text-3xl md:text-4xl font-serif font-bold mb-4 text-center drop-shadow-lg">
          Sistema de Gestión Hotelera
        </h2>
        <p className="text-white/90 text-lg mb-8 text-center drop-shadow-md">
          Seleccione una opción del menú para comenzar
        </p>
        
        {/* Botón para ir al menú */}
        <button
          onClick={scrollToMenu}
          className="flex flex-col items-center gap-2 text-white hover:text-indigo-200 transition-all group"
        >
          <span className="text-sm font-medium">Ver opciones</span>
          <div className="w-12 h-12 rounded-full border-2 border-white group-hover:border-indigo-200 flex items-center justify-center animate-bounce">
            <svg 
              className="w-6 h-6" 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
            </svg>
          </div>
        </button>
      </div>

      {/* Flecha izquierda */}
      <button
        onClick={goToPrevious}
        className="absolute top-1/2 left-4 transform -translate-y-1/2 bg-black bg-opacity-50 text-white p-3 rounded-full 
                   hover:bg-opacity-75 transition-colors z-20"
      >
        &#10094; {/* Carácter de flecha izquierda */}
      </button>

      {/* Flecha derecha */}
      <button
        onClick={goToNext}
        className="absolute top-1/2 right-4 transform -translate-y-1/2 bg-black bg-opacity-50 text-white p-3 rounded-full 
                   hover:bg-opacity-75 transition-colors z-20"
      >
        &#10095; {/* Carácter de flecha derecha */}
      </button>

      {/* Indicadores de slide */}
      <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex gap-2 z-20">
        {images.map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentIndex(index)}
            className={`w-3 h-3 rounded-full transition-all ${
              index === currentIndex ? 'bg-white scale-125' : 'bg-white/50 hover:bg-white/75'
            }`}
          />
        ))}
      </div>
    </div>
  );
}