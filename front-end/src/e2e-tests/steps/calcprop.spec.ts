import process from "process";
import { Common } from "../pages/common";
import { ProportionalCalculator } from "../pages/proportionalCalculator";
import { Util } from "../utils/main";
import { expect, test } from "@playwright/test";

let commonPage
let proportionalCalculatorPage
let userData: any;

const util = new Util()

test.describe('Proportional calculator page', () => {
  test.beforeEach(async ({ page }) => {
    commonPage = new Common(page);
    proportionalCalculatorPage = new ProportionalCalculator(page)

    await commonPage.loadToPage('/');
  })

  test('Should load financial summary', async () => {
    await expect(proportionalCalculatorPage.financialSummaryTitle).toBeVisible();
  });

  test('Should add a new participant in people list', async () => {
    userData = await util.generateLead();

    await expect(proportionalCalculatorPage.participantsSectionTitle).toBeVisible();

    await proportionalCalculatorPage.addNewPersonToTheDistribution(userData.name, userData.salary, userData.reserve);

  });

  test.afterEach(async () => {
    if (userData) {
      await proportionalCalculatorPage.removePerson(userData.name);
    }
  });
})