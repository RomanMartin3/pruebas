import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8080', // Especifica el puerto de tu backend
        pathname: '/uploads/**', // Permite todas las rutas dentro de /uploads/
      },
    ],
  },
};

export default nextConfig;