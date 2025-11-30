import './globals.css';
import { ReactNode } from 'react';

export const metadata = {
    title: 'Motivational Messages',
    description: 'Get a random motivational message',
};

export default function RootLayout({ children }: { children: ReactNode }) {
    return (
        <html lang="en">
        <body className="flex flex-col min-h-screen">
        <header className="bg-black text-white p-4 text-center font-bold text-xl">
            Motivational Messages
        </header>
        <main className="flex-grow flex items-center justify-center p-6">
            {children}
        </main>
        <footer className="bg-black text-gray-700 p-4 text-center">
            4802C - David Winter - Final Project - 11/24/2025
        </footer>
        </body>
        </html>
    );
}
