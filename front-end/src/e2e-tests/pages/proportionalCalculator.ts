import { Locator, Page, expect } from "@playwright/test";

export class ProportionalCalculator {
    readonly page: Page;
    readonly financialSummaryTitle: Locator;
    readonly totalBillsAmount: Locator;

    readonly participantsSectionTitle: Locator;
    readonly addNewParticipantButton: Locator;
    readonly newParticipantTitle: Locator;
    readonly newParticipantNameInput: Locator;
    readonly newParticipantSalaryInput: Locator;
    readonly newParticipantReserveInput: Locator;
    readonly saveNewParticipantButton: Locator;

    readonly billsListTitle: Locator;
    readonly addNewBillButton: Locator;

    readonly distributionBillsTitle: Locator;

    readonly generateImageButton: Locator;

    constructor(page: Page) {
        this.page = page;
        this.financialSummaryTitle = page.getByTestId('summary-title');
        this.totalBillsAmount = page.getByTestId('summary-total-bills-amount');

        this.participantsSectionTitle = page.getByTestId('people-list-title');
        this.addNewParticipantButton = page.getByTestId('btn-add-person-open');
        this.newParticipantTitle = page.getByTestId('new-participant-title');
        this.newParticipantNameInput = page.getByTestId('input-person-name');
        this.newParticipantSalaryInput = page.getByTestId('input-person-salary');
        this.newParticipantReserveInput = page.getByTestId('input-person-reserve');
        this.saveNewParticipantButton = page.getByTestId('btn-save-person');

        this.billsListTitle = page.getByTestId('bills-list-title');
        this.addNewBillButton = page.getByTestId('btn-add-bill');

        this.distributionBillsTitle = page.getByTestId('distribution-bills-title');

        this.generateImageButton = page.getByTestId('generate-image-button');
    }

    getParticipantCard(name: string): Locator {
        return this.page.getByTestId(`person-card-list-${name}`);
    }

    getBillCard(description: string): Locator {
        return this.page.getByTestId(`input-bill-description-${description}`);
    }


    getInputBillDescription(description: string): Locator {
        return this.page.getByTestId(`input-bill-description-${description}`);
    }

    getInputBillAmount(description: string): Locator {
        return this.page.getByTestId(`input-bill-amount-${description}`);
    }

    getInputPersonSalary(name: string): Locator {
        return this.page.getByTestId(`input-card-person-salary-${name}`);
    }

    getInputPersonReserve(name: string): Locator {
        return this.page.getByTestId(`input-card-person-reserve-${name}`);
    }

    getDistributionPercentage(name: string): Locator {
        return this.page.getByTestId(`distribution-bill-percentage-${name}`);
    }

    getRemovePersonButton(name: string): Locator {
        return this.page.getByTestId(`btn-remove-person-${name}`);
    }

    getRemoveBillButton(description: string): Locator {
        return this.page.getByTestId(`btn-remove-bill-${description}`);
    }

    async addNewPersonToTheDistribution(name: string, salary: number, reserve: number) {
        await this.addNewParticipantButton.scrollIntoViewIfNeeded();
        await this.addNewParticipantButton.click();

        await this.newParticipantNameInput.fill(name);
        await this.newParticipantSalaryInput.fill((salary * 100).toString());
        await this.newParticipantReserveInput.fill(reserve.toString());
        await this.saveNewParticipantButton.click();

        await expect(this.newParticipantTitle).toBeHidden();
    }


    async updatePersonSalary(name: string, salary: number) {
        const personCard = this.getParticipantCard(name);

        await personCard.scrollIntoViewIfNeeded();

        const salaryInput = this.getInputPersonSalary(name);
        await salaryInput.fill((salary * 100).toString());
        await salaryInput.blur();
    }

    async addNewBillToTheDistribution(description: string, amount: number) {
        await this.addNewBillButton.scrollIntoViewIfNeeded();
        
        const initialCount = await this.page.locator('[data-testid^="input-bill-description"]:visible').count();
        
        await this.addNewBillButton.click();

        await expect(this.page.locator('[data-testid^="input-bill-description"]:visible')).toHaveCount(initialCount + 1);

        const lastDesc = this.page.locator('[data-testid="input-bill-description-Nova Conta"]:visible').last();
        await lastDesc.fill(description);
        await lastDesc.blur();

        const lastAmount = this.page.locator(`[data-testid="input-bill-amount-${description}"]:visible`).last();
        await expect(lastAmount).toBeVisible();

        await lastAmount.fill((amount * 100).toString());
        await lastAmount.blur();
    }

    async getTotalBillsAmountValue(): Promise<number> {
        const amount = await this.totalBillsAmount.innerText();
        return this.parseCurrency(amount);
    }

    async removePerson(name: string) {
        const personCard = this.getParticipantCard(name);

        await personCard.scrollIntoViewIfNeeded();

        await this.getRemovePersonButton(name).click();
    }

    async removeBill(description: string) {
        const billCard = this.getInputBillDescription(description);

        await billCard.scrollIntoViewIfNeeded();

        await this.getRemoveBillButton(description).click();
    }

    private parseCurrency(amount: string): number {
        return Number(amount.replace(/[^\d,]/g, '').replace(',', '.'));
    }

    formatCurrency(amount: number): string {
        return amount.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });
    }
}