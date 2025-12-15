// /components/Header.tsx
"use client";

import { Cormorant_Garamond } from "next/font/google";
import Link from "next/link";
import { usePathname } from "next/navigation";
import Image from "next/image";

const cormorant = Cormorant_Garamond({
  subsets: ["latin"],
  weight: ["700"], // para hacerlo bien elegante
});


export default function Header() {
  const pathname = usePathname();
  const mostrarHeader = pathname !== "/loging";
  const mostrarHeader1 = pathname !== "/paginaPrincipal";

  if (!mostrarHeader || !mostrarHeader1) return null;

  return (
    <header className="w-full bg-indigo-950 text-white py-6 px-6 shadow-lg">
      <Link href="/menu" className="hover:text-gray-500"> 
        <div className="flex items-center gap-4 mb-4">
        <Image 
          src="/logoHotelPremier.png"        // Asegurate de que la imagen se llame así en la carpeta public
          alt="Logo Hotel Premier"
          width={80}             // Un tamaño acorde al texto grande
          height={80}
          className="object-contain" // Para asegurar que no se estire raro
        />
        <h1 className={`${cormorant.className} text-5xl font-bold`}>Hotel Premier</h1>
        </div>
      </Link>
      

      <nav className="flex gap-6 text-lg overflow-x-auto whitespace-nowrap">
        
        <Link
          href="/reservarHabitacion"
          className={` ${
            pathname === "/reservarHabitacion"
              ? "text-gray-500"
              : "hover:text-gray-500"
          }`}
        >
          Reservar Habitación
        </Link>

        <Link  
          href="/buscarHuesped"
          className={` ${
              pathname === "/buscarHuesped"
                ? "text-gray-500"
                : "hover:text-gray-500"
            }`}
        >
            Buscar Huésped
        </Link>

        <Link href="/facturar"
          className={` ${
              pathname === "/facturar"
                ? "text-gray-500"
                : "hover:text-gray-500"
            }`}
        >
          Facturar
        </Link>

        <Link href="/darAltaHuesped"
          className={` ${
                pathname === "/darAltaHuesped"
                  ? "text-gray-500"
                  : "hover:text-gray-500"
            }`}
        >
          Dar alta Huésped
        </Link>

        {/*<Link href="/darBajaHuesped"
          className={` ${
                pathname === "/darBajaHuesped"
                  ? "text-gray-500"
                  : "hover:text-gray-500"
            }`}
        >
          Dar baja Huésped
        </Link> */}

        {/*<Link href="/listarIngresos" 
          className={` ${
                pathname === "/listarIngresos"
                  ? "text-gray-500"
                  : "hover:text-gray-500"
              }`}
        >
          Listar Ingresos
        </Link> */}

        {/*<Link href="/cancelarReserva"
          className={` ${
                pathname === "/cancelarReserva"
                  ? "text-gray-500"
                  : "hover:text-gray-500"
              }`}
        >
          Cancelar Reserva
        </Link> */} 

        <Link href="/ocuparHabitacion"
          className={` ${
                pathname === "/ocuparHabitacion"
                  ? "text-gray-500"
                  : "hover:text-gray-500"
              }`}
        >
          Ocupar Habitacion
        </Link>


      </nav>
    </header>
  );
}
