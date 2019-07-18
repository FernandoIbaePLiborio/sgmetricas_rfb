export class TreatDate {

    public static getDateAsString = (data: Date): string => {
        var ano: string = data.getFullYear() + "";
        var mes: string = TreatDate.completaZeroEsquerda((data.getMonth() + 1) + "", 2);
        var dia: string = TreatDate.completaZeroEsquerda(data.getDate() + "", 2);
        return ano+mes+dia;
    }

    private static completaZeroEsquerda = (numero: string, tamanho: number): string => {
        var novo: string = numero;
        while (novo.length < tamanho) {
            novo = "0" + numero;
        };
        return novo;
    }

    constructor(){

    }
}