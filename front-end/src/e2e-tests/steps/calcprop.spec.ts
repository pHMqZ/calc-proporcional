import { Common } from "../pages/common";
import { ProportionalCalculator } from "../pages/proportionalCalculator";
import { Util } from "../utils/main";
import { expect, test } from "@playwright/test";

let commonPage
let proportionalCalculatorPage
let userData: any;
let billData: any;

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

    await proportionalCalculatorPage.addNewPersonToTheDistribution(userData.name, userData.salary, userData.reserve);

    await expect(proportionalCalculatorPage.getParticipantCard(userData.name)).toBeVisible();

  });

  test('Should update distribuition percentage when salary is changed', async () => {
    userData = await util.generateLead();

    await proportionalCalculatorPage.addNewPersonToTheDistribution(userData.name, 1000, 0);

    await expect(proportionalCalculatorPage.getDistributionPercentage(userData.name)).toBeVisible();

    const percentageNewPerson = proportionalCalculatorPage.getDistributionPercentage(userData.name)

    const oldPercentagePerson = await percentageNewPerson.innerText();

    await proportionalCalculatorPage.updatePersonSalary(userData.name, 5000);

    await expect(percentageNewPerson).not.toHaveText(oldPercentagePerson);

  });

  test('Should remove a participant and update the list', async () => {
    userData = await util.generateLead();

    await proportionalCalculatorPage.addNewPersonToTheDistribution(userData.name, userData.salary, userData.reserve);

    await expect(proportionalCalculatorPage.getParticipantCard(userData.name)).toBeVisible();

    await proportionalCalculatorPage.removePerson(userData.name);

    await expect(proportionalCalculatorPage.getParticipantCard(userData.name)).toBeHidden();

    userData = null;

  });

  test('Should add a new bill in bills list', async () => {
    billData = await util.generateBill();

    await proportionalCalculatorPage.addNewBillButton.scrollIntoViewIfNeeded();

    await proportionalCalculatorPage.addNewBillToTheDistribution(billData.description, billData.totalAmount);

    await expect(proportionalCalculatorPage.getBillCard(billData.description)).toBeVisible();
  });

  test('Should update summary total when a new bill is added', async () => {
    const newBillAmount = 1500;
    const description = "Moveis";

    billData = { description, totalAmount: newBillAmount }

    const initialTotalBillsAmount = await proportionalCalculatorPage.getTotalBillsAmountValue();

    await proportionalCalculatorPage.addNewBillToTheDistribution(billData.description, billData.totalAmount);

    const expectedTotal = initialTotalBillsAmount + newBillAmount;

    const expectedTotalFormatted = proportionalCalculatorPage.formatCurrency(expectedTotal);

    await expect(proportionalCalculatorPage.totalBillsAmount).toHaveText(expectedTotalFormatted);
  });

  test.afterEach(async () => {
    if (userData) {
      await proportionalCalculatorPage.removePerson(userData.name);

      userData = null;
    }

    if (billData) {
      await proportionalCalculatorPage.removeBill(billData.description);
      billData = null;
    }
  });
})