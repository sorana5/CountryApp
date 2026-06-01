import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import en from './en.json';
import ro from './ro.json';
import fr from './fr.json';
import de from './de.json';
import hu from './hu.json';

i18n.use(initReactI18next).init({
    resources: {
        en: { translation: en },
        ro: { translation: ro },
        fr: { translation: fr },
        de: { translation: de },
        hu: { translation: hu },
    },
    lng: localStorage.getItem('language') || 'en',
    fallbackLng: 'en',
    interpolation: { escapeValue: false },
});

export default i18n;