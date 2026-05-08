declare module 'pure-gen' {
    interface Pure {
        setLocale(locale: string): void;
        name: {
            findName(): string;
        };
        random: {
            number(options?: { min?: number; max?: number }): number;
            arrayElement<T>(arr: T[]): T;
        };
    }
    const pure: Pure;
    export default pure;
}
