import { test, expect } from '@playwright/test';
import { Common } from '../pages/common';
import { ProportionalCalculator } from '../pages/proportionalCalculator';

let commonPage;
let proportionalCalculatorPage;

test.describe('Empty states in proportional calculator page', () => {

    test.beforeEach(async ({ page }) => {
        commonPage = new Common(page);
        proportionalCalculatorPage = new ProportionalCalculator(page)

        await page.route('**/person', async (route) => {
            await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify([]) });
        });

        await page.route('**/bill', async (route) => {
            await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify([]) });
        });

        await page.route('**/calculation', async (route) => {
            await route.fulfill({
                status: 200, contentType: 'application/json', body: JSON.stringify({
                    totalBills: 0,
                    totalSalary: 0,
                    totalWithReserve: 0,
                    peopleData: []
                })
            });
        });

        await commonPage.loadToPage('/');
        await expect(page.locator('.animate-pulse')).toHaveCount(0);
    })

    test('Should disable generate image button when there is no data', async () => {
        await expect(proportionalCalculatorPage.generateImageButton).toBeVisible();
        await expect(proportionalCalculatorPage.generateImageButton).toBeDisabled();
    });

    test('Should display empty state messages when there are no participants', async ({ page }) => {
        await expect(page.getByText('Adicione participantes para visualizar o rateio.')).toBeVisible();
        await expect(page.getByText('Adicione participantes para visualizar os saldos.')).toBeVisible();

        await expect(page.getByTestId('summary-person-section')).toHaveCount(0);
        await expect(page.getByTestId('summary-remaining-section')).toHaveCount(0);
    });
})