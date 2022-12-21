
namespace LayerBOL.Models
{
    public class PedidoLoja
    {
        /// <summary>
        /// Id do pedido do produto
        /// </summary>
        /// <example>1</example>
        public int id_pedido { get; set; }
        /// <summary>
        /// Id do produto pedido
        /// </summary>
        /// <example>1</example>
        public int id_produto { get; set; }
        /// <summary>
        /// Quantidade do produto pedido
        /// </summary>
        /// <example>20</example>
        public int quantidade { get; set; }
    }
}
