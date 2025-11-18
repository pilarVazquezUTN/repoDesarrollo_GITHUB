import { Cormorant_Garamond } from "next/font/google";
import Image from "next/image";
import Link from "next/link";

const cormorant = Cormorant_Garamond({
  subsets: ["latin"],
  weight: ["700"],
});

export default function Menu() {
  return (
    <main className=" bg-gray-200">

        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <h1 className={`${cormorant.className} text-5xl font-serif text-indigo-950 mb-6`}>
                Hola este es el menu
            </h1>
        </div>
        
    </main>
  );
}