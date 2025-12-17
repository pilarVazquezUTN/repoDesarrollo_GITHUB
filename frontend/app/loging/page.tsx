//import Link from "next/dist/client/link";
import Link from "next/link";

export default function LogingPage() {
  return (
    <main className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-4xl font-bold text-indigo-950 mb-4">Iniciar Sesión</h1>
      <form className="flex flex-col  w-80">

        <label className="text-indigo-950 font-medium mb-0">nombre de usuario:</label>
        <input type="text" placeholder="Usuario" className="p-2 border rounded mt-0 text-black" />
        <label className="text-indigo-950 font-medium mt-4">contraseña:</label>
        <input type="password" placeholder="Contraseña" className="p-2 border rounded text-black" />

        <nav> 
          <Link href="/menu"> 
            <button className="mt-10 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition cursor-pointer">Entrar</button>
          </Link>
        </nav>

        <nav>
          <Link href="/contrasenia" className="text-sm text-indigo-600 hover:underline mb-2">¿Olvidaste tu contraseña?</Link>
        </nav>

      </form>
    </main>
  );
}
