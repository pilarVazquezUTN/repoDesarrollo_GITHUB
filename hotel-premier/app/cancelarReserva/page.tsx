//import Link from "next/dist/client/link";
import Link from "next/link";

export default function cancelarReserva() {
  return (
    <main className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-4xl font-bold text-indigo-950 mb-4">Cancelar Reserva</h1>
      <form className="flex flex-col  w-80">

        <label className="text-indigo-950 font-medium mb-0">Apellido:</label>
        <input type="text" placeholder="Apellido" className="p-2 border rounded mt-0 text-black" />
        <label className="text-indigo-950 font-medium mt-4">Nombres:</label>
        <input type="text" placeholder="Nombres" className="p-2 border rounded text-black" />

        <nav> 
          <Link href="/mostrarGrilla"> 
            <button className="mt-10 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition cursor-pointer">Aceptar</button>
          </Link>
        </nav>

      </form>
    </main>
  );
}

