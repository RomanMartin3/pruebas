// greenthumb/FrontEnd/src/components/AdminNavCard.tsx
import Link from 'next/link';
import React from 'react';
import styles from './AdminNavCard.module.css';

interface AdminNavCardProps {
    title: string;
    description: string;
    href: string;
}

const AdminNavCard: React.FC<AdminNavCardProps> = ({ title, description, href }) => {
    return (
        <Link href={href} className={styles.card}>
            <h3 className={styles.title}>{title} &rarr;</h3>
            <p className={styles.description}>{description}</p>
        </Link>
    );
};

export default AdminNavCard;