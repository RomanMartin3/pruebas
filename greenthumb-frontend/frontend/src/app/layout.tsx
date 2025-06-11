// src/app/layout.tsx

import type { Metadata } from "next";
import { Providers } from "./providers"; // Asegúrate de que la ruta sea correcta
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
          <main>{children}</main>
        </Providers>
      </body>
    </html>
  );
}