"use client";

import Link from 'next/link';
import styles from './Header.module.css';
import { useCart } from '@/contexts/CartContext';
import { useAuth } from '@/contexts/AuthContext';

const Header = () => {
  const { cartItemCount } = useCart();
  const { isAuthenticated, isLoading, user, login, logout } = useAuth();

  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <div className={styles.logo}>
          <Link href="/">GreenThumb</Link>
        </div>
        <nav className={styles.nav}>
          <Link href="/products">Productos</Link>
          <Link href="/categories">Categorías</Link>
          
          {/* 1. AÑADIMOS EL ENLACE CONDICIONAL A LA ADMINISTRACIÓN */}
          {isAuthenticated && user?.rol === 'ADMIN' && (
            <Link href="/admin">Administración</Link>
          )}

          {isAuthenticated && (
            <Link href="/cart">Carrito ({cartItemCount})</Link>
          )}
        </nav>
        <div className={styles.authSection}>
          {isLoading ? (
            <span></span> 
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
      </div>
    </header>
  );
};

export default Header;