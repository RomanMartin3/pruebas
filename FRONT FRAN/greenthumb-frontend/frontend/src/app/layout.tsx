import type { Metadata } from "next";
import { Providers } from "./providers";
import Header from "@/components/Header"; // <-- 1. Importamos el Header
import { Footer } from "@/components/Footer"; // <-- 2. Importamos el Footer
import "./globals.css";

export const metadata: Metadata = {
  title: "GreenThumb Market",
  description: "Your one-stop shop for all things gardening.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body>
        <Providers>
          <Header /> {/* <-- 3. Añadimos el Header aquí */}
          <main>{children}</main>
          <Footer /> {/* <-- 4. Añadimos el Footer aquí */}
        </Providers>
      </body>
    </html>
  );
}