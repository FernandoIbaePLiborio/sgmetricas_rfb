export class ErrorMessage {
    
    public error_description: string;
    public error: string;
    
    static descricao (error_description: string): string {
        if(error_description == "Unhandled server exception"){
            return "Erro";
        }

        return error_description;
    }
}