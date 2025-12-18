"use client";

import Link from "next/link";

const menuOptions = [
  {
    titulo: "Reservar Habitación",
    descripcion: "Realizar una nueva reserva de habitación",
    href: "/reservarHabitacion",
    icono: "🛏️",
    color: "from-blue-600 to-blue-800"
  },
  {
    titulo: "Ocupar Habitación",
    descripcion: "Check-in de huéspedes",
    href: "/ocuparHabitacion",
    icono: "🔑",
    color: "from-green-600 to-green-800"
  },
  {
    titulo: "Buscar Huésped",
    descripcion: "Buscar y gestionar huéspedes",
    href: "/buscarHuesped",
    icono: "🔍",
    color: "from-purple-600 to-purple-800"
  },
  {
    titulo: "Dar Alta Huésped",
    descripcion: "Registrar un nuevo huésped",
    href: "/darAltaHuesped",
    icono: "👤",
    color: "from-teal-600 to-teal-800"
  },
  {
    titulo: "Cancelar Reserva",
    descripcion: "Cancelar una reserva existente",
    href: "/cancelarReserva",
    icono: "❌",
    color: "from-red-600 to-red-800"
  },
  {
    titulo: "Facturar",
    descripcion: "Generar facturas de estadías",
    href: "/facturar",
    icono: "🧾",
    color: "from-amber-600 to-amber-800"
  },
  {
    titulo: "Ingresar Pago",
    descripcion: "Registrar pagos de huéspedes",
    href: "/ingresarPago",
    icono: "💰",
    color: "from-emerald-600 to-emerald-800"
  },
  {
    titulo: "Ingresar Nota de Crédito",
    descripcion: "Registrar notas de crédito",
    href: "/ingresarNotaCredito",
    icono: "📝",
    color: "from-indigo-600 to-indigo-800"
  },
];

export default function MenuOptionsGrid() {
  return (
    <section id="menu-opciones" className="w-full flex flex-col items-center">
      {/* Título */}
      <h2 className="text-3xl font-serif text-indigo-950 mb-2">Menú Principal</h2>
      <div className="w-16 h-[2px] bg-indigo-950 mb-10"></div>

      {/* Grid de opciones */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 w-full max-w-6xl px-4">
        {menuOptions.map((option, index) => (
          <Link key={index} href={option.href}>
            <div className={`bg-gradient-to-br ${option.color} rounded-xl shadow-lg p-6 flex flex-col items-center justify-center min-h-[180px] hover:shadow-2xl hover:scale-105 transition-all duration-300 cursor-pointer group`}>
              
              {/* Icono */}
              <span className="text-5xl mb-4 group-hover:scale-110 transition-transform">
                {option.icono}
              </span>
              
              {/* Título */}
              <h3 className="text-white font-bold text-lg text-center mb-2">
                {option.titulo}
              </h3>
              
              {/* Descripción */}
              <p className="text-white/80 text-sm text-center">
                {option.descripcion}
              </p>
            </div>
          </Link>
        ))}
      </div>
    </section>
  );
}
