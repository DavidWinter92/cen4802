'use client';

import {useEffect, useState} from 'react';

const version = "v1.0.0"; // Deployment version

export default function HomePage() {
    const [messages, setMessages] = useState<string[]>([]);
    const [messageIndex, setMessageIndex] = useState(0);
    const [fade, setFade] = useState(true);

    // Use environment variable for backend URL
    const backendUrl = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8081';

    // Fetch messages from backend on mount
    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const res = await fetch(`${backendUrl}/messages`);
                const data = await res.json();
                setMessages(data);
            } catch (err) {
                console.error('Failed to fetch messages:', err);
                setMessages(["Failed to load messages."]);
            }
        };
        fetchMessages();
    }, [backendUrl]);

    // Cycle messages every 5 seconds
    useEffect(() => {
        if (messages.length === 0) return;
        const interval = setInterval(() => {
            setFade(false); // start fade out
            setTimeout(() => {
                setMessageIndex(Math.floor(Math.random() * messages.length));
                setFade(true); // fade in new message
            }, 500);
        }, 5000);
        return () => clearInterval(interval);
    }, [messages]);

    return (
        <div className="flex items-center justify-center p-8">
            <div
                className="relative bg-gradient-to-br dark:from-black dark:to-gray-950 shadow-2xl rounded-2xl p-8 max-w-lg w-full text-center transition-colors duration-500">

                <p className={`text-2xl md:text-3xl font-semibold text-gray-900 dark:text-white transition-opacity duration-500 transform ${
                    fade ? 'opacity-100 scale-100' : 'opacity-0 scale-95'
                }`}>
                    {messages[messageIndex]}
                </p>

                <div className="absolute right-4 bottom-1 text-sm text-gray-500 dark:text-gray-400 font-mono">
                    {version}
                </div>
            </div>
        </div>
    );
}
