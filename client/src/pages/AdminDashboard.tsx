import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import ErrorAlert from '../components/ErrorAlert';

export default function AdminDashboard() {
    const [countries, setCountries] = useState<any[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [form, setForm] = useState({
        name: '', capital: '', population: '', continent: '', description: ''
    });
    const [editId, setEditId] = useState<number | null>(null);
    const { t } = useTranslation();
    const navigate = useNavigate();

    const fetchAll = () => {
        api.get('/countries', { params: { size: 1000 } })
            .then(res => setCountries(res.data.content))
            .catch(err => setError(err.message));
    };

    useEffect(() => { fetchAll(); }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const payload = { ...form, population: Number(form.population) };
        try {
            if (editId) {
                await api.put(`/admin/countries/${editId}`, payload);
            } else {
                await api.post('/admin/countries', payload);
            }
            setForm({ name: '', capital: '', population: '',
                continent: '', description: '' });
            setEditId(null);
            fetchAll();
        } catch (err: any) {
            setError(err.message);
        }
    };

    const handleEdit = (c: any) => {
        setForm({ name: c.name, capital: c.capital,
            population: String(c.population), continent: c.continent,
            description: c.description || '' });
        setEditId(c.id);
    };

    const handleDelete = async (id: number) => {
        if (!window.confirm('Delete this country?')) return;
        try {
            await api.delete(`/admin/countries/${id}`);
            fetchAll();
        } catch (err: any) {
            setError(err.message);
        }
    };

    const inputStyle = {
        width: '100%', padding: '9px 14px', marginBottom: '10px',
        border: '1.5px solid #c7d2fe', borderRadius: '8px',
        boxSizing: 'border-box' as const
    };

    return (
        <div style={{ maxWidth: '1100px', margin: '40px auto', padding: '0 40px' }}>
            <h1 style={{ color: '#3730a3', marginBottom: '24px' }}>{t('admin.title')}</h1>
            <ErrorAlert message={error} onClose={() => setError(null)} />

            <div style={{ background: '#fff', borderRadius: '12px', padding: '24px',
                marginBottom: '32px', boxShadow: '0 1px 4px rgba(99,102,241,0.08)' }}>
                <h2 style={{ color: '#3730a3', marginBottom: '16px' }}>
                    {editId ? 'Edit Country' : t('admin.addCountry')}
                </h2>
                <form onSubmit={handleSubmit}>
                    {(['name', 'capital', 'continent'] as const).map(field => (
                        <input key={field} placeholder={field}
                               value={form[field]}
                               onChange={e => setForm(f => ({ ...f, [field]: e.target.value }))}
                               style={inputStyle} />
                    ))}
                    <input type="number" placeholder="population"
                           value={form.population}
                           onChange={e => setForm(f => ({ ...f, population: e.target.value }))}
                           style={inputStyle} />
                    <textarea placeholder="description" value={form.description}
                              onChange={e => setForm(f => ({ ...f, description: e.target.value }))}
                              rows={3}
                              style={{ ...inputStyle, fontFamily: 'inherit' }} />
                    <div style={{ display: 'flex', gap: '8px' }}>
                        <button type="submit" style={{
                            background: '#4338ca', color: '#fff', border: 'none',
                            padding: '9px 20px', borderRadius: '8px', cursor: 'pointer'
                        }}>
                            {editId ? 'Update' : 'Save'}
                        </button>
                        {editId && (
                            <button type="button"
                                    onClick={() => { setEditId(null);
                                        setForm({ name: '', capital: '', population: '',
                                            continent: '', description: '' }); }}
                                    style={{ background: '#e0e7ff', color: '#3730a3',
                                        border: 'none', padding: '9px 20px',
                                        borderRadius: '8px', cursor: 'pointer' }}>
                                Cancel
                            </button>
                        )}
                    </div>
                </form>
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse',
                background: '#fff', borderRadius: '12px', overflow: 'hidden' }}>
                <thead>
                <tr style={{ background: '#eef2ff' }}>
                    {['Name', 'Capital', 'Continent', 'Population',
                        t('admin.actions')].map(h => (
                        <th key={h} style={{ padding: '12px 16px',
                            textAlign: 'left', color: '#3730a3' }}>{h}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {countries.map((c: any) => (
                    <tr key={c.id} style={{ borderBottom: '1px solid #e0e7ff' }}>
                        <td style={{ padding: '12px 16px' }}>{c.name}</td>
                        <td style={{ padding: '12px 16px' }}>{c.capital}</td>
                        <td style={{ padding: '12px 16px' }}>{c.continent}</td>
                        <td style={{ padding: '12px 16px' }}>
                            {c.population?.toLocaleString()}
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                            <div style={{ display: 'flex', gap: '8px' }}>
                                <button onClick={() => handleEdit(c)}
                                        style={{ background: '#e0e7ff', color: '#3730a3',
                                            border: 'none', padding: '5px 12px',
                                            borderRadius: '6px', cursor: 'pointer',
                                            fontSize: '13px' }}>
                                    {t('country.edit')}
                                </button>
                                <button onClick={() => handleDelete(c.id)}
                                        style={{ background: '#fee2e2', color: '#b91c1c',
                                            border: 'none', padding: '5px 12px',
                                            borderRadius: '6px', cursor: 'pointer',
                                            fontSize: '13px' }}>
                                    {t('country.delete')}
                                </button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}