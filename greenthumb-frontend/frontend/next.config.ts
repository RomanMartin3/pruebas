
/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8080',
        pathname: '/uploads/**', // Ajusta si tus imágenes están en otra ruta
      },
    ],
  },
};

export default nextConfig;