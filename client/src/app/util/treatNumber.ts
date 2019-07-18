export class TreatNumber {

    public static format = (numero: number): number => {
        var texto: string = numero + "";
        texto = texto.replace(".","").replace(",",".");
        return Number.parseFloat(texto);
    }
}