// /components/Header.tsx
"use client";

import { Cormorant_Garamond } from "next/font/google";
import Link from "next/link";
import { usePathname } from "next/navigation";

const cormorant = Cormorant_Garamond({
  subsets: ["latin"],
  weight: ["700"], // para hacerlo bien elegante
});


export default function Header() {
  const pathname = usePathname();
  const mostrarHeader = pathname !== "/loging";

  if (!mostrarHeader) return null;

  return (
    <header className="w-full bg-indigo-950 text-white py-6 px-6 shadow-lg">
      <h1 className={`${cormorant.className} text-5xl font-bold mb-4 `}>Hotel Premier</h1>
      <nav className="flex flex-wrap gap-6 text-lg">
        <Link href="/" className="hover:text-gray-500">Inicio</Link>
        <Link href="/reservarHabitacion" className="hover:text-gray-500">Reservar Habitación</Link>
        <Link href="/buscarHuesped" className="hover:text-gray-500">Buscar Huésped</Link>
        <Link href="/estadoHabitacion" className="hover:text-gray-500">Estado de la Habitación</Link>
        <Link href="/facturar" className="hover:text-gray-500">Facturar</Link>
        <Link href="/darAltaHuesped" className="hover:text-gray-500">Dar alta Huésped</Link>
        <Link href="/darBajaHuesped" className="hover:text-gray-500">Dar baja Huésped</Link>
        <Link href="/listarIngresos" className="hover:text-gray-500">Listar Ingresos</Link>
      </nav>
    </header>
  );
}
