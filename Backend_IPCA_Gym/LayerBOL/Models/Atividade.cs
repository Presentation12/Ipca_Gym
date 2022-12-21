namespace LayerBOL.Models
{
    public class Atividade
    {

        /// <summary>
        /// Id de uma atividade
        /// </summary>
        /// <example>1</example>
        public int id_atividade { get; set; }

        /// <summary>
        /// Id do ginásio onde ocorreu a atividade
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }

        /// <summary>
        /// Id do cliente que realizou a atividade
        /// </summary>
        /// <example>1</example>
        public int id_cliente { get; set; }

        /// <summary>
        /// Data de entrada do cliente no ginásio
        /// </summary>
        /// <example>12-12-2010T12:12:00</example>
        public DateTime data_entrada { get; set; }

        /// <summary>
        /// Data de saída do cliente no ginásio
        /// </summary>
        /// <example>12-12-2010T12:12:00</example>
        public DateTime data_saida { get; set; }
    }
}
