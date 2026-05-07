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
}