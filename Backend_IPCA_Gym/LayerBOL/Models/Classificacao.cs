namespace LayerBOL.Models
{
    public class Classificacao
    {
        /// <summary>
        /// Id da avaliacao
        /// </summary>
        /// <example>1</example>
        public int id_avaliacao { get; set; }
        /// <summary>
        /// Id do ginásio a ser avaliado
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Id do cliente que está a avaliar
        /// </summary>
        /// <example>1</example>
        public int id_cliente { get; set; }
        /// <summary>
        /// Avaliação quantitativa
        /// </summary>
        /// <example>3</example>
        public int avaliacao { get; set; }
        /// <summary>
        /// Comentário textual realizada pelo cliente
        /// </summary>
        /// <example>Razovel</example>
        public string comentario { get; set; }
        /// <summary>
        /// Data de inserção da avaliação
        /// </summary>
        /// <example>12-12-2010</example>
        public DateTime data_avaliacao { get; set; }
    }
}
