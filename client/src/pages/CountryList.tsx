import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import api from '../api/axios';
import ErrorAlert from '../components/ErrorAlert';

export default function CountryList() {
    const [countries, setCountries] = useState([]);
    const [name, setName] = useState('');
    const [continent, setContinent] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [sort, setSort] = useState('name');
    const [dir, setDir] = useState('asc');
    const [error, setError] = useState<string | null>(null);
    const { t } = useTranslation();

    const fetchCountries = async () => {
        try {
            const res = await api.get('/countries', {
                params: { name, continent, page, sort, dir }
            });
            setCountries(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (err: any) {
            setError(err.message);
        }
    };

    useEffect(() => { fetchCountries(); }, [page, sort, dir]);

    const toggleSort = (col: string) => {
        if (sort === col) setDir(d => d === 'asc' ? 'desc' : 'asc');
        else { setSort(col); setDir('asc'); }
    };

    const exportData = (format: string) => {
        window.open(
            `http://localhost:8080/api/countries/export?format=${format}&name=${name}&continent=${continent}&sort=${sort}&dir=${dir}`
        );
    };

    return (
        <div style={{ maxWidth: '1100px', margin: '40px auto', padding: '0 40px' }}>
            <h1 style={{ color: '#3730a3', marginBottom: '24px' }}>{t('countries.title')}</h1>
            <ErrorAlert message={error} onClose={() => setError(null)} />

            <div style={{ display: 'flex', gap: '12px', marginBottom: '16px', flexWrap: 'wrap' }}>
                <input placeholder={t('countries.search')} value={name}
                       onChange={e => setName(e.target.value)}
                       style={{ padding: '9px 14px', border: '1.5px solid #c7d2fe',
                           borderRadius: '8px', width: '220px' }} />
                <input placeholder={t('countries.filter')} value={continent}
                       onChange={e => setContinent(e.target.value)}
                       style={{ padding: '9px 14px', border: '1.5px solid #c7d2fe',
                           borderRadius: '8px', width: '220px' }} />
                <button onClick={() => { setPage(0); fetchCountries(); }}
                        style={{ background: '#4338ca', color: '#fff', border: 'none',
                            padding: '9px 18px', borderRadius: '8px', cursor: 'pointer' }}>
                    {t('countries.searchBtn')}
                </button>
                <button onClick={() => { setName(''); setContinent(''); setPage(0); }}
                        style={{ background: '#e0e7ff', color: '#3730a3', border: 'none',
                            padding: '9px 18px', borderRadius: '8px', cursor: 'pointer' }}>
                    {t('countries.clear')}
                </button>
            </div>

            <div style={{ display: 'flex', gap: '8px', marginBottom: '16px' }}>
                {['json', 'xml', 'csv'].map(fmt => (
                    <button key={fmt} onClick={() => exportData(fmt)}
                            style={{ background: '#e0e7ff', color: '#3730a3', border: 'none',
                                padding: '6px 14px', borderRadius: '8px', cursor: 'pointer',
                                fontSize: '13px' }}>
                        {t('countries.export')} {fmt.toUpperCase()}
                    </button>
                ))}
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse',
                background: '#fff', borderRadius: '12px', overflow: 'hidden' }}>
                <thead>
                <tr style={{ background: '#eef2ff' }}>
                    {['name', 'continent'].map(col => (
                        <th key={col} onClick={() => toggleSort(col)}
                            style={{ padding: '12px 16px', textAlign: 'left',
                                cursor: 'pointer', color: '#3730a3', fontWeight: 600 }}>
                            {t(`countries.${col}`)}
                            {sort === col ? (dir === 'asc' ? ' ↑' : ' ↓') : ''}
                        </th>
                    ))}
                    <th style={{ padding: '12px 16px', textAlign: 'left', color: '#3730a3' }}>
                        {t('countries.capital')}
                    </th>
                    <th style={{ padding: '12px 16px', textAlign: 'left', color: '#3730a3' }}>
                        {t('countries.population')}
                    </th>
                </tr>
                </thead>
                <tbody>
                {countries.map((c: any) => (
                    <tr key={c.id} style={{ borderBottom: '1px solid #e0e7ff' }}>
                        <td style={{ padding: '12px 16px' }}>
                            <Link to={`/countries/${c.id}`}
                                  style={{ color: '#4338ca', textDecoration: 'none', fontWeight: 500 }}>
                                {c.name}
                            </Link>
                        </td>
                        <td style={{ padding: '12px 16px' }}>{c.continent}</td>
                        <td style={{ padding: '12px 16px' }}>{c.capital}</td>
                        <td style={{ padding: '12px 16px' }}>{c.population?.toLocaleString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div style={{ display: 'flex', gap: '8px', marginTop: '16px' }}>
                {page > 0 && (
                    <button onClick={() => setPage(p => p - 1)}
                            style={{ background: '#fff', border: '1.5px solid #c7d2fe',
                                color: '#4338ca', padding: '6px 14px', borderRadius: '6px',
                                cursor: 'pointer' }}>
                        {t('countries.previous')}
                    </button>
                )}
                {Array.from({ length: totalPages }, (_, i) => (
                    <button key={i} onClick={() => setPage(i)}
                            style={{ background: i === page ? '#4338ca' : '#fff',
                                color: i === page ? '#fff' : '#4338ca',
                                border: '1.5px solid #c7d2fe', padding: '6px 12px',
                                borderRadius: '6px', cursor: 'pointer' }}>
                        {i + 1}
                    </button>
                ))}
                {page < totalPages - 1 && (
                    <button onClick={() => setPage(p => p + 1)}
                            style={{ background: '#fff', border: '1.5px solid #c7d2fe',
                                color: '#4338ca', padding: '6px 14px', borderRadius: '6px',
                                cursor: 'pointer' }}>
                        {t('countries.next')}
                    </button>
                )}
            </div>
        </div>
    );
}