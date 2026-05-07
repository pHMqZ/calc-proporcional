declare module 'pure-gen' {
    interface Pure {
        setLocale(locale: string): void;
        name: {
            findName(): string;
        };
        random: {
            number(options?: { min?: number; max?: number }): number;
        };
    }
    const pure: Pure;
    export default pure;
}
