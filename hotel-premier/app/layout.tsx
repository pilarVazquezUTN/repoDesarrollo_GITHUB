import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { Cormorant_Garamond } from "next/font/google";
import Header from "./components/header";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const cormorant = Cormorant_Garamond({
  subsets: ["latin"],
  weight: ["700"], // para hacerlo bien elegante
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});


export const metadata: Metadata = {
  title: "Hotel Premier",
  description: "Hotel management system",
  other: {
    "google": "notranslate",
  },
};



export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="notranslate" translate="no">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased "notranslate" translate="no"`}
      >
      <Header />

        {children}
      </body>
    </html>
  );
}
