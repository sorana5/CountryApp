import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';
import ErrorAlert from '../components/ErrorAlert';

export default function CountryDetail() {
    const { id } = useParams();
    const [country, setCountry] = useState<any>(null);
    const [visited, setVisited] = useState(false);
    const [factContent, setFactContent] = useState('');
    const [error, setError] = useState<string | null>(null);
    const { username, role } = useAuth();
    const { t } = useTranslation();

    useEffect(() => {
        api.get(`/countries/${id}`)
            .then(res => setCountry(res.data))
            .catch(err => setError(err.message));

        if (username) {
            api.get(`/profile/visited/${id}/check`)
                .then(res => setVisited(res.data.visited))
                .catch(() => {});
        }
    }, [id, username]);

    const markVisited = async () => {
        try {
            await api.post(`/profile/visited/${id}`,
                { countryName: country.name });
            setVisited(true);
        } catch (err: any) {
            setError(err.message);
        }
    };

    const addFunFact = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await api.post(`/countries/${id}/fun-facts`, { content: factContent });
            setFactContent('');
            const res = await api.get(`/countries/${id}`);
            setCountry(res.data);
        } catch (err: any) {
            setError(err.message);
        }
    };

    const deleteFunFact = async (factId: number) => {
        try {
            await api.delete(`/countries/${id}/fun-facts/${factId}`);
            const res = await api.get(`/countries/${id}`);
            setCountry(res.data);
        } catch (err: any) {
            setError(err.message);
        }
    };

    if (!country) return <div style={{ padding: '40px' }}>Loading...</div>;

    return (
        <div style={{ maxWidth: '1100px', margin: '40px auto', padding: '0 40px' }}>
            <Link to="/countries" style={{ color: '#6366f1', textDecoration: 'none',
                fontSize: '14px', display: 'block', marginBottom: '20px' }}>
                ← {t('country.back')}
            </Link>
            <ErrorAlert message={error} onClose={() => setError(null)} />

            <h1 style={{ color: '#3730a3', marginBottom: '24px' }}>{country.name}</h1>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
                gap: '16px', marginBottom: '24px' }}>
                {[
                    [t('country.capital'), country.capital],
                    [t('country.population'), country.population?.toLocaleString()],
                    [t('country.continent'), country.continent],
                ].map(([label, value]) => (
                    <div key={label} style={{ background: '#eef2ff', borderRadius: '8px',
                        padding: '14px 18px' }}>
                        <div style={{ fontSize: '12px', fontWeight: 600, color: '#6366f1',
                            textTransform: 'uppercase', marginBottom: '4px' }}>{label}</div>
                        <div style={{ fontSize: '16px', fontWeight: 600 }}>{value}</div>
                    </div>
                ))}
            </div>

            <p style={{ marginBottom: '24px', color: '#444' }}>{country.description}</p>

            {username && !visited && (
                <button onClick={markVisited} style={{
                    background: '#d1fae5', color: '#065f46', border: 'none',
                    padding: '8px 18px', borderRadius: '8px', cursor: 'pointer',
                    marginBottom: '24px'
                }}>
                    {t('country.markVisited')}
                </button>
            )}
            {visited && (
                <div style={{ background: '#d1fae5', color: '#065f46', padding: '8px 16px',
                    borderRadius: '8px', marginBottom: '24px', display: 'inline-block' }}>
                    ✓ {t('country.visited')}
                </div>
            )}

            {/*{role === 'ADMIN' && (*/}
            {/*    <div style={{ display: 'flex', gap: '8px', marginBottom: '24px' }}>*/}
            {/*        <Link to={`/admin/edit/${id}`} style={{*/}
            {/*            background: '#e0e7ff', color: '#3730a3', padding: '8px 18px',*/}
            {/*            borderRadius: '8px', textDecoration: 'none', fontSize: '14px'*/}
            {/*        }}>*/}
            {/*            {t('country.edit')}*/}
            {/*        </Link>*/}
            {/*    </div>*/}
            {/*)}*/}

            <div style={{ background: '#fff', borderRadius: '12px', padding: '24px',
                boxShadow: '0 1px 4px rgba(99,102,241,0.08)', marginBottom: '24px' }}>
                <h2 style={{ color: '#3730a3', marginBottom: '16px' }}>
                    {t('country.funFacts')}
                </h2>
                {country.funFacts?.length === 0 && (
                    <p style={{ color: '#6366f1' }}>{t('country.noFunFacts')}</p>
                )}
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {country.funFacts?.map((f: any) => (
                        <li key={f.id} style={{ padding: '12px 0',
                            borderBottom: '1px solid #e0e7ff',
                            display: 'flex', justifyContent: 'space-between' }}>
                            <div>
                                <span>{f.content}</span>
                                <em style={{ color: '#6366f1', fontSize: '12px',
                                    marginLeft: '8px' }}>— {f.authorUsername}</em>
                            </div>
                            {role === 'ADMIN' && (
                                <button onClick={() => deleteFunFact(f.id)}
                                        style={{ background: '#fee2e2', color: '#b91c1c',
                                            border: 'none', padding: '4px 10px',
                                            borderRadius: '6px', cursor: 'pointer',
                                            fontSize: '12px' }}>
                                    {t('country.delete')}
                                </button>
                            )}
                        </li>
                    ))}
                </ul>

                {username && (
                    <form onSubmit={addFunFact} style={{ marginTop: '16px' }}>
                        <h3 style={{ color: '#4338ca', marginBottom: '8px' }}>
                            {t('country.addFunFact')}
                        </h3>
                        <textarea value={factContent}
                                  onChange={e => setFactContent(e.target.value)}
                                  rows={3}
                                  style={{ width: '100%', padding: '9px 14px',
                                      border: '1.5px solid #c7d2fe', borderRadius: '8px',
                                      marginBottom: '8px', boxSizing: 'border-box' as const,
                                      fontFamily: 'inherit' }} />
                        <button type="submit" style={{
                            background: '#4338ca', color: '#fff', border: 'none',
                            padding: '8px 18px', borderRadius: '8px', cursor: 'pointer'
                        }}>
                            {t('country.submit')}
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
}