namespace LayerBOL.Models
{
    public class Marcacao
    {
        /// <summary>
        /// Id de marcação de uma consulta
        /// </summary>
        /// <example>1</example>
        public int id_marcacao { get; set; }
        /// <summary>
        /// Id do funcionario que realizará a consulta
        /// </summary>
        /// <example>1</example>
        public int id_funcionario { get; set; }
        /// <summary>
        /// Id do cliente que realizou a marcação
        /// </summary>
        /// <example>1</example>
        public int id_cliente { get; set; }
        /// <summary>
        /// Data da marcação da consulta
        /// </summary>
        /// <example>12-12-2010</example>
        public DateTime data_marcacao { get; set; }
        /// <summary>
        /// Descrição da marcação
        /// </summary>
        /// <example>Consulta Nutricional</example>
        public string descricao { get; set; }
        /// <summary>
        /// Estado da marcação (Ativo ou Inativo)
        /// </summary>
        /// <example>Ativo</example>
        public string estado { get; set; }
    }
}
