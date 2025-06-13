import React from 'react';
import Link from 'next/link';
import styles from './Footer.module.css';
// Importamos los íconos que acabamos de instalar
import { FaInstagram, FaFacebook, FaTwitter } from 'react-icons/fa';

export const Footer = () => {
    return (
        <footer className={styles.footer}>
            <div className={styles.footerContainer}>
                {/* Sección Sobre Nosotros */}
                <div className={styles.footerSection}>
                    <h3 className={styles.footerTitle}>Sobre Nosotros</h3>
                    <p className={styles.aboutText}>
                        En GreenThumb Market, nuestra pasión es la jardinería. Ofrecemos plantas de la más alta calidad, herramientas y todo lo necesario para que tu pulgar verde prospere.
                    </p>
                </div>

                {/* Sección de Redes Sociales */}
                <div className={styles.footerSection}>
                    <h3 className={styles.footerTitle}>Síguenos</h3>
                    <div className={styles.socialLinks}>
                        <Link href="https://instagram.com" target="_blank" aria-label="Instagram" className={styles.socialLink}>
                            <FaInstagram />
                        </Link>
                        <Link href="https://facebook.com" target="_blank" aria-label="Facebook" className={styles.socialLink}>
                            <FaFacebook />
                        </Link>
                        <Link href="https://twitter.com" target="_blank" aria-label="Twitter" className={styles.socialLink}>
                            <FaTwitter />
                        </Link>
                    </div>
                </div>
            </div>
            <div className={styles.footerBottom}>
                <p>&copy; {new Date().getFullYear()} GreenThumb Market. Todos los derechos reservados.</p>
            </div>
        </footer>
    );
};

export default Footer;