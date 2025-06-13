"use client";

import React, { ReactNode } from 'react';
import styles from './Modal.module.css';

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    title: string;
    children: ReactNode;
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, title, children }) => {
    if (!isOpen) {
        return null;
    }

    // Prevenir que el click dentro del contenido cierre el modal
    const handleContentClick = (e: React.MouseEvent) => {
        e.stopPropagation();
    };

    return (
        <div className={styles.modalBackdrop} onClick={onClose}>
            <div className={styles.modalContent} onClick={handleContentClick}>
                <div className={styles.modalHeader}>
                    <h2>{title}</h2>
                    <button onClick={onClose} className={styles.closeButton}>
                        &times;
                    </button>
                </div>
                <div className={styles.modalBody}>
                    {children}
                </div>
            </div>
        </div>
    );
};

export default Modal;