import type { Metadata } from "next";
import { Geist_Mono } from "next/font/google";
import "./globals.css";
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
// import { CartProvider } from "@/contexts/CartContext"; // <-- ¡Elimina esta línea!
import { Providers } from "./providers"; // <-- ¡Importa tu componente Providers!

const geistSans = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "GreenThumb Market",
  description: "Your one-stop shop for all things gardening",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={geistSans.variable}>
        {/* <CartProvider> <-- ¡Elimina esta envoltura directa! */}
        <Providers> {/* <-- ¡Envuelve toda la aplicación con tu componente Providers! */}
          <Header />
          <main style={{ minHeight: '80vh', padding: '2rem' }}>
            {children}
          </main>
          <Footer />
        </Providers> {/* <-- Cierra la envoltura de Providers */}
        <script src="https://sdk.mercadopago.com/js/v2"></script>
      </body>
    </html>
  );
}