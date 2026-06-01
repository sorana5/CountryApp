import React, { createContext, useContext, useState } from 'react';

interface AuthContextType {
    token: string | null;
    username: string | null;
    role: string | null;
    login: (token: string, username: string, role: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [token, setToken] = useState(localStorage.getItem('token'));
    const [username, setUsername] = useState(localStorage.getItem('username'));
    const [role, setRole] = useState(localStorage.getItem('role'));

    const login = (t: string, u: string, r: string) => {
        localStorage.setItem('token', t);
        localStorage.setItem('username', u);
        localStorage.setItem('role', r);
        setToken(t); setUsername(u); setRole(r);
    };

    const logout = () => {
        //localStorage.clear();
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('role');
        setToken(null); setUsername(null); setRole(null);
    };

    return (
        <AuthContext.Provider value={{ token, username, role, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);