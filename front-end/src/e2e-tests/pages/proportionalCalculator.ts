import { Locator, Page } from "@playwright/test";

export class ProportionalCalculator {
    readonly page: Page;
    readonly financialSummaryTitle: Locator;
    readonly participantsSectionTitle: Locator;
    readonly addNewParticipantButton: Locator;
    readonly newParticipantTitle: Locator;
    readonly newParticipantNameInput: Locator;
    readonly newParticipantSalaryInput: Locator;
    readonly newParticipantReserveInput: Locator;
    readonly saveNewParticipantButton: Locator;


    constructor(page: Page) {
        this.page = page;
        this.financialSummaryTitle = page.getByTestId('summary-title');
        this.participantsSectionTitle = page.getByTestId('people-list-title');
        this.addNewParticipantButton = page.getByTestId('btn-add-person-open');
        this.newParticipantTitle = page.getByTestId('new-participant-title');
        this.newParticipantNameInput = page.getByTestId('input-person-name');
        this.newParticipantSalaryInput = page.getByTestId('input-person-salary');
        this.newParticipantReserveInput = page.getByTestId('input-person-reserve');
        this.saveNewParticipantButton = page.getByTestId('btn-save-person');
    }

    getParticipantCard(name: string): Locator {
        return this.page.getByTestId(`person-card-list-${name}`);
    }

    getRemovePersonButton(name: string): Locator {
        return this.page.getByTestId(`btn-remove-person-${name}`);
    }

    async addNewPersonToTheDistribution(name: string, salary: number, reserve: number) {
        await this.addNewParticipantButton.click();
        await this.newParticipantNameInput.fill(name);
        await this.newParticipantSalaryInput.fill(salary.toString());
        await this.newParticipantReserveInput.fill(reserve.toString());
        await this.saveNewParticipantButton.click();
    }

    async removePerson(name: string) {
        const personCard = this.getParticipantCard(name);

        await personCard.scrollIntoViewIfNeeded();

        await this.getRemovePersonButton(name).click();
    }
}