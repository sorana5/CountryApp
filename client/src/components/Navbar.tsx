import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

export default function Navbar() {
    const { username, role, logout } = useAuth();
    const { t, i18n } = useTranslation();  // ← get i18n from hook, not import
    const navigate = useNavigate();

    const handleLogout = () => { logout(); navigate('/login'); };
    // const toggleLang = () => {
    //     const newLang = i18n.language === 'en' ? 'ro' : 'en';
    //     i18n.changeLanguage(newLang);
    //     localStorage.setItem('language', newLang);
    // };
    // const toggleLang = () => {
    //     const langs = ['en', 'ro', 'fr'];
    //     const current = langs.indexOf(i18n.language);
    //     const newLang = langs[(current + 1) % langs.length];
    //     i18n.changeLanguage(newLang);
    //     localStorage.setItem('language', newLang);
    // };

    // and the button label:
    //const langLabel = ({ en: '🇷🇴 RO', ro: '🇫🇷 FR', fr: '🇬🇧 EN' } as const)[i18n.language as 'en' | 'ro' | 'fr'] || '🇷🇴 RO';

    return (
        <nav style={{
            background: '#3730a3', padding: '14px 40px',
            display: 'flex', justifyContent: 'space-between', alignItems: 'center'
        }}>
            <div style={{ display: 'flex', gap: '24px', alignItems: 'center' }}>
                <span style={{ color: '#fff', fontWeight: 700, fontSize: '18px' }}>
                    CountryApp
                </span>
                <Link to="/countries" style={{ color: '#c7d2fe', textDecoration: 'none' }}>
                    {t('nav.countries')}
                </Link>
                <Link to="/chat" style={{ color: '#c7d2fe', textDecoration: 'none' }}>
                    {t('nav.chat')}
                </Link>
                {role === 'ADMIN' && (
                    <Link to="/admin" style={{ color: '#c7d2fe', textDecoration: 'none' }}>
                        {t('nav.admin')}
                    </Link>
                )}
            </div>
            <div style={{display: 'flex', gap: '16px', alignItems: 'center'}}>
                {/*<button onClick={toggleLang} style={{*/}
                {/*    background: 'transparent', border: '1px solid #818cf8',*/}
                {/*    color: '#c7d2fe', padding: '4px 12px', borderRadius: '6px',*/}
                {/*    cursor: 'pointer', fontSize: '13px'*/}
                {/*}}>*/}
                {/*    {i18n.language === 'en' ? '🇷🇴 RO' : '🇬🇧 EN'}*/}
                {/*</button>*/}
                {/*<button onClick={toggleLang} style={{*/}
                {/*    background: 'transparent', border: '1px solid #818cf8',*/}
                {/*    color: '#c7d2fe', padding: '4px 12px', borderRadius: '6px',*/}
                {/*    cursor: 'pointer', fontSize: '13px'*/}
                {/*}}>*/}
                {/*    {langLabel}*/}
                {/*</button>*/}
                <select
                    value={i18n.language}
                    onChange={e => {
                        i18n.changeLanguage(e.target.value);
                        localStorage.setItem('language', e.target.value);
                    }}
                    style={{
                        background: 'transparent',
                        border: '1px solid #818cf8',
                        color: '#c7d2fe',
                        padding: '4px 8px',
                        borderRadius: '6px',
                        cursor: 'pointer',
                        fontSize: '13px'
                    }}
                >
                    <option value="en" style={{background: '#3730a3'}}>🇬🇧 English</option>
                    <option value="ro" style={{background: '#3730a3'}}>🇷🇴 Română</option>
                    <option value="fr" style={{background: '#3730a3'}}>🇫🇷 Français</option>
                    <option value="de" style={{background: '#3730a3'}}>🇩🇪 Deutsch</option>
                    <option value="hu" style={{background: '#3730a3'}}>🇭🇺 Magyar</option>
                </select>
                {username ? (
                    <>
                        <span style={{color: '#c7d2fe', fontSize: '14px'}}>
                            {username}
                        </span>
                        <Link to="/profile" style={{color: '#c7d2fe', textDecoration: 'none'}}>
                            {t('nav.profile')}
                        </Link>
                        <button onClick={handleLogout} style={{
                            background: 'transparent', border: '1px solid #818cf8',
                            color: '#c7d2fe', padding: '5px 14px', borderRadius: '6px',
                            cursor: 'pointer'
                        }}>
                            {t('nav.logout')}
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login" style={{color: '#c7d2fe', textDecoration: 'none'}}>
                            {t('nav.login')}
                        </Link>
                        <Link to="/register" style={{color: '#c7d2fe', textDecoration: 'none'}}>
                            {t('nav.register')}
                        </Link>
                    </>
                )}
            </div>
        </nav>
    );
}