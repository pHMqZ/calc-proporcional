import pure from 'pure-gen'
pure.setLocale('pt_BR')


export class Util {
    async generateLead() {
        const data = {
            name: pure.name.findName(),
            salary: pure.random.number({ min: 1500, max: 6500 }),
            reserve: pure.random.number({ min: 1, max: 100 })
        }

        return data
    }

    async generateBill() {
        const data = {
            description: pure.random.arrayElement(['Água', 'Mercado', 'Transporte', 'Gatos', 'Remédios', 'Outros']),
            totalAmount: pure.random.number({ min: 100, max: 5000 })
        }

        return data;
    }
}