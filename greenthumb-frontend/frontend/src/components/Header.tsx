// Ruta: src/components/Header.tsx

"use client";

import Link from 'next/link';
import styles from './Header.module.css';
import { useCart } from '@/contexts/CartContext';
import { useAuth } from '@/contexts/AuthContext'; // <-- Importar el hook useAuth

const Header = () => {
  const { itemCount } = useCart();
  const { isAuthenticated, user, logout } = useAuth(); // <-- Usar el hook de autenticación

  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <div className={styles.logo}>
          <Link href="/">GreenThumb</Link>
        </div>
        <nav className={styles.nav}>
          <Link href="/products">Productos</Link>
          <Link href="/categories">Categorías</Link>
          {/* El enlace a "Administración" se mantiene visible para fines de presentación.
              La protección real se haría con Spring Security en el backend. */}
          <Link href="/admin">Administración</Link>
        </nav>
        <div className={styles.actions}> {/* Cambié el nombre de la clase para que sea más genérico */}
          {isAuthenticated ? (
            <>
              <span className={styles.userEmail}>{user?.email}</span> {/* Muestra el email del usuario */}
              <button onClick={logout} className={styles.logoutButton}> {/* Botón de logout */}
                Logout
              </button>
            </>
          ) : (
            <Link href="/login">Login</Link> // <-- Enlace a la página de Login si no está autenticado
          )}
          <Link href="/cart" className={styles.cartLink}>
            Carrito ({itemCount})
          </Link>
        </div>
      </div>
    </header>
  );
};

export default Header;