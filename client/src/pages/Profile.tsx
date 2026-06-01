import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';
import ErrorAlert from '../components/ErrorAlert';

export default function Profile() {
    const [visited, setVisited] = useState<any[]>([]);
    const [error, setError] = useState<string | null>(null);
    const { username } = useAuth();
    const { t } = useTranslation();

    useEffect(() => {
        api.get('/profile/visited')
            .then(res => setVisited(res.data))
            .catch(err => setError(err.message));
    }, []);

    return (
        <div style={{ maxWidth: '1100px', margin: '40px auto', padding: '0 40px' }}>
            <h1 style={{ color: '#3730a3', marginBottom: '8px' }}>{t('profile.title')}</h1>
            <p style={{ color: '#6366f1', marginBottom: '24px' }}>{username}</p>
            <ErrorAlert message={error} onClose={() => setError(null)} />
            <h2 style={{ color: '#3730a3', marginBottom: '16px' }}>
                {t('profile.visitedTitle')} ({visited.length})
            </h2>
            {visited.length === 0 ? (
                <p style={{ color: '#6366f1' }}>{t('profile.noVisited')}</p>
            ) : (
                <table style={{ width: '100%', borderCollapse: 'collapse',
                    background: '#fff', borderRadius: '12px', overflow: 'hidden' }}>
                    <thead>
                    <tr style={{ background: '#eef2ff' }}>
                        <th style={{ padding: '12px 16px', textAlign: 'left', color: '#3730a3' }}>
                            {t('countries.name')}
                        </th>
                        <th style={{ padding: '12px 16px', textAlign: 'left', color: '#3730a3' }}>
                            Date
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {visited.map((v: any, i) => (
                        <tr key={i} style={{ borderBottom: '1px solid #e0e7ff' }}>
                            <td style={{ padding: '12px 16px' }}>{v.countryName}</td>
                            <td style={{ padding: '12px 16px' }}>{v.visitedAt}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}