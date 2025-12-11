export interface Contacto {
  codigoCon: number;
  cliente: string;
  codcli: string;
  nombreCon: string;
  telefonoCon: string;
  movilCon: string;
  emailCon: string;
  observCon: string;
  activoCon: boolean;
  principalCon: boolean;
  usuarioCon: string;
  agenteCon: string;
  cifCon: string;
  fechaMod: string | null; // ISO date string from backend
  idContactoERP: number | null;
}
