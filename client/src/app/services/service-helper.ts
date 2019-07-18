import { ConsultaFiltro } from "../filtro/consulta.filtro";

export class ServiceHelper {
    
    static createCommonUrlSearchParams = (filtro: ConsultaFiltro): string[] => {
        
        let result: string[] = [];

        let inicioRange = filtro.offset;
        let fimRange = (filtro.offset + filtro.limit - 1);
        result.push("range=" + inicioRange + "-" + fimRange);

        if(filtro.sortField) {
            result.push("sort=" + filtro.sortField + ",id");
        } else {
            result.push("sort=id");
        }

        if(filtro.desc) {
            if(filtro.sortField) {
                result.push("desc=" + filtro.sortField + ",id");
            } else {
                result.push("desc=id");
            }
        } 

        return result;
    }
}