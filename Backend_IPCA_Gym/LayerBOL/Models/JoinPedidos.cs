using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LayerBOL.Models
{
    public class JoinPedido
    {
        #region Pedido
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
        #endregion

        #region PedidoLoja
        /// <summary>
        /// Quantidade do produto pedido
        /// </summary>
        /// <example>20</example>
        public int quantidade_pedido { get; set; }
        #endregion

        #region Loja (ou produto)
        /// <summary>
        /// Id do produto da loja
        /// </summary>
        /// <example>1</example>
        public int id_produto { get; set; }
        /// <summary>
        /// Id do ginásio a quem pertence o produto
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Nome do produto
        /// </summary>
        /// <example>T-shirt Azul</example>
        public string nome_produto { get; set; }
        /// <summary>
        /// Tipo de produto
        /// </summary>
        /// <example>Roupa</example>
        public string tipo_produto { get; set; }
        /// <summary>
        /// Preço do produto
        /// </summary>
        /// <example>1.99</example>
        public double preco_produto { get; set; }
        /// <summary>
        /// Descrição do produto
        /// </summary>
        /// <example>M</example>
        public string descricao_produto { get; set; }
        /// <summary>
        /// Estado do produto
        /// </summary>
        /// <example>Ativo</example>
        public string estado_produto { get; set; }
        /// <summary>
        /// Foto do produto que pode ser nulo
        /// </summary>
        /// <example>C:\OneDrive\tshirt.png</example>
        public string? foto_produto { get; set; }
        /// <summary>
        /// Quantidade do produto
        /// </summary>
        /// <example>100</example>
        public int quantidade_produto { get; set; }
        #endregion
    }
}
