/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useEffect, useState, type ReactNode } from "react";


type Theme = 'light' | 'dark';

interface ThemeContext {
    theme: Theme;
    toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContext | undefined>(undefined);

export const useTheme = () => {
    const context = useContext(ThemeContext);

    if(!context) {
        throw new Error('useTheme must be used within a ThemeProvider');
    }

    return context;
};

interface ThemeProviderProps {
    children: ReactNode;
}

export const ThemeProvider: React.FC<ThemeProviderProps> = ({ children }) => {
    const [theme, setTheme] = useState<Theme>(() => {
        const savedTheme = localStorage.getItem('theme') as Theme;
        return savedTheme || 'light';
    });

    useEffect(() => {
        localStorage.setItem('theme', theme);

        if (theme === 'dark') {
            document.documentElement.classList.add('dark');
        } else {
            document.documentElement.classList.remove('dark');
        }
    }, [theme]);
    
    const toggleTheme = () => {
        setTheme((prev) => (prev === 'light' ? 'dark' : 'light' ));
    };

    return(
        <ThemeContext.Provider value={{ theme, toggleTheme}}>
            {children}
        </ThemeContext.Provider>
    );
};