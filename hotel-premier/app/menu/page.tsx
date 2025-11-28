'use client'; // Mantén esto si usas el carrusel o el menú con hooks

import MenuOptionsGrid from './components/menuOptionsGrid';
import ImageCarousel from './components/imageCarousel';

export default function MenuPage() {
  const carouselImages = [
    '/habitacionHotelPremierNosotras.png',
    '/fachadaHotelPremier.png',
    '/piletaHotelPremierNosotras.png',
  ];

  return (
    <main className="min-h-screen bg-gray-100 p-10 flex flex-col items-center">
      
      {/* Carrusel */}
      <ImageCarousel images={carouselImages} />

      {/* ----- SECCIÓN SERVICIOS ----- */}
<section className="mt-20 w-full flex flex-col items-center">

  {/* Título */}
  <h2 className="text-3xl font-serif text-indigo-950 mb-2">Servicios</h2>
  <div className="w-16 h-[2px] bg-yellow-600 mb-10"></div>

  {/* Grid de tarjetas */}
  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">

    {/* Estacionamiento */}
    <div className="bg-white rounded-lg shadow-md px-10 py-8 flex flex-col items-center hover:shadow-lg transition">
      <img src="/imagenEstacionamiento.png" alt="Estacionamiento" className="w-18 h-18 mb-4" />
      <p className="text-indigo-950 font-medium text-lg">Estacionamiento</p>
    </div>

    {/* Sauna */}
        <div className="bg-white rounded-lg shadow-md px-10 py-8 flex flex-col items-center hover:shadow-lg transition">
          <img src="/imagenSauna.png" alt="Sauna" className="w-14 h-14 mb-4" />
          <p className="text-indigo-950 font-medium text-lg">Sauna</p>
        </div>

        {/* Lavado y Planchado */}
        <div className="bg-white rounded-lg shadow-md px-10 py-8 flex flex-col items-center hover:shadow-lg transition">
          <img src="/imagenLavanderia.png" alt="Lavado y Planchado" className="w-14 h-14 mb-4" />
          <p className="text-indigo-950 font-medium text-lg">Lavado y Planchado</p>
        </div>

        {/* Bar */}
        <div className="bg-white rounded-lg shadow-md px-10 py-8 flex flex-col items-center hover:shadow-lg transition">
          <img src="/imagenBbar.png" alt="Bar" className="w-14 h-14 mb-4" />
          <p className="text-indigo-950 font-medium text-lg">Bar</p>
        </div>

      </div>
    </section>


      {/* Botones del menú */}
      <MenuOptionsGrid />

      {/* --- INFORMACIÓN DE CONTACTO --- */}
      <footer className="mt-16 w-full text-center text-indigo-900">
        <div className="py-6 bg-white rounded-lg shadow-md mx-auto max-w-xl">
          <h3 className="text-lg font-semibold mb-2">Contacto</h3>

          <p className="text-sm">
            <b>Teléfono:</b> +54 381 4210000
          </p>
          <p className="text-sm">
            <b>Email:</b> contacto@hotelpremier.com.ar
          </p>
          <p className="text-sm">
            <b>Dirección:</b> San Martín 177, San Miguel de Tucumán, Argentina
          </p>

          <p className="text-xs mt-4 text-gray-600">
            © {new Date().getFullYear()} Hotel Premier — Todos los derechos reservados.
          </p>
        </div>
      </footer>
    </main>
  );
}
