import { Cormorant_Garamond } from "next/font/google";
import Image from "next/image";
import Link from "next/link";

const cormorant = Cormorant_Garamond({
  subsets: ["latin"],
  weight: ["700"],
});


export default function Home() {
  return (
    <main className=" bg-gray-200">

      <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className={`${cormorant.className} text-5xl font-serif text-indigo-950 mb-6`}>
        Welcome to HOTEL PREMIER
      </h1>
        <nav> 
          <Link href="/loging"> 
            <button className="px-6 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition cursor-pointer">iniciar Sesion</button>
          </Link>
        </nav>

    </div>
    </main>
  );
}