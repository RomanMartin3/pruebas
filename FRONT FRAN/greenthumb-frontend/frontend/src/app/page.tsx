import styles from "./page.module.css";

export default function Home() {
  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <h1>Welcome to GreenThumb Market</h1>
        <p>Your one-stop shop for all things gardening.</p>
      </main>
    </div>
  );
}