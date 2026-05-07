import { Page } from "@playwright/test";

export class Common {
    readonly page: Page;

    constructor(page: Page) {
        this.page = page;
    }

    async loadToPage(path: string) {
        await this.page.goto(path);
        await this.page.waitForLoadState('networkidle');
    }
}