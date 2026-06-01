import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import api from '../api/axios';
import ErrorAlert from '../components/ErrorAlert';

export default function Register() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await api.post('/auth/register', { username, email, password });
            navigate('/login');
        } catch (err: any) {
            setError(err.message);
        }
    };

    return (
        <div style={{ maxWidth: '420px', margin: '60px auto', padding: '0 24px' }}>
            <div style={{
                background: '#fff', borderRadius: '16px',
                padding: '36px 40px', boxShadow: '0 4px 16px rgba(99,102,241,0.12)'
            }}>
                <h1 style={{ color: '#3730a3', marginBottom: '24px', textAlign: 'center' }}>
                    {t('auth.register')}
                </h1>
                <ErrorAlert message={error} onClose={() => setError(null)} />
                <form onSubmit={handleSubmit}>
                    <input placeholder={t('auth.username')} value={username}
                           onChange={e => setUsername(e.target.value)}
                           style={{ width: '100%', padding: '9px 14px', marginBottom: '12px',
                               border: '1.5px solid #c7d2fe', borderRadius: '8px',
                               boxSizing: 'border-box' as const }} />
                    <input type="email" placeholder={t('auth.email')} value={email}
                           onChange={e => setEmail(e.target.value)}
                           style={{ width: '100%', padding: '9px 14px', marginBottom: '12px',
                               border: '1.5px solid #c7d2fe', borderRadius: '8px',
                               boxSizing: 'border-box' as const }} />
                    <input type="password" placeholder={t('auth.password')} value={password}
                           onChange={e => setPassword(e.target.value)}
                           style={{ width: '100%', padding: '9px 14px', marginBottom: '16px',
                               border: '1.5px solid #c7d2fe', borderRadius: '8px',
                               boxSizing: 'border-box' as const }} />
                    <button type="submit" style={{
                        width: '100%', background: '#4338ca', color: '#fff',
                        border: 'none', padding: '10px', borderRadius: '8px',
                        cursor: 'pointer', fontSize: '15px'
                    }}>
                        {t('auth.register')}
                    </button>
                </form>
                <p style={{ textAlign: 'center', marginTop: '16px', fontSize: '14px' }}>
                    {t('auth.haveAccount')}{' '}
                    <Link to="/login" style={{ color: '#4338ca' }}>{t('auth.login')}</Link>
                </p>
            </div>
        </div>
    );
}