'use client';

import { useState } from 'react';
import Image from 'next/image';

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

  if (images.length === 0) {
    return null; // No muestra nada si no hay imágenes
  }

  return (
    <div className="relative w-full h-[500px] overflow-hidden rounded-lg shadow-xl mb-10"> {/* Altura fija para el carrusel */}
      
      {/* Imagen actual */}
      <Image
        src={images[currentIndex]}
        alt={`Slide ${currentIndex + 1}`}
        fill // Esto hace que la imagen ocupe todo el espacio del div padre
        style={{ objectFit: 'cover' }} // Para que la imagen se adapte sin distorsionarse
        priority // Carga esta imagen con alta prioridad
      />

      {/* Flecha izquierda */}
      <button
        onClick={goToPrevious}
        className="absolute top-1/2 left-4 transform -translate-y-1/2 bg-black bg-opacity-50 text-white p-3 rounded-full 
                   hover:bg-opacity-75 transition-colors z-10"
      >
        &#10094; {/* Carácter de flecha izquierda */}
      </button>

      {/* Flecha derecha */}
      <button
        onClick={goToNext}
        className="absolute top-1/2 right-4 transform -translate-y-1/2 bg-black bg-opacity-50 text-white p-3 rounded-full 
                   hover:bg-opacity-75 transition-colors z-10"
      >
        &#10095; {/* Carácter de flecha derecha */}
      </button>
    </div>
  );
}