namespace LayerBOL.Models
{
    public class Pedido
    {
        /// <summary>
        /// Id do pedido de um produto
        /// </summary>
        /// <example>1</example>
        public int id_pedido { get; set; }
        /// <summary>
        /// Id do cliente que realizou o pedido
        /// </summary>
        /// <example>1</example>
        public int id_cliente { get; set; }
        /// <summary>
        /// Data do pedido
        /// </summary>
        /// <example>12-12-2010</example>
        public DateTime data_pedido { get; set; }
        /// <summary>
        /// Estado do pedido (Ativo ou Inativo)
        /// </summary>
        /// <example>Ativo</example>
        public string estado_pedido { get; set; }
    }
}
