import styles from './Footer.module.css';

export const Footer = () => {
    return (
        <footer className={styles.footer}>
            <p>&copy; {new Date().getFullYear()} GreenThumb Market. All rights reserved.</p>
        </footer>
    );
};