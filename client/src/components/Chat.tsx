import { useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

interface Message {
    sender: string;
    content: string;
    timestamp: string;
}

export default function Chat() {
    //const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState('');
    const [connected, setConnected] = useState(false);
    const [postAsAnon, setPostAsAnon] = useState(false);
    const { username, token, role } = useAuth();
    const clientRef = useRef<Client | null>(null);
    const bottomRef = useRef<HTMLDivElement>(null);
    const { t } = useTranslation();

    const [messages, setMessages] = useState<Message[]>(() => {
        const stored = localStorage.getItem('chatMessages');
        return stored ? JSON.parse(stored) : [];
    });

    // save to localStorage whenever messages change
    useEffect(() => {
        localStorage.setItem('chatMessages', JSON.stringify(messages));
    }, [messages]);

    const clearHistory = () => {
        setMessages([]);
        localStorage.removeItem('chatMessages');
    };

    // useEffect(() => {
    //     const client = new Client({
    //         webSocketFactory: () => new SockJS('http://localhost:8080/ws/chat'),
    //         connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
    //         onConnect: () => setConnected(true),
    //         onDisconnect: () => setConnected(false),
    //         onStompError: () => setConnected(false),
    //     });
    //
    //     client.subscribe = (() => {
    //         const original = client.subscribe.bind(client);
    //         return original;
    //     })();
    //
    //     client.onConnect = () => {
    //         setConnected(true);
    //         client.subscribe('/topic/chat', msg => {
    //             const body: Message = JSON.parse(msg.body);
    //             setMessages(prev => [...prev, body]);
    //         });
    //     };
    //
    //     client.activate();
    //     clientRef.current = client;
    //     return () => { client.deactivate(); };
    // }, [token]);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws/chat'),
            connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
            onConnect: () => {
                setConnected(true);
                client.subscribe('/topic/chat', msg => {
                    const body: Message = JSON.parse(msg.body);
                    setMessages(prev => [...prev, body]);
                });
            },
            onDisconnect: () => setConnected(false),
            onStompError: () => setConnected(false),
        });

        client.activate();
        clientRef.current = client;
        return () => { client.deactivate(); };
    }, [token]);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const send = () => {
        //console.log('username:', username, 'postAsAnon:', postAsAnon);
        if (!input.trim() || !clientRef.current?.connected) return;
        clientRef.current.publish({
            destination: '/app/chat.send',
            // body: JSON.stringify({
            //     sender: username || 'Anonymous',
            //     content: input
            // }),
            body: JSON.stringify({
                sender: (!username || postAsAnon) ? 'Anonymous' : username,
                content: input
            }),
        });
        setInput('');
    };

    return (
        <div style={{ maxWidth: '700px', margin: '0 auto', padding: '24px' }}>
            <div style={{
                display: 'flex', alignItems: 'center',
                gap: '12px', marginBottom: '16px'
            }}>
                <h1 style={{color: '#3730a3', margin: 0}}>{t('chat.title')}</h1>
                <span style={{
                    background: connected ? '#d1fae5' : '#fee2e2',
                    color: connected ? '#065f46' : '#b91c1c',
                    padding: '3px 10px', borderRadius: '20px', fontSize: '12px'
                }}>
                    {connected ? 'Connected' : 'Disconnected'}
                </span>
                {role === 'ADMIN' && (
                    <button onClick={clearHistory} style={{
                        background: '#fee2e2', color: '#b91c1c', border: 'none',
                        padding: '3px 10px', borderRadius: '6px', fontSize: '12px',
                        cursor: 'pointer'
                    }}>
                        Clear
                    </button>
                )}
            </div>
            <div style={{
                height: '400px', overflowY: 'auto',
                border: '1.5px solid #c7d2fe', borderRadius: '12px',
                padding: '16px', background: '#fff', marginBottom: '12px'
            }}>
                {messages.length === 0 && (
                    <p style={{ color: '#a5b4fc', textAlign: 'center', marginTop: '160px' }}>
                        No messages yet. Say something!
                    </p>
                )}
                {messages.map((msg, i) => (
                    <div key={i} style={{ marginBottom: '12px' }}>
                        <span style={{ fontWeight: 600, color: '#3730a3' }}>
                            {msg.sender}
                        </span>
                        <span style={{ fontSize: '12px', color: '#888', marginLeft: '8px' }}>
                            {new Date(msg.timestamp).toLocaleTimeString()}
                        </span>
                        <div style={{ marginTop: '4px', color: '#1e1e2e' }}>
                            {msg.content}
                        </div>
                    </div>
                ))}
                <div ref={bottomRef} />
            </div>
            <div style={{ display: 'flex', gap: '8px' }}>
                <input
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    onKeyDown={e => e.key === 'Enter' && send()}
                    placeholder={username ?
                        t('chat.placeholder') :
                        `${t('chat.placeholder')} (as Anonymous)`}
                    style={{
                        flex: 1, padding: '10px 14px',
                        border: '1.5px solid #c7d2fe',
                        borderRadius: '8px', fontSize: '14px'
                    }}
                />
                <button onClick={send} style={{
                    background: '#4338ca', color: '#fff', border: 'none',
                    padding: '10px 20px', borderRadius: '8px', cursor: 'pointer'
                }}>
                    {t('chat.send')}
                </button>
            </div>
            {/*{!username && (*/}
            {/*    <p style={{ fontSize: '13px', color: '#6366f1', marginTop: '8px' }}>*/}
            {/*        You are chatting as <strong>Anonymous</strong>.*/}
            {/*        <a href="/login" style={{ color: '#4338ca', marginLeft: '4px' }}>*/}
            {/*            Log in*/}
            {/*        </a> to show your name.*/}
            {/*    </p>*/}
            {/*)}*/}
            {username && (
                <div style={{ marginTop: '8px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <input type="checkbox" id="anon" checked={postAsAnon}
                           onChange={e => setPostAsAnon(e.target.checked)} />
                    <label htmlFor="anon" style={{ fontSize: '13px', color: '#6366f1', cursor: 'pointer' }}>
                        Post as Anonymous
                    </label>
                </div>
            )}
        </div>
    );
}