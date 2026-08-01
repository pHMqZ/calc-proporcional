
import { Common } from "../pages/common";
import { ProportionalCalculator } from "../pages/proportionalCalculator";
import { Util } from "../utils/main";
import { expect, test } from "@playwright/test";

const util = new Util();

test.describe('Session Persistence via localStorage', () => {

  test('Should retain session UUID and data after browser context reload', async ({ page, context, browser }) => {
    // Inicialização usando o contexto padrão fornecido pelo Playwright
    const commonPage1 = new Common(page);
    const proportionalCalculatorPage1 = new ProportionalCalculator(page);

    await commonPage1.loadToPage('/');
    await expect(proportionalCalculatorPage1.financialSummaryTitle).toBeVisible();

    // Adiciona uma nova pessoa para forçar o frontend a salvar o UUID e criar dados no backend
    const userData = await util.generateLead();
    await proportionalCalculatorPage1.addNewPersonToTheDistribution(userData.name, userData.salary, userData.reserve);
    
    // Verifica se a pessoa foi adicionada no frontend
    await expect(proportionalCalculatorPage1.getParticipantCard(userData.name)).toBeVisible();

    // Salva o estado do storage atual
    const storage = await context.storageState();
    
    // Abre um novo contexto injetando o storage persistido, simulando o retorno do usuário em outra aba/sessão
    const context2 = await browser.newContext({ storageState: storage });
    const page2 = await context2.newPage();
    const commonPage2 = new Common(page2);
    const proportionalCalculatorPage2 = new ProportionalCalculator(page2);

    // Carrega a página novamente
    await commonPage2.loadToPage('/');
    await expect(proportionalCalculatorPage2.financialSummaryTitle).toBeVisible();
    
    // Validação: A pessoa deve estar visível, pois o X-Client-Id-Data persistiu no localStorage
    // e o backend retornou os dados vinculados a esse UUID na chamada inicial da página.
    await expect(proportionalCalculatorPage2.getParticipantCard(userData.name)).toBeVisible();

    // Cleanup: Remove a pessoa para manter o banco limpo
    await proportionalCalculatorPage2.removePerson(userData.name);
    await expect(proportionalCalculatorPage2.getParticipantCard(userData.name)).toBeHidden();

    await context2.close();
  });
});
