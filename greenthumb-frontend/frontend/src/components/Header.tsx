"use client"; // Necesario para usar hooks como useCart

import Link from 'next/link';
import styles from './Header.module.css';
import { useCart } from '@/contexts/CartContext'; // Importar el hook
import { useAuth } from '@/contexts/AuthContext';

const Header = () => {
  const { cartItemCount } = useCart();
  const { isAuthenticated, isLoading, user, login, logout } = useAuth(); // 2. Usamos el hook

  return (
    <header className={styles.header}>
      <div className={styles.logo}>
        <Link href="/">GreenThumb</Link>
      </div>
      <nav className={styles.nav}>
        <Link href="/products">Productos</Link>
        <Link href="/categories">Categorías</Link>
        {/* Mostramos el carrito solo si el usuario está autenticado */}
        {isAuthenticated && (
          <Link href="/cart">Carrito ({cartItemCount})</Link>
        )}
      </nav>
      <div className={styles.authSection}>
        {/* 3. Lógica para mostrar botones y estado del usuario */}
        {isLoading ? (
          <span>Cargando...</span>
        ) : isAuthenticated && user ? (
          <>
            <span className={styles.welcomeMessage}>Hola, {user.nombre}</span>
            <button onClick={() => logout()} className={styles.authButton}>
              Cerrar Sesión
            </button>
          </>
        ) : (
          <button onClick={() => login()} className={styles.authButton}>
            Iniciar Sesión
          </button>
        )}
      </div>
    </header>
  );
};

export default Header;