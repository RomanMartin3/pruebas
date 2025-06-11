import React from 'react';
import styles from './DashboardCard.module.css';

interface DashboardCardProps {
    title: string;
    value: string | number;
    icon: React.ReactNode;
}

const DashboardCard: React.FC<DashboardCardProps> = ({ title, value, icon }) => {
    return (
        <div className={styles.card}>
            <div className={styles.iconWrapper}>
                {icon}
            </div>
            <div className={styles.textWrapper}>
                <h3 className={styles.title}>{title}</h3>
                <p className={styles.value}>{value}</p>
            </div>
        </div>
    );
};

export default DashboardCard;