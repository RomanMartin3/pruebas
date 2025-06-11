// src/app/providers.tsx

"use client";

import { Auth0Provider } from "@auth0/auth0-react";
import React from "react";

export function Providers({ children }: { children: React.ReactNode }) {
  const domain = process.env.NEXT_PUBLIC_AUTH0_DOMAIN;
  const clientId = process.env.NEXT_PUBLIC_AUTH0_CLIENT_ID;

  // ¡Esta comprobación es clave para evitar la página en blanco!
  if (!domain || !clientId) {
    return (
      <div style={{ padding: "2rem", margin: "2rem", background: "darkred", color: "white", border: "2px solid white" }}>
        <h1>Error de Configuración de Auth0</h1>
        <p>Las variables de entorno NEXT_PUBLIC_AUTH0_DOMAIN o NEXT_PUBLIC_AUTH0_CLIENT_ID no están definidas.</p>
        <p>Asegúrate de tener un archivo <code>.env.local</code> en la raíz del proyecto y de haber <strong>REINICIADO</strong> el servidor de desarrollo.</p>
      </div>
    );
  }

  return (
    <Auth0Provider
      domain={domain}
      clientId={clientId}
      authorizationParams={{
        redirect_uri: typeof window !== "undefined" ? window.location.origin : undefined
      }}
    >
      {children}
    </Auth0Provider>
  );
}